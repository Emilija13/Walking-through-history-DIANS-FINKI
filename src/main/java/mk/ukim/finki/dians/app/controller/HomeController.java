package mk.ukim.finki.dians.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value={"","/home"})
public class HomeController {
    @GetMapping
    public String  getHomePage(Model model)
    {
        return "home";
    }
}
