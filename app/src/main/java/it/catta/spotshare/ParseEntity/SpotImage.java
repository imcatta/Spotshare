package it.catta.spotshare.ParseEntity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by andrea on 20/04/15.
 */
@ParseClassName("SpotImage")
public class SpotImage extends SmartParseObject {

    public Bitmap getImage() throws ParseException {
        byte[] imageBytes = getParseFile("image").getData();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public void setImage(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        assert(image.compress(Bitmap.CompressFormat.JPEG, 90, stream));
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
