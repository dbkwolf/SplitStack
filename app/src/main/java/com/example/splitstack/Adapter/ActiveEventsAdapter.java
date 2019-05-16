package com.example.splitstack.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.splitstack.ActiveEventsItem;
import com.example.splitstack.DBUtility.EventData;
import com.example.splitstack.R;

import java.util.ArrayList;

public class ActiveEventsAdapter extends RecyclerView.Adapter<ActiveEventsAdapter.ActiveEventsViewHolder> {
    private ArrayList<EventData> mExampleList;


    public static class ActiveEventsViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;

        public ActiveEventsViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.eventName_textView);
            mTextView2 = itemView.findViewById(R.id.contributor_textView);
        }
    }

    public ActiveEventsAdapter(ArrayList<EventData> eventItems){

        mExampleList = eventItems;

    }

    @Override
    public ActiveEventsViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activeevents_item,parent,false);
        ActiveEventsViewHolder activeEventsViewHolder = new ActiveEventsViewHolder(v);

        return activeEventsViewHolder;
    }

    @Override
    public void onBindViewHolder( ActiveEventsViewHolder activeEventsViewHolder, int position) {
        EventData currentItem = mExampleList.get(position);
        activeEventsViewHolder.mTextView1.setText(currentItem.getEventName());
        activeEventsViewHolder.mTextView2.setText("test");
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}