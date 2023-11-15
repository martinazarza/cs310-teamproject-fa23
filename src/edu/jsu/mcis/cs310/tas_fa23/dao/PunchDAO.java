package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.EventType;
import edu.jsu.mcis.cs310.tas_fa23.Punch;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.*;

/**
 * The PunchDAO class retrieves the punch data
 * from the database and passes it to and from the
 * methods in the TAS libraries.
 * 
 * @author Jalen
 */

public class PunchDAO {
    private static final String QUERY_FIND = "SELECT * FROM event WHERE id = ?";
    private static final String QUERY_LIST = "SELECT * FROM event WHERE badgeid = ? AND DATE(timestamp) = ? ORDER BY timestamp";

    private final DAOFactory daoFactory;

    PunchDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }

    public int create(Punch punch){
        String badgeId = punch.getBadge().getId();
        LocalDateTime timestamp = punch.getOriginaltimestamp();
        try{
            Connection conn = daoFactory.getConnection();
            PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO event (badgeid, terminalid, eventtypeid, timestamp)"+
                "VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, badgeId);
            statement.setInt(2, punch.getTerminalid());
            statement.setInt(3, punch.getPunchtype().ordinal());
            statement.setObject(4, timestamp);
            
            int affectedRows = statement.executeUpdate();
            
            if(affectedRows == 0){
                return 0; 
            }
            
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next()){
                int id = generatedKeys.getInt(1);
                punch.setId(id);
                return id;
            } else{
                
                return 0;
            
            }
        
        } catch(SQLException e){
            throw new DAOException(e.getMessage());
        }
                
    
    }
    public Punch find(int id) {

        Punch punch = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        
                        // Convert the timestamp in the database to LocalDateTime in java
                        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                        
                        // Get the terminal id from the database
                        int terminalId = rs.getInt("terminalid");
                        
                        // Create a new Badge object that has the required badge id
                        Badge badge = new Badge(rs.getString("badgeid"),null);
                        
                        // Check the "eventtypeid" to set the EventType in the final constructor
                        EventType event = null;
                        switch (rs.getInt("eventtypeid")) {
                            case 0:
                                event = EventType.CLOCK_OUT;
                                break;
                            case 1:
                                event = EventType.CLOCK_IN;
                                break;
                            case 2:
                                event = EventType.TIME_OUT;
                                break;
                        }
                        
                        // Set the return variable
                        punch = new Punch(id, terminalId, badge, timestamp, event);
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
                    throw new DAOException(e.getMessage());
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

        return punch;

    }
    
    public ArrayList<Punch> list(Badge badge, LocalDate begin, LocalDate end) {
        ArrayList<Punch> result = new ArrayList<>();

        // Iterate through each day in the date range
        LocalDate currentDate = begin;
        while (!currentDate.isAfter(end)) {
            // Use the existing list method to get punches for the current day
            ArrayList<Punch> punchesForDay = list(badge, currentDate);

            // Add punches for the current day to the result list
            result.addAll(punchesForDay);

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }

        return result;
    }

    
    public ArrayList list(Badge badge, LocalDate day) {
        
        ArrayList<Punch> result = new ArrayList<>();
        LocalDate secondDay = day.plusDays(1);
        boolean hasResults;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                // Use prepared statement to stage the "SELECT * FROM " argument
                ps = conn.prepareStatement(QUERY_LIST);
                
                ps.setString(1, badge.getId());
                ps.setString(2, day.toString());
                
                // Execute 
                hasResults = ps.execute();
                
                // Check if results
                if (hasResults) {
                    
                    /* Get ResultSet */
                    
                    rs = ps.getResultSet();
                    
                    while (rs.next()) {
                        // Get the equivelent EventType
                        EventType event = null;
                        switch (rs.getInt("eventtypeid")) {
                            case 0:
                                event = EventType.CLOCK_OUT;
                                break;
                            case 1:
                                event = EventType.CLOCK_IN;
                                break;
                            case 2:
                                event = EventType.TIME_OUT;
                                break;
                        }
                        
                        // Get terminal id
                        int terminalId = rs.getInt("terminalid");
                        
                        // Convert the timestamp in the database to LocalDateTime in java
                        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                        
                        // Create Punch object
                        Punch punch = new Punch(rs.getInt("id"),terminalId, badge, timestamp, event);
                        
                        // Add the new punch object to the result
                        result.add(punch);
                    }
                    
                }
                
                /* If no data available, print an error */

                else {

                    System.err.println("ERROR: No data returned!");

                }
                
                /* Execute prepare statement again but with the next day of punch*/
                
                ps.setString(1, badge.getId());
                ps.setString(2, secondDay.toString() + "%");
                
                // Execute 
                hasResults = ps.execute();
                
                // Check if results
                if (hasResults) {
                    
                    /* Get ResultSet */
                    
                    rs = ps.getResultSet();
                    
                    if (rs.next()) {
                        // Get the equivelent EventType
                        EventType event = null;
                        switch (rs.getInt("eventtypeid")) {
                            case 0:
                                event = EventType.CLOCK_OUT;
                                break;
                            case 1:
                                event = EventType.CLOCK_IN;
                                break;
                            case 2:
                                event = EventType.TIME_OUT;
                                break;
                        }
                        
                        /* Check if next day punch is  a clock out or a time out */
                        if (event == EventType.CLOCK_OUT || event == EventType.TIME_OUT) {
                            // Get terminal id
                            int terminalId = rs.getInt("terminalid");

                            // Convert the timestamp in the database to LocalDateTime in java
                            LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();

                            // Create Punch object
                            Punch punch = new Punch(rs.getInt("id"),terminalId, badge, timestamp, event);

                            // Add the new punch object to the result
                            result.add(punch);
                        }
                        /* If it isn't then don't add the next day to the list */
                    }
                    
                }
                
                /* If no data available, print an error */

                else {

                    System.err.println("ERROR: No data returned!");

                }
                
            }
            
        }
        
        catch (SQLException e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (SQLException e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (SQLException e) { e.printStackTrace(); } }
            
        }
        
        return result;
    }
}