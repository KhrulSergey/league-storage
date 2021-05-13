package com.freetonleague.storage.util;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

public class StringUtil {

    private static final int HASH_LENGTH = 256;
    private static final char MINIMUM_CODE_POINT = '0';
    private static final char MAXIMUM_CODE_POINT = 'z';

    public static String generateRandomSpecialCharacters(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder()
                .withinRange(MINIMUM_CODE_POINT, MAXIMUM_CODE_POINT)
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                .build();
        return pwdGenerator.generate(length);
    }

    public static String generateRandomHash() {
        return generateRandomSpecialCharacters(HASH_LENGTH);
    }
}
