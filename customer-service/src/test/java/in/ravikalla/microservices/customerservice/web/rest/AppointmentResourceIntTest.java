package in.ravikalla.microservices.customerservice.web.rest;

import in.ravikalla.microservices.customerservice.CustomerserviceApp;

import in.ravikalla.microservices.customerservice.domain.Appointment;
import in.ravikalla.microservices.customerservice.repository.AppointmentRepository;
import in.ravikalla.microservices.customerservice.service.AppointmentService;
import in.ravikalla.microservices.customerservice.service.dto.AppointmentDTO;
import in.ravikalla.microservices.customerservice.service.mapper.AppointmentMapper;
import in.ravikalla.microservices.customerservice.web.rest.errors.ExceptionTranslator;
import in.ravikalla.microservices.customerservice.service.dto.AppointmentCriteria;
import in.ravikalla.microservices.customerservice.service.AppointmentQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static in.ravikalla.microservices.customerservice.web.rest.TestUtil.sameInstant;
import static in.ravikalla.microservices.customerservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AppointmentResource REST controller.
 *
 * @see AppointmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerserviceApp.class)
public class AppointmentResourceIntTest {

    private static final String DEFAULT_VISITORNAME = "AAAAAAAAAA";
    private static final String UPDATED_VISITORNAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private AppointmentRepository appointmentRepository;


    @Autowired
    private AppointmentMapper appointmentMapper;
    

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentQueryService appointmentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppointmentMockMvc;

    private Appointment appointment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppointmentResource appointmentResource = new AppointmentResource(appointmentService, appointmentQueryService);
        this.restAppointmentMockMvc = MockMvcBuilders.standaloneSetup(appointmentResource)
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
    public static Appointment createEntity(EntityManager em) {
        Appointment appointment = new Appointment()
            .visitorname(DEFAULT_VISITORNAME)
            .time(DEFAULT_TIME)
            .comments(DEFAULT_COMMENTS);
        return appointment;
    }

