package co.com.japl.ea.common.reflection;

import static org.pyt.common.constants.languages.Reflection.CONST_ERR_ACCESS_ILEGAL;
import static org.pyt.common.constants.languages.Reflection.CONST_ERR_ARGUMENT_ILEGAL;
import static org.pyt.common.constants.languages.Reflection.CONST_ERR_CAST_FIELD_INPUT_VALUE;
import static org.pyt.common.constants.languages.Reflection.CONST_ERR_METHOD_NOT_FOUND;
import static org.pyt.common.constants.languages.Reflection.CONST_ERR_OBJECT_INVOKE;
import static org.pyt.common.constants.languages.Reflection.CONST_ERR_SECURITY;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.Log;
import org.pyt.common.constants.ReflectionConstants;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.common.validates.ValidateValues;
import co.com.japl.ea.exceptions.ReflectionException;
import co.com.japl.ea.exceptions.validates.ValidateValueException;

/**
 * Se encarga de realizar reflecciones para el dto
 * 
 * @author Alejandro Parra
 * @since 2018-05-18
 */
public abstract class ReflectionDto {
	private final static String CONST_SERIAL_VERSION_UID = "serialVersionUID";

	private Log logger = Log.Log(this.getClass());

	@SuppressWarnings("unchecked")
	private <S extends ADto> Field searchField(Class<S> clazz, String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			try {
				return searchField((Class<S>) clazz.getSuperclass(), name);
			} catch (ClassCastException e2) {
				return null;
			}
		}
	}

	/**
	 * Se encarga de poder realiar set sobre los valores de los campos del dto por
	 * medio de refleccion
	 * 
	 * @param nombreCampo {@link String}
	 * @param value       {@link Object}
	 * @throws {@link ReflectionException}
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public final <S extends ADto, T extends Object> void set(String nombreCampo, T value) throws ReflectionException {
		Class<S> clase = (Class<S>) super.getClass();
		String nameMethod;
		Field field = null;
		try {
			if(value == null) {
				var campo = searchField(clase, nombreCampo);
				if (campo != null && campo.trySetAccessible()) {
					campo.set(this, null);
				}else {
					return;
				}
			}
			field = searchField(nombreCampo, this.getClass());
			nameMethod = ReflectionConstants.SET + field.getName().substring(0, 1).toUpperCase()
					+ field.getName().substring(1);
			Method method = clase.getMethod(nameMethod, field.getType());
			ValidateValues vv = new ValidateValues();
			method.invoke(this, vv.cast(value, field.getType()));
		} catch (SecurityException e) {
			throw new ReflectionException(CONST_ERR_SECURITY, e);
		} catch (NoSuchMethodException e) {
			if (field != null) {
				field.setAccessible(true);
				try {
					field.set(this, value);
				} catch (IllegalArgumentException e1) {
					throw new ReflectionException(CONST_ERR_ARGUMENT_ILEGAL, e);
				} catch (IllegalAccessException e1) {
					throw new ReflectionException(CONST_ERR_ACCESS_ILEGAL, e);
				}
			} else {
				throw new ReflectionException(CONST_ERR_METHOD_NOT_FOUND, e);
			}
		} catch (IllegalAccessException e) {
			throw new ReflectionException(CONST_ERR_ACCESS_ILEGAL, e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(CONST_ERR_ARGUMENT_ILEGAL, e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException(CONST_ERR_OBJECT_INVOKE, e);
		} catch (ValidateValueException e) {
			throw new ReflectionException(CONST_ERR_CAST_FIELD_INPUT_VALUE, e);
		}
	}

	/**
	 * Se encarga de obtener de obtner un valor apartir de un nombre del campo del
	 * medio de refleccion
	 * 
	 * @param nombreCampo {@link String}
	 * @param value       {@link Object}
	 * @return {@link Object}
	 * @throws {@link ReflectionException}
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public final <T, S extends Object> T get(String nombreCampo, S value) throws ReflectionException {
		Class<S> clase = (Class<S>) super.getClass();
		String nameMethod;
		Field field = null;
		try {
			field = searchField(nombreCampo, this.getClass());
			if (field != null) {
				if (field.getName().length() > 1)
					nameMethod = ReflectionConstants.GET + field.getName().substring(0, 1).toUpperCase()
							+ field.getName().substring(1);
				else
					nameMethod = ReflectionConstants.GET + field.getName().toUpperCase();

				Method method = clase.getMethod(nameMethod, value != null ? value.getClass() : null);
				return (T) method.invoke(this, value);
			}
			return null;
		} catch (SecurityException e) {
			throw new ReflectionException(CONST_ERR_SECURITY, e);
		} catch (NoSuchMethodException e) {
			if (field != null) {
				field.setAccessible(true);
				try {
					return (T) field.get(this);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					throw new ReflectionException(CONST_ERR_ACCESS_ILEGAL, e1);
				}
			} else {
				throw new ReflectionException(CONST_ERR_METHOD_NOT_FOUND, e);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ReflectionException(CONST_ERR_OBJECT_INVOKE, e);
		}
	}

	/**
	 * Se encarga de retornar el tipo de campo apartir de nombre suministrado
	 * 
	 * @param nombreCampo {@link String}
	 * @return {@link Class}
	 * @throws {@link ReflectionException}
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Object> Class<T> getType(String nombreCampo) throws ReflectionException {
		try {
			Field field = searchField(nombreCampo, this.getClass());
			Class<T> clas = (Class<T>) field.getType();
			return clas;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Se encargad e buscar un campo dentro de todas las clases y parents
	 * 
	 * @param nombreCampo {@link String}
	 * @param clase       {@link Class}
	 * @return {@link Field}
	 * @throws {@link ReflectionException}
	 */
	private final <T extends Object> Field searchField(String nombreCampo, Class<T> clase) throws ReflectionException {
		if (StringUtils.isBlank(nombreCampo))
			return null;
		if (clase == Object.class)
			return null;
		Field campo = null;
		try {
			campo = clase.getDeclaredField(nombreCampo);
		} catch (Exception e) {
			try {
				campo = searchField(nombreCampo, clase.getSuperclass());
			} catch (ReflectionException e1) {
				throw e1;
			}
		}
		return campo;
	}

	/**
	 * Se encarga de obtener de obtner un valor apartir de un nombre del campo del
	 * medio de refleccion
	 * 
	 * @param nombreCampo {@link String}
	 * @return {@link Object}
	 * @throws {@link ReflectionException}
	 */
	public final <T extends Object> T get(String nombreCampo) throws ReflectionException {
		return get(nombreCampo, null);
	}

	/**
	 * Se encarga de entregar untexto con todos los campos nombre valor
	 * 
	 * @return {@link String}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final String toStringAll() {
		String campos = "{";
		campos += stringAll((Class) this.getClass());
		campos += "}";
		return campos;
	}

	@SuppressWarnings("unchecked")
	private final <T extends ADto> String stringAll(Class<T> clase) {
		String campos = "";
		Field[] fields = clase.getDeclaredFields();
		try {
			for (Field field : fields) {
				campos += String.format("%s%s=%s", campos.length() > 3 ? "," : "", field.getName(),
						get(field.getName()));
			}
		} catch (ReflectionException e) {
			logger.DEBUG(e.getMessage());
		}
		if (clase.getSuperclass() != ReflectionDto.class) {
			campos += stringAll((Class<T>) clase.getSuperclass());
		}
		return campos;
	}

	/**
	 * Se encarga de retornar todos los nomrbs de los camopos que pueden ser usado
	 * en el objeto.
	 */
	@SuppressWarnings("unchecked")
	public final <T extends ADto> List<String> getNameFields() throws ReflectionException {
		return nameFields((Class<T>) this.getClass());
	}

	/**
	 * Se encarga de retornar el nombre de todos los campos usados sobre el objeto
	 * suministrado
	 * 
	 * @param clase Class
	 * @return {@link List} of {@link String}
	 * @throws {@link ReflectionException}
	 */
	@SuppressWarnings("unchecked")
	private final <T extends ADto> List<String> nameFields(Class<T> clase) throws ReflectionException {
		List<String> list = new ArrayList<String>();
		Field[] fields = clase.getDeclaredFields();
		for (Field field : fields) {
			try {
				if (field.getName().contains(CONST_SERIAL_VERSION_UID))
					continue;
				get(field.getName());
				list.add(field.getName());
			} catch (ReflectionException e) {
				if (e.getCause() instanceof NoSuchMethodException) {
					continue;
				}
				throw e;
			}
		}
		if (clase.getSuperclass() != ReflectionDto.class) {
			list.addAll(nameFields((Class<T>) clase.getSuperclass()));
		}
		return list;
	}

	/**
	 * Se encarga de retornar la clase que identifica el tipo del campo
	 * 
	 * @param name {@link String}
	 * @return {@link Class}
	 * @throws {@link ReflectionException}
	 */
	public Class typeField(String name) throws ReflectionException {
		Field field = searchField(name, this.getClass());
		return field.getType();
	}

	@Override
	public boolean equals(Object obj) {
		try {
			if (obj == null)
				return false;
			if (obj instanceof ADto) {
				if (((ADto) obj).getCodigo() == null && ((ADto) this).getCodigo() == null)
					return true;
				return ((ADto) obj).getCodigo().equals(((ADto) this).getCodigo());
			}
		} catch (Exception e) {
			logger.DEBUG(e);
		}
		return false;
	}
}