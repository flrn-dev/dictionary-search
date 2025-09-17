package example.com.dictionarysearch.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import example.com.dictionarysearch.auth.model.ApiKey;

@Service
public class ApiKeyService {

    private final List<ApiKey> validKeys = List.of(
            new ApiKey("123456", "test-client", true)
    );

    public boolean isValid(String apiKey) {
        return validKeys.stream()
                .anyMatch(key -> key.isActive() && key.getKey().equals(apiKey));
    }
}
