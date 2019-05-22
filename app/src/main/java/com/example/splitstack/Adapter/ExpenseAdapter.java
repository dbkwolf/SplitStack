package com.example.splitstack.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.splitstack.ExpenseItem;
import com.example.splitstack.R;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private ArrayList<ExpenseItem> mExampleList;


    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.textView3);
        }
    }

    public ExpenseAdapter(ArrayList<ExpenseItem> expenseItems){

        mExampleList = expenseItems;

    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_txt_item,parent,false);
        ExpenseViewHolder expenseViewHolder = new ExpenseViewHolder(v);

        return expenseViewHolder;
    }

    @Override
    public void onBindViewHolder( ExpenseViewHolder expenseViewHolder, int position) {
    ExpenseItem currentItem = mExampleList.get(position);

        expenseViewHolder.mTextView1.setText(currentItem.getTitle());
        expenseViewHolder.mTextView2.setText(currentItem.getAmount());
        expenseViewHolder.mTextView3.setText(currentItem.getParticipant());
}

    @Override
    public int getItemCount() {
       return mExampleList.size();
        //return 0;
    }
}