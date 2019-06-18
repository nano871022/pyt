package co.com.japl.ea.gdb.privates.utils;

import java.util.Properties;

import org.pyt.common.constants.EnviromentConstants;
import org.pyt.common.properties.PropertiesUtils;

import co.com.japl.ea.gdb.privates.constants.ConnectionPropertiesConstant;

public final class NameSqlProperties {
	private Properties properties;
	private static NameSqlProperties propertiesJdbc;

	private NameSqlProperties() {
	}

	public final static NameSqlProperties getInstance() {
		if (propertiesJdbc == null) {
			propertiesJdbc = new NameSqlProperties();
		}
		return propertiesJdbc;
	}
	
	public NameSqlProperties load() throws Exception {
		var pu = PropertiesUtils.getInstance();
		var path = System.getenv(EnviromentConstants.PROP_ENV_PATH);
		if(path == null) {
			path = ConnectionPropertiesConstant.PROP_PATH;
		}
		pu.setNameProperties(ConnectionPropertiesConstant.PROP_NAMES_SQL);
		pu.setPathProperties(path);
		properties = pu.load().getProperties();
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T getValue(String propertyName) {
		return (T) properties.get(propertyName);
	}

}
