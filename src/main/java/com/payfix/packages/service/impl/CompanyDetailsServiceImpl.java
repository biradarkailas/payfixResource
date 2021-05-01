package com.payfix.packages.service.impl;

import com.payfix.packages.domain.CompanyDetails;
import com.payfix.packages.repository.CompanyDetailsRepository;
import com.payfix.packages.repository.UserRepository;
import com.payfix.packages.service.CompanyDetailsService;
import com.payfix.packages.service.dto.CompanyDetailsDTO;
import com.payfix.packages.service.mapper.CompanyDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CompanyDetails}.
 */
@Service
@Transactional
public class CompanyDetailsServiceImpl implements CompanyDetailsService {

    private final Logger log = LoggerFactory.getLogger(CompanyDetailsServiceImpl.class);

    private final CompanyDetailsRepository companyDetailsRepository;

    private final CompanyDetailsMapper companyDetailsMapper;

    private final UserRepository userRepository;

    public CompanyDetailsServiceImpl(
        CompanyDetailsRepository companyDetailsRepository,
        CompanyDetailsMapper companyDetailsMapper,
        UserRepository userRepository
    ) {
        this.companyDetailsRepository = companyDetailsRepository;
        this.companyDetailsMapper = companyDetailsMapper;
        this.userRepository = userRepository;
    }

    @Override
    public CompanyDetailsDTO save(CompanyDetailsDTO companyDetailsDTO) {
        log.debug("Request to save CompanyDetails : {}", companyDetailsDTO);
        CompanyDetails companyDetails = companyDetailsMapper.toEntity(companyDetailsDTO);
        Long userId = companyDetailsDTO.getUserId();
        userRepository.findById(userId).ifPresent(companyDetails::user);
        companyDetails = companyDetailsRepository.save(companyDetails);
        return companyDetailsMapper.toDto(companyDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyDetails");
        return companyDetailsRepository.findAll(pageable).map(companyDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyDetailsDTO> findOne(Long id) {
        log.debug("Request to get CompanyDetails : {}", id);
        return companyDetailsRepository.findById(id).map(companyDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompanyDetails : {}", id);
        companyDetailsRepository.deleteById(id);
    }
}
