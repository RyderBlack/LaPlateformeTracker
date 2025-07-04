package app.tracky.model;

import org.mindrot.jbcrypt.BCrypt;

public class Hashing {


    public static String hashPassword(String Password) {
        return BCrypt.hashpw(Password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String Password, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            throw new IllegalArgumentException("Invalid hash provided for comparison");
        }
        return BCrypt.checkpw(Password, hashedPassword);
    }
}
