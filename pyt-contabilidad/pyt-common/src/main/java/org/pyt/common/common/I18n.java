package org.pyt.common.common;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.constants.AppConstants;
import org.pyt.common.constants.LanguageConstant;

/**
 * Archvios que se encarga de cargar la internacionalizacion de lenguage y
 * usarla donde se desea
 * 
 * @author Alejandro Parra
 * @since 15/04/2019
 */
public final class I18n {
	private final Log logger = Log.Log(this.getClass());
	private ResourceBundle bundle;
	private String bundleNoDefault;

	/**
	 * Se encarga de retornar la lista de lenguages que se encuentran en el
	 * resources bundle de la aplicacion
	 * 
	 * @return {@link ResourceBundle}
	 */
	private final ResourceBundle getLanguages() {
		if (bundle == null) {
			bundle = ResourceBundle.getBundle(AppConstants.RESOURCE_BUNDLE, getLocaleUsed());
		}
		return bundle;
	}

	/**
	 * Se encarga de obtener la conguguracion de locale
	 * 
	 * @return {@link Locale}
	 */
	private final Locale getLocaleUsed() {
		if (StringUtils.isNotBlank(bundleNoDefault)) {
			return new Locale(bundleNoDefault);
		}
		return Locale.getDefault();
	}

	/**
	 * Retorna el valor que se encuentra en la llave suministrada de la
	 * internacionalizacion
	 * 
	 * @param key {@link String}
	 * @return {@link String}
	 */
	public final String valueBundle(String key) {
		try {
			return getLanguages().getString(key);
		}catch(Exception exception) {
			logger.logger(String.format(getLanguages().getString(LanguageConstant.LANGUAGE_KEY_NOT_FOUND),key));
			return key;
		}
	}

	public final void setBunde(String bundle) {
		this.bundleNoDefault = bundle;
	}

	/**
	 * recargar el bundle para volver a configurar la locación
	 */
	public final void reloadBundle() {
		bundle = null;
		getLanguages();
	}

}
