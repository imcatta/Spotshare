package it.catta.spotshare;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import it.catta.spotshare.ParseEntity.Comment;
import it.catta.spotshare.ParseEntity.Spot;
import it.catta.spotshare.ParseEntity.SpotImage;
import it.catta.spotshare.ParseEntity.Vote;


public class SpotshareApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();

        // uncomment to enable Local Datastore.
        Parse.enableLocalDatastore(this);


        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Spot.class);
        ParseObject.registerSubclass(SpotImage.class);
        ParseObject.registerSubclass(Vote.class);

        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
    }

}
