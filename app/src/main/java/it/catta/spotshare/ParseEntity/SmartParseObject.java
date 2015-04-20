package it.catta.spotshare.ParseEntity;

import com.parse.ParseObject;

/**
 * Created by andrea on 20/04/15.
 */
public class SmartParseObject extends ParseObject {

    public String getObjectId() {
        return getString("objectId");
    }

}
