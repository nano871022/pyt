package com.pyt.service.dto;

import org.pyt.common.common.ADto;

public class CiudadDTO extends ADto{
	private static final long serialVersionUID = 8327265201569056423L;
	
	private String nombre;
	private DepartamentoDTO departamento;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public DepartamentoDTO getDepartamento() {
		return departamento;
	}
	public void setDepartamento(DepartamentoDTO departamento) {
		this.departamento = departamento;
	}
}
