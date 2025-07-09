package com.codegym.foody.controller.home;
import com.codegym.foody.model.*;
import com.codegym.foody.service.impl.CartItemService;
import com.codegym.foody.service.impl.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CustomerCartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        Cart cart = cartService.getCartByUser(username);

        BigDecimal totalAmount = cartService.calculateTotalAmount(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", totalAmount);
        return "home/cart/view";
    }

    @PostMapping("/add/{menuId}")
    public String addToCart(@PathVariable Long menuId, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Bạn cần đăng nhập để thêm món vào giỏ");
            return "redirect:/login";
        }

        String username = principal.getName();
        cartService.addItemToCart(menuId, username);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm món vào giỏ hàng thành công.");
        return "redirect:/cart";
    }

    @PostMapping(value = "/update", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCart(@RequestParam Long cartItemId, @RequestParam Integer quantity) {
        Map<String, Object> response = new HashMap<>();

        if (quantity < 1) {
            response.put("success", false);
            response.put("message", "Số lượng không hợp lệ.");
            return ResponseEntity.badRequest().body(response);
        }

        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        if (cartItem == null) {
            response.put("success", false);
            response.put("message", "Không có sản phẩm nào trong giỏ hàng.");
            return ResponseEntity.badRequest().body(response);
        }

        cartItem.setQuantity(quantity);
        BigDecimal updatedTotal = cartItem.getMenu().getPrice().multiply(BigDecimal.valueOf(quantity));
        cartItemService.saveCartItem(cartItem);

        BigDecimal cartTotal = cartService.calculateTotalAmount(cartItem.getCart());

        response.put("success", true);
        response.put("updatedTotal", updatedTotal);
        response.put("cartTotal", cartTotal);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/remove")
    public String removeFromCart(@RequestParam Long cartItemId, RedirectAttributes redirectAttributes) {
        try {
            cartItemService.removeCartItem(cartItemId);
            redirectAttributes.addFlashAttribute("messageType", "success");
            redirectAttributes.addFlashAttribute("message", "Xóa món khỏi giỏ hàng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Lỗi khi xóa món ăn");
        }
        return "redirect:/cart";
    }

}

