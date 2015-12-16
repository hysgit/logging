package com.caitu99.log.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lion on 2015/12/12 0012.
 */
@Service
public class AnsMsg {
    @Autowired
    private  AppConfig appConfig;

    public  boolean sendOrNot(String string) {
        Pattern pattern = Pattern.compile(appConfig.pattern);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }
}
