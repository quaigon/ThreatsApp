package com.quaigon.threatsapp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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


                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
                final MultipartBody.Part body = MultipartBody.Part.createFormData("file", photoFile.getName(), requestBody);
                ConnectionService addPhotoService = ServiceGenerator.createService(ConnectionService.class, "multipart/form-data");
                ConnectionService addThreatService = ServiceGenerator.createService(ConnectionService.class);
                addThreatService.addThreat(typeEditText.getText().toString(), descriptionEditText.getText().toString(),
                        latLng.latitude + ";" + latLng.longitude, street + ";" + city, authRepo.loadToken().getToken())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new Func1<Status, Observable<Status>>() {
                            @Override
                            public Observable<Status> call(Status status) {
                                return addPhotoService.addImage(status.getUuid(), body).
                                        subscribeOn(Schedulers.newThread()).
                                        observeOn(AndroidSchedulers.mainThread());
                            }
                        })
                        .subscribe(new Subscriber<Status>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Ln.e(e);
                            }

                            @Override
                            public void onNext(Status status) {
                                Ln.d(status.getStatus());
                            }
                        });
            }
        });

        postPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != uuid && null != photoFile) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", photoFile.getName(), requestBody);
                    ConnectionService connectionService = ServiceGenerator.createService(ConnectionService.class, "multipart/form-data");
                    connectionService.addImage(uuid, body)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Status>() {
                                @Override
                                public void onCompleted() {
//                                    new AlertDialog.Builder(AddThreatActivity.this)
//                                            .setTitle("Sukces")
//                                            .setMessage("Dodano zagrozenie!")
//                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    AddThreatActivity.this.finish();
//                                                }
//                                            })
//                                            .setIcon(android.R.drawable.ic_dialog_alert)
//                                            .show();
                                    Ln.d("oncompleted");
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                                @Override
                                public void onNext(Status status) {
                                    Ln.d(status.getUuid());
                                }
                            });
                }
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(photoFile.getPath());
            threatImg = imageBitmap;
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private LatLng getLatLangFromAddres(String address) throws IOException, JSONException {
        LatLng latLng = null;
        if (null != address) {
            Geocoder geocoder = new Geocoder(AddThreatActivity.this);
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
        }
        return latLng;
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
