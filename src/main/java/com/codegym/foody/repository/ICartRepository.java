package com.codegym.foody.repository;

import com.codegym.foody.model.Cart;
import com.codegym.foody.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
