package com.quaigon.threatsapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.connection.ConnectionService;
import com.quaigon.threatsapp.connection.ServiceGenerator;
import com.quaigon.threatsapp.dto.Threat;
import com.quaigon.threatsapp.ui.adapters.ThreatAdapter;

import org.parceler.Parcels;

import java.util.List;

import roboguice.activity.RoboListActivity;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ThreatsListActivity extends RoboListActivity {

    private ThreatAdapter threatAdapter;
    private List<Threat> threatsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threats_list);

        ConnectionService getThreatService = ServiceGenerator.createService(ConnectionService.class);
        getThreatService.getThreats().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Threat>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Threat> threats) {
                        threatAdapter = new ThreatAdapter(threats, ThreatsListActivity.this);
                        setListAdapter(threatAdapter);
                        setThreatsList(threats);
                    }
                });
    }

    public void setThreatsList(List<Threat> threatsList) {
        this.threatsList = threatsList;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(ThreatsListActivity.this, ThreatActivity.class);
        Threat threat = threatsList.get(position);
        intent.putExtra("threat", Parcels.wrap(threat));
        startActivity(intent);

    }
}
