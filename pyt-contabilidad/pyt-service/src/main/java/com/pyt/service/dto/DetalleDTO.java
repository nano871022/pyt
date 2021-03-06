package com.pyt.service.dto;

import java.math.BigDecimal;
import java.util.List;

import org.pyt.common.annotations.Increment;
import org.pyt.common.annotations.NoEdit;
import org.pyt.common.annotations.Operacion;
import org.pyt.common.annotations.Operar;
import org.pyt.common.common.ADto;

/**
 * Contiene el detalle de la factura
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public class DetalleDTO extends ADto {
	private static final long serialVersionUID = -3773810908869511818L;
	private String codeDocumento;
	@Increment
	private Integer renglon;
	private ServicioDTO concepto;
	private CentroCostoDTO centroCosto;
	private ParametroDTO categoriaGasto;
	private BigDecimal valorBruto;
	@NoEdit
	@Operacion(valor1 = "valorBruto",valor2="valorIva", operacion = Operar.SUMA)
	@Operacion(valor1 = "valorConsumo", operacion = Operar.SUMA)
	private BigDecimal valorNeto;
	private Long porcentajeIva;
	@Operacion(valor1 = "porcentajeIva", valor2 = "valorBruto", operacion = Operar.MULTIPLICAR)
	private BigDecimal valorIva;
	private Long impuestoConsumo;
	@Operacion(valor1 = "impuestoConsumo", valor2 = "valorBruto", operacion = Operar.MULTIPLICAR)
	private BigDecimal valorConsumo;
	private List<TrabajadorDTO> ejecutadores;
	private ActividadIcaDTO actividadIca;
	private String codigoDocumento;
	private IngresoDTO ingreso;
	
	public String getCodeDocumento() {
		return codeDocumento;
	}

	public void setCodeDocumento(String codeDocumento) {
		this.codeDocumento = codeDocumento;
	}

	public IngresoDTO getIngreso() {
		return ingreso;
	}

	public void setIngreso(IngresoDTO ingreso) {
		this.ingreso = ingreso;
	}

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public Integer getRenglon() {
		return renglon;
	}

	public void setRenglon(Integer renglon) {
		this.renglon = renglon;
	}

	public ServicioDTO getConcepto() {
		return concepto;
	}

	public void setConcepto(ServicioDTO concepto) {
		this.concepto = concepto;
	}

	public CentroCostoDTO getCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(CentroCostoDTO centroCosto) {
		this.centroCosto = centroCosto;
	}

	public ParametroDTO getCategoriaGasto() {
		return categoriaGasto;
	}

	public void setCategoriaGasto(ParametroDTO categoriaGasto) {
		this.categoriaGasto = categoriaGasto;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public BigDecimal getValorNeto() {
		return valorNeto;
	}

	public void setValorNeto(BigDecimal valorNeto) {
		this.valorNeto = valorNeto;
	}

	public Long getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(Long porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public BigDecimal getValorIva() {
		return valorIva;
	}

	public void setValorIva(BigDecimal valorIva) {
		this.valorIva = valorIva;
	}

	public Long getImpuestoConsumo() {
		return impuestoConsumo;
	}

	public void setImpuestoConsumo(Long impuestoConsumo) {
		this.impuestoConsumo = impuestoConsumo;
	}

	public BigDecimal getValorConsumo() {
		return valorConsumo;
	}

	public void setValorConsumo(BigDecimal valorConsumo) {
		this.valorConsumo = valorConsumo;
	}

	public ActividadIcaDTO getActividadIca() {
		return actividadIca;
	}

	public void setActividadIca(ActividadIcaDTO actividadIca) {
		this.actividadIca = actividadIca;
	}

	public List<TrabajadorDTO> getEjecutadores() {
		return ejecutadores;
	}

	public void setEjecutadores(List<TrabajadorDTO> ejecutadores) {
		this.ejecutadores = ejecutadores;
	}

}
