package org.example;


import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/areaCheck")
public class AreaCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            double time = System.nanoTime();

            String x = request.getParameter("x");
            String y = request.getParameter("y");
            String r = request.getParameter("r");


            Validate v = new Validate();
            Validate.ValidateRes isvalid = v.check(x, y, r);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentTime = LocalDateTime.now().format(formatter);

            if (isvalid.isValid()) {
                String isShoot = checkHit(x, y, r);
                Point result = new Point(isShoot, x, y, r, currentTime, String.format("%.3f ms", (System.nanoTime() - time) / 1_000_000.0));
                saveToApplicationContext(request, result);

                request.setAttribute("result", result);
                request.getRequestDispatcher("/result.jsp").forward(request, response);
            } else {
                response.sendError(400, isvalid.getMessage());
            }

        } catch (Exception e) {
            response.sendError(500, "Server error: " + e.getMessage());
        }
    }


    public String checkHit(String xParams, String yParams, String rParams) {
        var x = Integer.parseInt(xParams);
        BigDecimal y = new BigDecimal(yParams.trim());


        BigDecimal xDec = new BigDecimal(xParams.trim());
        BigDecimal rDec = new BigDecimal(rParams.trim());


        if (x >= 0 &&
                y.compareTo(BigDecimal.ZERO) >= 0 &&
                xDec.compareTo(rDec) <= 0 &&
                y.compareTo(rDec) <= 0) {
            return "Попал";
        }


        if (x <= 0 &&
                y.compareTo(BigDecimal.ZERO) >= 0 &&
                xDec.compareTo(rDec.divide(new BigDecimal(2)).negate()) >= 0 &&
                y.compareTo(xDec.multiply(new BigDecimal(2)).add(rDec)) <= 0) {
            return "Попал";
        }


        if (x >= 0 &&
                y.compareTo(BigDecimal.ZERO) <= 0 &&
                xDec.compareTo(rDec.divide(new BigDecimal(2))) <= 0 &&
                y.compareTo(rDec.divide(new BigDecimal(2)).negate()) >= 0) {
            BigDecimal xSq = xDec.multiply(xDec);
            BigDecimal ySq = y.multiply(y);
            BigDecimal rSq = rDec.multiply(rDec);
            if (xSq.add(ySq).compareTo(rSq) <= 0) {
                return "Попал";
            }
        }
        return "Промах";
    }

    private void saveToApplicationContext(HttpServletRequest request, Point result) {
        ServletContext context = getServletContext();
        List<Point> results = (List<Point>) context.getAttribute("results");

        if (results == null) {
            results = new ArrayList<>();
        }

        results.add(result);
        context.setAttribute("results", results);
    }
}
