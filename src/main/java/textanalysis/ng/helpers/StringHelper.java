package textanalysis.ng.helpers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.Scanner;

public final class StringHelper {

    static final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int DEFAULT_LENGTH = 8;

    public static final String random(int len) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public static final String random() {
        return random(DEFAULT_LENGTH);
    }

    public static String getResourceAsString(String fileName) {

        StringBuilder result = new StringBuilder("");

        Scanner scanner = new Scanner(StringHelper.class.getResourceAsStream(fileName));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            result.append(line).append("\n");
        }

        scanner.close();

        return result.toString();

    }

    public static String stackTraceAsString(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

}