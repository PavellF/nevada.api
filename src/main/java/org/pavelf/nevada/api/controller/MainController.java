package org.pavelf.nevada.api.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.FlushMode;
import org.hibernate.criterion.Example;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.persistence.domain.Access;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.Person;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.Token;
import org.pavelf.nevada.api.persistence.repository.LikeRepository;
import org.pavelf.nevada.api.persistence.repository.MessageRepository;
import org.pavelf.nevada.api.persistence.repository.PeopleRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.TokenRepository;
import org.pavelf.nevada.api.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class MainController {

	@Autowired
	private ProfileRepository principalRepository;
	
	@Autowired
	private ProfileService principalService;
	
	@Autowired
	private LikeRepository lr;
	
	@Autowired
	private MessageRepository mr;
	
	@Autowired
	private PeopleRepository pr;
	
	@GetMapping("/test")
	public ProfileDTO test() {
		Profile p = new Profile();
		p.setEmail("ada@lol.omg");
		p.setPassword(new char[] {'d', 'd'});
		p.setUsername("user");
		p.setSignDate(Instant.now());
		p.setAbout(null);
		//p.setPreferences(null);
		
		Person per = new Person();
		per.setGender("void");
		per.setFullName("John");
		per.setLocation("Mars");
		
		pr.save(per);
		p.setPerson(per);
		principalRepository.save(p);
		
		return null;
	}
	
	@GetMapping("/postmsg")
	@Transactional
	public Long like() {
		Message msg = new Message();
		msg.setAuthor(principalRepository.getOne(1));
		msg.setContent("hello world");
		msg.setDate(Instant.now());
		long id = mr.save(msg).getId();
		System.out.println("hello "+ id);
		return id;
	}
	
	@GetMapping("/likemsg/{id}")
	@Transactional(propagation=Propagation.REQUIRED)
	public Profile likemsg(@org.springframework.web.bind.annotation.PathVariable Integer id) {
		Like like =new Like();
		like.setDate(Instant.now());
		like.setLikedBy(principalRepository.getOne(1));
		like.setMessage(mr.getOne(id));
		lr.save(like);
		return null;
	}
	
	@Autowired 
	private TokenRepository tr;
	
	@GetMapping("/testtoken")
	@Transactional(propagation=Propagation.REQUIRED)
	public Profile testtoken() {
		Profile p = new Profile();
		p.setEmail("ada@lol.omg");
		p.setPassword(new char[] {'d', 'd'});
		p.setUsername("user");
		p.setSignDate(Instant.now());
		p.setAbout(null);
		//p.setPreferences(null);
		
		Person per = new Person();
		per.setGender("void");
		per.setFullName("John");
		per.setLocation("Mars");
		
		pr.save(per);
		p.setPerson(per);
		principalRepository.save(p);
		
		Token tok = new Token();
		tok.setAccountAccess(Access.READ);
		tok.setChatAccess(Access.READ);
		tok.setFriendsAccess(Access.READ);
		tok.setMessagesAccess(Access.READ);
		tok.setNotificationsAccess(Access.READ);
		tok.setPhotoAccess(Access.READ);
		tok.setProfile(principalRepository.getOne(1));
		tok.setStreamAccess(Access.READ);
		tok.setSuperToken(false);
		tok.setToken("123");
		tok.setUssuedBy(null);
		tok.setValidUntil(Instant.parse("2077-12-03T10:15:30.00Z"));
		tr.save(tok);
		
		
		
		
		
		return tr.findByToken("123").get().getProfile();
	}
	
	
}
