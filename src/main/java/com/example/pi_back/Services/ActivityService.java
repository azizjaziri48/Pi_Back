package com.example.pi_back.Services;

import com.example.pi_back.Entities.Activity;
import java.util.List;

public interface ActivityService {
    List<Activity> retrieveAllActivity();
    Activity AddActivity (Activity activity);
    void removeActivity (int idActivity);
    Activity retrieveActivity (int idActivity);
    Activity updateActivity (Activity activity);
    Activity assignActivityToInterService(Integer idAct, Integer idInterService);
}
