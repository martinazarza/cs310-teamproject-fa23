package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_fa23.Punch;

/**
 * 
 * Utility class for DAOs.  This is a final, non-constructable class containing
 * common DAO logic and other repeated and/or standardized code, refactored into
 * individual static methods.
 * 
 */
public final class DAOUtility {

    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {
        int totalMinutes = 0;
        boolean insideShift = false;
        LocalDateTime lunchStart = null;
        LocalDateTime lunchEnd = null;

        for (int i = 0; i < dailypunchlist.size(); i++) {
            Punch punch = dailypunchlist.get(i);

            if (punch.getType() == Punch.PunchType.CLOCK_IN) {
                insideShift = true;
            } else if (punch.getType() == Punch.PunchType.CLOCK_OUT) {
                insideShift = false;
            } else if (punch.getType() == Punch.PunchType.TIME_OUT) {
                insideShift = false;
                continue;
            }

            if (insideShift) {
                if (punch.getType() == Punch.PunchType.CLOCK_IN) {
                    if (lunchStart == null) {
                        lunchStart = punch.getOriginaltimestamp().toLocalDateTime();
                    }
                } else if (punch.getType() == Punch.PunchType.CLOCK_OUT) {
                    if (lunchStart != null) {
                        lunchEnd = punch.getOriginaltimestamp().toLocalDateTime();
                        long lunchDuration = ChronoUnit.MINUTES.between(lunchStart, lunchEnd);
                        if (lunchDuration >= shift.getLunchDeduct()) {
                            totalMinutes -= shift.getLunchDuration();
                        }
                        lunchStart = null;
                        lunchEnd = null;
                    }
                    totalMinutes += ChronoUnit.MINUTES.between(punch.getOriginaltimestamp().toLocalDateTime(), dailypunchlist.get(i - 1).getOriginaltimestamp().toLocalDateTime());
                }
            }
        }
        return totalMinutes;
    }
}