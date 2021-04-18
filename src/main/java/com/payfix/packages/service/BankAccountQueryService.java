package com.payfix.packages.service;

import com.payfix.packages.domain.*; // for static metamodels
import com.payfix.packages.domain.BankAccount;
import com.payfix.packages.repository.BankAccountRepository;
import com.payfix.packages.service.criteria.BankAccountCriteria;
import com.payfix.packages.service.dto.BankAccountDTO;
import com.payfix.packages.service.mapper.BankAccountMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BankAccount} entities in the database.
 * The main input is a {@link BankAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BankAccountDTO} or a {@link Page} of {@link BankAccountDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BankAccountQueryService extends QueryService<BankAccount> {

    private final Logger log = LoggerFactory.getLogger(BankAccountQueryService.class);

    private final BankAccountRepository bankAccountRepository;

    private final BankAccountMapper bankAccountMapper;

    public BankAccountQueryService(BankAccountRepository bankAccountRepository, BankAccountMapper bankAccountMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    /**
     * Return a {@link List} of {@link BankAccountDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BankAccountDTO> findByCriteria(BankAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BankAccount> specification = createSpecification(criteria);
        return bankAccountMapper.toDto(bankAccountRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BankAccountDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BankAccountDTO> findByCriteria(BankAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BankAccount> specification = createSpecification(criteria);
        return bankAccountRepository.findAll(specification, page).map(bankAccountMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BankAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BankAccount> specification = createSpecification(criteria);
        return bankAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link BankAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BankAccount> createSpecification(BankAccountCriteria criteria) {
        Specification<BankAccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BankAccount_.id));
            }
            if (criteria.getAccountNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountNumber(), BankAccount_.accountNumber));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), BankAccount_.name));
            }
            if (criteria.getIfscCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIfscCode(), BankAccount_.ifscCode));
            }
            if (criteria.getBankName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankName(), BankAccount_.bankName));
            }
        }
        return specification;
    }
}
