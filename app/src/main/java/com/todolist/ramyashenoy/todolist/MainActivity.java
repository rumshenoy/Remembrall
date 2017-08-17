package com.todolist.ramyashenoy.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.todolist.ramyashenoy.todolist.persistence.ToDoListDatabaseHelper;
import com.todolist.ramyashenoy.todolist.persistence.models.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> tasks;
    ListView lvItems;
    TasksAdapter tasksAdapter;
    private final int REQUEST_CODE = 20;
    private ToDoListDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = ToDoListDatabaseHelper.getInstance(getApplicationContext());

        tasks = dbHelper.getAllTasks();
        tasksAdapter = new TasksAdapter(this, tasks);

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(tasksAdapter);
        setUpListViewListener();

    }

    public void showAddEditActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Bundle extras = data.getExtras();
            long id = extras.getLong("id");
            int position = extras.getInt("position");
            String actionType = extras.getString("actionType");

            Task task = dbHelper.getTask(id);
            if(position > 0){
                tasks.set(position, task);
            }
            else if(actionType == "delete"){
                tasks.remove(position);
            }else{
                tasks.add(task);
            }
            sortTasksByPriority();
            tasksAdapter.notifyDataSetChanged();

            Toast.makeText(this, task.title, Toast.LENGTH_SHORT).show();
        }
    }

    private void sortTasksByPriority() {
        Collections.sort(tasks, new Comparator<Task>(){
            @Override
            public int compare(Task o1, Task o2) {
                return o1.priority.compareTo(o2.priority);
            }
        });
    }

    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task taskToDelete = (Task) lvItems.getItemAtPosition(position);
                tasks.remove(position);
                tasksAdapter.notifyDataSetChanged();
                dbHelper.deleteTask(taskToDelete.id);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) lvItems.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("id", task.id);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }
}
