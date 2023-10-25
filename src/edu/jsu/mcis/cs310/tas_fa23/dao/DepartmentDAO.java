/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23.dao;

/**
 *
 * @author Johna
 * @author quint
 */

import edu.jsu.mcis.cs310.tas_fa23.Department;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
public class DepartmentDAO {
    private final String QUERY_FIND = "SELECT * FROM department WHERE id = ?";
    
    private final DAOFactory daoFactory;
    
    DepartmentDAO(DAOFactory daoFactory)
    {
        this.daoFactory = daoFactory;
    }
    
    /**
     *
     * @param id
     * @return - return's a department and its values based on an input of an id
     */
    public Department find(int id)
    {
        Department department = null;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try
        {
            
            Connection conn = daoFactory.getConnection();
            if (conn.isValid(0))
            {
                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);
                
                boolean result = ps.execute();
                
                if (result)
                {
                    rs = ps.getResultSet();
                    
                   if (rs.next())
                    {
                        String description = rs.getString("description");
                        int terminalID = rs.getInt("terminalid");
                        
                        department = new Department(id, description, terminalID);
                    }
                }
            }
        }            
        catch (SQLException e)
        {
            throw new DAOException(e.getMessage());
        }
        
        finally 
        {
            if (rs != null)
            {
                try 
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null)
            {
                try
                {
                    ps.close();
                }
                catch (SQLException e)
                {
                   throw new DAOException(e.getMessage());
                }       
            }
        }
        return department;
    }
}