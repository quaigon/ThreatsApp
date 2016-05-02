package com.quaigon.threatsapp.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class GeocodingUtils {
    public static LatLng getLatLangFromAddres(String address, Context contex) throws IOException, JSONException {
        LatLng latLng = null;
        if (null != address) {
            Geocoder geocoder = new Geocoder(contex);
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
        }
        return latLng;
    }
}
