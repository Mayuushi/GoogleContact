package com.canencia.oauth2login.Controller;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final String BASE_URL = "https://people.googleapis.com/v1/";
    private final String TOKEN =
            "ya29.a0AeXRPp5QxVCcgyV2qYeAzOfb5o6kfTluHWQ680Knem0PdTJTaExzqVB9O4HSQfTvtqdK2CMrnMUuQ7Il3kw-tIP4RfMYU9rzjxmbstEW0iJApKOI23r4AsPclAXW8N1nwTJnqm1ZOEN4Ar82oNFl-OmnfkQWF2DY48IHDzBqaCgYKATgSARESFQHGX2MiaWm4EDEFI7oP-y8HguqbIQ0175"; // Replace this with your OAuth token
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String home() {
        return "index";
    }

    // ✅ Display contacts list
    @GetMapping("/contacts")
    public String listContacts(Model model) {
        System.out.println("Accessed /contacts");
        String url = BASE_URL + "people/me/connections?personFields=names,emailAddresses,phoneNumbers";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        List<Map<String, Object>> contacts = new ArrayList<>();
        if (response.getBody() != null && response.getBody().containsKey("connections")) {
            List<Map<String, Object>> connections = (List<Map<String, Object>>) response.getBody().get("connections");
            for (Map<String, Object> person : connections) {
                Map<String, Object> contact = new HashMap<>();
                contact.put("id", person.get("resourceName"));
                contact.put("name", ((List<Map<String, String>>) person.get("names")).get(0).get("displayName"));
                contact.put("email", person.containsKey("emailAddresses") ? ((List<Map<String, String>>) person.get("emailAddresses")).get(0).get("value") : "N/A");
                contact.put("phone", person.containsKey("phoneNumbers") ? ((List<Map<String, String>>) person.get("phoneNumbers")).get(0).get("value") : "N/A");
                contacts.add(contact);
            }
        }
        model.addAttribute("contacts", contacts);
        return "contacts";
    }

    // ✅ Show the "Add Contact" form
    @GetMapping("/contacts/add")
    public String showAddContactForm() {
        System.out.println("Accessed contacts/add");
        return "add-contact"; // Make sure you have add-contact.html
    }

    // ✅ Handle form submission and create a contact in Google People API
    @PostMapping("/contacts/create")
    public String addContact(@RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) String phone) {

        String url = "https://people.googleapis.com/v1/people:createContact";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> contact = new HashMap<>();
        contact.put("names", List.of(Map.of("givenName", firstName, "familyName", lastName)));
        if (email != null && !email.isEmpty()) {
            contact.put("emailAddresses", List.of(Map.of("value", email, "type", "home")));
        }
        if (phone != null && !phone.isEmpty()) {
            contact.put("phoneNumbers", List.of(Map.of("value", phone, "type", "mobile")));
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(contact, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Error creating contact: " + e.getResponseBodyAsString());
        }

        return "redirect:/contacts"; // Redirect back to contacts list
    }

    // ✅ Show the "Edit Contact" form
    @GetMapping("/contacts/edit/{id}")
    public String editContact(@PathVariable("id") String id, Model model) {
        System.out.println("🔥 DEBUG: Edit request received for ID = " + id);

        String url = "https://people.googleapis.com/v1/" + id + "?personFields=names,emailAddresses,phoneNumbers";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getBody() == null) {
            System.out.println("❌ ERROR: Contact not found.");
            return "redirect:/contacts?error=notfound";
        }

        Map<String, Object> person = response.getBody();
        Map<String, String> contact = new HashMap<>();
        contact.put("id", id);
        contact.put("givenName", ((List<Map<String, String>>) person.get("names")).get(0).get("givenName"));
        contact.put("familyName", ((List<Map<String, String>>) person.get("names")).get(0).get("familyName"));
        contact.put("email", person.containsKey("emailAddresses") ? ((List<Map<String, String>>) person.get("emailAddresses")).get(0).get("value") : "");
        contact.put("phone", person.containsKey("phoneNumbers") ? ((List<Map<String, String>>) person.get("phoneNumbers")).get(0).get("value") : "");

        System.out.println("✅ DEBUG: Contact Data = " + contact);

        model.addAttribute("contact", contact);
        return "edit-contact";  // Ensure this template exists
    }

    // ✅ Update the contact after the form submission
    @PatchMapping("/contacts/update/{id}")
    public String updateContact(@PathVariable String id,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String phone) {

        String url = "https://people.googleapis.com/v1/" + id + ":updateContact"; // Correct Google API URL for PATCH request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the updated contact details to send in the PATCH request
        Map<String, Object> contact = new HashMap<>();
        contact.put("names", List.of(Map.of("givenName", firstName, "familyName", lastName)));
        if (email != null && !email.isEmpty()) {
            contact.put("emailAddresses", List.of(Map.of("value", email, "type", "home")));
        }
        if (phone != null && !phone.isEmpty()) {
            contact.put("phoneNumbers", List.of(Map.of("value", phone, "type", "mobile")));
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(contact, headers);

        try {
            // Send the PATCH request to update the contact
            restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);
            return "redirect:/contacts";  // Redirect back to the contacts list after successful update
        } catch (HttpClientErrorException e) {
            System.out.println("Error updating contact: " + e.getResponseBodyAsString());
            return "redirect:/contacts?error=updatefailed";  // Redirect with an error if something goes wrong
        }
    }

    // ✅ Delete a contact
    // ✅ Delete a contact
    @PostMapping("/contacts/delete/{id}")
    public String deleteContact(@PathVariable("id") String id, Model model) {
        System.out.println("🔴 Attempting to delete contact with ID: " + id);

        try {
            String url = BASE_URL + id + ":deleteContact"; // Google API delete contact endpoint

            // Create an HTTP request with authorization header
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + TOKEN);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            // Send a DELETE request to the Google API
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                System.out.println("✅ Contact deleted successfully.");
            } else {
                System.out.println("❌ Error deleting contact: " + response.getBody());
            }

        } catch (HttpClientErrorException e) {
            System.out.println("❌ Error deleting contact: " + e.getResponseBodyAsString());
        }

        return "redirect:/contacts"; // Redirect to contacts list after deletion
    }


}

