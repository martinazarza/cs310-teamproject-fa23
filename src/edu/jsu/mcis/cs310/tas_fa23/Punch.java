package edu.jsu.mcis.cs310.tas_fa23;

import java.sql.Timestamp;

public class Punch {
    private int id;
    private int terminalId;
    private int badgeId;
    private long originalTimeStamp;
    private long adjustedTimeStamp;
    private PunchAdjustmentType adjustmentType;

    public Punch(int terminalId, Badge badge, EventType punchType) {
        this.terminalId = terminalId;
        this.badgeId = badge.getId();
        this.originalTimeStamp = System.currentTimeMillis();
        this.adjustedTimeStamp = this.originalTimeStamp;
        this.adjustmentType = PunchAdjustmentType.NONE;
    }

    public Punch(int id, int terminalId, int badgeId, long originalTimeStamp, int punchTypeId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Add additional constructors and methods as needed

    // Getter and setter methods for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }

    public int getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(int badgeId) {
        this.badgeId = badgeId;
    }

    public long getOriginalTimeStamp() {
        return originalTimeStamp;
    }

    public void setOriginalTimeStamp(long originalTimeStamp) {
        this.originalTimeStamp = originalTimeStamp;
    }

    public long getAdjustedTimeStamp() {
        return adjustedTimeStamp;
    }

    public void setAdjustedTimeStamp(long adjustedTimeStamp) {
        this.adjustedTimeStamp = adjustedTimeStamp;
    }

    public PunchAdjustmentType getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(PunchAdjustmentType adjustmentType) {
        this.adjustmentType = adjustmentType;
    }
}