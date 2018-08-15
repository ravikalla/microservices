package in.ravikalla.microservices.savingsaccount.web.rest;

import in.ravikalla.microservices.savingsaccount.SavingsaccountApp;

import in.ravikalla.microservices.savingsaccount.domain.Savingsaccount;
import in.ravikalla.microservices.savingsaccount.repository.SavingsaccountRepository;
import in.ravikalla.microservices.savingsaccount.service.SavingsaccountService;
import in.ravikalla.microservices.savingsaccount.service.dto.SavingsaccountDTO;
import in.ravikalla.microservices.savingsaccount.service.mapper.SavingsaccountMapper;
import in.ravikalla.microservices.savingsaccount.web.rest.errors.ExceptionTranslator;
import in.ravikalla.microservices.savingsaccount.service.dto.SavingsaccountCriteria;
import in.ravikalla.microservices.savingsaccount.service.SavingsaccountQueryService;

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
import java.util.List;


import static in.ravikalla.microservices.savingsaccount.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SavingsaccountResource REST controller.
 *
 * @see SavingsaccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SavingsaccountApp.class)
public class SavingsaccountResourceIntTest {

    private static final Integer DEFAULT_ACCOUNTNUMBER = 1;
    private static final Integer UPDATED_ACCOUNTNUMBER = 2;

    private static final String DEFAULT_ACCOUNTNAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTNAME = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_GENERALINFO = "AAAAAAAAAA";
    private static final String UPDATED_GENERALINFO = "BBBBBBBBBB";

    @Autowired
    private SavingsaccountRepository savingsaccountRepository;


    @Autowired
    private SavingsaccountMapper savingsaccountMapper;
    

    @Autowired
    private SavingsaccountService savingsaccountService;

    @Autowired
    private SavingsaccountQueryService savingsaccountQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSavingsaccountMockMvc;

    private Savingsaccount savingsaccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SavingsaccountResource savingsaccountResource = new SavingsaccountResource(savingsaccountService, savingsaccountQueryService);
        this.restSavingsaccountMockMvc = MockMvcBuilders.standaloneSetup(savingsaccountResource)
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
    public static Savingsaccount createEntity(EntityManager em) {
        Savingsaccount savingsaccount = new Savingsaccount()
            .accountnumber(DEFAULT_ACCOUNTNUMBER)
            .accountname(DEFAULT_ACCOUNTNAME)
            .amount(DEFAULT_AMOUNT)
            .generalinfo(DEFAULT_GENERALINFO);
        return savingsaccount;
    }

