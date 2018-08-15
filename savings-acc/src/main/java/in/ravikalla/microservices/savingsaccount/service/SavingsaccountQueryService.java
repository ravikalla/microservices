package in.ravikalla.microservices.savingsaccount.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import in.ravikalla.microservices.savingsaccount.domain.Savingsaccount;
import in.ravikalla.microservices.savingsaccount.domain.*; // for static metamodels
import in.ravikalla.microservices.savingsaccount.repository.SavingsaccountRepository;
import in.ravikalla.microservices.savingsaccount.service.dto.SavingsaccountCriteria;

import in.ravikalla.microservices.savingsaccount.service.dto.SavingsaccountDTO;
import in.ravikalla.microservices.savingsaccount.service.mapper.SavingsaccountMapper;

/**
 * Service for executing complex queries for Savingsaccount entities in the database.
 * The main input is a {@link SavingsaccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SavingsaccountDTO} or a {@link Page} of {@link SavingsaccountDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SavingsaccountQueryService extends QueryService<Savingsaccount> {

    private final Logger log = LoggerFactory.getLogger(SavingsaccountQueryService.class);

    private final SavingsaccountRepository savingsaccountRepository;

    private final SavingsaccountMapper savingsaccountMapper;

    public SavingsaccountQueryService(SavingsaccountRepository savingsaccountRepository, SavingsaccountMapper savingsaccountMapper) {
        this.savingsaccountRepository = savingsaccountRepository;
        this.savingsaccountMapper = savingsaccountMapper;
    }

    /**
     * Return a {@link List} of {@link SavingsaccountDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SavingsaccountDTO> findByCriteria(SavingsaccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Savingsaccount> specification = createSpecification(criteria);
        return savingsaccountMapper.toDto(savingsaccountRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SavingsaccountDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SavingsaccountDTO> findByCriteria(SavingsaccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Savingsaccount> specification = createSpecification(criteria);
        return savingsaccountRepository.findAll(specification, page)
            .map(savingsaccountMapper::toDto);
    }

    /**
     * Function to convert SavingsaccountCriteria to a {@link Specification}
     */
    private Specification<Savingsaccount> createSpecification(SavingsaccountCriteria criteria) {
        Specification<Savingsaccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Savingsaccount_.id));
            }
            if (criteria.getAccountnumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccountnumber(), Savingsaccount_.accountnumber));
            }
            if (criteria.getAccountname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountname(), Savingsaccount_.accountname));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Savingsaccount_.amount));
            }
            if (criteria.getGeneralinfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGeneralinfo(), Savingsaccount_.generalinfo));
            }
        }
        return specification;
    }

}
