/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author User
 */
public class Absenteeism {
    private Employee employee;
    private LocalDate localDate;
    private BigDecimal absentPercent;
    
    public Absenteeism(Employee employee, LocalDate localDate, BigDecimal absentPercent){
        this.employee = employee;
        this.localDate = localDate;
        this.absentPercent = absentPercent;
    }
    public Employee getEmployee(){
        return employee;
    }
    public LocalDate getLocalDate(){
        return localDate;
    }
    public BigDecimal getAbsentPercent(){
        return absentPercent;
    }
    @Override
    public String toString(){
        StringBuilder s =new StringBuilder();
        s.append(employee.toString());
        s.append("Local Date: ");
        s.append(localDate);
        s.append("Absentee Percentage: ");
        s.append(absentPercent);
        return s.toString();
        
    }
    
}
