package com.quaigon.threatsapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.inject.Inject;
import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.connection.AuthenticationRepository;
import com.quaigon.threatsapp.connection.ConnectionService;
import com.quaigon.threatsapp.connection.ServiceGenerator;
import com.quaigon.threatsapp.dto.Status;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddThreatActivity extends RoboActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Inject
    private AuthenticationRepository authRepo;

    @InjectView(R.id.typeEditText)
    private EditText typeEditText;

    @InjectView(R.id.descriptionEditText)
    private EditText descriptionEditText;

    @InjectView(R.id.streetEditText)
    private EditText streetEditText;

    @InjectView(R.id.cityEditText)
    private EditText cityEditText;

    @InjectView(R.id.addButton)
    private Button addThreatButton;

    @InjectView(R.id.addPhotoButton)
    private Button addPhotoButton;

    @InjectView(R.id.imgView)
    private ImageView imageView;

    private Bitmap threatImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_threat);

        addThreatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = null;
                try {
                    latLng = getAddresses(streetEditText.getText().toString() + ", " + cityEditText.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ConnectionService connectionService = ServiceGenerator.createService(ConnectionService.class);
                Observable<Status> status = connectionService.addThreat("lol","lol","lol", authRepo.loadToken().getToken());
                status.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe( current -> {
                            Ln.d(current.getStatus());
                        });


                SendThreatAsyncTask sendThreatAsyncTask = new SendThreatAsyncTask(AddThreatActivity.this, latLng);

//              sendThreatAsyncTask.execute();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            threatImg = imageBitmap;
        }
    }

    private LatLng getAddresses(String address) throws IOException,JSONException {
        LatLng latLng = null;
        if (null != address) {
            Geocoder geocoder = new Geocoder(AddThreatActivity.this);
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
        }
        return latLng;
    }



    private class SendThreatAsyncTask extends RoboAsyncTask<Void> {

        private LatLng latLng;

        public SendThreatAsyncTask(Context context, LatLng latLng) {
            super(context);
            this.latLng = latLng;
        }

        @Override
        public Void call() throws Exception {

            String type = typeEditText.getText().toString();
            String descripiton = descriptionEditText.getText().toString();
            String street = streetEditText.getText().toString();
            String city = cityEditText.getText().toString();



            ConnectionService connectionService = ServiceGenerator.createService(ConnectionService.class);
            Observable<Status> call = connectionService.addThreat(type, descripiton, street, authRepo.loadToken().getToken());
//            Status status = call.execute().body();
//            Ln.d(status);

            return null;
        }


        @Override
        protected void onSuccess(Void aVoid) throws Exception {
            new AlertDialog.Builder(context)
                    .setTitle("Sukces")
                    .setMessage("Dodano zagrozenie!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AddThreatActivity.this.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    private class SendImageAsyncTask extends RoboAsyncTask<Void> {
        private Bitmap bitMap;
        private String uuid;

        public SendImageAsyncTask(Context context, Bitmap bitMap, String uuid) {
            super(context);
            this.bitMap = bitMap;
            this.uuid = uuid;
        }

        @Override
        public Void call() throws Exception {
            ConnectionService connectionService = ServiceGenerator.createService(ConnectionService.class, "multipart/form-data");




            return null;
        }


        @Override
        protected void onSuccess(Void aVoid) throws Exception {
            super.onSuccess(aVoid);
        }
    }

}
