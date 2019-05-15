package com.example.splitstack.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.splitstack.ExpenseActivity;
import com.example.splitstack.Models.ChildItem;
import com.example.splitstack.Models.ParentItem;
import com.example.splitstack.ParticipantActivity;
import com.example.splitstack.R;
import com.example.splitstack.ViewHolders.TitleChildViewHolder;
import com.example.splitstack.ViewHolders.TitleParentViewHolder;

import java.util.List;

public class MyAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder, TitleChildViewHolder> {


    LayoutInflater inflater;


    public MyAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);

    }


    @Override
    public void onParentItemClickListener(int position) {
        super.onParentItemClickListener(position);


    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_parent, viewGroup, false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_child, viewGroup, false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(final TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        ParentItem title = (ParentItem) o;
        titleParentViewHolder.getTextView().setText(title.getTitle());
    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int i, Object o) {
        ChildItem title = (ChildItem)o;
        titleChildViewHolder.expenses.setText(title.getExpenses());
        titleChildViewHolder.participants.setText(title.getParticipants());
        titleChildViewHolder.location.setText(title.getLocation());
        titleChildViewHolder.expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExpenseActivity.class);
                mContext.startActivity(intent);
            }
        });
        titleChildViewHolder.participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ParticipantActivity.class);
                mContext.startActivity(intent);
            }
        });

    }
}
