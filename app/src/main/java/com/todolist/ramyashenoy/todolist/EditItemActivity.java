package com.todolist.ramyashenoy.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    int position;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        setBodyOfEditText();

    }

    private void setBodyOfEditText() {
        String taskName = getIntent().getStringExtra("taskName");
        position = getIntent().getIntExtra("position", 0);
        id = getIntent().getIntExtra("id", 0);

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(taskName);
        editText.setSelection(editText.getText().length());
    }

    public void onSubmit(View v) {
        EditText etName = (EditText) findViewById(R.id.editText);
        Intent data = new Intent();
        data.putExtra("taskName", etName.getText().toString());
        data.putExtra("id", id);
        data.putExtra("position", position); // ints work too

        setResult(RESULT_OK, data);
        finish();
    }
}