    @Before
    public void initTest() {
        appointment = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppointment() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);
        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate + 1);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getVisitorname()).isEqualTo(DEFAULT_VISITORNAME);
        assertThat(testAppointment.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testAppointment.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createAppointmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // Create the Appointment with an existing ID
        appointment.setId(1L);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkVisitornameIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setVisitorname(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setTime(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppointments() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList
        restAppointmentMockMvc.perform(get("/api/appointments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].visitorname").value(hasItem(DEFAULT_VISITORNAME.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }
    

    @Test
    @Transactional
    public void getAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", appointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appointment.getId().intValue()))
            .andExpect(jsonPath("$.visitorname").value(DEFAULT_VISITORNAME.toString()))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }

    @Test
    @Transactional
    public void getAllAppointmentsByVisitornameIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where visitorname equals to DEFAULT_VISITORNAME
        defaultAppointmentShouldBeFound("visitorname.equals=" + DEFAULT_VISITORNAME);

        // Get all the appointmentList where visitorname equals to UPDATED_VISITORNAME
        defaultAppointmentShouldNotBeFound("visitorname.equals=" + UPDATED_VISITORNAME);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByVisitornameIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where visitorname in DEFAULT_VISITORNAME or UPDATED_VISITORNAME
        defaultAppointmentShouldBeFound("visitorname.in=" + DEFAULT_VISITORNAME + "," + UPDATED_VISITORNAME);

        // Get all the appointmentList where visitorname equals to UPDATED_VISITORNAME
        defaultAppointmentShouldNotBeFound("visitorname.in=" + UPDATED_VISITORNAME);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByVisitornameIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where visitorname is not null
        defaultAppointmentShouldBeFound("visitorname.specified=true");

        // Get all the appointmentList where visitorname is null
        defaultAppointmentShouldNotBeFound("visitorname.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppointmentsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where time equals to DEFAULT_TIME
        defaultAppointmentShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the appointmentList where time equals to UPDATED_TIME
        defaultAppointmentShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where time in DEFAULT_TIME or UPDATED_TIME
        defaultAppointmentShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the appointmentList where time equals to UPDATED_TIME
        defaultAppointmentShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where time is not null
        defaultAppointmentShouldBeFound("time.specified=true");

        // Get all the appointmentList where time is null
        defaultAppointmentShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppointmentsByTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where time greater than or equals to DEFAULT_TIME
        defaultAppointmentShouldBeFound("time.greaterOrEqualThan=" + DEFAULT_TIME);

        // Get all the appointmentList where time greater than or equals to UPDATED_TIME
        defaultAppointmentShouldNotBeFound("time.greaterOrEqualThan=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where time less than or equals to DEFAULT_TIME
        defaultAppointmentShouldNotBeFound("time.lessThan=" + DEFAULT_TIME);

        // Get all the appointmentList where time less than or equals to UPDATED_TIME
        defaultAppointmentShouldBeFound("time.lessThan=" + UPDATED_TIME);
    }


    @Test
    @Transactional
    public void getAllAppointmentsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where comments equals to DEFAULT_COMMENTS
        defaultAppointmentShouldBeFound("comments.equals=" + DEFAULT_COMMENTS);

        // Get all the appointmentList where comments equals to UPDATED_COMMENTS
        defaultAppointmentShouldNotBeFound("comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where comments in DEFAULT_COMMENTS or UPDATED_COMMENTS
        defaultAppointmentShouldBeFound("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS);

        // Get all the appointmentList where comments equals to UPDATED_COMMENTS
        defaultAppointmentShouldNotBeFound("comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where comments is not null
        defaultAppointmentShouldBeFound("comments.specified=true");

        // Get all the appointmentList where comments is null
        defaultAppointmentShouldNotBeFound("comments.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAppointmentShouldBeFound(String filter) throws Exception {
        restAppointmentMockMvc.perform(get("/api/appointments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].visitorname").value(hasItem(DEFAULT_VISITORNAME.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAppointmentShouldNotBeFound(String filter) throws Exception {
        restAppointmentMockMvc.perform(get("/api/appointments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingAppointment() throws Exception {
        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment
        Appointment updatedAppointment = appointmentRepository.findById(appointment.getId()).get();
        // Disconnect from session so that the updates on updatedAppointment are not directly saved in db
        em.detach(updatedAppointment);
        updatedAppointment
            .visitorname(UPDATED_VISITORNAME)
            .time(UPDATED_TIME)
            .comments(UPDATED_COMMENTS);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(updatedAppointment);

        restAppointmentMockMvc.perform(put("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getVisitorname()).isEqualTo(UPDATED_VISITORNAME);
        assertThat(testAppointment.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testAppointment.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppointmentMockMvc.perform(put("/api/appointments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeDelete = appointmentRepository.findAll().size();

        // Get the appointment
        restAppointmentMockMvc.perform(delete("/api/appointments/{id}", appointment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appointment.class);
        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        Appointment appointment2 = new Appointment();
        appointment2.setId(appointment1.getId());
        assertThat(appointment1).isEqualTo(appointment2);
        appointment2.setId(2L);
        assertThat(appointment1).isNotEqualTo(appointment2);
        appointment1.setId(null);
        assertThat(appointment1).isNotEqualTo(appointment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentDTO.class);
        AppointmentDTO appointmentDTO1 = new AppointmentDTO();
        appointmentDTO1.setId(1L);
        AppointmentDTO appointmentDTO2 = new AppointmentDTO();
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
        appointmentDTO2.setId(appointmentDTO1.getId());
        assertThat(appointmentDTO1).isEqualTo(appointmentDTO2);
        appointmentDTO2.setId(2L);
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
        appointmentDTO1.setId(null);
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(appointmentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(appointmentMapper.fromId(null)).isNull();
    }
}
