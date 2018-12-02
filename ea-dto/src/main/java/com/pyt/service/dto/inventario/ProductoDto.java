package com.pyt.service.dto.inventario;

import org.pyt.common.common.ADto;

public class ProductoDto extends ADto {
	private static final long serialVersionUID = -5885260384830867325L;
	private String nombre;
	private String descripcion;
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
