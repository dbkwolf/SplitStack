package com.example.splitstack;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.splitstack.Adapter.ActiveEventsAdapter;
import com.example.splitstack.DBUtility.EventData;
import com.example.splitstack.DBUtility.UserData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManage;
    private String decentralizedUserId = "";


    private String id = "";
    private UserData currentUserData = null;
    private Dialog myDialog;

    FirebaseFirestore database = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setUserID();



        database = FirebaseFirestore.getInstance();


        myDialog = new Dialog(this);


        mRecyclerView = findViewById(R.id.activeEvents_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManage = new LinearLayoutManager(this);


        mRecyclerView.setLayoutManager(mLayoutManage);
        mRecyclerView.setAdapter(mAdapter);

        findUser();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(" Dashboard");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_toolbar_icon);


        barChartOps();



        //anyChartView.bringToFront();
        //((View)anyChartView.getParent()).requestLayout();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Toast.makeText(this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setUserID() {
        String userIdFromPreviousScreen = getIntent().getStringExtra("userId");
        TextView userIdText = findViewById(R.id.UserIdText);
        Toast toast = Toast.makeText(this, userIdFromPreviousScreen, Toast.LENGTH_LONG);
        toast.show();
        System.out.println("WORKING" + userIdFromPreviousScreen);
        userIdText.setText("Logged in as " + userIdFromPreviousScreen);
        id = userIdFromPreviousScreen;
        System.out.println(userIdFromPreviousScreen);
        decentralizedUserId = getIntent().getStringExtra("decentralizedUserId");
    }

    public void goToEventList(View view) {
        Intent intent = new Intent(this, EventListActivity.class);
        intent.putExtra("uid", id);
        startActivity(intent);
    }


    public void findUser() {


        if (!id.equals("")) {
            DocumentReference docIdRef = database.collection("users").document(id);

            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //Document exists!
                            currentUserData = document.toObject(UserData.class);
                            findEvents();


                        } else {
                            myDialog.setContentView(R.layout.userdata_dialog);
                            final EditText firstName_ET = myDialog.findViewById(R.id.firstName_EditText);
                            final EditText lastName_ET = myDialog.findViewById(R.id.lastName_EditText);
                            Button confirmButton = myDialog.findViewById(R.id.confirm_Button);


                            //Document does not exist!

                            confirmButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String firstName = firstName_ET.getText().toString();
                                    String lastName = lastName_ET.getText().toString();
                                    saveNewUserData(firstName, lastName);
                                    myDialog.dismiss();
                                    System.out.println("working");
                                }
                            });

                            //UserData userData = new UserData(null, firstName, lastName, "0", "0");
                            // Add a new document with a generated ID
                            //database.collection("users").document(uuid).set(userData);

                            myDialog.show();

                        }
                    } else {
                        //failed
                    }
                }


            });


        }


    }

    public void saveNewUserData(String firstName, String lastName) {
        UserData userData = new UserData(null, firstName, lastName, "0", "0");
        database.collection("users").document(id).set(userData);
    }


    public void findEvents() {


        System.out.println("----------------------------------in find event");

        System.out.println(currentUserData.toString());

        if (currentUserData != null) {


            final ArrayList<EventData> eventDataList = new ArrayList<>();

            List<String> eventIdList = currentUserData.getEventList();

            TextView tv_amount = findViewById(R.id.activeEventsAmount_textView);





            if (eventIdList != null) {
                tv_amount.setText(String.valueOf(eventIdList.size()));

                for (String eventId : eventIdList) {

                    DocumentReference docIdRef = database.collection("events").document(eventId);

                    docIdRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            EventData eventData = documentSnapshot.toObject(EventData.class);
                            eventDataList.add(eventData);

                            mAdapter = new ActiveEventsAdapter(eventDataList);

                            mRecyclerView.setAdapter(mAdapter);


                        }
                    });



                }

            }


        }

    }

    public void barChartOps(){
        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Golf Day", 25000));
        data.add(new ValueDataEntry("Festival 19",5000));
        data.add(new ValueDataEntry("Spain Holiday", 12000));
        data.add(new ValueDataEntry("Saturday Night", 1800));

        pie.data(data);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);
    }




    @Override
    protected void onRestart() {
        super.onRestart();

        System.out.println();
        findUser();


    }

}