package it.catta.spotshare;


import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import java.io.Serializable;
import java.util.List;

import it.catta.spotshare.ParseEntity.Comment;
import it.catta.spotshare.ParseEntity.Spot;

@EFragment
public class CommentListFragment extends ListFragment implements Serializable {

    @FragmentArg
    protected Spot spot;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showSpotDetailsHeader();
        refreshComments();
    }

    public void refreshComments() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.whereEqualTo("referredTo", spot);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> list, ParseException e) {
                if (list != null) {
                    showComments(list.toArray(new Comment[list.size()]));
                }
            }
        });
    }

    private void showComments(Comment[] comments) {
        CommentArrayAdapter adapter = new CommentArrayAdapter(getActivity(), comments);
        setListAdapter(adapter);
    }

    private void showSpotDetailsHeader() {
        View container = LayoutInflater.from(
                getActivity()).inflate(R.layout.spot_details_header_container, null);
        getListView().addHeaderView(container);
        Fragment fragment = SpotDetailsFragment_.builder()
                .spot(spot)
                .commentListFragment(this)
                .build();
        getFragmentManager().beginTransaction()
                .add(R.id.spotDetailsListViewHeaderContainer, fragment)
                .commit();

    }

}
