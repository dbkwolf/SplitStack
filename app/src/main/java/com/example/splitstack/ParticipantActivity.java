package com.example.splitstack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.splitstack.Adapter.ExpenseAdapter;
import com.example.splitstack.Adapter.ParticipantAdapter;

import java.util.ArrayList;

public class ParticipantActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_expenses);

        ArrayList<ParticipantItem> participantList = new ArrayList<>();
        participantList.add(new ParticipantItem("Marcus", "100 kroners"));
        participantList.add(new ParticipantItem("John", "20 kroners"));
        participantList.add(new ParticipantItem("Delay", "50 kroners"));



        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManage = new LinearLayoutManager(this);
        mAdapter = new ParticipantAdapter(participantList);

        mRecyclerView.setLayoutManager(mLayoutManage);
        mRecyclerView.setAdapter(mAdapter);
    }
}