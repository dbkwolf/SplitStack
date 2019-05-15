package com.example.splitstack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.splitstack.Adapter.ActiveEventsAdapter;
import com.example.splitstack.Adapter.ExpenseAdapter;
import com.example.splitstack.Adapter.ParticipantAdapter;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManage;
    private String decentralizedIdForUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        setUserID();
        ArrayList<ActiveEventsItem> activeEventsList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            activeEventsList.add(new ActiveEventsItem("Spain Holiday", "John"));
            activeEventsList.add(new ActiveEventsItem("Spain Holiday", "Delay"));
            activeEventsList.add(new ActiveEventsItem("Spain Holiday", "Marcus"));
            activeEventsList.add(new ActiveEventsItem("Spain Holiday", "Truss"));
        }


        mRecyclerView = findViewById(R.id.activeEvents_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManage = new LinearLayoutManager(this);
        mAdapter = new ActiveEventsAdapter(activeEventsList);

        mRecyclerView.setLayoutManager(mLayoutManage);
        mRecyclerView.setAdapter(mAdapter);
        setUserID();
    }
public void setUserID(){
   String userIdFromPreviousScreen = getIntent().getStringExtra("userId");
    TextView userIdText = findViewById(R.id.UserIdText);
    userIdText.setText("Logged in as " + userIdFromPreviousScreen);
    System.out.println(userIdFromPreviousScreen);
    decentralizedIdForUser = getIntent().getStringExtra("decentralizedIdForUser");
    }

public void goToEventList(View view){
        Intent intent =new  Intent(this,EventListActivity.class);
        startActivity(intent);
}
}