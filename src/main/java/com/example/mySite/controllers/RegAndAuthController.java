package com.example.mySite.controllers;

import com.example.mySite.models.Person;
import com.example.mySite.security.PersonDetails;
import com.example.mySite.servises.PeopleService;
import com.example.mySite.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegAndAuthController {
    private final PersonValidator personValidator;
    private final PeopleService peopleService;

    public RegAndAuthController(PersonValidator personValidator, PeopleService peopleService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(){
        return "index";
    }

    @GetMapping("/info")
    public String info(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person personFromPrincipal = personDetails.getPerson();
        Person person = peopleService.findOne(personFromPrincipal.getId());
        model.addAttribute("person", person);
        return "info";
    }

    @GetMapping("/auth")
    public String loginPage() {
        return "auth";
    }

    @GetMapping("/reg")
    public String regForm(@ModelAttribute("person") Person person) {
        return "reg";
    }

    @PostMapping("/reg")
    public String registration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        person.setEmail(person.getEmail());
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "reg";
        peopleService.savePerson(person);
        return "sucreg";
    }
}