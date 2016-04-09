package com.quaigon.threatsapp.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.dto.Threat;

import org.parceler.Parcels;

import roboguice.activity.RoboActionBarActivity;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

public class ThreatActivity extends RoboActionBarActivity {



    @InjectView(R.id.threatCity)
    TextView cityTextView;

    @InjectView(R.id.threatDescription)
    TextView descriptionTextView;

    @InjectView(R.id.threatStreet)
    TextView streetTextView;

    @InjectView(R.id.threatType)
    TextView typeTextView;

    @InjectView(R.id.threatImage)
    ImageView threatImageView;

    Threat threat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        threat = Parcels.unwrap(getIntent().getParcelableExtra("threat"));

        cityTextView.setText(threat.getCoordinates().getCity());
        descriptionTextView.setText(threat.getDescription());
        streetTextView.setText(threat.getCoordinates().getStreet());
        typeTextView.setText(threat.getType().getThreatType());

    }

}
