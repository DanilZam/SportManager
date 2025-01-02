package org.app.sportmanager.fx_nodes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TrainingTableRow {

    private final StringProperty trainName;
    private final StringProperty trainDate;
    private final StringProperty muscles;

    public TrainingTableRow(String trainName, String trainDate, String muscles) {
        this.trainName = new SimpleStringProperty(trainName);
        this.trainDate = new SimpleStringProperty(trainDate);
        this.muscles = new SimpleStringProperty(muscles);
    }

    public String getTrainName() {
        return trainName.get();
    }

    public StringProperty trainNameProperty() {
        return trainName;
    }

    public String getTrainDate() {
        return trainDate.get();
    }

    public StringProperty trainDateProperty() {
        return trainDate;
    }

    public String getMuscles() {
        return muscles.get();
    }

    public StringProperty musclesProperty() {
        return muscles;
    }

}
