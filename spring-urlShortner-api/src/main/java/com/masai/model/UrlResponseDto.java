package com.masai.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UrlResponseDto {
	
	private String orignalUrl;
	
	private String shortLink;
	
	private LocalDateTime expiirationDate;

}
