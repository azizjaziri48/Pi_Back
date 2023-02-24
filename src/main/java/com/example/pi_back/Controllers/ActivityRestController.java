package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Activity;
import com.example.pi_back.Services.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/activity")
public class ActivityRestController {
    private ActivityService activityService;
    @GetMapping("/all")
    List<Activity> retrieveAllActivity() {
        return activityService.retrieveAllActivity();
    }
    @PostMapping("/add")
    Activity AddActivity(@RequestBody Activity activity){
        return activityService.AddActivity(activity);
    }
    @DeleteMapping("/delete/{id}")
    void removeActivity (@PathVariable("id") Integer idActivity){
        activityService.removeActivity(idActivity);
    }
    @GetMapping("/get/{id}")
    Activity retrieveActivity (@PathVariable("id") Integer idActivity){
        return activityService.retrieveActivity(idActivity);
 }
 @PutMapping("/update")
    Activity updateActivity (@RequestBody Activity activity){
        return activityService.updateActivity(activity);
 }

}
