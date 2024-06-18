package com.example.mySite.controllers;

import com.example.mySite.models.Person;
import com.example.mySite.models.Role;
import com.example.mySite.security.PersonDetails;
import com.example.mySite.servises.PeopleService;
import com.example.mySite.util.PersonValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PeopleController {
    private final PersonValidator personValidator;
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PersonValidator personValidator, PeopleService peopleService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person personFromPrincipal = personDetails.getPerson();
        Person person = peopleService.findOne(id);
        if ((personFromPrincipal.getId() == id) |
                (personFromPrincipal.getRole().equals(Role.ROLE_ADMIN))) {
            model.addAttribute("person", person);
            return "update";
        }
        return "redirect:/info";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "/update";
        }
        Person updatedPerson = peopleService.findOne(id);
        if (!person.getEmail().equalsIgnoreCase(updatedPerson.getEmail())) {
            personValidator.validate(person, bindingResult);
            if (bindingResult.hasErrors()) {
                return "/update";
            }
        }
        updatedPerson.setUsername(person.getUsername());
        updatedPerson.setEmail(person.getEmail());
        peopleService.update(updatedPerson.getId(), updatedPerson);
        return "/sucupdate";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person personFromPrincipal = personDetails.getPerson();
        if (personFromPrincipal.getId() == id) {
            peopleService.delete(id);
            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
            return "redirect:/";
        }
        if (personFromPrincipal.getRole().equals(Role.ROLE_ADMIN)) {
            peopleService.delete(id);
            return "redirect:/view";
        } else return "redirect:/info";
    }
}
