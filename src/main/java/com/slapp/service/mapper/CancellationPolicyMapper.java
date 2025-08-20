package com.slapp.service.mapper;

import com.slapp.domain.CancellationPolicy;
import com.slapp.service.dto.CancellationPolicyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CancellationPolicy} and its DTO {@link CancellationPolicyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CancellationPolicyMapper extends EntityMapper<CancellationPolicyDTO, CancellationPolicy> {}
