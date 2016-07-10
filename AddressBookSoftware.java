/*
 * [AddressBookSoftware.java]
 * Description: An address book designed using object oriented programming that allows the user to store and manage contacts.
 *              The user is able to store as many contacts as needed using a dynamic array list, with a maximum of 
 *              ten different address books being used simultaneously. The program allows multiple address books to be
 *              open in case there are multiple users or cross referencing is needed. Functions include searching, 
 *              sorting, importing, exporting, and editing contacts.
 * Author: Allison Chow
 * January 11, 2016
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class AddressBookSoftware extends JFrame implements ActionListener {
  
  JPanel summedPan = new JPanel(); // final panel for everything to go on
  JPanel mainInfoPan = new JPanel(); // panel for the mainInfoPan info tab
  JPanel contactPan = new JPanel(); // panel for scrolling list of contacts
  
// tabs for the info editing and viewing
  JTabbedPane tabs = new JTabbedPane();
  JPanel basicTab = new JPanel();
  
// components for the menu
  JMenuBar menuBar = new JMenuBar();
  JMenu file = new JMenu("File");
  JMenuItem searchItem = new JMenuItem("Search");
  JMenuItem importItem = new JMenuItem("Import");
  JMenuItem exportItem = new JMenuItem("Export");
  JMenuItem newBookItem = new JMenuItem("New Book");
  
// a panel for the buttons that should always be visible (ie. save, delete, previous, next) and don't change (stagnant)
  JPanel stagButtonPan = new JPanel();
  
// buttons on the stagnant button panel
  JButton saveButton = new JButton("Save");
  JButton previousButton = new JButton("Previous");
  JButton nextButton = new JButton("Next");
  JButton addButton = new JButton("Add Contact");
  JButton deleteButton = new JButton("Delete Contact");
  JButton editButton = new JButton("Edit");
  
// components to get basic info for each contact
  String[] salutation = { " ", "Mr", "Miss", "Ms", "Mrs" };
  JComboBox<String> salutationList = new JComboBox<String>(salutation);
  JTextField firstName = new JTextField(35);
  JTextField middleName = new JTextField(35);
  JTextField lastName = new JTextField(35);
  JLabel salutationLabel = new JLabel("Salutation");
  JLabel firstLabel = new JLabel("First Name");
  JLabel middleLabel = new JLabel("Middle Name");
  JLabel lastLabel = new JLabel("Last Name");
  JPanel emailPan = new JPanel();
  JLabel emailLabel = new JLabel("Email Address: ");
  JTextField emailField = new JTextField(50);
  JPanel addressPan = new JPanel();
  JLabel addressLabel = new JLabel("Address: ");
  JTextField addressField = new JTextField(50);
  JPanel telephonePan = new JPanel();
  JLabel telephoneLabel = new JLabel("Telephone: ");
  JTextField telephoneField = new JTextField(50);
  JPanel birthdayPan = new JPanel();
  JLabel birthdayLabel = new JLabel("Birthday: ");
  JTextField yearField = new JTextField(8);
  String[] month = { " ", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
  JComboBox<String> monthList = new JComboBox<String>(month);
  Integer[] day = { null, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };
  JComboBox<Integer> dayList = new JComboBox<Integer>(day);
  
// panel to store the basic info
  JPanel basicPan = new JPanel();
  
// panel for the side list of all contacts within each book
  JPanel constViewPanel = new JPanel();
  
  int currentBook = 0; // tracks the address book currently being viewed
  
// components for side list of all contacts within each book
  ArrayList<Book> bookNames = new ArrayList<Book>();
  DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<Object>(bookNames.toArray());
  JComboBox<Object> bookList = new JComboBox<Object>(model);
  DefaultListModel<Object> contact = new DefaultListModel<Object>();
  JList<Object> list = new JList<Object>(contact);
  JScrollPane scrollContact = new JScrollPane(list);
  
// frame for pop up dialogue 
  JFrame pop = new JFrame();
  
// boolean to track whether the user is editing a contact or not (will tell the program whether to store the info in a new contact, or an existing contact)
  boolean editing = false;
  
// an array to store the number of contacts in each address book (max. 10 address books at once)
  int[] i = new int[10];
  
// tracks the current contact being viewed  
  int current = 0;
  
// components for the searching function
  JPanel searchingPan = new JPanel();
  JLabel searchingLabel = new JLabel("Field to Search By:");
  JTextField searchingField = new JTextField(); // to take user input
  String[] field = { "First Name", "Middle Name", "Last Name", "Phone Number" }; // methods to search by
  JComboBox<String> searchingFields = new JComboBox<String>(field); // stores the methods to search by 
  JButton enterButton = new JButton("Enter"); 
// components to display search results if found
  DefaultListModel<Object> contactFound = new DefaultListModel<Object>(); 
  JList<Object> listFound = new JList<Object>(contactFound);
  JScrollPane scrollContactFound = new JScrollPane(listFound);
  
// components for choosing how to sort and view the existing contacts
  String[] view = { "First Name A-Z", "First Name Z-A", "Last Name A-Z", "Last Name Z-A" };
  JComboBox<String> viewField = new JComboBox<String>(view);
  JLabel viewLabel = new JLabel("View By: ");
  
  /*
   * constructor
   */
  public AddressBookSoftware() {
    
    setTitle ("Address Book");
    setSize(1200, 750);
    setResizable(false);
    
    summedPan.setLayout(new BoxLayout(summedPan, BoxLayout.PAGE_AXIS));
    
    contactPan.setLayout(new FlowLayout());
    
    // ---------------------------------------- designing the file/menu bar ---------------------------------------- //
    
    file.setMnemonic(KeyEvent.VK_A);
    file.getAccessibleContext().setAccessibleDescription(" ");
    
    searchItem.setPreferredSize(new Dimension(100, 30));
    importItem.setPreferredSize(new Dimension(100, 30));
    exportItem.setPreferredSize(new Dimension(100, 30));
    newBookItem.setPreferredSize(new Dimension(100, 30));
    file.setPreferredSize(new Dimension(60, 30));
    
    file.add(searchItem);
    file.add(importItem);
    file.add(exportItem);
    file.add(newBookItem);
    
    menuBar.add(file);
    
    newBookItem.addActionListener(this);
    searchItem.addActionListener(this);
    exportItem.addActionListener(this);
    importItem.addActionListener(this);
    
    // end of file/menu bar design
    
    // --------------------------------- designing the main info panels/components --------------------------------- //
    
    // set layouts
    mainInfoPan.setLayout(new BoxLayout(mainInfoPan, BoxLayout.PAGE_AXIS));
    
    GridLayout layout2 = new GridLayout(2, 4, 5, 0);
    basicPan.setLayout(layout2);
    
    FlowLayout layout1 = new FlowLayout();
    emailPan.setLayout(layout1);
    addressPan.setLayout(layout1);
    telephonePan.setLayout(layout1);
    birthdayPan.setLayout(layout1);
    
    basicTab.setLayout(new BoxLayout(basicTab, BoxLayout.PAGE_AXIS));
    
    // change the size of components and colors of drop down menus
    mainInfoPan.setPreferredSize(new Dimension(800, 600));
    emailField.setPreferredSize(new Dimension(100, 50));
    addressField.setPreferredSize(new Dimension(100, 50));
    telephoneField.setPreferredSize(new Dimension(100, 50));
    dayList.setPreferredSize(new Dimension(60, 50));
    monthList.setPreferredSize(new Dimension(100, 50));
    yearField.setPreferredSize(new Dimension(60, 50));
    
    monthList.setBackground(Color.WHITE);
    dayList.setBackground(Color.WHITE);
    salutationList.setBackground(Color.WHITE);
    
    // add components to panels
    basicPan.add(salutationLabel);
    basicPan.add(firstLabel);
    basicPan.add(middleLabel);
    basicPan.add(lastLabel);
    
    basicPan.add(salutationList);
    basicPan.add(firstName);
    basicPan.add(middleName);
    basicPan.add(lastName);
    
    emailPan.add(emailLabel);
    emailPan.add(emailField);
    addressPan.add(addressLabel);
    addressPan.add(addressField);
    telephonePan.add(telephoneLabel);
    telephonePan.add(telephoneField);
    birthdayPan.add(birthdayLabel);
    birthdayPan.add(yearField);
    birthdayPan.add(monthList);
    birthdayPan.add(dayList);
    
    basicTab.add(basicPan);
    basicTab.add(emailPan);
    basicTab.add(addressPan);
    basicTab.add(telephonePan);
    basicTab.add(birthdayPan);
    
    // add the created panels to the tab layout
    tabs.addTab("Basic Info", basicTab);
    tabs.setMnemonicAt(0, KeyEvent.VK_1);
    
    // end of main info panels/components design
    
    // ---------------------------------- designing the viewing contacts panel ------------------------------------- //
    
    // design the components
    constViewPanel.setLayout(new BoxLayout(constViewPanel, BoxLayout.PAGE_AXIS));
    constViewPanel.setPreferredSize(new Dimension(200, 600));
    bookList.setPreferredSize(new Dimension(200, 25));
    scrollContact.setPreferredSize(new Dimension(200, 550));
    scrollContact.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setLayoutOrientation(JList.VERTICAL);
    ListSelectionModel listSelectionModel = list.getSelectionModel();
    listSelectionModel.addListSelectionListener(new SharedListSelectionHandler()); // any selections made on the list will go through a new class to listen to the actions
    
    // add an action listener to the drop down menu storing the names of the address books
    bookList.addActionListener(this); 
    
    // add components to panel
    constViewPanel.add(bookList);
    constViewPanel.add(scrollContact);
    
    // end of viewing contacts panel 
    
    // --------------------------------------- designing the button panel ------------------------------------------ //
    
    // set layout of the panel
    stagButtonPan.setLayout(new FlowLayout());
    
    // add action listeners to the components
    saveButton.addActionListener(this);
    addButton.addActionListener(this);
    deleteButton.addActionListener(this);
    previousButton.addActionListener(this);
    nextButton.addActionListener(this);
    editButton.addActionListener(this);
    viewField.addActionListener(this);
    
    // add components to the panel
    stagButtonPan.add(viewLabel);
    stagButtonPan.add(viewField);
    stagButtonPan.add(addButton);
    stagButtonPan.add(deleteButton);
    stagButtonPan.add(saveButton);
    stagButtonPan.add(previousButton);
    stagButtonPan.add(nextButton);
    stagButtonPan.add(editButton);
    
    // end of designing the button panel
    
    // ------------------------------------- designing the searching panel ----------------------------------------- //
    
    // design the scrolling panel
    scrollContactFound.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollContactFound.setPreferredSize(new Dimension(750, 400));
    listFound.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listFound.setLayoutOrientation(JList.VERTICAL);
    
    // design the searching panel
    enterButton.addActionListener(this);
    searchingField.setPreferredSize(new Dimension(600, 30));
    searchingField.setLayout(new FlowLayout());
    searchingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    searchingFields.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // add components to the panel
    searchingPan.add(searchingLabel);
    searchingPan.add(searchingFields);
    searchingPan.add(searchingField);
    searchingPan.add(enterButton);
    searchingPan.add(scrollContactFound);
    
    // when the user double clicks on a found contact, the mouse listener handles the action and performs a function
    listFound.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
          // double-click detected
          int index = listFound.locationToIndex(evt.getPoint()); // finds the index at which the double click was detected
          for (int k = 0; k < i[currentBook]; k++) {
            if (contactFound.getElementAt(index).equals(contact.getElementAt(k))) { // searches through the list of contacts to find a match to what was found
              list.setSelectedIndex(k); // displays the contact's info on the main panel
              if (k == i[currentBook]-1) {
                nextButton.setEnabled(false); 
              } else if (k == 0) {
                previousButton.setEnabled(false);
              }
            }
          }
        } 
      }
    });
    
    // end of designing the searching panel
    
    // add all the panels created to the main panels
    mainInfoPan.add(tabs);
    contactPan.add(constViewPanel);
    contactPan.add(mainInfoPan);
    summedPan.add(contactPan);
    summedPan.add(stagButtonPan);
    
    // add the whole panel to the frame
    add(summedPan);
    
    setJMenuBar(menuBar); // set the menu bar from default to the created one
    
    // disable all fields and buttons at initialization until the user creates a new address book
    firstName.setEnabled(false);
    middleName.setEnabled(false);
    lastName.setEnabled(false);
    emailField.setEnabled(false);
    addressField.setEnabled(false);
    telephoneField.setEnabled(false);
    monthList.setEnabled(false);
    yearField.setEnabled(false);
    dayList.setEnabled(false);
    salutationList.setEnabled(false);
    deleteButton.setEnabled(false);
    addButton.setEnabled(false);
    nextButton.setEnabled(false);
    previousButton.setEnabled(false);
    saveButton.setEnabled(false);
    editButton.setEnabled(false);
    
    setVisible(true);
    
    JOptionPane.showMessageDialog(pop, "Welcome to your address book! To begin, click File on the menu bar, then New Book to open a brand new address book to \nstore your contact information in, or Import to begin with info from an existing file.", "Start", JOptionPane.PLAIN_MESSAGE);
    
  } // end of constructor
  
  /*
   * Creates a class to handle events on the JList rather than implementing the standard action listener class.
   */
  class SharedListSelectionHandler implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
      
      ListSelectionModel lsm = (ListSelectionModel)e.getSource(); // create a list selection model to read selections from the list
      
      if (!lsm.isSelectionEmpty()) { // if an item has been selected
        
        // finds the bounds of the list
        int minIndex = lsm.getMinSelectionIndex();
        int maxIndex = lsm.getMaxSelectionIndex();
        
        for (int o = minIndex; o <= maxIndex; o++) {
          if (lsm.isSelectedIndex(o)) {
            
            if (o == 0) {
              previousButton.setEnabled(false); // doesn't allow the user to use the previous button if there are no contacts to be found before the current
            } else if (o == i[currentBook] - 1) {
              nextButton.setEnabled(false); // doesn't allow the user to use the next button if there are no contacts to be found after the current
            }
            
            System.out.println(o);
            // displays the contact's info if the index on the list matches the index of the contact in the address book
            salutationList.setSelectedItem(bookNames.get(currentBook).getContact().get(o).getSalutation());
            firstName.setText(bookNames.get(currentBook).getContact().get(o).getFirstName());
            middleName.setText(bookNames.get(currentBook).getContact().get(o).getMiddleName());
            lastName.setText(bookNames.get(currentBook).getContact().get(o).getLastName());
            telephoneField.setText(bookNames.get(currentBook).getContact().get(o).getTelephone().toString());
            emailField.setText(bookNames.get(currentBook).getContact().get(o).getEmail());
            addressField.setText(bookNames.get(currentBook).getContact().get(o).getAddress());
            yearField.setText(bookNames.get(currentBook).getContact().get(o).getBirthYear());
            monthList.setSelectedItem(bookNames.get(currentBook).getContact().get(o).getBirthMonth());
            dayList.setSelectedItem(bookNames.get(currentBook).getContact().get(o).getBirthDay());
            
            current = o; // matches the current contact with what is being viewed in case an edit is to be made after
            
            if (current < i[currentBook] - 1) {
              nextButton.setEnabled(true);
            } // enable the next button if there are more contacts after the current to be viewed
            
            if (current > 0) {
              previousButton.setEnabled(true);
            } // enable the previous button if there are more contacts before the current to be viewed
            
          }
        }
      }
    }
  } // end of SharedListSelectionHandler class
  
  /*
   * Creates a method to handle events performed on buttons and combo boxes.
   */
  public void actionPerformed(ActionEvent event) {
    
    String command = event.getActionCommand();
    
    if (event.getSource() == newBookItem) {
      
      String nameBook = (String)JOptionPane.showInputDialog(pop, "Name of new address book:", "New Book", JOptionPane.PLAIN_MESSAGE); // gets the name of the new contact book from the user
      
      model.addElement(nameBook); // adds the name of the book to the list of books
      
      bookNames.add(new Book()); // creates a new book which will store an arraylist of contacts
      currentBook = bookNames.size() - 1;
      
      // allow the user to add a new contact, but not delete one yet as there are none to delete
      addButton.setEnabled(true); 
      deleteButton.setEnabled(false);
      
    } else if (event.getSource() == searchItem) {
      
      // displays a new frame for the user to search for a contact
      JFrame searching = new JFrame("Search");
      searching.setSize(800,600);
      searching.setResizable(false);
      searching.getContentPane().add(searchingPan);
      contactFound.clear();
      searching.setVisible(true);
      
    } else if (event.getSource().equals(bookList)) {
      
      currentBook = bookList.getSelectedIndex(); // determines the current book being viewed
      
      if (i[currentBook] == 0) {
        deleteButton.setEnabled(false);
      } // do not allow the user to use the delete button if there are no contacts in the book
      
      contact.removeAllElements(); // clear the list of contacts
      
      // add the contacts stored in the current book to the list
      for (int b = 0; b < i[currentBook]; b++) {
        contact.addElement(bookNames.get(currentBook).getContact().get(b).getFirstName() + " " + bookNames.get(currentBook).getContact().get(b).getLastName());
      }
      
    } else if (event.getSource().equals(exportItem)) {
      
      // call the exporting method to store all contact info into a new file
      try {
        exporting();
      } catch (Exception e) { };
      
    } else if (event.getSource().equals(importItem)) {
      
      // call the importing method to read contact info from a file
      try {
        importing();
      } catch (Exception e) { };
      
    } else if (event.getSource().equals(viewField)) {
      
      // re-sort the list of contacts each time the field of which to view is changed
      sort();
      
    }
    
    if (command.equals("Save")) {
      
      if (editing == false) { // a new contact is being created
        
        bookNames.get(currentBook).addContact(new Contact()); // create a new contact to be added to the current book and store all of the info from the fields
        bookNames.get(currentBook).getContact().get(i[currentBook]).setFirstName(firstName.getText().replaceAll("\\s+",""));
        bookNames.get(currentBook).getContact().get(i[currentBook]).setMiddleName(middleName.getText().replaceAll("\\s+",""));
        bookNames.get(currentBook).getContact().get(i[currentBook]).setLastName(lastName.getText().replaceAll("\\s+",""));
        bookNames.get(currentBook).getContact().get(i[currentBook]).setEmail(emailField.getText().replaceAll("\\s+",""));
        bookNames.get(currentBook).getContact().get(i[currentBook]).setAddress(addressField.getText().replaceAll("\\s+",""));
        bookNames.get(currentBook).getContact().get(i[currentBook]).setSalutation(salutationList.getSelectedItem().toString());
        bookNames.get(currentBook).getContact().get(i[currentBook]).setTelephone(telephoneField.getText().replaceAll("\\s+","").replaceAll("\\-","").replaceAll("\\(", "").replaceAll("\\)",""));
        bookNames.get(currentBook).getContact().get(i[currentBook]).setBirthYear(yearField.getText().replaceAll("\\s+",""));
        bookNames.get(currentBook).getContact().get(i[currentBook]).setBirthMonth(monthList.getSelectedItem().toString());
        bookNames.get(currentBook).getContact().get(i[currentBook]).setBirthDay((Integer)(dayList.getSelectedItem()));
        
        current = i[currentBook]; // matches the current viewed contact to the latest created contact
        contact.addElement(bookNames.get(currentBook).getContact().get(i[currentBook]).getFirstName() + " " + bookNames.get(currentBook).getContact().get(i[currentBook]).getLastName()); // add the contact to the list of all contacts
        i[currentBook]++; // create a new space for the next new contact to be stored
        
      } else if (editing) {
        
        // saves the new info for the contact if the user pressed the editing button first
        // removes all spaces, unnecessary dashes and brackets from the telephone field in order to maintain consistency in storage
        // removes all spaces from the name (consistency in searching and sorting) and email address fields (not acceptable to have a space in an email) 
        bookNames.get(currentBook).getContact().get(current).setFirstName(firstName.getText().replaceAll("\\s+",""));
        bookNames.get(currentBook).getContact().get(current).setMiddleName(middleName.getText().replaceAll("\\s+",""));
        bookNames.get(currentBook).getContact().get(current).setLastName(lastName.getText().replaceAll("\\s+",""));
        bookNames.get(currentBook).getContact().get(current).setEmail(emailField.getText().replaceAll("\\s+",""));
        bookNames.get(currentBook).getContact().get(current).setAddress(addressField.getText());
        bookNames.get(currentBook).getContact().get(current).setSalutation(salutationList.getSelectedItem().toString());
        bookNames.get(currentBook).getContact().get(current).setTelephone(telephoneField.getText().replaceAll("\\s+","").replaceAll("\\-","").replaceAll("\\(", "").replaceAll("\\)",""));
        bookNames.get(currentBook).getContact().get(current).setBirthYear(yearField.getText().replaceAll("\\s+",""));
        bookNames.get(currentBook).getContact().get(current).setBirthMonth(monthList.getSelectedItem().toString());
        bookNames.get(currentBook).getContact().get(current).setBirthDay((Integer)dayList.getSelectedItem());
        
      }
      
      // do not allow any more changes to be made
      firstName.setEditable(false);
      middleName.setEditable(false);
      lastName.setEditable(false);
      emailField.setEditable(false);
      addressField.setEditable(false);
      telephoneField.setEditable(false);
      monthList.setEnabled(false);
      yearField.setEditable(false);
      dayList.setEnabled(false);
      dayList.setSelectedItem(false);
      salutationList.setEnabled(false);
      deleteButton.setEnabled(true);
      editButton.setEnabled(true);
      editing = false; // exit editing mode
      
      // only allow the user to use the previous button if there are multiple contacts in the address book
      if (i[currentBook] > 1) {
        
        previousButton.setEnabled(true);
        
      }
      
      sort(); // resort the address book after changes have been made or a new contact has been added
      
    } else if (command.equals("Add Contact")) {
      
      // clear all fields and (re-)enable all
      saveButton.setEnabled(true);
      firstName.setEnabled(true);
      firstName.setEditable(true);
      firstName.setText(" ");
      middleName.setEnabled(true);
      middleName.setEditable(true);
      middleName.setText(" ");
      lastName.setEnabled(true);
      lastName.setEditable(true);
      lastName.setText(" ");
      emailField.setEnabled(true);
      emailField.setEditable(true);
      emailField.setText(" ");
      addressField.setEnabled(true);
      addressField.setEditable(true);
      addressField.setText(" ");
      telephoneField.setEnabled(true);
      telephoneField.setEditable(true);
      telephoneField.setText(" ");
      monthList.setEnabled(true);
      monthList.setSelectedItem(" ");
      yearField.setEnabled(true);
      yearField.setEditable(true);
      yearField.setText(" ");
      dayList.setEnabled(true);
      dayList.setSelectedItem(null);
      salutationList.setEnabled(true);
      salutationList.setSelectedItem(" ");
      
    } else if (command.equals("Delete Contact")) {
      
      bookNames.get(currentBook).deleteContact(current); // removes the contact from the address book
      contact.remove(current); // removes the contact from the list on the scrolling panel
      // reset all fields
      firstName.setEditable(false);
      firstName.setText(" ");
      middleName.setEditable(false);
      middleName.setText(" ");
      lastName.setEditable(false);
      lastName.setText(" ");
      emailField.setEditable(false);
      emailField.setText(" ");
      addressField.setEditable(false);
      addressField.setText(" ");
      telephoneField.setEditable(false);
      telephoneField.setText(" ");
      monthList.setEnabled(false);
      monthList.setSelectedItem(" ");
      yearField.setEditable(false);
      yearField.setText(" ");
      dayList.setEnabled(false);
      dayList.setSelectedItem(false);
      salutationList.setEnabled(false);
      salutationList.setSelectedItem(" ");
      i[currentBook]--; // decrease the number of contacts in the book by one
      list.revalidate(); // update the list of contacts
      
      // only allow use of these buttons if there are multiple contacts still stored in the address book
      if (i[currentBook] == 0) { 
        
        deleteButton.setEnabled(false);
        editButton.setEnabled(false);
        
      } else {
        
        deleteButton.setEnabled(true);
        
      }
      
      if (i[currentBook] == 1) {
        
        previousButton.setEnabled(false);
        
      }
      
      
    } else if (command.equals("Previous")) {
      
      if (current > 0) {
        
        current--;
        list.setSelectedIndex(current); // displays the contact that was stored previous to the current one being viewed
        if (current == 0) {
          previousButton.setEnabled(false);
        } // only allow use of the button if there are still contacts previous to the current
        
      }
      
    } else if (command.equals("Next")) {
      
      if (current < i[currentBook]) {
        
        current++;
        list.setSelectedIndex(current); // displays the contact that was stored after the current one being viewed
        if (current == i[currentBook]-1) {
          nextButton.setEnabled(false);
        } // only allow use of the button if there are still contacts after the current
        
      }
      
    } else if (command.equals("Enter")) {
      
      contactFound.clear(); // clears the list of found contacts every time a new search is made
      
      boolean foundBoolean = false; // begin with saying none have been found
      
      if (searchingFields.getSelectedItem().equals("First Name")) { // search by first name (case sensitive)
        
        for (int k = 0; k < i[currentBook]; k++) { // using the sequential search method of comparing the searched field to every contact's matching field
          
          if (bookNames.get(currentBook).getContact().get(k).getFirstName().equals(searchingField.getText().replaceAll("\\s",""))) {
            
            // add the found name to the list of found elements
            contactFound.addElement(bookNames.get(currentBook).getContact().get(k).getFirstName() + " " + bookNames.get(currentBook).getContact().get(k).getLastName());
            foundBoolean = true; 
            
          }
          
        }
        
      } else if (searchingFields.getSelectedItem().equals("Last Name")) {
        
        for (int k = 0; k < i[currentBook]; k++) {
          
          if (bookNames.get(currentBook).getContact().get(k).getLastName().equals(searchingField.getText().replaceAll("\\s",""))) {
            
            contactFound.addElement(bookNames.get(currentBook).getContact().get(k).getFirstName() + " " + bookNames.get(currentBook).getContact().get(k).getLastName());
            foundBoolean = true;
            
          }
          
        }
        
      } else if (searchingFields.getSelectedItem().equals("Middle Name")) {
        
        for (int k = 0; k < i[currentBook]; k++) {
          
          if (bookNames.get(currentBook).getContact().get(k).getMiddleName().equals(searchingField.getText().replaceAll("\\s",""))) {
            
            contactFound.addElement(bookNames.get(currentBook).getContact().get(k).getFirstName() + " " + bookNames.get(currentBook).getContact().get(k).getLastName());
            foundBoolean = true;
            
          }
          
        }
        
      } else if (searchingFields.getSelectedItem().equals("Phone Number")) {
        
        for (int k = 0; k < i[currentBook]; k++) {
          
          if (bookNames.get(currentBook).getContact().get(k).getTelephone().equals(searchingField.getText().replaceAll("\\s","").replaceAll("\\-","").replaceAll("\\(","").replaceAll("\\)",""))) {
            
            contactFound.addElement(bookNames.get(currentBook).getContact().get(k).getFirstName() + " " + bookNames.get(currentBook).getContact().get(k).getLastName());
            foundBoolean = true;
            
          }
          
        }
        
      }
      
      if (foundBoolean == false) {
        
        JOptionPane.showMessageDialog(pop, "No results found", "Search", JOptionPane.PLAIN_MESSAGE);
        
      } // if nothing has been found to match what was searched, create a pop up message to the user telling them that nothing was found
      
      
    } else if (command.equals("Edit")) {
      
      // re-enable all of the fields and buttons
      saveButton.setEnabled(true);
      firstName.setEnabled(true);
      firstName.setEditable(true);
      middleName.setEnabled(true);
      middleName.setEditable(true);
      lastName.setEnabled(true);
      lastName.setEditable(true);
      emailField.setEnabled(true);
      emailField.setEditable(true);
      addressField.setEnabled(true);
      addressField.setEditable(true);
      telephoneField.setEnabled(true);
      telephoneField.setEditable(true);
      monthList.setEnabled(true);
      yearField.setEnabled(true);
      yearField.setEditable(true);
      dayList.setEnabled(true);
      salutationList.setEnabled(true);
      editing = true; // the boolean identifies that the new info should be stored in the current contact, not a new contact
      
    }
  } // end of actionListener method
  
  /*
   * A method that sorts the contact list by varying fields using the insertion sort method. This method begins with 
   * the second element in the array, compares it to everything before it, and swaps the two each time the selection 
   * statement is met. After the element has been passed to the left as many times as needed, the element of one index 
   * greater goes through the same process until all elements have undergone the swapping method.
   */
  public void sort() {
    
    // temporary variables to store the current contact index's info
    int j;
    Contact temp;
    
    if (viewField.getSelectedItem().equals("First Name A-Z")) { // sort by first name lexographically
      
      for (int p = 1; p < i[currentBook]; p++) {
        
        temp = bookNames.get(currentBook).getContact().get(p);
        j = p; // stores the current contact's info into the temporary placeholders
        
        while (j > 0 && (bookNames.get(currentBook).getContact().get(j-1).getFirstName()).compareTo(temp.getFirstName()) > 0) {
          
          bookNames.get(currentBook).getContact().set(j, bookNames.get(currentBook).getContact().get(j-1));
          
          j--;
          
        } // if the contact comes before the one previous in the alphabet, it goes to check the contact back one more previous until it reaches the beginning of the array
        
        bookNames.get(currentBook).getContact().set(j, temp); // swaps the contacts
        
      }
      
    } else if (viewField.getSelectedItem().equals("First Name Z-A")) { // sort by first name lexographically backwards
      
      for (int p = 1; p < i[currentBook]; p++) {
        
        temp = bookNames.get(currentBook).getContact().get(p);
        j = p;
        
        while (j > 0 && (bookNames.get(currentBook).getContact().get(j-1).getFirstName().compareTo(temp.getFirstName()) < 0)) {
          
          bookNames.get(currentBook).getContact().set(j, bookNames.get(currentBook).getContact().get(j-1));
          
          j--;
          
        }
        
        bookNames.get(currentBook).getContact().set(j, temp);
        
      }
      
    } else if (viewField.getSelectedItem().equals("Last Name A-Z")) { // sort by last name lexographically
      
      for (int p = 1; p < i[currentBook]; p++) {
        
        temp = bookNames.get(currentBook).getContact().get(p);
        j = p;
        
        while (j > 0 && (bookNames.get(currentBook).getContact().get(j-1).getLastName().compareTo(temp.getLastName()) > 0)) {
          
          bookNames.get(currentBook).getContact().set(j, bookNames.get(currentBook).getContact().get(j-1));
          
          j--;
          
        }
        
        bookNames.get(currentBook).getContact().set(j, temp);
        
      }
      
    } else if (viewField.getSelectedItem().equals("Last Name Z-A")) { // sort by last name lexographically backwards
      
      for (int p = 1; p < i[currentBook]; p++) {
        
        temp = bookNames.get(currentBook).getContact().get(p);
        j = p;
        
        while (j > 0 && (bookNames.get(currentBook).getContact().get(j-1).getLastName().compareTo(temp.getLastName()) < 0)) {
          
          bookNames.get(currentBook).getContact().set(j, bookNames.get(currentBook).getContact().get(j-1));
          
          j--;
          
        }
        
        bookNames.get(currentBook).getContact().set(j, temp);
      }
      
    }
    
    // removes all the previously unsorted contacts from the scrolling list
    contact.removeAllElements();
    
    // adds all of the sorted contacts back to the scrolling list
    for (int d = 0; d < i[currentBook]; d++) {
      
      contact.addElement(bookNames.get(currentBook).getContact().get(d).getFirstName() + " " + bookNames.get(currentBook).getContact().get(d).getLastName());
      
    }
    
  } // end of sort method
  
  /*
   * A method to export the current address book's contact info to a new document
   */
  public void exporting() throws Exception {
    
    // creates a new file and writer for the info to be stored with
    File myFile = new java.io.File(model.getSelectedItem().toString());
    PrintWriter output = new PrintWriter(myFile); 
    
    // goes through all of the contacts and prints the info in the same format information would be imported in
    for (int w = 0; w < i[currentBook]; w++) {
      
      output.println(bookNames.get(currentBook).getContact().get(w).getSalutation());
      output.println(bookNames.get(currentBook).getContact().get(w).getFirstName());
      output.println(bookNames.get(currentBook).getContact().get(w).getMiddleName());
      output.println(bookNames.get(currentBook).getContact().get(w).getLastName());
      output.println(bookNames.get(currentBook).getContact().get(w).getEmail());
      output.println(bookNames.get(currentBook).getContact().get(w).getAddress());
      output.println(bookNames.get(currentBook).getContact().get(w).getTelephone());
      output.println(bookNames.get(currentBook).getContact().get(w).getBirthYear());
      output.println(bookNames.get(currentBook).getContact().get(w).getBirthMonth());
      output.println(bookNames.get(currentBook).getContact().get(w).getBirthDay());
    
    }
    
    output.close(); // close the printer
    
    JOptionPane.showMessageDialog(pop, "Export Complete", "Export", JOptionPane.PLAIN_MESSAGE); // display a message to the user stating that the export was successful
    
  } // end of exporting method
  
  /*
   * A method to import contact info from an existing file in a specific format to match that of the exporting function.
   */
  public void importing() throws Exception {
    
    // obtains the name of the text file from the user and creates a variable to store the file to be read from
    String fileNameImport = (String)JOptionPane.showInputDialog(pop, "*info must be stored in the form: \nSalutation \nFirst Name \nMiddle Name \nLast Name \nEmail \nAddress \nTelephone \nBirth Year (YYYY) \nBirth Month (Xxxx) \nBirth Day \neach on their own line (any fields not known for a contact should be represented by an empty line)* \n Name of file to import: (case sensitive in the form xxx.txt)", "Import", JOptionPane.PLAIN_MESSAGE);
    File file = new java.io.File(fileNameImport);
    model.addElement(fileNameImport); // adds the name of the file to a new address book
    bookNames.add(new Book());
    currentBook = bookNames.size();
    
    Scanner input = new Scanner(file); // create a scanner to read from the file
    
    while (input.hasNext()) { // will continue so long as there is information to be read in the file
      
      bookNames.get(currentBook).addContact(new Contact()); // adds a new contact to the address book, then stores and reads the data
      bookNames.get(currentBook).getContact().get(i[currentBook]).setSalutation(input.nextLine().replaceAll("\\s+",""));
      bookNames.get(currentBook).getContact().get(i[currentBook]).setFirstName(input.nextLine().replaceAll("\\s+",""));
      bookNames.get(currentBook).getContact().get(i[currentBook]).setMiddleName(input.nextLine().replaceAll("\\s+",""));
      bookNames.get(currentBook).getContact().get(i[currentBook]).setLastName(input.nextLine().replaceAll("\\s+",""));
      bookNames.get(currentBook).getContact().get(i[currentBook]).setEmail(input.nextLine().replaceAll("\\s+",""));
      bookNames.get(currentBook).getContact().get(i[currentBook]).setAddress(input.nextLine());
      bookNames.get(currentBook).getContact().get(i[currentBook]).setTelephone(input.nextLine().replaceAll("\\s+","").replaceAll("\\-","").replaceAll("\\(", "").replaceAll("\\)",""));
      bookNames.get(currentBook).getContact().get(i[currentBook]).setBirthYear(input.nextLine().replaceAll("\\s+",""));
      bookNames.get(currentBook).getContact().get(i[currentBook]).setBirthMonth(input.nextLine().replaceAll("\\s+",""));
      bookNames.get(currentBook).getContact().get(i[currentBook]).setBirthDay(Integer.parseInt(input.nextLine()));
      
      // adds the contact to the scrolling list so it can be viewed later
      contact.addElement(bookNames.get(currentBook).getContact().get(i[currentBook]).getFirstName() + " " + bookNames.get(currentBook).getContact().get(i[currentBook]).getLastName());
      
    }
    
    input.close(); // close the scanner
    
  } // end of importing method
  
  /*
   * Main method to run the program and create the GUI
   */
  public static void main(String args[]) throws Exception {
    new AddressBookSoftware();
  } // end of main method
} // end of AddressBookSoftware class

