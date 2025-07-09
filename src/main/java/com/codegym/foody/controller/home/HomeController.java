package com.codegym.foody.controller.home;

import com.codegym.foody.model.Category;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.service.impl.CategoryService;
import com.codegym.foody.service.impl.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public String homePage(Model model) {
        List<Restaurant> activeRestaurants = restaurantService.getActiveRestaurants();
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("restaurants", activeRestaurants);
        model.addAttribute("categories", categories);
        return "home/home";
    }
}
