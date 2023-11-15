import java.util.ArrayList;
import java.util.HashMap;
import com.github.cliftonlabs.json_simple.Jsoner;

public class DAOUtility {

    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();

        for (Punch punch : dailypunchlist) {
            HashMap<String, String> punchData = new HashMap<>();

            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", punch.getBadgeid());
            punchData.put("terminalid", String.valueOf(punch.getTerminalid()));
            punchData.put("punchtype", punch.getPunchtype().toString());
            punchData.put("adjustmenttype", punch.getAdjustmenttype().toString());
            punchData.put("originaltimestamp", punch.printOriginal());
            punchData.put("adjustedtimestamp", punch.printAdjusted());

            jsonData.add(punchData);
        }

        return Jsoner.serialize(jsonData);
    }
}
