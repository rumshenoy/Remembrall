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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_row, parent, false);
        }
        TextView rowTitle = (TextView) convertView.findViewById(R.id.row_title);
        rowTitle.setText(task.title);

        TextView rowPriority = (TextView) convertView.findViewById(R.id.row_priority);
        rowPriority.setText(task.priority.toString());

        return convertView;
    }

}
