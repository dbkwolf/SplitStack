package com.example.splitstack;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.splitstack.Adapter.ExpenseAdapter;
import com.example.splitstack.DBUtility.ExpenseData;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;


public class EventActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManage;
    private TabLayout tabLayout;
    private String uid;
    private String eventId;
    private ArrayList<ExpenseData> eventExpenses;
    FirebaseFirestore database;
    private Dialog myDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_expenses);
        tabLayout = findViewById(R.id.eventTabLayout);

        eventExpenses = new ArrayList<>();

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

        uid = getIntent().getStringExtra("uid");
        eventId = getIntent().getStringExtra("eventId");
        Toast.makeText(this, "Event ID Is:" + eventId, Toast.LENGTH_LONG).show();

        database = FirebaseFirestore.getInstance();


        myDialog = new Dialog(this);

        initTabListeners();




    }

    public ArrayList<ExpenseItem> onExpenseClick() {
        ArrayList<ExpenseItem> list = new ArrayList<>();
        list.add(new ExpenseItem("Hotel", "200 SEK", "Sophie"));
        list.add(new ExpenseItem("Friday Dinner", "50 SEK", "Max"));
        list.add(new ExpenseItem("Friday Drinks", "120 SEK", "Max"));
        list.add(new ExpenseItem("Flight", "100 SEK", "Laura"));
        list.add(new ExpenseItem("Taxi", "20 SEK", "Me"));

        return list;
    }

    public ArrayList<ExpenseItem> onParticipantClick() {
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

    private void loadTab(int tabNum) {

        if (tabNum == 0) {
            ExpenseAdapter adapter = new ExpenseAdapter(onExpenseClick());

            mRecyclerView.setLayoutManager(mLayoutManage);
            mRecyclerView.setAdapter(adapter);
        }
        if (tabNum == 1) {
            ExpenseAdapter adapter = new ExpenseAdapter(onParticipantClick());

            mRecyclerView.setLayoutManager(mLayoutManage);
            mRecyclerView.setAdapter(adapter);

        }


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

    public void onClickAdd(View view) {

        if (!eventId.equals("")) {


            if (tabLayout.getSelectedTabPosition() == 0) {

                //ADD EXPENSE

                myDialog.setContentView(R.layout.add_expense_dialog);
                final EditText txtExpenseTitle = myDialog.findViewById(R.id.txt_expense_title);
                final EditText txtExpenseAmount = myDialog.findViewById(R.id.txt_expense_amount);
                Button btnAddExpense = myDialog.findViewById(R.id.btn_add_expense);


                btnAddExpense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String expenseTitle = txtExpenseTitle.getText().toString();
                        String expenseAmount = txtExpenseAmount.getText().toString();
                        saveExpensesData(expenseTitle, expenseAmount);
                        myDialog.dismiss();
                        System.out.println("working");
                    }
                });

                myDialog.show();

            }else if(tabLayout.getSelectedTabPosition() == 1) {

                //ADD PARTICIPANT

            }
        }
    }

    public void saveExpensesData(String expenseTitle, String expenseAmount) {

        ExpenseData newExpenseData = new ExpenseData(eventId, expenseTitle, expenseAmount, uid, new Timestamp(new Date()).toDate());

        //Create a new event document
        DocumentReference key = database.collection("expenses").document();

        //Add data to new document
        key.set(newExpenseData);

        //Save the new document key (needed to update the userdata)
        final String expenseId = key.getId();


    }

    public void initDBListener() {

           /* docEventIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
*/

    }



}
