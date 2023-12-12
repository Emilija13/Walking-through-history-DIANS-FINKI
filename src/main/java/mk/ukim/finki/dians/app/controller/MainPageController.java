package mk.ukim.finki.dians.app.controller;

import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.dians.app.model.Heritage;
import mk.ukim.finki.dians.app.service.HeritageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/mainPage")
public class MainPageController {

    @Autowired
    private HeritageService heritageService;
    //public final HeritageService heritageService;

    //public MainPageController(HeritageService heritageService) {
     //   this.heritageService = heritageService;
    //}

    @GetMapping
    public String  getMainPage(Model model)
    {
        List<Heritage> allHeritages = heritageService.findAll();
        //System.out.println("Number of heritages found: " + allHeritages.size());
        model.addAttribute("cities",heritageService.findAllCities());
        model.addAttribute("categories",heritageService.findAllCategories());
        model.addAttribute("heritages",heritageService.findAll());
        return "mainPage";
    }

    @GetMapping("/search")
    public String searchHeritage(@RequestParam(name = "name", required = false) String name,@RequestParam(name = "city", required = false) String city,@RequestParam(name = "category", required = false) String category, Model model) {
        model.addAttribute("cities",heritageService.findAllCities());
        model.addAttribute("categories",heritageService.findAllCategories());
        model.addAttribute("heritages", heritageService.search(name,city,category));
        return "mainPage";
    }

}
