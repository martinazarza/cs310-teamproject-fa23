/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23;

/**
 *
 * @author Xavier Bausley
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class EmployeeDAO {
    private final DAOFactory daoFactory;
    
    private static final String QUERY_FIND_BY_ID = "SELECT * FROM employee WHERE id = ?";
    private static final String QUERY_FIND_BY_EMPLOYEE_ID = "SELECT * FROM employee WHERE badgeid = ?";
    
    public EmployeeDAO(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }
    public Employee find(int id){
        Employee employee = null;
        
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try{
            Connection connection = daoFactory.getConnection();
            
            if (connection.isValid(0)){
                preparedStatement = connection.prepareStatement(QUERY_FIND_BY_ID);
                preparedStatement.setString(1, String.valueOf(id));
                
                boolean hasResult = preparedStatement.execute();
                
                if(hasResult){
                    
                    resultSet = preparedStatement.getResultSet();
                    
                    while(resultSet.next()){
                        int idnum = id;
                        EmployeeType type = EmployeeType.values()[resultSet.getInt("employeetypeid")];
                        LocalDateTime active = resultSet.getTimestamp("active").toLocalDateTime();
                        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
                        String badgeid = resultSet.getString("badgeid");
                        Badge badge = badgeDAO.find(badgeid);
                        String firstname = resultSet.getString("firstname");
                        String middlename = resultSet.getString("middlename");
                        String lastname = resultSet.getString("lastname");
                        //DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();
                        //int departmentid = resultSet.getInt("departmentid");
                        //Department department = departmentDAO.find(departmentid);
                        //ShiftDAO shiftDAO = daoFactory.getShiftDAO();
                        //int shiftid = resultSet.getInt("shiftid");
                        //Shift shift = shiftDAO.find(shiftid);
                        employee = new Employee(idnum, active, firstname, middlename, lastname, badge, department, shift, type);
                    }
                }
            }
        } catch (SQLException e){
            throw new DAOException(e.getMessage());
        }
    }
finally {
    if (resultSet != null){
        try{
            resultSet.close();
        } catch (SQLException e){
            throw new DAOException(e.getMessage());
        }
    }
    if (preparedStatement != null){
        try{
            preparedStatement.close();
        } catch(SQLException e){
            throw new DAOException(e.getMessage());
        }
    }
    
}
return employee;

public Employee find(Badge badge) {

        Employee employee = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                preparedStatement = conn.prepareStatement(QUERY_FIND_BY_EMPLOYEE_ID);
                preparedStatement.setString(1, String.valueOf(badge.getId()));

                boolean hasresults = preparedStatement.execute();

                if (hasresults) {

                    resultSet = preparedStatement.getResultSet();

                    while (resultSet.next()) {
                        int idnum = resultSet.getInt("id");
                        EmployeeType type = EmployeeType.values()[resultSet.getInt("employeetypeid")];
                        LocalDateTime active = resultSet.getTimestamp("active").toLocalDateTime();
                        Badge badgenum = badge;
                        String firstname = resultSet.getString("firstname");
                        String middlename = resultSet.getString("middlename");
                        String lastname = resultSet.getString("lastname");
                        //DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();
                       // int departmentid = resultSet.getInt("departmentid");
                       // Department department = departmentDAO.find(departmentid);
                       // ShiftDAO shiftDAO = daoFactory.getShiftDAO();
                        //int shiftid = resultSet.getInt("shiftid");
                       // Shift shift = shiftDAO.find(shiftid);
                        employee = new Employee(idnum, active, firstname, middlename, lastname, badgenum, department, shift, type);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());

        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw  new DAOException(e.getMessage());
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }
        return employee;
    }
}
}
