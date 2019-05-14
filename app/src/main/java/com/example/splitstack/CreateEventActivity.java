package com.example.splitstack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }
    public void addUserButton(View view){


        Toast toast = Toast.makeText(getApplicationContext(), "Participant _______ added", Toast.LENGTH_LONG); // initiate the Toast with context, message and duration for the Toast
        toast.show(); // display the Toast

        TextView textView8 = findViewById(R.id.editText8);

        textView8.setText("");

    }
    public void createButton(View view){



    }
}
