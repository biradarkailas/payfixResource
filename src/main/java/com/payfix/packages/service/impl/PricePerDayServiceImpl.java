package com.payfix.packages.service.impl;

import com.payfix.packages.domain.PricePerDay;
import com.payfix.packages.repository.PricePerDayRepository;
import com.payfix.packages.service.PricePerDayService;
import com.payfix.packages.service.dto.PricePerDayDTO;
import com.payfix.packages.service.mapper.PricePerDayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PricePerDay}.
 */
@Service
@Transactional
public class PricePerDayServiceImpl implements PricePerDayService {

    private final Logger log = LoggerFactory.getLogger(PricePerDayServiceImpl.class);

    private final PricePerDayRepository pricePerDayRepository;

    private final PricePerDayMapper pricePerDayMapper;

    public PricePerDayServiceImpl(PricePerDayRepository pricePerDayRepository, PricePerDayMapper pricePerDayMapper) {
        this.pricePerDayRepository = pricePerDayRepository;
        this.pricePerDayMapper = pricePerDayMapper;
    }

    @Override
    public PricePerDayDTO save(PricePerDayDTO pricePerDayDTO) {
        log.debug("Request to save PricePerDay : {}", pricePerDayDTO);
        PricePerDay pricePerDay = pricePerDayMapper.toEntity(pricePerDayDTO);
        pricePerDay = pricePerDayRepository.save(pricePerDay);
        return pricePerDayMapper.toDto(pricePerDay);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PricePerDayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PricePerDays");
        return pricePerDayRepository.findAll(pageable).map(pricePerDayMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PricePerDayDTO> findOne(Long id) {
        log.debug("Request to get PricePerDay : {}", id);
        return pricePerDayRepository.findById(id).map(pricePerDayMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PricePerDay : {}", id);
        pricePerDayRepository.deleteById(id);
    }
}
