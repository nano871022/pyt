package com.pyt.service.dto;

import org.pyt.common.common.ADto;

public class DepartamentoDTO extends ADto {
	private String nombre;
	private PaisDTO pais;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public PaisDTO getPais() {
		return pais;
	}
	public void setPais(PaisDTO pais) {
		this.pais = pais;
	}
}
