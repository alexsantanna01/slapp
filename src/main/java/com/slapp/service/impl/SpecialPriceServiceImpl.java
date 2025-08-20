package com.slapp.service.impl;

import com.slapp.domain.SpecialPrice;
import com.slapp.repository.SpecialPriceRepository;
import com.slapp.service.SpecialPriceService;
import com.slapp.service.dto.SpecialPriceDTO;
import com.slapp.service.mapper.SpecialPriceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.slapp.domain.SpecialPrice}.
 */
@Service
@Transactional
public class SpecialPriceServiceImpl implements SpecialPriceService {

    private static final Logger LOG = LoggerFactory.getLogger(SpecialPriceServiceImpl.class);

    private final SpecialPriceRepository specialPriceRepository;

    private final SpecialPriceMapper specialPriceMapper;

    public SpecialPriceServiceImpl(SpecialPriceRepository specialPriceRepository, SpecialPriceMapper specialPriceMapper) {
        this.specialPriceRepository = specialPriceRepository;
        this.specialPriceMapper = specialPriceMapper;
    }

    @Override
    public SpecialPriceDTO save(SpecialPriceDTO specialPriceDTO) {
        LOG.debug("Request to save SpecialPrice : {}", specialPriceDTO);
        SpecialPrice specialPrice = specialPriceMapper.toEntity(specialPriceDTO);
        specialPrice = specialPriceRepository.save(specialPrice);
        return specialPriceMapper.toDto(specialPrice);
    }

    @Override
    public SpecialPriceDTO update(SpecialPriceDTO specialPriceDTO) {
        LOG.debug("Request to update SpecialPrice : {}", specialPriceDTO);
        SpecialPrice specialPrice = specialPriceMapper.toEntity(specialPriceDTO);
        specialPrice = specialPriceRepository.save(specialPrice);
        return specialPriceMapper.toDto(specialPrice);
    }

    @Override
    public Optional<SpecialPriceDTO> partialUpdate(SpecialPriceDTO specialPriceDTO) {
        LOG.debug("Request to partially update SpecialPrice : {}", specialPriceDTO);

        return specialPriceRepository
            .findById(specialPriceDTO.getId())
            .map(existingSpecialPrice -> {
                specialPriceMapper.partialUpdate(existingSpecialPrice, specialPriceDTO);

                return existingSpecialPrice;
            })
            .map(specialPriceRepository::save)
            .map(specialPriceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialPriceDTO> findAll() {
        LOG.debug("Request to get all SpecialPrices");
        return specialPriceRepository.findAll().stream().map(specialPriceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpecialPriceDTO> findOne(Long id) {
        LOG.debug("Request to get SpecialPrice : {}", id);
        return specialPriceRepository.findById(id).map(specialPriceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SpecialPrice : {}", id);
        specialPriceRepository.deleteById(id);
    }
}
