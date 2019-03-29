/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
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
import org.symfound.controls.system.dialog.FixableErrorDialog;
import org.symfound.controls.system.dialog.ScreenDialog;
import org.symfound.controls.system.dialog.ScreenPopup;
import org.symfound.controls.system.grid.editor.DeleteKeyButton;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.AnimatedPane;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.controls.user.SubGrid;
import org.symfound.main.FullSession;
import static org.symfound.main.FullSession.getMainUI;
import org.symfound.main.HomeController;
import org.symfound.main.settings.SettingsController;
import org.symfound.tools.iteration.ParallelList;
import org.symfound.tools.ui.ColourChoices;
import org.symfound.tools.ui.FontTracker;

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

    public String defaultTitle = "";
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
        setConfirmable(Boolean.FALSE);
        configureFeatures();
    }

    /**
     *
     */
    public void configureFeatures() {
        configureDrag();
        configurePrimaryDisabled();
        configButtons();
    }

    /**
     *
     */
    public static Integer SOURCE_ROW_INDEX = 0;

    /**
     *
     */
    public static Integer SOURCE_COLUMN_INDEX = 0;

    /**
     *
     */
    public static Integer SOURCE_ROW_SPAN = 0;

    /**
     *
     */
    public static Integer SOURCE_COLUMN_SPAN = 0;

    /**
     *
     */
    public static Integer GRID_LOCATION = 0;

    //TODO: Give credit for code?
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

    /**
     *
     */
    public void configurePrimaryDisabled() {
        getPrimaryControl().setDisable(isPrimaryDisabled());
        disablePrimaryProperty().addListener((observable, oldValue, newValue) -> {
            getPrimaryControl().setDisable(newValue);
        });
    }

    /**
     *
     */
    public void configButtons() {
        boolean isSettingsControl = getControlType().equals(ControlType.SETTING_CONTROL);

        // TO DO : CAUSING ISSUES WITH PHOTO AND YOUTUBE. Try when playing?
        if (ConfigurableGrid.inEditMode() && !isSettingsControl && isEditable()) {
            addConfigButtons();
        }
        /*else {
            removeConfigButtons();
        }*/

        ConfigurableGrid.editModeProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1 && !isSettingsControl && isEditable()) {
                addConfigButtons();
            } else {
                removeConfigButtons();
            }
        });
    }

    /**
     *
     */
    public void configureStyle() {

        configureFont();

        final StringExpression concat = Bindings.concat("-fx-background-color:-fx-", backgroundColourProperty().asString(), "; \n",
                "-fx-text-fill:-fx-", textColourProperty().asString(), "; \n",
                "-fx-background-image:url(\"", backgroundURLProperty(), "\"); \n"
                + "-fx-background-repeat:no-repeat;\n"
                + "-fx-background-position:center;\n"
                + "-fx-background-size:", backgroundSizeProperty(), "; \n",
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
    private final ObjectProperty<Font> fontTracking2 = new SimpleObjectProperty<>();

    /**
     *
     */
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

    /**
     *
     */
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

    /**
     *
     */
    public DeleteKeyButton deleteKeyButton;

    /**
     *
     */
    public List<SettingsRow> textSettings = new ArrayList<>();

    /**
     *
     */
    public OnOffButton showTitleButton;

    /**
     *
     */
    public TextArea titleArea;
    private ChoiceBox<ColourChoices> textColourChoices;
    private Slider fontScaleSlider = new Slider();

    /**
     *
     */
    public ChoiceBox<Pos> textAlignment;
    // TODO: Split into font size, color, background image, background size
    //  private ChoiceBox<String> backgroundSizeChoices;
    private Slider backgroundSizeSlider;
    private OnOffButton speakableButton;
    private TextArea speakTextArea;

    /**
     *
     */
    public List<SettingsRow> backgroundSettings = new ArrayList<>();
    public List<SettingsRow> usageSettings = new ArrayList<>();

    /**
     *
     */
    public TextArea overrideStyleField;
    private ChoiceBox<ColourChoices> backgroundColourChoices;
    private ChoiceBox<BackgroundSizeChoices> backgroundSizeChoices;

    /**
     *
     */
    public TextField backgroundURLField;

    /**
     *
     */
    public OnOffButton selectableButton;

    /**
     *
     */
    public OnOffButton disabledPrimaryButton;

    /**
     *
     */
    public ChoiceBox<String> navigateIndexChoices;

    /**
     *
     */
    public List<SettingsRow> selectionSettings = new ArrayList<>();

    /**
     *
     */
    public TextField rowExpandField;

    /**
     *
     */
    public TextField columnExpandField;

    public RunnableControl resetUsageButton;
    public AnimatedLabel totalUsageLabel;

    /**
     *
     */
    public List<SettingsRow> settings = new ArrayList<>();

    /**
     *
     */
    public void resetAppableSettings() {
        titleArea.setText(getTitle());
        textAlignment.setValue(Pos.valueOf(getTitlePos()));
        textColourChoices.setValue(getTextColour());
        speakableButton.setValue(isSpeakable());
        speakTextArea.setText(getSpeakText());
        fontScaleSlider.setValue(getFontScale());
        overrideStyleField.setText(getOverrideStyle());
        backgroundColourChoices.setValue(getBackgroundColour());
        backgroundURLField.setText(getBackgroundURL());
        showTitleButton.setValue(showTitle());
        selectableButton.setValue(isSelectable());
        disabledPrimaryButton.setValue(isPrimaryDisabled());
        rowExpandField.setText(String.valueOf(getRowExpand()));
        columnExpandField.setText(String.valueOf(getColumnExpand()));
        navigateIndexChoices.setValue(getNavigateIndex());
        totalUsageLabel.setText(getTotalUsageCount().toString());
        if (getBackgroundSize().toUpperCase().equals(BackgroundSizeChoices.CONTAIN.toString())
                || getBackgroundSize().toUpperCase().equals(BackgroundSizeChoices.COVER.toString())
                || getBackgroundSize().toUpperCase().equals(BackgroundSizeChoices.STRETCH.toString())) {
            backgroundSizeChoices.setValue(BackgroundSizeChoices.valueOf(getBackgroundSize()));
        } else {
            backgroundSizeChoices.setValue(BackgroundSizeChoices.CUSTOM);
            backgroundSizeSlider.setValue(Double.valueOf(getBackgroundSize()));
        }
        SettingsController.setUpdated(Boolean.FALSE);
    }

    /**
     *
     */
    public void setAppableSettings() {
        setTitle(titleArea.getText());
        setTitlePos(textAlignment.getValue().toString());
        setTextColour(textColourChoices.getValue());
        setFontScale(fontScaleSlider.getValue());
        setSpeakable(speakableButton.getValue());

        String speakTextValue = " ";

        if (speakTextArea.getText() != null && !speakTextArea.getText().isEmpty()) {
            speakTextValue = speakTextArea.getText();

        } else {
            if (!titleArea.getText().isEmpty()) {
                LOGGER.info("Speak Text field is empty. "
                        + "Setting value to Title: " + titleArea.getText());
                speakTextValue = titleArea.getText();
            } else {
                LOGGER.warn("No value entered in Speak Text field");
            }
        }

        setSpeakText(speakTextValue);
        setAlignment(textAlignment.getValue());
        setOverrideStyle(overrideStyleField.getText());
        setBackgroundColour(backgroundColourChoices.getValue());

        if (backgroundSizeChoices.getValue().equals(BackgroundSizeChoices.CONTAIN)
                || backgroundSizeChoices.getValue().equals(BackgroundSizeChoices.COVER)
                || backgroundSizeChoices.getValue().equals(BackgroundSizeChoices.STRETCH)) {
            setBackgroundSize(backgroundSizeChoices.getValue().toString().toLowerCase());
        } else {
            setBackgroundSize(String.valueOf(backgroundSizeSlider.getValue()));
        }

        setBackgroundURL(backgroundURLField.getText());
        setShowTitle(showTitleButton.getValue());
        setSelectable(selectableButton.getValue());
        setDisablePrimary(disabledPrimaryButton.getValue());
        setNavigateIndex(navigateIndexChoices.getValue());
        Boolean reloadRequired = Boolean.FALSE;
        if (!rowExpandField.getText().equals(getRowExpand().toString())) {
            setRowExpand(Integer.valueOf(rowExpandField.getText()));
            reloadRequired = Boolean.TRUE;
        }
        if (!columnExpandField.getText().equals(getColumnExpand().toString())) {
            setColumnExpand(Integer.valueOf(columnExpandField.getText()));
            reloadRequired = Boolean.TRUE;
        }

        SettingsController.setUpdated(reloadRequired);
    }

    /**
     *
     * @return
     */
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

        titleArea = new TextArea();
        titleArea.setText(getTitle());
        GridPane.setMargin(titleArea, new Insets(10.0));
        titleArea.prefHeight(80.0);
        titleArea.prefWidth(360.0);
        titleArea.getStyleClass().add("settings-text-area");
        showTitleRow.add(titleArea, 2, 0, 1, 1);

        SettingsRow speakableRow = createSettingRow("Speakable", "Say the phrase on the button");
        speakableButton = new OnOffButton("YES", "NO");
        speakableButton.setMaxSize(180.0, 60.0);
        speakableButton.setValue(isSpeakable());
        GridPane.setHalignment(speakableButton, HPos.LEFT);
        GridPane.setValignment(speakableButton, VPos.CENTER);
        speakableRow.add(speakableButton, 1, 0, 1, 1);

        speakTextArea = new TextArea();
        speakTextArea.disableProperty().bind(Bindings.not(speakableButton.valueProperty()));
        speakTextArea.setText(getSpeakText());
        GridPane.setMargin(speakTextArea, new Insets(10.0));
        speakTextArea.prefHeight(80.0);
        speakTextArea.prefWidth(360.0);
        speakTextArea.getStyleClass().add("settings-text-area");
        speakableRow.add(speakTextArea, 2, 0, 1, 1);

        SettingsRow textColourRow = createSettingRow("Colour", "Change the colour of the text");
        textColourChoices = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        ColourChoices.DARK,
                        ColourChoices.BLACK,
                        ColourChoices.LIGHT,
                        ColourChoices.WHITE,
                        ColourChoices.RED,
                        ColourChoices.BLUE,
                        ColourChoices.PURPLE,
                        ColourChoices.ORANGE,
                        ColourChoices.GREEN
                )));
        textColourChoices.setValue(getTextColour());
        textColourChoices.setMaxSize(180.0, 60.0);
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
        textAlignment.setMaxSize(180.0, 60.0);

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

        SettingsRow backgroundColourRow = createSettingRow("Colour", "Colour of the button background");
        backgroundColourChoices = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        ColourChoices.DARK,
                        ColourChoices.BLACK,
                        ColourChoices.LIGHT,
                        ColourChoices.WHITE,
                        ColourChoices.RED,
                        ColourChoices.BLUE,
                        ColourChoices.PURPLE,
                        ColourChoices.ORANGE,
                        ColourChoices.GREEN,
                        ColourChoices.TRANSPARENT
                )));
        backgroundColourChoices.setValue(getBackgroundColour());
        backgroundColourChoices.setMaxSize(180.0, 60.0);
        backgroundColourChoices.getStyleClass().add("settings-text-area");
        backgroundColourRow.add(backgroundColourChoices, 1, 0, 2, 1);

        SettingsRow backgroundSizeRow = EditDialog.createSettingRow("Size", "Size of the background image");

        backgroundSizeChoices = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        BackgroundSizeChoices.CUSTOM,
                        BackgroundSizeChoices.CONTAIN,
                        BackgroundSizeChoices.COVER,
                        BackgroundSizeChoices.STRETCH
                )));
        backgroundSizeChoices.setMaxSize(180.0, 60.0);
        backgroundSizeChoices.getStyleClass().add("settings-text-area");
        backgroundSizeRow.add(backgroundSizeChoices, 1, 0, 1, 1);
        backgroundSizeSlider = new Slider(1, 1000, 120);
        backgroundSizeSlider.visibleProperty().bind(Bindings.equal(backgroundSizeChoices.valueProperty(), BackgroundSizeChoices.CUSTOM));

        backgroundSizeSlider.setMajorTickUnit(50);
        backgroundSizeSlider.setMinorTickCount(5);
        backgroundSizeSlider.setShowTickLabels(true);
        backgroundSizeSlider.setShowTickMarks(true);

        if (getBackgroundSize().toUpperCase().equals(BackgroundSizeChoices.CONTAIN.toString())
                || getBackgroundSize().toUpperCase().equals(BackgroundSizeChoices.COVER.toString())
                || getBackgroundSize().toUpperCase().equals(BackgroundSizeChoices.STRETCH.toString())) {
            backgroundSizeChoices.setValue(BackgroundSizeChoices.valueOf(getBackgroundSize().toUpperCase()));
        } else {
            backgroundSizeChoices.setValue(BackgroundSizeChoices.CUSTOM);
            backgroundSizeSlider.setValue(Double.valueOf(getBackgroundSize()));
        }
        backgroundSizeRow.add(backgroundSizeSlider, 2, 0, 1, 1);

        SettingsRow backgroundURLRow = EditDialog.createSettingRow("Image", "Background URL");

        backgroundURLField = new TextField();
        backgroundURLField.setText(getBackgroundURL());
        backgroundURLField.setPrefSize(360.0, 80.0);
        backgroundURLField.getStyleClass().add("settings-text-area");
        backgroundURLRow.add(backgroundURLField, 1, 0, 2, 1);

        SettingsRow settingsRow4 = EditDialog.createSettingRow("Selectable", "Able to select?");

        selectableButton = new OnOffButton("YES", "NO");
        selectableButton.setMaxSize(180.0, 60.0);
        selectableButton.setValue(isSelectable());
        GridPane.setHalignment(selectableButton, HPos.LEFT);
        GridPane.setValignment(selectableButton, VPos.CENTER);
        settingsRow4.add(selectableButton, 1, 0, 1, 1);

        SettingsRow settingsRow45 = EditDialog.createSettingRow("Primary Disabled", "Lock this button");

        disabledPrimaryButton = new OnOffButton("YES", "NO");
        disabledPrimaryButton.setMaxSize(180.0, 60.0);
        disabledPrimaryButton.setValue(isPrimaryDisabled());
        GridPane.setHalignment(disabledPrimaryButton, HPos.LEFT);
        GridPane.setValignment(disabledPrimaryButton, VPos.CENTER);
        settingsRow45.add(disabledPrimaryButton, 1, 0, 1, 1);

        SettingsRow settingsRow5 = EditDialog.createSettingRow("Expand button", "Row x Column");

        rowExpandField = new TextField();
        rowExpandField.setText(getRowExpand().toString());
        rowExpandField.setMaxSize(180.0, 60.0);
        rowExpandField.getStyleClass().add("settings-text-area");

        columnExpandField = new TextField();
        columnExpandField.setText(getColumnExpand().toString());
        columnExpandField.setMaxSize(180.0, 60.0);
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

        SettingsRow navigateRow = createSettingRow("Navigate", "Screen to navigate to after click");
        List<String> navigatableScreens = new ArrayList<>();
        try {
            navigatableScreens = getNavigatableScreens("subgrid");
        } catch (BackingStoreException ex) {
            LOGGER.fatal("Unable to load Preferences" + ex.getMessage());
        }

        navigateIndexChoices = new ChoiceBox<>(FXCollections.observableArrayList(navigatableScreens));
        navigateIndexChoices.setValue(getNavigateIndex());
        navigateIndexChoices.setMaxSize(180.0, 60.0);
        navigateIndexChoices.getStyleClass().add("settings-text-area");
        navigateRow.add(navigateIndexChoices, 1, 0, 2, 1);

        SettingsRow totalUsageRow = EditDialog.createSettingRow("Usage Count", "Number of times this button has been clicked");

        totalUsageLabel = new AnimatedLabel();
        totalUsageLabel.setStyle("-fx-font-size:3em;");

        totalUsageLabel.setText(getTotalUsageCount().toString());
        totalUsageRow.add(totalUsageLabel, 2, 0, 1, 1);

        resetUsageButton = new RunnableControl("settings-button") {
            @Override
            public void run() {
                resetTotalUsageCount();
                totalUsageLabel.setText(getTotalUsageCount().toString());
            }
        };
        resetUsageButton.setVisible(getTotalUsageCount() > 0);
        totalUsageCountProperty().addListener((observable, oldValue, newValue) -> {
            resetUsageButton.setVisible(newValue.intValue() > 0);
        });
        resetUsageButton.setText("RESET");
        resetUsageButton.setMaxSize(180.0, 60.0);
        totalUsageRow.add(resetUsageButton, 1, 0, 1, 1);

        settings.add(settingsRow5);
        Tab actionTab = buildTab("ACTION", settings);

        selectionSettings.add(settingsRow4);
        selectionSettings.add(settingsRow45);
        selectionSettings.add(navigateRow);
        Tab selectionTab = buildTab("SELECTION", selectionSettings);

        textSettings.add(showTitleRow);
        textSettings.add(speakableRow);
        textSettings.add(textColourRow);
        textSettings.add(fontScaleRow);
        textSettings.add(textAlignmentRow);
        Tab textTab = buildTab("TEXT", textSettings);

        backgroundSettings.add(backgroundColourRow);
        backgroundSettings.add(backgroundSizeRow);
        backgroundSettings.add(backgroundURLRow);
        backgroundSettings.add(overrideStyleRow);
        Tab backgroundTab = buildTab("BACKGROUND", backgroundSettings);

        usageSettings.add(totalUsageRow);
        Tab usageTab = buildTab("STATS", usageSettings);

        List<Tab> tabs = new ArrayList<>();
        tabs.add(actionTab);
        tabs.add(textTab);
        tabs.add(backgroundTab);
        tabs.add(selectionTab);
        tabs.add(usageTab);

        return tabs;
    }

    private List<String> getNavigatableScreens(String node) throws BackingStoreException {
        List<String> screenNames = new ArrayList<>();
        List<String> childrenNames = Arrays.asList(Preferences.userNodeForPackage(this.getClass()).node(node).childrenNames());
        if (childrenNames.size() > 0) {
            for (String child : childrenNames) {
                final String name = node + "/" + child;
                screenNames.add(name.replaceAll("subgrid/", ""));
                screenNames.addAll(getNavigatableScreens(name));
            }
        }

        return screenNames;
    }

    /**
     *
     * @param title
     * @param rows
     * @return
     */
    public Tab buildTab(String title, List<SettingsRow> rows) {
        BuildableGrid grid = EditDialog.buildSettingsGrid(rows);
        ScrollPane scrollPane = buildScrollPane(grid);
        Tab tab = new Tab(title, scrollPane);
        //     tab.setContent(scrollPane);
        return tab;
    }

    /**
     *
     * @param textGrid
     * @return
     */
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

    /**
     *
     * @return
     */
    public EditAppButton getEditAppButton() {
        if (editAppButton == null) {
            EditDialog editDialog = new EditDialog("Edit " + defaultTitle + " Button") {
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

                    final AnimatedButton animatedButton = new AnimatedButton("");
                    animatedButton.setMaxWidth(540.0);
                    animatedButton.maxHeightProperty().bind(Bindings.multiply(0.325, baseGrid.heightProperty()));
                    //animatedButton.setPrefSize(540.0, 360.0);
                    GridPane.setHalignment(animatedButton, HPos.CENTER);
                    GridPane.setValignment(animatedButton, VPos.CENTER);
                    GridPane.setMargin(animatedButton, new Insets(10.0, 0, 10.0, 0));
                    animatedButton.textProperty().bind(titleArea.textProperty());

                    animatedButton.alignmentProperty().bind(textAlignment.valueProperty());

                    animatedButton.styleProperty().bind(Bindings.concat("-fx-background-color:-fx-", backgroundColourChoices.valueProperty().asString(), "; \n",
                            "-fx-text-fill:-fx-", textColourChoices.valueProperty().asString(), "; \n",
                            "-fx-background-image:url(\"", backgroundURLField.textProperty(), "\"); \n",
                            "-fx-border-color: -fx-dark;\n",
                            "-fx-border-insets: 0;\n",
                            "-fx-border-width: 5;\n",
                            "-fx-background-size:", backgroundSizeSlider.valueProperty().asString(), "; \n",
                            "-fx-background-repeat:no-repeat;\n",
                            "-fx-background-position:center;", overrideStyleField.textProperty()
                    ));

                    baseGrid.add(animatedButton, 0, 1);

                    animatedButton.fontProperty().bind(fontTracking2);
                    fontTracking2.setValue(Font.font("Roboto", getFontWeight(), null, getFontScale()));
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
    StringBinding backgroundSizeActive;

    /**
     *
     */
    public void addConfigButtons() {
        addToPane(getEditAppButton(), null, null, 0.0, 0.0);
        getEditAppButton().toFront();
        getPrimaryControl().setDisable(true);
    }

    /**
     *
     */
    public void removeConfigButtons() {
        getEditAppButton().removeFromParent();
        getPrimaryControl().setDisable(isPrimaryDisabled());
    }
    /**
     *
     */
    private FixableErrorDialog errorDialog;

    /**
     *
     * @param message
     * @return
     */
    public FixableErrorDialog getFixableErrorDialog(String message) {
        if (errorDialog == null) {
            errorDialog = new FixableErrorDialog("ERROR", message, "EDIT", "BACK") {
                @Override
                public void onOk() {
                    final EditAppButton thisEditButton = getEditAppButton();
                    HomeController.getGrid().getChildren().add(thisEditButton.getPopup(thisEditButton.getDialog()));
                    final Double selectionTime = getSession().getUser().getInteraction().getSelectionTime();
                    getDialog().animate().startScale(selectionTime, 0.8, 1.0);
                    final SubGrid homeGrid = HomeController.getGrid();
                    homeGrid.setInError(Boolean.FALSE);
                }

                @Override
                public void onCancel() {
                    openHomeScreen();
                    ConfigurableGrid configurableGrid = HomeController.getGrid().getConfigurableGrid();
                    configurableGrid.setIndex("home");
                    getSession().setPlaying(false);
                    final SubGrid homeGrid = HomeController.getGrid();
                    homeGrid.setInError(Boolean.FALSE);
                }
            };
        }
        return errorDialog;
    }

    public void generateFixableError(String message) {
        final SubGrid homeGrid = HomeController.getGrid();
        if (!homeGrid.isInError()) {
            homeGrid.setInError(Boolean.TRUE);
            final FixableErrorDialog errDialog = getFixableErrorDialog(message);
            final ScreenPopup<ScreenDialog> errorPopup = getPopup(errDialog);
            homeGrid.getChildren().add(errorPopup);
        }
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

    @Override
    public void run() {
        if (isSpeakable()) {
            speak(getSpeakText());
        }
        incrementTotalUsageCount();
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

    private StringProperty backgroundSize;

    /**
     *
     * @param value
     */
    public void setBackgroundSize(String value) {
        backgroundSizeProperty().setValue(value);
        getPreferences().put("backgroundSize", value);
    }

    /**
     *
     * @return
     */
    public String getBackgroundSize() {
        return backgroundSizeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty backgroundSizeProperty() {
        if (backgroundSize == null) {
            backgroundSize = new SimpleStringProperty(getPreferences().get("backgroundSize", "contain"));
        }
        return backgroundSize;
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
    public Boolean isPrimaryDisabled() {
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

    private StringProperty speakText;

    /**
     *
     * @param value
     */
    public void setSpeakText(String value) {
        speakTextProperty().set(value);
        getPreferences().put("speakText", value);
    }

    /**
     *
     * @return
     */
    public String getSpeakText() {
        return speakTextProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty speakTextProperty() {
        if (speakText == null) {
            speakText = new SimpleStringProperty(getPreferences().get("speakText", getText()));
        }
        return speakText;
    }

    private final String navigateTo = "";
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

    private IntegerProperty totalUsageCount;

    /**
     *
     * @param value
     */
    public void setTotalUsageCount(Integer value) {
        totalUsageCountProperty().set(value);
        getPreferences().put("usage.count", value.toString());
    }

    public void incrementTotalUsageCount() {
        setTotalUsageCount(getTotalUsageCount() + 1);
    }

    public void resetTotalUsageCount() {
        setTotalUsageCount(0);
    }

    /**
     *
     * @return
     */
    public Integer getTotalUsageCount() {
        return totalUsageCountProperty().get();
    }

    /**
     *
     * @return
     */
    public IntegerProperty totalUsageCountProperty() {
        if (totalUsageCount == null) {
            totalUsageCount = new SimpleIntegerProperty(Integer.valueOf(getPreferences().get("usage.count", "0")));
        }
        return totalUsageCount;
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

    /**
     *
     */
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
     * @param dimension
     */
    public void setExpandByDimension(Integer value, String dimension) {
        switch (dimension) {
            case "row":
                setRowExpand(value);
                break;
            case "column":
                setColumnExpand(value);
                break;
            default:
                throw new IllegalArgumentException(dimension + " parameter not recognized");
        }
    }

    /**
     *
     * @param dimension
     * @return
     */
    public Integer getExpandByDimension(String dimension) {
        Integer expand = -1;
        switch (dimension) {
            case "row":
                expand = getRowExpand();
                break;
            case "column":
                expand = getColumnExpand();
                break;
            default:
                throw new IllegalArgumentException(dimension + " parameter not recognized");
        }
        return expand;
    }
    private IntegerProperty rowExpand;

    /**
     *
     * @param value
     */
    public void setRowExpand(Integer value) {
        rowExpandProperty().set(value);
        getPreferences().put("rowExpand", value.toString());
    }

    /**
     *
     * @return
     */
    public Integer getRowExpand() {
        return rowExpandProperty().get();
    }

    /**
     *
     * @return
     */
    public IntegerProperty rowExpandProperty() {
        if (rowExpand == null) {
            rowExpand = new SimpleIntegerProperty(Integer.valueOf(getPreferences().get("rowExpand", "0")));
        }
        return rowExpand;
    }

    private IntegerProperty columnExpand;

    /**
     *
     * @param value
     */
    public void setColumnExpand(Integer value) {
        columnExpandProperty().set(value);
        getPreferences().put("columnExpand", value.toString());
    }

    /**
     *
     * @return
     */
    public Integer getColumnExpand() {
        return columnExpandProperty().get();
    }

    /**
     *
     * @return
     */
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
