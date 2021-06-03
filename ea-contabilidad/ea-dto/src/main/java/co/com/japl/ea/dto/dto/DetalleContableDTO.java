package co.com.japl.ea.dto.dto;

import java.math.BigDecimal;

import co.com.arquitectura.annotation.proccessor.DelClass;
import co.com.arquitectura.annotation.proccessor.UpdClass;
import co.com.japl.ea.common.abstracts.ADto;
@DelClass(nombre="co.com.japl.ea.dto.dto.dels.DetalleContableDelDTO")
@UpdClass(nombre="co.com.japl.ea.dto.dto.upds.DetalleContableUpdDTO")

public class DetalleContableDTO extends ADto {
	private static final long serialVersionUID = -501193270090048865L;
	private ConceptoDTO concepto;
	private CuentaContableDTO cuentaContable;
	private BigDecimal valor;
	private Integer renglon;
	private String observacion;
	private String codigoDocumento;
	public ConceptoDTO getConcepto() {
		return concepto;
	}
	public void setConcepto(ConceptoDTO concepto) {
		this.concepto = concepto;
	}
	public CuentaContableDTO getCuentaContable() {
		return cuentaContable;
	}
	public void setCuentaContable(CuentaContableDTO cuentaContable) {
		this.cuentaContable = cuentaContable;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Integer getRenglon() {
		return renglon;
	}
	public void setRenglon(Integer renglon) {
		this.renglon = renglon;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
}