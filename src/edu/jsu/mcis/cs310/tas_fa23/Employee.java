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
public class Employee {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDateTime localDateTime;
    private Badge badge;
    private Department department;
    private Shift shift;
    private EmployeeType employeeType;
    public Employee(int id, String firstName, String middleName, String lastName, LocalDateTime localDateTime, Badge badge, Department department, Shift shift, EmployeeType employeeType){
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.localDateTime = localDateTime;
        this.badge = badge;
        this.department = department;
        this.shift = shift;
        this.employeeType = employeeType;
    }
    public long getID(){
        return id;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getMiddleName(){
       return middleName;
    }
    public String getLastName(){
        return lastName;
    }
    public LocalDateTime getLocalDateTime(){
        return localDateTime;
    }
    public Badge getBadge(){
        return badge;
    }
    public Department getDepartment(){
        return department;
    }
    public Shift getShift(){
        return shift;
    }
    public EmployeeType getEmployeeType(){
        return employeeType;
    }
    
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("ID#");
        s.append(id);
        s.append(":");
        s.append(lastName);
        s.append(",");
        s.append(firstName);
        s.append("");
        s.append(middleName);
        s.append("(#");
        //s.append(badgeid);
        s.append("), Type: ");
       // s.append(type);
       s.append(", Department: ");
       s.append(department.getDesc());
       s.append(", Active: ");
       //s.append(active.format(format));
       return s.toString();
        
    }
    
}
