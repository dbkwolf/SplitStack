package com.example.splitstack.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.splitstack.ExampleItem;
import com.example.splitstack.R;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private ArrayList<ExampleItem> mExampleList;


    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }

    public ExpenseAdapter(ArrayList<ExampleItem> exampleItems){

        mExampleList = exampleItems;

    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_txt_item,parent,false);
        ExpenseViewHolder expenseViewHolder = new ExpenseViewHolder(v);

        return expenseViewHolder;
    }

    @Override
    public void onBindViewHolder( ExpenseViewHolder expenseViewHolder, int position) {
    ExampleItem currentItem = mExampleList.get(position);

        expenseViewHolder.mTextView1.setText(currentItem.getText1());
        expenseViewHolder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
       return mExampleList.size();
        //return 0;
    }
}