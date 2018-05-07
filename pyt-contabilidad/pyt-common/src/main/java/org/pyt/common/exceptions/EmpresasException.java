package org.pyt.common.exceptions;

/**
 * Se encarga de controlar las excepciones de empresas
 * 
 * @author alejandro parra
 * @since 06/05/2018
 */
public class EmpresasException extends AExceptions {

	private static final long serialVersionUID = 2767550412983144964L;

	public EmpresasException(String mensaje, Throwable e) {
		super(mensaje, e);
		setMensage(mensaje);
		setE(e);
	}

	public EmpresasException(String mensaje) {
		super(mensaje);
		setMensage(mensaje);
	}
}
