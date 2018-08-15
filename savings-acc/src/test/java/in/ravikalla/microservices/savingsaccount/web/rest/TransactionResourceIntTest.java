package in.ravikalla.microservices.savingsaccount.web.rest;

import in.ravikalla.microservices.savingsaccount.SavingsaccountApp;

import in.ravikalla.microservices.savingsaccount.domain.Transaction;
import in.ravikalla.microservices.savingsaccount.domain.Savingsaccount;
import in.ravikalla.microservices.savingsaccount.repository.TransactionRepository;
import in.ravikalla.microservices.savingsaccount.service.TransactionService;
import in.ravikalla.microservices.savingsaccount.service.dto.TransactionDTO;
import in.ravikalla.microservices.savingsaccount.service.mapper.TransactionMapper;
import in.ravikalla.microservices.savingsaccount.web.rest.errors.ExceptionTranslator;
import in.ravikalla.microservices.savingsaccount.service.dto.TransactionCriteria;
import in.ravikalla.microservices.savingsaccount.service.TransactionQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static in.ravikalla.microservices.savingsaccount.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TransactionResource REST controller.
 *
 * @see TransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SavingsaccountApp.class)
public class TransactionResourceIntTest {

    private static final Double DEFAULT_AMOUNT = -100000000000D;
    private static final Double UPDATED_AMOUNT = -99999999999D;

    private static final LocalDate DEFAULT_TRANSACTIONDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTIONDATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TransactionRepository transactionRepository;


    @Autowired
    private TransactionMapper transactionMapper;
    

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionQueryService transactionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransactionMockMvc;

    private Transaction transaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionResource transactionResource = new TransactionResource(transactionService, transactionQueryService);
        this.restTransactionMockMvc = MockMvcBuilders.standaloneSetup(transactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .amount(DEFAULT_AMOUNT)
            .transactiondate(DEFAULT_TRANSACTIONDATE);
        return transaction;
    }

    @Before
    public void initTest() {
        transaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);
        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isCreated());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTransaction.getTransactiondate()).isEqualTo(DEFAULT_TRANSACTIONDATE);
    }

    @Test
    @Transactional
    public void createTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction with an existing ID
        transaction.setId(1L);
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setAmount(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactiondateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setTransactiondate(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList
        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].transactiondate").value(hasItem(DEFAULT_TRANSACTIONDATE.toString())));
    }
    

    @Test
    @Transactional
    public void getTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.transactiondate").value(DEFAULT_TRANSACTIONDATE.toString()));
    }

    @Test
    @Transactional
    public void getAllTransactionsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount equals to DEFAULT_AMOUNT
        defaultTransactionShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the transactionList where amount equals to UPDATED_AMOUNT
        defaultTransactionShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultTransactionShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the transactionList where amount equals to UPDATED_AMOUNT
        defaultTransactionShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount is not null
        defaultTransactionShouldBeFound("amount.specified=true");

        // Get all the transactionList where amount is null
        defaultTransactionShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactiondateIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactiondate equals to DEFAULT_TRANSACTIONDATE
        defaultTransactionShouldBeFound("transactiondate.equals=" + DEFAULT_TRANSACTIONDATE);

        // Get all the transactionList where transactiondate equals to UPDATED_TRANSACTIONDATE
        defaultTransactionShouldNotBeFound("transactiondate.equals=" + UPDATED_TRANSACTIONDATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactiondateIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactiondate in DEFAULT_TRANSACTIONDATE or UPDATED_TRANSACTIONDATE
        defaultTransactionShouldBeFound("transactiondate.in=" + DEFAULT_TRANSACTIONDATE + "," + UPDATED_TRANSACTIONDATE);

        // Get all the transactionList where transactiondate equals to UPDATED_TRANSACTIONDATE
        defaultTransactionShouldNotBeFound("transactiondate.in=" + UPDATED_TRANSACTIONDATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactiondateIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactiondate is not null
        defaultTransactionShouldBeFound("transactiondate.specified=true");

        // Get all the transactionList where transactiondate is null
        defaultTransactionShouldNotBeFound("transactiondate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactiondateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactiondate greater than or equals to DEFAULT_TRANSACTIONDATE
        defaultTransactionShouldBeFound("transactiondate.greaterOrEqualThan=" + DEFAULT_TRANSACTIONDATE);

        // Get all the transactionList where transactiondate greater than or equals to UPDATED_TRANSACTIONDATE
        defaultTransactionShouldNotBeFound("transactiondate.greaterOrEqualThan=" + UPDATED_TRANSACTIONDATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactiondateIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactiondate less than or equals to DEFAULT_TRANSACTIONDATE
        defaultTransactionShouldNotBeFound("transactiondate.lessThan=" + DEFAULT_TRANSACTIONDATE);

        // Get all the transactionList where transactiondate less than or equals to UPDATED_TRANSACTIONDATE
        defaultTransactionShouldBeFound("transactiondate.lessThan=" + UPDATED_TRANSACTIONDATE);
    }


    @Test
    @Transactional
    public void getAllTransactionsBySavingsaccountIsEqualToSomething() throws Exception {
        // Initialize the database
        Savingsaccount savingsaccount = SavingsaccountResourceIntTest.createEntity(em);
        em.persist(savingsaccount);
        em.flush();
        transaction.setSavingsaccount(savingsaccount);
        transactionRepository.saveAndFlush(transaction);
        Long savingsaccountId = savingsaccount.getId();

        // Get all the transactionList where savingsaccount equals to savingsaccountId
        defaultTransactionShouldBeFound("savingsaccountId.equals=" + savingsaccountId);

        // Get all the transactionList where savingsaccount equals to savingsaccountId + 1
        defaultTransactionShouldNotBeFound("savingsaccountId.equals=" + (savingsaccountId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTransactionShouldBeFound(String filter) throws Exception {
        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].transactiondate").value(hasItem(DEFAULT_TRANSACTIONDATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTransactionShouldNotBeFound(String filter) throws Exception {
        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).get();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction
            .amount(UPDATED_AMOUNT)
            .transactiondate(UPDATED_TRANSACTIONDATE);
        TransactionDTO transactionDTO = transactionMapper.toDto(updatedTransaction);

        restTransactionMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransaction.getTransactiondate()).isEqualTo(UPDATED_TRANSACTIONDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransactionMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Get the transaction
        restTransactionMockMvc.perform(delete("/api/transactions/{id}", transaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        Transaction transaction2 = new Transaction();
        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);
        transaction2.setId(2L);
        assertThat(transaction1).isNotEqualTo(transaction2);
        transaction1.setId(null);
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionDTO.class);
        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setId(1L);
        TransactionDTO transactionDTO2 = new TransactionDTO();
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
        transactionDTO2.setId(transactionDTO1.getId());
        assertThat(transactionDTO1).isEqualTo(transactionDTO2);
        transactionDTO2.setId(2L);
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
        transactionDTO1.setId(null);
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionMapper.fromId(null)).isNull();
    }
}
