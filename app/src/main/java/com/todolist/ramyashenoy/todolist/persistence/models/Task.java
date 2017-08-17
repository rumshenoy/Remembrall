package com.todolist.ramyashenoy.todolist.persistence.models;

import java.util.Date;

/**
 * Created by ramyashenoy on 8/11/17.
 */

public class Task {
    public String title;
    public String description;
    public Date dueDate;
    public TaskPriority priority;
    public Integer id;

    public String getTitle(){
        return title;
    }
}
