package it.catta.spotshare;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements CreateSpotFragment.OnSpotCreatedListener {

    private static final String FRAGMENT_TAG = "tag";

    @ViewById
    protected ListView drawerListView;
    @ViewById
    protected DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    public void onBackPressed() {
        Fragment currentFragment = getFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (!(currentFragment instanceof OnBackPressedListener &&
                ((OnBackPressedListener) currentFragment).onBackPressed())) {
            super.onBackPressed();
        }
    }

    @AfterViews
    protected void initViews() {
        String[] drawerItems = getResources().getStringArray(R.array.drawer_items);
        drawerListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, drawerItems));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description */
                R.string.navigation_drawer_close  /* "close drawer" description */
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (getFragmentManager().findFragmentByTag(FRAGMENT_TAG) == null) {
            drawerListView.setItemChecked(0, true);
            showShowSpotsFragment();
        }
    }



    @Override
    public void onSpotCreated() {
       Toast.makeText(this, "Spot creato", Toast.LENGTH_SHORT).show();
       showShowSpotsFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (drawerToggle != null) {
            drawerToggle.syncState();
        }

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    private void showShowSpotsFragment() {
        Fragment fragment = ShowSpotsFragment_
                .builder()
                .build();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_fragment_container, fragment, FRAGMENT_TAG)
                .commit();
    }

    private void showCreateSpotFragment() {
        Fragment fragment = CreateSpotFragment_
                .builder()
                .build();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_fragment_container, fragment, FRAGMENT_TAG)
                .commit();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        switch (position) {
            case 0:
                showShowSpotsFragment();
                break;
            case 1:
                showCreateSpotFragment();
                break;
            case 2:
                logout();
                break;
           default:
               Log.w(MainActivity.class.getSimpleName(), "Click on an unhandled item");

        }

        // Highlight the selected item, update the title, and close the drawer
        drawerListView.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerListView);
    }

    private void logout() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                Intent intent = new Intent(MainActivity.this, DispatchActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }


    public interface OnBackPressedListener {
        boolean onBackPressed();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
   */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
}
