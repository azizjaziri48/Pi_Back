package com.example.pi_back.Services;

import com.example.pi_back.Entities.Activity;
import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Repositories.ActivityRepository;
import com.example.pi_back.Repositories.InternalServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class ActivityServiceImpl implements ActivityService{
    private ActivityRepository activityRepository;
    private InternalServiceRepository internalServiceRepository;
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
    @Override
    public Activity assignActivityToInterService(Integer idAct, Integer idInterService){
        Activity activity=activityRepository.findById(idAct).orElse(null);
        InternalService internalService=internalServiceRepository.findById(idInterService).orElse(null);
        activity.setInternalService(internalService);
        return activityRepository.save(activity);
    }

}
