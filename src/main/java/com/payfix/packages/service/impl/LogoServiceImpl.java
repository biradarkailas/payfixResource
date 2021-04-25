package com.payfix.packages.service.impl;

import com.payfix.packages.domain.Logo;
import com.payfix.packages.repository.LogoRepository;
import com.payfix.packages.service.LogoService;
import com.payfix.packages.service.dto.LogoDTO;
import com.payfix.packages.service.mapper.LogoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Logo}.
 */
@Service
@Transactional
public class LogoServiceImpl implements LogoService {

    private final Logger log = LoggerFactory.getLogger(LogoServiceImpl.class);

    private final LogoRepository logoRepository;

    private final LogoMapper logoMapper;

    public LogoServiceImpl(LogoRepository logoRepository, LogoMapper logoMapper) {
        this.logoRepository = logoRepository;
        this.logoMapper = logoMapper;
    }

    @Override
    public LogoDTO save(LogoDTO logoDTO) {
        log.debug("Request to save Logo : {}", logoDTO);
        Logo logo = logoMapper.toEntity(logoDTO);
        logo = logoRepository.save(logo);
        return logoMapper.toDto(logo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LogoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Logos");
        return logoRepository.findAll(pageable).map(logoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LogoDTO> findOne(Long id) {
        log.debug("Request to get Logo : {}", id);
        return logoRepository.findById(id).map(logoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Logo : {}", id);
        logoRepository.deleteById(id);
    }
}
