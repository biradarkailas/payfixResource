package com.payfix.packages.service;

import com.payfix.packages.domain.*; // for static metamodels
import com.payfix.packages.domain.UserDetails;
import com.payfix.packages.repository.UserDetailsRepository;
import com.payfix.packages.service.criteria.UserDetailsCriteria;
import com.payfix.packages.service.dto.UserDetailsDTO;
import com.payfix.packages.service.mapper.UserDetailsMapper;
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
 * Service for executing complex queries for {@link UserDetails} entities in the database.
 * The main input is a {@link UserDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserDetailsDTO} or a {@link Page} of {@link UserDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserDetailsQueryService extends QueryService<UserDetails> {

    private final Logger log = LoggerFactory.getLogger(UserDetailsQueryService.class);

    private final UserDetailsRepository userDetailsRepository;

    private final UserDetailsMapper userDetailsMapper;

    public UserDetailsQueryService(UserDetailsRepository userDetailsRepository, UserDetailsMapper userDetailsMapper) {
        this.userDetailsRepository = userDetailsRepository;
        this.userDetailsMapper = userDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link UserDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserDetailsDTO> findByCriteria(UserDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserDetails> specification = createSpecification(criteria);
        return userDetailsMapper.toDto(userDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserDetailsDTO> findByCriteria(UserDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserDetails> specification = createSpecification(criteria);
        return userDetailsRepository.findAll(specification, page).map(userDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserDetails> specification = createSpecification(criteria);
        return userDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link UserDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserDetails> createSpecification(UserDetailsCriteria criteria) {
        Specification<UserDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserDetails_.id));
            }
            if (criteria.getCompanyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyName(), UserDetails_.companyName));
            }
            if (criteria.getRegistrationNumber() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRegistrationNumber(), UserDetails_.registrationNumber));
            }
            if (criteria.getRegistrationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegistrationDate(), UserDetails_.registrationDate));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), UserDetails_.balance));
            }
            if (criteria.getMobileNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMobileNumber(), UserDetails_.mobileNumber));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(UserDetails_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
