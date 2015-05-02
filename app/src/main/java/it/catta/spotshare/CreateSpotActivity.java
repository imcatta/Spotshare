package it.catta.spotshare;

import android.app.Activity;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;


@EActivity
public class CreateSpotActivity extends Activity implements CreateSpotFragment.OnSpotCreatedListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot);

        if (savedInstanceState != null) {
            CreateSpotFragment fragment = CreateSpotFragment_.builder().build();
            getFragmentManager().beginTransaction()
                    .add(R.id.create_spot_activity_fragment_container, fragment).commit();
        }
    }

    @Override
    public void onSpotCreated() {}

}
