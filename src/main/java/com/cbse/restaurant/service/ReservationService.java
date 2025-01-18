package com.cbse.restaurant.service;

import com.cbse.restaurant.domain.Reservation;
import com.cbse.restaurant.repository.ReservationRepository;
import com.cbse.restaurant.service.dto.ReservationDTO;
import com.cbse.restaurant.service.mapper.ReservationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cbse.restaurant.domain.Reservation}.
 */
@Service
@Transactional
public class ReservationService {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    /**
     * Save a reservation.
     *
     * @param reservationDTO the entity to save.
     * @return the persisted entity.
     */
    public ReservationDTO save(ReservationDTO reservationDTO) {
        LOG.debug("Request to save Reservation : {}", reservationDTO);
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(reservation);
    }

    /**
     * Update a reservation.
     *
     * @param reservationDTO the entity to save.
     * @return the persisted entity.
     */
    public ReservationDTO update(ReservationDTO reservationDTO) {
        LOG.debug("Request to update Reservation : {}", reservationDTO);
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(reservation);
    }

    /**
     * Partially update a reservation.
     *
     * @param reservationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReservationDTO> partialUpdate(ReservationDTO reservationDTO) {
        LOG.debug("Request to partially update Reservation : {}", reservationDTO);

        return reservationRepository
            .findById(reservationDTO.getId())
            .map(existingReservation -> {
                reservationMapper.partialUpdate(existingReservation, reservationDTO);

                return existingReservation;
            })
            .map(reservationRepository::save)
            .map(reservationMapper::toDto);
    }

    /**
     * Get all the reservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReservationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Reservations");
        return reservationRepository.findAll(pageable).map(reservationMapper::toDto);
    }

    /**
     * Get one reservation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReservationDTO> findOne(Long id) {
        LOG.debug("Request to get Reservation : {}", id);
        return reservationRepository.findById(id).map(reservationMapper::toDto);
    }

    /**
     * Delete the reservation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Reservation : {}", id);
        reservationRepository.deleteById(id);
    }
}
