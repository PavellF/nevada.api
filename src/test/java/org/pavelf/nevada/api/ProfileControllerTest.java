package org.pavelf.nevada.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.exception.ExceptionCase;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Attachment;
import org.pavelf.nevada.api.persistence.domain.Tag;
import org.pavelf.nevada.api.persistence.domain.Token;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.persistence.repository.ApplicationRepository;
import org.pavelf.nevada.api.persistence.repository.LikeRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.StreamPostRepository;
import org.pavelf.nevada.api.persistence.repository.AttachmentRepository;
import org.pavelf.nevada.api.persistence.repository.TagRepository;
import org.pavelf.nevada.api.persistence.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.assertj.core.api.Assertions;
import org.pavelf.nevada.api.persistence.domain.Access;
import org.pavelf.nevada.api.persistence.domain.Application;
import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Tuple;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
properties = "server.port=7777")
public class ProfileControllerTest {

	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired 
    private StreamPostRepository spr;
    
    @Autowired 
    private LikeRepository lr;
    
    @Autowired 
    private ProfileRepository pr;
    
    @Autowired
    private ApplicationRepository applicationRepository;
    	
    @Autowired
    private TagRepository tr;
    
    @Autowired
    private AttachmentRepository sptr;
    
    @Autowired
    private TokenRepository tokenRepository;
    
    
    public void setInitialData() {
    	Profile executor = new Profile();
    	executor.setEmail("tester@test.com");
    	executor.setPassword("secret".toCharArray());
    	executor.setSignDate(Instant.now());
    	executor.setUsername("Joe Tester");
    	pr.save(executor);
    	
    	Application application = new Application();
    	application.setAccessKey("random");
    	application.setBelongsTo(executor.getId());
    	application.setSince(Instant.now());
    	application.setTitle("Testing app");
    	applicationRepository.save(application);
    	
    	Token godToken = new Token();
    	godToken.setAccountAccess(Access.READ_WRITE);
    	godToken.setApplicationAccess(Access.READ_WRITE);
    	godToken.setBelongsToProfile(executor.getId());
    	godToken.setFriendsAccess(Access.READ_WRITE);
    	godToken.setIssuedBy(application.getId());
    	godToken.setMessagesAccess(Access.READ_WRITE);
    	godToken.setNotificationsAccess(Access.READ_WRITE);
    	godToken.setPersonInfoAccess(Access.READ_WRITE);
    	godToken.setPhotoAccess(Access.READ_WRITE);
    	godToken.setStreamAccess(Access.READ_WRITE);
    	godToken.setSuperToken(true);
    	godToken.setToken("testMD5");
    	godToken.setValidUntil(Instant.now().plusSeconds(9999999));
    	tokenRepository.save(godToken);
    	
    }
    
    @Test
    public void testy() throws Exception {
    	setInitialData();
    	controllerShouldAcceptPostedProfile();
    	Stream.generate(() -> {
    		StreamPost sp = new StreamPost();
    		sp.setAuthorId(1);
    		sp.setCommentable(Visibility.ALL);
    		sp.setContent("lol " + (int) (Math.random() * 100));
    		sp.setDate(Instant.now());
    		sp.setVisibility(Visibility.ALL);
    		sp.setAssociatedProfile(1);
    		return sp;
    	}).limit(3).map(sp -> spr.save(sp).getId())
    	.forEach((Integer id) -> {
    		Like l = new Like();
    		
    		l.setDate(Instant.now());
    		l.setLikedById(2);
    		l.setRating((short) 1);
    		l.setLikedStreamPost(id);
    		lr.save(l);
    		
    		l.setId(null);
    		l.setRating((short) 100);
    		lr.save(l);
    	});
    	
    	Tag tag = new Tag();
		tag.setName("wow");
		tr.save(tag);
		
		tr.save(tag);
		
		Attachment spTag = new Attachment();
		spTag.setToStreamPost(1);
		spTag.setTagName("wow");
		sptr.save(spTag);
		spTag.setId(0);
		spTag.setToStreamPost(2);
		sptr.save(spTag);
		
    	List<Visibility> levels = 
    			Stream.of(Visibility.FRIENDS, 
    					Visibility.ALL)
    			.collect(Collectors.toList());
    	Sort sort = Sort.by(Direction.ASC, "id");
		Pageable pageable = PageRequest.of(0, 15, sort);
    	
		List<Tuple> list1 = 
				spr.findAllByAuthorIdWithLikeInfo(1, levels, 2, pageable);
		
		list1.forEach((Tuple tuple) -> {
			System.out.println(tuple.get(0, StreamPost.class).getContent());
			System.out.println(tuple.get(1, Like.class).getRating());
		});
		
    	StreamPost a = spr.findById(2).get();
    	System.err.println(spr.countPostBelongAuthor(1, 1));
    	System.err.println(port);
    	Thread.sleep(9000000);
    }
    
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
		headers.set(HttpHeaders.AUTHORIZATION, "testMD5");
		
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
				endpoint, HttpMethod.POST, new HttpEntity<>(profile, headers), 
				ExceptionCase.class);
		
		HttpHeaders returnedHeaders = response.getHeaders();
		ExceptionCase returnedBody = response.getBody();
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
		headers.set(HttpHeaders.AUTHORIZATION, "testMD5");
		
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
