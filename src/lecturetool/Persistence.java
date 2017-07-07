/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturetool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author dunning
 */
public class Persistence {
    //defaults
    public String filename = "options";
    public boolean autoNotes;
    public String noteLocation;
    public String logLocation;
    public String currentCourse;
    public String currentNote;
    
    /**
     * Set default
     */
    public Persistence() {
        setDefaults();
    }
    
    /**
     * Persistence default options
     */
    final void setDefaults() {
        autoNotes = false;
        noteLocation = "notes";
        logLocation = "log.txt";
        currentCourse = "";
        currentNote = "";
    }
    
    /**
     * Read options from file and store options 
     * @return true if successful, false if not successful
     */
    public boolean readOptions() {
        File optFile = new File(filename);
        try(Scanner reader = new Scanner(optFile)) {
            autoNotes = reader.nextLine().equals("true");
            noteLocation = reader.nextLine();
            logLocation = reader.nextLine();
            currentCourse = reader.nextLine();
            currentNote = reader.nextLine();
            return true;
        } catch(IOException e) {
            Logger.logError("Could not read options from " + filename +
                    ". Resetting to default settings.");
            setDefaults();
            return false;
        } catch(NoSuchElementException e) {
            Logger.logError("Could not read options from " + filename +
                    ". Resetting to default settings.");
            setDefaults();
            return false;
        }
    }
    
    /**
     * Set options
     * @param autoNote
     * @param noteLoc
     * @param logLoc 
     * @param course 
     * @param note 
     */
    public void setOptions(boolean autoNote, String noteLoc, String logLoc,
            String course, String note) {
        autoNotes = autoNote;
        noteLocation = noteLoc;
        logLocation = logLoc;
        currentCourse = course;
        currentNote = note;
    }
    
    /**
     * Write options to file
     * @return true if successful, false if unsuccessful
     */
    public boolean writeOptions() {
        File optFile = new File(filename);
        try(FileWriter writer = new FileWriter(optFile)) {
            writer.write(autoNotes + System.lineSeparator());
            writer.write(noteLocation + System.lineSeparator());
            writer.write(logLocation + System.lineSeparator());
            writer.write(currentCourse + System.lineSeparator());
            writer.write(currentNote + System.lineSeparator());
            
            return true;
        } catch(IOException e) {
            Logger.logError("Could not write options to " + filename);
            return false;
        }
    }
}
