package com.caitu99.log.kafka;

/**
 * Created by Lion on 2015/12/14 0014.
 */
public class TextContent {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if(content.contains("\n\t"))
        {
            content = content.substring(0,content.indexOf("\n\t"));
        }
        this.content = content;
    }
}
