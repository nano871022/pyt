package co.com.japl.ea.dto.dto.inventario;

import java.math.BigDecimal;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.dto.ParametroDTO;

public class ResumenProductoDTO extends ADto {
	private static final long serialVersionUID = -4721145467642162080L;
	private ProductoDTO producto;
	private Integer cantidad;
	private ParametroDTO ivaPercentAplicarVenta;
	private Long gananciaPercentVenta;
	private BigDecimal valorVenta;
	private BigDecimal valorCompra;
	public ProductoDTO getProducto() {
		return producto;
	}
	public BigDecimal getValorVenta() {
		return valorVenta;
	}

	public void setValorVenta(BigDecimal valorVenta) {
		this.valorVenta = valorVenta;
	}

	public BigDecimal getValorCompra() {
		return valorCompra;
	}

	public void setValorCompra(BigDecimal valorCompra) {
		this.valorCompra = valorCompra;
	}

	public void setProducto(ProductoDTO producto) {
		this.producto = producto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public ParametroDTO getIvaPercentAplicarVenta() {
		return ivaPercentAplicarVenta;
	}
	public void setIvaPercentAplicarVenta(ParametroDTO ivaPercentAplicarVenta) {
		this.ivaPercentAplicarVenta = ivaPercentAplicarVenta;
	}
	public Long getGananciaPercentVenta() {
		return gananciaPercentVenta;
	}
	public void setGananciaPercentVenta(Long gananciaPercentVenta) {
		this.gananciaPercentVenta = gananciaPercentVenta;
	}
}
