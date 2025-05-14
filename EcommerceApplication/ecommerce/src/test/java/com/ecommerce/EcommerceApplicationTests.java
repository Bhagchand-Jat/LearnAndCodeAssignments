package com.ecommerce;

import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.handler.AuthenticationHandler;
import com.ecommerce.model.User;

@SpringBootTest
class EcommerceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Mock
    private RestTemplate restTemplate;

    @Mock
    private AuthenticationHandler authenticationHandler;

    @InjectMocks
    private EcommerceApplication application;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUpAndExitFlow() throws Exception {

        String input = "1\n3\n"; 
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        when(authenticationHandler.login()).thenReturn(Optional.of(new User()));

        EcommerceApplication.main(new String[]{});
    }


}
