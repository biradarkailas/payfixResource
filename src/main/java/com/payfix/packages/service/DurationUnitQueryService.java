package com.payfix.packages.service;

import com.payfix.packages.domain.*; // for static metamodels
import com.payfix.packages.domain.DurationUnit;
import com.payfix.packages.repository.DurationUnitRepository;
import com.payfix.packages.service.criteria.DurationUnitCriteria;
import com.payfix.packages.service.dto.DurationUnitDTO;
import com.payfix.packages.service.mapper.DurationUnitMapper;
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
 * Service for executing complex queries for {@link DurationUnit} entities in the database.
 * The main input is a {@link DurationUnitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DurationUnitDTO} or a {@link Page} of {@link DurationUnitDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DurationUnitQueryService extends QueryService<DurationUnit> {

    private final Logger log = LoggerFactory.getLogger(DurationUnitQueryService.class);

    private final DurationUnitRepository durationUnitRepository;

    private final DurationUnitMapper durationUnitMapper;

    public DurationUnitQueryService(DurationUnitRepository durationUnitRepository, DurationUnitMapper durationUnitMapper) {
        this.durationUnitRepository = durationUnitRepository;
        this.durationUnitMapper = durationUnitMapper;
    }

    /**
     * Return a {@link List} of {@link DurationUnitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DurationUnitDTO> findByCriteria(DurationUnitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DurationUnit> specification = createSpecification(criteria);
        return durationUnitMapper.toDto(durationUnitRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DurationUnitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DurationUnitDTO> findByCriteria(DurationUnitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DurationUnit> specification = createSpecification(criteria);
        return durationUnitRepository.findAll(specification, page).map(durationUnitMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DurationUnitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DurationUnit> specification = createSpecification(criteria);
        return durationUnitRepository.count(specification);
    }

    /**
     * Function to convert {@link DurationUnitCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DurationUnit> createSpecification(DurationUnitCriteria criteria) {
        Specification<DurationUnit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DurationUnit_.id));
            }
            if (criteria.getUnitValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitValue(), DurationUnit_.unitValue));
            }
            if (criteria.getAlies() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAlies(), DurationUnit_.alies));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DurationUnit_.name));
            }
        }
        return specification;
    }
}
