package io.sevcik.csdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sevcik.csdemo.models.Smoothie;
import io.sevcik.csdemo.payload.response.JwtResponse;
import io.sevcik.csdemo.repositories.SmoothieRepository;
import io.sevcik.csdemo.repositories.UserRepository;
import io.sevcik.csdemo.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CsdemoApplication.class)
@AutoConfigureMockMvc
class CsdemoApplicationTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SmoothieRepository smoothieRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String testUser = "testuser";
    private static final String testAdmin = "testadmin";
    private static final String testPassword = "abc123";
    private static final String testPasswordEncrypted = "$2a$10$YVsVTCY2a5Yr3Wmw1ZZlue6GIYlA3xDR.jgXFq2uWkIMKLNAJjzNq";
    void createTestUsers() {
        deleteTestUsers();
        User user = new User(testUser, testPasswordEncrypted, false);
        User admin = new User(testAdmin, testPasswordEncrypted, true);
        userRepository.save(user);
        userRepository.save(admin);
    }
    void deleteTestUsers() {
        User user = userRepository.findByUsername(testUser).orElse(null);
        if (user != null)
            userRepository.delete(user);
        User userAdmin = userRepository.findByUsername(testAdmin).orElse(null);
        if (userAdmin != null)
            userRepository.delete(userAdmin);
    }
    @Test
    void failedLoginUnknownUser() throws Exception {
        createTestUsers();
        String body = "{\"username\":\"aaacd\", \"password\":\"pwd123}\"";
        mvc.perform(
                post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is4xxClientError());
        deleteTestUsers();
    }

    @Test
    void failedLoginWrongPwd() throws Exception {
        createTestUsers();
        String body = "{\"username\":\"testuser\", \"password\":\"pwd123}\"" ;
        mvc.perform(
                post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().is4xxClientError());
        deleteTestUsers();
    }

    @Test
    void successfulLogin() throws Exception {
        createTestUsers();
        String body = "{\"username\":\"" + testUser + "\", \"password\":\"" + testPassword + "\"}";
        //String body = "{\"username\":\"ejossev\", \"password\":\"abc123\"}" ;
        mvc.perform(
                post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().is2xxSuccessful());
        deleteTestUsers();
    }

    @Test
    void cannotCreateSmoothieNoAuth() throws Exception {
        String createBody = "{\"name\":\"TestSmoothie\", \"description\":\"super smoothie\", \"nutritions\":\"TBD\"}";
        mvc.perform(
                post("/api/smoothie")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createBody))
                .andExpect(status().is4xxClientError());
    }
    @Test
    void loginUserCannotCreateSmoothie() throws Exception {
        createTestUsers();
        String loginBody = "{\"username\":\"" + testUser + "\", \"password\":\"" + testPassword + "\"}";
        String createBody = "{\"name\":\"TestSmoothie\", \"description\":\"super smoothie\", \"nutritions\":\"TBD\"}";
        MvcResult rv = mvc.perform(
                post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginBody))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        JwtResponse response = objectMapper.readValue(rv.getResponse().getContentAsString(), JwtResponse.class);
        String authHeader = "Bearer " + response.getAccessToken();

        mvc.perform(
                post("/api/smoothie")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", authHeader)
                                .content(createBody))
                .andExpect(status().is4xxClientError());
        deleteTestUsers();
    }
    @Test
    void loginAdminCreateSmoothie() throws Exception {
        createTestUsers();
        String loginBody = "{\"username\":\"" + testAdmin + "\", \"password\":\"" + testPassword + "\"}";
        String createBody = "{\"name\":\"TestSmoothie\", \"description\":\"super smoothie\", \"nutritions\":\"TBD\"}";
        MvcResult rv = mvc.perform(
                post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginBody))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        JwtResponse response = objectMapper.readValue(rv.getResponse().getContentAsString(), JwtResponse.class);
        String authHeader = "Bearer " + response.getAccessToken();

        rv = mvc.perform(
                post("/api/smoothie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authHeader)
                        .content(createBody))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Cleanup
        Smoothie smoothie = smoothieRepository.findByName("TestSmoothie").orElse(null);
        assertThat(smoothie != null);
        smoothieRepository.delete(smoothie);
        deleteTestUsers();
    }

    @Test
    void loginAdminCreateSmoothie2ndSmoothieWithSameNameFails() throws Exception {
        createTestUsers();
        String loginBody = "{\"username\":\"" + testAdmin + "\", \"password\":\"" + testPassword + "\"}";
        String createBody = "{\"name\":\"TestSmoothie\", \"description\":\"super smoothie\", \"nutritions\":\"TBD\"}";
        MvcResult rv = mvc.perform(
                post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginBody))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        JwtResponse response = objectMapper.readValue(rv.getResponse().getContentAsString(), JwtResponse.class);
        String authHeader = "Bearer " + response.getAccessToken();

        mvc.perform(
                post("/api/smoothie")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", authHeader)
                                .content(createBody))
                .andExpect(status().is2xxSuccessful());

        mvc.perform(
                post("/api/smoothie")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", authHeader)
                                .content(createBody))
                .andExpect(status().is4xxClientError());


        // Cleanup
        Smoothie smoothie = smoothieRepository.findByName("TestSmoothie").orElse(null);
        assertThat(smoothie != null);
        smoothieRepository.delete(smoothie);
        deleteTestUsers();
    }

}
