package com.canencia.oauth2login.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleContactsService {

    private static final String CONTACTS_API_URL = "https://people.googleapis.com/v1/people/me/connections?personFields=names,emailAddresses";

    public String getAllContacts(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(CONTACTS_API_URL, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}

