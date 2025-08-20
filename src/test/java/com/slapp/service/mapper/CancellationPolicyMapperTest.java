package com.slapp.service.mapper;

import static com.slapp.domain.CancellationPolicyAsserts.*;
import static com.slapp.domain.CancellationPolicyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CancellationPolicyMapperTest {

    private CancellationPolicyMapper cancellationPolicyMapper;

    @BeforeEach
    void setUp() {
        cancellationPolicyMapper = new CancellationPolicyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCancellationPolicySample1();
        var actual = cancellationPolicyMapper.toEntity(cancellationPolicyMapper.toDto(expected));
        assertCancellationPolicyAllPropertiesEquals(expected, actual);
    }
}
