package com.pyt.service.interfaces.inventarios;

import org.pyt.common.common.UsuarioDTO;
import org.pyt.common.exceptions.inventario.InventarioException;

import com.pyt.service.dto.inventario.MovimientoDto;

/**
 * Se encarga de realiazar ingresos de inventarios como movimientos de los
 * mismos en forma kardex
 * 
 * @author Alejandro Parra
 * @since 01-12-2018
 */
public interface IInventarioSvc {
	/**
	 * Se enccarga de agregar la cantidad de productos en el inventario y realizar los movimientos necesarios
	 * @param dto {@link MovimientoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link InventarioException}
	 */
	public void agregarInventario(MovimientoDto dto,UsuarioDTO usuario)throws InventarioException;
	/**
	 * Se encarga de retirar un producto del inventarioy segun la configuracion lifo y fifo y segun la cantidad se cuadra los precios de compra y venta
	 * @param dto {@link MovimientoDto}
	 * @param usuario {@link UsuarioDTO}
	 * @throws {@link InventarioException}
	 */
	public void retirarInventario(MovimientoDto dto,UsuarioDTO usuario)throws InventarioException;
}