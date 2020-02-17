package com.pyt.service.dto;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotation.proccess.ValueInObject;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;

/**
 * areas para contabilizar los costos
 * 
 * @author alejando parra
 * @since 06/05/2018
 */
@DelClass(nombre="com.pyt.service.dto.dels.CentroCostoDelDTO")
@UpdClass(nombre="com.pyt.service.dto.upds.CentroCostoUpdDTO")

public class CentroCostoDTO extends ADto{
	private static final long serialVersionUID = -7537599944970961982L;
	private String nombre;
	private String descripcion;
	private String estado;
	private Integer orden;
	@ValueInObject(field = "nit")
	private EmpresaDTO empresa;
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
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getOrden() {
		return orden;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	public EmpresaDTO getEmpresa() {
		return empresa;
	}
	public void setEmpresa(EmpresaDTO empresa) {
		this.empresa = empresa;
	}
	
}
