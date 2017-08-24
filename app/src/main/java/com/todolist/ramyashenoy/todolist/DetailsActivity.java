package com.todolist.ramyashenoy.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.todolist.ramyashenoy.todolist.persistence.ToDoListDatabaseHelper;
import com.todolist.ramyashenoy.todolist.persistence.models.Task;

import java.text.SimpleDateFormat;

public class DetailsActivity extends AppCompatActivity {
    int position;
    long id;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent data = getIntent();
        id = data.getIntExtra("id", -1);
        position = data.getIntExtra("position", -1);

        populateViewForDetail(id);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
           populateViewForDetail(id);
        }
    }

    private void populateViewForDetail(long id) {
        ToDoListDatabaseHelper dbHelper = ToDoListDatabaseHelper.getInstance(getApplicationContext());
        Task editTask = dbHelper.getTask(id);

        TextView title = (TextView) findViewById(R.id.detail_title);
        title.setText(editTask.title);
        title.setTag(editTask.id);

        TextView description = (TextView) findViewById(R.id.detail_description);
        description.setText(editTask.description);

        TextView priority = (TextView) findViewById(R.id.detail_priority);
        priority.setText(editTask.priority.toString());

        TextView dueDate = (TextView) findViewById(R.id.detail_due_date);
        dueDate.setText(new SimpleDateFormat("MMM dd YYYY").format(editTask.dueDate));
    }

    public void dismiss(View view) {
        finish();
    }

    public void edit(View view){
        ToDoListDatabaseHelper dbHelper = ToDoListDatabaseHelper.getInstance(getApplicationContext());

        Task task = dbHelper.getTask(id);
        Intent intent = new Intent(DetailsActivity.this, AddEditItemActivity.class);
        intent.putExtra("id", task.id);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void delete(View view){
        ToDoListDatabaseHelper dbHelper = ToDoListDatabaseHelper.getInstance(getApplicationContext());

        dbHelper.deleteTask(id);
        Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("position", position);
        intent.putExtra("actionType", "delete");
        startActivityForResult(intent, REQUEST_CODE);
    }

}
