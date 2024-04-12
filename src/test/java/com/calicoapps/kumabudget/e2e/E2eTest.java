package com.calicoapps.kumabudget.e2e;

import com.calicoapps.kumabudget.security.service.login.AuthenticationService;
import com.calicoapps.kumabudget.security.service.token.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
public class E2eTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private JwtService jwtService;
//    @MockBean
//    private AuthenticationService authenticationService;
//
//    @BeforeEach
//    public void setup() {
//        // Mock the authentication and authorization behavior
//        Authentication auth = new TestingAuthenticationToken("user" , null, "ROLE_USER");
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(auth);
//        SecurityContextHolder.setContext(securityContext);
//
//        when(jwtService.isTokenValidInDB(anyString())).thenReturn(true);
//        when(jwtService.isRefreshTokenValidInDB(anyString())).thenReturn(true);
//        when(jwtService.isTokenExpired(anyString())).thenReturn(false);
//        when(authenticationService.getAuthentication(any(), anyString(), any())).thenReturn(auth);
//    }
//
//    @Test
//    public void testYourEndpoint() throws Exception {
//        mockMvc.perform(get("/testSecurity"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("OK passed"));
//    }

}
