package com.pyt.service.dto;

import java.util.Date;
/**
 * Se encarga de realizar crud sobre los objetos de bd.
 * @author alejandro parra
 * @since 06/05/2018
 */
public abstract class ADto {
	private String codigo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private Date fechaEliminacion;
	private String creador;
	private String actualizador;
	private String eliminador;
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}
	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
	public Date getFechaEliminacion() {
		return fechaEliminacion;
	}
	public void setFechaEliminacion(Date fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}
	public String getCreador() {
		return creador;
	}
	public void setCreador(String creador) {
		this.creador = creador;
	}
	public String getActualizador() {
		return actualizador;
	}
	public void setActualizador(String actualizador) {
		this.actualizador = actualizador;
	}
	public String getEliminador() {
		return eliminador;
	}
	public void setEliminador(String eliminador) {
		this.eliminador = eliminador;
	}
	
}
