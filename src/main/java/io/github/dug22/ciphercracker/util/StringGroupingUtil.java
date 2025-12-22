package io.github.dug22.ciphercracker.util;

import java.util.ArrayList;
import java.util.List;

public class StringGroupingUtil {

    public static List<String> group(String text, int groups) {
        List<StringBuilder> groupListOfStringBuilders = new ArrayList<>();
        for (int i = 0; i < groups; i++) {
            groupListOfStringBuilders.add(new StringBuilder());
        }
        for (int i = 0; i < text.length(); i++) {
            int groupIndex = i % groups;
            groupListOfStringBuilders.get(groupIndex).append(text.charAt(i));
        }

        List<String> groupList = new ArrayList<>();
        for (StringBuilder sb : groupListOfStringBuilders) {
            groupList.add(sb.toString());
        }

        return groupList;
    }
}