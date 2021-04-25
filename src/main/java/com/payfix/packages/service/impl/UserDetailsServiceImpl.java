package com.payfix.packages.service.impl;

import com.payfix.packages.domain.UserDetails;
import com.payfix.packages.repository.UserDetailsRepository;
import com.payfix.packages.repository.UserRepository;
import com.payfix.packages.service.UserDetailsService;
import com.payfix.packages.service.dto.UserDetailsDTO;
import com.payfix.packages.service.mapper.UserDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserDetails}.
 */
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserDetailsRepository userDetailsRepository;

    private final UserDetailsMapper userDetailsMapper;

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(
        UserDetailsRepository userDetailsRepository,
        UserDetailsMapper userDetailsMapper,
        UserRepository userRepository
    ) {
        this.userDetailsRepository = userDetailsRepository;
        this.userDetailsMapper = userDetailsMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsDTO save(UserDetailsDTO userDetailsDTO) {
        log.debug("Request to save UserDetails : {}", userDetailsDTO);
        UserDetails userDetails = userDetailsMapper.toEntity(userDetailsDTO);
        Long userId = userDetailsDTO.getUserId();
        userRepository.findById(userId).ifPresent(userDetails::user);
        userDetails = userDetailsRepository.save(userDetails);
        return userDetailsMapper.toDto(userDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserDetails");
        return userDetailsRepository.findAll(pageable).map(userDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDetailsDTO> findOne(Long id) {
        log.debug("Request to get UserDetails : {}", id);
        return userDetailsRepository.findById(id).map(userDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserDetails : {}", id);
        userDetailsRepository.deleteById(id);
    }
}
