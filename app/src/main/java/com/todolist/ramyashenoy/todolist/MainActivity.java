package com.todolist.ramyashenoy.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.todolist.ramyashenoy.todolist.persistence.ToDoListDatabaseHelper;
import com.todolist.ramyashenoy.todolist.persistence.models.Task;

import java.util.ArrayList;

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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        dbHelper = ToDoListDatabaseHelper.getInstance(getApplicationContext());

        tasks = dbHelper.getAllTasks();
        tasksAdapter = new TasksAdapter(this, tasks);

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(tasksAdapter);
        setUpListViewListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_add_task:
                showAddEditActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddEditActivity() {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Bundle extras = data.getExtras();
            long id = extras.getLong("id");

            Task task = dbHelper.getTask(id);
            tasks.set(0, task);
            tasksAdapter.notifyDataSetChanged();

            Toast.makeText(this, task.title, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task taskToDelete = (Task) lvItems.getItemAtPosition(position);
                tasks.remove(position);
                tasksAdapter.notifyDataSetChanged();
                dbHelper.deleteTask(taskToDelete);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) lvItems.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("taskName", task.title);
                intent.putExtra("position", position);
                intent.putExtra("id", task.id);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

//    public void OnAddItem(View v){
//        EditText etNewItem = (EditText) findViewById(R.id.etNewItems);
//        String itemText = etNewItem.getText().toString();
//        tasksAdapter.add(itemText);
//        etNewItem.setText("");
//
//        Task newTask = new Task();
//        newTask.title = itemText;
//        dbHelper.addTask(newTask);
//    }
}
