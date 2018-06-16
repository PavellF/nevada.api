package org.pavelf.nevada.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.domain.VersionImpl;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.persistence.repository.AdvancedStreamPostRepository;
import org.pavelf.nevada.api.persistence.repository.LikeRepository;
import org.pavelf.nevada.api.persistence.repository.MessageRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.StreamPostRepository;
import org.pavelf.nevada.api.service.PageAndSort;
import org.pavelf.nevada.api.service.impl.PageAndSortImpl;
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
import org.springframework.util.MultiValueMap;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.Lists;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
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
    private AdvancedStreamPostRepository aspr;
    
    
    @Test
    
	public void testy() throws Exception {
    	controllerShouldAcceptPostedProfile();
    	Stream.generate(() -> {
    		StreamPost sp = new StreamPost();
    		sp.setAssociatedProfile(pr.getOne(1));
    		//sp.setAssociatedProfile(pr.getOne(1));
    		sp.setAuthorId(1);
    		sp.setCommentable(Visibility.ALL);
    		sp.setContent("lol " + (int) (Math.random() * 100));
    		sp.setDate(Instant.now());
    		sp.setVisibility(Visibility.ALL);
    		return sp;
    	}).limit(3).map(sp -> spr.save(sp).getId())
    	.forEach((Integer id) -> {
    		Like l = new Like();
    		l.setDate(Instant.now());
    		l.setLikedById(2);
    		l.setRating((short) 1);
    		l.setPost(spr.getOne(id));
    		lr.save(l);
    		l.setId(0);
    		l.setRating((short) 100);
    		lr.save(l);
    	});
    	
    	List<Visibility> levels = 
    			Stream.of(Visibility.FRIENDS, 
    					Visibility.ALL)
    			.collect(Collectors.toList());
    	Sort sort = Sort.by(Direction.ASC, "id");
		Pageable pageRequest = PageRequest.of(0, 15, sort);
    	
		
    	List<StreamPost> list = 
    			aspr.getAllPostsAssociatedWithProfileWithLikeInfo
    			(1, 2,levels, pageRequest);
    	System.out.println(list.toString());
    	
    	StreamPost a = spr.findById(2).get();
    	System.err.println(spr.countByAuthorIdAndId(1, 1));
    	System.err.println(port);
    	Thread.sleep(9000000);
    }
    
    /*
SELECT sp.id, sp.author, sp.date, sp.content, sp.rating, 
			sp.popularity, sp.priority, sp.visibility, sp.commentable, 
			sp.last_change, l.rating, l.id, l.by_user, l.date 
			FROM stream_post AS sp 
			INNER JOIN profile_has_stream_post AS phsp ON phsp.stream_post_id = sp.id
			 INNER JOIN like_stream_post AS lsp ON lsp.stream_post_id = sp.id
			 LEFT OUTER JOIN _likes AS l ON l.id = lsp.like_id AND l.by_user = 2
			WHERE phsp.profile_id = 1 AND sp.visibility IN ('ALL', 'FRIENDS');
     * */
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
