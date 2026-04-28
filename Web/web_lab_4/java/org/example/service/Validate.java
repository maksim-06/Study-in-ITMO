package org.example.service;

import java.math.BigDecimal;

public class Validate {

    public ValidateRes check(String xParam, String yParam, String rParam) {
        if (xParam.trim().isEmpty() || xParam == null) {
            return new ValidateRes(false, "X parameter cannot be empty");
        }

        if (yParam.trim().isEmpty() || yParam == null) {
            return new ValidateRes(false, "Y parameter cannot be empty");
        }

        if (rParam.trim().isEmpty() || rParam == null) {
            return new ValidateRes(false, "R parameter cannot be empty");
        }

        try {
            BigDecimal x = new BigDecimal(xParam);
            BigDecimal min_x = new BigDecimal("-3");
            BigDecimal max_x = new BigDecimal("3");
            if (x.compareTo(min_x) < 0) {
                return new ValidateRes(false, "Y parameter cannot be less than -3");
            }

            if (x.compareTo(max_x) > 0) {
                return new ValidateRes(false, "Y parameter cannot be greater than 3");
            }


        } catch (NumberFormatException e) {
            return new ValidateRes(false, "X parameter must be number");
        }

        try {
            BigDecimal y = new BigDecimal(yParam);
            BigDecimal min_y = new BigDecimal("-3");
            BigDecimal max_y = new BigDecimal("5");
            if (y.compareTo(min_y) < 0) {
                return new ValidateRes(false, "Y parameter cannot be less than -3");
            }

            if (y.compareTo(max_y) > 0) {
                return new ValidateRes(false, "Y parameter cannot be greater than 3");
            }

        } catch (NumberFormatException e) {
            return new ValidateRes(false, "Y parameter must be number");
        }

        try {
            BigDecimal r = new BigDecimal(rParam);
            BigDecimal min_r = new BigDecimal("0");
            BigDecimal max_r = new BigDecimal("3");

            if (r.compareTo(min_r) <= 0) {
                return new ValidateRes(false, "R parameter cannot be less than 0");
            }

            if (r.compareTo(max_r) > 0) {
                return new ValidateRes(false, "R parameter cannot be greater than 3");
            }
        } catch (NumberFormatException e) {
            return new ValidateRes(false, "R parameter must be number");
        }

        return new ValidateRes(true, "All parameters are valid");
    }
}
