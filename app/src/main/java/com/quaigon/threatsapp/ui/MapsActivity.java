package com.quaigon.threatsapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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

import org.parceler.Parcels;

import java.util.List;

import roboguice.util.Ln;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private List<Threat> threatList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        threatList = Parcels.unwrap(getIntent().getParcelableExtra("threats"));
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
        Ln.d(threatList.size());
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

}
