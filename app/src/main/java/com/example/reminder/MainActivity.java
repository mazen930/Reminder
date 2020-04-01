package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    RemindersDbAdapter remindersDbAdapter;
    //RemindersSimpleCursorAdapter remindersSimpleCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        remindersDbAdapter=new RemindersDbAdapter(this);
        //remindersSimpleCursorAdapter=new RemindersSimpleCursorAdapter(this);
    }
}
