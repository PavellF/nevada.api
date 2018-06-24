package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;

import java.util.List;

import org.pavelf.nevada.api.domain.Destination;
import org.pavelf.nevada.api.domain.TagDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes some endpoints that return {@code Tag} objects.
 * @author Pavel F.
 * @since 1.0
 * */
@RestController
public class TagController {

	private TagsService tagsService;
	
	@Autowired
	public TagController(TagsService tagsService) {
		this.tagsService = tagsService;
	}

	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".tag+json", 
			APPLICATION_ACCEPT_PREFIX+".tag+xml"},
			path = "/{destination}/{destination_id}/tags")	
	public ResponseEntity<List<TagDTO>> getMessagesForAuthor(
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			@RequestHeader(HttpHeaders.ACCEPT) Version version) {
		
		List<TagDTO> toReturn = null;
		
		if (destination == Destination.MESSAGE) {
			toReturn = tagsService.getAllForMessage(destinationId, version);
		} else if (destination == Destination.STREAM_POST) {
			toReturn = tagsService.getAllForStreamPost(destinationId, version);
		}
		
		return (toReturn == null) 
				? ResponseEntity.notFound().build()
				: ResponseEntity.ok(toReturn);
		
	}
}
