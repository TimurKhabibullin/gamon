package ru.timur.gamon.controllers;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.timur.gamon.models.Booking;
import ru.timur.gamon.models.Computer;
import ru.timur.gamon.services.BookingService;
import ru.timur.gamon.services.ComputerService;

@Controller
@RequestMapping("/booking")
public class BookController {
    private final ComputerService computerService;
    private final BookingService bookingService;

    @Autowired
    public BookController(ComputerService computerService, BookingService bookingService) {
        this.computerService = computerService;
        this.bookingService = bookingService;
    }

    @GetMapping()
    public String booking(Model model, HttpServletRequest request){
        model.addAttribute("computers",computerService.showAll(getJwtToken(request)));
        return "booking/index";
    }

    @GetMapping("/{id}")
    public String bookingPage(Model model, @PathVariable int id,boolean error,HttpServletRequest request){
        Booking booking = new Booking();
        booking.setError(error);
        Computer computer = null;
        try {
            computer = computerService.findById(id,getJwtToken(request));
        } catch (Exception ignored){

        }

        model.addAttribute("computer",computer);
        model.addAttribute("booking",booking);
        model.addAttribute("bookingList",bookingService.findAllByComputer(id,getJwtToken(request)));
        return "booking/show";
    }

    @PostMapping("/create/{id}")
    public String createBooking(@PathVariable int id, @ModelAttribute("booking") Booking booking,Model model, HttpServletRequest request){
        boolean resultOfCreatingBooking = bookingService.create(booking,id,getJwtToken(request));
        if (!resultOfCreatingBooking){
            model.addAttribute("booking",booking);
            return bookingPage(model,id,true,request);
        }
        return "redirect:/booking";
    }

    @GetMapping("/edit/{id}")
    public String editBooking(@PathVariable int id,Model model, HttpServletRequest request){
        model.addAttribute("booking",bookingService.findById(id,getJwtToken(request)));
        return "booking/edit";
    }

    @PostMapping("/delete/{id}")
    public String deleteBooking(@PathVariable int id,HttpServletRequest request){
        bookingService.delete(id, getJwtToken(request));
        return "redirect:/person";
    }

    public String getJwtToken(HttpServletRequest request){
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

        return jwtToken;
    }
}