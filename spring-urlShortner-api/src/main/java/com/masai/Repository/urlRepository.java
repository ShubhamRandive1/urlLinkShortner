package com.masai.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.masai.model.Url;

@Repository
public interface urlRepository extends JpaRepository<Url, Integer>{
	
	public Url findByShortLink(String shortLink);
	
	

}
