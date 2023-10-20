package edu.jsu.mcis.cs310.tas_fa23;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Punch {
    private int id;
    private int terminalId;
    private int badgeId;
    private long originalTimeStamp;
    private LocalDateTime adjustedTimeStamp;
    private PunchAdjustmentType adjustmentType;

    public Punch(int terminalId, Badge badge, EventType punchType) {
        this.terminalId = terminalId;
        this.badgeId = badge.getId();
        this.originalTimeStamp = System.currentTimeMillis();
        this.adjustedTimeStamp = LocalDateTime.ofEpochSecond(this.originalTimeStamp / 1000, 0, null);
        this.adjustmentType = PunchAdjustmentType.NONE;
    }

    public Punch(int id, int terminalId, int badgeId, long originalTimeStamp, int punchTypeId) {
        this.id = id;
        this.terminalId = terminalId;
        this.badgeId = badgeId;
        this.originalTimeStamp = originalTimeStamp;
        this.adjustedTimeStamp = LocalDateTime.ofEpochSecond(this.originalTimeStamp / 1000, 0, null);
        this.adjustmentType = PunchAdjustmentType.NONE;
    }

    public Punch(int terminalId, int badgeId, long originalTimeStamp, int punchTypeId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void adjust(Shift s) {
        LocalTime shiftStart = s.getShiftStart();
        LocalTime shiftStop = s.getShiftStop();
        LocalTime lunchStart = s.getLunchStart();
        LocalTime lunchStop = s.getLunchStop();
        int roundInterval = s.getRoundInterval();
        int gracePeriod = s.getGracePeriod();
        int dockPenalty = s.getDockPenalty();

        LocalTime originalTime = adjustedTimeStamp.toLocalTime();
        PunchAdjustmentType AdjustmentType = PunchAdjustmentType.NONE;

        // Adjust based on the shift parameters
        if (originalTime.isBefore(shiftStart)) {
            if (shiftStart.minusMinutes(roundInterval).isBefore(originalTime)) {
                adjustedTimeStamp = adjustedTimeStamp.with(shiftStart);
                AdjustmentType = PunchAdjustmentType.SHIFT_START;
            } else {
                adjustedTimeStamp = adjustedTimeStamp.plusMinutes((roundInterval - originalTime.getMinute() % roundInterval) % roundInterval)
                        .withSecond(0);
                AdjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
            }
        } else if (originalTime.isAfter(shiftStop)) {
            if (originalTime.isBefore(shiftStop.plusMinutes(roundInterval))) {
                adjustedTimeStamp = adjustedTimeStamp.with(shiftStop);
                AdjustmentType = PunchAdjustmentType.SHIFT_STOP;
            } else {
                adjustedTimeStamp = adjustedTimeStamp.minusMinutes(originalTime.getMinute() % roundInterval)
                        .withSecond(0);
                AdjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
            }
        } else if (originalTime.isAfter(shiftStart) && originalTime.isBefore(lunchStart)) {
            if (originalTime.isAfter(shiftStart.plusMinutes(gracePeriod))) {
                adjustedTimeStamp = adjustedTimeStamp.with(shiftStart);
                AdjustmentType = PunchAdjustmentType.GRACE_PERIOD;
            }
        } else if (originalTime.isAfter(lunchStop) && originalTime.isBefore(shiftStop)) {
            if (originalTime.isBefore(shiftStop.minusMinutes(gracePeriod))) {
                adjustedTimeStamp = adjustedTimeStamp.with(shiftStop);
                AdjustmentType = PunchAdjustmentType.GRACE_PERIOD;
            }
        } else if (originalTime.isAfter(lunchStart) && originalTime.isBefore(lunchStop)) {
            if (lunchStart.minusMinutes(roundInterval).isBefore(originalTime)) {
                adjustedTimeStamp = adjustedTimeStamp.with(lunchStart);
                AdjustmentType = PunchAdjustmentType.LUNCH_START;
            } else {
                adjustedTimeStamp = adjustedTimeStamp.plusMinutes((roundInterval - originalTime.getMinute() % roundInterval) % roundInterval)
                        .withSecond(0);
                AdjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
            }
        } else if (originalTime.isBefore(lunchStart) || originalTime.isAfter(lunchStop)) {
            if (originalTime.isAfter(shiftStart.plusMinutes(gracePeriod)) && originalTime.isBefore(shiftStop.minusMinutes(gracePeriod))) {
                if (originalTime.isAfter(shiftStart.plusMinutes(gracePeriod + dockPenalty))) {
                    adjustedTimeStamp = adjustedTimeStamp.with(shiftStart).plusMinutes(dockPenalty);
                    AdjustmentType = PunchAdjustmentType.DOCK_PENALTY;
                }
            }
        }
        this.adjustmentType = AdjustmentType;
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

    public LocalDateTime getAdjustedTimeStamp() {
        return adjustedTimeStamp;
    }

    public void setAdjustedTimeStamp(LocalDateTime adjustedTimeStamp) {
        this.adjustedTimeStamp = adjustedTimeStamp;
    }

    public PunchAdjustmentType getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(PunchAdjustmentType adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public int getPunchTypeId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean getType() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getOriginaltimestamp() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static class PunchType {

        public static boolean CLOCK_IN;

        public PunchType() {
        }
    }
}