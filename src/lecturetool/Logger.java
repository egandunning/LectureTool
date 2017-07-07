/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturetool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author dunning
 */
public class Logger {
    private static String filename;
    private static File logFile;
    private static FileWriter writer;
    private static ArrayList<String> queuedMsg = new ArrayList<String>();
    
    /**
     * 
     * @param file 
     */
    public static void init(String file) {
        filename = file;
        logFile = new File(file);
        try {
            writer = new FileWriter(logFile, true);
            writer.append("----------------------------\r\n");
            writer.append(new Date().toString() + " > Logger started.\r\n");
            writer.flush();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
    }
    
    /**
     * Close file
     */
    public static void close() {
        if(writer == null) {
            //nothing to close
            return;
        }
        try {
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Writes msg to file
     * @param msg string to write to file
     * @return true if successful
     */
    public static boolean log(String msg) {
        if(writer == null) {
            queuedMsg.add(msg);
            return false;
        }
        while(!queuedMsg.isEmpty()) {
            log(queuedMsg.remove(queuedMsg.size() - 1));
        }
        try {
            writer.append(new Date().toString() + " > " + msg + "\r\n");
            writer.flush();
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * Log error message
     * @param msg
     * @return true if successful
     */
    public static boolean logError(String msg) {
        return log("ERROR: " + msg);
    }
    
    /**
     * Log general info
     * @param msg
     * @return 
     */
    public static boolean logInfo(String msg) {
        return log("Info: " + msg);
    }
    
    /**
     * Return log file name
     * @return filename
     */
    public static String getFilename() {
        return filename;
    }
    
    /**
     * Closes current log file and creates new log file
     * @param fname new log file location
     */
    public static void setFilename(String fname) {
        logInfo("new log file location is at: " + fname);
        close();
        init(fname);
    }
}
