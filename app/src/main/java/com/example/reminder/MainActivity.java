package com.example.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ListView listReminders;
    private ArrayList<Integer> id = new ArrayList<Integer>();
    private ArrayList<String> reminders = new ArrayList<String>();
    private ArrayList<Boolean> important = new ArrayList<Boolean>();
    private MyListAdapter adapter;

    RemindersDbAdapter remindersDbAdapter;
    //RemindersSimpleCursorAdapter remindersSimpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        remindersDbAdapter=new RemindersDbAdapter(this);
        //remindersSimpleCursorAdapter=new RemindersSimpleCursorAdapter(this,);
        //TODO: read reminders and important from database
        updateData();
        adapter = new MyListAdapter(this, reminders, important);
        listReminders = (ListView) findViewById(R.id.reminders_list);
        listReminders.setAdapter(adapter);

        listReminders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showRowMenu(position);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.exit:
                finish();
                System.exit(0);
                return true;
            case R.id.new_reminder:
                openNewReminderDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openNewReminderDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("New Reminder");
        dialog.setContentView(R.layout.dialog_custom);
        final EditText editTextReminder = (EditText) dialog.findViewById(R.id.reminder_edit);
        final CheckBox checkBoxImportant = (CheckBox) dialog.findViewById(R.id.important_checkbox);
        Button commitButton = (Button) dialog.findViewById(R.id.commit_button);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add item in database
                //reminders.add(editTextReminder.getText().toString());
                //important.add(checkBoxImportant.isChecked());
                remindersDbAdapter.createReminder(editTextReminder.getText().toString(),checkBoxImportant.isChecked());
                updateData();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void openEditReminderDialog(String oldReminder, boolean oldImportant, final int index) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Edit Reminder");
        dialog.setContentView(R.layout.dialog_custom);
        final EditText editTextReminder = (EditText) dialog.findViewById(R.id.reminder_edit);
        final CheckBox checkBoxImportant = (CheckBox) dialog.findViewById(R.id.important_checkbox);
        Button commitButton = (Button) dialog.findViewById(R.id.commit_button);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
        editTextReminder.setText(oldReminder);
        checkBoxImportant.setChecked(oldImportant);

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: update item in database
                remindersDbAdapter.updateReminder(new Reminder(id.get(index),editTextReminder.getText().toString(),checkBoxImportant.isChecked()? 1:0));
                //reminders.set(index, editTextReminder.getText().toString());
                //important.set(index, checkBoxImportant.isChecked());
                updateData();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showRowMenu(final int position) {
        String [] rowMenu = {"Edit Reminder", "Delete Reminder"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(rowMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    openEditReminderDialog(reminders.get(position), important.get(position), position);
                }
                else{
                    //TODO: remove item from database
                    //reminders.remove(position);
                    remindersDbAdapter.deleteReminderById(id.get(position));
                    updateData();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        AlertDialog rowMenuDialog = builder.create();
        rowMenuDialog.show();
    }
    public void updateData(){
        id.clear();
        reminders.clear();
        important.clear();
        Cursor fetchedData=remindersDbAdapter.fetchAllReminders();
        if(fetchedData.getCount()>0){
            while(fetchedData.moveToNext()){
                id.add(Integer.parseInt(fetchedData.getString(0)));
                reminders.add(fetchedData.getString(1));
                important.add(Integer.parseInt(fetchedData.getString(2)) ==1 ?true:false);
            }
        }
    }
}