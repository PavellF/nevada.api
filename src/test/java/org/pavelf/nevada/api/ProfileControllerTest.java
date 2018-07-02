package org.pavelf.nevada.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pavelf.nevada.api.domain.GuestDTO;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.exception.ExceptionCase;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Attachment;
import org.pavelf.nevada.api.persistence.domain.Tag;
import org.pavelf.nevada.api.persistence.domain.Token;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.persistence.repository.AdvancedStreamPostRepository;
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
import org.springframework.core.ParameterizedTypeReference;
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
import static org.assertj.core.api.Assertions.*;
import org.pavelf.nevada.api.persistence.domain.Access;
import org.pavelf.nevada.api.persistence.domain.Application;
import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Tuple;

/**
 * Tests {@code ProfileController} endpoints.
 * @author Pavel F.
 * @since 1.0
 * */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
properties = "server.port=7777")
public class ProfileControllerTest {

	@LocalServerPort
    private int port;
	
	private static final String CONTENT_TYPE_XML = APPLICATION_ACCEPT_PREFIX + 
			".profile+xml;version=1.0";
	
	@Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ProfileRepository pr;
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private TokenRepository tokenRepository;
    
    /*
    @Autowired 
    private StreamPostRepository spr;
    
    @Autowired 
    private LikeRepository lr;
    
    @Autowired
    private TagRepository tr;
    
    @Autowired
    private AttachmentRepository sptr;
    
    @Autowired
    private AdvancedStreamPostRepository aspr;
    */
    
    
/*
	public Stream<Integer> populateStreamPosts(int count) {
    	return Stream.generate(() -> {
    		StreamPost sp = new StreamPost();
    		sp.setAuthorId(1);
    		sp.setCommentable(Visibility.ALL);
    		sp.setContent("lol " + (int) (Math.random() * 100));
    		sp.setDate(Instant.now());
    		sp.setVisibility(Visibility.ALL);
    		sp.setAssociatedProfile(1);
    		return sp;
    	}).limit(count).map(sp -> spr.save(sp).getId());
    }
    
    public Stream<Integer> createProfiles(int count) {
    	return Stream.generate(() -> {
    		Profile p = new Profile();
        	p.setEmail((int) (Math.random() * 10000) + "profile@test.com");
        	p.setPassword("secret".toCharArray());
        	p.setSignDate(Instant.now());
        	p.setUsername("Catch" + (int) (Math.random() * 10000));
        	return p;
    	}).limit(count).map(p -> pr.save(p).getId());
    }
    
    @Test
    public void shouldUpdateStreamPostRating() throws InterruptedException {
    	setInitialData();
    	List<Integer> sList = populateStreamPosts(3)
    			.collect(Collectors.toList());
    	
    	List<Integer> pList = createProfiles(9).collect(Collectors.toList());
    	
    	List<Integer> postRatingSum = new ArrayList<>();
    	
    	for (Integer sId : sList) {
    		
    		int currentPostRatingSum = 0;
    		
    		for (Integer pId : pList) {
    			
    			Like l = new Like();
    			l.setDate(Instant.now());
	    		l.setLikedById(pId);
	    		
	    		short thisProfileRating = (short) (Math.random() * 10);
	    		currentPostRatingSum += thisProfileRating;
	    		
	    		l.setRating(thisProfileRating);
	    		l.setLikedStreamPost(sId);
	    		lr.save(l);
    		}
    		
    		postRatingSum.add(currentPostRatingSum);
    	}
    	
    	aspr.updateRating();
    	
    	List<Integer> updatedRatings = spr.findAllById(sList).stream()
    			.map(StreamPost::getRating).collect(Collectors.toList());
    	
    	updatedRatings.forEach(System.out::println);
    	Assertions.assertThat(updatedRatings).containsAll(postRatingSum);
    	
    	Thread.sleep(9000000);
    }
    */
    
    /*
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
    */
    
    
    
