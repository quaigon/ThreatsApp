package com.quaigon.threatsapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quaigon.threatsapp.R;
import com.quaigon.threatsapp.dto.Threat;

import java.util.List;

/**
 * Created by Kamil on 13.03.2016.
 */
public class ThreatAdapter extends BaseAdapter {

    private List<Threat> threatsList;
    private Context contex;
    LayoutInflater layoutInflater;

    public ThreatAdapter(List<Threat> threatsList, Context contex) {
        this.threatsList = threatsList;
        this.contex = contex;
        this.layoutInflater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return threatsList.size();
    }

    @Override
    public Object getItem(int position) {
        return threatsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;


        if (null == view) {
            view = layoutInflater.inflate(R.layout.threat_item,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }


        Threat threat = (Threat) getItem(position);
        viewHolder.typeTextView.setText(threat.getType().getThreatType());
        viewHolder.descriptionTextView.setText(threat.getDescription());
        viewHolder.streetTextView.setText(threat.getCoordinates().getStreet());
        viewHolder.cityTextView.setText(threat.getCoordinates().getCity());
        return view;
    }


    static class ViewHolder {
        public TextView typeTextView;
        public TextView descriptionTextView;
        public TextView streetTextView;
        public TextView cityTextView;
        public ViewHolder (View convertView) {
            typeTextView = (TextView) convertView.findViewById(R.id.threatType);
            descriptionTextView = (TextView) convertView.findViewById(R.id.description);
            streetTextView = (TextView) convertView.findViewById(R.id.street);
            cityTextView = (TextView) convertView.findViewById(R.id.city);
        }



    }
}
