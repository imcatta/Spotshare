package it.catta.spotshare;

import android.app.Fragment;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;

import it.catta.spotshare.ParseEntity.Spot;


@EFragment(R.layout.fragment_show_spots)
public class ShowSpotsFragment extends Fragment implements OnMapReadyCallback,
        SlidingUpPanelLayout.PanelSlideListener, GoogleMap.OnMarkerClickListener,
        MainActivity.OnBackPressedListener, GoogleMap.OnMapClickListener {

    private GoogleMap map;
    @ViewById
    protected SlidingUpPanelLayout slidingLayout;
    @ViewById(R.id.frame_layout_spot_details_container)
    protected FrameLayout spotDetailsContainer;


    private String currentSpotId;
    private HashMap<String, Spot> spots;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);
        findAndDrawAllSpots();
        zoomToCurrentPosition();
    }

    @Override
    public boolean onMarkerClick(Marker marker){
        currentSpotId = marker.getId();
        showSmallSpotDetails();
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @Override
    public boolean onBackPressed() {
        if (slidingLayout == null) {
            return false;
        }

        switch (slidingLayout.getPanelState()) {
            case EXPANDED:
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                return true;
            case COLLAPSED:
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                return true;
            default:
                return false;
        }
    }

    private void findAndDrawAllSpots() {
        ParseQuery<Spot> query = ParseQuery.getQuery(Spot.class);
        query.findInBackground(new FindCallback<Spot>() {
            @Override
            public void done(List<Spot> list, ParseException e) {
                if (e == null) {
                    spots = new HashMap<String, Spot>(list.size());

                    for (Spot spot : list) {
                        Marker marker = drawSpot(spot);
                        spots.put(marker.getId(), spot);
                    }
                } else {
                    Log.e(ShowSpotsFragment.class.getSimpleName(), e.getMessage());
                }
            }
        });

    }

    private Marker drawSpot(Spot spot) {
        ParseGeoPoint geoPoint = spot.getPosition();

        return map.addMarker(new MarkerOptions()
                .position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()))
                .title(spot.getName())
                .snippet(spot.getDescription()));
    }

    private void zoomToCurrentPosition() {
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                map.setOnMyLocationChangeListener(null);
                LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngLocation, 12));
            }
        });
    }




    @AfterViews
    protected void initViews() {
        MapFragment mapFragment = new MapFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.frame_layout_map_container, mapFragment)
                .commit();
        slidingLayout.setPanelSlideListener(this);
        mapFragment.getMapAsync(this);
    }


    private void showSmallSpotDetails() {
        Spot spot = spots.get(currentSpotId);

        Fragment fragment = SpotDetailsSmallFragment_.builder()
                .spot(spot)
                .build();
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_spot_details_container, fragment)
                .commit();


    }

    private void showSpotDetails() {
        Spot spot = spots.get(currentSpotId);

        Fragment fragment = CommentListFragment_.builder()
                .spot(spot)
                .build();
        getFragmentManager().beginTransaction()
            .replace(R.id.frame_layout_spot_details_container, fragment)
            .commit();

    }


    @Override
    public void onPanelCollapsed(View view) {
        map.setPadding(0, 0, 0, (int) convertDpToPixel(68));
        showSmallSpotDetails();
    }

    @Override
    public void onPanelExpanded(View view) {
        showSpotDetails();
    }

    @Override
    public void onPanelSlide(View view, float v) {
    }
    @Override
    public void onPanelAnchored(View view) {
    }
    @Override
    public void onPanelHidden(View view) {
        map.setPadding(0, 0, 0, 0);
    }

    public float convertDpToPixel(float dp){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }


}
