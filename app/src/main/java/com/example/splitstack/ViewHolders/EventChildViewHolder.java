package com.example.splitstack.ViewHolders;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.example.splitstack.MainActivity;
import com.example.splitstack.R;

public class EventChildViewHolder extends ChildViewHolder {

    public TextView expenses, participants, btnDelete;

    public EventChildViewHolder(View itemView){

        super(itemView);
        expenses = (TextView) itemView.findViewById(R.id.expenses);
        participants = (TextView) itemView.findViewById(R.id.participants);
        btnDelete = (TextView) itemView.findViewById(R.id.delete);

        expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}
