package com.example.splitstack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_dialog);
    }
    public void addUserButton(View view){


        Toast toast = Toast.makeText(getApplicationContext(), "Participant _______ added", Toast.LENGTH_LONG);
        toast.show();






    }
    public void createButton(View view){

    Intent intent = new Intent(this, EventListActivity.class);
    startActivity(intent);

        Toast.makeText(this, "splitStack created", Toast.LENGTH_SHORT).show();



    }
}
