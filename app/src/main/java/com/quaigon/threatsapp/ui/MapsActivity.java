package com.quaigon.threatsapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Permission;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.dto.Threat;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.parceler.Parcels;

import java.util.List;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

public class MapsActivity extends RoboActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;

    private LocationManager mLocationManager;


    private GoogleApiClient googleApiClient;

    private List<Threat> threatList;

    @InjectView(R.id.fab)
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        threatList = Parcels.unwrap(getIntent().getParcelableExtra("threats"));

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings({"MissingPermission"})
            public void onClick(View v) {
//                Location location = LocationServices.FusedLocationApi.getLastLocation(
//                        googleApiClient);
                Location location = getLastKnownLocation();
                Ln.d(location.getLatitude() + "  " + location.getLongitude());
            }
        });
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

        // Add a marker in Sydney and move the camera
        LatLng cracow = new LatLng(50.0636244, 19.9382476);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(cracow)
                .zoom(13).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (null != threatList) {
            for (Threat threat : threatList) {
                Double latitude = Double.parseDouble(threat.getCoordinates().getHorizontal());
                Double longitude = Double.parseDouble(threat.getCoordinates().getVertical());
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).snippet(threat.getUuid()));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        for (Threat threat : threatList) {
                            if (marker.getSnippet().equals(threat.getUuid())) {
                                Intent intent = new Intent(MapsActivity.this, ThreatActivity.class);
                                intent.putExtra("threat", Parcels.wrap(threat));
                                startActivity(intent);
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }


    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        List<String> providers2 = mLocationManager.getAllProviders();
        Location bestLocation = null;
        for (String provider : providers) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        RxPermissions.getInstance(this).request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {

                        }
                    });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
