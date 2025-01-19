package com.cbse.restaurant.service;

import com.cbse.restaurant.service.dto.MenuItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface MenuItemService {
    MenuItemDTO save(MenuItemDTO menuItemDTO);

    MenuItemDTO update(MenuItemDTO menuItemDTO);

    Optional<MenuItemDTO> partialUpdate(MenuItemDTO menuItemDTO);

    @Transactional(readOnly = true)
    Page<MenuItemDTO> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<MenuItemDTO> findOne(Long id);

    void delete(Long id);
}
