package it.catta.spotshare.ParseEntity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;

/**
 * Created by andrea on 20/04/15.
 */
@ParseClassName("SpotImage")
public class SpotImage extends ParseObject {


    public static void getSpotImages(Spot spot, FindCallback<SpotImage> callback) {
        ParseQuery<SpotImage> query = ParseQuery.getQuery(SpotImage.class);
        query.whereEqualTo("referredTo", spot);
        query.findInBackground(callback);
    }


    public ParseFile getParseFileImage() {
        return getParseFile("image");
    }

    public Bitmap getImage() throws ParseException {
        byte[] imageBytes = getParseFile("image").getData();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public void setImage(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        ParseFile file = new ParseFile("image.jpeg", stream.toByteArray());
        file.saveInBackground();

        put("image", file);
    }

    public void fetchReferredTo(GetCallback<Spot> callback) {
        getParseObject("referredTo").fetchIfNeededInBackground(callback);
    }

    public void setReferredTo(Spot spot) {
        put("referredTo", ParseObject.createWithoutData("Spot", spot.getObjectId()));
    }

    public void setReferredTo(String spotObjectId) {
        put("referredTo", ParseObject.createWithoutData("Spot", spotObjectId));
    }

}
