package it.catta.spotshare;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Trace;


@EActivity
public class CreateSpotActivity extends ActionBarActivity implements CreateSpotFragment.OnSpotCreatedListener{

    @Override
    @Trace
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot);

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        // Create a new Fragment to be placed in the activity layout
       CreateSpotFragment fragment = CreateSpotFragment_.builder().build();

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.create_spot_activity_fragment_container, fragment).commit();

    }

    @Override
    public void onSpotCreated() {}

}
