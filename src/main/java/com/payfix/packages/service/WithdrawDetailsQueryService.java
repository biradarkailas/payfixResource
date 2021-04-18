package com.payfix.packages.service;

import com.payfix.packages.domain.*; // for static metamodels
import com.payfix.packages.domain.WithdrawDetails;
import com.payfix.packages.repository.WithdrawDetailsRepository;
import com.payfix.packages.service.criteria.WithdrawDetailsCriteria;
import com.payfix.packages.service.dto.WithdrawDetailsDTO;
import com.payfix.packages.service.mapper.WithdrawDetailsMapper;
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
 * Service for executing complex queries for {@link WithdrawDetails} entities in the database.
 * The main input is a {@link WithdrawDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WithdrawDetailsDTO} or a {@link Page} of {@link WithdrawDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WithdrawDetailsQueryService extends QueryService<WithdrawDetails> {

    private final Logger log = LoggerFactory.getLogger(WithdrawDetailsQueryService.class);

    private final WithdrawDetailsRepository withdrawDetailsRepository;

    private final WithdrawDetailsMapper withdrawDetailsMapper;

    public WithdrawDetailsQueryService(WithdrawDetailsRepository withdrawDetailsRepository, WithdrawDetailsMapper withdrawDetailsMapper) {
        this.withdrawDetailsRepository = withdrawDetailsRepository;
        this.withdrawDetailsMapper = withdrawDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link WithdrawDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WithdrawDetailsDTO> findByCriteria(WithdrawDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WithdrawDetails> specification = createSpecification(criteria);
        return withdrawDetailsMapper.toDto(withdrawDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WithdrawDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WithdrawDetailsDTO> findByCriteria(WithdrawDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WithdrawDetails> specification = createSpecification(criteria);
        return withdrawDetailsRepository.findAll(specification, page).map(withdrawDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WithdrawDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WithdrawDetails> specification = createSpecification(criteria);
        return withdrawDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link WithdrawDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WithdrawDetails> createSpecification(WithdrawDetailsCriteria criteria) {
        Specification<WithdrawDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WithdrawDetails_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), WithdrawDetails_.amount));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), WithdrawDetails_.date));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(WithdrawDetails_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getWithdrawStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWithdrawStatusId(),
                            root -> root.join(WithdrawDetails_.withdrawStatus, JoinType.LEFT).get(WithdrawStatus_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
