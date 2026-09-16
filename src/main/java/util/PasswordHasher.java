package util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHasher {

    private static final Argon2 argon2 = Argon2Factory.create();

    public static String hashPassword(String password) {

        char[] passwordToBeHashed = password.toCharArray();
        try {
            return argon2.hash(2, 65536, 1, passwordToBeHashed);
        } finally {
            argon2.wipeArray(passwordToBeHashed);
        }
    }

    public static boolean verifyPassword(String password, String passwordHash) {
        char[] pswd = password.toCharArray();
        try {
            return argon2.verify(passwordHash, pswd);
        } finally {
            argon2.wipeArray(pswd);
        }
    }
}
