package com.codegym.foody.service.impl;

import com.codegym.foody.model.Menu;
import com.codegym.foody.repository.ICartItemRepository;
import com.codegym.foody.repository.IMenuRepository;
import com.codegym.foody.repository.IOrderItemRepository;
import com.codegym.foody.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService implements IMenuService {
    @Autowired
    private IMenuRepository menuRepository;

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Autowired
    private IOrderItemRepository orderItemRepository;

    @Override
    public void save(Menu menu) {
        menuRepository.save(menu);
    }

    @Override
    public void update(Menu menu) {
        if (menuRepository.existsById(menu.getId())) {
            menuRepository.save(menu);
        } else {
            throw new RuntimeException("Không tìm thấy thực đơn");
        }
    }

    @Override
    public void delete(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Thực đơn không tồn tại"));

        if (!cartItemRepository.findByMenuId(id).isEmpty()) {
            throw new IllegalArgumentException("Không thể xóa món ăn này vì vẫn còn trong giỏ hàng.");
        }

        if (!orderItemRepository.findByMenuId(id).isEmpty()) {
            throw new IllegalArgumentException("Không thể xóa món ăn này vì vẫn còn trong đơn hàng.");
        }

        menuRepository.delete(menu);
    }

    @Override
    public long getTotal() {
        return menuRepository.count();
    }

    @Override
    public Menu getById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thực đơn"));
    }

    @Override
    public int getPage(Long menuId) {
        int pageSize = 10;
        List<Menu> sortedMenus = menuRepository.findAll();
        int index = sortedMenus.indexOf(menuRepository.findById(menuId).orElse(null));
        return index / pageSize;
    }

    @Override
    public List<Menu> findByRestaurantId(Long restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public Page<Menu> findWithFilters(String keyword, List<Long> categoryIds, List<Long> restaurantIds, Double minPrice, Double maxPrice, Pageable pageable) {
        return menuRepository.findWithFilters(keyword, categoryIds, restaurantIds, minPrice, maxPrice, pageable);
    }


    @Override
    public Page<Menu> findWithPaginationAndKeywordAndRestaurant(String keyword, Long categoryId, Long restaurantId, Pageable pageable) {
        return menuRepository.searchMenus(keyword, categoryId, restaurantId, pageable);
    }

    @Override
    public Page<Menu> findAllMenusByMerchantId(Long merchantId, String keyword, Long restaurantId, Pageable pageable) {
        return menuRepository.findAllMenusByMerchantId(merchantId, restaurantId, keyword, pageable);
    }

}
