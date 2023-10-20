package edu.jsu.mcis.cs310.tas_fa23;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Punch {
    private int id;
    private int terminalId;
    private Badge badge;
    private LocalDateTime originalTimeStamp;
    private LocalDateTime adjustedTimeStamp;
    private PunchAdjustmentType adjustmentType;

    public Punch(int terminalId, Badge badge, EventType punchType) {
        this.terminalId = terminalId;
        this.badge = badge;
        this.originalTimeStamp = LocalDateTime.now();
        this.adjustmentType = PunchAdjustmentType.NONE;
    }

    public Punch(int id, int terminalId, Badge badge, LocalDateTime originalTimeStamp, int punchTypeId) {
        this.id = id;
        this.terminalId = terminalId;
        this.badge = badge;
        this.originalTimeStamp = originalTimeStamp;
        this.adjustmentType = PunchAdjustmentType.NONE;
    }

    public Punch(int terminalId, int badgeId, long originalTimeStamp, int punchTypeId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}