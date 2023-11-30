package com.ali.scheduler.controller;

import com.ali.scheduler.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduler")
public class MainController {

    private final ScheduleService scheduleService;

    @Autowired
    public MainController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedulePresentations")
    public String schedulePresentations(@RequestBody String data) {
        return scheduleService.schedulePresentations(data);
    }
}
