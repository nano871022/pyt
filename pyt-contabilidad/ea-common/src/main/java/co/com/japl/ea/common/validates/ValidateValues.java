package co.com.japl.ea.common.validates;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.Log;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.exceptions.validates.ValidateValueException;

/**
 * Se encarga de validar dos valores si son iguales o uno esta contenido en el
 * otro
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
public final class ValidateValues {
	private Log logger = Log.Log(this.getClass());
	private List<MethodToValue> list;

	public ValidateValues() {
		list = new ArrayList<MethodToValue>();
	}

	/**
	 * Se encarga de validar dos valores que son de tipo Integer
	 * 
	 * @param value1 {@link Integer}
	 * @param value2 {@link Integer}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(Integer value1, Integer value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo BigDecimal
	 * 
	 * @param value1 {@link BigDecimal}
	 * @param value2 {@link BigDecimal}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(BigDecimal value1, BigDecimal value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo BigInteger
	 * 
	 * @param value1 {@link BigInteger}
	 * @param value2 {@link BigInteger}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(BigInteger value1, BigInteger value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo Number
	 * 
	 * @param value1 {@link Number}
	 * @param value2 {@link Number}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(Number value1, Number value2) throws Exception {
		return value1.byteValue() == (value2).byteValue();
	}

	/**
	 * Se encarga de validar dos valores que son de tipo Long
	 * 
	 * @param value1 {@link Long}
	 * @param value2 {@link Long}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(Long value1, Long value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo Double
	 * 
	 * @param value1 {@link Double}
	 * @param value2 {@link Double}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(Double value1, Double value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo Short
	 * 
	 * @param value1 {@link Short}
	 * @param value2 {@link Short}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(Short value1, Short value2) throws Exception {
		return value1.compareTo(value2) == 0;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo String
	 * 
	 * @param value1 {@link String}
	 * @param value2 {@link String}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	private final Boolean validate(String value1, String value2) throws Exception {
		if (StringUtils.isNotBlank(value2)) {
			if (StringUtils.isBlank(value1))
				return false;
			if (StringUtils.isNotBlank(value1) && StringUtils.isNotBlank(value2)) {
				if (value2.contains("%")) {
					String[] split = value2.split("%");
					Boolean valid = true;
					for (String seg : split) {
						if (StringUtils.isNotBlank(seg)) {
							valid &= value1.contains(seg);
						}
					}
					return valid;
				} else {
					Boolean val = value1.contentEquals(value2);
					return val;
				}
			}
		} else if (StringUtils.isBlank(value2)) {
			return true;
		}
		return false;
	}

	/**
	 * Se encarga de validar dos valores que son de tipo Object
	 * 
	 * @param value1 {@link Object}
	 * @param value2 {@link Object}
	 * @return {@link Boolean}
	 * @throws {@link Exception}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final Boolean validate(Object value1, Object value2) throws ValidateValueException {
		try {
			if (value1 instanceof Integer && value2 instanceof Integer)
				return validate((Integer) value1, (Integer) value2);
			if (value1 instanceof BigDecimal && value2 instanceof BigDecimal)
				return validate((BigDecimal) value1, (BigDecimal) value2);
			if (value1 instanceof BigInteger && value2 instanceof BigInteger)
				return validate((BigInteger) value1, (BigInteger) value2);
			if (value1 instanceof Long && value2 instanceof Long)
				return validate((Long) value1, (Long) value2);
			if (value1 instanceof Double && value2 instanceof Double)
				return validate((Double) value1, (Double) value2);
			if (value1 instanceof Short && value2 instanceof Short)
				return validate((Short) value1, (Short) value2);
			if (value1 instanceof Number && value2 instanceof Number)
				return validate((Number) value1, (Number) value2);
			if (value1 instanceof String && value2 instanceof Long && isCast(value1, Long.class))
				return validate(cast(value1, Long.class), (Long) value2);
			if (value1 instanceof String && value2 instanceof Integer && isCast(value1, Integer.class))
				return validate(cast(value1, Integer.class), (Integer) value2);
			if (value1 instanceof String && value2 instanceof BigDecimal && isCast(value1, BigDecimal.class))
				return validate(cast(value1, BigDecimal.class), (BigDecimal) value2);
			if (value1 instanceof String && value2 instanceof String)
				return validate((String) value1, (String) value2);
			if (value1 instanceof ADto && value2 instanceof ADto)
				if (value1.getClass() == value2.getClass())
					return validateDto(value1, value2);
			if (value1 != null && value2 != null) {
				try {
					Object o1 = ((Class) value1).getConstructor().newInstance();
					Object o2 = ((Class) value2).getConstructor().newInstance();
					return (o1.getClass() == o2.getClass());
				} catch (Exception e) {
				}
				return value1.getClass() == value2.getClass();
			}
			return true;
		} catch (Exception e) {
			throw new ValidateValueException("Se presento error en la validacion.", e);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public final <T extends ADto> Boolean validateDto(Object obj, Object obj2) throws ValidateValueException {
		T dto = (T) obj;
		T dto2 = (T) obj2;
		if (dto.getCodigo().contentEquals(dto2.getCodigo())) {
			return true;
		}
		return false;
	}

	/**
	 * Se encarga de realizar cast de un valor a otro valor
	 * 
	 * @param value {@link Object} extended
	 * @param clase {@link Class} extended
	 * @return {@link Object} extended
	 * @throws ValidateValueException
	 */
	@SuppressWarnings("unchecked")
	public final <T, S extends Object> T cast(S value, Class<T> clase) throws ValidateValueException {
		try {
			if (value == null || clase == null)
				return null;

			var originClass = clase;

			var castDates = castDates(value, clase);
			if (castDates != null) {
				return (T) castDates;
			}

			var numbers = castNumbers(value, clase);
			if (numbers != null) {
				return numbers;
			}

			clase = convertFromPrimitive(clase);
			if (value.getClass() == clase) {
				return (T) value;
			}

			var valueReturn = stringToClass(value, clase);
			if (valueReturn != null) {
				return (T) valueReturn;
			}

			valueReturn = convertValueToClassMethodStatic(value, clase, originClass);
			if (valueReturn != null) {
				return (T) valueReturn;
			}
			valueReturn = getValueFromMethod(value, clase, originClass);
			if (valueReturn != null) {
				return (T) valueReturn;
			}

			if (value.getClass() == clase) {
				return (T) value;
			}

		} catch (IllegalArgumentException e) {
			throw new ValidateValueException("ERR convert " + value + " to " + clase.getSimpleName(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private final <T, S> T castNumbers(S value, Class<T> clazz) {
		if (clazz == Long.class) {
			if (value.getClass() == BigDecimal.class) {
				return (T) Long.valueOf(((BigDecimal) value).longValue());
			}
		} else if (clazz == BigDecimal.class) {
			if (value.getClass() == Integer.class) {
				return (T) new BigDecimal((int) value);
			} else if (value.getClass() == String.class && StringUtils.isNotBlank((String) value)) {
				return (T) new BigDecimal((String) value);
			}
		} else if (clazz == Integer.class) {
			if (value.getClass() == BigDecimal.class) {
				return (T) Integer.valueOf(((BigDecimal) value).intValue());
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private final <T, S> T castDates(S value, Class<T> clazz) {
		if (clazz == Date.class) {
			if (value.getClass() == LocalDate.class) {
				return (T) Date.from(((LocalDate) value).atStartOfDay(ZoneId.systemDefault()).toInstant());
			}
			if (value.getClass() == Timestamp.class) {
				return (T) Date.from(((Timestamp) value).toInstant());
			}
		}
		if (clazz == LocalDate.class) {
			if (value.getClass() == Date.class) {
				return (T) LocalDate.ofInstant(((Date) value).toInstant(), ZoneId.systemDefault());
			}
			if(value.getClass() == Timestamp.class) {
				return (T)LocalDate.ofInstant(((Timestamp)value).toInstant(),ZoneId.systemDefault());
			}
		}
		if(clazz == LocalDateTime.class) {
			if (value.getClass() == Date.class) {
				return (T) LocalDateTime.ofInstant(((Date) value).toInstant(), ZoneId.systemDefault());
			}
			if(value.getClass() == Timestamp.class) {
				return (T)LocalDateTime.ofInstant(((Timestamp)value).toInstant(),ZoneId.systemDefault());
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final <T> Class<T> convertFromPrimitive(Class clazz) {
		if (clazz == int.class) {
			clazz = (Class<T>) Integer.class;
		}
		if (clazz == short.class) {
			clazz = (Class<T>) Short.class;
		}
		if (clazz == double.class) {
			clazz = (Class<T>) Double.class;
		}
		if (clazz == long.class) {
			clazz = (Class<T>) Long.class;
		}
		return clazz;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final <T, S> T stringToClass(S value, Class clazz) {
		if (value.getClass() == String.class) {
			if (StringUtils.isBlank((String) value)) {
				return null;
			}
			if (clazz == Integer.class) {
				return (T) Integer.valueOf((String) value);
			}
			if (clazz == Double.class) {
				return (T) Double.valueOf((String) value);
			}
			if (clazz == Long.class) {
				return (T) Long.valueOf((String) value);
			}
			if (clazz == Short.class) {
				return (T) Short.valueOf((String) value);
			}
			if (clazz == BigDecimal.class) {
				return (T) new BigDecimal((String) value);
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final <T, S> T getValueFromMethod(S value, Class clazz, Class originClass) throws ValidateValueException {
		try {
//		for (var method : list) {
//			if (method.isClassInOut(value.getClass(), originClass)) {
//				return (T) method.getValueInvoke(value);
//			}
//		}
			var methods = value.getClass().getDeclaredMethods();
			for (var method : methods) {
				if (method.getParameterCount() == 0 && !Modifier.isStatic(method.getModifiers())
						&& (method.getReturnType() == clazz || method.getReturnType() == originClass)) {
					list.add(new MethodToValue(method, value.getClass(), originClass, true));
					return (T) method.invoke(value);
				}
			}
		} catch (Exception e) {
			throw new ValidateValueException("No se logra encontrar el metodo de conversion.", e);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final <T, S> T convertValueToClassMethodStatic(S value, Class clazz, Class originClass)
			throws ValidateValueException {
		try {
			if (value instanceof String && StringUtils.isBlank((String) value))
				return null;
			var metodos = clazz.getDeclaredMethods();
			for (Method metodo : metodos) {
				if (Modifier.isStatic(metodo.getModifiers())
						&& (metodo.getReturnType() == clazz || metodo.getReturnType() == originClass)
						&& metodo.getParameterCount() == 1) {
					var clases = metodo.getParameterTypes()[0];
					if (clases == value.getClass()) {
						list.add(new MethodToValue(metodo, value.getClass(), originClass, true));
						return (T) metodo.invoke(null, value);
					} else if (isCast(value, clases)) {
						T val = (T) cast(value, clases);
						list.add(new MethodToValue(metodo, val.getClass(), originClass, true));
						return (T) metodo.invoke(null, val);
					}
				}
			}
		} catch (Exception e) {
			throw new ValidateValueException("No se logra obtener la conversion de metodo estatico.", e);
		}
		return null;
	}

	/**
	 * Se encarga de verificar si el valor no esta vacio y es del tipo de la clase
	 * indicado
	 * 
	 * @param value {@link Object}
	 * @param clase {@link Class}
	 * @return {@link Boolean}
	 */
	public final <T, L extends Object> Boolean isCast(T value, Class<L> clase) {
		try {
			clase = convertFromPrimitive(clase);
			var numeric = isNumber(value, clase);
			if (numeric) {
				return true;
			}
			var date = isDate(value, clase);
			if (date) {
				return true;
			}
			var str = isString(value, clase);
			if (str) {
				return true;
			}
			if (clase.getConstructor().newInstance() instanceof ADto) {
				if (value instanceof ADto) {
					try {
						clase.cast(value);
						return true;
					} catch (ClassCastException cast) {
						try {
							clase.asSubclass(value.getClass());
							return true;
						} catch (ClassCastException e1) {
							return false;
						}
					}
				}
			}
			if (clase == ADto.class)
				if (value instanceof ADto)
					return true;
			return clase == value.getClass();
		} catch (Exception e) {
			return false;
		}
	}
	
	public final Boolean isNumber( Class clazz) {
		return clazz == int.class || clazz == long.class || clazz == double.class || clazz == float.class || clazz == BigDecimal.class || clazz == Integer.class || clazz == BigInteger.class || clazz == Double.class || clazz == Long.class || clazz == Short.class|| clazz == Number.class;
		
	}
	
	public final <M, N, V> Boolean numericBetween(M value, N value2, V value3) {
		Boolean valid = true;
		var r1 = compareNumbers(value, value2);
		var r2 = compareNumbers(value, value3);
		valid &= r1 == 1 || r1 == 0;
		valid &= r2 == -1 || r2 == 0;
		return valid;
	}

	public final Boolean isDate(Class clazz) {
		return clazz == Date.class || clazz == LocalDate.class || clazz == LocalDateTime.class; 
	}
	
	public final Boolean isString(Class clazz) {
		return clazz == String.class  || clazz == Character.class;
	}

	private final <V, T> Boolean isString(V value, Class<T> clazz) {
		if (clazz == String.class) {
			return true;
		}
		return false;
	}

	private final <V, T> Boolean isDate(V value, Class<T> clazz) {
		try {
			if (clazz == Date.class) {
				if (value instanceof Date || value instanceof Long) {
					return true;
				} else if (value instanceof String) {
					return Optional.ofNullable(Date.parse((String) value)).isPresent();
				}
			} else if (clazz == Instant.class) {
				if (value instanceof Instant || value instanceof Long) {
					return true;
				} else if (value instanceof String) {
					return Optional.ofNullable(Instant.parse((String) value)).isPresent();
				}
			} else if (clazz == LocalDate.class) {
				if (value instanceof LocalDate || value instanceof Long || value instanceof Instant) {
					return true;
				} else if (value instanceof String) {
					return Optional.ofNullable(LocalDate.parse((String) value)).isPresent();
				}
			} else if (clazz == LocalDateTime.class) {
				if (value instanceof LocalDateTime || value instanceof Long || value instanceof Instant) {
					return true;
				} else if (value instanceof String) {
					return Optional.ofNullable(LocalDateTime.parse((String) value)).isPresent();
				}
			}
		} catch (ClassCastException e) {
			logger.DEBUG(e);
		}
		return false;
	}

	private final <V, T> Boolean isNumber(V value, Class<T> clazz) {
		try {
		if (clazz == BigDecimal.class) {
			if (value instanceof BigInteger || value instanceof Long || value instanceof Double) {
				return true;
			} else if (value instanceof String) {
				return Optional.ofNullable(new BigDecimal((String) value)).isPresent();
			}
		} else if (clazz == Integer.class) {
				if (value instanceof Integer) {
					return true;
				} else if (value instanceof String) {
					return Optional.ofNullable(Integer.valueOf((String) value)).isPresent();
				}
		} else if (clazz == BigInteger.class) {
				if (value instanceof Long || value instanceof Integer) {
					return true;
				} else if (value instanceof String) {
					return Optional.ofNullable(new BigInteger((String) value)).isPresent();
				}
		} else if (clazz == Double.class) {
				if (value instanceof Double) {
					return true;
				} else if (value instanceof String) {
					return Optional.ofNullable(Double.valueOf((String) value)).isPresent();
				}
		} else if (clazz == Long.class) {
				if (value instanceof Long) {
					return true;
				} else if (value instanceof String) {
					return Optional.ofNullable(Long.valueOf((String) value)).isPresent();
				}
			} else if (clazz == Short.class) {
				if (value instanceof Short) {
					return true;
				} else if (value instanceof String) {
					return Optional.ofNullable(Short.valueOf((String) value)).isPresent();
				}
		} else if ( clazz == Number.class && value instanceof Number) {
				return true;
		}
		} catch (ClassCastException e) {
			logger.DEBUG(e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public final <S extends Object, T extends Object, P extends Object> S multiplicar(T value, P value2) {
		try {
			if (value == null)
				return null;
			if (value2 == null)
				return null;
			if (isCast(value, Long.class)) {
				if (isCast(value2, BigDecimal.class)) {
					return (S) cast(value2, BigDecimal.class).multiply(cast(value, BigDecimal.class));
				}
			}
		} catch (ValidateValueException e) {
			logger.logger(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public final <S, T, P extends Object> S sumar(T value, P value2) {
		try {
			if (value == null)
				return null;
			if (value2 == null)
				return null;
			if (isCast(value, Long.class)) {
				if (isCast(value2, BigDecimal.class)) {
					return (S) cast(value2, BigDecimal.class).add(cast(value, BigDecimal.class));
				}
			}
			if (isCast(value, BigDecimal.class)) {
				if (isCast(value2, BigDecimal.class)) {
					return (S) cast(value2, BigDecimal.class).add(cast(value, BigDecimal.class));
				}
			}
		} catch (ValidateValueException e) {
			logger.logger(e);
		}
		return null;
	}

	public final <V1, V2> int compareNumbers(V1 value1, V2 value2) {
		if (value1 instanceof Integer) {
			if (value2 instanceof Integer) {
				return ((Integer) value1).compareTo((Integer) value2);
			}
		}
		if (value1 instanceof BigInteger) {
			if (value2 instanceof BigInteger) {
				return ((BigInteger) value1).compareTo((BigInteger) value2);
			}
		}
		if (value1 instanceof BigDecimal) {
			if (value2 instanceof BigDecimal) {
				return ((BigDecimal) value1).compareTo((BigDecimal) value2);
			}
		}
		if (value1 instanceof Float) {
			if (value2 instanceof Float) {
				return ((Float) value1).compareTo((Float) value2);
			}
		}
		if (value1 instanceof Double) {
			if (value2 instanceof Double) {
				return ((Double) value1).compareTo((Double) value2);
			}
		}
		if (value1 instanceof Short) {
			if (value2 instanceof Short) {
				return ((Short) value1).compareTo((Short) value2);
			}
		}
		return -1;
	}

	public final <O> Boolean isNotBlank(O value) {
		if (value == null) {
			return false;
		}
		if (isString(value.getClass())) {
			return StringUtils.isNotBlank((String) value);
		} else if (isDate(value.getClass())) {
			return true;
		} else if (isNumber(value.getClass())) {
			return true;
		} else if (value instanceof ADto) {
			return DtoUtils.isNotBlank((ADto) value);
		}
		return false;
	}
}