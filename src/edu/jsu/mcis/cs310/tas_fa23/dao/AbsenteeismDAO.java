/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Absenteeism;
import edu.jsu.mcis.cs310.tas_fa23.Employee;
import java.math.BigDecimal;
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
        Absenteeism absenteeism = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try{
            Connection connection = daoFactory.getConnection();
            String query = "SELECT * FROM absenteeism WHERE employeeid, payperiod, percentage) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, employee.getId());
            statement.setDate(2, java.sql.Date.valueOf(localDate));
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                absenteeism = extractAbsenteeismFromResultSet(resultSet);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }finally{
            
        }
        return absenteeism;
    }
    public void create(Absenteeism absenteeism){
        PreparedStatement statement = null;
        
        try{
            Connection connection = daoFactory.getConnection();
            String query = "REPLACE INTO absenteeism (employeeid, payperiod, percentage) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, absenteeism.getEmployee().getId());
            statement.setDate(2, java.sql.Date.valueOf(absenteeism.getLocalDate()));
            statement.setBigDecimal(3, absenteeism.getAbsentPercent());
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        } finally{
            
        }
    }
    private Absenteeism extractAbsenteeismFromResultSet(ResultSet resultSet) throws SQLException{
        int employeeId = resultSet.getInt("employeeid");
        LocalDate localDate = resultSet.getDate("payperiod").toLocalDate();
        BigDecimal percentage = resultSet.getBigDecimal("percentage");
        
        Employee employee = fetchEmployeeDetails(employeeId);
        return new Absenteeism(employee, localDate, percentage);
    }
    private Employee fetchEmployeeDetails(int employeeId){
        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        return employeeDAO.find(employeeId);
    }
}
