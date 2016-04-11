package com.quaigon.threatsapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.inject.Inject;
import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.connection.AuthenticationRepository;
import com.quaigon.threatsapp.connection.ConnectionService;
import com.quaigon.threatsapp.connection.ServiceGenerator;
import com.quaigon.threatsapp.dto.Threat;
import com.quaigon.threatsapp.dto.Token;

import org.parceler.Parcels;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MenuActivity extends RoboActivity {


    @Inject
    AuthenticationRepository authRepo;

    @InjectView(R.id.addThreat)
    private Button addThreatButton;

    @InjectView(R.id.showThreats)
    private Button showThreatsButton;

    @InjectView(R.id.showThreatsMap)
    private Button showThreatsMapButton;

    private Token token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        addThreatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AddThreatActivity.class);
                startActivity(intent);
            }
        });

        showThreatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ThreatsListActivity.class);
                startActivity(intent);
            }
        });

        showThreatsMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MapsActivity.class);
                ConnectionService getThreatService = ServiceGenerator.createService(ConnectionService.class);
                getThreatService.getThreats().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<Threat>>() {
                            @Override
                            public void onCompleted() {
                                startActivity(intent);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<Threat> threats) {
                                intent.putExtra("threats", Parcels.wrap(threats));
                            }
                        });
            }
        });
    }



}

