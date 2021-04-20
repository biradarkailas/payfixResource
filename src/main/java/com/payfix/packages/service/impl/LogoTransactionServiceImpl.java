package com.payfix.packages.service.impl;

import com.payfix.packages.domain.LogoTransaction;
import com.payfix.packages.repository.LogoTransactionRepository;
import com.payfix.packages.service.LogoTransactionService;
import com.payfix.packages.service.dto.LogoTransactionDTO;
import com.payfix.packages.service.mapper.LogoTransactionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LogoTransaction}.
 */
@Service
@Transactional
public class LogoTransactionServiceImpl implements LogoTransactionService {

    private final Logger log = LoggerFactory.getLogger(LogoTransactionServiceImpl.class);

    private final LogoTransactionRepository logoTransactionRepository;

    private final LogoTransactionMapper logoTransactionMapper;

    public LogoTransactionServiceImpl(LogoTransactionRepository logoTransactionRepository, LogoTransactionMapper logoTransactionMapper) {
        this.logoTransactionRepository = logoTransactionRepository;
        this.logoTransactionMapper = logoTransactionMapper;
    }

    @Override
    public LogoTransactionDTO save(LogoTransactionDTO logoTransactionDTO) {
        log.debug("Request to save LogoTransaction : {}", logoTransactionDTO);
        LogoTransaction logoTransaction = logoTransactionMapper.toEntity(logoTransactionDTO);
        logoTransaction = logoTransactionRepository.save(logoTransaction);
        return logoTransactionMapper.toDto(logoTransaction);
    }

    @Override
    public Optional<LogoTransactionDTO> partialUpdate(LogoTransactionDTO logoTransactionDTO) {
        log.debug("Request to partially update LogoTransaction : {}", logoTransactionDTO);

        return logoTransactionRepository
            .findById(logoTransactionDTO.getId())
            .map(
                existingLogoTransaction -> {
                    logoTransactionMapper.partialUpdate(existingLogoTransaction, logoTransactionDTO);
                    return existingLogoTransaction;
                }
            )
            .map(logoTransactionRepository::save)
            .map(logoTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogoTransactionDTO> findAll() {
        log.debug("Request to get all LogoTransactions");
        return logoTransactionRepository
            .findAll()
            .stream()
            .map(logoTransactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LogoTransactionDTO> findOne(Long id) {
        log.debug("Request to get LogoTransaction : {}", id);
        return logoTransactionRepository.findById(id).map(logoTransactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LogoTransaction : {}", id);
        logoTransactionRepository.deleteById(id);
    }
}
