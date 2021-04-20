package com.payfix.packages.service;

import com.payfix.packages.domain.*; // for static metamodels
import com.payfix.packages.domain.LogoTransaction;
import com.payfix.packages.repository.LogoTransactionRepository;
import com.payfix.packages.service.criteria.LogoTransactionCriteria;
import com.payfix.packages.service.dto.LogoTransactionDTO;
import com.payfix.packages.service.mapper.LogoTransactionMapper;
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
 * Service for executing complex queries for {@link LogoTransaction} entities in the database.
 * The main input is a {@link LogoTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LogoTransactionDTO} or a {@link Page} of {@link LogoTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LogoTransactionQueryService extends QueryService<LogoTransaction> {

    private final Logger log = LoggerFactory.getLogger(LogoTransactionQueryService.class);

    private final LogoTransactionRepository logoTransactionRepository;

    private final LogoTransactionMapper logoTransactionMapper;

    public LogoTransactionQueryService(LogoTransactionRepository logoTransactionRepository, LogoTransactionMapper logoTransactionMapper) {
        this.logoTransactionRepository = logoTransactionRepository;
        this.logoTransactionMapper = logoTransactionMapper;
    }

    /**
     * Return a {@link List} of {@link LogoTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LogoTransactionDTO> findByCriteria(LogoTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LogoTransaction> specification = createSpecification(criteria);
        return logoTransactionMapper.toDto(logoTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LogoTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LogoTransactionDTO> findByCriteria(LogoTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LogoTransaction> specification = createSpecification(criteria);
        return logoTransactionRepository.findAll(specification, page).map(logoTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LogoTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LogoTransaction> specification = createSpecification(criteria);
        return logoTransactionRepository.count(specification);
    }

    /**
     * Function to convert {@link LogoTransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LogoTransaction> createSpecification(LogoTransactionCriteria criteria) {
        Specification<LogoTransaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LogoTransaction_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), LogoTransaction_.amount));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(LogoTransaction_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getLogoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLogoId(), root -> root.join(LogoTransaction_.logo, JoinType.LEFT).get(Logo_.id))
                    );
            }
            if (criteria.getTransactionTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionTypeId(),
                            root -> root.join(LogoTransaction_.transactionType, JoinType.LEFT).get(TransactionType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
