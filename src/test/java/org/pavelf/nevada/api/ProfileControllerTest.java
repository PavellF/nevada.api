package org.pavelf.nevada.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.repository.LikeRepository;
import org.pavelf.nevada.api.persistence.repository.MessageRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest {

	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
   @Test
	public void controllerShouldAcceptPostedProfile() throws Exception {
		final String endpoint = "http://localhost:" + port + "/profiles";
		ProfileDTO profile = ProfileDTO.builder()
				.withEmail("test@testing.com")
				.withPassword(new char [] { 's', 'e', 'c', 'r', 'e', 't' })
				.withUsername("test").build();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, 
				APPLICATION_ACCEPT_PREFIX+".profile+json;version=1.0");	
		headers.set(HttpHeaders.AUTHORIZATION, "fosof94nswf9wa");
		
		ResponseEntity<ProfileDTO> response = restTemplate.exchange(
				endpoint, HttpMethod.POST, new HttpEntity<>(profile, headers), ProfileDTO.class);
		
		HttpHeaders returnedHeaders = response.getHeaders();
		ProfileDTO returnedBody = response.getBody();
		HttpStatus returnedStatus = response.getStatusCode();
		
		Assertions.assertThat(returnedBody).isNull();
		Assertions.assertThat(returnedStatus).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(returnedHeaders.getContentLength()).isEqualTo(0);
		Assertions.assertThat(returnedHeaders.getLocation()).isNotNull();
	 }
   
   	@Test
	public void controllerShouldReturnPostedProfileWithNoSensetiveInfo() throws Exception {
   		final String endpoint = "http://localhost:" + port + "/profiles";
		ProfileDTO profile = ProfileDTO.builder()
				.withEmail("controllerShouldReturnPostedProfile@testing.com")
				.withPassword("secret".toCharArray())
				.withUsername("controllerShouldReturnPostedProfile").build();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, 
				APPLICATION_ACCEPT_PREFIX+".profile+xml;version=1.0");	
		headers.set(HttpHeaders.AUTHORIZATION, "fosof94nswf9wa");
		
		ResponseEntity<ProfileDTO> response = restTemplate.exchange(
				endpoint, HttpMethod.POST, new HttpEntity<>(profile, headers), ProfileDTO.class);
		
		headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, 
					APPLICATION_ACCEPT_PREFIX+".profile+xml;version=1.0");	
		headers.set(HttpHeaders.AUTHORIZATION, "fosof94nswf9wa");
		
		response = restTemplate.exchange(
				response.getHeaders().getLocation(), 
				HttpMethod.GET, 
				new HttpEntity<>(profile, headers), 
				ProfileDTO.class);
		
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().getEmail()).isNull();
		Assertions.assertThat(response.getBody().getId()).isNotNull();
   	}
	
   	
   	
   	
   	
   	
   	
}
