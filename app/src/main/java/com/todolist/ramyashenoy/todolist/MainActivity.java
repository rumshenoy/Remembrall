package com.todolist.ramyashenoy.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.todolist.ramyashenoy.todolist.persistence.ToDoListDatabaseHelper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ListView lvItems;
    ArrayAdapter<String> itemsAdapter;
    private final int REQUEST_CODE = 20;
    private ToDoListDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = ToDoListDatabaseHelper.getInstance(getApplicationContext());

        readItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setUpListViewListener();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String taskName = data.getExtras().getString("taskName");
            int position = data.getExtras().getInt("position", 0);

            items.set(position, taskName);
            itemsAdapter.notifyDataSetChanged();

            writeItems();
            Toast.makeText(this, taskName, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String taskName = (String) lvItems.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("taskName", taskName);
                intent.putExtra("position", position);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File toDoFile = new File(filesDir, "todo.txt");
        try{
            items = new ArrayList<>(FileUtils.readLines(toDoFile));
        }catch(IOException e){
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void OnAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItems);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }
}
