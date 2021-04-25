package com.payfix.packages.service.impl;

import com.payfix.packages.domain.UserMemberShip;
import com.payfix.packages.repository.UserMemberShipRepository;
import com.payfix.packages.service.UserMemberShipService;
import com.payfix.packages.service.dto.UserMemberShipDTO;
import com.payfix.packages.service.mapper.UserMemberShipMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link UserMemberShip}.
 */
@Service
@Transactional
public class UserMemberShipServiceImpl implements UserMemberShipService {

    private final Logger log = LoggerFactory.getLogger(UserMemberShipServiceImpl.class);

    private final UserMemberShipRepository userMemberShipRepository;

    private final UserMemberShipMapper userMemberShipMapper;

    public UserMemberShipServiceImpl(UserMemberShipRepository userMemberShipRepository, UserMemberShipMapper userMemberShipMapper) {
        this.userMemberShipRepository = userMemberShipRepository;
        this.userMemberShipMapper = userMemberShipMapper;
    }

    @Override
    public UserMemberShipDTO save(UserMemberShipDTO userMemberShipDTO) {
        log.debug("Request to save UserMemberShip : {}", userMemberShipDTO);
        UserMemberShip userMemberShip = userMemberShipMapper.toEntity(userMemberShipDTO);
        userMemberShip = userMemberShipRepository.save(userMemberShip);
        return userMemberShipMapper.toDto(userMemberShip);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserMemberShipDTO> findAll() {
        log.debug("Request to get all UserMemberShips");
        return userMemberShipRepository
            .findAll()
            .stream()
            .map(userMemberShipMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserMemberShipDTO> findOne(Long id) {
        log.debug("Request to get UserMemberShip : {}", id);
        return userMemberShipRepository.findById(id).map(userMemberShipMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserMemberShip : {}", id);
        userMemberShipRepository.deleteById(id);
    }
}
