package th.co.easygas.admin.easygasdeliver.Model;

import java.io.Serializable;

public class Tasks implements Serializable{

    private final int taskId;
    private final String storeName;
    private final double latitude;
    private final double longitude;
    private final int tanksQuantity;
    private final String taskType;

    public Tasks(int taskId, String storeName, double latitude, double longitude, int tanksQuantity
    , String taskType) {
        this.taskId = taskId;
        this.storeName = storeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tanksQuantity = tanksQuantity;
        this.taskType = taskType;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getStoreName() {
        return storeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getTanksQuantity() {
        return tanksQuantity;
    }

    public String getTaskType() {
        return taskType;
    }
}
