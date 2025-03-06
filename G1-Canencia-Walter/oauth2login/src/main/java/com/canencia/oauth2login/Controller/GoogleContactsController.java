/*package com.canencia.oauth2login.Controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contacts")
public class GoogleContactsController {

    private final String BASE_URL = "https://people.googleapis.com/v1/";
    private final RestTemplate restTemplate = new RestTemplate();

    // 🔹 Replace this with proper OAuth 2.0 token handling
    private String getAccessToken() {
        return "ya29.a0AeXRPp5QxVCcgyV2qYeAzOfb5o6kfTluHWQ680Knem0PdTJTaExzqVB9O4HSQfTvtqdK2CMrnMUuQ7Il3kw-tIP4RfMYU9rzjxmbstEW0iJApKOI23r4AsPclAXW8N1nwTJnqm1ZOEN4Ar82oNFl-OmnfkQWF2DY48IHDzBqaCgYKATgSARESFQHGX2MiaWm4EDEFI7oP-y8HguqbIQ0175";
    }

    // ✅ GET: List all contacts
    @GetMapping("/list")
    public ResponseEntity<String> getAllContacts() {
        String url = BASE_URL + "people/me/connections?personFields=names,emailAddresses,phoneNumbers";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // ✅ POST: Add a new contact
    @PostMapping("/add")
    public ResponseEntity<String> addContact(@RequestBody Map<String, String> contactDetails) {
        String url = BASE_URL + "people:createContact";  // ✅ Fixed URL

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request body
        Map<String, Object> contact = new HashMap<>();
        contact.put("names", List.of(Map.of(
                "givenName", contactDetails.get("firstName"),
                "familyName", contactDetails.get("lastName")
        )));
        contact.put("emailAddresses", List.of(Map.of(
                "value", contactDetails.get("email"),
                "type", "home"
        )));
        contact.put("phoneNumbers", List.of(Map.of(
                "value", contactDetails.get("phone"),
                "type", "mobile"
        )));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(contact, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("Error Response: " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // ✅ GET: Get a single contact by resourceName
    @GetMapping("/people/{resourceName}")
    public ResponseEntity<String> getContact(@PathVariable String resourceName) {
        String url = BASE_URL + "people/" + resourceName + "?personFields=names,emailAddresses,phoneNumbers";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // ✅ PATCH: Update a contact (Fixed from PUT → PATCH)
    @PatchMapping("/update/{resourceName}")
    public ResponseEntity<String> updateContact(@PathVariable String resourceName, @RequestBody Map<String, Object> contactDetails) {
        String url = BASE_URL + "people/" + resourceName + ":updateContact";  // Use Google API's update contact endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // You need to structure the request body to match the expected format for the Google People API
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("names", contactDetails.get("names"));
        requestBody.put("emailAddresses", contactDetails.get("emailAddresses"));
        requestBody.put("phoneNumbers", contactDetails.get("phoneNumbers"));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Make the PATCH request to update the contact
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    // ✅ DELETE: Remove a contact (Fixed URL)
    @RequestMapping(value = "/delete/{resourceName}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContact(@PathVariable String resourceName) {
        String url = BASE_URL + "people/" + resourceName;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error: " + e.getResponseBodyAsString());
        }
    }
}*/
