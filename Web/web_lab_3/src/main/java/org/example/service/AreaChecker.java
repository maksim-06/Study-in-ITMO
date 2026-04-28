package org.example.service;


import java.math.BigDecimal;

public class AreaChecker {

    public String checkHit(String xParam, String yParam, String rParam) {
        var x = Integer.parseInt(xParam);
        BigDecimal y = new BigDecimal(yParam);
        var r = Integer.parseInt(rParam);

        var rDec = new BigDecimal(rParam);
        var xDec = new BigDecimal(xParam);

        if (x <= 0 &&
                y.compareTo(BigDecimal.ZERO) <= 0 &&
                y.compareTo(rDec.divide(new BigDecimal(2)).negate()) >= 0) {
            return "Попал";
        }

        if (x >= 0 && y.compareTo(BigDecimal.ZERO) >= 0 &&
                xDec.multiply(xDec).add(y.multiply(y))
                        .compareTo(rDec.multiply(rDec)) <= 0) {
            return "Попал";
        }

        if (x >= 0 &&
                y.compareTo(BigDecimal.ZERO) <= 0 &&
                y.compareTo(xDec.divide(new BigDecimal(2))
                        .subtract(rDec.divide(new BigDecimal(2)))) >= 0) {
            return "Попал";
        }
        return "Промах";
    }
}
