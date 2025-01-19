package com.cbse.restaurant.service.Impl;

import com.cbse.restaurant.service.dto.ReservationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationService {
    ReservationDTO save(ReservationDTO reservationDTO);

    ReservationDTO update(ReservationDTO reservationDTO);

    Optional<ReservationDTO> partialUpdate(ReservationDTO reservationDTO);

    @Transactional(readOnly = true)
    Page<ReservationDTO> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<ReservationDTO> findOne(Long id);

    void delete(Long id);
}
