package org.app.sportmanager.models;

import java.util.List;

public class Exercise {

    private String id;
    private String name;
    private List<String> muscleGroups;
    private String description;

    public Exercise(String  id, String name, List<String> muscleGroups, String description) {
        this.id = id;
        this.name = name;
        this.muscleGroups = muscleGroups;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMuscleGroups() {
        return muscleGroups;
    }

    public void setMuscleGroups(List<String> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
