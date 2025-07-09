package com.codegym.foody.service;

import com.codegym.foody.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    void save(Cart cart);
    Cart getCartByUser(String username);
    void addItemToCart(Long menuId, String username);
    void updateCartItemQuantity(Long cartItemId, Integer quantity, String username);
    void removeItemFromCart(Long cartItemId, String username);
    BigDecimal calculateTotalAmount(Cart cart);

    void clearCart(Cart cart);
}
