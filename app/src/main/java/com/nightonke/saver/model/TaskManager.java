package com.nightonke.saver.model;

/**
 * Created by Weiping on 2016/1/27.
 */
public class TaskManager {

    public static int QUERY_UPDATE_TASK = 0;































    private static TaskManager ourInstance = new TaskManager();

    public static TaskManager getInstance() {
        return ourInstance;
    }

    private TaskManager() {
    }
}
