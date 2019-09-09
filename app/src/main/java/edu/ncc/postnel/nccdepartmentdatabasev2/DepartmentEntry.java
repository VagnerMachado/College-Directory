package edu.ncc.postnel.nccdepartmentdatabasev2;

/***************************************************************************************************
 ****** This is the Project 3 for the Mobile application Course in Nassau Community College  *******
 *
 * This application uses the Nassau Community College Department Directory to provide a table with
 * information to be parsed with SQLITE. This is DepartementEntry class that contains the code for the
 * setup and data for a department object.
 *
 * ***********************  Vagner Machado - Fall 2016 - N00820127  ********************************
 **************************************************************************************************/

public class DepartmentEntry {
    //instance data
    private long id;
    private String name;
    private String location;
    private String phone;

    /**
     * DepartmentEntry default constructor
     */
    public DepartmentEntry()
    {

    }

    /**
     * DepartmentEntry parameterized constructor
     * @param name - the dept name
     * @param location - the dept location
     * @param phone - the dept phone
     */
    public DepartmentEntry(String name, String location, String phone)
    {
        this.name = name;
        this.location = location;
        this.phone = phone;
    }
    /*
    The block of code below contains the getter and setter methods for the instance data
    Additionally, it contains a toString metod to print the dept objects and a comparator
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean equals(Object otherDept)
    {
        return this.id == ((DepartmentEntry)otherDept).id;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return id + ": " + name + " - " + location + " - " + phone;
    }
}

