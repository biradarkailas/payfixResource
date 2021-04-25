package com.payfix.packages.service.impl;

import com.payfix.packages.domain.WithdrawStatus;
import com.payfix.packages.repository.WithdrawStatusRepository;
import com.payfix.packages.service.WithdrawStatusService;
import com.payfix.packages.service.dto.WithdrawStatusDTO;
import com.payfix.packages.service.mapper.WithdrawStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WithdrawStatus}.
 */
@Service
@Transactional
public class WithdrawStatusServiceImpl implements WithdrawStatusService {

    private final Logger log = LoggerFactory.getLogger(WithdrawStatusServiceImpl.class);

    private final WithdrawStatusRepository withdrawStatusRepository;

    private final WithdrawStatusMapper withdrawStatusMapper;

    public WithdrawStatusServiceImpl(WithdrawStatusRepository withdrawStatusRepository, WithdrawStatusMapper withdrawStatusMapper) {
        this.withdrawStatusRepository = withdrawStatusRepository;
        this.withdrawStatusMapper = withdrawStatusMapper;
    }

    @Override
    public WithdrawStatusDTO save(WithdrawStatusDTO withdrawStatusDTO) {
        log.debug("Request to save WithdrawStatus : {}", withdrawStatusDTO);
        WithdrawStatus withdrawStatus = withdrawStatusMapper.toEntity(withdrawStatusDTO);
        withdrawStatus = withdrawStatusRepository.save(withdrawStatus);
        return withdrawStatusMapper.toDto(withdrawStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WithdrawStatusDTO> findAll() {
        log.debug("Request to get all WithdrawStatuses");
        return withdrawStatusRepository
            .findAll()
            .stream()
            .map(withdrawStatusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WithdrawStatusDTO> findOne(Long id) {
        log.debug("Request to get WithdrawStatus : {}", id);
        return withdrawStatusRepository.findById(id).map(withdrawStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WithdrawStatus : {}", id);
        withdrawStatusRepository.deleteById(id);
    }
}