    @Before
    public void initTest() {
        savingsaccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createSavingsaccount() throws Exception {
        int databaseSizeBeforeCreate = savingsaccountRepository.findAll().size();

        // Create the Savingsaccount
        SavingsaccountDTO savingsaccountDTO = savingsaccountMapper.toDto(savingsaccount);
        restSavingsaccountMockMvc.perform(post("/api/savingsaccounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(savingsaccountDTO)))
            .andExpect(status().isCreated());

        // Validate the Savingsaccount in the database
        List<Savingsaccount> savingsaccountList = savingsaccountRepository.findAll();
        assertThat(savingsaccountList).hasSize(databaseSizeBeforeCreate + 1);
        Savingsaccount testSavingsaccount = savingsaccountList.get(savingsaccountList.size() - 1);
        assertThat(testSavingsaccount.getAccountnumber()).isEqualTo(DEFAULT_ACCOUNTNUMBER);
        assertThat(testSavingsaccount.getAccountname()).isEqualTo(DEFAULT_ACCOUNTNAME);
        assertThat(testSavingsaccount.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testSavingsaccount.getGeneralinfo()).isEqualTo(DEFAULT_GENERALINFO);
    }

    @Test
    @Transactional
    public void createSavingsaccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = savingsaccountRepository.findAll().size();

        // Create the Savingsaccount with an existing ID
        savingsaccount.setId(1L);
        SavingsaccountDTO savingsaccountDTO = savingsaccountMapper.toDto(savingsaccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSavingsaccountMockMvc.perform(post("/api/savingsaccounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(savingsaccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Savingsaccount in the database
        List<Savingsaccount> savingsaccountList = savingsaccountRepository.findAll();
        assertThat(savingsaccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAccountnumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = savingsaccountRepository.findAll().size();
        // set the field null
        savingsaccount.setAccountnumber(null);

        // Create the Savingsaccount, which fails.
        SavingsaccountDTO savingsaccountDTO = savingsaccountMapper.toDto(savingsaccount);

        restSavingsaccountMockMvc.perform(post("/api/savingsaccounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(savingsaccountDTO)))
            .andExpect(status().isBadRequest());

        List<Savingsaccount> savingsaccountList = savingsaccountRepository.findAll();
        assertThat(savingsaccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = savingsaccountRepository.findAll().size();
        // set the field null
        savingsaccount.setAccountname(null);

        // Create the Savingsaccount, which fails.
        SavingsaccountDTO savingsaccountDTO = savingsaccountMapper.toDto(savingsaccount);

        restSavingsaccountMockMvc.perform(post("/api/savingsaccounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(savingsaccountDTO)))
            .andExpect(status().isBadRequest());

        List<Savingsaccount> savingsaccountList = savingsaccountRepository.findAll();
        assertThat(savingsaccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = savingsaccountRepository.findAll().size();
        // set the field null
        savingsaccount.setAmount(null);

        // Create the Savingsaccount, which fails.
        SavingsaccountDTO savingsaccountDTO = savingsaccountMapper.toDto(savingsaccount);

        restSavingsaccountMockMvc.perform(post("/api/savingsaccounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(savingsaccountDTO)))
            .andExpect(status().isBadRequest());

        List<Savingsaccount> savingsaccountList = savingsaccountRepository.findAll();
        assertThat(savingsaccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSavingsaccounts() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList
        restSavingsaccountMockMvc.perform(get("/api/savingsaccounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(savingsaccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountnumber").value(hasItem(DEFAULT_ACCOUNTNUMBER)))
            .andExpect(jsonPath("$.[*].accountname").value(hasItem(DEFAULT_ACCOUNTNAME.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].generalinfo").value(hasItem(DEFAULT_GENERALINFO.toString())));
    }
    

    @Test
    @Transactional
    public void getSavingsaccount() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get the savingsaccount
        restSavingsaccountMockMvc.perform(get("/api/savingsaccounts/{id}", savingsaccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(savingsaccount.getId().intValue()))
            .andExpect(jsonPath("$.accountnumber").value(DEFAULT_ACCOUNTNUMBER))
            .andExpect(jsonPath("$.accountname").value(DEFAULT_ACCOUNTNAME.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.generalinfo").value(DEFAULT_GENERALINFO.toString()));
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByAccountnumberIsEqualToSomething() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where accountnumber equals to DEFAULT_ACCOUNTNUMBER
        defaultSavingsaccountShouldBeFound("accountnumber.equals=" + DEFAULT_ACCOUNTNUMBER);

        // Get all the savingsaccountList where accountnumber equals to UPDATED_ACCOUNTNUMBER
        defaultSavingsaccountShouldNotBeFound("accountnumber.equals=" + UPDATED_ACCOUNTNUMBER);
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByAccountnumberIsInShouldWork() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where accountnumber in DEFAULT_ACCOUNTNUMBER or UPDATED_ACCOUNTNUMBER
        defaultSavingsaccountShouldBeFound("accountnumber.in=" + DEFAULT_ACCOUNTNUMBER + "," + UPDATED_ACCOUNTNUMBER);

        // Get all the savingsaccountList where accountnumber equals to UPDATED_ACCOUNTNUMBER
        defaultSavingsaccountShouldNotBeFound("accountnumber.in=" + UPDATED_ACCOUNTNUMBER);
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByAccountnumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where accountnumber is not null
        defaultSavingsaccountShouldBeFound("accountnumber.specified=true");

        // Get all the savingsaccountList where accountnumber is null
        defaultSavingsaccountShouldNotBeFound("accountnumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByAccountnumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where accountnumber greater than or equals to DEFAULT_ACCOUNTNUMBER
        defaultSavingsaccountShouldBeFound("accountnumber.greaterOrEqualThan=" + DEFAULT_ACCOUNTNUMBER);

        // Get all the savingsaccountList where accountnumber greater than or equals to UPDATED_ACCOUNTNUMBER
        defaultSavingsaccountShouldNotBeFound("accountnumber.greaterOrEqualThan=" + UPDATED_ACCOUNTNUMBER);
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByAccountnumberIsLessThanSomething() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where accountnumber less than or equals to DEFAULT_ACCOUNTNUMBER
        defaultSavingsaccountShouldNotBeFound("accountnumber.lessThan=" + DEFAULT_ACCOUNTNUMBER);

        // Get all the savingsaccountList where accountnumber less than or equals to UPDATED_ACCOUNTNUMBER
        defaultSavingsaccountShouldBeFound("accountnumber.lessThan=" + UPDATED_ACCOUNTNUMBER);
    }


    @Test
    @Transactional
    public void getAllSavingsaccountsByAccountnameIsEqualToSomething() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where accountname equals to DEFAULT_ACCOUNTNAME
        defaultSavingsaccountShouldBeFound("accountname.equals=" + DEFAULT_ACCOUNTNAME);

        // Get all the savingsaccountList where accountname equals to UPDATED_ACCOUNTNAME
        defaultSavingsaccountShouldNotBeFound("accountname.equals=" + UPDATED_ACCOUNTNAME);
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByAccountnameIsInShouldWork() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where accountname in DEFAULT_ACCOUNTNAME or UPDATED_ACCOUNTNAME
        defaultSavingsaccountShouldBeFound("accountname.in=" + DEFAULT_ACCOUNTNAME + "," + UPDATED_ACCOUNTNAME);

        // Get all the savingsaccountList where accountname equals to UPDATED_ACCOUNTNAME
        defaultSavingsaccountShouldNotBeFound("accountname.in=" + UPDATED_ACCOUNTNAME);
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByAccountnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where accountname is not null
        defaultSavingsaccountShouldBeFound("accountname.specified=true");

        // Get all the savingsaccountList where accountname is null
        defaultSavingsaccountShouldNotBeFound("accountname.specified=false");
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where amount equals to DEFAULT_AMOUNT
        defaultSavingsaccountShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the savingsaccountList where amount equals to UPDATED_AMOUNT
        defaultSavingsaccountShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultSavingsaccountShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the savingsaccountList where amount equals to UPDATED_AMOUNT
        defaultSavingsaccountShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where amount is not null
        defaultSavingsaccountShouldBeFound("amount.specified=true");

        // Get all the savingsaccountList where amount is null
        defaultSavingsaccountShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByGeneralinfoIsEqualToSomething() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where generalinfo equals to DEFAULT_GENERALINFO
        defaultSavingsaccountShouldBeFound("generalinfo.equals=" + DEFAULT_GENERALINFO);

        // Get all the savingsaccountList where generalinfo equals to UPDATED_GENERALINFO
        defaultSavingsaccountShouldNotBeFound("generalinfo.equals=" + UPDATED_GENERALINFO);
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByGeneralinfoIsInShouldWork() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where generalinfo in DEFAULT_GENERALINFO or UPDATED_GENERALINFO
        defaultSavingsaccountShouldBeFound("generalinfo.in=" + DEFAULT_GENERALINFO + "," + UPDATED_GENERALINFO);

        // Get all the savingsaccountList where generalinfo equals to UPDATED_GENERALINFO
        defaultSavingsaccountShouldNotBeFound("generalinfo.in=" + UPDATED_GENERALINFO);
    }

    @Test
    @Transactional
    public void getAllSavingsaccountsByGeneralinfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        // Get all the savingsaccountList where generalinfo is not null
        defaultSavingsaccountShouldBeFound("generalinfo.specified=true");

        // Get all the savingsaccountList where generalinfo is null
        defaultSavingsaccountShouldNotBeFound("generalinfo.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSavingsaccountShouldBeFound(String filter) throws Exception {
        restSavingsaccountMockMvc.perform(get("/api/savingsaccounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(savingsaccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountnumber").value(hasItem(DEFAULT_ACCOUNTNUMBER)))
            .andExpect(jsonPath("$.[*].accountname").value(hasItem(DEFAULT_ACCOUNTNAME.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].generalinfo").value(hasItem(DEFAULT_GENERALINFO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSavingsaccountShouldNotBeFound(String filter) throws Exception {
        restSavingsaccountMockMvc.perform(get("/api/savingsaccounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingSavingsaccount() throws Exception {
        // Get the savingsaccount
        restSavingsaccountMockMvc.perform(get("/api/savingsaccounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSavingsaccount() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        int databaseSizeBeforeUpdate = savingsaccountRepository.findAll().size();

        // Update the savingsaccount
        Savingsaccount updatedSavingsaccount = savingsaccountRepository.findById(savingsaccount.getId()).get();
        // Disconnect from session so that the updates on updatedSavingsaccount are not directly saved in db
        em.detach(updatedSavingsaccount);
        updatedSavingsaccount
            .accountnumber(UPDATED_ACCOUNTNUMBER)
            .accountname(UPDATED_ACCOUNTNAME)
            .amount(UPDATED_AMOUNT)
            .generalinfo(UPDATED_GENERALINFO);
        SavingsaccountDTO savingsaccountDTO = savingsaccountMapper.toDto(updatedSavingsaccount);

        restSavingsaccountMockMvc.perform(put("/api/savingsaccounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(savingsaccountDTO)))
            .andExpect(status().isOk());

        // Validate the Savingsaccount in the database
        List<Savingsaccount> savingsaccountList = savingsaccountRepository.findAll();
        assertThat(savingsaccountList).hasSize(databaseSizeBeforeUpdate);
        Savingsaccount testSavingsaccount = savingsaccountList.get(savingsaccountList.size() - 1);
        assertThat(testSavingsaccount.getAccountnumber()).isEqualTo(UPDATED_ACCOUNTNUMBER);
        assertThat(testSavingsaccount.getAccountname()).isEqualTo(UPDATED_ACCOUNTNAME);
        assertThat(testSavingsaccount.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testSavingsaccount.getGeneralinfo()).isEqualTo(UPDATED_GENERALINFO);
    }

    @Test
    @Transactional
    public void updateNonExistingSavingsaccount() throws Exception {
        int databaseSizeBeforeUpdate = savingsaccountRepository.findAll().size();

        // Create the Savingsaccount
        SavingsaccountDTO savingsaccountDTO = savingsaccountMapper.toDto(savingsaccount);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSavingsaccountMockMvc.perform(put("/api/savingsaccounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(savingsaccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Savingsaccount in the database
        List<Savingsaccount> savingsaccountList = savingsaccountRepository.findAll();
        assertThat(savingsaccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSavingsaccount() throws Exception {
        // Initialize the database
        savingsaccountRepository.saveAndFlush(savingsaccount);

        int databaseSizeBeforeDelete = savingsaccountRepository.findAll().size();

        // Get the savingsaccount
        restSavingsaccountMockMvc.perform(delete("/api/savingsaccounts/{id}", savingsaccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Savingsaccount> savingsaccountList = savingsaccountRepository.findAll();
        assertThat(savingsaccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Savingsaccount.class);
        Savingsaccount savingsaccount1 = new Savingsaccount();
        savingsaccount1.setId(1L);
        Savingsaccount savingsaccount2 = new Savingsaccount();
        savingsaccount2.setId(savingsaccount1.getId());
        assertThat(savingsaccount1).isEqualTo(savingsaccount2);
        savingsaccount2.setId(2L);
        assertThat(savingsaccount1).isNotEqualTo(savingsaccount2);
        savingsaccount1.setId(null);
        assertThat(savingsaccount1).isNotEqualTo(savingsaccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SavingsaccountDTO.class);
        SavingsaccountDTO savingsaccountDTO1 = new SavingsaccountDTO();
        savingsaccountDTO1.setId(1L);
        SavingsaccountDTO savingsaccountDTO2 = new SavingsaccountDTO();
        assertThat(savingsaccountDTO1).isNotEqualTo(savingsaccountDTO2);
        savingsaccountDTO2.setId(savingsaccountDTO1.getId());
        assertThat(savingsaccountDTO1).isEqualTo(savingsaccountDTO2);
        savingsaccountDTO2.setId(2L);
        assertThat(savingsaccountDTO1).isNotEqualTo(savingsaccountDTO2);
        savingsaccountDTO1.setId(null);
        assertThat(savingsaccountDTO1).isNotEqualTo(savingsaccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(savingsaccountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(savingsaccountMapper.fromId(null)).isNull();
    }
}
