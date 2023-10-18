/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Shift;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author quint
 */
public class ShiftDAO {
    
    private static final String QUERY_FIND_BY_ID = "SELECT * FROM shift WHERE id = ?";
    private static final String QUERY_FIND_BY_EMPLOYEE_ID = "SELECT * FROM employee WHERE badgeid = ?";
    private final DAOFactory daoFactory;

    public ShiftDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
     public Shift find(int id) {
        Shift shift = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND_BY_ID);
                ps.setString(1, String.valueOf(id));

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        HashMap<String, String> shiftrules = buildShiftHash(rs);
                        shift = new Shift(shiftrules);
                    }
                }
            }
        } catch (SQLException e) {

            throw new DAOException(e.getMessage());

        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw  new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }

        return shift;
    }

    public Shift find(Badge badge) {

        Shift shift = null;

        PreparedStatement ps = null;
        ResultSet rs = null;
        
        
        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND_BY_EMPLOYEE_ID);
                ps.setString(1, String.valueOf(badge.getId()));

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        String shiftid = rs.getString("shiftid");
                        shift = find(Integer.parseInt(shiftid));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw  new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }
        return shift;
    }

    private HashMap<String, String> buildShiftHash(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String description = rs.getString("description");
        String shiftstart = rs.getString("shiftstart");
        String shiftstop = rs.getString("shiftstop");
        String lunchstart = rs.getString("lunchstart");
        String lunchstop = rs.getString("lunchstop");
        String roundinterval = rs.getString("roundinterval");
        String graceperiod = rs.getString("graceperiod");
        String dockpenalty = rs.getString("dockpenalty");
        String lunchthreshold = rs.getString("lunchthreshold");

        HashMap<String, String> shiftrules;

        shiftrules = new HashMap<>();
        shiftrules.put("id", id);
        shiftrules.put("description", description);
        shiftrules.put("shiftstart", shiftstart);
        shiftrules.put("shiftstop", shiftstop);
        shiftrules.put("lunchstart", lunchstart);
        shiftrules.put("lunchstop", lunchstop);
        shiftrules.put("roundinterval", roundinterval);
        shiftrules.put("graceperiod", graceperiod);
        shiftrules.put("dockpenalty", dockpenalty);
        shiftrules.put("lunchthreshold", lunchthreshold);

        return shiftrules;
    }
    
}
