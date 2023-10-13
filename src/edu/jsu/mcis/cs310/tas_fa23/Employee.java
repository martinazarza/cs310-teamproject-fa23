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
    private long ID;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDateTime localDateTime;
    private Badge badge;
    private Department department;
    private Shift shift;
    private EmployeeType employeeType;
    
    public long getID(){
        return ID;
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
    
    
}
