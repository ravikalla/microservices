package in.ravikalla.microservices.savingsaccount.service;

import in.ravikalla.microservices.savingsaccount.service.dto.SavingsaccountDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Savingsaccount.
 */
public interface SavingsaccountService {

    /**
     * Save a savingsaccount.
     *
     * @param savingsaccountDTO the entity to save
     * @return the persisted entity
     */
    SavingsaccountDTO save(SavingsaccountDTO savingsaccountDTO);

    /**
     * Get all the savingsaccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SavingsaccountDTO> findAll(Pageable pageable);


    /**
     * Get the "id" savingsaccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SavingsaccountDTO> findOne(Long id);

    /**
     * Delete the "id" savingsaccount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
