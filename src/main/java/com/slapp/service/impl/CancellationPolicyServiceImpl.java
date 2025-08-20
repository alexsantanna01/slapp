package com.slapp.service.impl;

import com.slapp.domain.CancellationPolicy;
import com.slapp.repository.CancellationPolicyRepository;
import com.slapp.service.CancellationPolicyService;
import com.slapp.service.dto.CancellationPolicyDTO;
import com.slapp.service.mapper.CancellationPolicyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.slapp.domain.CancellationPolicy}.
 */
@Service
@Transactional
public class CancellationPolicyServiceImpl implements CancellationPolicyService {

    private static final Logger LOG = LoggerFactory.getLogger(CancellationPolicyServiceImpl.class);

    private final CancellationPolicyRepository cancellationPolicyRepository;

    private final CancellationPolicyMapper cancellationPolicyMapper;

    public CancellationPolicyServiceImpl(
        CancellationPolicyRepository cancellationPolicyRepository,
        CancellationPolicyMapper cancellationPolicyMapper
    ) {
        this.cancellationPolicyRepository = cancellationPolicyRepository;
        this.cancellationPolicyMapper = cancellationPolicyMapper;
    }

    @Override
    public CancellationPolicyDTO save(CancellationPolicyDTO cancellationPolicyDTO) {
        LOG.debug("Request to save CancellationPolicy : {}", cancellationPolicyDTO);
        CancellationPolicy cancellationPolicy = cancellationPolicyMapper.toEntity(cancellationPolicyDTO);
        cancellationPolicy = cancellationPolicyRepository.save(cancellationPolicy);
        return cancellationPolicyMapper.toDto(cancellationPolicy);
    }

    @Override
    public CancellationPolicyDTO update(CancellationPolicyDTO cancellationPolicyDTO) {
        LOG.debug("Request to update CancellationPolicy : {}", cancellationPolicyDTO);
        CancellationPolicy cancellationPolicy = cancellationPolicyMapper.toEntity(cancellationPolicyDTO);
        cancellationPolicy = cancellationPolicyRepository.save(cancellationPolicy);
        return cancellationPolicyMapper.toDto(cancellationPolicy);
    }

    @Override
    public Optional<CancellationPolicyDTO> partialUpdate(CancellationPolicyDTO cancellationPolicyDTO) {
        LOG.debug("Request to partially update CancellationPolicy : {}", cancellationPolicyDTO);

        return cancellationPolicyRepository
            .findById(cancellationPolicyDTO.getId())
            .map(existingCancellationPolicy -> {
                cancellationPolicyMapper.partialUpdate(existingCancellationPolicy, cancellationPolicyDTO);

                return existingCancellationPolicy;
            })
            .map(cancellationPolicyRepository::save)
            .map(cancellationPolicyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancellationPolicyDTO> findAll() {
        LOG.debug("Request to get all CancellationPolicies");
        return cancellationPolicyRepository
            .findAll()
            .stream()
            .map(cancellationPolicyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CancellationPolicyDTO> findOne(Long id) {
        LOG.debug("Request to get CancellationPolicy : {}", id);
        return cancellationPolicyRepository.findById(id).map(cancellationPolicyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CancellationPolicy : {}", id);
        cancellationPolicyRepository.deleteById(id);
    }
}
