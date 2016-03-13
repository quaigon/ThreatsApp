package com.quaigon.threatsapp.ui;

import android.content.Context;
import android.os.Bundle;

import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.connection.ConnectionService;
import com.quaigon.threatsapp.connection.ServiceGenerator;
import com.quaigon.threatsapp.dto.Threat;
import com.quaigon.threatsapp.ui.adapters.ThreatAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import roboguice.activity.RoboListActivity;
import roboguice.util.RoboAsyncTask;

public class ThreatsListActivity extends RoboListActivity {

    private ThreatAdapter threatAdapter;
    private List<Threat> threatsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threats_list);

        GetThreatsAsyncTask getThreatsAsyncTaskt = new GetThreatsAsyncTask(this);
        getThreatsAsyncTaskt.execute();
    }

    private class GetThreatsAsyncTask extends RoboAsyncTask<List<Threat>> {

        public GetThreatsAsyncTask(Context context) {
            super(context);
        }

        @Override
        public List<Threat> call() throws Exception {

            ConnectionService connectionService = ServiceGenerator.createService(ConnectionService.class);
            Call<List<Threat>> call = connectionService.getThreats();
            List<Threat> threats = call.execute().body();
            return threats;
        }

        @Override
        protected void onSuccess(List<Threat> threats) throws Exception {
            threatsList = threats;
            threatAdapter = new ThreatAdapter(threats, ThreatsListActivity.this);
            setListAdapter(threatAdapter);
        }
    }


}
