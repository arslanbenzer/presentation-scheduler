package com.ali.scheduler.controller;

import com.ali.scheduler.dto.PresentationList;
import com.ali.scheduler.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/scheduler-ui")
public class UiController {

    private final ScheduleService scheduleService;

    @Autowired
    public UiController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/form")
    public String formPage(Model model) {
        model.addAttribute(new PresentationList());
        return "form";
    }

    @PostMapping("/schedule")
    public String schedule(@ModelAttribute PresentationList presentation, Model model) {
        model.addAttribute("result", scheduleService.schedulePresentations(presentation.getData()));
        return "result";
    }
}
