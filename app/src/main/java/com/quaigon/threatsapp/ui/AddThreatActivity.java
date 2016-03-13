package com.quaigon.threatsapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;
import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.connection.AuthenticationRepository;
import com.quaigon.threatsapp.connection.ConnectionService;
import com.quaigon.threatsapp.connection.ServiceGenerator;
import com.quaigon.threatsapp.dto.Status;

import retrofit2.Call;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;

public class AddThreatActivity extends RoboActivity {

    @Inject
    private AuthenticationRepository authRepo;

    @InjectView(R.id.typeEditText)
    private EditText typeEditText;

    @InjectView(R.id.descriptionEditText)
    private EditText descriptionEditText;

    @InjectView(R.id.coordsEditText)
    private EditText coordsEditText;

    @InjectView(R.id.addButton)
    private Button addThreatButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_threat);

        this.addThreatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendThreatAsyncTask sendThreatAsyncTask = new SendThreatAsyncTask(AddThreatActivity.this);
                sendThreatAsyncTask.execute();
            }
        });
    }

    private class SendThreatAsyncTask extends RoboAsyncTask<Void> {

        public SendThreatAsyncTask(Context context) {
            super(context);
        }

        @Override
        public Void call() throws Exception {

            String type = typeEditText.getText().toString();
            String descripiton = descriptionEditText.getText().toString();
            String coords = coordsEditText.getText().toString();

            ConnectionService connectionService = ServiceGenerator.createService(ConnectionService.class);
            Call<Status> call = connectionService.addThreat(type, descripiton, coords, authRepo.loadToken().getToken());
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
}
