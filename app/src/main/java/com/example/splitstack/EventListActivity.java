package com.example.splitstack;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.splitstack.Adapter.EventAdapter;
import com.example.splitstack.DBUtility.EventData;
import com.example.splitstack.DBUtility.UserData;
import com.example.splitstack.Models.EventChildItem;
import com.example.splitstack.Models.EventParentItem;
import com.example.splitstack.Models.TitleCreator;
import com.example.splitstack.SQLite.SQLiteHelper;
import com.example.splitstack.SQLite.UserHistoryContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class EventListActivity extends AppCompatActivity {

    private static final String TAG = "EventListActivity";
    RecyclerView recyclerView;
    TextView tvAmountDue;
    private TabLayout tabLayout;
    String uid = "";
    UserData currentUserData;
    ArrayList<EventData> userEventDataList = new ArrayList<>();
    FirebaseFirestore database;
    private SQLiteHelper sqlHelper = new SQLiteHelper(this);


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((EventAdapter) recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(" My Events");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_toolbar_icon);

        tabLayout = findViewById(R.id.tabLayout);
        uid = getIntent().getStringExtra("uid");
        tvAmountDue=findViewById(R.id.tv_total_amount_due);

        database = FirebaseFirestore.getInstance();

        initDbListeners();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initTabListeners();

        tvAmountDue.setText("1205.90 SEK");

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
            Toast.makeText(this, readHistory().toString(), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initTabListeners() {

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

                    loadTab(2);

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

    private void loadTab(int tabNum) {

        if (initData(tabNum) != null) {

            EventAdapter adapter = new EventAdapter(EventListActivity.this, initData(tabNum), uid, database);
            //List<ParentObject> parentObject = initData(tabNum);

            adapter.setParentClickableViewAnimationDefaultDuration();
            adapter.setParentAndIconExpandOnClick(true);

            recyclerView.setAdapter(adapter);
        }

    }

    private List<ParentObject> initData(int tabnumber) {
        //TitleCreator titleCreator = TitleCreator.get(this);
        Log.d(TAG, "Current tab: " + tabLayout.getSelectedTabPosition());
        ArrayList<EventData> activeEventList = new ArrayList<>();
        ArrayList<EventData> closedEventList = new ArrayList<>();

        List<ParentObject> parentObject = null;

        if (userEventDataList != null) {

            for (EventData eD : userEventDataList) {


                if (eD.isActive()) {
                    activeEventList.add(eD);
                } else {
                    closedEventList.add(eD);
                }
            }
            Log.d(TAG, "closed events list: " + closedEventList.size());
            parentObject = new ArrayList<>();

            if (tabnumber == 1) {

                Log.d(TAG, "Performing operations for tab number: " + tabnumber);

                ArrayList<EventParentItem> parentList = makeParentList(activeEventList);
                TitleCreator titleCreator = new TitleCreator();
                List<EventParentItem> titles = titleCreator.makeList(parentList);

                Log.d(TAG, "titles size " + titles.size());

                for (int i = 0; i < titles.size(); i++) {


                    String formattedTotal = String.format("%.2f", Double.valueOf(activeEventList.get(i).getTotalExpenses()));

                    Log.d(TAG, "TOTAL EVENT AMOUNT: " + formattedTotal);

                    String totalExpenses = "Total: " + formattedTotal + " SEK";
                    String participants = "Participants: ";

                    for (String p : activeEventList.get(i).getParticipants()) {
                        participants = participants.concat(" " + p);
                    }



                    List<Object> childList = new ArrayList<>();
                    childList.add(new EventChildItem(totalExpenses, participants, titles.get(i).getEventId(), activeEventList.get(i).getParticipants()));
                    titles.get(i).setChildObjectList(childList);
                    parentObject.add(titles.get(i));

                }


            } else if (tabnumber == 0) {
                Log.d(TAG, "Performing operations for tab number: " + tabnumber);
                ArrayList<EventParentItem> parentList = makeParentList(closedEventList);
                TitleCreator titleCreator = new TitleCreator();
                List<EventParentItem> titles = titleCreator.makeList(parentList);

                Log.d(TAG, "titles size " + titles.size());
                for (int i = 0; i < titles.size(); i++) {


                    String formattedTotal = String.format("%.2f", Double.valueOf(closedEventList.get(i).getTotalExpenses()));

                    Log.d(TAG, "TOTAL EVENT AMOUNT: " + formattedTotal);

                    String totalExpenses = "Total: " + formattedTotal + " SEK";
                    String participants = "Participants: ";

                    for (String p : closedEventList.get(i).getParticipants()) {
                        participants = participants.concat(" " + p);
                    }

                    List<Object> childList = new ArrayList<>();
                    childList.add(new EventChildItem(totalExpenses, participants, titles.get(i).getEventId(), closedEventList.get(i).getParticipants()));
                    titles.get(i).setChildObjectList(childList);
                    parentObject.add(titles.get(i));
                }
            }

        }

        return parentObject;
    }


    public ArrayList<EventParentItem> makeParentList(ArrayList<EventData> eventList) {

        ArrayList<EventParentItem> eventParentItemList = new ArrayList<>();

        for (EventData eventdata : eventList) {
            eventParentItemList.add(new EventParentItem(eventdata.getEventName(), eventdata.getId()));
        }
        return eventParentItemList;
    }


    public void testMethod(View view) {
        System.out.println("THE TEST IS WORKING");
    }


    public void saveNewEventToDb(String newEventName) {

        //create list of participant adding the current user
        ArrayList<String> participantList = new ArrayList<>();
        participantList.add(uid);


        EventData newEventData = new EventData(newEventName, true, participantList, "0");

        //Create a new event document
        DocumentReference key = database.collection("events").document();

        //Add data to new document
        key.set(newEventData);

        //Save the new document key (needed to update the userdata)
        final String eventId = key.getId();

        //Update user data
        if (!uid.equals("")) {

            DocumentReference userRef = database.collection("users").document(uid);

            userRef.update("eventList", FieldValue.arrayUnion(eventId));

        }


    }


    public void onClickAddEvent(View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Give the name of the new event");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setGravity(Gravity.CENTER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {

                String newEventName = input.getText().toString();

                saveNewEventToDb(newEventName);

                //TODO CHANGE TO EVENT

            }

        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    public void initDbListeners() {

        final DocumentReference userRef = database.collection("users").document(uid);
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    currentUserData = snapshot.toObject(UserData.class);
                    saveHistory(new Date(), "User was added to an event: " + currentUserData.toString(), uid);
                    createUserEventList();

                    Log.d(TAG, "Current user data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current user data: null");
                }
            }
        });


    }

    private void createUserEventList() {

        userEventDataList.clear();

        if (currentUserData.getEventList() != null) {



            for (final String eventId : currentUserData.getEventList()) {



                final DocumentReference eventRef = database.collection("events").document(eventId);


                eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                //Boolean d = document.getBoolean("active");
                               ;
                               EventData evD = document.toObject(EventData.class);
                                userEventDataList.add(document.toObject(EventData.class));
                                userEventDataList.get(userEventDataList.size() - 1).setId(eventId);
                                Log.d(TAG, "THE CURRENT DOCUMENT IS : " + userEventDataList.get(userEventDataList.size() - 1).isActive().toString());



                                tabLayout.getTabAt(1).select();

                                loadTab(1);

                                Log.d(TAG, "Current event data: " + document.getData());

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }



        }
    }

    public void saveHistory(Date date, String activityStr, String user) {

        SQLiteDatabase sqliteDatabase = sqlHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(UserHistoryContract.HistoryEntry.COLUMN_NAME_DATE, date.toString());
        values.put(UserHistoryContract.HistoryEntry.COLUMN_NAME_ACTIVITY, activityStr);
        values.put(UserHistoryContract.HistoryEntry.COLUMN_NAME_USER, user);


        Long newRowId = sqliteDatabase.insert(UserHistoryContract.HistoryEntry.TABLE_NAME, null, values);
    }

    public ArrayList<String> readHistory() {

        SQLiteDatabase sqliteDatabase = sqlHelper.getReadableDatabase();

        String[] projection = new String[]{BaseColumns._ID,
                UserHistoryContract.HistoryEntry.COLUMN_NAME_DATE,
                UserHistoryContract.HistoryEntry.COLUMN_NAME_ACTIVITY,
                UserHistoryContract.HistoryEntry.COLUMN_NAME_USER};

        Cursor cursor = sqliteDatabase.query(
                UserHistoryContract.HistoryEntry.TABLE_NAME,  // The table to query
                projection,                         // The array of columns to return (pass null to get all)
                null,
                null,
                null,                      // don't group the rows
                null,                       // don't filter by row groups
                null                       // don't sort
        );

        ArrayList<String> outputArr = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(BaseColumns._ID));
                String date = cursor.getString(cursor.getColumnIndex(UserHistoryContract.HistoryEntry.COLUMN_NAME_DATE));
                String activity = cursor.getString(cursor.getColumnIndex(UserHistoryContract.HistoryEntry.COLUMN_NAME_ACTIVITY));
                String user = cursor.getString(cursor.getColumnIndex(UserHistoryContract.HistoryEntry.COLUMN_NAME_USER));

                outputArr.add("On " + date + " user " + user + "made the following changes: " +activity);

            }

            cursor.close();
        }

        return outputArr;

    }



    @Override
    protected void onRestart() {
        super.onRestart();

        createUserEventList();

    }
}

