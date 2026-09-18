package ru.arman.taskflow.model;

import java.util.ArrayList;
import java.util.List;

public class Project extends BaseEntity {
    private String name;
    private List<Task> tasks = new ArrayList<>();

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    public Project(String name) {
        this.name = name;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
