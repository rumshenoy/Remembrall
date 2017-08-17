package com.todolist.ramyashenoy.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.todolist.ramyashenoy.todolist.persistence.ToDoListDatabaseHelper;
import com.todolist.ramyashenoy.todolist.persistence.models.Task;
import com.todolist.ramyashenoy.todolist.persistence.models.TaskPriority;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddEditItemActivity extends AppCompatActivity {

    int position;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent data = getIntent();
        id = data.getIntExtra("id", -1);
        position = data.getIntExtra("position", -1);
        if(id > 0){
            populateViewForEdit(id);
        }else{
            populateEmptyView();
        }
    }

    private void populateEmptyView() {
        EditText title = (EditText) findViewById(R.id.add_edit_title);
        title.setTag(-1);
    }

    private void populateViewForEdit(long id) {
        ToDoListDatabaseHelper dbHelper = ToDoListDatabaseHelper.getInstance(getApplicationContext());
        Task editTask = dbHelper.getTask(id);

        EditText title = (EditText) findViewById(R.id.add_edit_title);
        title.setText(editTask.title);
        title.setTag(editTask.id);

        EditText description = (EditText) findViewById(R.id.add_edit_description);
        description.setText(editTask.description);

        DatePicker dueDate = (DatePicker) findViewById(R.id.add_edit_due_date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(editTask.dueDate);
        dueDate.updateDate(calendar.YEAR, calendar.MONTH, calendar.DAY_OF_MONTH);

        Spinner priority =  (Spinner) findViewById(R.id.priority_dropdown);
        priority.setSelection(((ArrayAdapter<String>)priority.getAdapter()).getPosition(editTask.priority.toString()));

    }

    public void onSubmit(View v) {
        Task newTask = new Task();
        ToDoListDatabaseHelper dbHelper = ToDoListDatabaseHelper.getInstance(getApplicationContext());

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

        int id = (int) title.getTag();
        if(id > 0){
            newTask.id = id;
            this.id = id;
            dbHelper.updateTask(newTask);
        }else{
            this.id = dbHelper.addTask(newTask);
            position = -1;
        }

        Intent data = new Intent();
        data.putExtra("id", this.id);
        data.putExtra("position", position);
        setResult(RESULT_OK, data);
        finish();
    }

    public void dismiss(View view) {
        finish();
    }
}
