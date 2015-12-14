package com.caitu99.log.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lion on 2015/12/12 0012.
 */
public class AnsMsg {
    private static final Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\s{1,2}\\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*?ERROR");
    public static boolean sendOrNot(String string)
    {
        Matcher matcher = pattern.matcher(string);
        if(matcher.find())
        {
            return true;
        }
        else {
            return false;
        }
    }
}
