/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23.dao;

/**
 *
 * @author Johna
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
public class DepartmentDAO {

    private final DAOFactory daofactory;
    
    private static final String QUERY_FIND = "SELECT * FROM department WHERE id = ?";
    
    public DepartmentDAO(DAOFactory daofactory) {
        this.daofactory = daofactory;
    }
    
    public Department find(int id) {
        Department department = null;
        
        PreparedStatement ps = null;
        Result rs = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if(conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND);
                ps.setString(1, String.valueOf(id));
                
                boolean hasresult = ps.execute();
                
                if(hasresult) {
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        int terminalid = rs.getInt("terminalid");
                        int departmentid = rs.getInt("id");
                        String desc = rs.getString("desc");
                        
                        department = new department(departmentid, terminalid, desc);       
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
            
        } finally { 
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOExecption(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLExecption e) {
                    throw new DAOExeption(e.getMessage());
                }
            }
        }    
        return department;
    }    
}
