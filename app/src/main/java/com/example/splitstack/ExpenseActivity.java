package com.example.splitstack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.splitstack.Adapter.ExpenseAdapter;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManage;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_expenses);
        tabLayout= findViewById(R.id.participant_tabLayout);

        ArrayList<ExpenseItem> expenseList = new ArrayList<>();
        expenseList.add(new ExpenseItem("Hotel", "200 SEK", "Sophie"));
        expenseList.add(new ExpenseItem("Friday Dinner", "50 SEK", "Max"));
        expenseList.add(new ExpenseItem("Friday Drinks", "120 SEK", "Max"));
        expenseList.add(new ExpenseItem("Flight", "100 SEK", "Laura"));
        expenseList.add(new ExpenseItem("Taxi", "20 SEK", "Me"));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManage = new LinearLayoutManager(this);
        mAdapter = new ExpenseAdapter(expenseList);
        mRecyclerView.setLayoutManager(mLayoutManage);
        mRecyclerView.setAdapter(mAdapter);

        initTabListeners();
    }

    public ArrayList<ExpenseItem> onExpenseClick(){
        ArrayList<ExpenseItem> list = new ArrayList<>();
        list.add(new ExpenseItem("Hotel", "200 SEK", "Sophie"));
        list.add(new ExpenseItem("Friday Dinner", "50 SEK", "Max"));
        list.add(new ExpenseItem("Friday Drinks", "120 SEK", "Max"));
        list.add(new ExpenseItem("Flight", "100 SEK", "Laura"));
        list.add(new ExpenseItem("Taxi", "20 SEK", "Me"));

        return list;
    }
    
    public ArrayList<ExpenseItem> onParticipantClick(){
        ArrayList<ExpenseItem> list = new ArrayList<>();
        list.add(new ExpenseItem("", "200 SEK", "Sophie"));
        list.add(new ExpenseItem("", "170 SEK", "Max"));
        list.add(new ExpenseItem("", "100 SEK", "Laura"));
        list.add(new ExpenseItem("", "20 SEK", "Me"));

        mAdapter = new ExpenseAdapter(list);
        mRecyclerView.setLayoutManager(mLayoutManage);
        mRecyclerView.setAdapter(mAdapter);

        return list;
    }

    private void loadTab(int tabNum){

        if(tabNum==0) {
            ExpenseAdapter adapter = new ExpenseAdapter(onExpenseClick());

            mRecyclerView.setLayoutManager(mLayoutManage);
            mRecyclerView.setAdapter(adapter);
        }
        if(tabNum==1){
            ExpenseAdapter adapter = new ExpenseAdapter(onParticipantClick());

            mRecyclerView.setLayoutManager(mLayoutManage);
            mRecyclerView.setAdapter(adapter);

        }


    }

    private void initTabListeners(){

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {

                    loadTab(0);

                }
                if (tab.getPosition() == 1) {

                    loadTab(1);

                }
                if (tab.getPosition() == 2) {


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
