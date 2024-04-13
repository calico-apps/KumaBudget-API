package com.calicoapps.kumabudget.e2e;

import com.calicoapps.kumabudget.auth.dto.CredentialsRequest;
import com.calicoapps.kumabudget.auth.dto.RefreshTokenRequest;
import com.calicoapps.kumabudget.auth.dto.TokenResponse;
import com.calicoapps.kumabudget.auth.entity.Token;
import com.calicoapps.kumabudget.auth.repository.TokenRepository;
import com.calicoapps.kumabudget.auth.service.CredentialsService;
import com.calicoapps.kumabudget.common.Constants;
import com.calicoapps.kumabudget.common.Device;
import com.calicoapps.kumabudget.common.util.JsonUtil;
import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.ErrorJson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class E2eSecurityTest {

    private final String EMAIL = "my@email.com";
    private final String PASSWORD = "my_password";
    private final Device DEVICE = Device.TABLET;
    private final Device DEVICE2 = Device.MOBILE;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CredentialsService credentialsService;
    @Autowired
    private TokenRepository tokenRepository;

    private String credentialsRequest;

    private String accessToken;
    private String accessToken2;

    private String refreshToken;
    private String refreshToken2;


    @BeforeEach
    public void setup() {
        credentialsService.create(EMAIL, PASSWORD);
        credentialsRequest = JsonUtil.toJson(new CredentialsRequest(EMAIL, PASSWORD));
    }

    //
    @Test
    public void testSecurityScenario() throws Exception {
        log.info("User correctly created for test purpose");
        assertNotNull(credentialsService.findById(EMAIL));

        testMissingDeviceHeader_returnError();

        log.info("Login on Device 1");
        testLogin();

        testSecuredEndpointMissingBearerPrefix_returnError();
        testSecuredEndpointWrongToken_returnError();
        testSecuredEndpointWrongDevice1_returnError();
        testSecuredEndpointMissingAuthorizationHeader_returnError();

        testSecuredEndpointWithTokenDevice1_returnOK();

        log.info("Login on Device 2");
        testLoginOnDevice2();
        testSecuredEndpointWrongDevice2_returnError();

        testSecuredEndpointWithTokenDevice2_returnOK();

        testRefreshTokenWithWrongToken_returnError();
        testRefreshToken_returnOK();

        log.info("Set all tokens expired in DB");
        List<Token> tokens = tokenRepository.findAll();
        for (Token tk : tokens) {
            tk.setExpirationDateTime(LocalDateTime.now().minusHours(1));
        }
        tokenRepository.saveAll(tokens);

        testExpiredRefreshToken_returnError();
        testExpiredAccessToken_returnError();

        log.info("Set all tokens valid again in DB");
        tokens = tokenRepository.findAll();
        for (Token tk : tokens) {
            tk.setExpirationDateTime(LocalDateTime.now().plusHours(1));
        }
        tokenRepository.saveAll(tokens);
        testSecuredEndpointWithTokenDevice1_returnOK();

        log.info("Logout from Device 1");
        mockMvc
                .perform(put(Constants.API_URL + "auth/logout")
                        .header("Device" , DEVICE)
                        .header("Authorization" , "Bearer " + accessToken))
                .andExpect(status().isOk());

        testDeletedRefreshTokenOnDevice1_returnError();
        testDeletedAccessTokenOnDevice1_returnError();

        // we still should be able to login on Device 2
        testSecuredEndpointWithTokenDevice2_returnOK();

        log.info("Login again on Device 1");
        testLogin();

        log.info("Logout from all devices");
        mockMvc
                .perform(put(Constants.API_URL + "auth/logout/all")
                        .header("Device" , DEVICE)
                        .header("Authorization" , "Bearer " + accessToken))
                .andExpect(status().isOk());

        // all tokens are invalidated
        testDeletedRefreshTokenOnDevice1_returnError();
        testDeletedAccessTokenOnDevice1_returnError();
        testDeletedRefreshTokenOnDevice2_returnError();
        testDeletedAccessTokenOnDevice2_returnError();

    }

    private void testMissingDeviceHeader_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(post(Constants.API_URL + "auth/login")
                                // .header("Device", Device.WEB) <-- MISSING !
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(credentialsRequest))
                        .andExpect(status().isBadRequest())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.BAD_REQUEST_DEVICE);

        log.info("TEST OK - Missing device header");
    }

    private void testLogin() throws Exception {
        String tokenResponseString =
                mockMvc
                        .perform(post(Constants.API_URL + "auth/login")
                                .header("Device" , DEVICE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(credentialsRequest))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        TokenResponse tokenResponse = JsonUtil.fromJson(tokenResponseString, TokenResponse.class);
        accessToken = tokenResponse.getAccessToken();
        refreshToken = tokenResponse.getRefreshToken();

        assertNotNull(accessToken);
        assertNotNull(refreshToken);
        assertEquals(DEVICE.name(), tokenResponse.getForDevice());
        assertEquals(EMAIL, tokenResponse.getUserEmail());

        log.info("TEST OK - Login successful");
    }

    private void testLoginOnDevice2() throws Exception {
        String tokenResponseString =
                mockMvc
                        .perform(post(Constants.API_URL + "auth/login")
                                .header("Device" , DEVICE2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(credentialsRequest))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        TokenResponse tokenResponse = JsonUtil.fromJson(tokenResponseString, TokenResponse.class);
        accessToken2 = tokenResponse.getAccessToken();
        refreshToken2 = tokenResponse.getRefreshToken();

        assertNotNull(accessToken2);
        assertNotNull(refreshToken2);
        assertEquals(DEVICE2.name(), tokenResponse.getForDevice());
        assertEquals(EMAIL, tokenResponse.getUserEmail());

        log.info("TEST OK - Login on 2nd device successful");
    }

    private void testSecuredEndpointWrongToken_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(get("/testSecurity")
                                .header("Device" , DEVICE)
                                .header("Authorization" , "Bearer " + refreshToken)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Secured endpoint with wrong token");
    }

    private void testSecuredEndpointWrongDevice1_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(get("/testSecurity")
                                .header("Device" , DEVICE2)
                                .header("Authorization" , "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Secured endpoint with wrong device 1");
    }

    private void testSecuredEndpointWrongDevice2_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(get("/testSecurity")
                                .header("Device" , DEVICE)
                                .header("Authorization" , "Bearer " + accessToken2)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Secured endpoint with wrong device 2");
    }

    private void testSecuredEndpointMissingBearerPrefix_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(get("/testSecurity")
                                .header("Device" , DEVICE)
                                .header("Authorization" , accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Secured endpoint with malformed token header");
    }

    private void testSecuredEndpointMissingAuthorizationHeader_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(get("/testSecurity")
                                .header("Device" , DEVICE)
                                // .header("Authorization" , "Bearer " + accessToken) <-- MISSING
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Secured endpoint without token header");
    }

    private void testSecuredEndpointWithTokenDevice1_returnOK() throws Exception {
        mockMvc
                .perform(get("/testSecurity")
                        .header("Device" , DEVICE)
                        .header("Authorization" , "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        log.info("TEST OK - Secured endpoint with Device1 successful");
    }

    private void testSecuredEndpointWithTokenDevice2_returnOK() throws Exception {
        mockMvc
                .perform(get("/testSecurity")
                        .header("Device" , DEVICE2)
                        .header("Authorization" , "Bearer " + accessToken2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        log.info("TEST OK - Secured endpoint with Device2 successful");
    }

    private void testRefreshTokenWithWrongToken_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(post(Constants.API_URL + "auth/token/refresh")
                                .header("Device" , DEVICE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.toJson(new RefreshTokenRequest(accessToken)))) // <-- not the right token expected
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Refresh token with wrong token");
    }

    private void testRefreshToken_returnOK() throws Exception {
        String tokenResponseString =
                mockMvc
                        .perform(post(Constants.API_URL + "auth/token/refresh")
                                .header("Device" , DEVICE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.toJson(new RefreshTokenRequest(refreshToken))))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        TokenResponse tokenResponse = JsonUtil.fromJson(tokenResponseString, TokenResponse.class);

        assertNotNull(tokenResponse.getRefreshToken());
        assertNotNull(tokenResponse.getAccessToken());
        assertEquals(DEVICE.name(), tokenResponse.getForDevice());
        assertEquals(EMAIL, tokenResponse.getUserEmail());
        assertEquals(refreshToken, tokenResponse.getRefreshToken());
        assertNotEquals(accessToken, tokenResponse.getAccessToken());

        log.info("TEST OK - Refresh token successful");

        // Old access token should be revoked and invalidated
        String errorJsonString =
                mockMvc
                        .perform(get("/testSecurity")
                                .header("Device" , DEVICE)
                                .header("Authorization" , "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        accessToken = tokenResponse.getAccessToken();

        log.info("TEST OK - Secured endpoint with revoked old token");
    }

    private void testExpiredRefreshToken_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(post(Constants.API_URL + "auth/token/refresh")
                                .header("Device" , DEVICE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.toJson(new RefreshTokenRequest(refreshToken)))) // <-- right token but expired
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Refresh token with expired token");
    }

    private void testExpiredAccessToken_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(get("/testSecurity")
                                .header("Device" , DEVICE)
                                .header("Authorization" , "Bearer " + accessToken) // <-- right token but expired
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Secured endpoint with expired token");
    }

    private void testDeletedRefreshTokenOnDevice1_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(post(Constants.API_URL + "auth/token/refresh")
                                .header("Device" , DEVICE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.toJson(new RefreshTokenRequest(refreshToken)))) // <-- right token but deleted
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Refresh token with deleted Device1 token");
    }

    private void testDeletedAccessTokenOnDevice1_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(get("/testSecurity")
                                .header("Device" , DEVICE)
                                .header("Authorization" , "Bearer " + accessToken) // <-- right token but deleted
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Secured endpoint with deleted Device1 token");
    }

    private void testDeletedRefreshTokenOnDevice2_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(post(Constants.API_URL + "auth/token/refresh")
                                .header("Device" , DEVICE2)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtil.toJson(new RefreshTokenRequest(refreshToken2)))) // <-- right token but deleted
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Refresh token with deleted Device2 token");
    }

    private void testDeletedAccessTokenOnDevice2_returnError() throws Exception {
        String errorJsonString =
                mockMvc
                        .perform(get("/testSecurity")
                                .header("Device" , DEVICE)
                                .header("Authorization" , "Bearer " + accessToken2) // <-- right token but deleted
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized())
                        .andReturn().getResponse().getContentAsString();
        assertErrorJsonIsErrorCode(errorJsonString, ErrorCode.UNAUTHORIZED_TOKEN);

        log.info("TEST OK - Secured endpoint with deleted Device2 token");
    }

    private void assertErrorJsonIsErrorCode(String errorJsonString, ErrorCode errorCode) {
        ErrorJson errorJson = JsonUtil.fromJson(errorJsonString, ErrorJson.class);

        assertEquals(errorCode.getErrorCode(), errorJson.getErrorCode());
        assertEquals(errorCode.getErrorMessage(), errorJson.getErrorMessage());
    }

}
