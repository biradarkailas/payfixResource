package com.payfix.packages.service.impl;

import com.payfix.packages.domain.DurationUnit;
import com.payfix.packages.repository.DurationUnitRepository;
import com.payfix.packages.service.DurationUnitService;
import com.payfix.packages.service.dto.DurationUnitDTO;
import com.payfix.packages.service.mapper.DurationUnitMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DurationUnit}.
 */
@Service
@Transactional
public class DurationUnitServiceImpl implements DurationUnitService {

    private final Logger log = LoggerFactory.getLogger(DurationUnitServiceImpl.class);

    private final DurationUnitRepository durationUnitRepository;

    private final DurationUnitMapper durationUnitMapper;

    public DurationUnitServiceImpl(DurationUnitRepository durationUnitRepository, DurationUnitMapper durationUnitMapper) {
        this.durationUnitRepository = durationUnitRepository;
        this.durationUnitMapper = durationUnitMapper;
    }

    @Override
    public DurationUnitDTO save(DurationUnitDTO durationUnitDTO) {
        log.debug("Request to save DurationUnit : {}", durationUnitDTO);
        DurationUnit durationUnit = durationUnitMapper.toEntity(durationUnitDTO);
        durationUnit = durationUnitRepository.save(durationUnit);
        return durationUnitMapper.toDto(durationUnit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DurationUnitDTO> findAll() {
        log.debug("Request to get all DurationUnits");
        return durationUnitRepository.findAll().stream().map(durationUnitMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DurationUnitDTO> findOne(Long id) {
        log.debug("Request to get DurationUnit : {}", id);
        return durationUnitRepository.findById(id).map(durationUnitMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DurationUnit : {}", id);
        durationUnitRepository.deleteById(id);
    }
}
