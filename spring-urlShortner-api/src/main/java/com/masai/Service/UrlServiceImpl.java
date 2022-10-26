package com.masai.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.apache.catalina.util.ToStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;
import com.masai.Repository.urlRepository;
import com.masai.model.Url;
import com.masai.model.UrlDto;

import antlr.StringUtils;

@Component
public class UrlServiceImpl implements UrlService{
	
	@Autowired
	private urlRepository urlRepo;
	

	@Override
	public Url generateShortLink(UrlDto urlDto) {
		// TODO Auto-generated method stub
		
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(urlDto.getUrl())){
			
			String encodedUrl = encodeUrl(urlDto.getUrl());
			
			Url urlToPersist = new Url();
			
			urlToPersist.setCreationDate(LocalDateTime.now());
			
			urlToPersist.setOriginalUrl(urlDto.getUrl());
			
			urlToPersist.setShortLink(encodedUrl);
			
			urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(),urlToPersist.getCreationDate()));
			
			Url urlToRet = persistShortLink(urlToPersist);
			
			if(urlToPersist != null) {
				
				return urlToRet;
			}
			
			return null;
		}
		
		return null;
	}

	private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
		// TODO Auto-generated method stub
		
		if(org.apache.commons.lang3.StringUtils.isBlank(expirationDate)) {
			
			return creationDate.plusSeconds(120);
		}
		LocalDateTime expirratinDateToRet = LocalDateTime.parse(expirationDate);
		return expirratinDateToRet;
	}

	private String encodeUrl(String url) {
		// TODO Auto-generated method stub
		
		String encodedUrl="";
		
		LocalDateTime time = LocalDateTime.now();
		
		encodedUrl = Hashing.murmur3_32()
				.hashString(url.concat(time.toString()),StandardCharsets.UTF_8)
				.toString();
		return encodedUrl;
	}

	@Override
	public Url persistShortLink(Url url) {
		// TODO Auto-generated method stub
		
		Url urlToRet = urlRepo.save(url);
		
		
		return urlToRet;
	}

	@Override
	public Url getEncodedUrl(String url) {
		// TODO Auto-generated method stub
		
		Url urlToRet = urlRepo.findByShortLink(url);
		
		return urlToRet;
	}

	@Override
	public void deleteShortLink(Url url) {
		// TODO Auto-generated method stub
		
		urlRepo.delete(url);
		
	}

}
