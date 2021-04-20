package com.payfix.packages.service;

import com.payfix.packages.domain.*; // for static metamodels
import com.payfix.packages.domain.LogoSubscription;
import com.payfix.packages.repository.LogoSubscriptionRepository;
import com.payfix.packages.service.criteria.LogoSubscriptionCriteria;
import com.payfix.packages.service.dto.LogoSubscriptionDTO;
import com.payfix.packages.service.mapper.LogoSubscriptionMapper;
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
 * Service for executing complex queries for {@link LogoSubscription} entities in the database.
 * The main input is a {@link LogoSubscriptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LogoSubscriptionDTO} or a {@link Page} of {@link LogoSubscriptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LogoSubscriptionQueryService extends QueryService<LogoSubscription> {

    private final Logger log = LoggerFactory.getLogger(LogoSubscriptionQueryService.class);

    private final LogoSubscriptionRepository logoSubscriptionRepository;

    private final LogoSubscriptionMapper logoSubscriptionMapper;

    public LogoSubscriptionQueryService(
        LogoSubscriptionRepository logoSubscriptionRepository,
        LogoSubscriptionMapper logoSubscriptionMapper
    ) {
        this.logoSubscriptionRepository = logoSubscriptionRepository;
        this.logoSubscriptionMapper = logoSubscriptionMapper;
    }

    /**
     * Return a {@link List} of {@link LogoSubscriptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LogoSubscriptionDTO> findByCriteria(LogoSubscriptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LogoSubscription> specification = createSpecification(criteria);
        return logoSubscriptionMapper.toDto(logoSubscriptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LogoSubscriptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LogoSubscriptionDTO> findByCriteria(LogoSubscriptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LogoSubscription> specification = createSpecification(criteria);
        return logoSubscriptionRepository.findAll(specification, page).map(logoSubscriptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LogoSubscriptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LogoSubscription> specification = createSpecification(criteria);
        return logoSubscriptionRepository.count(specification);
    }

    /**
     * Function to convert {@link LogoSubscriptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LogoSubscription> createSpecification(LogoSubscriptionCriteria criteria) {
        Specification<LogoSubscription> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LogoSubscription_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), LogoSubscription_.date));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(LogoSubscription_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getLogoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLogoId(), root -> root.join(LogoSubscription_.logo, JoinType.LEFT).get(Logo_.id))
                    );
            }
        }
        return specification;
    }
}
