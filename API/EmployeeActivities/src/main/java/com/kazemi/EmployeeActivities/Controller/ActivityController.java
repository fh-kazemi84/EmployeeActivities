package com.kazemi.EmployeeActivities.Controller;

import com.kazemi.EmployeeActivities.DTO.ActivityDTO;
import com.kazemi.EmployeeActivities.Service.ActivityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author fh.kazemi
 **/
@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private  final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/get")
    public ActivityDTO getActivity(@RequestParam String filePath,
                                   @RequestParam String sheet,
                                   @RequestParam String cell) throws Exception {
        return activityService.getActivity(filePath, sheet, cell);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importFromExcel(@RequestParam String filePath,
                                @RequestParam String sheet) throws Exception {
        activityService.importFromExcel(filePath, sheet);
        return ResponseEntity.ok("Import successful!");
    }

    @GetMapping("/searchByNameAndDate")
    public ResponseEntity<ActivityDTO> getActivityByNameAndDate(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        try {
            ActivityDTO activityDTO = activityService.getActivityByEmployeeNameAndDate(name, date);
            return ResponseEntity.ok(activityDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/searchByNameAndMonth")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByNameAndMonth(
            @RequestParam String name,
            @RequestParam int month) {
        List<ActivityDTO> activities = activityService.getActivitiesByEmployeeNameAndMonth(name, month);
        if (activities.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(activities);
    }
}
