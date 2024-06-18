package com.example.mySite.controllers;

import com.example.mySite.servises.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private final PeopleService peopleService;

    @Autowired
    public AdminController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/view")
    public String viewAll(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "view";
    }
}
