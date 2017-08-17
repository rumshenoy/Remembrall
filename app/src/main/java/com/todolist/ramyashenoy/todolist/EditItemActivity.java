package com.todolist.ramyashenoy.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.todolist.ramyashenoy.todolist.persistence.ToDoListDatabaseHelper;
import com.todolist.ramyashenoy.todolist.persistence.models.Task;
import com.todolist.ramyashenoy.todolist.persistence.models.TaskPriority;

import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {

    int position;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
    }

    public void onSubmit(View v) {
        Task newTask = new Task();
        EditText title = (EditText) findViewById(R.id.add_edit_title);
        newTask.title = title.getText().toString();

        EditText description = (EditText) findViewById(R.id.add_edit_description);
        newTask.description = description.getText().toString();

        DatePicker dueDate = (DatePicker) findViewById(R.id.add_edit_due_date);
        Calendar newDate = Calendar.getInstance();
        newDate.set(dueDate.getYear(), dueDate.getMonth(), dueDate.getDayOfMonth());
        newTask.dueDate = newDate.getTime();

        Spinner priority =  (Spinner) findViewById(R.id.priority_dropdown);
        newTask.priority = TaskPriority.valueOf((priority.getSelectedItem().toString()));

        ToDoListDatabaseHelper dbHelper = ToDoListDatabaseHelper.getInstance(getApplicationContext());
        long taskId = dbHelper.addTask(newTask);

        Intent data = new Intent();
        data.putExtra("id", taskId);
        setResult(RESULT_OK, data);
        finish();
    }

    public void dismiss(View view) {
        finish();
    }
}
