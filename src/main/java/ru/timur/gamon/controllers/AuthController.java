package ru.timur.gamon.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.timur.gamon.models.Person;
import ru.timur.gamon.services.PeopleService;
import ru.timur.gamon.services.RegistrationService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final PeopleService peopleService;

    @Autowired
    public AuthController(RegistrationService registrationService, PeopleService peopleService) {
        this.registrationService = registrationService;
        this.peopleService = peopleService;
    }

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("person") Person person, boolean error, Model model) {
        model.addAttribute("error",error);
        return "auth/login";
    }

    @PostMapping("/login")
    public String performLogin(@ModelAttribute("person") Person person, HttpServletResponse httpServletResponse, Model model){
        String jwtToken = null;
        try {
            jwtToken = peopleService.login(person.getUsername(), person.getPassword());
        } catch (Exception e){
            return loginPage(person,true,model);
        }
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setMaxAge(3600); // Установка срока действия cookie (в секундах)
        cookie.setPath("/"); // Установка пути cookie на корень домена

        System.out.println(person + "       ------       " + jwtToken);

        httpServletResponse.addCookie(cookie);

        return "redirect:/index";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(HttpServletResponse httpServletResponse, @ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "auth/registration";

        String jwtToken = registrationService.register(person);

        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setMaxAge(3600); // Установка срока действия cookie (в секундах)
        cookie.setPath("/"); // Установка пути cookie на корень домена

        httpServletResponse.addCookie(cookie);

        return "redirect:/auth/login";
    }
}