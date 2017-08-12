package com.todolist.ramyashenoy.todolist.persistence.models;

import java.util.Date;

/**
 * Created by ramyashenoy on 8/11/17.
 */

public class Task {
    public String title;
    public String details;
    public Date dueDate;
    public Date dateCreated;
    public TaskPriority priority;
    public int id;

    public String getTitle(){
        return title;
    }
}
