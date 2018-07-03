package org.pavelf.nevada.api;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pavelf.nevada.api.exception.ExceptionCase;
import org.pavelf.nevada.api.persistence.domain.Access;
import org.pavelf.nevada.api.persistence.domain.Token;
import org.pavelf.nevada.api.persistence.repository.ApplicationRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.TokenRepository;
import org.pavelf.nevada.api.service.PhotoService.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
properties = "server.port=7777")
public class PhotoControllerTest {

	@LocalServerPort
    private int port;
	
	private static final String JSON = APPLICATION_ACCEPT_PREFIX+
			".photo+json;version=1.0";
	private static final String XML = APPLICATION_ACCEPT_PREFIX+
			".photo+xml;version=1.0";
	
	private static final String JPEG = 
			MediaType.IMAGE_JPEG_VALUE +";version=1.0";
	
	@Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ProfileRepository pr;
    
    @Autowired
    private ApplicationRepository ar;
    
    @Autowired
    private TokenRepository tr;
    
    @Autowired
	private ResourceLoader resourceLoader;
    
    private String getBaseURL() {
		return "http://localhost:" + port;
	}
    
    private byte[] fromFilesystem(String path) throws IOException {
    	try (InputStream is = new FileInputStream(new File(path))) {
    		byte[] targetArray = new byte[is.available()];
	        is.read(targetArray);
	    	return targetArray;
    	}
    }
    
    protected void toFilesystem(byte[] raw, String saveTo) {
    	try (FileOutputStream fos = new FileOutputStream(saveTo)) {
    	    fos.write(raw);
    	} catch (IOException ioe) {
    	    ioe.printStackTrace();
    	}
    }
    
    @Test
	public void controllerShouldAcceptPostedRawImage() throws Exception {
    	Token token = TestTokenBuilder.get()
	    	.setPhotoAccess(Access.READ_WRITE)
	    	.build(pr, ar, tr);
    	
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, JPEG);	
		headers.set(HttpHeaders.AUTHORIZATION, token.getToken());
		
		ResponseEntity<ExceptionCase> response = restTemplate.exchange(
    			getBaseURL() + "/photos?visibility=ALL&version=1.0", 
    			HttpMethod.POST, 
    			new HttpEntity<>(
    					fromFilesystem("/Users/macuser/Desktop/test.png"),
    					headers), 
    			ExceptionCase.class);
    	
		final Integer createdImageId = Integer
				.valueOf(response.getHeaders().getLocation().toString());
		final Integer imageOwnerId = token.getBelongsToProfile();
		
		assertThat(response.getBody()).isNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		byte[] small = loadImage(Size.SMALL, imageOwnerId, createdImageId);
		toFilesystem(small, "/Users/macuser/Desktop/testedSm.png");
		
		byte[] medium = loadImage(Size.MEDIUM, imageOwnerId, createdImageId);
		toFilesystem(medium, "/Users/macuser/Desktop/testedMe.png");
		
		byte[] original = loadImage(Size.ORIGINAL, imageOwnerId, createdImageId);
		toFilesystem(original, "/Users/macuser/Desktop/testedOr.png");
    }
    
    public byte[] loadImage(Size size, int profileId, int photoId) {
    	HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, JPEG);	
		
		return restTemplate.exchange(
    			getBaseURL() + "/profile/"+profileId+"/photos/" + 
    					photoId + "?size=" + size.toString(), 
    			HttpMethod.GET, 
    			new HttpEntity<>(null, headers), 
    			byte[].class).getBody();
	}
    
    @Test
	public void controllerShouldRefuseTooLargeSizedImage() throws Exception {
    	//Resource imageToLoad = resourceLoader.getResource("");
    	
    	
    	
    	
    }
	
}
