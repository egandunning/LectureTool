/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturetool;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author dunning
 */
public class Person {
    private final SimpleStringProperty fname = new SimpleStringProperty("");
    private final SimpleStringProperty lname = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    
    public Person() {
        this("","","");
    }
    
    public Person(String firstname, String lastname, String emailaddr) {
        setFname(firstname);
        setLname(lastname);
        setEmail(emailaddr);
    }
    
    public void setFname(String firstname) {
        fname.set(firstname);
    }
    
    public void setLname(String lastname) {
        lname.set(lastname);
    }
    
    public void setEmail(String emailaddr) {
        email.set(emailaddr);
    }
    
    public String getFname() {
        return fname.get();
    }
    
    public String getLname() {
        return lname.get();
    }
    
    public String getEmail() {
        return email.get();
    }
}
