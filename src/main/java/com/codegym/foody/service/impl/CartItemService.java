package com.codegym.foody.service.impl;

import com.codegym.foody.model.CartItem;
import com.codegym.foody.repository.ICartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    @Autowired
    private ICartItemRepository iCartItemRepository;

    public CartItem getCartItemById(Long cartItemId) {
        return iCartItemRepository.findById(cartItemId).orElse(null);
    }

    public void saveCartItem(CartItem cartItem) {
        iCartItemRepository.save(cartItem);
    }

    public void removeCartItem(Long cartItemId) {iCartItemRepository.deleteById(cartItemId);
    }
}