package com.example.pi_back.Services;

import com.example.pi_back.Entities.Activity;
import com.example.pi_back.Repositories.ActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class ActivityServiceImpl implements ActivityService{
    private ActivityRepository activityRepository;
    @Override
    public List<Activity> retrieveAllActivity() {
        return activityRepository.findAll();
    }

    @Override
    public Activity AddActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    @Override
    public void removeActivity(int idActivity) {
     activityRepository.deleteById(idActivity);
    }

    @Override
    public Activity retrieveActivity(int idActivity) {
        return activityRepository.findById(idActivity).orElse(null);
    }

    @Override
    public Activity updateActivity(Activity activity) {
        return activityRepository.save(activity);
    }

}
