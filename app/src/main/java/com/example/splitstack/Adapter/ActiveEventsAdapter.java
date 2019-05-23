package com.example.splitstack.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.splitstack.DBUtility.EventData;
import com.example.splitstack.R;

import java.util.ArrayList;

public class ActiveEventsAdapter extends RecyclerView.Adapter<ActiveEventsAdapter.ActiveEventsViewHolder> {

    private ArrayList<EventData> userEventDataList;


    public static class ActiveEventsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEventName;
        public TextView tvParticipant;

        public ActiveEventsViewHolder(View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.eventName_textView);
            tvParticipant = itemView.findViewById(R.id.contributor_textView);
        }
    }

    public ActiveEventsAdapter(ArrayList<EventData> eventItems){

        userEventDataList = eventItems;

    }

    @Override
    public ActiveEventsViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_event_item,parent,false);
        ActiveEventsViewHolder activeEventsViewHolder = new ActiveEventsViewHolder(v);

        return activeEventsViewHolder;
    }

    @Override
    public void onBindViewHolder( ActiveEventsViewHolder activeEventsViewHolder, int position) {
        EventData currentItem = userEventDataList.get(position);





        activeEventsViewHolder.tvEventName.setText(currentItem.getEventName());
        activeEventsViewHolder.tvParticipant.setText(currentItem.getTotalExpenses());
    }

    @Override
    public int getItemCount() {
        return userEventDataList.size();
    }
}