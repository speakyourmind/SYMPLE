/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.log4j.Logger;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import org.symfound.controls.system.EditAppButton;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.dialog.EditDialog;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.AnimatedPane;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.controls.system.grid.editor.DeleteKeyButton;
import org.symfound.controls.system.grid.editor.ReplaceKeyButton;
import static org.symfound.controls.user.ButtonGrid.BUTTON_DELIMITER;
import static org.symfound.controls.user.ButtonGrid.KEY_DELIMITER;
import org.symfound.main.FullSession;
import static org.symfound.main.FullSession.getMainUI;
import org.symfound.tools.selection.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public abstract class AppableControl extends ConfirmableControl {

    private static final String NAME = AppableControl.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private String defaultTitle = "";
    private String initKey = "";

    /**
     *
     * @param CSSClass
     * @param key
     * @param title
     * @param index
     */
    public AppableControl(String CSSClass, String key, String title, String index) {
        super(CSSClass);
        defaultTitle = title;
        this.initIndex = index;
        this.initKey = key;
        initialize();
    }

    private void initialize() {
        setConfirmable(false);
        configurePrimaryDisabled();
        configureDrag();
        configButtons();

    }

    public static Integer SOURCE_ROW_INDEX = 0;
    public static Integer SOURCE_COLUMN_INDEX = 0;
    public static Integer SOURCE_ROW_SPAN = 0;
    public static Integer SOURCE_COLUMN_SPAN = 0;
    public static Integer GRID_LOCATION = 0;

    private void configureDrag() {
        setOnDragDetected((MouseEvent event) -> {
            /* drag was detected, start drag-and-drop gesture*/


 /* allow any transfer mode */
            Dragboard db = startDragAndDrop(TransferMode.ANY);

            /* put a string on dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(GridPane.getRowIndex(this))
                    + "," + String.valueOf(GridPane.getColumnIndex(this))
                    + "," + String.valueOf(GridPane.getRowSpan(this))
                    + "," + String.valueOf(GridPane.getColumnSpan(this))
                    + "," + String.valueOf(this.getGridLocation()));
            db.setContent(content);
            LOGGER.info("Drag start detected for button " + this.getText() + " with data " + content.getString());
            event.consume();
        });
        setOnDragDone((DragEvent event) -> {
            /* the drag-and-drop gesture ended */
            LOGGER.info("onDragDone");
            /* if the data was successfully moved, clear it */
            if (event.getTransferMode().equals(TransferMode.MOVE)) {
                //   setText("Moved over");
                GridPane.setRowIndex(this, SOURCE_ROW_INDEX);
                GridPane.setColumnIndex(this, SOURCE_COLUMN_INDEX);
                GridPane.setRowSpan(this, SOURCE_ROW_SPAN);
                GridPane.setColumnSpan(this, SOURCE_COLUMN_SPAN);
                setGridLocation(GRID_LOCATION);

                updateGridOrder();
            }

            event.consume();
        });
        setOnDragOver((DragEvent event) -> {
            /* data is dragged over the source */
            //     LOGGER.info("onDragOver");

            /* accept it only if it is  not dragged from the same node
            * and if it has a string data */
            if (event.getGestureSource() != this
                    && event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        });

        setOnDragEntered((DragEvent event) -> {
            /* the drag-and-drop gesture entered the source */
            //     LOGGER.info("onDragEntered");
            /* show to the user that it is an actual gesture source */
            if (event.getGestureSource() != this
                    && event.getDragboard().hasString()) {
                //setText("Drag Entered");

            }
            event.consume();
        });

        setOnDragExited((DragEvent event) -> {
            /* mouse moved away, remove the graphical cues */
            //  setText("Drag Exited");

            event.consume();
        });
        setOnDragDropped((DragEvent event) -> {
            /* data dropped */
 /* if there is a string data on dragboard, read it and use it */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                SOURCE_COLUMN_INDEX = GridPane.getColumnIndex(this);
                SOURCE_ROW_INDEX = GridPane.getRowIndex(this);
                SOURCE_COLUMN_SPAN = GridPane.getColumnSpan(this);
                SOURCE_ROW_SPAN = GridPane.getRowSpan(this);
                GRID_LOCATION = this.getGridLocation();
                LOGGER.info("onDragDropped " + db.getString());

                final String[] split = db.getString().split(",");

                GridPane.setRowIndex(this, Integer.valueOf(split[0]));
                GridPane.setColumnIndex(this, Integer.valueOf(split[1]));
                GridPane.setRowSpan(this, Integer.valueOf(split[2]));
                GridPane.setColumnSpan(this, Integer.valueOf(split[3]));
                setGridLocation(Integer.valueOf(split[4]));

                updateGridOrder();
                //    GridPane.setColumnIndex(target, sourceIndex);
                success = true;
            }
            /* let the target know whether the string was successfully
            * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });
    }

    private void updateGridOrder() {
        final Parent parent = this.getParent();
        ConfigurableGrid configurableGrid;
        if (parent instanceof ConfigurableGrid) {
            configurableGrid = (ConfigurableGrid) parent;
            final ParallelList<String, String> order1 = configurableGrid.getOrder();
            order1.getFirstList().set(getGridLocation(), getKey());
            order1.getSecondList().set(getGridLocation(), getIndex());
            configurableGrid.setOrder(order1);
            configurableGrid.getGridManager().setOrder(configurableGrid.getOrder());
        }
    }

    public void configurePrimaryDisabled() {
        getPrimaryControl().setDisable(disablePrimary());
        disablePrimaryProperty().addListener((observable, oldValue, newValue) -> {
            getPrimaryControl().setDisable(newValue);
        });
    }

    public void configButtons() {
        boolean isSettingsControl = getControlType().equals(ControlType.SETTING_CONTROL);
    

        ConfigurableGrid.editModeProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1 && !isSettingsControl && isEditable()) {
                addConfigButtons();
            } else {
                removeConfigButtons();
            }
        });
    }

    public void configureStyle() {

        configureFont();

        final StringExpression concat = Bindings.concat("-fx-background-color:-fx-", backgroundColourProperty().asString(), "; \n",
                "-fx-text-fill:-fx-", textColourProperty().asString(), "; \n",
                "-fx-background-image:url(\"", backgroundURLProperty(), "\"); "
                + "-fx-background-repeat:no-repeat;\n"
                + "-fx-background-position:center;\n",
                overrideStyleProperty());
        setConcatStyle(concat.getValue());
        concatStyleProperty().bind(concat);
        getPrimaryControl().setStyle(getConcatStyle());
        concatStyleProperty().addListener((obversable1, oldValue1, newValue1) -> {
            LOGGER.info("Setting style for " + getKey() + "/" + getIndex() + " to " + newValue1);
            this.setSymStyle("");
            getPrimaryControl().getStyleClass().clear();
            getPrimaryControl().setStyle(newValue1);
        });

    }

    private FontTracker fontTracker;

    private FontTracker getFontTracker() {
        if (fontTracker == null) {
            fontTracker = new FontTracker();
        }
        return fontTracker;
    }
    private ObjectProperty<Font> fontTracking2 = new SimpleObjectProperty<>();

    public void configureFont() {
        setFont();
        getPrimaryControl().setFont(getFontTracker().fontTracking.getValue());
        getPrimaryControl().fontProperty().bind(getFontTracker().fontTracking);
        getPrimaryControl().widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldWidth, Number newWidth) -> {
            setFont();
        });

    }

    private void setFont() {
        getFontTracker().fontTracking.setValue(buildFont());
    }

    private Font buildFont() {
        return Font.font("Roboto", getFontWeight(), null, getFontSize());
    }

    private double getFontSize() {
        return (3 * getPrimaryControl().getWidth() + getPrimaryControl().getHeight()) * (getFontScale() / 1000);
    }

    public void openHomeScreen() {
        LOGGER.info("Opening Home screen: " + FullSession.HOME);
        getMainUI().getStack().load(FullSession.HOME);
        if (!getMainUI().isShowing()) {
            LOGGER.info("Main UI is not showing. Hiding parent for current window");
            getPrimaryControl().getScene().getWindow().hide();
            LOGGER.info("Opening Main UI");
            getMainUI().open();
        }
    }

    /*  private KeyRemoveButton keyRemoveButton;

    public KeyRemoveButton getKeyRemoveButton() {
        if (keyRemoveButton == null) {
            keyRemoveButton = new KeyRemoveButton(getKey(), getIndex());
            keyRemoveButton.setPane("apMain");
            keyRemoveButton.setConfirmable(false);
        }
        return keyRemoveButton;
    }*/
    /**
     *
     */
    public EditAppButton editAppButton;

    public DeleteKeyButton deleteKeyButton;
    public List<SettingsRow> textSettings = new ArrayList<>();
    public OnOffButton showTitleButton;
    public TextArea titleField;
    private ChoiceBox<ColourChoices> textColourChoices;
    private Slider fontScaleSlider = new Slider();

    public ChoiceBox<Pos> textAlignment;
    // TODO: Split into font size, color, background image, background size
    public List<SettingsRow> backgroundSettings = new ArrayList<>();

    public TextArea overrideStyleField;
    private ChoiceBox<ColourChoices> backgroundColourChoices;
    public TextField backgroundURLField;

    public OnOffButton selectableButton;
    public OnOffButton disabledPrimaryButton;

    public TextField navigateIndexField;
    public List<SettingsRow> selectionSettings = new ArrayList<>();

    public TextField rowExpandField;
    public TextField columnExpandField;

    public List<SettingsRow> settings = new ArrayList<>();

    public void resetAppableSettings() {
        titleField.setText(getTitle());
        textAlignment.setValue(Pos.valueOf(getTitlePos()));
        textColourChoices.setValue(getTextColour());
        fontScaleSlider.setValue(getFontScale());
        overrideStyleField.setText(getOverrideStyle());
        backgroundColourChoices.setValue(getBackgroundColour());
        backgroundURLField.setText(getBackgroundURL());
        showTitleButton.setValue(showTitle());
        selectableButton.setValue(isSelectable());
        disabledPrimaryButton.setValue(disablePrimary());
        rowExpandField.setText(String.valueOf(getRowExpand()));
        columnExpandField.setText(String.valueOf(getColumnExpand()));
        navigateIndexField.setText(getNavigateIndex());
    }

    public void setAppableSettings() {
        setTitle(titleField.getText());
        setTitlePos(textAlignment.getValue().toString());
        setTextColour(textColourChoices.getValue());
        setFontScale(fontScaleSlider.getValue());
        setAlignment(textAlignment.getValue());
        setOverrideStyle(overrideStyleField.getText());
        setBackgroundColour(backgroundColourChoices.getValue());
        setBackgroundURL(backgroundURLField.getText());
        setShowTitle(showTitleButton.getValue());
        setSelectable(selectableButton.getValue());
        setDisablePrimary(disabledPrimaryButton.getValue());
        setRowExpand(Integer.valueOf(rowExpandField.getText()));
        setColumnExpand(Integer.valueOf(columnExpandField.getText()));
        setNavigateIndex(navigateIndexField.getText());
    }

    public List<Tab> addAppableSettings() {
        SettingsRow deleteKeyRow = createSettingRow("Delete", "Remove this button");
        final Parent parent = this.getParent();
        ConfigurableGrid configurableGrid;
        if (parent instanceof ConfigurableGrid) {
            configurableGrid = (ConfigurableGrid) parent;
            deleteKeyButton = new DeleteKeyButton(AppableControl.this, configurableGrid);
            deleteKeyButton.setFontScale(10.0);
            deleteKeyButton.setMaxHeight(60.0);
            deleteKeyRow.add(deleteKeyButton, 1, 0, 2, 1);
            settings.add(deleteKeyRow);
        }

        SettingsRow showTitleRow = EditDialog.createSettingRow("Title", "Text to be displayed on this button");
        showTitleButton = new OnOffButton("SHOW", "HIDE");
        showTitleButton.setMaxSize(180.0, 60.0);
        showTitleButton.setValue(showTitle());
        GridPane.setHalignment(showTitleButton, HPos.LEFT);
        GridPane.setValignment(showTitleButton, VPos.CENTER);
        showTitleRow.add(showTitleButton, 1, 0, 1, 1);

        titleField = new TextArea();
        titleField.setText(getTitle());
        GridPane.setMargin(titleField, new Insets(10.0));
        titleField.prefHeight(80.0);
        titleField.prefWidth(360.0);
        titleField.getStyleClass().add("settings-text-area");
        showTitleRow.add(titleField, 2, 0, 1, 1);

        SettingsRow textColourRow = createSettingRow("Colour", "Change the colour of the text");
        textColourChoices = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        ColourChoices.DARK,
                        ColourChoices.LIGHT,
                        ColourChoices.RED,
                        ColourChoices.BLUE,
                        ColourChoices.PURPLE,
                        ColourChoices.ORANGE,
                        ColourChoices.GREEN
                )));
        textColourChoices.setValue(getTextColour());
        textColourChoices.maxHeight(80.0);
        textColourChoices.maxWidth(360.0);
        textColourChoices.getStyleClass().add("settings-text-area");
        textColourRow.add(textColourChoices, 1, 0, 2, 1);

        SettingsRow fontScaleRow = EditDialog.createSettingRow("Scale", "Font scale");

        fontScaleSlider = new Slider(5, 100, getFontScale());
        fontScaleSlider.setMajorTickUnit(5);
        fontScaleSlider.setMinorTickCount(1);
        fontScaleSlider.setShowTickLabels(true);
        fontScaleSlider.setShowTickMarks(true);
        fontScaleRow.add(fontScaleSlider, 1, 0, 2, 1);

        SettingsRow textAlignmentRow = EditDialog.createSettingRow("Text Alignment", "Location of text on this button");

        textAlignment = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList(
                Pos.BOTTOM_LEFT,
                Pos.BOTTOM_CENTER,
                Pos.BOTTOM_RIGHT,
                Pos.CENTER_LEFT,
                Pos.CENTER,
                Pos.CENTER_RIGHT,
                Pos.TOP_LEFT,
                Pos.TOP_CENTER,
                Pos.TOP_RIGHT
        )));
        textAlignment.setValue(Pos.valueOf(getTitlePos()));
        textAlignment.prefHeight(80.0);
        textAlignment.prefWidth(360.0);
        textAlignment.getStyleClass().add("settings-text-area");
        textAlignmentRow.add(textAlignment, 1, 0, 2, 1);

        SettingsRow overrideStyleRow = EditDialog.createSettingRow("Style", "CSS Style code");

        overrideStyleField = new TextArea();
        overrideStyleField.setStyle("-fx-font-size:1.6em;");
        overrideStyleField.setText(getOverrideStyle());
        overrideStyleField.prefHeight(80.0);
        overrideStyleField.prefWidth(360.0);
        overrideStyleField.getStyleClass().add("settings-text-area");
        overrideStyleRow.add(overrideStyleField, 1, 0, 2, 1);

        SettingsRow settingsRowB = createSettingRow("Colour", "Colour of the button background");
        backgroundColourChoices = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        ColourChoices.DARK,
                        ColourChoices.LIGHT,
                        ColourChoices.RED,
                        ColourChoices.BLUE,
                        ColourChoices.PURPLE,
                        ColourChoices.ORANGE,
                        ColourChoices.GREEN
                )));
        backgroundColourChoices.setValue(getBackgroundColour());
        backgroundColourChoices.maxHeight(80.0);
        backgroundColourChoices.maxWidth(360.0);
        backgroundColourChoices.getStyleClass().add("settings-text-area");
        settingsRowB.add(backgroundColourChoices, 1, 0, 2, 1);

        SettingsRow settingsRowU = EditDialog.createSettingRow("Image", "Background URL");

        backgroundURLField = new TextField();
        backgroundURLField.setText(getBackgroundURL());
        backgroundURLField.prefHeight(80.0);
        backgroundURLField.prefWidth(360.0);
        backgroundURLField.getStyleClass().add("settings-text-area");
        settingsRowU.add(backgroundURLField, 1, 0, 2, 1);

        SettingsRow settingsRow4 = EditDialog.createSettingRow("Selectable", "Skip scanning process?");

        selectableButton = new OnOffButton("YES", "NO");
        selectableButton.setMaxSize(180.0, 60.0);
        selectableButton.setValue(isSelectable());
        GridPane.setHalignment(selectableButton, HPos.LEFT);
        GridPane.setValignment(selectableButton, VPos.CENTER);
        settingsRow4.add(selectableButton, 1, 0, 1, 1);

        SettingsRow settingsRow45 = EditDialog.createSettingRow("Primary Disabled", "Lock this button");

        disabledPrimaryButton = new OnOffButton("YES", "NO");
        disabledPrimaryButton.setMaxSize(180.0, 60.0);
        disabledPrimaryButton.setValue(disablePrimary());
        GridPane.setHalignment(disabledPrimaryButton, HPos.LEFT);
        GridPane.setValignment(disabledPrimaryButton, VPos.CENTER);
        settingsRow45.add(disabledPrimaryButton, 1, 0, 1, 1);

        SettingsRow settingsRow5 = EditDialog.createSettingRow("Expand button", "Row x Column");

        rowExpandField = new TextField();
        rowExpandField.setText(getRowExpand().toString());
        rowExpandField.prefHeight(80.0);
        rowExpandField.prefWidth(360.0);
        rowExpandField.getStyleClass().add("settings-text-area");

        columnExpandField = new TextField();
        columnExpandField.setText(getColumnExpand().toString());
        columnExpandField.prefHeight(80.0);
        columnExpandField.prefWidth(360.0);
        columnExpandField.getStyleClass().add("settings-text-area");
        HBox expandHBox = new HBox();
        expandHBox.setSpacing(10.0);
        expandHBox.setPrefHeight(Double.POSITIVE_INFINITY);
        expandHBox.setPrefWidth(Double.POSITIVE_INFINITY);
        expandHBox.getChildren().add(new Label("Row:"));
        expandHBox.getChildren().add(rowExpandField);
        expandHBox.getChildren().add(new Label("Column:"));
        expandHBox.getChildren().add(columnExpandField);

        expandHBox.setAlignment(Pos.CENTER);
        settingsRow5.add(expandHBox, 1, 0, 2, 1);

        SettingsRow settingsRowA = createSettingRow("Navigate", "Screen to navigate to after click");
        navigateIndexField = new TextField();
        navigateIndexField.setText(getNavigateIndex());
        navigateIndexField.maxHeight(80.0);
        navigateIndexField.maxWidth(60.0);
        navigateIndexField.getStyleClass().add("settings-text-area");
        settingsRowA.add(navigateIndexField, 1, 0, 1, 1);

        settings.add(settingsRow5);
        Tab actionTab = buildTab("ACTION", settings);

        selectionSettings.add(settingsRow4);
        selectionSettings.add(settingsRow45);
        selectionSettings.add(settingsRowA);
        Tab selectionTab = buildTab("SELECTION", selectionSettings);

        textSettings.add(showTitleRow);
        textSettings.add(textColourRow);
        textSettings.add(fontScaleRow);
        textSettings.add(textAlignmentRow);
        Tab textTab = buildTab("TEXT", textSettings);

        backgroundSettings.add(settingsRowB);
        backgroundSettings.add(settingsRowU);
        backgroundSettings.add(overrideStyleRow);
        Tab backgroundTab = buildTab("BACKGROUND", backgroundSettings);

        List<Tab> tabs = new ArrayList<>();
        tabs.add(actionTab);
        tabs.add(textTab);
        tabs.add(backgroundTab);
        tabs.add(selectionTab);

        return tabs;
    }

    public Tab buildTab(String title, List<SettingsRow> rows) {
        BuildableGrid grid = EditDialog.buildSettingsGrid(rows);
        ScrollPane scrollPane = buildScrollPane(grid);
        Tab tab = new Tab(title, scrollPane);
        //     tab.setContent(scrollPane);
        return tab;
    }

    public ScrollPane buildScrollPane(BuildableGrid textGrid) {
        ScrollPane textScrollPane = new ScrollPane();
        textScrollPane.setStyle("-fx-background-color:-fx-light;");
        textScrollPane.setPadding(new Insets(10, 30, 30, 30));
        textScrollPane.setFitToWidth(Boolean.TRUE);
        setSizeMax(textScrollPane);
        textScrollPane.setContent(textGrid);
        textScrollPane.getStylesheets().add(CSS_PATH);
        setCSS("main", textScrollPane);
        return textScrollPane;
    }

    public EditAppButton getEditAppButton() {
        if (editAppButton == null) {
            EditDialog editDialog = new EditDialog() {
                @Override
                public void buildDialog() {
                    getStylesheets().add(CSS_PATH);
                    baseGrid.getStyleClass().add("border-background");
                    List<Double> rowPercentages = Arrays.asList(7.5, 32.5, 60.0);
                    buildBaseGrid(3, 1, rowPercentages);
                    baseGrid.add(addSettingControls(), 0, 2);

                    baseGrid.setVgap(0);
                    AnimatedPane actionPane = buildActionPane(HPos.CENTER, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
                    baseGrid.add(actionPane, 0, 0);

                    final AnimatedButton animatedButton = new AnimatedButton();
                    animatedButton.setMaxWidth(540.0);
                    animatedButton.maxHeightProperty().bind(Bindings.multiply(0.325, baseGrid.heightProperty()));
                    //animatedButton.setPrefSize(540.0, 360.0);
                    GridPane.setHalignment(animatedButton, HPos.CENTER);
                    GridPane.setValignment(animatedButton, VPos.CENTER);
                    GridPane.setMargin(animatedButton, new Insets(10.0, 0, 10.0, 0));
                    animatedButton.textProperty().bind(titleField.textProperty());

                    animatedButton.alignmentProperty().bind(textAlignment.valueProperty());

                    animatedButton.styleProperty().bind(
                            Bindings.concat("-fx-background-color:-fx-", backgroundColourChoices.valueProperty().asString(), "; \n",
                                    "-fx-text-fill:-fx-", textColourChoices.valueProperty().asString(), "; \n",
                                    "-fx-background-image:url(\"", backgroundURLField.textProperty(), "\");  "
                                    + "-fx-border-color: -fx-dark;\n"
                                    + "    -fx-border-insets: 0;\n"
                                    + "    -fx-border-width: 5;\n"
                                    + "-fx-background-repeat:no-repeat;\n"
                                    + "-fx-background-position:center;",
                                    overrideStyleField.textProperty()));

                    baseGrid.add(animatedButton, 0, 1);
                    animatedButton.fontProperty().bind(fontTracking2);
                    fontTracking2.setValue(AppableControl.this.getPrimaryControl().getFont());
                    fontScaleSlider.valueProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldWidth, Number newWidth) -> {

                        final double name = (3 * AppableControl.this.getPrimaryControl().getWidth() + AppableControl.this.getPrimaryControl().getHeight()) * fontScaleSlider.getValue() / 1000;
                        fontTracking2.setValue(Font.font("Roboto", getFontWeight(), null, name));
                    });

                    addToStackPane(baseGrid);
                }

                @Override
                public void setSettings() {
                    setAppableSettings();
                    setFont();
                }

                @Override
                public void resetSettings() {
                    resetAppableSettings();
                }

                @Override
                public Node addSettingControls() {
                    TabPane tabPane = new TabPane();
                    tabPane.setPadding(new Insets(5));
                    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                    tabPane.getTabs().addAll(addAppableSettings());
                    return tabPane;
                }

            };

            editAppButton = new EditAppButton(editDialog);
            editAppButton.setPane("apMain");
        }
        return editAppButton;
    }

    public void addConfigButtons() {
        addToPane(getEditAppButton(), null, null, 0.0, 0.0);
        getEditAppButton().toFront();
        getPrimaryControl().setDisable(true);
    }

    public void removeConfigButtons() {
        //     getKeyRemoveButton().removeFromParent();
        getEditAppButton().removeFromParent();
        getPrimaryControl().setDisable(this.disablePrimary());
    }

    /**
     *
     */
    public void configureTitle() {
        loadTitle();
        showTitleProperty().addListener((observable, oldValue, newValue) -> {
            loadTitle();
        });

        titleProperty().addListener((observable, oldValue, newValue) -> {
            loadTitle();
        });
    }

    /**
     *
     */
    public void loadTitle() {
        if (showTitle()) {
            setAlignment(Pos.valueOf(getTitlePos()));
            setText(getTitle());
        } else {
            setText("");
        }
    }

    private StringProperty title;

    /**
     *
     * @param value
     */
    public void setTitle(String value) {
        titleProperty().set(value);
        getPreferences().put("title", value);
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return titleProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(getPreferences().get("title", defaultTitle));
        }
        return title;
    }

    private BooleanProperty editable;

    /**
     *
     * @param value
     */
    public void setEditable(Boolean value) {
        editableProperty().set(value);
        getPreferences().put("editable", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isEditable() {
        return editableProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty editableProperty() {
        if (editable == null) {
            editable = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("editable", "true")));
        }
        return editable;
    }

    private static final Double DEFAULT_FONT_SCALE = 25.0;
    private DoubleProperty fontScale;

    /**
     *
     * @param value
     */
    public void setFontScale(Double value) {
        fontScaleProperty().setValue(value);
        getPreferences().put("fontScale", value.toString());
    }

    /**
     *
     * @return
     */
    public Double getFontScale() {
        return fontScaleProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty fontScaleProperty() {
        if (fontScale == null) {
            fontScale = new SimpleDoubleProperty(Double.valueOf(getPreferences().get("fontScale", DEFAULT_FONT_SCALE.toString())));
        }
        return fontScale;
    }
    private static final Pos DEFAULT_TITLE_POS = Pos.BOTTOM_CENTER;
    private StringProperty titlePos;

    /**
     *
     * @param value
     */
    public void setTitlePos(String value) {
        titlePosProperty().setValue(value);
        getPreferences().put("titlePos", value);
    }

    /**
     *
     * @return
     */
    public String getTitlePos() {
        return titlePosProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty titlePosProperty() {
        if (titlePos == null) {
            titlePos = new SimpleStringProperty(getPreferences().get("titlePos", DEFAULT_TITLE_POS.toString()));
        }
        return titlePos;
    }
    private BooleanProperty showTitle;

    /**
     *
     * @param value
     */
    public void setShowTitle(Boolean value) {
        showTitleProperty().set(value);
        getPreferences().put("showTitle", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean showTitle() {
        return showTitleProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty showTitleProperty() {
        if (showTitle == null) {
            showTitle = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("showTitle", "true")));
        }
        return showTitle;
    }

    private BooleanProperty selectable;

    /**
     *
     * @param value
     */
    public void setSelectable(Boolean value) {
        selectableProperty().set(value);
        getPreferences().put("selectable", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isSelectable() {
        return selectableProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty selectableProperty() {
        if (selectable == null) {
            selectable = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("selectable", "true")));
        }
        return selectable;
    }

    private ObjectProperty<FontWeight> fontWeight;

    /**
     *
     * @param value
     */
    public void setFontWeight(FontWeight value) {
        fontWeightProperty().setValue(value);
        getPreferences().put("fontWeight", value.toString().toUpperCase());
    }

    /**
     *
     * @return
     */
    public FontWeight getFontWeight() {
        return fontWeightProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<FontWeight> fontWeightProperty() {
      //  if (fontWeight == null) {
            fontWeight = new SimpleObjectProperty(FontWeight.valueOf(getPreferences().get("fontWeight", "BOLD").toUpperCase()));
        //}
        return fontWeight;
    }

    private ObjectProperty<ColourChoices> textColour;

    /**
     *
     * @param value
     */
    public void setTextColour(ColourChoices value) {
        textColourProperty().setValue(value);
        getPreferences().put("textColour", value.toString().toUpperCase());
    }

    /**
     *
     * @return
     */
    public ColourChoices getTextColour() {
        return textColourProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ColourChoices> textColourProperty() {
        if (textColour == null) {
            textColour = new SimpleObjectProperty(ColourChoices.valueOf(getPreferences().get("textColour", "DARK").toUpperCase()));
        }
        return textColour;
    }
    private StringProperty backgroundURL;

    /**
     *
     * @param value
     */
    public void setBackgroundURL(String value) {
        backgroundURLProperty().set(value);
        getPreferences().put("backgroundURL", value);
    }

    /**
     *
     * @return
     */
    public String getBackgroundURL() {
        return backgroundURLProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty backgroundURLProperty() {
        if (backgroundURL == null) {
            backgroundURL = new SimpleStringProperty(getPreferences().get("backgroundURL", ""));
        }
        return backgroundURL;
    }
    private ObjectProperty<ColourChoices> backgroundColour;

    /**
     *
     * @param value
     */
    public void setBackgroundColour(ColourChoices value) {
        backgroundColourProperty().setValue(value);
        getPreferences().put("backgroundColour", value.toString().toUpperCase());
    }

    /**
     *
     * @return
     */
    public ColourChoices getBackgroundColour() {
        return backgroundColourProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ColourChoices> backgroundColourProperty() {
        if (backgroundColour == null) {
            backgroundColour = new SimpleObjectProperty(ColourChoices.valueOf(getPreferences().get("backgroundColour", "LIGHT").toUpperCase()));
        }
        return backgroundColour;
    }
    private StringProperty concatStyle;

    /**
     *
     * @param value
     */
    public void setConcatStyle(String value) {
        concatStyleProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getConcatStyle() {
        return concatStyleProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty concatStyleProperty() {
        if (concatStyle == null) {
            concatStyle = new SimpleStringProperty("");

        }
        return concatStyle;
    }
    private StringProperty overrideStyle;

    /**
     *
     * @param value
     */
    public void setOverrideStyle(String value) {
        overrideStyleProperty().set(value);
        getPreferences().put("overrideStyle", value);
    }

    /**
     *
     * @return
     */
    public String getOverrideStyle() {
        return overrideStyleProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty overrideStyleProperty() {
        if (overrideStyle == null) {
            overrideStyle = new SimpleStringProperty(getPreferences().get("overrideStyle", ""));
        }
        return overrideStyle;
    }

    private BooleanProperty disablePrimary;

    /**
     *
     * @param value
     */
    public void setDisablePrimary(Boolean value) {
        disablePrimaryProperty().set(value);
        getPreferences().put("disablePrimary", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean disablePrimary() {
        return disablePrimaryProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty disablePrimaryProperty() {
        if (disablePrimary == null) {
            disablePrimary = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("disablePrimary", "false")));
        }
        return disablePrimary;
    }
    private BooleanProperty speakable;

    /**
     *
     * @param value
     */
    public void setSpeakable(Boolean value) {
        speakableProperty().set(value);
        getPreferences().put("speakable", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isSpeakable() {
        return speakableProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty speakableProperty() {
        if (speakable == null) {
            speakable = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("speakable", "true")));
        }
        return speakable;
    }

    private String navigateTo = "home";
    private StringProperty navigateIndex;

    /**
     *
     * @param value
     */
    public void setNavigateIndex(String value) {
        navigateIndexProperty().set(value);
        getPreferences().put("navigateIndex", value);
    }

    /**
     *
     * @return
     */
    public String getNavigateIndex() {
        return navigateIndexProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty navigateIndexProperty() {
        if (navigateIndex == null) {
            navigateIndex = new SimpleStringProperty(getPreferences().get("navigateIndex", navigateTo));
        }
        return navigateIndex;
    }

    private StringProperty key;

    /**
     *
     * @param value
     */
    public void setKey(String value) {
        keyProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final String getKey() {
        return keyProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty keyProperty() {
        if (key == null) {
            key = new SimpleStringProperty(initKey);
        }
        return key;
    }

    public String initIndex;
    private StringProperty index;

    /**
     * Sets the offset of the UI in the Application list that this button will
     * point to.
     *
     * @param value index offset
     */
    public void setIndex(String value) {
        indexProperty().set(value);
    }

    /**
     * Gets the offset of the UI in the Application list that this button will
     * point to.
     *
     * @return index offset
     */
    public String getIndex() {
        return indexProperty().get();
    }

    /**
     * Represents the offset of the UI in the Application list that this button
     * will point to
     *
     * @return index offset property
     */
    public StringProperty indexProperty() {
        if (index == null) {
            index = new SimpleStringProperty(initIndex);
        }
        return index;
    }

    /**
     *
     * @param value
     */
    @Override
    public void setRowExpand(Integer value) {
        rowExpandProperty().set(value);
        getPreferences().put("rowExpand", value.toString());
    }

    /**
     *
     * @return
     */
    @Override
    public IntegerProperty rowExpandProperty() {
        if (rowExpand == null) {
            rowExpand = new SimpleIntegerProperty(Integer.valueOf(getPreferences().get("rowExpand", "0")));
        }
        return rowExpand;
    }

    /**
     *
     * @param value
     */
    @Override
    public void setColumnExpand(Integer value) {
        columnExpandProperty().set(value);
        getPreferences().put("columnExpand", value.toString());
    }

    /**
     *
     * @return
     */
    @Override
    public IntegerProperty columnExpandProperty() {
        if (columnExpand == null) {
            columnExpand = new SimpleIntegerProperty(Integer.valueOf(getPreferences().get("columnExpand", "0")));
        }
        return columnExpand;
    }

    /**
     *
     */
    public Preferences preferences;

    /**
     *
     * @return
     */
    public abstract Preferences getPreferences();

}
