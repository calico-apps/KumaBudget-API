package com.calicoapps.kumabudget.e2e;

import com.calicoapps.kumabudget.common.util.JsonUtil;
import com.calicoapps.kumabudget.security.dto.LoginRequest;
import com.calicoapps.kumabudget.security.dto.TokenResponse;
import com.calicoapps.kumabudget.security.service.user.CredentialsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class E2eTest {

    private final String EMAIL = "email@email.com";
    private final String PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CredentialsService credentialsService;


//    @MockBean
//    private JwtService jwtService;

    //    @MockBean
//    private AuthenticationService authenticationService;
//
    @BeforeEach
    public void setup() {
        credentialsService.create(EMAIL, PASSWORD);
//        Authentication auth = new TestingAuthenticationToken("user" , null, "ROLE_USER");
//        mockStatic(AuthUtil.class);
//        when(AuthUtil.buildAuthentication(any(), anyString(), any())).thenReturn(auth);
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(auth);
//        SecurityContextHolder.setContext(securityContext);
//
//        when(jwtService.isTokenValidInDB(anyString())).thenReturn(true);
//        when(jwtService.isRefreshTokenValidInDB(anyString())).thenReturn(true);
//        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
    }

    //
//    @Test
    public void testYourEndpoint() throws Exception {
        LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);
        String tokenResponseString = mockMvc.perform(post("/api/auth/login").content(loginRequest.toString()))
                .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();
        TokenResponse tokenResponse = JsonUtil.fromJson(tokenResponseString, TokenResponse.class);

        System.out.println(tokenResponse);


//        mockMvc.perform(get("/testSecurity").header("Authorization" , "Bearer 123"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("OK passed"));
    }

}
