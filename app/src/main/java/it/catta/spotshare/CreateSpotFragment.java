package it.catta.spotshare;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import bolts.Continuation;
import bolts.Task;
import it.catta.spotshare.ParseEntity.Spot;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateSpotFragment.OnSpotCreatedListener} interface
 * to handle interaction events.
 */
@EFragment(R.layout.fragment_create_spot)
public class CreateSpotFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    protected GoogleApiClient googleApiClient;
    @ViewById
    protected EditText editTextSpotName;
    @ViewById
    protected EditText editTextSpotDescription;
    @ViewById
    protected Spinner spinnerSpotLocation;


    private OnSpotCreatedListener listener;

    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buildGoogleApiClient();
        googleApiClient.connect();
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

    @AfterViews
    protected void initViews() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.spinner_spot_location_elements, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerSpotLocation.setAdapter(adapter);
    }


    @Click
    protected void createSpot() {
        //TODO bloccare tutto mentre si sta creando lo spot
        Spot spot = new Spot();
        spot.setName(editTextSpotName.getText().toString());
        spot.setDescription(editTextSpotDescription.getText().toString());
        spot.setCreatedBy(ParseUser.getCurrentUser());

        //TODO controllare il valore dello spinner
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        ParseGeoPoint geoPoint = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
        spot.setPosition(geoPoint);
        spot.saveInBackground().continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) {
                if (task.isCancelled() || task.isFaulted()) {
                    Toast.makeText(CreateSpotFragment.this.getActivity(), R.string.generic_error, Toast.LENGTH_LONG).show();
                }
                listener.onSpotCreated();
                return null;
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    //TODO gestione degli errori di connessione
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(CreateSpotFragment.class.getSimpleName(), "GoogleApiClient connected");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.w((CreateSpotFragment.class.getSimpleName()), "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(CreateSpotFragment.class.getSimpleName(), "GoogleApiClient connection failed!");
    }




    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
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
        public void onSpotCreated();
    }

}
