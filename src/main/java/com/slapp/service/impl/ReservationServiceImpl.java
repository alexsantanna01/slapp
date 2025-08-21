package com.slapp.service.impl;

import com.slapp.domain.Reservation;
import com.slapp.domain.enumeration.ReservationStatus;
import com.slapp.repository.ReservationRepository;
import com.slapp.service.ReservationService;
import com.slapp.service.dto.ReservationDTO;
import com.slapp.service.mapper.ReservationMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> findReservationsByRoomAndDate(Long roomId, String date) {
        LOG.debug("Request to get reservations for room: {} on date: {}", roomId, date);
        try {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            Instant startOfDay = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant endOfDay = localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

            List<ReservationStatus> activeStatuses = Arrays.asList(
                ReservationStatus.CONFIRMED,
                ReservationStatus.PENDING,
                ReservationStatus.IN_PROGRESS
            );

            List<Reservation> reservations = reservationRepository.findReservationsByRoomAndDate(
                roomId,
                startOfDay,
                endOfDay,
                activeStatuses
            );

            LOG.debug("Found {} reservations for room {} on date {}", reservations.size(), roomId, date);

            return reservations.stream().map(reservationMapper::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("Error parsing date: {}", date, e);
            return List.of();
        }
    }

    @Override
    @Transactional
    public Optional<ReservationDTO> approveReservation(Long id) {
        LOG.debug("Request to approve Reservation : {}", id);
        return reservationRepository
            .findById(id)
            .filter(reservation -> ReservationStatus.PENDING.equals(reservation.getStatus()))
            .map(reservation -> {
                reservation.setStatus(ReservationStatus.CONFIRMED);
                reservation.setUpdatedAt(Instant.now());
                return reservationRepository.save(reservation);
            })
            .map(reservationMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<ReservationDTO> rejectReservation(Long id, String reason) {
        LOG.debug("Request to reject Reservation : {} with reason: {}", id, reason);
        return reservationRepository
            .findById(id)
            .filter(reservation -> ReservationStatus.PENDING.equals(reservation.getStatus()))
            .map(reservation -> {
                reservation.setStatus(ReservationStatus.CANCELLED);
                reservation.setCancelledAt(Instant.now());
                reservation.setCancelReason(reason != null ? reason : "Reprovada pelo proprietário");
                reservation.setUpdatedAt(Instant.now());
                return reservationRepository.save(reservation);
            })
            .map(reservationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> findPendingReservationsByStudio(Long studioId) {
        LOG.debug("Request to get pending reservations for studio: {}", studioId);
        List<Reservation> pendingReservations = reservationRepository.findPendingReservationsByStudio(studioId);
        return pendingReservations.stream().map(reservationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int autoConfirmExpiredPendingReservations() {
        LOG.debug("Request to auto-confirm expired pending reservations");
        Instant thirtyMinutesAgo = Instant.now().minusSeconds(30 * 60);

        List<Reservation> expiredPendingReservations = reservationRepository.findExpiredPendingReservations(thirtyMinutesAgo);

        int confirmedCount = 0;
        for (Reservation reservation : expiredPendingReservations) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservation.setUpdatedAt(Instant.now());
            reservationRepository.save(reservation);
            confirmedCount++;
            LOG.info("Auto-confirmed reservation {} after 30 minutes", reservation.getId());
        }

        return confirmedCount;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> findAllReservationsByRoom(Long roomId) {
        LOG.debug("Request to get all reservations for room: {}", roomId);
        List<Reservation> reservations = reservationRepository.findAllByRoomId(roomId);
        return reservations.stream().map(reservationMapper::toDto).collect(Collectors.toList());
    }
}
