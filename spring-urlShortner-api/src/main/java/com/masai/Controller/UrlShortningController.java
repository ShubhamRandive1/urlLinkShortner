package com.masai.Controller;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.Service.UrlService;
import com.masai.model.Url;
import com.masai.model.UrlDto;
import com.masai.model.UrlErrorResponseDto;
import com.masai.model.UrlResponseDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class UrlShortningController {
	
	@Autowired
	private UrlService urlService;
	
	@PostMapping("/generate")
	public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urldto){
		
		Url urlToRet = urlService.generateShortLink(urldto);
		
		if(urlToRet != null) {
			
			UrlResponseDto urlResponsedto = new UrlResponseDto();
			
			urlResponsedto.setOrignalUrl(urlToRet.getOriginalUrl());
			
			urlResponsedto.setExpiirationDate(urlToRet.getExpirationDate());
			
			urlResponsedto.setShortLink(urlToRet.getShortLink());
			
			
			return new ResponseEntity<UrlResponseDto>(urlResponsedto,HttpStatus.ACCEPTED);
			
			
		}
		
		UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
		
		urlErrorResponseDto.setStatus("404");
		
		urlErrorResponseDto.setError("There is error is the request , please try again");
		
		return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
		
	}
	
	@GetMapping("/generates/{shortLink}")
	public ResponseEntity<?> redirectToOrignalurl(@PathVariable String shortLink, HttpServletResponse res) throws IOException{
		
		if(StringUtils.isEmpty(shortLink)) {
			
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			
			urlErrorResponseDto.setError("Invalid URL");
			
			urlErrorResponseDto.setStatus("400");
			
			return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.ACCEPTED);
		}
		
		Url urlToRet = urlService.getEncodedUrl(shortLink);
		
		if(urlToRet == null) {
			
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			
			urlErrorResponseDto.setError("Url Not Exist");
			
			urlErrorResponseDto.setStatus("400");
			
			
			return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.ACCEPTED);
			
			
		}
		
		
		if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now())) {
			
			urlService.deleteShortLink(urlToRet);
			
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			
			urlErrorResponseDto.setError("Url expierd");
			
			urlErrorResponseDto.setStatus("200");
			
			return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.ACCEPTED);
			
		}
		
			res.sendRedirect(urlToRet.getOriginalUrl());
			
			return null;
		
		
	}
	
	

}
