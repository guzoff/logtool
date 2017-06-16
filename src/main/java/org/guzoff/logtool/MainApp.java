package org.guzoff.logtool;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.guzoff.logtool.logio.GrouperPojo;
import org.guzoff.logtool.logio.FilterPojo;
import org.guzoff.logtool.logio.Units;
import static javafx.concurrent.Worker.State.RUNNING;

/**
 * Main GUI application, running on the JavaFX Application thread.
 * @author guzoff
 */
public class MainApp extends Application {
    
    private final FilterPojo filter = new FilterPojo();
    private final GrouperPojo grouper = new GrouperPojo();
    private File saveToFile = new File(System.getProperty("user.dir"), "result.log");
    private ExecutorService executor = Executors.newFixedThreadPool(1);
    
    //filtering section
    private final DatePicker startDatePicker = new DatePicker();
    private final DatePicker endDatePicker = new DatePicker();
    private final ChoiceBox usernameChoiceBox = new ChoiceBox(FXCollections
            .observableArrayList(
            "ANY", "alex", "bobby", "carol", "dave", "ethan", "felix", "ginger",
            "helen", "irene", "john"));
    private final TextField searchTextField = new TextField();
    
    //grouping section
    private final CheckBox byUserCheckBox = new CheckBox("Username");
    private final CheckBox byTimeunitCheckBox = new CheckBox("Time Unit:");
    private final RadioButton yearRadioButton = new RadioButton("Year");
    private final RadioButton monthRadioButton = new RadioButton("Month");
    private final RadioButton dayRadioButton = new RadioButton("Day");
    
    //other section
    private final ChoiceBox threadsChoiceBox = new ChoiceBox(FXCollections
            .observableArrayList(Arrays.asList(
                    new Integer[] {1,2,3,4,5,6,7,8,9,10})));
    private final TextField fileNameField = new TextField("File not selected");
    private final Button browseButton = new Button("Browse");
    
    //administrative section
    private boolean notFirstStart = false;
    private final Button startButton = new Button("Start");
    private final Button cancelButton = new Button("Cancel");
    private final Button exitButton = new Button("Exit");
    private final ProgressBar progressBar = new ProgressBar();
    private final Label progressLabel = new Label("None");
    private final TextArea statisticsTextArea = new TextArea();
    private final TextArea messageTextArea = new TextArea();
    
    //wrapping task instance into the service object
    Service<String> service = new Service<String>() {
        @Override
        protected Task<String> createTask() {
            return new ReadAndGroupTask(filter, grouper, saveToFile, executor);
        }
    };
    
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(final Stage stage) {
        //starting GUI
        Scene scene = buildGUI(stage);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setScene(scene);
        stage.setTitle("Log Tool v.0.1.1");
        stage.show();
    }
    
    //invoked by pressing "Start" button
    private void startTask() {
        if ((startDatePicker.valueProperty().isNull().get()
                && endDatePicker.valueProperty().isNull().get()
                && usernameChoiceBox.valueProperty().isEqualTo("ANY").get()
                && searchTextField.textProperty().isEmpty().get())
                        || !(byUserCheckBox.isSelected()
                        || byTimeunitCheckBox.isSelected())) {
            messageTextArea.setText("WARNING!\n"
                    + "You should specify at least one filter"
                    + " and one grouping criteria.");
            return;
        }
        filter.setMsgPattern(searchTextField.getText());
        if (notFirstStart) {
            service.restart();
        } else {
            notFirstStart = true;
            service.start();
        }
    }

