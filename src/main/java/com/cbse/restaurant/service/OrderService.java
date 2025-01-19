package com.cbse.restaurant.service;

import com.cbse.restaurant.service.dto.OrderDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    OrderDTO save(OrderDTO orderDTO);

    OrderDTO update(OrderDTO orderDTO);

    Optional<OrderDTO> partialUpdate(OrderDTO orderDTO);

    @Transactional(readOnly = true)
    Page<OrderDTO> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<OrderDTO> findOne(Long id);

    void delete(Long id);
}
