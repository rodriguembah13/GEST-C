package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.CustomUser;
import com.ballack.com.repository.CustomUserRepository;
import com.ballack.com.service.CustomUserService;
import com.ballack.com.repository.search.CustomUserSearchRepository;
import com.ballack.com.web.rest.errors.ExceptionTranslator;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CustomUserResource REST controller.
 *
 * @see CustomUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class CustomUserResourceIntTest {

    private static final Boolean DEFAULT_ISADU_USER = false;
    private static final Boolean UPDATED_ISADU_USER = true;

    private static final Boolean DEFAULT_IS_VENDRE = false;
    private static final Boolean UPDATED_IS_VENDRE = true;

    private static final Boolean DEFAULT_IS_APPROV_STOCK = false;
    private static final Boolean UPDATED_IS_APPROV_STOCK = true;

    private static final Boolean DEFAULT_COMMANDER = false;
    private static final Boolean UPDATED_COMMANDER = true;

    private static final Boolean DEFAULT_IS_PRINT_FAC = false;
    private static final Boolean UPDATED_IS_PRINT_FAC = true;

    private static final Boolean DEFAULT_ISUPDATE_CMDE = false;
    private static final Boolean UPDATED_ISUPDATE_CMDE = true;

    private static final Boolean DEFAULT_ISUPDATE_STCK = false;
    private static final Boolean UPDATED_ISUPDATE_STCK = true;

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VIEW_VENTE = false;
    private static final Boolean UPDATED_VIEW_VENTE = true;

    private static final Boolean DEFAULT_VIEW_CMDE = false;
    private static final Boolean UPDATED_VIEW_CMDE = true;

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private CustomUserSearchRepository customUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomUserMockMvc;

    private CustomUser customUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomUserResource customUserResource = new CustomUserResource(customUserService);
        this.restCustomUserMockMvc = MockMvcBuilders.standaloneSetup(customUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomUser createEntity(EntityManager em) {
        CustomUser customUser = new CustomUser()
            .isaduUser(DEFAULT_ISADU_USER)
            .isVendre(DEFAULT_IS_VENDRE)
            .isApprovStock(DEFAULT_IS_APPROV_STOCK)
            .commander(DEFAULT_COMMANDER)
            .isPrintFac(DEFAULT_IS_PRINT_FAC)
            .isupdateCmde(DEFAULT_ISUPDATE_CMDE)
            .isupdateStck(DEFAULT_ISUPDATE_STCK)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .telephone(DEFAULT_TELEPHONE)
            .viewVente(DEFAULT_VIEW_VENTE)
            .viewCmde(DEFAULT_VIEW_CMDE);
        return customUser;
    }

    @Before
    public void initTest() {
        customUserSearchRepository.deleteAll();
        customUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomUser() throws Exception {
        int databaseSizeBeforeCreate = customUserRepository.findAll().size();

        // Create the CustomUser
        restCustomUserMockMvc.perform(post("/api/custom-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customUser)))
            .andExpect(status().isCreated());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeCreate + 1);
        CustomUser testCustomUser = customUserList.get(customUserList.size() - 1);
        assertThat(testCustomUser.isIsaduUser()).isEqualTo(DEFAULT_ISADU_USER);
        assertThat(testCustomUser.isIsVendre()).isEqualTo(DEFAULT_IS_VENDRE);
        assertThat(testCustomUser.isIsApprovStock()).isEqualTo(DEFAULT_IS_APPROV_STOCK);
        assertThat(testCustomUser.isCommander()).isEqualTo(DEFAULT_COMMANDER);
        assertThat(testCustomUser.isIsPrintFac()).isEqualTo(DEFAULT_IS_PRINT_FAC);
        assertThat(testCustomUser.isIsupdateCmde()).isEqualTo(DEFAULT_ISUPDATE_CMDE);
        assertThat(testCustomUser.isIsupdateStck()).isEqualTo(DEFAULT_ISUPDATE_STCK);
        assertThat(testCustomUser.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testCustomUser.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testCustomUser.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testCustomUser.isViewVente()).isEqualTo(DEFAULT_VIEW_VENTE);
        assertThat(testCustomUser.isViewCmde()).isEqualTo(DEFAULT_VIEW_CMDE);

        // Validate the CustomUser in Elasticsearch
        CustomUser customUserEs = customUserSearchRepository.findOne(testCustomUser.getId());
        assertThat(customUserEs).isEqualToComparingFieldByField(testCustomUser);
    }

    @Test
    @Transactional
    public void createCustomUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customUserRepository.findAll().size();

        // Create the CustomUser with an existing ID
        customUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomUserMockMvc.perform(post("/api/custom-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customUser)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCustomUsers() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);

        // Get all the customUserList
        restCustomUserMockMvc.perform(get("/api/custom-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].isaduUser").value(hasItem(DEFAULT_ISADU_USER.booleanValue())))
            .andExpect(jsonPath("$.[*].isVendre").value(hasItem(DEFAULT_IS_VENDRE.booleanValue())))
            .andExpect(jsonPath("$.[*].isApprovStock").value(hasItem(DEFAULT_IS_APPROV_STOCK.booleanValue())))
            .andExpect(jsonPath("$.[*].commander").value(hasItem(DEFAULT_COMMANDER.booleanValue())))
            .andExpect(jsonPath("$.[*].isPrintFac").value(hasItem(DEFAULT_IS_PRINT_FAC.booleanValue())))
            .andExpect(jsonPath("$.[*].isupdateCmde").value(hasItem(DEFAULT_ISUPDATE_CMDE.booleanValue())))
            .andExpect(jsonPath("$.[*].isupdateStck").value(hasItem(DEFAULT_ISUPDATE_STCK.booleanValue())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].viewVente").value(hasItem(DEFAULT_VIEW_VENTE.booleanValue())))
            .andExpect(jsonPath("$.[*].viewCmde").value(hasItem(DEFAULT_VIEW_CMDE.booleanValue())));
    }

    @Test
    @Transactional
    public void getCustomUser() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);

        // Get the customUser
        restCustomUserMockMvc.perform(get("/api/custom-users/{id}", customUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customUser.getId().intValue()))
            .andExpect(jsonPath("$.isaduUser").value(DEFAULT_ISADU_USER.booleanValue()))
            .andExpect(jsonPath("$.isVendre").value(DEFAULT_IS_VENDRE.booleanValue()))
            .andExpect(jsonPath("$.isApprovStock").value(DEFAULT_IS_APPROV_STOCK.booleanValue()))
            .andExpect(jsonPath("$.commander").value(DEFAULT_COMMANDER.booleanValue()))
            .andExpect(jsonPath("$.isPrintFac").value(DEFAULT_IS_PRINT_FAC.booleanValue()))
            .andExpect(jsonPath("$.isupdateCmde").value(DEFAULT_ISUPDATE_CMDE.booleanValue()))
            .andExpect(jsonPath("$.isupdateStck").value(DEFAULT_ISUPDATE_STCK.booleanValue()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()))
            .andExpect(jsonPath("$.viewVente").value(DEFAULT_VIEW_VENTE.booleanValue()))
            .andExpect(jsonPath("$.viewCmde").value(DEFAULT_VIEW_CMDE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomUser() throws Exception {
        // Get the customUser
        restCustomUserMockMvc.perform(get("/api/custom-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomUser() throws Exception {
        // Initialize the database
        customUserService.save(customUser);

        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();

        // Update the customUser
        CustomUser updatedCustomUser = customUserRepository.findOne(customUser.getId());
        updatedCustomUser
            .isaduUser(UPDATED_ISADU_USER)
            .isVendre(UPDATED_IS_VENDRE)
            .isApprovStock(UPDATED_IS_APPROV_STOCK)
            .commander(UPDATED_COMMANDER)
            .isPrintFac(UPDATED_IS_PRINT_FAC)
            .isupdateCmde(UPDATED_ISUPDATE_CMDE)
            .isupdateStck(UPDATED_ISUPDATE_STCK)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .telephone(UPDATED_TELEPHONE)
            .viewVente(UPDATED_VIEW_VENTE)
            .viewCmde(UPDATED_VIEW_CMDE);

        restCustomUserMockMvc.perform(put("/api/custom-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomUser)))
            .andExpect(status().isOk());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate);
        CustomUser testCustomUser = customUserList.get(customUserList.size() - 1);
        assertThat(testCustomUser.isIsaduUser()).isEqualTo(UPDATED_ISADU_USER);
        assertThat(testCustomUser.isIsVendre()).isEqualTo(UPDATED_IS_VENDRE);
        assertThat(testCustomUser.isIsApprovStock()).isEqualTo(UPDATED_IS_APPROV_STOCK);
        assertThat(testCustomUser.isCommander()).isEqualTo(UPDATED_COMMANDER);
        assertThat(testCustomUser.isIsPrintFac()).isEqualTo(UPDATED_IS_PRINT_FAC);
        assertThat(testCustomUser.isIsupdateCmde()).isEqualTo(UPDATED_ISUPDATE_CMDE);
        assertThat(testCustomUser.isIsupdateStck()).isEqualTo(UPDATED_ISUPDATE_STCK);
        assertThat(testCustomUser.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testCustomUser.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testCustomUser.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testCustomUser.isViewVente()).isEqualTo(UPDATED_VIEW_VENTE);
        assertThat(testCustomUser.isViewCmde()).isEqualTo(UPDATED_VIEW_CMDE);

        // Validate the CustomUser in Elasticsearch
        CustomUser customUserEs = customUserSearchRepository.findOne(testCustomUser.getId());
        assertThat(customUserEs).isEqualToComparingFieldByField(testCustomUser);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomUser() throws Exception {
        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();

        // Create the CustomUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomUserMockMvc.perform(put("/api/custom-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customUser)))
            .andExpect(status().isCreated());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomUser() throws Exception {
        // Initialize the database
        customUserService.save(customUser);

        int databaseSizeBeforeDelete = customUserRepository.findAll().size();

        // Get the customUser
        restCustomUserMockMvc.perform(delete("/api/custom-users/{id}", customUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean customUserExistsInEs = customUserSearchRepository.exists(customUser.getId());
        assertThat(customUserExistsInEs).isFalse();

        // Validate the database is empty
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomUser() throws Exception {
        // Initialize the database
        customUserService.save(customUser);

        // Search the customUser
        restCustomUserMockMvc.perform(get("/api/_search/custom-users?query=id:" + customUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].isaduUser").value(hasItem(DEFAULT_ISADU_USER.booleanValue())))
            .andExpect(jsonPath("$.[*].isVendre").value(hasItem(DEFAULT_IS_VENDRE.booleanValue())))
            .andExpect(jsonPath("$.[*].isApprovStock").value(hasItem(DEFAULT_IS_APPROV_STOCK.booleanValue())))
            .andExpect(jsonPath("$.[*].commander").value(hasItem(DEFAULT_COMMANDER.booleanValue())))
            .andExpect(jsonPath("$.[*].isPrintFac").value(hasItem(DEFAULT_IS_PRINT_FAC.booleanValue())))
            .andExpect(jsonPath("$.[*].isupdateCmde").value(hasItem(DEFAULT_ISUPDATE_CMDE.booleanValue())))
            .andExpect(jsonPath("$.[*].isupdateStck").value(hasItem(DEFAULT_ISUPDATE_STCK.booleanValue())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].viewVente").value(hasItem(DEFAULT_VIEW_VENTE.booleanValue())))
            .andExpect(jsonPath("$.[*].viewCmde").value(hasItem(DEFAULT_VIEW_CMDE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomUser.class);
        CustomUser customUser1 = new CustomUser();
        customUser1.setId(1L);
        CustomUser customUser2 = new CustomUser();
        customUser2.setId(customUser1.getId());
        assertThat(customUser1).isEqualTo(customUser2);
        customUser2.setId(2L);
        assertThat(customUser1).isNotEqualTo(customUser2);
        customUser1.setId(null);
        assertThat(customUser1).isNotEqualTo(customUser2);
    }
}
