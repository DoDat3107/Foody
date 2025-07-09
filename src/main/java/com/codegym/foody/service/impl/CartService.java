package com.codegym.foody.service.impl;

import com.codegym.foody.model.Cart;
import com.codegym.foody.model.CartItem;
import com.codegym.foody.model.Menu;
import com.codegym.foody.model.User;
import com.codegym.foody.repository.ICartItemRepository;
import com.codegym.foody.repository.ICartRepository;
import com.codegym.foody.repository.IMenuRepository;
import com.codegym.foody.repository.IUserRepository;
import com.codegym.foody.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService implements ICartService {
    @Autowired
    private ICartRepository cartRepository;
    @Autowired
    private ICartItemRepository cartItemRepository ;
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IMenuRepository menuRepository;

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("không tìm thấy: " + username);
        }

        User user = optionalUser.get();
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });
    }

    @Override
    public void addItemToCart(Long menuId, String username) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("Món ăn không tồn tại"));
        Cart cart = getCartByUser(username);

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getMenu().getId().equals(menuId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
        } else {
            CartItem newItem = new CartItem();
            newItem.setMenu(menu);
            newItem.setQuantity(1);
            newItem.setCart(cart);
            cart.getCartItems().add(newItem);
        }

        cartRepository.save(cart);
    }

    @Override
    public void updateCartItemQuantity(Long cartItemId, Integer quantity, String username) {
        Cart cart = getCartByUser(username);

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại trong giỏ"));

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartItemId, String username) {
        Cart cart = getCartByUser(username);

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại trong giỏ"));

        cart.getCartItems().remove(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public BigDecimal calculateTotalAmount(Cart cart) {
        return cart.getCartItems().stream()
                .map(item -> item.getMenu().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
@Override
public void clearCart(Cart cart) {
            cartItemRepository.deleteAll(cart.getCartItems());
            cart.getCartItems().clear();
            cartRepository.save(cart);
    }
}

