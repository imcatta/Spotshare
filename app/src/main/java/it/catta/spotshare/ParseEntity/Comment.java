package it.catta.spotshare.ParseEntity;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by andrea on 20/04/15.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject {



    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content", content);
    }

    public ParseUser getCreatedBy() {
        return getParseUser("createdBy");
    }

    public void setCreatedBy(ParseUser creator) {
        put("createdBy", ParseObject.createWithoutData("_User", creator.getObjectId()));
    }

    public Spot getReferredTo() {
        return ParseObject.createWithoutData(Spot.class, getParseObject("referredTo").getObjectId());
    }

    public void setReferredTo(Spot spot) {
        put("referredTo", spot);
    }
}
