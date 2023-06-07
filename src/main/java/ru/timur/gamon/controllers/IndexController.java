package ru.timur.gamon.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.timur.gamon.services.NewsService;
import ru.timur.gamon.services.PeopleService;

@Controller
@RequestMapping("/index")
public class IndexController {
    private final NewsService newsService;
    private final PeopleService peopleService;

    @Autowired
    public IndexController(NewsService newsService, PeopleService peopleService) {
        this.newsService = newsService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(Model model, HttpServletRequest request){

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

        if (jwtToken != null){
            model.addAttribute("person",peopleService.loadUser(jwtToken));
        }

        return "index";
    }

    @GetMapping("/gallery")
    public String gallery(){
        return "gallery";
    }

    @GetMapping("/news")
    public String news(Model model){
        model.addAttribute("newsList",newsService.getNews());
        return "news";
    }

    @GetMapping("/news/{id}")
    public String showNews(@PathVariable int id, Model model){
        model.addAttribute("news",newsService.getOne(id));
        return "showNews";
    }

}
