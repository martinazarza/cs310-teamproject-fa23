package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Employee;
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
 * @author quint
 * @author Jalen
 */

public class PunchDAO {

    private static final String QUERY_FIND = "SELECT * FROM event WHERE id = ?";
    private static final String QUERY_LIST = "SELECT * FROM event WHERE badgeid = ? ORDER BY timestamp";
    private static final String QUERY_CREATE = "INSERT INTO event (terminalid, badgeid, timestamp, eventtypeid) VALUES (?, ?, ?, ?)";
    private static final String QUERY_LIST_E = "SELECT * FROM event WHERE badgeid = ? AND timestamp > ? LIMIT 1";

    private final DAOFactory daoFactory;

    // constructor
    PunchDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * This method is used to find and return individual 
     * model objects from the database.
     * 
     * @param id Represents employee ID
     * @return 
     */
    
    public Punch find(int id) {

        Punch punch = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setString(1, Integer.toString(id));

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {

                        int terminalid;
                        String badgeid;
                        EventType punchtype;
                        LocalDateTime originaltimestamp;

                        // get terminal id  
                        terminalid = rs.getInt("terminalid");

                        // getting badge
                        badgeid = rs.getString("badgeid");
                        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
                        Badge b = badgeDAO.find(badgeid);

                        // get punch type 
                        punchtype = EventType.values()[rs.getInt("eventtypeid")];

                        // get timestamp
                        originaltimestamp = rs.getTimestamp("timestamp").toLocalDateTime();

                        punch = new Punch(id, terminalid, b, originaltimestamp, punchtype);

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

    /**
     * This method is used to retrieve all the punches entered by
     * an employee for a single day.
     * 
     * @param badge Represents the employee badge.
     * @param date Represents specified day.
     * @return Returns punches for a single day.
     */
    
    public ArrayList list(Badge badge, LocalDate date) {
        ArrayList<Punch> list = new ArrayList();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_LIST);
                ps.setString(1, badge.getId());

                boolean hasresults = ps.execute();
                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {

                        Timestamp punchdate = rs.getTimestamp(4);
                        LocalDateTime local = punchdate.toLocalDateTime();
                        LocalDate ld = local.toLocalDate();

                        if (ld.equals(date)) {
                            int id = rs.getInt(1);
                            list.add(find(id));
                        }

                    }

                }

            }

            if (((list.get(list.size() - 1)).getPunchtype() == EventType.CLOCK_IN)) {
                LocalDateTime newdate = list.get(list.size() - 1).getOriginaltimestamp();
                Timestamp newts = Timestamp.valueOf(newdate);

                ps = conn.prepareStatement(QUERY_LIST_E);
                ps.setString(1, badge.getId());
                ps.setString(2, newts.toString());

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        int id = rs.getInt(1);
                        list.add(find(id));
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

        return list;

    }

    /**
     * This method is used to retrieve all the punches entered by
     * an employee within a range of dates.
     * 
     * @param badge Represents the employee badge.
     * @param lowerDate Represents the beginning of the date range.
     * @param upperDate Represents the ending of the date range.
     * @return Returns punches within a range of dates.
     */
    
    public ArrayList list(Badge badge, LocalDate lowerDate, LocalDate upperDate) {
        ArrayList<Punch> list = new ArrayList();
        
        LocalDate date = lowerDate;
        
        while (date.isBefore(upperDate) || date.equals(upperDate)) {
            ArrayList<Punch> entries = new ArrayList();
            
            try {
                entries = list(badge, date);
            } catch (IndexOutOfBoundsException e) {}
            
            if (!entries.isEmpty() && !list.isEmpty()) {
                if (list.get(list.size() - 1).toString().equals(entries.get(0).toString())) {
                    list.remove(list.size() - 1);
                }
            }
            
            list.addAll(entries);
            
            date = date.plusDays(1);
        }
        
        return list;
    }

    /**
     * This method is used to add new punches to the database.
     * 
     * @param punch Represents a new punch.
     * @return Returns the numeric ID retrieved from the database as an integer.
     */
    
    public int create(Punch punch) {

        int punchId = 0;

        PreparedStatement ps = null;
        ResultSet rs = null;

        EmployeeDAO employeeDao = daoFactory.getEmployeeDAO();
        Employee employee = employeeDao.find(punch.getBadge());

        int empTerminalId = employee.getDepartment().getTerminalid();

        if (empTerminalId == punch.getTerminalid()) {

            try {

                Connection conn = daoFactory.getConnection();

                if (conn.isValid(0)) {

                    ps = conn.prepareStatement(QUERY_CREATE, PreparedStatement.RETURN_GENERATED_KEYS);

                    ps.setInt(1, punch.getTerminalid());
                    ps.setString(2, punch.getBadge().getId());
                    ps.setString(3, punch.getOriginaltimestamp().toString());
                    ps.setInt(4, punch.getPunchtype().ordinal());

                    int rowAffected = ps.executeUpdate();

                    if (rowAffected == 1) {

                        rs = ps.getGeneratedKeys();

                        if (rs.next()) {
                            punchId = rs.getInt(1);
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

        }

        return punchId;

    }
}