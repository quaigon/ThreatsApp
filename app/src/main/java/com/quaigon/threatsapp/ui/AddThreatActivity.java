package com.quaigon.threatsapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;
import rx.Subscriber;
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

    @InjectView(R.id.postPhoto)
    private Button postPhotoButton;

    private Bitmap threatImg;

    private String mCurrentPhotoPath;

    private String uuid = null;

    private File photoFile = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_threat);

        addThreatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = null;
                String street = streetEditText.getText().toString();
                String city = cityEditText.getText().toString();
                try {
                    latLng = getLatLangFromAddres(street + ", " + city);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ConnectionService connectionService = ServiceGenerator.createService(ConnectionService.class);
                connectionService.addThreat(typeEditText.getText().toString(), descriptionEditText.getText().toString(),
                        latLng.latitude + ";" + latLng.longitude, street + ";" + city, authRepo.loadToken().getToken())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Status>() {
                            @Override
                            public void onCompleted() {
                                new AlertDialog.Builder(AddThreatActivity.this)
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

                            @Override
                            public void onError(Throwable e) {
                                Ln.e(e);
                            }

                            @Override
                            public void onNext(Status status) {
                                uuid = status.getUuid();
                            }
                        });
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (null != photoFile) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });


        postPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != uuid) {
                    SendImageAsyncTask sendImageAsyncTask = new SendImageAsyncTask(AddThreatActivity.this, uuid);
                    sendImageAsyncTask.execute();
                }
            }
        });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            threatImg = imageBitmap;
//            imageView.setImageBitmap(imageBitmap);
//        }
//    }

    private LatLng getLatLangFromAddres(String address) throws IOException, JSONException {
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
            String city = "bc;def;ghi;4324;";


            ConnectionService connectionService = ServiceGenerator.createService(ConnectionService.class);
            Call<Status> call = connectionService.addThreat2(type, descripiton, street,city, authRepo.loadToken().getToken());
            Status status = call.execute().body();
            Ln.d(status);

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

        private String uuid;
        public SendImageAsyncTask(Context context, String uuid) {
            super(context);
            this.uuid = uuid;
        }

        @Override
        public Void call() throws Exception {
            File file = photoFile;
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), uuid);
            ConnectionService connectionService = ServiceGenerator.createService(ConnectionService.class, "multipart/form-data");
            Call<Status> call = connectionService.addImage(uuid, body);
            Status status = call.execute().body();
            Ln.d(status);
            return null;
        }

        @Override
        protected void onSuccess(Void aVoid) throws Exception {
            super.onSuccess(aVoid);
            Ln.d("super");
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

}
