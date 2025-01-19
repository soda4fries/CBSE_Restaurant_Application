package com.cbse.restaurant.service;

import com.cbse.restaurant.service.dto.CustomerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerService {
    CustomerDTO save(CustomerDTO customerDTO);

    CustomerDTO update(CustomerDTO customerDTO);

    Optional<CustomerDTO> partialUpdate(CustomerDTO customerDTO);

    @Transactional(readOnly = true)
    Page<CustomerDTO> findAll(Pageable pageable);

    Page<CustomerDTO> findAllWithEagerRelationships(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<CustomerDTO> findOne(Long id);

    void delete(Long id);
}
