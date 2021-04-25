package com.payfix.packages.service.impl;

import com.payfix.packages.domain.TransactionType;
import com.payfix.packages.repository.TransactionTypeRepository;
import com.payfix.packages.service.TransactionTypeService;
import com.payfix.packages.service.dto.TransactionTypeDTO;
import com.payfix.packages.service.mapper.TransactionTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransactionType}.
 */
@Service
@Transactional
public class TransactionTypeServiceImpl implements TransactionTypeService {

    private final Logger log = LoggerFactory.getLogger(TransactionTypeServiceImpl.class);

    private final TransactionTypeRepository transactionTypeRepository;

    private final TransactionTypeMapper transactionTypeMapper;

    public TransactionTypeServiceImpl(TransactionTypeRepository transactionTypeRepository, TransactionTypeMapper transactionTypeMapper) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionTypeMapper = transactionTypeMapper;
    }

    @Override
    public TransactionTypeDTO save(TransactionTypeDTO transactionTypeDTO) {
        log.debug("Request to save TransactionType : {}", transactionTypeDTO);
        TransactionType transactionType = transactionTypeMapper.toEntity(transactionTypeDTO);
        transactionType = transactionTypeRepository.save(transactionType);
        return transactionTypeMapper.toDto(transactionType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionTypeDTO> findAll() {
        log.debug("Request to get all TransactionTypes");
        return transactionTypeRepository
            .findAll()
            .stream()
            .map(transactionTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionTypeDTO> findOne(Long id) {
        log.debug("Request to get TransactionType : {}", id);
        return transactionTypeRepository.findById(id).map(transactionTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionType : {}", id);
        transactionTypeRepository.deleteById(id);
    }
}
