/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturetool;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dunning
 */
public class Course {
     
    /**
     * Make a new Course directory
     * @param describe name of course
     * @param location url of course
     * @return true if successful, false if unsuccessful
     */
    public static boolean makeCourse(String describe, String location) {
        File dir = new File("notes\\"+describe);
        if(dir.exists()){
            Logger.logInfo("\"notes\\"+describe+"\" directory already exists.");
            return false;
        }
        if(dir.mkdirs()) {
            Logger.logInfo("\"notes\\"+describe+"\" directory created.");
            location = cleanUrl(location);
            return Note.writeData(describe, "url", location);
        } else {
            Logger.logError("\"notes\\"+describe+"\" directory NOT created.");
        }
        return false;
    }
    
    /**
     * Changes regular youtube url into embedded url
     * @param url url to change
     * @return embedded url
     */
    public static String cleanUrl(String url) {
        return url.replace("watch", "embed");
    }
    
    /**
     * Delete the course
     * @param describe the course to delete
     * @return 
     */
    public static boolean deleteCourse(String describe) {
        File dir = new File("notes\\" + describe);
        String[] notes = dir.list();
        for(String note : notes) {
            new File("notes\\" + describe, note).delete();
        }
        boolean success = dir.delete();
        if(success) {
            Logger.logInfo("Course \"" + describe +"\" deleted.");
        } else {
            Logger.logInfo("Course \"" + describe +"\" NOT deleted.");
        }
        return success;
    }
    
    /**
     * Return the list of courses
     * @return 
     */
    public static String[] getCourses() {
        File dir = new File("notes");
        return dir.list();
    }
    
    /**
     * Retrieve the video url from the file notes/course/url
     * @param course
     * @return 
     */
    public static String getUrl(String course) {
        File url = new File("notes\\"+course, "url");
        String urlString = "";
        try(Scanner reader = new Scanner(url)) {
            //grab the first line from the file
            urlString = reader.nextLine();
        } catch(IOException e) {
            Logger.logError("could not load url for " + course);
        } catch(NoSuchElementException e) {
            Logger.logError("No url specified for " + course);
        }
        return urlString;
    }
    
    public static void updateUrl(String course, String newUrl) {
        newUrl = cleanUrl(newUrl);
        Note.writeData(course, "url", newUrl);
    }
    
    /**
     * Retrieve video ID from youtube url
     * @param url
     * @return 
     */
    public static String parseVideoId(String url) {
        Pattern p = Pattern.compile("v\\=.*\\&");
        Matcher m = p.matcher(url);
        if(m.find()) {
            String match = url.substring(m.start(), m.end());
            return match.substring(2, match.length()-1);
        } else {
            Logger.logError("Incorrect youtube playlist url: " + url);
            return "";
        }
    }
    
    /**
     * Retrieve playlist ID from youtube url
     * @param url
     * @return 
     */
    public static String parsePlaylistId(String url) {
        Pattern p = Pattern.compile("list\\=.*$");
        Matcher m = p.matcher(url);
        if(m.find()) {
            String match = url.substring(m.start(), m.end());
            System.out.println("match: " + match);
            return match.substring(5, match.length());
        } else {
            Logger.logError("Incorrect youtube playlist url: " + url);
            return "";
        }
    }
}
