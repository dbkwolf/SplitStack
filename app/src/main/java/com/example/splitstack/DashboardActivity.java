package com.example.splitstack;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManage;
    private String decentralizedUserId = "";

    private String id = "";
    private UserData currentUserData = null ;
    private Dialog myDialog;


    FirebaseFirestore database = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        setUserID();

        database = FirebaseFirestore.getInstance();

        findUser();
        myDialog = new Dialog(this);



        mRecyclerView = findViewById(R.id.activeEvents_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManage = new LinearLayoutManager(this);
        //mAdapter = new ActiveEventsAdapter(null);

        mRecyclerView.setLayoutManager(mLayoutManage);
        mRecyclerView.setAdapter(mAdapter);


        //  setUserID();
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

                            confirmButton.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                    String firstName = firstName_ET.getText().toString();
                                    String lastName = lastName_ET.getText().toString();
                                    createNewUserData(firstName, lastName);
                                    myDialog.dismiss();
                                    System.out.println("working");
                                }
                            });

                            //UserData userData = new UserData(null, firstName, lastName, "0", "0");
                            // Add a new document with a generated ID
                            //database.collection("users").document(id).set(userData);

                            myDialog.show();

                        }
                    } else {
                        //failed
                    }
                }



            });


        }


    }

    public void createNewUserData(String firstName, String lastName){
        UserData userData = new UserData(null, firstName, lastName, "0", "0");
        database.collection("users").document(id).set(userData);
    }


    public void findEvents() {


        System.out.println("----------------------------------in find event");

        System.out.println(currentUserData.toString());

            if(currentUserData!=null) {


                final ArrayList<EventData> eventDataList = new ArrayList<>();

                List<String> eventIdList = currentUserData.getEventList();


                if (eventIdList != null) {
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

}