    @Test
	public void controllerShouldAcceptPostedProfile() throws Exception {
    	String token = TestTokenBuilder.get()
    		.setAccountAccess(Access.READ_WRITE)
    		.build(pr, applicationRepository, tokenRepository).getToken();
    	
    	final String endpoint = "http://localhost:" + port + "/profiles";
		final ProfileDTO profile = ProfileDTO.builder()
				.withEmail("controllerShouldAcceptPostedProfile@testing.com")
				.withPassword(new char [] { 's', 'e', 'c', 'r', 'e', 't' })
				.withUsername("controllerShouldAcceptPostedProfile").build();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, 
				APPLICATION_ACCEPT_PREFIX+".profile+json;version=1.0");	
		headers.set(HttpHeaders.AUTHORIZATION, token);
		
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
				endpoint, HttpMethod.POST, new HttpEntity<>(profile, headers), 
				ExceptionCase.class);
		
		HttpHeaders returnedHeaders = response.getHeaders();
		ExceptionCase returnedBody = response.getBody();
		HttpStatus returnedStatus = response.getStatusCode();
		
		assertThat(returnedBody).isNull();
		assertThat(returnedStatus).isEqualTo(HttpStatus.CREATED);
		assertThat(returnedHeaders.getContentLength()).isEqualTo(0);
		assertThat(returnedHeaders.getLocation()).isNotNull();
	}
    
    @Test
	public void controllerShouldNotAcceptPostedEmptyBody() throws Exception {
    	String token = TestTokenBuilder.get()
    			.setAccountAccess(Access.READ_WRITE)
    			.build(pr, applicationRepository, tokenRepository)
    			.getToken();
    	
		final String endpoint = "http://localhost:" + port + "/profiles";
		final ProfileDTO profile = null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, 
				APPLICATION_ACCEPT_PREFIX+".profile+json;version=1.0");	
		headers.set(HttpHeaders.AUTHORIZATION, token);
		
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
				endpoint, HttpMethod.POST, new HttpEntity<>(profile, headers), 
				ExceptionCase.class);
		
		HttpHeaders returnedHeaders = response.getHeaders();
		ExceptionCase returnedBody = response.getBody();
		HttpStatus returnedStatus = response.getStatusCode();
		
		assertThat(returnedBody).isNotNull();
		assertThat(returnedStatus).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(returnedHeaders.getLocation()).isNull();
	}
    
    @Test
	public void controllerShouldNotAcceptMalformedBody() throws Exception {
    	String token = TestTokenBuilder.get()
    			.setAccountAccess(Access.READ_WRITE)
    			.build(pr, applicationRepository, tokenRepository)
    			.getToken();
		final String endpoint = "http://localhost:" + port + "/profiles";
		final ProfileDTO profile = ProfileDTO.builder()
				.withEmail("acceptable@email.com")
				.withUsername("No password").build();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, 
				APPLICATION_ACCEPT_PREFIX+".profile+json;version=1.0");	
		headers.set(HttpHeaders.AUTHORIZATION, token);
		
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
				endpoint, HttpMethod.POST, new HttpEntity<>(profile, headers), 
				ExceptionCase.class);
		
		HttpHeaders returnedHeaders = response.getHeaders();
		ExceptionCase returnedBody = response.getBody();
		HttpStatus returnedStatus = response.getStatusCode();
		
		assertThat(returnedBody).isNotNull();
		assertThat(returnedBody.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(returnedStatus).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(returnedHeaders.getLocation()).isNull();
	}
   
   	@Test
	public void controllerShouldReturnPostedProfileWithNoSensetiveInfo() throws Exception {
   		String token = TestTokenBuilder.get()
			.setAccountAccess(Access.READ_WRITE)
			.build(pr, applicationRepository, tokenRepository)
			.getToken();
   		
   		ProfileDTO profile = ProfileDTO.builder()
				.withEmail("controllerShouldReturnPostedProfile@testing.com")
				.withPassword("secret".toCharArray())
				.withUsername("controllerShouldReturnPostedProfile").build();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, 
				APPLICATION_ACCEPT_PREFIX+".profile+xml;version=1.0");	
		headers.set(HttpHeaders.AUTHORIZATION, token);
		
		ResponseEntity<ProfileDTO> response = restTemplate.exchange(
				"http://localhost:" + port + "/profiles", 
				HttpMethod.POST, 
				new HttpEntity<>(profile, headers), 
				ProfileDTO.class);
		
		headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, 
					APPLICATION_ACCEPT_PREFIX+".profile+xml;version=1.0");	
		headers.set(HttpHeaders.AUTHORIZATION, null);
		
		final String URLToGet = "http://localhost:" + port + "/profile/" + 
				response.getHeaders().getLocation(); 
		
		response = restTemplate.exchange(
				URLToGet, 
				HttpMethod.GET, 
				new HttpEntity<>(profile, headers), 
				ProfileDTO.class);
		
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getEmail()).isNull();
		assertThat(response.getBody().getId()).isNotNull();
   	}
   	
	private String getBaseURL() {
		return "http://localhost:" + port;
	}
   	
	@Test
	public void controllerShouldReturnPostedProfileWithSensetiveInfo() 
			throws Exception {
		Integer createdProfileId = sendProfile();
		String URLToGet = getBaseURL() + "/profile/" + createdProfileId; 
		
		String createdProfileToken = TestTokenBuilder.get()
			.withHolder(createdProfileId)
			.build(pr, applicationRepository, tokenRepository).getToken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, CONTENT_TYPE_XML);	
		headers.set(HttpHeaders.AUTHORIZATION, createdProfileToken);
		
		ResponseEntity<ProfileDTO> response = restTemplate.exchange(
				URLToGet, 
				HttpMethod.GET, 
				new HttpEntity<>(null, headers), 
				ProfileDTO.class);
		
		ProfileDTO body = response.getBody();
		
		assertThat(body).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(body.getEmail()).isNotNull();
		assertThat(body.getId()).isNotNull();
   	}
   	
	@Test
	public void controllerShouldReturnPostedProfileAndRegisterGuest() 
			throws Exception {
		Token tokenToCreateProfile = TestTokenBuilder.get()
				.setAccountAccess(Access.READ_WRITE)
				.build(pr, applicationRepository, tokenRepository);
			
		Integer createdProfileId = sendProfile(tokenToCreateProfile.getToken());
		
		final String createdUserToken = TestTokenBuilder.get()
				.withHolder(createdProfileId)
				.build(pr, applicationRepository, tokenRepository).getToken();
		
		List<GuestDTO> shouldBeZeroGuests = 
				getGuestsForProfile(createdProfileId, createdUserToken);
		
		final String URLToGet = getBaseURL() + "/profile/" + createdProfileId; 
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, CONTENT_TYPE_XML);	
		headers.set(HttpHeaders.AUTHORIZATION, tokenToCreateProfile.getToken());
		
		 ResponseEntity<ProfileDTO> response = restTemplate.exchange(
				URLToGet, 
				HttpMethod.GET, 
				new HttpEntity<>(null, headers), 
				ProfileDTO.class);
		
		List<GuestDTO> shouldBeOneGuest = 
				getGuestsForProfile(createdProfileId, createdUserToken);
		
		final ProfileDTO body = response.getBody();
		
		assertThat(body).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(body.getEmail()).isNull();
		assertThat(body.getId()).isNotNull();
		assertThat(shouldBeZeroGuests).isEmpty();
		assertThat(shouldBeOneGuest.size()).isEqualTo(1);
		
		final GuestDTO guest = shouldBeOneGuest.get(0);
		
		assertThat(guest.getToProfile()).isEqualTo(createdProfileId);
		assertThat(guest.getWhoId()).isEqualTo(tokenToCreateProfile.getBelongsToProfile());

   	}
	
	@Test
	public void controllerShouldReturnPostedProfiles() throws Exception {
		final Token token = TestTokenBuilder.get()
			.setAccountAccess(Access.READ_WRITE)
			.build(pr, applicationRepository, tokenRepository);
		final int count = 16;
		
		Set<String> createdProfiles = sendSeveralProfiles(count, token.getToken());
		String ids = createdProfiles.stream().reduce("", 
				(a,b) -> a.concat("-").concat(b));
		ids = ids.substring(1);
		String URLToGet = "http://localhost:" + port + "/profiles/" + ids; 
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, CONTENT_TYPE_XML);	
		headers.set(HttpHeaders.AUTHORIZATION, token.getToken());
		
		ResponseEntity<ProfileDTO[]> response = restTemplate.exchange(
				URLToGet, 
				HttpMethod.GET, 
				new HttpEntity<>(null, headers), 
				ProfileDTO[].class);
		
		
		ProfileDTO[] body = response.getBody();
		
		assertThat(body).isNotNull();
		
		List<Integer> responceIds = Stream.of(body)
				.map(ProfileDTO::getId).collect(Collectors.toList());
		List<Integer> initialIds = createdProfiles.stream()
				.map(Integer::valueOf).collect(Collectors.toList());
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(body.length).isEqualTo(count);
		assertThat(responceIds).containsExactlyInAnyOrderElementsOf(initialIds);

   	}
	
	@Test
	public void controllerShouldNotHandleRandomStringValueAsIds() 
			throws Exception {
		String URLToGet = "http://localhost:" + port + "/profiles/lol"; 
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, CONTENT_TYPE_XML);	
		headers.set(HttpHeaders.ACCEPT_LANGUAGE, "fr");
		
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
				URLToGet, 
				HttpMethod.GET, 
				new HttpEntity<>(null, headers), 
				ExceptionCase.class);
		
		ExceptionCase body = response.getBody();
		
		assertThat(body).isNotNull();
		assertThat(body.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(body.getCode()).isEqualTo(21);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void controllerShouldNotHandleMoreThan100Ids() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, CONTENT_TYPE_XML);	
		headers.set(HttpHeaders.ACCEPT_LANGUAGE, "fr");
		
		final int count = 166;
		String ids = Stream
				.generate(() -> String.valueOf((int) (Math.random() * 1000)))
				.limit(count)
				.reduce("", (a, b) -> a.concat("-").concat(b));
		
		ids = ids.substring(1);
		
		String URLToGet = "http://localhost:" + port + "/profiles/" + ids; 
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
				URLToGet, 
				HttpMethod.GET, 
				new HttpEntity<>(null, headers), 
				ExceptionCase.class);
		
		ExceptionCase body = response.getBody();
		
		assertThat(body).isNotNull();
		assertThat(body.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(body.getCode()).isEqualTo(11);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

   	}
	
	@Test
	public void controllerShouldReturnExceptionWhenTryingToUpdateEmptyBody() 
			throws Exception {
    	Token token = TestTokenBuilder.get()
    		.setAccountAccess(Access.READ_WRITE)
    		.build(pr, applicationRepository, tokenRepository);
    	
    	final String endpoint = "http://localhost:" + port + "/profiles";
		HttpHeaders headers = new HttpHeaders();
		
		headers.set(HttpHeaders.CONTENT_TYPE, 
				APPLICATION_ACCEPT_PREFIX+".profile+json;version=1.0");
		
		headers.set(HttpHeaders.AUTHORIZATION, token.getToken());
		
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
				endpoint, 
				HttpMethod.PUT, 
				new HttpEntity<>(null, headers), 
				ExceptionCase.class);
		
		ExceptionCase returnedBody = response.getBody();
		
		assertThat(returnedBody).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(returnedBody.getCode()).isEqualTo(12);
		assertThat(returnedBody.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void controllerShouldReturnExceptionWhenTryingToUpdateWEmptyId() 
			throws Exception {
    	Token token = TestTokenBuilder.get()
    		.setAccountAccess(Access.READ_WRITE)
    		.build(pr, applicationRepository, tokenRepository);
    	
    	final String endpoint = "http://localhost:" + port + "/profiles";
		HttpHeaders headers = new HttpHeaders();
		
		headers.set(HttpHeaders.CONTENT_TYPE, 
				APPLICATION_ACCEPT_PREFIX+".profile+json;version=1.0");
		
		headers.set(HttpHeaders.AUTHORIZATION, token.getToken());
		
		ProfileDTO toUpdate = ProfileDTO.builder()
				.withId(null)
				.withEmail("updated@upd.com")
				.build();
		
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
				endpoint, 
				HttpMethod.PUT, 
				new HttpEntity<>(toUpdate, headers), 
				ExceptionCase.class);
		
		ExceptionCase returnedBody = response.getBody();
		
		assertThat(returnedBody).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(returnedBody.getCode()).isEqualTo(5);
		assertThat(returnedBody.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void controllerShouldNotAcceptWhenTryingChangePopularity() 
			throws Exception {
    	Token token = TestTokenBuilder.get()
    		.setAccountAccess(Access.READ_WRITE)
    		.build(pr, applicationRepository, tokenRepository);
    	
    	final String endpoint = "http://localhost:" + port + "/profiles";
    	final Integer profileToUpdateId = sendProfile(token.getToken());
    	final Token tokenForUpdatedProfile = TestTokenBuilder.get()
    			.withHolder(profileToUpdateId)
        		.setAccountAccess(Access.READ_WRITE)
        		.build(pr, applicationRepository, tokenRepository);
		final HttpHeaders headers = new HttpHeaders();
		
		headers.set(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_XML);
		headers.set(HttpHeaders.AUTHORIZATION, tokenForUpdatedProfile.getToken());
		
		ProfileDTO toUpdate = ProfileDTO.builder()
				.withId(profileToUpdateId)
				.withPopularity(9000)
				.build();
		
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
				endpoint, 
				HttpMethod.PUT, 
				new HttpEntity<>(toUpdate, headers), 
				ExceptionCase.class);
		
		ExceptionCase returnedBody = response.getBody();
		
		assertThat(returnedBody).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(returnedBody.getCode()).isEqualTo(10);
		assertThat(returnedBody.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void controllerShouldNotAcceptWhenTryingUpdateNotOwnedEntity() 
			throws Exception {
    	String tokenToCreateProfile = TestTokenBuilder.get()
    		.setAccountAccess(Access.READ_WRITE)
    		.build(pr, applicationRepository, tokenRepository).getToken();
    	
    	final String endpoint = "http://localhost:" + port + "/profiles";
    	final Integer profileToUpdateId = sendProfile(tokenToCreateProfile);
		final HttpHeaders headers = new HttpHeaders();
		
		headers.set(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_XML);
		headers.set(HttpHeaders.AUTHORIZATION, tokenToCreateProfile);
		
		ProfileDTO toUpdate = ProfileDTO.builder()
				.withId(profileToUpdateId)
				.build();
		
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
				endpoint, 
				HttpMethod.PUT, 
				new HttpEntity<>(toUpdate, headers), 
				ExceptionCase.class);
		
		ExceptionCase returnedBody = response.getBody();
		
		assertThat(returnedBody).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(returnedBody.getCode()).isEqualTo(10);
		assertThat(returnedBody.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void controllerShouldUpdatePopularityWhenSuper() 
			throws Exception {
    	Token token = TestTokenBuilder.get()
    			.setSuperToken(true)
    			.setAccountAccess(Access.READ_WRITE)
    			.build(pr, applicationRepository, tokenRepository);
    	
    	final String endpoint = "http://localhost:" + port + "/profiles";
    	final Integer profileToUpdateId = sendProfile(token.getToken());
		HttpHeaders headers = new HttpHeaders();
		
		headers.set(HttpHeaders.CONTENT_TYPE, 
				APPLICATION_ACCEPT_PREFIX+".profile+json;version=1.0");
		
		headers.set(HttpHeaders.AUTHORIZATION, token.getToken());
		
		ProfileDTO toUpdate = ProfileDTO.builder()
				.withId(profileToUpdateId)
				.withEmail("updated@upd.com")
				.withPopularity(9000)
				.build();
		
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
				endpoint, 
				HttpMethod.PUT, 
				new HttpEntity<>(toUpdate, headers), 
				ExceptionCase.class);
		
		assertThat(response.getBody()).isNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		String updatedToken = TestTokenBuilder.get()
    			.withHolder(profileToUpdateId)
    			.setAccountAccess(Access.READ_WRITE)
    			.build(pr, applicationRepository, tokenRepository).getToken();
		
		ProfileDTO updated = receiveProfileById(profileToUpdateId, updatedToken);
		
		assertThat(updated).isNotNull();
		assertThat(updated.getPopularity()).isEqualTo(9000);
		assertThat(updated.getEmail()).isEqualTo("updated@upd.com");
	}
	
	protected ProfileDTO receiveProfileById(int id, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, CONTENT_TYPE_XML);
		headers.set(HttpHeaders.AUTHORIZATION, token);
		
		return restTemplate.exchange(
				"http://localhost:" + port + "/profile/" + id, 
				HttpMethod.GET, 
				new HttpEntity<>(null, headers), 
				ProfileDTO.class).getBody();
	}
	
	protected Integer sendProfile(String token) {
		final String username = (int) (Math.random() * 10000) + "sendProfile";
		final ProfileDTO profile = ProfileDTO.builder()
				.withEmail(username + "@testing.com")
				.withPassword(new char [] { 's', 'e', 'c', 'r', 'e', 't' })
				.withUsername(username).build();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, 
				APPLICATION_ACCEPT_PREFIX+".profile+json;version=1.0");	
		headers.set(HttpHeaders.AUTHORIZATION, token);
		
		ResponseEntity<ProfileDTO> response = restTemplate.exchange(
				"http://localhost:" + port + "/profiles", 
				HttpMethod.POST, 
				new HttpEntity<>(profile, headers), 
				ProfileDTO.class);
		
		return Integer.valueOf(response.getHeaders().getLocation().toString());
	}
	
	protected Integer sendProfile() {
		String tokenToCreateProfile = TestTokenBuilder.get()
				.setAccountAccess(Access.READ_WRITE)
				.build(pr, applicationRepository, tokenRepository)
				.getToken();
			
		return sendProfile(tokenToCreateProfile);
	}
	
	protected Set<String> sendSeveralProfiles(int count, String token) 
			throws InterruptedException {
		
		final Set<String> ids = new HashSet<>();
 		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_XML);	
		headers.set(HttpHeaders.AUTHORIZATION, token);
		
		for (int i = 0; i < count; i++) {
			
			ProfileDTO profile = ProfileDTO.builder()
				.withEmail(i + "stClone@testing.com")
				.withPassword("secret".toCharArray())
				.withUsername(i + "clone").build();
			
			ResponseEntity<ProfileDTO> response = restTemplate.exchange(
					getBaseURL() + "/profiles", 
					HttpMethod.POST, 
					new HttpEntity<>(profile, headers), 
					ProfileDTO.class);
			
			ids.add(response.getHeaders().getLocation().toString());
			Thread.sleep(1000);
		}
		
		return ids;
	}
	
	protected List<GuestDTO> getGuestsForProfile(int profileId, String token) {
		final String contentType = APPLICATION_ACCEPT_PREFIX + 
				".guest+json;version=1.0";
		final String URLToGet = "http://localhost:" + port + "/profile/" 
				+ profileId + "/guests"; 
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, contentType);	
		headers.set(HttpHeaders.AUTHORIZATION, token);
		
		ResponseEntity<GuestDTO[]> response = restTemplate.exchange(
				URLToGet, 
				HttpMethod.GET, 
				new HttpEntity<>(null, headers), 
				GuestDTO[].class);
		
		return Arrays.asList(response.getBody());
	}
   	
	
}
