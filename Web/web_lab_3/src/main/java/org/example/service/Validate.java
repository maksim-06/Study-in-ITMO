package org.example.service;

import java.math.BigDecimal;

public class Validate {

    public ValidateRes check(String xParam, String yParam, String rParam) {
        if (xParam == null || xParam.isEmpty()) {
            return new ValidateRes(false, "X parameter cannot be empty");
        }

        if (yParam == null || yParam.isEmpty()) {
            return new ValidateRes(false, "Y parameter cannot be empty");
        }

        if (rParam == null || rParam.isEmpty()) {
            return new ValidateRes(false, "R parameter cannot be empty");
        }

        try {
            var x = Integer.parseInt(xParam);
            if (x < -5 || x > 5) {
                return new ValidateRes(false, "X parameter must be between -5 and 5");
            }
        } catch (NumberFormatException e) {
            return new ValidateRes(false, "X parameter must be an integer");
        }

        try {
            BigDecimal y = new BigDecimal(yParam);
            BigDecimal MIN_VALUE = new BigDecimal("-5");
            BigDecimal MAX_VALUE = new BigDecimal("5");
            if (y.compareTo(MIN_VALUE) < 0) {
                return new ValidateRes(false, "Y parameter cannot be less than -5");
            }

            if (y.compareTo(MAX_VALUE) > 0) {
                return new ValidateRes(false, "Y parameter cannot be greater than 5");
            }

        } catch (NumberFormatException e) {
            return new ValidateRes(false, "Y parameter must be a number");
        } catch (Exception e) {
            return new ValidateRes(false, "Invalid format for Y parameter");
        }

        try {
            var r = Integer.parseInt(rParam);
            if (r < 1 || r > 5) {
                return new ValidateRes(false, "R parameter must be between 1 and 5");
            }
        } catch (NumberFormatException e) {
            return new ValidateRes(false, "R parameter must be an integer");
        }

        return new ValidateRes(true, "All parameters are valid");
    }
}
