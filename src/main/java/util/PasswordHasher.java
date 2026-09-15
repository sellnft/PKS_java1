package util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHasher {

    public static String hashPassword(String password) {
        Argon2 argon2 = Argon2Factory.create();

        char[] passwordToBeHashed = password.toCharArray();
        try {
            return argon2.hash(1000, 65536, 1, passwordToBeHashed);
        } finally {
            argon2.wipeArray(passwordToBeHashed);
        }
    }
}
