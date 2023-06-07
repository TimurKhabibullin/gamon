package ru.timur.gamon.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.timur.gamon.models.News;
import ru.timur.gamon.services.BookingService;
import ru.timur.gamon.services.NewsService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final NewsService newsService;
    private final BookingService bookingService;

    @Autowired
    public AdminController(NewsService newsService, BookingService bookingService) {
        this.newsService = newsService;
        this.bookingService = bookingService;
    }

    @GetMapping()
    public String index(){
        return "admin/index";
    }

    @GetMapping("/create")
    public String createNews(@ModelAttribute("news")News news){
        return "admin/createNews";
    }

    @PostMapping("/create-news")
    public String create(@ModelAttribute("news") News news, HttpServletRequest request){

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

        newsService.createNews(news,jwtToken);
        return "redirect:/admin";
    }

    @GetMapping("statistic")
    public String statistic(Model model,HttpServletRequest request){


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
        model.addAttribute("bookings",bookingService.findAll(jwtToken));
        return "admin/statistic";
    }

}
