/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturetool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
/**
 *
 * @author dunning
 */
public class Note {
    
    public static String loc = "notes";
    
    /**
     * Make note file
     * @param dir
     * @param fname
     * @return 
     */
    public static boolean makeNote(String dir, String fname) {
        File noteFile = new File(loc+"\\"+dir, fname);
        boolean success = false;
        try {
            success = noteFile.createNewFile();
            if(success) {
                Logger.logInfo("created Note  at " + dir + "\\" + fname);
            } else {
                Logger.logInfo("did NOT create Note at " + dir + "\\" + fname);
            }
        } catch(IOException e) {
            Logger.logError(e.toString() + ". could not open " + fname);
            e.printStackTrace();
        } catch(SecurityException e) {
            Logger.logError(e.toString() + ". permission denied" + fname);
        }
        return success;
    }
    
    public static void makeNote(Optional<String> dir, String fname) {
        if(dir.isPresent()) {
            makeNote(dir.get(), fname);
        } else {
            Logger.logInfo("No note name specified. Note was not created.");
        }
    }
    
    /**
     * Delete note and description file at the specified location.
     * @param dir the directory of the note file
     * @param fname the name of the note file
     * @return 
     */
    public static boolean deleteNote(String dir, String fname) {
        File noteFile = new File(loc+"\\"+dir, fname);
        boolean success = noteFile.delete();
        if(success) {
            Logger.logInfo("Note "+loc+"\\" + dir + "\\" + fname + " deleted.");
        } else {
            Logger.logInfo("Note "+loc+"\\" + dir + "\\" + fname + " NOT deleted.");
        }
        return success;
    }
    
    /**
     * Get the contents of a note
     * @param course the course
     * @param note the note
     * @return a string containing the contents of the note
     */
    public static String getText(String course, String note) {
        String text = "";
        File f = new File(loc+"\\"+course, note);
        try {
            Scanner s = new Scanner(f);
            while(s.hasNext()) {
                text += s.nextLine() + "\n";
            }
            s.close();
        } catch(IOException e) {
            Logger.logError(e.toString() + ". could not open "+loc+"\\"+course + "\\" + note);
        }
        return text;
    }
    
    /**
     * Write text to note file
     * @param course
     * @param note
     * @param text content to write
     * @return true if successful
     */
    public static boolean writeData(String course, String note, String text) {
        try {
            File noteFile = new File(loc+"\\"+course, note);
            FileWriter writer = new FileWriter(noteFile);
            writer.append(text);
            writer.flush();
            writer.close();
        } catch(IOException e) {
            Logger.logError("could not write data to note file: "+loc+"\\"+course+"\\"+note);
            return false;
        }
        return true;
    }
    
    /**
     * Retrieve the notes for a given course
     * @param course
     * @return 
     */
    public static String[] getNotes(String course) {
        File courseDir = new File(loc+"\\" + course);
        String[] all = courseDir.list();
        if(all.length == 0) {
            return all;
        }
        //new array to hold names of notes without the course url
        String[] notes = new String[all.length - 1];
        int j = 0;
        for(int i = 0; i < notes.length; i++) {
            if(all[j].equals("url")) {
               j += 2; 
            } else {
                notes[i] = all[j];
                j++;
            }
        }
        return notes;
    }
}
