package com.codegym.foody.controller.home;

import com.codegym.foody.model.Category;
import com.codegym.foody.model.Menu;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.dto.PaginationResult;
import com.codegym.foody.service.ICategoryService;
import com.codegym.foody.service.IMenuService;
import com.codegym.foody.service.IRestaurantService;
import com.codegym.foody.service.impl.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home/menus")
public class HomeMenuController {
    @Autowired
    private IMenuService menuService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IRestaurantService restaurantService;

    @Autowired
    private PaginationService paginationService;

    @GetMapping
    public String listMenus(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) List<Long> categoryIds,
            @RequestParam(value = "restaurantId", required = false) List<Long> restaurantIds,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
        Page<Menu> menuPage = menuService.findWithFilters(keyword, categoryIds, restaurantIds, minPrice, maxPrice, pageable);

        List<Category> categories = categoryService.getAllCategories();
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        PaginationResult paginationResult = paginationService.calculatePagination(menuPage);

        model.addAttribute("menuPage", menuPage);
        model.addAttribute("menus", menuPage.getContent());
        model.addAttribute("categories", categories);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("currentPage", paginationResult.getCurrentPage());
        model.addAttribute("totalPages", paginationResult.getTotalPages());
        model.addAttribute("pageNumbers", paginationResult.getPageNumbers());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryIds);
        model.addAttribute("restaurantId", restaurantIds);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "home/menu/list";
    }
}
