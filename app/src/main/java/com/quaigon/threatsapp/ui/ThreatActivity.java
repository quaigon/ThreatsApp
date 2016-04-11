package com.quaigon.threatsapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.inject.Inject;
import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.connection.AuthenticationRepository;
import com.quaigon.threatsapp.connection.ServiceGenerator;
import com.quaigon.threatsapp.dto.Threat;

import org.parceler.Parcels;

import roboguice.activity.RoboActionBarActivity;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

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

    @InjectView(R.id.threatVoteButton)
    Button threatVoteButton;

    @Inject
    AuthenticationRepository authRepo;

    private Threat threat;

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

        Glide.with(this).load(ServiceGenerator.API_BASE_URL+"TrafficThreat/rest/getImage/?uuid="+threat.getUuid()).into(threatImageView);
        threatVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThreatActivity.this, VoteActivity.class);
                intent.putExtra("uuid", threat.getUuid());
                startActivity(intent);
            }
        });
    }

}
