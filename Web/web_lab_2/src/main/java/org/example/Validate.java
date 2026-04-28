package org.example;

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
            if (x < -3 || x > 5) {
                return new ValidateRes(false, "X parameter must be an integer between -3 and 5");
            }

        } catch (NumberFormatException e) {
            return new ValidateRes(false, "X parameter must be an integer");
        }

        try {
            BigDecimal y = new BigDecimal(yParam.trim());
            BigDecimal MIN_value = new BigDecimal("-5");
            BigDecimal MAX_VALUE = new BigDecimal("5");
            if (y.compareTo(MIN_value) < 0) {
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
            var r = Float.parseFloat(rParam);
            if (r < 1.0 || r > 3.0) {
                return new ValidateRes(false, "R parameter must be a number between 1.0 and 3.0");
            }

        } catch (NumberFormatException e) {
            return new ValidateRes(false, "R parameter must be a number");
        }
        return new ValidateRes(true, "All parameters are valid");
    }


    public static class ValidateRes {
        private boolean valid;
        private String message;


        public ValidateRes(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }


        public String getMessage() {
            return message;
        }
    }

}
