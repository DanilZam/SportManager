package org.app.sportmanager.models;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Training {
    private String id;
    private String userId;
    private LocalDate date;
    private String name;
    private Map<String, List<Integer>> exerciseMap;//Map<exerciseId,sets>

    public Training(String id, String userId, LocalDate date, String name, Map<String, List<Integer>> exerciseMap) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.name = name;
        this.exerciseMap = exerciseMap;
    }

    public Training() {
        this.id = "id";
        this.userId = "userId";
        this.date = null;
        this.name = "name";
        this.exerciseMap = null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, List<Integer>> getExerciseMap() {
        return exerciseMap;
    }

    public void setExerciseMap(Map<String, List<Integer>> exerciseMap) {
        this.exerciseMap = exerciseMap;
    }
}
