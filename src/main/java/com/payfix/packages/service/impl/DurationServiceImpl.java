package com.payfix.packages.service.impl;

import com.payfix.packages.domain.Duration;
import com.payfix.packages.repository.DurationRepository;
import com.payfix.packages.service.DurationService;
import com.payfix.packages.service.dto.DurationDTO;
import com.payfix.packages.service.mapper.DurationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Duration}.
 */
@Service
@Transactional
public class DurationServiceImpl implements DurationService {

    private final Logger log = LoggerFactory.getLogger(DurationServiceImpl.class);

    private final DurationRepository durationRepository;

    private final DurationMapper durationMapper;

    public DurationServiceImpl(DurationRepository durationRepository, DurationMapper durationMapper) {
        this.durationRepository = durationRepository;
        this.durationMapper = durationMapper;
    }

    @Override
    public DurationDTO save(DurationDTO durationDTO) {
        log.debug("Request to save Duration : {}", durationDTO);
        Duration duration = durationMapper.toEntity(durationDTO);
        duration = durationRepository.save(duration);
        return durationMapper.toDto(duration);
    }
    @Override
    @Transactional(readOnly = true)
    public List<DurationDTO> findAll() {
        log.debug("Request to get all Durations");
        return durationRepository.findAll().stream().map(durationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DurationDTO> findOne(Long id) {
        log.debug("Request to get Duration : {}", id);
        return durationRepository.findById(id).map(durationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Duration : {}", id);
        durationRepository.deleteById(id);
    }
}
