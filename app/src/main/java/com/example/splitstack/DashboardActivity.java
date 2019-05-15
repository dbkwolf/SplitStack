package com.example.splitstack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.splitstack.DBUtility.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManage;
    private String decentralizedUserId = "";

    private String id = "";

    FirebaseFirestore database = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        setUserID();

        database = FirebaseFirestore.getInstance();


        mRecyclerView = findViewById(R.id.activeEvents_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManage = new LinearLayoutManager(this);
        //mAdapter = new ActiveEventsAdapter(activeEventsList);
        findUser();

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



                        } else {
                            //Document does not exist!

                            UserData userData = new UserData(null, "Delay", "Wolfsteller", "0", "0");

                            // Add a new document with a generated ID
                            database.collection("users")
                                    .document(id)
                                    .set(userData);

                        }
                    } else {
                        //failed
                    }
                }

            });
        }


    }

    public static String removeWord(String string, String word) {

        // Check if the word is present in string
        // If found, remove it using removeAll()
        if (string.contains(word)) {

            // To cover the case
            // if the word is at the
            // beginning of the string
            // or anywhere in the middle
            String tempWord = word + " ";
            string = string.replaceAll(tempWord, "");

            // To cover the edge case
            // if the word is at the
            // end of the string
            tempWord = " " + word;
            string = string.replaceAll(tempWord, "");
        }

        // Return the resultant string
        return string;
    }
}