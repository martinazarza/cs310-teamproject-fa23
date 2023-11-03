package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_fa23.Punch;
import edu.jsu.mcis.cs310.tas_fa23.PunchAdjustmentType;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
/**
 *
 * @author quint
 */

public final class DAOUtility {

    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
        
        JsonArray jsonArr = new JsonArray();
        
        for (int x = 0; x < dailypunchlist.size(); x++){
            
            Punch punch = dailypunchlist.get(x);
            
            JsonObject obj = new JsonObject();

            obj.put("id", String.valueOf(punch.getId()));
            obj.put("badgeid", String.valueOf(punch.getBadgeid()));
            obj.put("punchtype", String.valueOf(punch.getPunchtype()));
            obj.put("adjustmenttype", String.valueOf(punch.getAdjustmenttype()));
            obj.put("originaltimestamp", String.valueOf(punch.getOriginaltimestamp()));
            obj.put("adjustedtimestamp", String.valueOf(punch.getAdjustedtimestamp()));
            
            jsonArr.add(obj);
        }

        String json = Jsoner.serialize(jsonArr);
        
        return json;
    }

     public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift s) {
        long totalMinutes = 0;
        long shiftDuration;

        LocalDateTime shiftStart = null;
        LocalDateTime shiftStop = null;

        Boolean isRecorded;
        Boolean isTimeout = false;

        for (Punch p : dailypunchlist) {
            isRecorded = false;

            PunchAdjustmentType type = p.getAdjustmenttype();

            if (type == PunchAdjustmentType.LUNCH_START || type == PunchAdjustmentType.LUNCH_STOP) {
                continue;
            }

            if (null != type) {
                switch (type) {
                    case SHIFT_START:
                        shiftStart = p.getAdjustedtimestamp();
                        isRecorded = true;
                        break;
                    case SHIFT_STOP:
                        shiftStop = p.getAdjustedtimestamp();
                        isRecorded = true;
                        break;
                    default:
                        break;
                }
            }

            // is the punch docked or in interval or no adjustment was made
           
            if (!isRecorded) {
               
                switch (p.getPunchtype()) {
                    case CLOCK_IN:
                        shiftStart = p.getAdjustedtimestamp();
                        break;
                    case CLOCK_OUT:
                        shiftStop = p.getAdjustedtimestamp();
                        break;
                    case TIME_OUT:
                        isTimeout = true;
                        break;
                }
            } 
            
            else {
                continue;
            }

            if (isTimeout) {
                return (int) totalMinutes;
            }
        }

        if (shiftStop == null) {
            LocalTime sStop = s.getShiftstop();
            LocalDateTime ot = dailypunchlist.get(0).getAdjustedtimestamp();
            
            shiftStop = ot.withHour(sStop.getHour()).withMinute(sStop.getMinute()).withSecond(0).withNano(0);
        }
        
        shiftDuration = ChronoUnit.MINUTES.between(shiftStart, shiftStop);
        
        //shiftDuration = Duration.between(shiftStart, shiftStop).toMinutes();

        if (shiftDuration > s.getLunchthreshold()) {
            totalMinutes = shiftDuration - s.getLunchduration();
        } 
        
        else {
            totalMinutes = shiftDuration;
        }

        return (int) totalMinutes;
    }
}
    

