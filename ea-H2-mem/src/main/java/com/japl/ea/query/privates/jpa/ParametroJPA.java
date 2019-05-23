package com.japl.ea.query.privates.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * Se almacenan todos los parametros que se van a utilizar en la aplicacion y se
 * separan por grupos.
 * 
 * @author alejandro parra 
 * @since 06/05/2018
 */
@Entity
@Table(name="MEM_Parametro")
public class ParametroJPA{
	private static final long serialVersionUID = -5396836082089633791L;
	@Id
	@Column(name="CODIGO")
	protected String codigo;
	protected Date fechaCreacion;
	protected Date fechaActualizacion;
	protected Date fechaEliminacion;
	protected String creador;
	protected String actualizador;
	protected String eliminador;

	private Long orden;
	private String nombre;
	private String descripcion;
	private String valor;
	private String valor2;
	private String grupo;
	private String estado;

	public Long getOrden() {
		return orden;
	}
	
	public void setOrden(Long orden) {
		this.orden = orden;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getValor2() {
		return valor2;
	}
	public void setValor2(String valor2) {
		this.valor2 = valor2;
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
}
