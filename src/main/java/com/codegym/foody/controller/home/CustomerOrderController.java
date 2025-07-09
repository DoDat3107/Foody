package com.codegym.foody.controller.home;


import com.codegym.foody.model.*;
import com.codegym.foody.model.enumable.OrderStatus;
import com.codegym.foody.service.impl.CartItemService;
import com.codegym.foody.service.impl.CartService;
import com.codegym.foody.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orderUser")
public class CustomerOrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public String placeOrder(Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Bạn cần đăng nhập để đặt hàng.");
            return "redirect:/login";
        }
        String username = principal.getName();
        Cart cart = cartService.getCartByUser(username);
        if (cart.getCartItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Giỏ hàng của bạn trống.");
            return "redirect:/cart";
        }
        Restaurant restaurant = cart.getCartItems().get(0).getMenu().getRestaurant();
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setRestaurant(restaurant);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(cartService.calculateTotalAmount(cart));
        orderService.saveOrder(order);
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenu(cartItem.getMenu());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getMenu().getPrice());
            orderService.saveOrderItem(orderItem);
        }
        cartService.clearCart(cart);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Đặt hàng thành công.");
        return "redirect:/cart";
    }
    @GetMapping("/orders")
    public String viewUserOrders(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        List<Order> orders = orderService.getOrdersByUsername(username);

        if (orders == null || orders.isEmpty()) {
            model.addAttribute("message", "Bạn chưa có hóa đơn nào.");
            model.addAttribute("orders", null);
        } else {
            model.addAttribute("orders", orders);
        }

        return "/home/order/user-orders";
    }
    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetail(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        Order order = orderService.getOrderById(id);

        if (!order.getUser().getUsername().equals(username)) {
            return "redirect:/403";
        }

        model.addAttribute("order", order);
        return "home/order/user-order-detail";
    }
}
