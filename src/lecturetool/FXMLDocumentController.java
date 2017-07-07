/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturetool;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author dunning
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private GridPane gridPane;
    @FXML private WebEngine engine;
    @FXML private WebView webView;
    @FXML private Label courseLabel;
    @FXML private Label noteLabel;
    @FXML private Label infoLabel;
    @FXML private TextArea noteTextArea;
    @FXML private ComboBox fontSizeCombo;
    private String currentCourse = "";
    private String currentNote = "";
    
    private final String version = "LectureTool version 1";
        
    private Persistence storedOptions;
    //Used for lambda expression
    private boolean defaults = false;
    //keyboard shortcut flag
    private boolean numbering = false;
    private boolean bullets = false;
    
    /**
     * Display options dialog. Save options to file
     */
    @FXML
    public void options() {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Options");
        dialog.setHeaderText("");
        dialog.setGraphic(new Label());
        dialog.setResizable(true);
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        
        ComboBox<String> courses = new ComboBox(FXCollections.observableList(
                Arrays.asList(Course.getCourses())));
        TextField courseUrlField = new TextField();
        courseUrlField.setPrefWidth(300);
        
        courses.addEventHandler(ActionEvent.ACTION, eventHandler -> {
            courseUrlField.setText(Course.getUrl(courses.getValue()));
        });
        
        CheckBox autoNotesCheckBox = new CheckBox();
        autoNotesCheckBox.setText("Automatically generate a new note "
                + "for each video in playlist?(not supported yet)");
        
        Label noteLocLabel = new Label("Set note storage location");
        TextField noteLocField = new TextField(Note.loc);
        Button noteLocButton = new Button("Browse");
        
        Label logLocLabel = new Label("Set log storage location");
        TextField logLocField = new TextField(Logger.getFilename());
        Button logLocButton = new Button("Browse");
        
        Button defaultsButton = new Button("Reset Defaults");
       
        defaults = false;
        defaultsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, handler -> {
            defaults = true;
        });
        
        gp.add(new Label("Change course URL"), 0, 0);
        gp.add(courses, 1, 0);
        gp.add(courseUrlField, 2, 0);
        gp.add(autoNotesCheckBox, 0, 1, 3, 1);
        gp.add(noteLocLabel, 0, 2);
        gp.add(noteLocField, 1, 2);
        gp.add(noteLocButton, 2, 2);
        gp.add(logLocLabel, 0, 3);
        gp.add(logLocField, 1, 3);
        gp.add(logLocButton, 2, 3);
        gp.add(defaultsButton, 2, 4);
        
        dialog.getDialogPane().setContent(gp);
        Optional<ButtonType> buttonPressed = dialog.showAndWait();
        if(buttonPressed.isPresent() && buttonPressed.get() == ButtonType.OK) {
            if(defaults) {
                storedOptions.setDefaults();
            } else {
                if(courses.getValue() != null) {
                    Course.updateUrl(courses.getValue(), courseUrlField.getText());
                }
                storedOptions.setOptions(buttonPressed.isPresent(), 
                        noteLocField.getText(), 
                        logLocField.getText(),
                        currentCourse,
                        currentNote);
            }
            storedOptions.writeOptions();
            applySettings();
        }
    }
        
    /**
     * Return true if a the OK button was pressed
     * @return true if course was created, false otherwise
     */
    @FXML
    public boolean newCourse() {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Add new course");
        dialog.setHeaderText("");
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        
        Label courseNameLabel = new Label("Enter course name:");
        TextField courseNameField = new TextField();
        Label courseLocLabel = new Label("Enter course URL:");
        TextField courseLocField = new TextField();
        
        gp.add(courseNameLabel, 0, 0);
        gp.add(courseNameField, 1, 0);
        gp.add(courseLocLabel, 0, 1);
        gp.add(courseLocField, 1, 1);
        
        dialog.getDialogPane().setContent(gp);
        Optional<ButtonType> buttonPressed = dialog.showAndWait();
        if(buttonPressed.isPresent() && buttonPressed.get() == ButtonType.OK) {
            if(Course.makeCourse(courseNameField.getText(), courseLocField.getText())) {
                DisplayLogger.setMessage(courseNameField.getText() + " created.");
                return true;
            } else {
                DisplayLogger.setMessage(courseNameField.getText() + " NOT created.");
            }
            
        }
        return false;
    }
    
    @FXML
    public boolean newNote() {
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add note for course " + currentCourse);
        dialog.setHeaderText("");
        dialog.setContentText("Note title:");
        Optional<String> noteName = dialog.showAndWait();
        if(noteName.isPresent()) {
            if(Note.makeNote(currentCourse, noteName.get())) {
                DisplayLogger.setMessage(noteName.get() + " created.");
                return true;
            } else {
                DisplayLogger.setMessage(noteName.get() + " NOT created.");
            }
        }
        return false;
    }
    
    @FXML
    public void openCourse() {
        unsavedNotePrompt();
        String[] courses = Course.getCourses();
        if(courses.length == 0) {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setHeaderText("");
            dialog.setContentText("No courses yet! Add a new course?");
            Optional<ButtonType> choice = dialog.showAndWait();
            if(choice.isPresent() && choice.get() == ButtonType.OK) {
                //if user doesn't create course, return
                if(!newNote()) {
                    return;
                }
            } else {
                //user chose not to make new course
                return;
            }
        } 
        
        courses = Course.getCourses();
        ChoiceDialog dialog = new ChoiceDialog(courses[0], Arrays.asList(courses));
        dialog.setTitle("Open Course");
        dialog.setHeaderText("");
        dialog.setContentText("Choose a course:");
        Optional<String> courseChoice = dialog.showAndWait();
        if(courseChoice.isPresent()) {
            setCurrentCourse(courseChoice.get());
            setCurrentNote("");
            engine.load(Course.getUrl(currentCourse));
        }
    }
    
    @FXML
    public void openNote() {
        unsavedNotePrompt();
        if(currentCourse.equals("")) {
            openCourse();
        }
        if(currentCourse.equals("")) {
            return;
        }
        String[] notes = Note.getNotes(currentCourse);
        
        if(notes.length == 0) {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setHeaderText("");
            dialog.setContentText("No notes for " + currentCourse + " yet! Add a new note?");
            Optional<ButtonType> choice = dialog.showAndWait();
            if(choice.isPresent() && choice.get() == ButtonType.OK) {
                //if user doesn't create course, return
                if(!newNote()) {
                    return;
                }
            } else {
                //user chose not to make new course
                return;
            }
        }
        
        notes = Note.getNotes(currentCourse);
        ChoiceDialog dialog = new ChoiceDialog(notes[0], Arrays.asList(notes));
        dialog.setTitle("Open Note");
        dialog.setHeaderText("");
        dialog.setContentText("Choose note to open:");
        Optional<String> choice = dialog.showAndWait();
        if(choice.isPresent()) {
            setCurrentNote(choice.get());
        }
    }
    
    @FXML
    public void deleteCourse() {
        if(currentCourse.equals("")) {
            openCourse();
            if(currentCourse.equals("")) {
                return;
            }
        }
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setHeaderText("");
        dialog.setContentText("Delete course " + currentCourse + "?");
        Optional<ButtonType> choice = dialog.showAndWait();
        if(choice.isPresent() && choice.get() == ButtonType.OK) {
            if(Course.deleteCourse(currentCourse)) {
                DisplayLogger.setMessage(currentCourse + " deleted.");
                setCurrentCourse("");
                setCurrentNote("");
            } else {
                DisplayLogger.setMessage(currentCourse + " NOT deleted.");
            }
        }
    }
    
    @FXML
    public void deleteNote() {
        if(currentCourse.equals("")) {
            openCourse();
            if(currentCourse.equals("")) {
                return;
            }
        }
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setHeaderText("");
        dialog.setContentText("Delete note " + currentNote + " from " + currentCourse + "?");
        Optional<ButtonType> choice = dialog.showAndWait();
        if(choice.isPresent() && choice.get() == ButtonType.OK) {
            if(Note.deleteNote(currentCourse, currentNote)) {
                DisplayLogger.setMessage(currentNote + " deleted.");
                setCurrentNote("");
                openNote();
            } else {
                DisplayLogger.setMessage(currentNote + " NOT deleted.");
            }
            
        }
    }
    
    @FXML
    public boolean saveNote() {
        if(currentNote == null) {
            openNote();
        }
        if(Note.writeData(currentCourse, currentNote, noteTextArea.getText())) {
            DisplayLogger.setMessage(currentNote + " saved.");
            return true;
        }
        DisplayLogger.setMessage(currentNote + " NOT saved.");
        return false;
    }
    
    @FXML
    public void fontSizeChanged(ActionEvent evt) {
        noteTextArea.setStyle("-fx-font-size:"+
                (int)fontSizeCombo.getSelectionModel().getSelectedItem());
    }
    
    @FXML
    public void viewInfo() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Information");
        dialog.setResizable(true);
        dialog.setHeaderText("");
        dialog.setContentText(DisplayLogger.displayMessages());
        dialog.showAndWait();
    }
    
    @FXML
    public void undoTyping() {
        noteTextArea.undo();
    }
    
    @FXML
    public void redoTyping() {
        noteTextArea.redo();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Logger.log(version);
        DisplayLogger.logLabel = infoLabel;
        storedOptions = new Persistence();
        if(storedOptions.readOptions()) {
            DisplayLogger.setMessage("Successfully read from settings file.");
        }
        setCurrentCourse(storedOptions.currentCourse);
        setCurrentNote(storedOptions.currentNote);
        applySettings();
        
        DisplayLogger.setMessage(version);
        
        
        engine = webView.getEngine();
        engine.load(Course.getUrl(currentCourse));
        
        fontSizeCombo.setItems(FXCollections.observableList(
                Arrays.asList(10,12,14,16,18)));
        fontSizeCombo.getSelectionModel().select(
                noteTextArea.getFont().getSize());
        
        noteTextArea.setMinWidth(0.33 * gridPane.getWidth());
        noteTextArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.S && event.isShortcutDown()) {
                saveNote();
            } else if(event.getCode() == KeyCode.DIGIT1 &&
                    event.isShortcutDown()) {
                System.out.println("here");
                bullets = !bullets;
            } else if(bullets && event.getCode() == KeyCode.ENTER) {
                int index = noteTextArea.getCaretPosition();
                noteTextArea.insertText(index, "\n\u2022 ");
                event.consume();
            }
        });
    }
    
    /**
     * Apply settings specified in persistence object
     */
    public void applySettings() {
        Note.loc = storedOptions.noteLocation;
        Logger.close();
        Logger.init(storedOptions.logLocation);
        DisplayLogger.setMessage("Settings applied.");
    }
    
    /**
     * Saves settings and exits program
     */
    public void saveAndQuit() {
        unsavedNotePrompt();
        storedOptions.currentCourse = currentCourse;
        storedOptions.currentNote = currentNote;
        storedOptions.writeOptions();
        Logger.close();
        System.exit(0);
    }
    
    /**
     * Sets the current course
     * @param course 
     */
    public void setCurrentCourse(String course) {
        currentCourse = course;
        courseLabel.setText(course);
    }
    
    public void setCurrentNote(String note) {
        currentNote = note;
        noteLabel.setText(note);
        noteTextArea.setText(Note.getText(currentCourse, currentNote));
    }
    
    /**
     * Shrink font size
     */
    public void smallFont() {
        gridPane.setStyle("-fx-font-size:10");
    }
    
    /**
     * Reset font size to normal
     */
    public void normalFont() {
        gridPane.setStyle("-fx-font-size:12");
    }
    
    /**
     * If note is unsaved, prompt user to save note.
     * @return Returns true if a note is been saved
     */
    public boolean unsavedNotePrompt() {
        
        if(noteTextArea.getText().equals(Note.getText(currentCourse, currentNote))) {
            return true;
        }
        
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Save note?");
        dialog.setHeaderText("");
        dialog.setContentText("Save note " + currentNote + "?");
        Optional<ButtonType> choice = dialog.showAndWait();
        if(choice.isPresent() && choice.get() == ButtonType.OK) {
            return saveNote();
        }
        return false;
    }
}
