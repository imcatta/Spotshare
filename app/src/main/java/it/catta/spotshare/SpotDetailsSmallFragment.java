package it.catta.spotshare;


import android.app.Fragment;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import it.catta.spotshare.ParseEntity.Spot;


@EFragment(R.layout.spot_details_small_container_layout)
public class SpotDetailsSmallFragment extends Fragment {


    @FragmentArg
    protected Spot spot;
    @ViewById
    protected TextView spotNameTextView;
    @ViewById
    protected RatingBar smallSpotDetailsRatingBar;


    @AfterViews
    protected void initViews() {
        updateViews(spot);
        spot.fetchInBackground(new GetCallback<Spot>() {
            @Override
            public void done(Spot spot, ParseException e) {
                if (spot != null) {
                    updateViews(spot);
                }
            }
        });
    }

    private void updateViews(Spot spot) {
        spotNameTextView.setText(spot.getName());
        smallSpotDetailsRatingBar.setRating((float) spot.getVoteAverage());
    }

}
