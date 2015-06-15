package it.catta.spotshare;


import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.List;

import it.catta.spotshare.ParseEntity.Spot;
import it.catta.spotshare.ParseEntity.SpotImage;
import it.catta.spotshare.ParseEntity.Vote;


@EFragment(R.layout.fragment_spot_details)
public class SpotDetailsFragment extends Fragment implements RatingBar.OnRatingBarChangeListener,
        SubmitCommentDialogFragment.SubmitCommentEventsHandler, Serializable {

    @ViewById
    protected ParseImageView spotDetailImageParseImageView;
    @ViewById
    protected TextView spotDetailNameTextView;
    @ViewById
    protected TextView spotDetailDescriptionTextView;
    @ViewById
    protected RatingBar voteSpotRatingBar;
    @FragmentArg
    protected Spot spot;

    @FragmentArg
    protected CommentListFragment commentListFragment;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


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
        SpotImage.getSpotImages(spot, new FindCallback<SpotImage>() {
            @Override
            public void done(List<SpotImage> list, ParseException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        spotDetailImageParseImageView.setVisibility(View.VISIBLE);
                        spotDetailImageParseImageView.setParseFile(list.get(0).getParseFileImage());
                        spotDetailImageParseImageView.loadInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                if (e != null) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } else {
                    Log.i(SpotDetailsFragment.class.getSimpleName(), e.getMessage());
                }
            }
        });

        Vote.getUserVote(spot, new GetCallback<Vote>() {
            @Override
            public void done(Vote vote, ParseException e) {
                if (e == null) {
                    voteSpotRatingBar.setRating((float) vote.getValue());
                }
                voteSpotRatingBar.setOnRatingBarChangeListener(SpotDetailsFragment.this);
                voteSpotRatingBar.setIsIndicator(false);
            }
        });

        spotDetailNameTextView.setText(spot.getName());
        spotDetailDescriptionTextView.setText(spot.getDescription());
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
        if (fromUser) {
           Vote.getUserVote(spot, new GetCallback<Vote>() {
                @Override
                public void done(Vote vote, ParseException e) {
                    if (vote == null) {
                        vote = new Vote();
                        vote.setReferredTo(spot);
                        vote.setCreatedBy(ParseUser.getCurrentUser());
                    }
                    vote.setValue((double) rating);
                    vote.saveInBackground();
                }
            });
        }
    }

    @Click(R.id.spotDetailsCommentButton)
    protected void showCommentDialog() {
        DialogFragment dialog = SubmitCommentDialogFragment_.builder()
                .callback(this)
                .spot(spot)
                .build();
        dialog.show(getFragmentManager(), null);
    }

    @Override
    public void onSubmitCommentError(ParseException e) {
        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSubmitCommentDone() {
        commentListFragment.refreshComments();
    }


}
