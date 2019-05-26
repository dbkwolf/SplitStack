package com.example.splitstack.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.splitstack.EventActivity;
import com.example.splitstack.EventListActivity;
import com.example.splitstack.Models.EventChildItem;
import com.example.splitstack.Models.EventParentItem;
import com.example.splitstack.R;
import com.example.splitstack.ViewHolders.EventChildViewHolder;
import com.example.splitstack.ViewHolders.EventParentViewHolder;

import java.util.List;

public class EventAdapter extends ExpandableRecyclerAdapter<EventParentViewHolder, EventChildViewHolder> {


    private LayoutInflater inflater;
    private String uid;



    public EventAdapter(Context context, List<ParentObject> parentItemList, String uid ) {
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
        final EventParentItem title = (EventParentItem) o;
        eventParentViewHolder.getTextView().setText(title.getTitle());
        eventParentViewHolder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("eventId", title.getEventId());
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

        //eventChildViewHolder.btnDelete.setText("DELETE THIS");

        eventChildViewHolder.btnDelete = title.getBtnDelete();

        eventChildViewHolder.expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventActivity.class);
                mContext.startActivity(intent);
            }
        });
        eventChildViewHolder.participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventActivity.class);
                mContext.startActivity(intent);
            }
        });

    }
}
