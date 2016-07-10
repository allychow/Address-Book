/*
 * [Contact.java]
 * Description: Object class that stores the information in a contact. Used in conjunction with the AddressBookSoftware
 * Author: Allison Chow
 * January 11, 2016
 */

public class Contact {

  // declare attributes of a contact
  private String firstName;
  private String middleName;
  private String lastName;
  private String salutation;
  private String birthYear;
  private String birthMonth;
  private Integer birthDay;
  private String telephone;
  private String address;
  private String email;
  
  // basic constructor
  public Contact() {
    
    salutation = " ";
    firstName = " ";
    middleName = " ";
    lastName = " ";
    birthYear = " ";
    birthMonth = " ";
    birthDay = null;
    telephone = " ";
    email = " ";
    address = " ";
    
  }
  
  // accessor methods
  public String getFirstName() {
    
    return firstName;
    
  }
  
  public String getMiddleName() {
    
    return middleName;
    
  }
  
  public String getLastName() {
    
    return lastName;
    
  }
  
  public String getSalutation() {
    
    return salutation;
    
  }
  
  public String getBirthYear() {
    
    return birthYear;
    
  }
  
  public String getBirthMonth() {
    
    return birthMonth;
    
  }
  
  public Integer getBirthDay() {
    
    return birthDay;
    
  }
  
  public String getAddress() {
    
    return address;
    
  }
  
  public String getEmail() {
    
    return email;
    
  }
  
  public String getTelephone() {
    
    return telephone;
    
  }
  
  // modifier methods
  public void setFirstName(String a) {
    
    firstName = a;
    
  }
  
  public void setMiddleName(String a) {
    
    middleName = a;
    
  }
  
  public void setLastName(String a) {
    
    lastName = a;
    
  }
  
  public void setSalutation(String a) {
    
    salutation = a;
    
  }
  
  public void setBirthYear(String b) {
    
    birthYear = b;
    
  }
  
  public void setBirthMonth(String m) {
    
    birthMonth = m;
    
  }
  
  public void setBirthDay(Integer d) {
    
    birthDay = d;
    
  }
  
  public void setTelephone(String a) {
    
    telephone = a;
    
  }
  
  public void setAddress(String a) {
    
    address = a;
    
  }
  
  public void setEmail(String a) {
    
    email = a;
    
  }
  
} // end of Contact class
