package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Punch;
import edu.jsu.mcis.cs310.tas_fa23.EventType;
import edu.jsu.mcis.cs310.tas_fa23.Badge;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PunchDAO {

    private static final String QUERY_FIND = "SELECT * FROM punch WHERE id = ?";
    private static final String QUERY_INSERT = "INSERT INTO punch (terminalid, badgeid, originaltimestamp, punchtypeid) VALUES (?, ?, ?, ?)";

    private final DAOFactory daoFactory;

    PunchDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
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

                boolean hasResults = ps.execute();

                if (hasResults) {
                    rs = ps.getResultSet();

                    while (rs.next()) {
                        int terminalId = rs.getInt("terminalId");
                        int badgeId = rs.getInt("badgeId");
                        long originalTimeStamp = rs.getLong("originalTimeStamp");
                        int punchTypeId = rs.getInt("punchTypeId");

                        punch = new Punch(id, terminalId, badgeId, originalTimeStamp, punchTypeId);
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
    public List<Punch> list(Badge badge, LocalDate date) {
        List<Punch> punches = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            // Retrieve punches for the specified badge and date
            String queryString = "SELECT * FROM punch WHERE badgeid = ? AND DATE(FROM_UNIXTIME(originaltimestamp/1000)) = ?";
            ps = conn.prepareStatement(queryString);
            ps.setInt(1, badge.getId());
            ps.setDate(2, Date.valueOf(date));

            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int terminalId = rs.getInt("terminalId");
                int badgeId = rs.getInt("badgeId");
                long originalTimeStamp = rs.getLong("originalTimeStamp");
                int punchTypeId = rs.getInt("punchTypeId");

                Punch punch = new Punch(id, terminalId, badgeId, originalTimeStamp, punchTypeId);
                punches.add(punch);
            }

            // Add the first punch from the following day if it's a clock out or time out punch
            LocalDate nextDay = date.plusDays(1);
            String nextDayQueryString = "SELECT * FROM punch WHERE badgeid = ? AND DATE(FROM_UNIXTIME(originaltimestamp/1000)) = ? ORDER BY originaltimestamp LIMIT 1";
            ps = conn.prepareStatement(nextDayQueryString);
            ps.setInt(1, badge.getId());
            ps.setDate(2, Date.valueOf(nextDay));

            rs = ps.executeQuery();

            while (rs.next()) {
                int terminalId = rs.getInt("terminalId");
                int badgeId = rs.getInt("badgeId");
                long originalTimeStamp = rs.getLong("originalTimeStamp");
                int punchTypeId = rs.getInt("punchTypeId");

                Punch punch = new Punch(terminalId, badgeId, originalTimeStamp, punchTypeId);
                punches.add(punch);
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
        return punches;
    }

 public int create(Punch punch) {
        int punchId = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            int terminalId = punch.getTerminalId();
            int badgeId = punch.getBadgeId();
            long originalTimeStamp = punch.getOriginalTimeStamp();
            int punchTypeId = punch.getPunchTypeId();

            // Check for terminal authorization
            if (terminalId != 0 && terminalId != getTerminalIdFromEmployeeDepartment(badgeId)) {
                return punchId;
            }

            ps = conn.prepareStatement(QUERY_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, terminalId);
            ps.setInt(2, badgeId);
            ps.setLong(3, originalTimeStamp);
            ps.setInt(4, punchTypeId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return punchId;
            }

            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                punchId = rs.getInt(1);
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
        return punchId;
    }

    private int getTerminalIdFromEmployeeDepartment(int badgeId) {
        // Implementation for retrieving terminal ID from the employee's department
        return 0; // Replace with the actual implementation
    }
}


