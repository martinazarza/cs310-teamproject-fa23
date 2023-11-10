/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Absenteeism;
import edu.jsu.mcis.cs310.tas_fa23.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Xavier Bausley
 */
public class AbsenteeismDAO {
    private final DAOFactory daoFactory;
    
    public AbsenteeismDAO(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }
    public Absenteeism find(Employee employee, LocalDate localDate){
        
     
    }
    
}
