/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23;

/**
 *
 * @author Xavier Bausley
 */

import java.time.*;
import java.time.format.DateTimeFormatter;
public class Employee {
    
    private int id;
    private LocalDateTime active;
    private String firstname;
    private String middlename;
    private String lastname;
    private String badgeid;
    private String workstation;
    private String shiftType;
    private Badge badge;
    private Department department;
    private Shift shift;
    private EmployeeType type;

    public Employee(int id, LocalDateTime active, String firstname, String middlename, String lastname, Badge badge, Department department, Shift shift, EmployeeType type) {
        this.id = id;
        this.active = active;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.badgeid = badge.getId();
        this.department = department;
        this.workstation = department.getDescription();
        this.shiftType = shift.getDescription();
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public LocalDateTime getActive() {
        return active;
    }

    public Badge getBadge() {
        return badge;
    }

    public Department getDepartment() {
        return department;
    }

    public Shift getShift() {
        return shift;
    }

    public EmployeeType getEmployeeType() {
        return type;
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        
        s
            .append("ID #")
            .append(id)
            .append(": ")
            .append(lastname)
            .append(", ")
            .append(firstname)
            .append(" ")
            .append(middlename)
            .append(" (#")
            .append(badgeid)
            .append("), Type: ")
            .append(type)
            .append(", Department: ")
            .append(department.getDescription())
            .append(", Active: ")
            .append(active.format(format));

        return s.toString();
    }
}