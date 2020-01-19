package ru.job4j.tourist.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ru.job4j.tourist.App;
import ru.job4j.tourist.R;
import ru.job4j.tourist.db.dao.LocationDao;
import ru.job4j.tourist.db.model.ModelLocation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location location;
    private static final String TAG = MapsActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 1;

    @Inject
    public LocationDao locationDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        App.getComponent().inject(this);
//        locationDao = App.getComponent().getLocationDao();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button current = findViewById(R.id.current);
        current.setOnClickListener(this::getCurrentLocation);
        Button allPositions = findViewById(R.id.allPositions);
        allPositions.setOnClickListener(this::viewAllPositions);
    }

    private void getCurrentLocation(View view) {
        if (location != null) {
            ModelLocation modelLocation = new ModelLocation();
            modelLocation.latitude = location.getLatitude();
            modelLocation.longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (!address.isEmpty()) {
                    modelLocation.title = String.format("%s, %s, %s", address.get(0).getLocality(), address.get(0).getThoroughfare(), address.get(0).getSubThoroughfare());
                }
                if (!modelLocation.equals(new ModelLocation())) {
                    locationDao.updateLocation(modelLocation)
                            .subscribeOn(Schedulers.io())
                            .subscribe(new DisposableSingleObserver<Integer>() {
                                @Override
                                public void onSuccess(Integer integer) {
                                    Log.d(TAG, String.format("id = %s", integer));
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, e.getLocalizedMessage());
                                }
                            });
                }
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
            displayCurrentPosition(modelLocation);
        }
    }

    private void viewAllPositions(View view) {
        Intent intent = new Intent(getApplicationContext(), LocationListActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationListener loc = new LocationListener() {
            @Override
            public void onLocationChanged(Location mLocation) {
                location = mLocation;
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
        };
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, loc);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ModelLocation modelLocation = data.getParcelableExtra("location");
            if (modelLocation != null) {
                removeAllMarkers();
                displayCurrentPosition(modelLocation);
            }

        }
    }

    private void displayCurrentPosition(ModelLocation modelLocation) {
        LatLng currentPosition = new LatLng(modelLocation.latitude, modelLocation.longitude);
        String title = "current position";
        if (modelLocation.title != null && !modelLocation.title.isEmpty()) {
            title = modelLocation.title;
        }
        MarkerOptions marker = new MarkerOptions().position(currentPosition).title(title);
        marker.flat(true);
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));
    }

    private void removeAllMarkers() {
        mMap.clear();
    }
}
