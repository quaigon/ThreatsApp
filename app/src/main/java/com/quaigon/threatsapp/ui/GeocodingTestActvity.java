package com.quaigon.threatsapp.ui;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.utils.MyGeocoder;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

public class GeocodingTestActvity extends RoboActivity {

    @InjectView(R.id.addressTextView)
    private TextView cordsTextView;

    @InjectView(R.id.lolol)
    private Button getAddressButton;

    private String addressInput = "krakowska, opole";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoding_test_actvity);

        getAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetGeoAsyncTask getGeoAsyncTask = new GetGeoAsyncTask(GeocodingTestActvity.this);
                getGeoAsyncTask.execute();
            }
        });
    }


    private class GetGeoAsyncTask extends RoboAsyncTask<List<Address>> {

        public GetGeoAsyncTask(Context context) {
            super(context);
        }


        private List<Address> getAddresses() throws IOException,JSONException {
            Geocoder geocoder = new Geocoder(GeocodingTestActvity.this);
            List<Address> addressList = geocoder.getFromLocationName(addressInput, 1);
            return addressList;
        }


        @Override
        public List<Address> call() throws Exception {
            return getAddresses();
        }


        @Override
        protected void onSuccess(List<Address> addresses) throws Exception {
            cordsTextView.setText(addresses.get(0).getLocality());
        }
    }
}


