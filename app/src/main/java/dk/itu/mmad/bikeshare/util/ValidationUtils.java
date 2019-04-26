package dk.itu.mmad.bikeshare.util;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        return email.contains("@") && email.substring(email.indexOf("@")).contains(".");
    }

    public static boolean isValidPassword(String password) {
        return password.trim().length() > 8;
    }

    public static boolean isValidCreditCardNumber(String creditCardNumberStr) {
        creditCardNumberStr = creditCardNumberStr.trim();

        if (creditCardNumberStr.length() != 16) {
            return false;
        }

        try {
            long creditCardNumber = Long.parseLong(creditCardNumberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidCreditCardCVVNumber(String creditCardCVVNumberStr) {
        creditCardCVVNumberStr = creditCardCVVNumberStr.trim();

        if (creditCardCVVNumberStr.length() != 3) {
            return false;
        }

        try {
            int creditCardCVVNumber = Integer.parseInt(creditCardCVVNumberStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDouble(String doubleStr) {
        try {
            double number = Double.parseDouble(doubleStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
