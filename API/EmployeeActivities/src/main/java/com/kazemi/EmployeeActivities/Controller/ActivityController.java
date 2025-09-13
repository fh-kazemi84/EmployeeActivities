package com.kazemi.EmployeeActivities.Controller;

import com.kazemi.EmployeeActivities.DTO.ActivityDTO;
import com.kazemi.EmployeeActivities.Model.Activity;
import com.kazemi.EmployeeActivities.Service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
