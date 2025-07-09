package com.codegym.foody.service;

import com.codegym.foody.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMenuService extends IGeneralService<Menu> {
    Page<Menu> findWithPaginationAndKeywordAndRestaurant(String keyword, Long categoryId, Long restaurantId, Pageable pageable);
    Page<Menu> findAllMenusByMerchantId(Long merchantId, String keyword, Long restaurantId, Pageable pageable);
    List<Menu> findByRestaurantId(Long restaurantId);
    Page<Menu> findWithFilters(String keyword, List<Long> categoryIds, List<Long> restaurantIds, Double minPrice, Double maxPrice, Pageable pageable);
}
