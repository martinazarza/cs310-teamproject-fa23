package edu.jsu.mcis.cs310.tas_fa23;

import java.time.*;
import java.time.format.*;

/**
 * The Punch model class contains information about
 * punches given in the database including 
 * original timestamps and adjusted timestamps.
 * 
 */

public class Punch {

    private Integer id, terminalid;
    private String badgeid;
    private EventType punchtype;
    private LocalDateTime timestamp, originaltimestamp, adjustedtimestamp;
    private PunchAdjustmentType adjustmenttype;
    private Badge badge;
    private LunchStatus adjustedlunchstatus;
    
    public enum LunchStatus {
        HAPPENING,
        HAPPENED,
        NOT_HAPPENING,
        INAPPLICABLE
    };


    public Punch(int terminalid, Badge badge, EventType punchtype) {
        this.id = null;
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = punchtype;
        this.originaltimestamp = LocalDateTime.now();
    }

    public Punch(int id, int terminalid, Badge badge, LocalDateTime originaltimestamp, EventType punchtype) {
        this.id = id;
        this.terminalid = terminalid;
        this.badge = badge;
        this.badgeid = badge.getId();
        this.originaltimestamp = originaltimestamp;
        this.timestamp = originaltimestamp;
        this.punchtype = punchtype;
        adjustedtimestamp = originaltimestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public void setTerminalid(int terminalid) {
        this.terminalid = terminalid;
    }

     public String getBadgeid() { 
         return badgeid; 
     }

     public void setBadgeid(String badgeid) {
         this.badgeid = badgeid; 
     }

    public EventType getPunchtype() {
        return punchtype;
    }

    public void setPunchtype(EventType punchtype) {
        this.punchtype = punchtype;
    }

    public LocalDateTime getOriginaltimestamp() {
        return originaltimestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getAdjustedtimestamp() {
        return adjustedtimestamp;
    }

    public void setAdjustedtimestamp(LocalDateTime adjustedtimestamp) {
        this.adjustedtimestamp = adjustedtimestamp;
    }

    public PunchAdjustmentType getAdjustmenttype() {
        return adjustmenttype;
    }

    public void setAdjustmenttype(PunchAdjustmentType adjustmenttype) {
        this.adjustmenttype = adjustmenttype;
    }

    public Badge getBadge() {
        return badge;
    }
    
    public LunchStatus getAdjustedLunchStatus(){
        return adjustedlunchstatus;
    }

    public String printOriginal() {
        
        StringBuilder s = new StringBuilder();

         
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");
        String formatted = timestamp.format(formatter);
        
        s.append("#").append(badgeid).append(" ").append(punchtype).append(": ").append(formatted);
        
        return s.toString().toUpperCase();

    }
    public void adjust(Shift s) {

        int shiftStart = s.getShiftstart().toSecondOfDay();
        int shiftStop = s.getShiftstop().toSecondOfDay();
        int lunchStart = s.getLunchstart().toSecondOfDay();
        int lunchStop = s.getLunchstop().toSecondOfDay();
        int gracePeriod = s.getGraceperiod() * 60;
        int dockPen = s.getDockpenalty() * 60;
        int roundInt = s.getRoundinterval() * 60;
        int originalTimeSec = originaltimestamp.toLocalTime().toSecondOfDay();

        if (originaltimestamp.getDayOfWeek() == DayOfWeek.SATURDAY || originaltimestamp.getDayOfWeek() == DayOfWeek.SUNDAY) {
            adjustmenttype = PunchAdjustmentType.INTERVAL_ROUND;
            int adjustedsec;
            if ((originalTimeSec % roundInt) < (roundInt / 2)) {
                adjustedsec = (Math.round(originalTimeSec / roundInt) * roundInt);
            } else {
                adjustedsec = (Math.round(originalTimeSec / roundInt) * roundInt) + roundInt;
            }
            adjustedtimestamp = adjustedtimestamp.plusSeconds(adjustedsec - originalTimeSec);
            adjustedtimestamp = adjustedtimestamp.withSecond(0).withNano(0);
        } else {
            switch (punchtype) {
                case CLOCK_IN:
                    //If time is before shift start
                    if (originalTimeSec < shiftStart) {
                        //if round interval is greater than difference between time and shift start
                        if (roundInt >= shiftStart - originalTimeSec) {
                            adjustmenttype = PunchAdjustmentType.SHIFT_START;
                            adjustedtimestamp = LocalTime.ofSecondOfDay(shiftStart).atDate(originaltimestamp.toLocalDate());
                            //If round interval is greater than time between timestamp and shift start
                        } else if (roundInt < shiftStart - originalTimeSec) {
                            System.out.println(originalTimeSec + "Original " + originaltimestamp);
                            System.out.println(shiftStart + "shiftstart " + s.getShiftstart());
                            adjustmenttype = PunchAdjustmentType.INTERVAL_ROUND;
                            int remainderSec = originalTimeSec % roundInt;
                            if (remainderSec < roundInt / 2) {
                                int roundedTime = originalTimeSec - remainderSec;
                                adjustedtimestamp = LocalTime.ofSecondOfDay(roundedTime).atDate(originaltimestamp.toLocalDate());
                            } else {
                                int roundedTime = originalTimeSec + remainderSec;
                                adjustedtimestamp = LocalTime.ofSecondOfDay(roundedTime).atDate(originaltimestamp.toLocalDate());
                            }

                        } //All else should be none
                        else {
                            adjustmenttype = PunchAdjustmentType.NONE;
                            adjustedtimestamp = originaltimestamp.withSecond(0).withNano(0);
                        }
                    }// if timestamp is before lunch
                    else if (originalTimeSec < lunchStart) {
                        //if time stamp is on the first minute after shift start
                        if ((originalTimeSec) < (shiftStart + (roundInt / 15))) {
                            adjustmenttype = PunchAdjustmentType.NONE;
                            int remainderSec = originalTimeSec % roundInt;
                            if (remainderSec < roundInt / 2) {
                                int roundedTime = originalTimeSec - remainderSec;
                                adjustedtimestamp = LocalTime.ofSecondOfDay(roundedTime).atDate(originaltimestamp.toLocalDate());
                            } else {
                                int roundedTime = originalTimeSec + remainderSec;
                                adjustedtimestamp = LocalTime.ofSecondOfDay(roundedTime).atDate(originaltimestamp.toLocalDate());
                            }
                        } //If the time stamp is after grace period and before dock pen time
                        else if (shiftStart + gracePeriod < originalTimeSec && originalTimeSec <= shiftStart + dockPen) {
                            adjustmenttype = PunchAdjustmentType.SHIFT_DOCK;
                            int dock = shiftStart + dockPen;
                            adjustedtimestamp = LocalTime.ofSecondOfDay(dock).atDate(originaltimestamp.toLocalDate());
                        } //All else should be shift start
                        else {
                            adjustmenttype = PunchAdjustmentType.SHIFT_START;
                            adjustedtimestamp = LocalTime.ofSecondOfDay(shiftStart).atDate(originaltimestamp.toLocalDate());
                        }
                    } //If time stamp is sftaer lunch start
                    else if (originalTimeSec > lunchStart) {
                        adjustmenttype = PunchAdjustmentType.LUNCH_STOP;
                        adjustedtimestamp = LocalTime.ofSecondOfDay(lunchStop).atDate(originaltimestamp.toLocalDate());
                    }
                    break;
                case CLOCK_OUT:
                    //If timestamp is after shift stop
                    if (originalTimeSec > shiftStop) {
                       
                        //Clock out for same minute of shift stop
                        if ((originalTimeSec) < (shiftStop + (roundInt / 15))) {
                            adjustmenttype = PunchAdjustmentType.NONE;
                            int remainderSec = originalTimeSec % roundInt;
                            if (remainderSec < roundInt / 2) {
                                int roundedTime = originalTimeSec - remainderSec;
                                adjustedtimestamp = LocalTime.ofSecondOfDay(roundedTime).atDate(originaltimestamp.toLocalDate());
                            } else {
                                int roundedTime = originalTimeSec + remainderSec;
                                adjustedtimestamp = LocalTime.ofSecondOfDay(roundedTime).atDate(originaltimestamp.toLocalDate());
                            }

                        }
                            //round for after first round interval
                        else if ((originalTimeSec) > (shiftStop + roundInt)) {
                            System.out.println(originalTimeSec + "Original " + originaltimestamp);
                            System.out.println(shiftStop + "shiftstop " + s.getShiftstop());
                            adjustmenttype = PunchAdjustmentType.INTERVAL_ROUND;
                            int remainderSec = originalTimeSec % roundInt;
                            if (remainderSec < roundInt / 2) {
                                int roundedTime = originalTimeSec - remainderSec;
                                adjustedtimestamp = LocalTime.ofSecondOfDay(roundedTime).atDate(originaltimestamp.toLocalDate());
                            } else {
                                int roundedTime = originalTimeSec + remainderSec;
                                adjustedtimestamp = LocalTime.ofSecondOfDay(roundedTime).atDate(originaltimestamp.toLocalDate());
                            }

                        } //Everything else should be shift stop
                        else {
                            adjustmenttype = PunchAdjustmentType.SHIFT_STOP;
                            adjustedtimestamp = LocalTime.ofSecondOfDay(shiftStop).atDate(originaltimestamp.toLocalDate());
                        }
                    } //If its between lunch
                    else if (originalTimeSec > lunchStart && originalTimeSec < lunchStop) {
                        adjustmenttype = PunchAdjustmentType.LUNCH_START;
                        adjustedtimestamp = LocalTime.ofSecondOfDay(lunchStart).atDate(originaltimestamp.toLocalDate());

                    }//If its before shift stop
                    else if (originalTimeSec < shiftStop) {
                        //If it's in the grace period
                        if (shiftStop - gracePeriod <= originalTimeSec) {
                            adjustmenttype = PunchAdjustmentType.SHIFT_STOP;
                            adjustedtimestamp = LocalTime.ofSecondOfDay(shiftStop).atDate(originaltimestamp.toLocalDate());

                        }//If its before grace period and after dock period
                        else if (originalTimeSec < (shiftStop - gracePeriod) && originalTimeSec >= (shiftStop - dockPen)) {
                            adjustmenttype = PunchAdjustmentType.SHIFT_DOCK;
                            int dock = shiftStop - dockPen;
                            adjustedtimestamp = LocalTime.ofSecondOfDay(dock).atDate(originaltimestamp.toLocalDate());
                        } //Interval round for everything else
                        else {
                            adjustmenttype = PunchAdjustmentType.INTERVAL_ROUND;
                            int remainderSec = originalTimeSec % roundInt;
                            if (remainderSec < roundInt / 2) {
                                int roundedTime = originalTimeSec - remainderSec;
                                adjustedtimestamp = LocalTime.ofSecondOfDay(roundedTime).atDate(originaltimestamp.toLocalDate());
                            } else {
                                int roundedTime = originalTimeSec + remainderSec;
                                adjustedtimestamp = LocalTime.ofSecondOfDay(roundedTime).atDate(originaltimestamp.toLocalDate());
                            }
                        }
                    }
                    break;
            }
        }
        adjustedtimestamp = adjustedtimestamp.withSecond(0).withNano(0);
    }
    
    public String printAdjusted() {
        
        StringBuilder s = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");
        String formatted = adjustedtimestamp.format(formatter).toUpperCase();
        String atype = adjustmenttype.toString();
       
        s.append("#").append(badgeid).append(" ").append(punchtype).append(": ").append(formatted).append(" (").append(atype).append(")");
        
        return s.toString();

    }
}