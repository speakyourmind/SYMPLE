/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.log4j.Logger;
import org.symfound.builder.user.characteristic.Statistics;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import org.symfound.controls.system.EditAppButton;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.SnapshotButton;
import org.symfound.controls.system.TabTitle;
import org.symfound.controls.system.dialog.EditDialog;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.system.dialog.FixableErrorDialog;
import org.symfound.controls.system.dialog.ScreenDialog;
import org.symfound.controls.system.dialog.ScreenPopup;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.AnimatedPane;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.controls.user.ConfigurableGrid;
import static org.symfound.controls.user.ConfigurableGrid.PREVIOUS_SCREEN_INDEX;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.SubGrid;
import org.symfound.main.FullSession;
import static org.symfound.main.FullSession.getMainUI;
import org.symfound.main.HomeController;
import org.symfound.tools.iteration.ParallelList;
import org.symfound.tools.ui.ColourChoices;

/**
 *
 * @author Javed Gangjee
 */
public abstract class AppableControl extends ConfirmableControl implements Cloneable {

    private static final String NAME = AppableControl.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    public String defaultTitle = "";
    private String initKey = "";
    public String fontFamily = "Roboto";

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
        defineButton();
        configureFeatures();

    }

    public void defineButton() {
        setConfirmable(Boolean.FALSE);
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
            if (ConfigurableGrid.inEditMode() && isEditable()) {

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
            }
        });
        setOnDragDone((DragEvent event) -> {
            if (ConfigurableGrid.inEditMode() && isEditable()) {

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
            }
        });
        setOnDragOver((DragEvent event) -> {
            if (ConfigurableGrid.inEditMode() && isEditable()) {

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
            }
        });

        setOnDragEntered((DragEvent event) -> {
            if (ConfigurableGrid.inEditMode() && isEditable()) {

                /* the drag-and-drop gesture entered the source */
                //     LOGGER.info("onDragEntered");
                /* show to the user that it is an actual gesture source */
                if (event.getGestureSource() != this
                        && event.getDragboard().hasString()) {
                    //setText("Drag Entered");

                }
                event.consume();
            }
        });

        setOnDragExited((DragEvent event) -> {
            if (ConfigurableGrid.inEditMode() && isEditable()) {

                /* mouse moved away, remove the graphical cues */
                //  setText("Drag Exited");
                event.consume();
            }
        });
        setOnDragDropped((DragEvent event) -> {
            if (ConfigurableGrid.inEditMode() && isEditable()) {

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
            }
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
    }

    public void configureStyle(String fontFamily, FontWeight fw) {

        configureFont(fontFamily, fw);

        final StringExpression concat = Bindings.concat("-fx-background-color:-fx-", backgroundColourProperty().asString(), "; \n",
                "-fx-text-fill:-fx-", textColourProperty().asString(), "; \n",
                "-fx-background-image:url(\"", backgroundURLProperty(), "\"); \n"
                + "-fx-background-repeat:no-repeat;\n"
                + "-fx-background-position:center;\n"
                //    + "-fx-padding:0 0 -"+Bindings.divide(fontScaleProperty(),4).asString()+" 0;\n"
                + "-fx-wrap-text:", wrapTextProperty().asString(), ";\n"
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

    private final ObjectProperty<Font> fontTracking2 = new SimpleObjectProperty<>();

    public void configureFont(String fontFamily, FontWeight fw) {
        resetFont(fontFamily, fw);
        final AnimatedButton primaryControl = getPrimaryControl();
        primaryControl.setFont(getFontTracker().fontTracking.getValue());
        primaryControl.fontProperty().bind(getFontTracker().fontTracking);
        //TO DO: Could be an issue
        HomeController.getSubGrid().getConfigurableGrid().statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(ScreenStatus.PLAYING)) {
                resetFont(fontFamily, fw);
                //setFont();
                primaryControl.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldWidth, Number newWidth) -> {
                    //    resetFont(fontFamily, fw);
                    setFont(fontFamily, fw);
                });

            }
        });
    }

    public void resetFont(String fontFamily, FontWeight fw) {
        getFontTracker().fontTracking.setValue(buildFont(fontFamily, fw, 0.0));
    }

    private void setFont(String fontFamily, FontWeight fw) {
        getFontTracker().fontTracking.setValue(buildFont(fontFamily, fw, getFontSize()));
    }

    private Font buildFont(String fontFamily, FontWeight fw, Double fontSize) {
        return Font.font(fontFamily, fw, fontSize);
    }

    private double getFontSize() {
        final AnimatedButton primaryControl = getPrimaryControl();
        final double width = primaryControl.getWidth();

        final double height = primaryControl.getHeight();
        final double fontSize = (3 * width + height) * (getFontScale() / 1000);
        return fontSize;
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
    /**
     *
     */
    public EditAppButton editAppButton;

    /**
     *
     */
    public List<SettingsRow> textSettings = new ArrayList<>();

    /**
     *
     */
    public OnOffButton showTitleButton;
    public TextArea titleArea;
    private ChoiceBox<ColourChoices> textColourChoices;
    private Slider fontScaleSlider = new Slider();
    public ChoiceBox<Pos> textAlignment = new ChoiceBox<>();
    // TODO: Split into font size, color, background image, background size
    //  private ChoiceBox<String> backgroundSizeChoices;
    private Slider backgroundSizeSlider;
    private OnOffButton speakableButton;
    private OnOffButton wrapTextButton;
    private TextArea speakTextArea;

    public SnapshotButton snapshotButton;
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

    public TextField backgroundURLField;
    public OnOffButton selectableButton;

    public OnOffButton disabledPrimaryButton;
    public ChoiceBox<String> navigateIndexChoices;
    public List<SettingsRow> selectionSettings = new ArrayList<>();

    private Slider rowExpandSlider;
    private Slider columnExpandSlider;

    public RunnableControl resetUsageButton;
    public AnimatedLabel totalUsageLabel;
    public AnimatedLabel lastUsedLabel;

    /**
     *
     */
    public List<SettingsRow> generalSettings = new ArrayList<>();

    /**
     *
     */
    public void resetAppableSettings() {
        titleArea.setText(getTitle());
        textAlignment.setValue(Pos.valueOf(getTitlePos()));
        textColourChoices.setValue(getTextColour());
        speakableButton.setValue(isSpeakable());
        wrapTextButton.setValue(wrapText());
        speakTextArea.setText(getSpeakText());
        fontScaleSlider.setValue(getFontScale());
        overrideStyleField.setText(getOverrideStyle());
        backgroundColourChoices.setValue(getBackgroundColour());
        backgroundURLField.setText(getBackgroundURL());
        showTitleButton.setValue(showTitle());
        selectableButton.setValue(isSelectable());
        disabledPrimaryButton.setValue(isPrimaryDisabled());
        rowExpandSlider.setValue(getRowExpand());
        columnExpandSlider.setValue(getColumnExpand());

        if (navigatePostClick()) {
            navigateIndexChoices.setValue(getNavigateIndex());
        }
        totalUsageLabel.setText(getTotalUsageCount().toString());
        if (getBackgroundSize().toUpperCase().equals(BackgroundSizeChoices.CONTAIN.toString())
                || getBackgroundSize().toUpperCase().equals(BackgroundSizeChoices.COVER.toString())
                || getBackgroundSize().toUpperCase().equals(BackgroundSizeChoices.STRETCH.toString())) {
            backgroundSizeChoices.setValue(BackgroundSizeChoices.valueOf(getBackgroundSize().toUpperCase()));
        } else {
            backgroundSizeChoices.setValue(BackgroundSizeChoices.CUSTOM);
            backgroundSizeSlider.setValue(Double.valueOf(getBackgroundSize()));
        }
        HomeController.setUpdated(Boolean.FALSE);
    }

    /**
     *
     */
    public void setAppableSettings() {

        HomeController.setUpdated(Boolean.FALSE);
        setTitle(titleArea.getText());
        setTitlePos(textAlignment.getValue().toString());
        setTextColour(textColourChoices.getValue());
        setFontScale(fontScaleSlider.getValue());
        setFont("Roboto", FontWeight.NORMAL);
        setSpeakable(speakableButton.getValue());
        setWrapText(wrapTextButton.getValue());

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
        if (navigatePostClick()) {
            setNavigateIndex(navigateIndexChoices.getValue());
        }
        Boolean reloadRequired = Boolean.FALSE;

        if (rowExpandSlider.getValue() != Double.valueOf(getRowExpand())) {
            setRowExpand(Double.valueOf(rowExpandSlider.getValue()).intValue());
            reloadRequired = Boolean.TRUE;
        }

        if (columnExpandSlider.getValue() != Double.valueOf(getColumnExpand())) {
            setColumnExpand(Double.valueOf(columnExpandSlider.getValue()).intValue());
            reloadRequired = Boolean.TRUE;
        }

        HomeController.setUpdated(reloadRequired);
    }

    /**
     *
     * @return
     */
    public List<Tab> addAppableSettings() {
        /*  SettingsRow deleteKeyRow = createSettingRow("Delete", "Remove this button");
        final Parent parent = this.getParent();
        ConfigurableGrid configurableGrid;
        if (parent instanceof ConfigurableGrid) {
            configurableGrid = (ConfigurableGrid) parent;
            deleteKeyButton = new DeleteKeyButton(AppableControl.this, configurableGrid);
            deleteKeyButton.setFontScale(10.0);
            deleteKeyButton.setMaxHeight(60.0);
            deleteKeyRow.add(deleteKeyButton, 1, 0, 2, 1);
            settings.add(deleteKeyRow);
        }*/

        SettingsRow snapshotRow = EditDialog.createSettingRow("Snapshot", "Export this button as a .PNG file");

        final AnimatedButton primaryControl = AppableControl.this.getPrimaryControl();

        snapshotButton = new SnapshotButton("settings-button", primaryControl, AppableControl.this.getText()) {
            @Override
            public void run() {
                primaryControl.setDisable(Boolean.FALSE);
                super.run();
                primaryControl.setDisable(Boolean.TRUE);
            }
        };
        snapshotButton.setControlType(ControlType.SETTING_CONTROL);

        snapshotButton.setText("SAVE");
        snapshotButton.setMaxSize(180.0, 60.0);
        snapshotRow.add(snapshotButton, 1, 0, 1, 1);

        List<Tab> tabs = new ArrayList<>();
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
        SettingsRow fontScaleRow = EditDialog.createSettingRow("Scale", "Set the size of text");
        fontScaleSlider = new Slider(5, 100, getFontScale());
        fontScaleSlider.setMajorTickUnit(5);
        fontScaleSlider.setMinorTickCount(1);
        fontScaleSlider.setShowTickLabels(true);
        fontScaleSlider.setShowTickMarks(true);
        fontScaleSlider.setSnapToTicks(true);
        fontScaleRow.add(fontScaleSlider, 1, 0, 2, 1);

        SettingsRow wrapTextRow = createSettingRow("Wrap Text", "Wrap the text displayed on the button");
        wrapTextButton = new OnOffButton("YES", "NO");
        wrapTextButton.setMaxSize(180.0, 60.0);
        wrapTextButton.setValue(wrapText());
        GridPane.setHalignment(wrapTextButton, HPos.LEFT);
        GridPane.setValignment(wrapTextButton, VPos.CENTER);
        wrapTextRow.add(wrapTextButton, 1, 0, 1, 1);

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

        SettingsRow selectableRow = EditDialog.createSettingRow("Selectable", "Able to select?");

        selectableButton = new OnOffButton("YES", "NO");
        selectableButton.setMaxSize(180.0, 60.0);
        selectableButton.setValue(isSelectable());
        GridPane.setHalignment(selectableButton, HPos.LEFT);
        GridPane.setValignment(selectableButton, VPos.CENTER);
        selectableRow.add(selectableButton, 1, 0, 1, 1);

        SettingsRow primaryDisabledRow = EditDialog.createSettingRow("Disable", "Lock this button");

        disabledPrimaryButton = new OnOffButton("YES", "NO");
        disabledPrimaryButton.setMaxSize(180.0, 60.0);
        disabledPrimaryButton.setValue(isPrimaryDisabled());
        GridPane.setHalignment(disabledPrimaryButton, HPos.LEFT);
        GridPane.setValignment(disabledPrimaryButton, VPos.CENTER);
        primaryDisabledRow.add(disabledPrimaryButton, 1, 0, 1, 1);

        SettingsRow rowExpandRow = createSettingRow("Row Expand", "Number of grid rows to expand.");

        rowExpandSlider = new Slider(0.0, 10.0, getRowExpand());
        rowExpandSlider.setMajorTickUnit(1);
        rowExpandSlider.setMinorTickCount(0);
        rowExpandSlider.setShowTickLabels(true);
        rowExpandSlider.setShowTickMarks(true);
        rowExpandSlider.setSnapToTicks(true);

        rowExpandRow.add(rowExpandSlider, 1, 0, 2, 1);
        SettingsRow columnExpandRow = createSettingRow("Column Expand", "Number of grid column to expand.");

        columnExpandSlider = new Slider(0.0, 10.0, getColumnExpand());
        columnExpandSlider.setMajorTickUnit(1);
        columnExpandSlider.setMinorTickCount(0);
        columnExpandSlider.setShowTickLabels(true);
        columnExpandSlider.setShowTickMarks(true);
        columnExpandSlider.setSnapToTicks(true);

        columnExpandRow.add(columnExpandSlider, 1, 0, 2, 1);

        //    if (navigatePostClick()) {
        SettingsRow navigateRow = createSettingRow("Navigate", "Screen to navigate to after click");
        List<String> navigatableScreens = new ArrayList<>();
        navigatableScreens.add("");
        navigatableScreens.add(PREVIOUS_SCREEN_INDEX);
        try {
            navigatableScreens.addAll(getNavigatableScreens("subgrid"));
        } catch (BackingStoreException ex) {
            LOGGER.fatal("Unable to load Preferences" + ex.getMessage());
        }
        navigateIndexChoices = new ChoiceBox<>(FXCollections.observableArrayList(navigatableScreens));
        navigateIndexChoices.setValue(getNavigateIndex());
        navigateIndexChoices.setMaxSize(180.0, 60.0);
        navigateIndexChoices.getStyleClass().add("settings-text-area");
        navigateRow.add(navigateIndexChoices, 1, 0, 2, 1);
        selectionSettings.add(navigateRow);
        //    }

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
        resetUsageButton.setControlType(ControlType.SETTING_CONTROL);
        resetUsageButton.setVisible(getTotalUsageCount() > 0);
        totalUsageCountProperty().addListener((observable, oldValue, newValue) -> {
            resetUsageButton.setVisible(newValue.intValue() > 0);
        });
        resetUsageButton.setText("RESET");
        resetUsageButton.setMaxSize(180.0, 60.0);
        totalUsageRow.add(resetUsageButton, 1, 0, 1, 1);

        SettingsRow lastUsedRow = EditDialog.createSettingRow("Last Used", "Last time this button was clicked");
        lastUsedLabel = new AnimatedLabel();
        lastUsedLabel.setStyle("-fx-font-size:3em;");
        Date date = new Date(getLastUsed());
        DateFormat formatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss aaa");
        formatter.setTimeZone(TimeZone.getDefault());
        String lastUsedString = formatter.format(date);
        lastUsedLabel.setText(lastUsedString);
        lastUsedRow.add(lastUsedLabel, 2, 0, 1, 1);

        generalSettings.add(rowExpandRow);
        generalSettings.add(columnExpandRow);
        generalSettings.add(snapshotRow);
        Tab actionTab = buildTab("General", generalSettings);

        selectionSettings.add(selectableRow);
        selectionSettings.add(primaryDisabledRow);
        Tab selectionTab = buildTab("Selection", selectionSettings);

        textSettings.add(showTitleRow);
        textSettings.add(speakableRow);
        textSettings.add(textColourRow);
        textSettings.add(fontScaleRow);
        textSettings.add(textAlignmentRow);
        textSettings.add(wrapTextRow);
        Tab textTab = buildTab("Text", textSettings);

        backgroundSettings.add(backgroundColourRow);
        backgroundSettings.add(backgroundSizeRow);
        backgroundSettings.add(backgroundURLRow);
        backgroundSettings.add(overrideStyleRow);
        Tab backgroundTab = buildTab("Background", backgroundSettings);

        usageSettings.add(totalUsageRow);
        usageSettings.add(lastUsedRow);
        Tab usageTab = buildTab("Statistics", usageSettings);

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
        Tab tab = new Tab("", scrollPane);
        final TabTitle tabTitle = new TabTitle(title);
        tab.setGraphic(tabTitle);
        tab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                tabTitle.getLabel().setStyle("-fx-text-fill:-fx-blue;");
            } else {
                tabTitle.getLabel().setStyle("-fx-text-fill:-fx-light;");

            }
        });
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
                    //     List<Double> rowPercentages = Arrays.asList(7.5, 32.5, 60.0);
                    List<Double> rowPercentages = Arrays.asList(7.5, 92.5);
                    //   buildBaseGrid(3, 1, rowPercentages);
                    buildBaseGrid(2, 1, rowPercentages);
                    baseGrid.add(addSettingControls(), 0, 1);

                    baseGrid.setVgap(0);
                    AnimatedPane actionPane = buildActionPane(HPos.CENTER, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
                    baseGrid.add(actionPane, 0, 0);

                    //  AnimatedButton animatedButton = buildPreviewButton();
                    //baseGrid.add(animatedButton, 0, 1);
                    //animatedButton.fontProperty().bind(fontTracking2);
                    fontTracking2.setValue(Font.font("Roboto", getFontScale()));
                    fontScaleSlider.valueProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldWidth, Number newWidth) -> {
                        final double name = (3 * AppableControl.this.getPrimaryControl().getWidth() + AppableControl.this.getPrimaryControl().getHeight()) * fontScaleSlider.getValue() / 1000;
                        fontTracking2.setValue(Font.font("Roboto", name));
                    });

                    addToStackPane(baseGrid);

                }

                public AnimatedButton buildPreviewButton() {
                    final AnimatedButton previewButton = new AnimatedButton("");
                    previewButton.setMaxWidth(540.0); // TO DO: Make it a percentage
                    previewButton.maxHeightProperty().bind(Bindings.multiply(0.325, baseGrid.heightProperty()));
                    //animatedButton.setPrefSize(540.0, 360.0);
                    GridPane.setHalignment(previewButton, HPos.CENTER);
                    GridPane.setValignment(previewButton, VPos.CENTER);
                    GridPane.setMargin(previewButton, new Insets(10.0, 0, 10.0, 0));
                    previewButton.textProperty().bind(titleArea.textProperty());
                    previewButton.alignmentProperty().bind(textAlignment.valueProperty());
                    previewButton.styleProperty().bind(Bindings.concat("-fx-background-color:-fx-", backgroundColourChoices.valueProperty().asString(), "; \n",
                            "-fx-text-fill:-fx-", textColourChoices.valueProperty().asString(), "; \n",
                            "-fx-background-image:url(\"", backgroundURLField.textProperty(), "\"); \n",
                            "-fx-border-color: -fx-dark;\n",
                            "-fx-border-insets: 0;\n",
                            "-fx-border-width: 5;\n",
                            "-fx-background-size:", backgroundSizeSlider.valueProperty().asString(), "; \n",
                            "-fx-background-size:", backgroundSizeChoices.valueProperty().asString(), "; \n", //TODO:Blank if custom
                            "-fx-background-repeat:no-repeat;\n",
                            "-fx-background-position:center;",
                            overrideStyleField.textProperty()
                    ));
                    return previewButton;
                }

                @Override
                public void setSettings() {
                    setAppableSettings();
                    //    setFont(fontFamily, getFontWeight());
                }

                @Override
                public void resetSettings() {
                    resetAppableSettings();
                    //  setFont(fontFamily, FontWeight.NORMAL);
                }

                @Override
                public Node addSettingControls() {
                    TabPane tabPane = new TabPane();
                    tabPane.setSide(Side.LEFT);
                    tabPane.setPadding(new Insets(0, 0, 5, 5));
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
                    HomeController.getSubGrid().getChildren().add(thisEditButton.getPopup(thisEditButton.getDialog()));
                    final Double selectionTime = getSession().getUser().getInteraction().getSelectionTime();
                    getDialog().animate().startScale(selectionTime, 0.8, 1.0);
                    final SubGrid homeGrid = HomeController.getSubGrid();
                    homeGrid.setInError(Boolean.FALSE);
                }

                @Override
                public void onCancel() {
                    openHomeScreen();
                    ConfigurableGrid configurableGrid = HomeController.getSubGrid().getConfigurableGrid();
                    configurableGrid.setIndex("home");
                    getSession().setPlaying(false);

                    final SubGrid homeGrid = HomeController.getSubGrid();
                    homeGrid.setInError(Boolean.FALSE);
                    ConfigurableGrid.setEditMode(Boolean.FALSE);

                }

            };
            final SubGrid homeGrid = HomeController.getSubGrid();
            homeGrid.indexProperty().addListener((observable, oldValue, newValue) -> {
                homeGrid.setInError(Boolean.FALSE);
            });
            errorDialog.buildDialog();
        }
        return errorDialog;
    }
    private ScreenPopup<ScreenDialog> errorPopup;

    public void generateFixableError(String message) {
        final SubGrid homeGrid = HomeController.getSubGrid();
        if (!homeGrid.isInError()) {
            homeGrid.setInError(Boolean.TRUE);
            final FixableErrorDialog errDialog = getFixableErrorDialog(message);
            errorPopup = getPopup(errDialog);
            //  homeGrid.getChildren().add(errorPopup);
            //Pane pane = (Pane) getScene().lookup("#apMain");
            HomeController.getSubGrid().getChildren().add(errorPopup);

        } else {
            HomeController.getSubGrid().getChildren().remove(errorPopup);
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
        final Statistics statistics = getSession().getUser().getStatistics();
        if (isSpeakable()) {
            Integer numOfWords = 0;
            final String wordsToSpeak = getSpeakText();
            if (wordsToSpeak != null && !wordsToSpeak.isEmpty()) {
                StringTokenizer tokens = new StringTokenizer(wordsToSpeak);
                numOfWords = tokens.countTokens();
                speak(wordsToSpeak);
            } else {
                LOGGER.warn("There are no words to speak");
            }

            if (statistics.isRecording()) {
                statistics.setTotalSpokenWordsCount(statistics.getTotalSpokenWordsCount() + numOfWords);
                statistics.setSessionSpokenWordCount(statistics.getSessionSpokenWordCount() + numOfWords);
            }
        }

        if (statistics.isRecording()) {
            if (!getSession().getUser().getInteraction().isInAssistedMode()) {
                setLastUsed(System.currentTimeMillis());
                incrementTotalUsageCount();
                if (!getControlType().equals(ControlType.SETTING_CONTROL)) {
                    statistics.incrementTotalSelectionCount();
                    statistics.incrementSessionSelections();
                }
            }
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

    private BooleanProperty removable;

    /**
     *
     * @param value
     */
    public void setRemovable(Boolean value) {
        removableProperty().set(value);
        getPreferences().put("removable", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isRemovable() {
        return removableProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty removableProperty() {
        if (removable == null) {
            removable = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("removable", "true")));
        }
        return removable;
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
        fontWeight = new SimpleObjectProperty(FontWeight.valueOf(getPreferences().get("fontWeight", FontWeight.NORMAL.toString()).toUpperCase()));
        //}
        return fontWeight;
    }

    private BooleanProperty wrapText;

    /**
     *
     * @param value
     */
    public void setWrapText(Boolean value) {
        wrapTextProperty().setValue(value);
        getPreferences().put("wrapText", value.toString().toUpperCase());
    }

    /**
     *
     * @return
     */
    public Boolean wrapText() {
        return wrapTextProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty wrapTextProperty() {
        if (wrapText == null) {
            wrapText = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("wrapText", "true")));
        }
        return wrapText;
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

    private BooleanProperty navigatePostClick;

    /**
     *
     * @param value
     */
    public void setNavigatePostClick(Boolean value) {
        navigatePostClickProperty().set(value);
        getPreferences().put("navigatePostClick", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean navigatePostClick() {
        return navigatePostClickProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty navigatePostClickProperty() {
        if (navigatePostClick == null) {
            navigatePostClick = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("navigatePostClick", "true")));
        }
        return navigatePostClick;
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
    private static final String LAST_USED_KEY = "usage.lastUsed";
    private LongProperty lastUsed;

    /**
     *
     * @param value
     */
    private void setLastUsed(Long value) {
        lastUsedProperty().set(value);
        getPreferences().put(LAST_USED_KEY, value.toString());
    }

    public Long getLastUsed() {
        return lastUsedProperty().get();
    }

    public LongProperty lastUsedProperty() {
        if (lastUsed == null) {
            lastUsed = new SimpleLongProperty(Long.valueOf(getPreferences().get(LAST_USED_KEY, "0")));
        }
        return lastUsed;
    }

    private static final String USAGE_COUNT_KEY = "usage.count";
    private IntegerProperty totalUsageCount;

    /**
     *
     * @param value
     */
    public void setTotalUsageCount(Integer value) {
        totalUsageCountProperty().set(value);
        getPreferences().put(USAGE_COUNT_KEY, value.toString());
    }

    public void incrementTotalUsageCount() {
        setTotalUsageCount(getTotalUsageCount() + 1);
    }

    public void resetTotalUsageCount() {
        setTotalUsageCount(0);
    }

    public Integer getTotalUsageCount() {
        return totalUsageCountProperty().get();
    }

    public IntegerProperty totalUsageCountProperty() {
        if (totalUsageCount == null) {
            totalUsageCount = new SimpleIntegerProperty(Integer.valueOf(getPreferences().get(USAGE_COUNT_KEY, "0")));
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
