package it.catta.spotshare;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;

import it.catta.spotshare.ParseEntity.Comment;
import it.catta.spotshare.ParseEntity.Spot;
import it.catta.spotshare.ParseEntity.SpotImage;
import it.catta.spotshare.ParseEntity.Vote;


public class SpotshareApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);

        ParseACL acl = new ParseACL();
        acl.setPublicWriteAccess(false);
        acl.setPublicReadAccess(true);
        ParseACL.setDefaultACL(acl, true);

        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Spot.class);
        ParseObject.registerSubclass(SpotImage.class);
        ParseObject.registerSubclass(Vote.class);

        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));;
    }

}
