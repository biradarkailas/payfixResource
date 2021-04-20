package com.payfix.packages.service.impl;

import com.payfix.packages.domain.MemberShip;
import com.payfix.packages.repository.MemberShipRepository;
import com.payfix.packages.service.MemberShipService;
import com.payfix.packages.service.dto.MemberShipDTO;
import com.payfix.packages.service.mapper.MemberShipMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MemberShip}.
 */
@Service
@Transactional
public class MemberShipServiceImpl implements MemberShipService {

    private final Logger log = LoggerFactory.getLogger(MemberShipServiceImpl.class);

    private final MemberShipRepository memberShipRepository;

    private final MemberShipMapper memberShipMapper;

    public MemberShipServiceImpl(MemberShipRepository memberShipRepository, MemberShipMapper memberShipMapper) {
        this.memberShipRepository = memberShipRepository;
        this.memberShipMapper = memberShipMapper;
    }

    @Override
    public MemberShipDTO save(MemberShipDTO memberShipDTO) {
        log.debug("Request to save MemberShip : {}", memberShipDTO);
        MemberShip memberShip = memberShipMapper.toEntity(memberShipDTO);
        memberShip = memberShipRepository.save(memberShip);
        return memberShipMapper.toDto(memberShip);
    }

    @Override
    public Optional<MemberShipDTO> partialUpdate(MemberShipDTO memberShipDTO) {
        log.debug("Request to partially update MemberShip : {}", memberShipDTO);

        return memberShipRepository
            .findById(memberShipDTO.getId())
            .map(
                existingMemberShip -> {
                    memberShipMapper.partialUpdate(existingMemberShip, memberShipDTO);
                    return existingMemberShip;
                }
            )
            .map(memberShipRepository::save)
            .map(memberShipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberShipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MemberShips");
        return memberShipRepository.findAll(pageable).map(memberShipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberShipDTO> findOne(Long id) {
        log.debug("Request to get MemberShip : {}", id);
        return memberShipRepository.findById(id).map(memberShipMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MemberShip : {}", id);
        memberShipRepository.deleteById(id);
    }
}
