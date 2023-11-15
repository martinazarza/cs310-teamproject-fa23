package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_fa23.Punch;
import java.util.ArrayList;
import java.util.HashMap;
import edu.jsu.mcis.cs310.tas_fa23.PunchAdjustmentType;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.math.BigDecimal;
/**
 *
 * @author quint
 */

public final class DAOUtility {

    public class DAOUtility {


public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();

        for (Punch punch : dailypunchlist) {
            HashMap<String, String> punchData = new HashMap<>();

            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", punch.getBadge().getId());
            punchData.put("terminalid", String.valueOf(punch.getTerminalid()));
            punchData.put("punchtype", punch.getPunchType().toString());
            punchData.put("adjustmenttype", punch.getAdjustmentType().toString());
            punchData.put("originaltimestamp", punch.getOriginaltimestamp().printOriginal());
            punchData.put("adjustedtimestamp", punch.getAdjustedtimestamp().printAdjusted());

            jsonData.add(punchData);
        }

        return Jsoner.serialize(jsonData);
    }
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
    

