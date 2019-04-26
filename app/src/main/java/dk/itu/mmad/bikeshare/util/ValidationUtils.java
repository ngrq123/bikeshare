package dk.itu.mmad.bikeshare.util;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        return email.contains("@") && email.substring(email.indexOf("@")).contains(".");
    }

    public static boolean isValidPassword(String password) {
        return password.length() > 8;
    }

}
