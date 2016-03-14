package com.quaigon.threatsapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;
import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.connection.AuthenticationRepository;
import com.quaigon.threatsapp.connection.ConnectionService;
import com.quaigon.threatsapp.connection.ServiceGenerator;
import com.quaigon.threatsapp.dto.Token;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;

public class LogActivity extends RoboActivity {

    @InjectView(R.id.usernameEditText)
    private EditText usernameEditText;

    @InjectView(R.id.passwordEditText)
    private EditText passwordEditText;

    @InjectView(R.id.signInButton)
    private Button signInButton;

    @InjectView(R.id.signUpButton)
    private Button signUpButton;

    @Inject
    private AuthenticationRepository authRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        this.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTokenAsyncTask getTokenAsyncTask = new GetTokenAsyncTask(LogActivity.this);
                getTokenAsyncTask.execute();
                Ln.d("lol");
            }
        });
    }


    public class GetTokenAsyncTask extends RoboAsyncTask<Void> {

        protected GetTokenAsyncTask(Context context) {
            super(context);
        }

        @Override
        public Void call() throws Exception {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

//            String username = "kamilkamil";
//            String password = "kamilkamil";
            ConnectionService connectionService = ServiceGenerator.createService(ConnectionService.class);
            Call<Token> call = connectionService.login(username, md5(password));
            Token token = call.execute().body();
            authRepo.saveToken(token);
            return null;
        }

        @Override
        protected void onException(Exception e) throws RuntimeException {
            Ln.e(e);
        }

        @Override
        protected void onSuccess(Void aVoid) throws Exception {
            Intent intent = new Intent(LogActivity.this, MenuActivity.class);
            startActivity(intent);
        }


        private String md5(String in) {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
                digest.reset();
                digest.update(in.getBytes());
                byte[] a = digest.digest();
                int len = a.length;
                StringBuilder sb = new StringBuilder(len << 1);
                for (int i = 0; i < len; i++) {
                    sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                    sb.append(Character.forDigit(a[i] & 0x0f, 16));
                }
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

