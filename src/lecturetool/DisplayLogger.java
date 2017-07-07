/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturetool;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Label;


/**
 *
 * @author dunning
 */
public class DisplayLogger {
    public static Label logLabel;
    public static ArrayList<String> msgList = new ArrayList<String>();
    public static short seconds = 10;
    public static Timer timer;
    
    public static void appendMessage(String msg) {
        if(logLabel == null) {
            return;
        }
        logLabel.setText(msg+" | "+logLabel.getText());
        msgList.add(new Date().toString() + ": " + msg);
    }
    
    /**
     * Sets label to display a message for a limited time
     * @param msg the message to display
     */
    public static void setMessage(String msg) {
        if(logLabel == null) {
            return;
        }
        logLabel.setText(msg);
        msgList.add(new Date().toString() + ": " + msg);
        
        //make message disappear
        try{
            timer.cancel();
        } catch(Exception e) {
            // do nothing, timer hasn't been initialized yet
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        logLabel.setText("");
                    }
                });
            }
        }, seconds * 1000);
    }
    

    
    public static void appendError() {
        appendMessage("Error! See log file for details");
    }
    
    public static String displayMessages() {
        String messages = "";
        for(String msg : msgList) {
            messages += msg + System.lineSeparator();
        }
        return messages;
    }
    
    public static void clearMessages() {
        msgList.clear();
        logLabel.setText("");
    }
}
