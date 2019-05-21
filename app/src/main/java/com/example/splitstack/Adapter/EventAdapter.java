package com.example.splitstack.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.splitstack.ExpenseActivity;
import com.example.splitstack.Models.EventChildItem;
import com.example.splitstack.Models.EventParentItem;
import com.example.splitstack.ParticipantActivity;
import com.example.splitstack.R;
import com.example.splitstack.ViewHolders.EventChildViewHolder;
import com.example.splitstack.ViewHolders.EventParentViewHolder;

import java.util.List;

public class EventAdapter extends ExpandableRecyclerAdapter<EventParentViewHolder, EventChildViewHolder> {


    LayoutInflater inflater;
    String uid;


    public EventAdapter(Context context, List<ParentObject> parentItemList, String uid) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);
        this.uid = uid;

    }


    @Override
    public void onParentItemClickListener(int position) {
        super.onParentItemClickListener(position);

        //TODO: GO TO EVENT VIEW

    }

    @Override
    public EventParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_event_parent, viewGroup, false);
        return new EventParentViewHolder(view);
    }

    @Override
    public EventChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_event_child, viewGroup, false);
        return new EventChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(final EventParentViewHolder eventParentViewHolder, int i, Object o) {
        EventParentItem title = (EventParentItem) o;
        eventParentViewHolder.getTextView().setText(title.getTitle());
        eventParentViewHolder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExpenseActivity.class);
                intent.putExtra("uid", uid);
                mContext.startActivity(intent);
            }
        });

        eventParentViewHolder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventParentViewHolder.onClick(v);

            }
        });


    }

    @Override
    public void onBindChildViewHolder(EventChildViewHolder eventChildViewHolder, int i, Object o) {
        EventChildItem title = (EventChildItem) o;
        eventChildViewHolder.expenses.setText(title.getExpenses());
        eventChildViewHolder.participants.setText(title.getParticipants());
        eventChildViewHolder.btnDelete.setText("DELETE THIS");
        eventChildViewHolder.expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExpenseActivity.class);
                mContext.startActivity(intent);
            }
        });
        eventChildViewHolder.participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ParticipantActivity.class);
                mContext.startActivity(intent);
            }
        });

    }
}
