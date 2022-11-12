package org.example.source;

import org.example.model.Role;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.example.model.Role.values;

public class Generator {

    public final static int NUMBER_OF_CHAR_A = 65;
    public final static int MAXIMUM_OF_NAME_SIZE = 7;
    public final static List<Role> ROLES = Collections.unmodifiableList(Arrays.asList(values()));
    public final static List<String> ROLES_STRING = Collections.unmodifiableList(Arrays.asList("ADMIN", "USER"));
    public final static int ROLES_SIZE = ROLES.size();
    public final static int ROLES_STRING_SIZE = ROLES_STRING.size();

    private static final Random random = new Random();

    public static String create() {
        int size = random.nextInt(MAXIMUM_OF_NAME_SIZE) + 1;
        char[] alphas = new char[size];

        for (int i = 0; i < size; i++) {
            alphas[i] = (char) getInt();
        }

        return String.valueOf(alphas);
    }

    public static Role getRandomRole() {
        return ROLES.get(random.nextInt(ROLES_SIZE));
    }

    public static String getRandomStringRole() {
        return ROLES_STRING.get(random.nextInt(ROLES_STRING_SIZE));
    }

    public static int getInt() {
        int difference = Math.abs(random.nextInt()) % 32;

        return NUMBER_OF_CHAR_A + difference;
    }

    public static int getInt(int bound) {
        return random.nextInt(bound);
    }

    public static Long getLong() {
        return random.nextLong();
    }
}
