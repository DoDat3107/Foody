package com.codegym.foody.controller.home;

import com.codegym.foody.model.Menu;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.service.impl.MenuService;
import com.codegym.foody.service.impl.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home/restaurant")
public class HomeRestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/{id}")
    public String restaurantDetail(@PathVariable("id") Long id, Model model) {
        Restaurant restaurant = restaurantService.getById(id);
        List<Menu> menus = menuService.findByRestaurantId(id);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menus", menus);
        return "home/restaurant/detail";
    }
}
