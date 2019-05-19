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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.splitstack.Adapter.EventAdapter;
import com.example.splitstack.DBUtility.EventData;
import com.example.splitstack.DBUtility.UserData;
import com.example.splitstack.Models.EventChildItem;
import com.example.splitstack.Models.EventParentItem;
import com.example.splitstack.Models.TitleCreator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TabLayout tabLayout;
    String uid = "";
    UserData currentUserData;
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

        uid = getIntent().getStringExtra("uid");
        currentUserData = (UserData) getIntent().getExtras().getSerializable("currentUserData");

        database = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        tabLayout = findViewById(R.id.tabLayout);


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

    private void loadTab(int tabNum){

        EventAdapter adapter = new EventAdapter(EventListActivity.this, initData(tabNum));
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);

    }

    private List<ParentObject> initData(int tabnumber) {
        //TitleCreator titleCreator = TitleCreator.get(this);


        ArrayList<EventParentItem> parentList = makeParentList();
        TitleCreator titleCreator = new TitleCreator();
        List<EventParentItem> titles = titleCreator.makeList(parentList);
        List<ParentObject> parentObject = new ArrayList<>();
        System.out.println(titles.size());




        if(tabnumber == 1) {
            for (int i = 0; i < titles.size(); i++) {
                List<Object> childList = new ArrayList<>();
                childList.add(new EventChildItem("expenses: 10000 SEK", "participants: " + 120, new Button(this)));
                titles.get(i).setChildObjectList(childList);
                parentObject.add(titles.get(i));
            }




        } else if(tabnumber==2){
            for (int i = 0; i < titles.size(); i++) {
                List<Object> childList = new ArrayList<>();
                childList.add(new EventChildItem("expenses: 50 SEK", "participants: " + 120, new Button(this)));
                titles.get(i).setChildObjectList(childList);
                parentObject.add(titles.get(i));
            }
        }


        return parentObject;
    }

    public void createButton(View view) {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);

    }

    public ArrayList<EventParentItem> makeParentList() {
        ArrayList<EventParentItem> parentList = new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            parentList.add(new EventParentItem("Spain Holiday"));
        }
        return parentList;
    }


    public void testMethod(View view) {
        System.out.println("THE TEST IS WORKING");
    }


    public void saveNewEventToDb(String newEventName) {

        //create list of participants adding the current user
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

        //This is the only way to get the variable out of the anon class below (handles closure)



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
}
