package it.catta.spotshare.ParseEntity;

import com.parse.ParseClassName;

/**
 * Created by andrea on 20/04/15.
 */
@ParseClassName("Comment")
public class Comment extends SmartParseObject {


    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content", content);
    }

}
