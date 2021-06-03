package co.com.japl.ea.service.implement.inventario;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.ListUtils;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.ParametroInventarioConstants;

import co.com.arquitectura.annotation.proccessor.Services.Type;
import co.com.arquitectura.annotation.proccessor.Services.kind;
import co.com.arquitectura.annotation.proccessor.Services.scope;
import co.com.japl.ea.dto.abstracts.Services;
import co.com.japl.ea.dto.dto.inventario.MovimientoDTO;
import co.com.japl.ea.dto.dto.inventario.ParametroInventarioDTO;
import co.com.japl.ea.dto.dto.inventario.ProductoDTO;
import co.com.japl.ea.dto.dto.inventario.RestarCantidadDTO;
import co.com.japl.ea.dto.dto.inventario.ResumenProductoDTO;
import co.com.japl.ea.dto.dto.inventario.SaldoDTO;
import co.com.japl.ea.dto.interfaces.inventarios.IInventarioSvc;
import co.com.japl.ea.dto.interfaces.inventarios.IMovimientoSvc;
import co.com.japl.ea.dto.interfaces.inventarios.IParametroInventariosSvc;
import co.com.japl.ea.dto.interfaces.inventarios.IProductosSvc;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.ParametroException;
import co.com.japl.ea.exceptions.inventario.InventarioException;
import co.com.japl.ea.exceptions.inventario.MovimientoException;
import co.com.japl.ea.exceptions.inventario.ResumenProductoException;

public class InventarioSvc extends Services implements IInventarioSvc {
	@Inject(resource = "com.pyt.service.implement.inventario.MovimientoSvc")
	private IMovimientoSvc movimientoSvc;
	@Inject(resource = "com.pyt.service.implement.inventario.ProductosSvc")
	private IProductosSvc productosSvc;
	@Inject(resource = "com.pyt.service.implement.inventario.ParametroInventariosSvc")
	private IParametroInventariosSvc parametroSvc;

	@co.com.arquitectura.annotation.proccessor.Services(alcance = scope.EJB, alias = "Agregar unidades", descripcion = "Agregar unidades asociados a los productos.", tipo = kind.PUBLIC, type = Type.CREATE)
	@Override
	public void agregarInventario(MovimientoDTO dto, UsuarioDTO usuario) throws InventarioException {
		if (dto == null)
			throw new InventarioException("No se suministro el movimiento.");
		if (usuario == null)
			throw new InventarioException("No se suministro el usuario.");
		if (dto.getProducto() == null)
			throw new InventarioException("El movimiento no tiene el producto.");
		if (StringUtils.isBlank(dto.getProducto().getCodigo()))
			throw new InventarioException("El producto suministrado no se encuentra creado.");
		try {
			if (dto.getCantidad() == null) {
				dto.setCantidad(0);
			}
			if (dto.getTipo() == null) {
				ParametroInventarioDTO parametro = new ParametroInventarioDTO();
				parametro.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
				parametro.setOrden(1L);
				List<ParametroInventarioDTO> tipoMovs = parametroSvc.getAllParametros(parametro,
						ParametroInventarioConstants.GRUPO_DESC_TIPO_MOVIMIENTO);
				if (ListUtils.haveMoreOneItem(tipoMovs)) {
					throw new InventarioException(
							"Se encontraron varios registros para el parametro de tipo novedad de entrada.");
				}
				if (ListUtils.isBlank(tipoMovs)) {
					throw new InventarioException(
							"No se encontro ningun registro para el parametro de tipo de novedad de entrada.");
				}
				dto.setTipo(tipoMovs.get(0));
			}
			dto = movimientoSvc.insert(dto, usuario);
			ResumenProductoDTO resumen = new ResumenProductoDTO();
			resumen.setProducto(dto.getProducto());
			List<ResumenProductoDTO> list = productosSvc.resumenProductos(resumen);
			if (ListUtils.isBlank(list)) {
				resumen.setCantidad(dto.getCantidad());
				resumen.setValorCompra(dto.getPrecioCompra());
				resumen = productosSvc.insert(resumen, usuario);
			} else if (ListUtils.haveMoreOneItem(list)) {
				movimientoSvc.del(dto, usuario);
				throw new InventarioException("Se encontraron varios resumenes, contacte con el administrador.");
			}
			SaldoDTO saldoOld = ultimoSaldo(resumen.getProducto());
			if (list != null && list.size() > 0) {
				resumen = list.get(0);
			}
			if (DtoUtils.isBlank(resumen)) {
				throw new InventarioException("No fue encontrado ningun resumen de movimientos.");
			}
			if (resumen.getCantidad() == null) {
				resumen.setCantidad(0);
			}
			resumen.setCantidad(resumen.getCantidad() + dto.getCantidad());
			if (resumen.getValorCompra() == null || resumen.getValorCompra().compareTo(new BigDecimal(0)) == 0) {
				resumen.setValorCompra(saldoOld.getMovimiento().getPrecioCompra());
			}
			productosSvc.update(resumen, usuario);
			BigDecimal totalMovimiento = dto.getPrecioCompra().multiply(new BigDecimal(dto.getCantidad()));
			SaldoDTO saldo = new SaldoDTO();
			saldo.setMovimiento(dto);
			saldo.setCantidad(resumen.getCantidad());
			saldo.setTotal(saldoOld.getTotal().add(totalMovimiento));
			saldo = movimientoSvc.insert(saldo, usuario);
		} catch (MovimientoException e) {
			throw new InventarioException("Se presento error con el ingreso del movimiento.", e);
		} catch (ResumenProductoException e) {
			throw new InventarioException("Se presento error en la actualizacion del resumen del producto.", e);
		} catch (ParametroException e) {
			throw new InventarioException(
					"Se presento error en la obtencion del parametro de invetarion de tipo movimiento de entrada.");
		}
	}

