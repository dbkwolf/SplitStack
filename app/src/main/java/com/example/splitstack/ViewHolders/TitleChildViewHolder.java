package com.example.splitstack.ViewHolders;

import android.view.View;
import android.widget.TextView;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.example.splitstack.R;

public class TitleChildViewHolder extends ChildViewHolder {

    public TextView expenses, participants, location;
    public TitleChildViewHolder(View itemView){
        super(itemView);
        expenses = (TextView) itemView.findViewById(R.id.expenses);
        participants = (TextView) itemView.findViewById(R.id.participants);
        location = (TextView) itemView.findViewById(R.id.location);

        expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}
