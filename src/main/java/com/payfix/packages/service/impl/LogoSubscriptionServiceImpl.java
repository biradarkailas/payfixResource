package com.payfix.packages.service.impl;

import com.payfix.packages.domain.LogoSubscription;
import com.payfix.packages.repository.LogoSubscriptionRepository;
import com.payfix.packages.service.LogoSubscriptionService;
import com.payfix.packages.service.dto.LogoSubscriptionDTO;
import com.payfix.packages.service.mapper.LogoSubscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link LogoSubscription}.
 */
@Service
@Transactional
public class LogoSubscriptionServiceImpl implements LogoSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(LogoSubscriptionServiceImpl.class);

    private final LogoSubscriptionRepository logoSubscriptionRepository;

    private final LogoSubscriptionMapper logoSubscriptionMapper;

    public LogoSubscriptionServiceImpl(
        LogoSubscriptionRepository logoSubscriptionRepository,
        LogoSubscriptionMapper logoSubscriptionMapper
    ) {
        this.logoSubscriptionRepository = logoSubscriptionRepository;
        this.logoSubscriptionMapper = logoSubscriptionMapper;
    }

    @Override
    public LogoSubscriptionDTO save(LogoSubscriptionDTO logoSubscriptionDTO) {
        log.debug("Request to save LogoSubscription : {}", logoSubscriptionDTO);
        LogoSubscription logoSubscription = logoSubscriptionMapper.toEntity(logoSubscriptionDTO);
        logoSubscription = logoSubscriptionRepository.save(logoSubscription);
        return logoSubscriptionMapper.toDto(logoSubscription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogoSubscriptionDTO> findAll() {
        log.debug("Request to get all LogoSubscriptions");
        return logoSubscriptionRepository
            .findAll()
            .stream()
            .map(logoSubscriptionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LogoSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get LogoSubscription : {}", id);
        return logoSubscriptionRepository.findById(id).map(logoSubscriptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LogoSubscription : {}", id);
        logoSubscriptionRepository.deleteById(id);
    }
}
