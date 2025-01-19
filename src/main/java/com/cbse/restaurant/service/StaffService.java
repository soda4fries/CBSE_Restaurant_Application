package com.cbse.restaurant.service;

import com.cbse.restaurant.service.dto.StaffDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface StaffService {
    StaffDTO save(StaffDTO staffDTO);

    StaffDTO update(StaffDTO staffDTO);

    Optional<StaffDTO> partialUpdate(StaffDTO staffDTO);

    @Transactional(readOnly = true)
    Page<StaffDTO> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<StaffDTO> findOne(Long id);

    void delete(Long id);
}
