package com.payfix.packages.service;

import com.payfix.packages.domain.*; // for static metamodels
import com.payfix.packages.domain.Logo;
import com.payfix.packages.repository.LogoRepository;
import com.payfix.packages.service.criteria.LogoCriteria;
import com.payfix.packages.service.dto.LogoDTO;
import com.payfix.packages.service.mapper.LogoMapper;
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
 * Service for executing complex queries for {@link Logo} entities in the database.
 * The main input is a {@link LogoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LogoDTO} or a {@link Page} of {@link LogoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LogoQueryService extends QueryService<Logo> {

    private final Logger log = LoggerFactory.getLogger(LogoQueryService.class);

    private final LogoRepository logoRepository;

    private final LogoMapper logoMapper;

    public LogoQueryService(LogoRepository logoRepository, LogoMapper logoMapper) {
        this.logoRepository = logoRepository;
        this.logoMapper = logoMapper;
    }

    /**
     * Return a {@link List} of {@link LogoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LogoDTO> findByCriteria(LogoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Logo> specification = createSpecification(criteria);
        return logoMapper.toDto(logoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LogoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LogoDTO> findByCriteria(LogoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Logo> specification = createSpecification(criteria);
        return logoRepository.findAll(specification, page).map(logoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LogoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Logo> specification = createSpecification(criteria);
        return logoRepository.count(specification);
    }

    /**
     * Function to convert {@link LogoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Logo> createSpecification(LogoCriteria criteria) {
        Specification<Logo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Logo_.id));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), Logo_.imageUrl));
            }
            if (criteria.getTermConditions() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTermConditions(), Logo_.termConditions));
            }
            if (criteria.getAbout() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAbout(), Logo_.about));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getUserId(), root -> root.join(Logo_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCategoryId(), root -> root.join(Logo_.category, JoinType.LEFT).get(Category_.id))
                    );
            }
            if (criteria.getDurationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDurationId(), root -> root.join(Logo_.duration, JoinType.LEFT).get(Duration_.id))
                    );
            }
            if (criteria.getDurationUnitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDurationUnitId(),
                            root -> root.join(Logo_.durationUnit, JoinType.LEFT).get(DurationUnit_.id)
                        )
                    );
            }
            if (criteria.getPricePerDayId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPricePerDayId(),
                            root -> root.join(Logo_.pricePerDay, JoinType.LEFT).get(PricePerDay_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
