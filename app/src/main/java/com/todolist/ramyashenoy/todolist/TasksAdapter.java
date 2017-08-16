package com.todolist.ramyashenoy.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.todolist.ramyashenoy.todolist.persistence.models.Task;

import java.util.ArrayList;

/**
 * Created by ramyashenoy on 8/11/17.
 */

public class TasksAdapter extends ArrayAdapter {
    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = (Task) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.taskTitle);
        tvName.setText(task.title);
        return convertView;
    }

}
