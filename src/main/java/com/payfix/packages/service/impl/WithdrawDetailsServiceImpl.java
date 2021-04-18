package com.payfix.packages.service.impl;

import com.payfix.packages.domain.WithdrawDetails;
import com.payfix.packages.repository.WithdrawDetailsRepository;
import com.payfix.packages.service.WithdrawDetailsService;
import com.payfix.packages.service.dto.WithdrawDetailsDTO;
import com.payfix.packages.service.mapper.WithdrawDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WithdrawDetails}.
 */
@Service
@Transactional
public class WithdrawDetailsServiceImpl implements WithdrawDetailsService {

    private final Logger log = LoggerFactory.getLogger(WithdrawDetailsServiceImpl.class);

    private final WithdrawDetailsRepository withdrawDetailsRepository;

    private final WithdrawDetailsMapper withdrawDetailsMapper;

    public WithdrawDetailsServiceImpl(WithdrawDetailsRepository withdrawDetailsRepository, WithdrawDetailsMapper withdrawDetailsMapper) {
        this.withdrawDetailsRepository = withdrawDetailsRepository;
        this.withdrawDetailsMapper = withdrawDetailsMapper;
    }

    @Override
    public WithdrawDetailsDTO save(WithdrawDetailsDTO withdrawDetailsDTO) {
        log.debug("Request to save WithdrawDetails : {}", withdrawDetailsDTO);
        WithdrawDetails withdrawDetails = withdrawDetailsMapper.toEntity(withdrawDetailsDTO);
        withdrawDetails = withdrawDetailsRepository.save(withdrawDetails);
        return withdrawDetailsMapper.toDto(withdrawDetails);
    }

    @Override
    public Optional<WithdrawDetailsDTO> partialUpdate(WithdrawDetailsDTO withdrawDetailsDTO) {
        log.debug("Request to partially update WithdrawDetails : {}", withdrawDetailsDTO);

        return withdrawDetailsRepository
            .findById(withdrawDetailsDTO.getId())
            .map(
                existingWithdrawDetails -> {
                    withdrawDetailsMapper.partialUpdate(existingWithdrawDetails, withdrawDetailsDTO);
                    return existingWithdrawDetails;
                }
            )
            .map(withdrawDetailsRepository::save)
            .map(withdrawDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WithdrawDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WithdrawDetails");
        return withdrawDetailsRepository.findAll(pageable).map(withdrawDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WithdrawDetailsDTO> findOne(Long id) {
        log.debug("Request to get WithdrawDetails : {}", id);
        return withdrawDetailsRepository.findById(id).map(withdrawDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WithdrawDetails : {}", id);
        withdrawDetailsRepository.deleteById(id);
    }
}
