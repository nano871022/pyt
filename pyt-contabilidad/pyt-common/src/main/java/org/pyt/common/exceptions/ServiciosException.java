package org.pyt.common.exceptions;

/**
 * Ser encarga de controlar la excepciones del serivicos de servicios
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public class ServiciosException extends AExceptions {

	private static final long serialVersionUID = 5962743388991994735L;

	public ServiciosException(String mensaje, Throwable e) {
		super(mensaje, e);
		setMensage(mensaje);
		setE(e);
	}

	public ServiciosException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}
}
