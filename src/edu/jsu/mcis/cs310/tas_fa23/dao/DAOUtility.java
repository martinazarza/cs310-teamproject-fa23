package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.util.ArrayList;
import java.util.HashMap;
import com.github.cliftonlabs.json_simple.*;

import edu.jsu.mcis.cs310.tas_fa23.Punch;

public class DAOUtility {

    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {
        int totalMinutes = 0;
        boolean clockedIn = false;
        Punch lastPunch = null;

        for (Punch punch : dailypunchlist) {
            if (punch.getPunchtypeid() == Punch.CLOCK_IN) {
                clockedIn = true;
            } else if (punch.getPunchtypeid() == Punch.CLOCK_OUT || punch.getPunchtypeid() == Punch.TIME_OUT) {
                if (clockedIn) {
                    int minutesWorked = calculateMinutesWorked(lastPunch, punch, shift);
                    totalMinutes += minutesWorked;
                    clockedIn = false;
                }
            }

            lastPunch = punch;
        }

        return totalMinutes;
    }

    private static int calculateMinutesWorked(Punch startPunch, Punch endPunch, Shift shift) {
        int minutesWorked = endPunch.getMinutesSinceMidnight() - startPunch.getMinutesSinceMidnight();

        if (shift.getLunchDeduct() && minutesWorked >= shift.getLunchThreshold()) {
            minutesWorked -= shift.getLunchDuration();
        }

        return minutesWorked;
    }
 public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();

        for (Punch punch : dailypunchlist) {
            HashMap<String, String> punchData = new HashMap<>();

            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", punch.getBadgeid());
            punchData.put("terminalid", punch.getTerminalid());
            punchData.put("punchtype", punch.getPunchType().toString());
            punchData.put("adjustmenttype", punch.getAdjustmentType().toString());
            punchData.put("originaltimestamp", punch.printOriginal());
            punchData.put("adjustedtimestamp", punch.printAdjusted());

            jsonData.add(punchData);
        }

        return Jsoner.serialize(jsonData);
    }
}
