package example.com.dictionarysearch.auth.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import example.com.dictionarysearch.auth.service.ApiKeyService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiKeyAuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApiKeyService apiKeyService;

    @Test
    void shouldReturnUnauthorizedWithoutApiKey() throws Exception {
        mockMvc.perform(get("/api/search"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWithInvalidApiKey() throws Exception {
        when(apiKeyService.isValid("wrong-key")).thenReturn(false);

        mockMvc.perform(get("/api/search")
                .header("X-API-KEY", "wrong-key"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnOkWithValidApiKey() throws Exception {
        when(apiKeyService.isValid("valid-key")).thenReturn(true);

        mockMvc.perform(get("/api/search")
                .header("X-API-KEY", "valid-key"))
                .andExpect(status().isOk());
    }
}
