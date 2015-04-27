package android.sample.mapsearch;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.sample.mapsearch.fragment.RecyclerFragment;
import android.sample.mapsearch.fragment.SearchAdapter;
import android.sample.mapsearch.support.Constants;
import android.sample.mapsearch.support.SearchTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, LocationListener {

    private SearchAdapter mRecyclerviewAdapter;
    private RecyclerFragment mRecyclerviewFragment;
    private LocationManager mLocationManager;
    private String mLocation;
    private String mProvider;
    private static TextView mTVInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerviewFragment = new RecyclerFragment();
        if (mRecyclerviewFragment != null) {
            getFragmentManager().beginTransaction().add(android.R.id.content, mRecyclerviewFragment).commit();
        }


        // Get the location manager
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (mLocationManager != null) {
            Criteria criteria = new Criteria();
            mProvider = mLocationManager.getBestProvider(criteria, false);
            Location location = mLocationManager.getLastKnownLocation(mProvider);
        }

        mTVInfo = (TextView) findViewById(R.id.tvInfoMsg);

    }

    public static TextView getMsgTextView() {
        return mTVInfo;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mLocationManager != null && mProvider != null) {
            mLocationManager.requestLocationUpdates(mProvider, Constants.MIN_TIME_IN_MS, Constants.MIN_DISTANCE_IN_METER, this);
        }

        //Adapter and LayoutManager created after Main Activity is created.
        if (mRecyclerviewFragment != null) {
            mRecyclerviewAdapter = mRecyclerviewFragment.getAdapter();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        mLocation = lat + "," + lng;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    /**
     * Here we create and execurte #SearchTask for the search query.
     * @param s
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String s) {
        if (s != null) {
            String query = null;
            try {
                query = URLEncoder.encode(s.trim(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String url = new String(Constants.SEARCHURL);
            url = url.replaceFirst("%@", Constants.APIKEY);
            url = url.replaceFirst("%@", Constants.RADIUS);
            url = url.replaceFirst("%@", query);
            if (mLocation != null) {
                url = url.replaceFirst("%@", mLocation);
            } else {
                url = url.replaceFirst("%@", "");
            }
            new SearchTask(url, MainActivity.this, false, mRecyclerviewAdapter).execute();
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
