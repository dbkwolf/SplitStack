package com.example.splitstack;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.example.splitstack.Adapter.ExpenseAdapter;
import com.example.splitstack.Adapter.ParticipantAdapter;
import com.example.splitstack.Controllers.SwipeController;
import com.example.splitstack.DBUtility.EventData;
import com.example.splitstack.DBUtility.ExpenseData;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;
import org.blockstack.android.sdk.BlockstackConfig;
import org.blockstack.android.sdk.BlockstackSession;

import java.util.*;


public class EventActivity extends AppCompatActivity {
    private static final String TAG = "EventActivity";
    FirebaseFirestore database;
    private RecyclerView mRecyclerView;
    private ArrayList participantsList;
    private TextView tvEventName;
    private RecyclerView.LayoutManager mLayoutManage;
    private TabLayout tabLayout;
    private String uid;
    private String eventId;
    private ArrayList<ExpenseData> eventExpenses;
    private EventData eventData;
    private Dialog myDialog;
    SwipeController swipeController;
    ItemTouchHelper itemTouchhelper;
    private double totalForSettleOutText = 0.1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_expenses);
        tabLayout = findViewById(R.id.eventTabLayout);

        eventExpenses = new ArrayList<>();
        eventData = new EventData();

        initTabListeners();

        database = FirebaseFirestore.getInstance();

        uid = getIntent().getStringExtra("uid");
        eventId = getIntent().getStringExtra("eventId");

        tvEventName = findViewById(R.id.event_title);

        DbExpensesListener();

        DbParticipantListener();


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManage = new LinearLayoutManager(this);


        Toast.makeText(this, "Event ID Is:" + eventId, Toast.LENGTH_LONG).show();


        myDialog = new Dialog(this);

        swipeController = new SwipeController();

        itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);


    }


    public ArrayList<ExpenseItem> onExpenseClick() {
        ArrayList<ExpenseItem> list = new ArrayList<>();

        for (ExpenseData ed : eventExpenses) {



            list.add(new ExpenseItem(ed.getTitle(), ed.getAmount(), ed.getUserId()));

        }


        return list;
    }

    public ArrayList<ParticipantItem> onParticipantClick() {
        ArrayList<ParticipantItem> list = new ArrayList<>();

        for (String participant: eventData.getParticipants()){
            list.add(new ParticipantItem(participant, calculateTotalContributions(participant)));


        }


        return list;
    }

    public String calculateTotalContributions(String participant) {

        Double amount = 0.0;


        for (ExpenseData ed : eventExpenses) {
            if (ed.getUserId().equals(participant)) {
                amount = amount + Double.valueOf(ed.getAmount());
            }
        }

        return amount.toString();

    }


    private void loadTab(int tabNum) {

        if (tabNum == 0) {
            ExpenseAdapter adapter = new ExpenseAdapter(onExpenseClick());


            mRecyclerView.setLayoutManager(mLayoutManage);
            mRecyclerView.setAdapter(adapter);
        }
        if (tabNum == 1) {

            ParticipantAdapter adapter = new ParticipantAdapter(onParticipantClick());
           // ExpenseAdapter adapter = new ExpenseAdapter(onParticipantClick());

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

            } else if (tabLayout.getSelectedTabPosition() == 1) {

                //ADD PARTICIPANT

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add a participant:");
                builder.setMessage("e.g. exampleuser.id.blockstack");

                // Set up the input
                final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setGravity(Gravity.LEFT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newParticipant = input.getText().toString();

                        addParticipant(newParticipant);


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
    }

    public void saveExpensesData(String expenseTitle, String expenseAmount) {

        //DocumentReference userNameRef = database.collection("users").document(uid);
        String userNameStr = "YOLO";//userNameRef.get().getResult().toObject(UserData.class).getFirstName();


        ExpenseData newExpenseData = new ExpenseData(eventId, expenseTitle, expenseAmount, uid, userNameStr, new Timestamp(new Date()).toDate());

        //Create a new event document
        DocumentReference expensKey = database.collection("expenses").document();

        newExpenseData.setId(expensKey.getId());

        //Add data to new document
        expensKey.set(newExpenseData);


        //Update total amount for event
        DocumentReference eventRef = database.collection("events").document(eventId);

        Double totalExp = Double.valueOf(eventData.getTotalExpenses()) + Double.valueOf(expenseAmount);

        eventRef.update("totalExpenses", totalExp.toString());


        //Save the new document key (needed to update the userdata)
        final String expenseId = expensKey.getId();


    }

    public void DbExpensesListener() {


        database.collection("expenses")
                .whereEqualTo("eventId", eventId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        eventExpenses.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("eventId") != null) {
                                eventExpenses.add(doc.toObject(ExpenseData.class));

                            }
                        }

                        tabLayout.getTabAt(0).select();
                        sendNotification("An new expense was added to one of your trips");


                        Log.d(TAG, "Current Expenses list : " + eventExpenses.toString());
                    }
                });


    }

    public void addParticipant(String participant) {

        if (!participant.equals("")) {

            DocumentReference eventRef = database.collection("events").document(eventId);

            eventRef.update("participants", FieldValue.arrayUnion(participant));


            DocumentReference userRef = database.collection("users").document(participant);
            userRef.update("eventList", FieldValue.arrayUnion(eventId));

        }


    }


    public void DbParticipantListener() {

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

                    eventData=snapshot.toObject(EventData.class);



                    tvEventName.setText(eventData.getEventName());

                    tabLayout.getTabAt(0).select();

                    loadTab(0);

                    sendNotification("An new participant was added to one of your trips");

                    Log.d(TAG, "Current event data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current event  data: null");
                }
            }
        });

    }


    // this code is from the firebase github for notifications with minor changes to work with our code.
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, EventListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public void settleUp(View view) {
        Double totalExp = Double.valueOf(eventData.getTotalExpenses());

        if (!eventId.equals("")) {


            if (tabLayout.getSelectedTabPosition() == 0) {

                //ADD EXPENSE

                myDialog.setContentView(R.layout.settle_accounts);
                TextView totalAmountText = myDialog.findViewById(R.id.totalAmountTextView);
                totalAmountText.setText(totalExp.toString());
                fillingParticipantArrayListForListView();
                ListView listview = myDialog.findViewById(R.id.participantsListView);

                StableArrayAdapter adapter = new StableArrayAdapter(this,
                        android.R.layout.simple_list_item_1, participantsList);
                listview.setAdapter(adapter);

                TextView userPayTextView = myDialog.findViewById(R.id.usersMustPayTextview);
                userPayTextView.setText(Double.toString(amountDueSpliter()));
                Button btnAddExpense = myDialog.findViewById(R.id.btn_add_expense);


                btnAddExpense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //math goes here and dialog or toast.
                        amountDueSpliter();
                        Toast toast =Toast.makeText(getApplicationContext(),"You paid!",Toast.LENGTH_LONG);
                       // toast.setMargin(50,50);
                        toast.show();
                        myDialog.dismiss();
                    }
                });

                myDialog.show();
            }
        }
    }

    public ArrayList<ExpenseItem> fillingParticipantArrayListForListView() {

        participantsList = new ArrayList<>();

        for (String participant : eventData.getParticipants()) {
            participantsList.add(participant);
        }

        return participantsList;

    }

    public double amountDueSpliter() {
        Double indivudalPromisedAmount = Double.valueOf(eventData.getTotalExpenses()) / participantsList.size();
        double amountDue = 0;
        double userContribution = Double.parseDouble(calculateTotalContributions(uid));
        amountDue = indivudalPromisedAmount - userContribution;
        System.out.println("The amount due for the user is !! " + amountDue + " sek.");
        return amountDue;

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