	@Override
	public void retirarInventario(MovimientoDTO dto, UsuarioDTO usuario) throws InventarioException {
		if (dto == null)
			throw new InventarioException("No se suministro el movimiento.");
		if (usuario == null)
			throw new InventarioException("No se suministro el usuario.");
		if (dto.getProducto() == null)
			throw new InventarioException("El movimiento no tiene el producto.");
		if (StringUtils.isBlank(dto.getProducto().getCodigo()))
			throw new InventarioException("El producto suministrado no se encuentra creado.");
		try {
			dto = movimientoSvc.insert(dto, usuario);
			ResumenProductoDTO resumen = new ResumenProductoDTO();
			resumen.setProducto(dto.getProducto());
			List<ResumenProductoDTO> list = productosSvc.resumenProductos(resumen);
			if (list == null || list.size() == 0) {
				movimientoSvc.del(dto, usuario);
				throw new InventarioException("No se encontro resumen, contacte con el administrador.");
			} else if (list.size() > 1) {
				movimientoSvc.del(dto, usuario);
				throw new InventarioException("Se encontraron varios resumenes, contacte con el administrador.");
			}
			SaldoDTO saldoOld = ultimoSaldo(resumen.getProducto());
			resumen = list.get(0);
			resumen.setCantidad(resumen.getCantidad() - dto.getCantidad());
			productosSvc.update(resumen, usuario);
			BigDecimal totalMovimiento = dto.getPrecioCompra().multiply(new BigDecimal(dto.getCantidad()));
			SaldoDTO saldo = new SaldoDTO();
			saldo.setMovimiento(dto);
			saldo.setCantidad(resumen.getCantidad());
			saldo.setTotal(saldoOld.getTotal().subtract(totalMovimiento));
			saldo = movimientoSvc.insert(saldo, usuario);
			RestarCantidadDTO restar = restarCantidad(dto.getProducto(), usuario);
			restar.setCantidad(restar.getCantidad() - dto.getCantidad());
			movimientoSvc.update(restar, usuario);
		} catch (MovimientoException e) {
			throw new InventarioException("Se presento error con el ingreso del movimiento.", e);
		} catch (ResumenProductoException e) {
			throw new InventarioException("Se presento error en la actualizacion del resumen del producto.", e);
		}

		/**
		 * Para agregar un movimiento en el kadex de retiro: 1. se agrega en la tabla el
		 * movminiento 2. se busca en la tabla de productos el producto anterior 3. se
		 * ajusta la cantida de valores 4. se ajusta el valor de compra y venta para los
		 * valores segun es fifo y lifo.
		 */
	}

	private final RestarCantidadDTO restarCantidad(ProductoDTO producto, UsuarioDTO usuario)
			throws InventarioException {
		try {
			MovimientoDTO movimiento = new MovimientoDTO();
			movimiento.setProducto(producto);
			Date fecha = null;
			RestarCantidadDTO primero = null;
			List<MovimientoDTO> movimientos = movimientoSvc.movimientos(movimiento);
			for (MovimientoDTO mov : movimientos) {
				RestarCantidadDTO restar = new RestarCantidadDTO();
				restar.setMovimiento(mov);
				List<RestarCantidadDTO> restars = movimientoSvc.restarCantidad(restar);
				if (restars == null || restars.size() == 0) {
					restar.setCantidad(mov.getCantidad());
					return movimientoSvc.insert(restar, usuario);
				}
				if (restars.size() > 1) {
					throw new InventarioException("Se encontraron varios registros de restar cantidad.");
				}
				if (restars.size() == 1) {
					restar = restars.get(0);
				}
				if (restar.getCantidad() > 0) {
					if (fecha == null || fecha.compareTo(restar.getFechaCreacion()) <= 0) {
						fecha = restar.getFechaCreacion();
						primero = restar;
					}
				}
			}
			return primero;
		} catch (MovimientoException e) {
			throw new InventarioException("Se presento problema con la obtencion del registro a restar cantidad.", e);
		}
	}

	/**
	 * Se encarga de obtener el ultimo saldo
	 * 
	 * @param producto {@link ProductoDTO}
	 * @return {@link SaldoDTO}
	 * @throws {@link InventarioException}
	 */
	private final SaldoDTO ultimoSaldo(ProductoDTO producto) throws InventarioException {
		if (producto == null || StringUtils.isBlank(producto.getCodigo()))
			throw new InventarioException("producto no suministrado.");
		try {
			MovimientoDTO movimiento = new MovimientoDTO();
			movimiento.setProducto(producto);
			List<MovimientoDTO> list = movimientoSvc.movimientos(movimiento);
			Date fecha = null;
			movimiento = null;
			for (MovimientoDTO mov : list) {
				if (fecha == null) {
					fecha = mov.getFechaCreacion();
					movimiento = mov;
					continue;
				}
				if (fecha.compareTo(mov.getFechaCreacion()) <= 0) {
					fecha = mov.getFechaCreacion();
					movimiento = mov;
				}
			}
			SaldoDTO saldo = new SaldoDTO();
			saldo.setMovimiento(movimiento);
			List<SaldoDTO> saldos = movimientoSvc.saldos(saldo);
			if (saldos == null || saldos.size() == 0) {
				saldo.setCantidad(0);
				saldo.setTotal(new BigDecimal(0));
				return saldo;
			}
			if (saldos.size() > 1) {
				throw new InventarioException("Se encontraron varios saldos.");
			}
			saldo = saldos.get(0);
			return saldo;
		} catch (MovimientoException e) {
			throw new InventarioException("Se presento un problema en la obtencion del movimiento.", e);
		}
	}
}