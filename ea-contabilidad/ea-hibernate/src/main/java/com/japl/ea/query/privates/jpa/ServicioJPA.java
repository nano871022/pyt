package com.japl.ea.query.privates.jpa;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Nombre de los servicios que ofrece la empresa
 * @author alejandro parra 
 * @since 06/05/2018
 */
@Entity(name="TBL_SERVICE")
@Table(name="TBL_SERVICE")
public class ServicioJPA extends AJPA{
	@Column(name="sname")
	private String nombre;
	@Column(name="nvaluehandwork")
	private Long  valorManoObra;
	@Column(name="sdescription")
	private String descripcion;
	@ManyToMany(targetEntity=IngresoJPA.class)
	private Set<IngresoJPA> servicios;
	
	public Set<IngresoJPA> getServicios() {
		return servicios;
	}
	public void setServicios(Set<IngresoJPA> servicios) {
		this.servicios = servicios;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getValorManoObra() {
		return valorManoObra;
	}
	public void setValorManoObra(Long valorManoObra) {
		this.valorManoObra = valorManoObra;
	}
}