    private Scene buildGUI(Stage stage) {
        
        //some inits
        startDatePicker.setValue(LocalDate.of(2010, Month.JANUARY, 1)); //my logs start from here
        usernameChoiceBox.setValue("ANY");
        ToggleGroup radioGroup = new ToggleGroup();
        radioGroup.getToggles().addAll(yearRadioButton, monthRadioButton, dayRadioButton);
        yearRadioButton.setSelected(true);
        threadsChoiceBox.setValue(1);
        fileNameField.setText("result.log");
        statisticsTextArea.setWrapText(true);
        statisticsTextArea.setStyle("-fx-font-family: monospace");
        statisticsTextArea.setPrefRowCount(20);
        statisticsTextArea.setPrefColumnCount(60);
        messageTextArea.setWrapText(true);
        messageTextArea.setPrefRowCount(3);
        
        //block ability of the endDatePicker to pick erroneous values
        final Callback<DatePicker, DateCell> dayCellFactory = (datePicker) -> 
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(startDatePicker.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #CCCCCC;");
                        }
                    }
                };
        endDatePicker.setDayCellFactory(dayCellFactory);
        
        bindGUIToEngine(stage);
        
        //building in order of appearance
        Label label1 = new Label("Filters:");
        HBox box1 = new HBox(5, new Label("From date:"), startDatePicker,
                                new Label("To date:"), endDatePicker);
        HBox box2 = new HBox(5, new Label("Username:"), usernameChoiceBox,
                                new Label("Search Text:"), searchTextField);
        Separator separ1 = new Separator();
        Label label2 = new Label("Group By:");
        HBox box3 = new HBox(10, byUserCheckBox, byTimeunitCheckBox, yearRadioButton, 
                                                        monthRadioButton, dayRadioButton);
        Separator separ2 = new Separator();
        HBox box4 = new HBox(5, new Label("Number of threads:"), threadsChoiceBox);
        HBox box5 = new HBox(10, new Label("Save logs to:"), fileNameField, browseButton);
        HBox box6 = new HBox(20, startButton, cancelButton, exitButton);
        Separator separ3 = new Separator();
        HBox box7 = new HBox(5, new Label("Progress:"), progressBar, progressLabel);
        HBox box8 = new HBox(5, new Label("Statistics:"), statisticsTextArea);
        HBox box9 = new HBox(5, new Label("Status:"), messageTextArea);
        
        //adding to the root
        VBox root = new VBox(5, label1, box1, box2, separ1, label2, box3, separ2,
                    box4, box5, box6, separ3, box7, box8, box9);
        root.setStyle("-fx-padding: 10;");
        
        Scene scene = new Scene(root, 640, 600);
        return scene;
    }

    private void bindGUIToEngine(Stage stage) {
        //GUI block#1
        startDatePicker.valueProperty().addListener((observable, oldVal, newVal) ->
                filter.setPeriodStart(LocalDateTime.of(newVal, LocalTime.MIN)));
        endDatePicker.valueProperty().addListener((observable, oldVal, newVal) -> 
                filter.setPeriodEnd(LocalDateTime.of(newVal, LocalTime.MAX)));
        usernameChoiceBox.setOnAction((a) -> {
            String user = (String) usernameChoiceBox.getValue();
            filter.setUsername(user.equals("ANY") ? "" : user);
        });
        
        //GUI block#2
        byUserCheckBox.setOnAction((a) -> grouper.setByUsername(byUserCheckBox.isSelected()));
        byTimeunitCheckBox.setOnAction((a) -> grouper.setByTimeUnit(byTimeunitCheckBox.isSelected()));
        yearRadioButton.setOnAction((a) -> grouper.setUnit(Units.YEAR));
        monthRadioButton.setOnAction((a) -> grouper.setUnit(Units.MONTH));
        dayRadioButton.setOnAction((a) -> grouper.setUnit(Units.DAY));
        
        //GUI block#3
        threadsChoiceBox.setOnAction((a) -> executor = Executors
                .newFixedThreadPool((Integer)threadsChoiceBox.getValue()));
        browseButton.setOnAction((a) -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = 
                    new FileChooser.ExtensionFilter("LOG files (*.log)", "*.log");
            fileChooser.getExtensionFilters().add(extFilter);
            saveToFile = fileChooser.showSaveDialog(stage);
            if (saveToFile != null) {
                fileNameField.setText(saveToFile.getPath());
            }
        });
        
        //GUI command buttons block
        startButton.setOnAction((a) -> startTask());
        exitButton.setOnAction((a) -> Platform.exit());
        cancelButton.setOnAction((a) -> service.cancel());
        startButton.disableProperty().bind(service.stateProperty().isEqualTo(RUNNING));
        cancelButton.disableProperty().bind(service.stateProperty().isNotEqualTo(RUNNING));
        
        //GUI bottom block
        progressBar.progressProperty().bind(service.progressProperty());
        progressLabel.textProperty().bind(new When(service.progressProperty().isEqualTo(-1))
                .then("None").otherwise(service.progressProperty().multiply(100)
                        .asString("%.0f%%")));
        statisticsTextArea.textProperty().bind(service.valueProperty());
        service.messageProperty().addListener((observable, oldVal, newVal) -> {
            messageTextArea.setText(newVal);
        });
    }
}
