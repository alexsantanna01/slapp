package com.slapp.service.impl;

import com.slapp.domain.Reservation;
import com.slapp.repository.ReservationRepository;
import com.slapp.service.ReservationService;
import com.slapp.service.dto.ReservationDTO;
import com.slapp.service.mapper.ReservationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.slapp.domain.Reservation}.
 */
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public ReservationDTO save(ReservationDTO reservationDTO) {
        LOG.debug("Request to save Reservation : {}", reservationDTO);
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(reservation);
    }

    @Override
    public ReservationDTO update(ReservationDTO reservationDTO) {
        LOG.debug("Request to update Reservation : {}", reservationDTO);
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(reservation);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public Optional<ReservationDTO> findOne(Long id) {
        LOG.debug("Request to get Reservation : {}", id);
        return reservationRepository.findById(id).map(reservationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Reservation : {}", id);
        reservationRepository.deleteById(id);
    }
}
