package com.codegym.foody.controller.merchant;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/merchant/dashboard")
public class MerchantDashboardController {
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("page", "dashboard");
        return "merchant/dashboard";
    }
}
