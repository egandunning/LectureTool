/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturetool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dunning
 */
public class Launch extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        stage.setTitle("Lecture Tool");
        try {
            stage.getIcons().add(new Image(
                    Launch.class.getResourceAsStream("lecturetool-icon.png")));
        } catch(Exception e) {
            Logger.logError("Unable to load program icons");
        }
        FXMLLoader loader = new FXMLLoader();
        Pane pane = (Pane)loader.load(getClass().getResource("FXMLDocument.fxml").openStream());
        FXMLDocumentController controller = loader.getController();
        double screenHSize = Screen.getPrimary().getBounds().getHeight();
        double screenVSize = Screen.getPrimary().getBounds().getWidth();
        
        Scene scene = new Scene(pane);
        stage.setMaximized(true);
        stage.setScene(scene);
        
        //window close listener
        stage.setOnCloseRequest((WindowEvent we) -> {
            controller.saveAndQuit();
        });
        
        //window resize listeners
        stage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            if(screenHSize / 2 >= (double)newHeight) {
                controller.smallFont();
            } else {
                controller.normalFont();
            }
        });
        
        stage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            if(screenVSize / 2 >= (double)newWidth) {
                controller.smallFont();
            } else {
                controller.normalFont();
            }
        });
        
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
