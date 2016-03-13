package com.quaigon.threatsapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.inject.Inject;
import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.connection.AuthenticationRepository;
import com.quaigon.threatsapp.dto.Token;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;


public class MenuActivity extends RoboActivity {


    @Inject
    AuthenticationRepository authRepo;

    @InjectView(R.id.addThreat)
    private Button addThreatButton;

    @InjectView(R.id.showThreats)
    private Button showThreatsButton;

    private Token token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.token = authRepo.loadToken();

        this.addThreatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AddThreatActivity.class);
                startActivity(intent);
            }
        });

        this.showThreatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ThreatsListActivity.class);
                startActivity(intent);
            }
        });

    }



}

