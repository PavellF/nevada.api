package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.VersionImpl;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.service.MessageService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

	private MessageService messageService;
	private TokenContext principal;
	
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".message+json", 
			APPLICATION_ACCEPT_PREFIX+".message+xml"})	
	public ResponseEntity<List<MessageDTO>> getMessages(
			@RequestParam("ids") String ids,
			@RequestHeader HttpHeaders headers) {
		
		final Version version = new VersionImpl(headers.getAccept().get(0).getParameter("version"));
		Set<Integer> messageIds = Pattern.compile("-")
				.splitAsStream(ids)
				.map(Integer::valueOf)
				.collect(Collectors.toSet());
		
		return null;
		
	}
	
	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".message+json", 
			APPLICATION_ACCEPT_PREFIX+".message+xml"})
	@Secured(access = Access.READ_WRITE, scope = { Scope.MESSAGE })
	public ResponseEntity<MessageDTO> postMessage(HttpEntity<MessageDTO> entity) {
		final MessageDTO posted = entity.getBody();
		final Version version = new VersionImpl(
				entity.getHeaders().getContentType().getParameter("version"));
		
		//check setPriority
		//check reply to
		//check user suspend
		Integer id = this.messageService.post(posted, version);
		
		return ResponseEntity.created(URI.create("/messages/" + id)).build();
	}
	
}
