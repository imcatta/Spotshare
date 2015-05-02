package it.catta.spotshare;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EFragment;


@EFragment
public class ShowSpotsFragment extends MapFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnCameraChangeListener {

    //private OnFragmentInteractionListener mListener;

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Circle mapCircle;


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setOnCameraChangeListener(this);
        buildGoogleApiClient();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (mapCircle == null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        }
        updateCircle(latLng);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        //updateCircle();
    }

    protected void startLocationUpdate() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
        }
    }

    private void updateCircle(LatLng latLng) {
        if (mapCircle == null) {
            mapCircle =
                    map.addCircle(
                            new CircleOptions().center(latLng).radius(50));
            int baseColor = Color.BLUE;
            mapCircle.setStrokeColor(baseColor);
            mapCircle.setStrokeWidth(2);
            mapCircle.setFillColor(Color.argb(50, Color.red(baseColor), Color.green(baseColor),
                    Color.blue(baseColor)));
        }
        mapCircle.setCenter(latLng);
        mapCircle.setRadius(50);
    }

    @AfterInject
    protected void afterInjection() {
        this.getMapAsync(this);
    }


    //TODO gestione degli errori di connessione
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(CreateSpotFragment.class.getSimpleName(), "GoogleApiClient connected");
        startLocationUpdate();
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

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/

}
