package edu.gatech.seclass.crypto6300.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ParsingUtil {

    public static List<Character> getUniqueCharsString(String stringToParse) {
        Set<Character> uniqueChars = new LinkedHashSet<>();

        for (char c : stringToParse.toCharArray()) {
            if (Character.isLetter(c)) {
                uniqueChars.add(Character.toUpperCase(c));
            }
        }

        return new ArrayList<>(uniqueChars);
    }
}
