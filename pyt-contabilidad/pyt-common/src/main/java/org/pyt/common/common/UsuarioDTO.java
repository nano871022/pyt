package org.pyt.common.common;

import java.util.Date;
/**
 * Se encarga de almacena rel suaurio que se encuentra en sesion en la aplicacion
 * @author alejandro parra
 * @since 06/05/2018
 */
public class UsuarioDTO extends ADto{
	private String nombre;
	private String password;
	private Date fechaIncio;
	private Date fechaFin;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getFechaIncio() {
		return fechaIncio;
	}
	public void setFechaIncio(Date fechaIncio) {
		this.fechaIncio = fechaIncio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
}
