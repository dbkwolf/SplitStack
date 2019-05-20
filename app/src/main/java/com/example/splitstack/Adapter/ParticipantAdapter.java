package com.example.splitstack.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.splitstack.ParticipantItem;
import com.example.splitstack.R;

import java.util.ArrayList;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {
    private ArrayList<ParticipantItem> mExampleList;


    public static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;

        public ParticipantViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.ParticipantTextView);
            mTextView2 = itemView.findViewById(R.id.ContributionTextView);
        }
    }

    public ParticipantAdapter(ArrayList<ParticipantItem> participantItems){

        mExampleList = participantItems;

    }

    @Override
    public ParticipantViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_item,parent,false);
        ParticipantViewHolder participantViewHolder = new ParticipantViewHolder(v);

        return participantViewHolder;
    }

    @Override
    public void onBindViewHolder( ParticipantViewHolder participantViewHolder, int position) {
        ParticipantItem currentItem = mExampleList.get(position);
        participantViewHolder.mTextView1.setText(currentItem.getParticipant());
        participantViewHolder.mTextView2.setText(currentItem.getContribution());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}