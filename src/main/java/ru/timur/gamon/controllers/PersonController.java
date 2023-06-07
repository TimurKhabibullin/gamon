package ru.timur.gamon.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.timur.gamon.models.Person;
import ru.timur.gamon.services.BookingService;
import ru.timur.gamon.services.PeopleService;

@Controller
@RequestMapping("/person")
public class PersonController {
    private final BookingService bookingService;
    private final PeopleService peopleService;

    @Autowired
    public PersonController(BookingService bookingService, PeopleService peopleService) {
        this.bookingService = bookingService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String showUser(Model model, HttpServletRequest request){

        Cookie[] cookies = request.getCookies();

        // Поиск куки с именем, содержащим JWT-токен
        String jwtToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken == null){
            return "redirect:/auth/login";
        }
        Person person = null;
        try {
            person = peopleService.loadUser(jwtToken);
        } catch (Exception ignored){

        }

        System.out.println(person);


        model.addAttribute("person",person);
        assert person != null;
        model.addAttribute("bookingList",bookingService.findBookingByPersonId(jwtToken));
        return "person/show";
    }

    @GetMapping("/edit")
    public String edit(Model model, HttpServletRequest request){



        Cookie[] cookies = request.getCookies();

        // Поиск куки с именем, содержащим JWT-токен
        String jwtToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken == null){
            return "redirect:/auth/login";
        }
        Person person = null;
        try {
            person = peopleService.loadUser(jwtToken);
        } catch (Exception ignored){

        }


        model.addAttribute("person",person);
        return "person/edit";
    }

    @PostMapping("/edit")
    public String doEdit(@ModelAttribute("person")Person person, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        // Поиск куки с именем, содержащим JWT-токен
        String jwtToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken == null){
            return "redirect:/auth/login";
        }
        Person personToBeUpdated = null;
        try {
            personToBeUpdated = peopleService.loadUser(jwtToken);
        } catch (Exception ignored){

        }

        assert personToBeUpdated != null;
        personToBeUpdated.setUsername(person.getUsername());
        personToBeUpdated.setEmail(person.getEmail());

        peopleService.edit(personToBeUpdated,jwtToken);
        return "redirect:/person";
    }

    @GetMapping("/balance/{id}")
    public String balance(Model model, @PathVariable int id){
        Person person = new Person();
        person.setId(id);
        model.addAttribute("person",person);
        return "person/card";
    }

    @PostMapping("/balance/top_up")
    public String toUpBalance(@ModelAttribute("person") Person person, HttpServletRequest request){


        Cookie[] cookies = request.getCookies();

        // Поиск куки с именем, содержащим JWT-токен
        String jwtToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken == null){
            return "redirect:/auth/login";
        }
        Person personToBeUpdated = null;
        try {
            personToBeUpdated = peopleService.loadUser(jwtToken);
        } catch (Exception ignored){

        }

        assert personToBeUpdated != null;
        personToBeUpdated.setBalance((person.getBalance() * 60) + personToBeUpdated.getBalance());

        peopleService.edit(personToBeUpdated,jwtToken);

        return "redirect:/person";
    }
}
