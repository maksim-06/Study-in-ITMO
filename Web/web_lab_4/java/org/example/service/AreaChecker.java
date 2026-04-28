package org.example.service;

import java.math.BigDecimal;

public class AreaChecker {

    public String checkHit(String xParam, String yParam, String rParam) {
        var x = new BigDecimal(xParam);
        var y = new BigDecimal(yParam);
        var r = new BigDecimal(rParam);


        if (x.compareTo(BigDecimal.ZERO) <= 0 &&
                y.compareTo(BigDecimal.ZERO) >= 0 &&
                y.compareTo(r.divide(new BigDecimal(2))) <= 0 &&
                x.compareTo(r.negate()) >= 0) {
            return "Попал";
        }

        if (x.compareTo(BigDecimal.ZERO) >= 0 &&
                y.compareTo(BigDecimal.ZERO) >= 0 &&
                x.compareTo(r.divide(new BigDecimal(2))) <= 0 &&
                y.compareTo(r.subtract(x.multiply(new BigDecimal(2)))) <= 0) {
            return "Попал";
        }

        if (x.compareTo(BigDecimal.ZERO) >= 0 &&
                y.compareTo(BigDecimal.ZERO) <= 0 &&
                (x.multiply(x).add(y.multiply(y))
                        .compareTo(r.divide(new BigDecimal(2)).multiply(r.divide(new BigDecimal(2)))) <= 0)) {

            return "Попал";
        }

        return "Промах";
    }
}
