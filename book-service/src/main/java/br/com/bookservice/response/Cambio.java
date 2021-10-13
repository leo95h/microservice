package br.com.bookservice.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Cambio implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String from;
	private String to;
	private Double conversionFactor;
	private Double convertedValue;
	private String environment;
	
}
