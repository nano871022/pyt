package org.pyt.common.exceptions.inventario;

import org.pyt.common.exceptions.AExceptions;

/**
 * Ser encarga de controlar la excepciones del serivicos de servicios
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public class InventarioException extends AExceptions {

	private static final long serialVersionUID = 5962743388991994735L;

	public InventarioException(String mensaje, Throwable e) {
		super(mensaje, e);
		setMensage(mensaje);
		setE(e);
	}

	public InventarioException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}
}