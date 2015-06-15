package it.catta.spotshare;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import it.catta.spotshare.ParseEntity.Spot;
import it.catta.spotshare.ParseEntity.SpotImage;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateSpotFragment.OnSpotCreatedListener} interface
 * to handle interaction events.
 */
@EFragment(R.layout.fragment_create_spot)
public class CreateSpotFragment extends Fragment  {

    private static final int REQUEST_TAKE_PHOTO = 1;

    private GoogleApiClient googleApiClient;
    @ViewById
    protected EditText editTextSpotName;
    @ViewById
    protected EditText editTextSpotDescription;
    @ViewById
    protected ImageView createSpotImageView;
    private String photoFilePath;


    private OnSpotCreatedListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnSpotCreatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSpotCreatedListener");
        }
    }



    private void createSpot() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (currentLocation == null) {
            Toast.makeText(getActivity(), R.string.generic_error, Toast.LENGTH_LONG).show();
        } else {
            //TODO bloccare tutto mentre si sta creando lo spot
            final Spot spot = new Spot();
            spot.setName(editTextSpotName.getText().toString());
            spot.setDescription(editTextSpotDescription.getText().toString());
            spot.setCreatedBy(ParseUser.getCurrentUser());


            ParseGeoPoint geoPoint = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
            spot.setPosition(geoPoint);

            final Bitmap image = BitmapFactory.decodeFile(photoFilePath);

            // TODO Gestire meglio questa roba
            // https://github.com/BoltsFramework/Bolts-Android
            spot.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        if (image != null) {
                            SpotImage spotImage = new SpotImage();
                            spotImage.setImage(image);
                            spotImage.setReferredTo(spot);
                            spotImage.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        listener.onSpotCreated();
                                    } else {
                                        Toast.makeText(CreateSpotFragment.this.getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        } else {
                            listener.onSpotCreated();
                        }
                    } else {
                        Toast.makeText(CreateSpotFragment.this.getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    protected void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = File.createTempFile("image", null, getActivity().getExternalCacheDir());
            } catch (IOException ex) { }

            if (photoFile == null) {
                Toast.makeText(getActivity(), R.string.generic_error, Toast.LENGTH_LONG).show();
            } else {
                photoFilePath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }



        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Bitmap image = BitmapFactory.decodeFile(photoFilePath);
            createSpotImageView.setImageBitmap(image);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_spot_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_spot_item:
                createSpot();
                return true;
            case R.id.take_photo_item:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSpotCreatedListener {
        void onSpotCreated();
    }

}
