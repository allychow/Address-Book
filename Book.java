/*
 * [Book.java]
 * Description: Object to store multiple contacts. Used in conjunction with AddressBookSoftware
 * Author: Allison Chow
 * January 11, 2016
 */

import java.util.*;
public class Book {
  
  // creates a dynamic array list to store an address book's contacts
  private ArrayList<Contact> contact = new ArrayList<Contact>();
  
  // basic constructor
  public Book() {
    
  }
  
  // add a contact to the array list
  public void addContact(Contact c) {
    
    contact.add(c);
    
  }
  
  // delete a contact from the array list
  public void deleteContact(int c) {
    
    contact.remove(c);
    
  }
  
  // accessor method to retrieve the current array list of contacts within the book
  public ArrayList<Contact> getContact() {
    
    return contact;
  
  }
  
} // end of Book class