package in.ravikalla.microservices.savingsaccount.service.impl;

import in.ravikalla.microservices.savingsaccount.service.SavingsaccountService;
import in.ravikalla.microservices.savingsaccount.domain.Savingsaccount;
import in.ravikalla.microservices.savingsaccount.repository.SavingsaccountRepository;
import in.ravikalla.microservices.savingsaccount.service.dto.SavingsaccountDTO;
import in.ravikalla.microservices.savingsaccount.service.mapper.SavingsaccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Savingsaccount.
 */
@Service
@Transactional
public class SavingsaccountServiceImpl implements SavingsaccountService {

    private final Logger log = LoggerFactory.getLogger(SavingsaccountServiceImpl.class);

    private final SavingsaccountRepository savingsaccountRepository;

    private final SavingsaccountMapper savingsaccountMapper;

    public SavingsaccountServiceImpl(SavingsaccountRepository savingsaccountRepository, SavingsaccountMapper savingsaccountMapper) {
        this.savingsaccountRepository = savingsaccountRepository;
        this.savingsaccountMapper = savingsaccountMapper;
    }

    /**
     * Save a savingsaccount.
     *
     * @param savingsaccountDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SavingsaccountDTO save(SavingsaccountDTO savingsaccountDTO) {
        log.debug("Request to save Savingsaccount : {}", savingsaccountDTO);
        Savingsaccount savingsaccount = savingsaccountMapper.toEntity(savingsaccountDTO);
        savingsaccount = savingsaccountRepository.save(savingsaccount);
        return savingsaccountMapper.toDto(savingsaccount);
    }

    /**
     * Get all the savingsaccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SavingsaccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Savingsaccounts");
        return savingsaccountRepository.findAll(pageable)
            .map(savingsaccountMapper::toDto);
    }


    /**
     * Get one savingsaccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SavingsaccountDTO> findOne(Long id) {
        log.debug("Request to get Savingsaccount : {}", id);
        return savingsaccountRepository.findById(id)
            .map(savingsaccountMapper::toDto);
    }

    /**
     * Delete the savingsaccount by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Savingsaccount : {}", id);
        savingsaccountRepository.deleteById(id);
    }
}
