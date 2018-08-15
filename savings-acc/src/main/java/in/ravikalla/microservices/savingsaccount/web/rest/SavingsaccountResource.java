package in.ravikalla.microservices.savingsaccount.web.rest;

import com.codahale.metrics.annotation.Timed;
import in.ravikalla.microservices.savingsaccount.service.SavingsaccountService;
import in.ravikalla.microservices.savingsaccount.web.rest.errors.BadRequestAlertException;
import in.ravikalla.microservices.savingsaccount.web.rest.util.HeaderUtil;
import in.ravikalla.microservices.savingsaccount.web.rest.util.PaginationUtil;
import in.ravikalla.microservices.savingsaccount.service.dto.SavingsaccountDTO;
import in.ravikalla.microservices.savingsaccount.service.dto.SavingsaccountCriteria;
import in.ravikalla.microservices.savingsaccount.service.SavingsaccountQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Savingsaccount.
 */
@RestController
@RequestMapping("/api")
public class SavingsaccountResource {

    private final Logger log = LoggerFactory.getLogger(SavingsaccountResource.class);

    private static final String ENTITY_NAME = "savingsaccount";

    private final SavingsaccountService savingsaccountService;

    private final SavingsaccountQueryService savingsaccountQueryService;

    public SavingsaccountResource(SavingsaccountService savingsaccountService, SavingsaccountQueryService savingsaccountQueryService) {
        this.savingsaccountService = savingsaccountService;
        this.savingsaccountQueryService = savingsaccountQueryService;
    }

    /**
     * POST  /savingsaccounts : Create a new savingsaccount.
     *
     * @param savingsaccountDTO the savingsaccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new savingsaccountDTO, or with status 400 (Bad Request) if the savingsaccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/savingsaccounts")
    @Timed
    public ResponseEntity<SavingsaccountDTO> createSavingsaccount(@Valid @RequestBody SavingsaccountDTO savingsaccountDTO) throws URISyntaxException {
        log.debug("REST request to save Savingsaccount : {}", savingsaccountDTO);
        if (savingsaccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new savingsaccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SavingsaccountDTO result = savingsaccountService.save(savingsaccountDTO);
        return ResponseEntity.created(new URI("/api/savingsaccounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /savingsaccounts : Updates an existing savingsaccount.
     *
     * @param savingsaccountDTO the savingsaccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated savingsaccountDTO,
     * or with status 400 (Bad Request) if the savingsaccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the savingsaccountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/savingsaccounts")
    @Timed
    public ResponseEntity<SavingsaccountDTO> updateSavingsaccount(@Valid @RequestBody SavingsaccountDTO savingsaccountDTO) throws URISyntaxException {
        log.debug("REST request to update Savingsaccount : {}", savingsaccountDTO);
        if (savingsaccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SavingsaccountDTO result = savingsaccountService.save(savingsaccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, savingsaccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /savingsaccounts : get all the savingsaccounts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of savingsaccounts in body
     */
    @GetMapping("/savingsaccounts")
    @Timed
    public ResponseEntity<List<SavingsaccountDTO>> getAllSavingsaccounts(SavingsaccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Savingsaccounts by criteria: {}", criteria);
        Page<SavingsaccountDTO> page = savingsaccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/savingsaccounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /savingsaccounts/:id : get the "id" savingsaccount.
     *
     * @param id the id of the savingsaccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the savingsaccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/savingsaccounts/{id}")
    @Timed
    public ResponseEntity<SavingsaccountDTO> getSavingsaccount(@PathVariable Long id) {
        log.debug("REST request to get Savingsaccount : {}", id);
        Optional<SavingsaccountDTO> savingsaccountDTO = savingsaccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(savingsaccountDTO);
    }

    /**
     * DELETE  /savingsaccounts/:id : delete the "id" savingsaccount.
     *
     * @param id the id of the savingsaccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/savingsaccounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteSavingsaccount(@PathVariable Long id) {
        log.debug("REST request to delete Savingsaccount : {}", id);
        savingsaccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
