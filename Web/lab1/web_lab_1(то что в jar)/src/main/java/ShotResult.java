import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ShotResult {
    private String isShoot;
    private String x;
    private String y;
    private String r;
    private String time;
    private String execTime;

    public ShotResult(String isShoot, String x, String y, String r, String time, String execTime) {
        this.isShoot = isShoot;
        this.x = x;
        this.y=y;
        this.r = r;
        this.time = time;
        this.execTime = execTime;
    }

    public static String historyAsjson(List<ShotResult> history) {
        if (history == null) {
            return "[]";
        }

        Gson gson = new Gson();
        return gson.toJson(history);
    }
}
