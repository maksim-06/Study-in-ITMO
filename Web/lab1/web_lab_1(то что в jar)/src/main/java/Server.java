import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.LinkedHashMap;

class Server {
    static ArrayList<ShotResult> history = new ArrayList<>();
    public static void main(String[] args) {
        FCGIInterface fcgiInterface = new FCGIInterface();
        while (fcgiInterface.FCGIaccept() >= 0) {
            try {
                if (FCGIInterface.request != null && FCGIInterface.request.params != null) {
                    var method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
                    var path = FCGIInterface.request.params.getProperty("PATH_INFO");

                    if ("GET".equals(method) && "/history".equals(path)) {
                        String gsonhistory = ShotResult.historyAsjson(history);
                        String response = "Content-Type: application/json; charset=utf-8\r\n\r\n" + gsonhistory;
                        FCGIInterface.request.outStream.write(response.getBytes(StandardCharsets.UTF_8));
                        FCGIInterface.request.outStream.flush();

                    } else if ("GET".equals(method)) {
                        double time = System.nanoTime();
                        var queryString = FCGIInterface.request.params.getProperty("QUERY_STRING");
                        if (!Objects.equals(queryString, "") && queryString != null) {
                            LinkedHashMap<String, String> a = getValues(queryString);
                            Validate v = new Validate();
                            boolean isvalid = v.check(a);
                            if (isvalid) {
                                String isShoot = checkHit(a);
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                String currentTime = LocalDateTime.now().format(formatter);
                                ShotResult result = new ShotResult(isShoot, a.get("x"), a.get("y"), a.get("r"), currentTime, String.format("%.3f ms", (System.nanoTime() - time) / 1_000_000.0 ));
                                history.add(result);
                                String response = resp(isShoot, a.get("x"), a.get("y"), a.get("r"), String.format("%.3f ms", (System.nanoTime() - time) / 1_000_000.0 ));
                                FCGIInterface.request.outStream.write(response.getBytes(StandardCharsets.UTF_8));
                                FCGIInterface.request.outStream.flush();
                            } else {
                                String errorResponse = err("Invalid parameters");
                                FCGIInterface.request.outStream.write(errorResponse.getBytes(StandardCharsets.UTF_8));
                                FCGIInterface.request.outStream.flush();
                            }
                        } else {
                            String errorResponse = err("Empty query string");
                            FCGIInterface.request.outStream.write(errorResponse.getBytes(StandardCharsets.UTF_8));
                            FCGIInterface.request.outStream.flush();
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("IO Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private static LinkedHashMap<String, String> getValues(String stroka) {
        String[] args = stroka.split("&");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (String s : args) {
            String[] arg = s.split("=");
            if (arg.length == 2) {
                map.put(arg[0], arg[1]);
            }
        }
        return map;
    }

    public static String checkHit(LinkedHashMap<String, String> params) {
        try {
            var x = Float.parseFloat(params.get("x"));
            var y = Float.parseFloat(params.get("y"));
            var r = Float.parseFloat(params.get("r"));


            if (x >= -r && x <= 0 && y >= 0 && y <= r) {
                return "Попал";
            }


            if (x >= 0 && y <= 0 && (x * x + y * y) <= (r / 2) * (r / 2)) {
                return "Попал";
            }


            if (x >= -r/2 && x <= 0 && y >= -r/2 && y <= 0) {
                if (y >= -x - r/2) {
                    return "Попал";
                }
            }

            return "Промах";
        } catch (Exception e) {
            return "Ошибка вычислений";
        }
    }

    private static String resp(String isShoot, String x, String y, String r, String wt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = LocalDateTime.now().format(formatter);

        return """
                Content-Type: application/json; charset=utf-8\r\n\r\n
                {
                    "isShoot": "%s",
                    "x": "%s",
                    "y": "%s",
                    "r": "%s",
                    "time": "%s",
                    "execTime": "%s"
                }
                """.formatted(isShoot, x, y, r, currentTime, wt);
    }

    public static String err(String message) {
        return """
                Content-Type: application/json; charset=utf-8\r\n\r\n
                {
                    "error": "%s"
                }
                """.formatted(message);
    }
}