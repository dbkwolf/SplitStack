package com.example.splitstack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.splitstack.Adapter.EventAdapter;
import com.example.splitstack.DBUtility.EventData;
import com.example.splitstack.DBUtility.UserData;
import com.example.splitstack.Models.EventChildItem;
import com.example.splitstack.Models.EventParentItem;
import com.example.splitstack.Models.TitleCreator;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {

    private static final String TAG = "EventListActivity";
    RecyclerView recyclerView;
    private TabLayout tabLayout;
    String uid = "";
    UserData currentUserData;
    ArrayList<EventData> userEventDataList = new ArrayList<>();
    FirebaseFirestore database;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((EventAdapter) recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        tabLayout = findViewById(R.id.tabLayout);
        uid = getIntent().getStringExtra("uid");

        database = FirebaseFirestore.getInstance();

        initDbListeners();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initTabListeners();



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

        if (initData(tabNum)!= null) {



            EventAdapter adapter = new EventAdapter(EventListActivity.this, initData(tabNum), uid, database);
            List<ParentObject> parentObject = initData(tabNum);

            adapter.setParentClickableViewAnimationDefaultDuration();
            adapter.setParentAndIconExpandOnClick(true);
            recyclerView.setAdapter(adapter);

        }

    }

    private List<ParentObject> initData(int tabnumber) {
        //TitleCreator titleCreator = TitleCreator.get(this);

        ArrayList<EventData> activeEventList = new ArrayList<>();
        ArrayList<EventData> closedEventList = new ArrayList<>();

        List<ParentObject> parentObject = null;

        if(userEventDataList!=null) {


            for (EventData eD : userEventDataList) {
                if (eD.isActive()) {
                    activeEventList.add(eD);
                } else {
                    closedEventList.add(eD);
                }
            }

            parentObject = new ArrayList<>();

            if (tabnumber == 1) {

                ArrayList<EventParentItem> parentList = makeParentList(activeEventList);
                TitleCreator titleCreator = new TitleCreator();
                List<EventParentItem> titles = titleCreator.makeList(parentList);


                for (int i = 0; i < titles.size(); i++) {


                    String totalExpenses = "Total: " + userEventDataList.get(i).getTotalExpenses();
                    String participants = "Participants: ";

                    for (String p : activeEventList.get(i).getParticipants()) {
                        participants = participants.concat(" " + p);
                    }

                    Button dltButton = new Button(this);
                    dltButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(EventListActivity.this, "Poof!", Toast.LENGTH_LONG).show();
                        }
                    });

                    List<Object> childList = new ArrayList<>();
                    childList.add(new EventChildItem(totalExpenses, participants, titles.get(i).getEventId(), activeEventList.get(i).getParticipants()));
                    titles.get(i).setChildObjectList(childList);
                    parentObject.add(titles.get(i));

                }


            } else if (tabnumber == 0) {

                ArrayList<EventParentItem> parentList = makeParentList(closedEventList);
                TitleCreator titleCreator = new TitleCreator();
                List<EventParentItem> titles = titleCreator.makeList(parentList);


                for (int i = 0; i < titles.size(); i++) {
                    List<Object> childList = new ArrayList<>();
                    childList.add(new EventChildItem("expenses: 50 SEK", "participant: " + 120, titles.get(i).getEventId(), closedEventList.get(i).getParticipants() ));
                    titles.get(i).setChildObjectList(childList);
                    parentObject.add(titles.get(i));
                }
            } else if (tabnumber == 2) {

//                ArrayList<EventParentItem> parentList = makeParentList(closedEventList);
//                TitleCreator titleCreator = new TitleCreator();
//                List<EventParentItem> titles = titleCreator.makeList(parentList);
//

//                for (int i = 0; i < titles.size(); i++) {
//                    List<Object> childList = new ArrayList<>();
//                    childList.add(new EventChildItem("expenses: 50 SEK", "participant: " + 120, titles.get(i).getEventId()));
//                    titles.get(i).setChildObjectList(childList);
//                    parentObject.add(titles.get(i));
//                }
//            }
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
                eventRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {

                            userEventDataList.add(snapshot.toObject(EventData.class));
                            userEventDataList.get(userEventDataList.size()-1).setId(eventId);

                            tabLayout.getTabAt(1).select();

                            loadTab(1);

                            Log.d(TAG, "Current event data: " + snapshot.getData());
                        } else {
                            Log.d(TAG, "Current event  data: null");
                        }
                    }
                });
            }
        }
    }
}
