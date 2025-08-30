package com.kazemi.EmployeeActivities.Controller;

import com.kazemi.EmployeeActivities.Models.CellInfo;
import com.kazemi.EmployeeActivities.Service.ExcelService;
import org.springframework.web.bind.annotation.*;

/**
 * @author fh.kazemi
 **/
@RestController
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/load")
    public String loadFile(
            @RequestParam String filePath,
            @RequestParam String sheet) throws Exception {
        excelService.load(filePath, sheet);
        return filePath + " with sheet " + sheet + " is loaded. ✅";
    }

    @GetMapping("/read-cell")
    public CellInfo readCell(@RequestParam String cell) {
        return excelService.readCell(cell);
    }

    @PostMapping("/close")
    public String closeFile() throws Exception {
        excelService.close();
        return "File is closed. ✅";
    }
}
