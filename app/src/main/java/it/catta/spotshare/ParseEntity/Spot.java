package it.catta.spotshare.ParseEntity;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by andrea on 20/04/15.
 */
@ParseClassName("Spot")
public class Spot extends SmartParseObject {

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getDescription() {
        return getString("description");
    }

    public void fetchCreatedBy(GetCallback<ParseUser> callback) {
        getParseObject("createdBy").fetchIfNeededInBackground(callback);
    }

    public void setCreatedBy(ParseUser creator) {
        put("createdBy", ParseObject.createWithoutData("_User", creator.getObjectId()));
    }

    public void setCreatedBy(String creatorObjectId) {
        put("createdBy", ParseObject.createWithoutData("_User", creatorObjectId));
    }

    public ParseGeoPoint getPosition() {
        return getParseGeoPoint("position");
    }

    public void setPosition(ParseGeoPoint position) {
        put("position", position);
    }

}
