import java.math.BigDecimal;
import java.util.LinkedHashMap;


public class Validate {

    public boolean check(LinkedHashMap<String, String> params) {

        if (!params.containsKey("x") || !params.containsKey("y") || !params.containsKey("r")) {
            return false;
        }

        try {
            var x = Float.parseFloat(params.get("x"));

            if (x > 2.0 || x < -2.0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        try {
            BigDecimal y = new BigDecimal(params.get("y").trim());
            BigDecimal MIN_value = new BigDecimal("-3");
            BigDecimal MAX_VALUE = new BigDecimal("5");

            if (y.compareTo(MIN_value) < 0 || y.compareTo(MAX_VALUE) > 0) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        try {
            var r = Float.parseFloat(params.get("r"));

            if (r < 1.0 || r > 3.0) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

