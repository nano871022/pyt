package co.com.japl.ea.gdb.privates.utils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.common.Log;
import org.pyt.common.constants.ConfigServiceConstant;

import co.com.japl.ea.query.interfaces.IAdvanceQuerySvc.triggerAction;
import co.com.japl.ea.query.interfaces.IAdvanceQuerySvc.triggerOption;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.exceptions.ReflectionException;
import co.com.japl.ea.gdb.privates.constants.QueryConstants;

public class StatementQuerysUtil {
	private final static String CONST_DTO = "DTO";
	private final static String CONST_ABC_CHAIN = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
	private final static String CONST_PREFIX_TABLE = "TBL_";
	private final static String CONST_FIELD_ALIAS = "%s as %s";
	private final static Integer CONST_LETTER_MAX_NAME = 14;
	private NameSqlProperties namesSql;
	private Log logger = Log.Log(this.getClass());
	private static StatementQuerysUtil statementQuerysUtil;

	private StatementQuerysUtil() {
		try {
			namesSql = NameSqlProperties.getInstance().load();
		} catch (Exception e) {
			logger.logger(e);
		}
	}

	public final static StatementQuerysUtil instance() {
		if (statementQuerysUtil == null) {
			statementQuerysUtil = new StatementQuerysUtil();
		}
		return statementQuerysUtil;
	}

	public final <T extends ADto> String fieldToWhere(T obj, boolean valuesInsert) throws ReflectionException {
		List<String> names = obj.getNameFields();
		String where = QueryConstants.CONST_EMPTY;
		for (String name : names) {
			var value = obj.get(name);
			if (value != null && !valuesInsert) {
				var valuePut = valueFormat(value);
				if (valuePut != null) {
					if (where.length() > 0) {
						where += QueryConstants.CONST_SPACE + QueryConstants.CONST_AND + QueryConstants.CONST_SPACE;
					}
					var comparator = QueryConstants.CONST_EQUAL;
					if (valuePut.contains("%")) {
						comparator = QueryConstants.CONST_LIKE;
					}
					where += getName(obj, name) + comparator + valuePut;
				}
			} else if (valuesInsert) {
				if (where.length() > 0)
					where += QueryConstants.CONST_COMMA + QueryConstants.CONST_SPACE;
				where += value != null ? valueFormat(value) : QueryConstants.CONST_NULL;
			}
		}
		return where;
	}

	public final <T extends ADto> String fieldTOSetUpdate(T obj) throws ReflectionException {
		List<String> names = obj.getNameFields();
		List<String> sets = new ArrayList<String>();
		names.forEach(name -> {
			try {
				var value = valueFormat(obj.get(name));
				sets.add(getName(obj, name) + QueryConstants.CONST_EQUAL + value);
			} catch (Exception e) {
				logger.logger(e);
			}
		});
		var fields = String.join(QueryConstants.CONST_COMMA, sets);
		return fields;
	}

	public final <T extends ADto> String fieldToWhereJPA(T obj, boolean valuesInsert) throws ReflectionException {
		List<String> names = obj.getNameFields();
		String where = QueryConstants.CONST_EMPTY;
		for (String name : names) {
			var value = obj.get(name);
			if (value != null && !valuesInsert) {
				if (where.length() > 0)
					where += QueryConstants.CONST_SPACE + QueryConstants.CONST_AND + QueryConstants.CONST_SPACE;
				where += getName(obj, name) + QueryConstants.CONST_EQUAL + ":" + name;
			} else if (valuesInsert) {
				if (where.length() > 0)
					where += QueryConstants.CONST_COMMA + QueryConstants.CONST_SPACE;
				where += value != null ? ":" + name : QueryConstants.CONST_NULL;
			}
		}
		return where;
	}

	public final <T extends ADto> String fieldWhereToUpdate(T obj) throws ReflectionException {
		var where = getName(obj, "codigo") + QueryConstants.CONST_EQUAL + valueFormat(obj.getCodigo());
		return where;
	}

	public final <T extends ADto> Map<String, Object> getFieldToWhereJPA(T obj) throws ReflectionException {
		List<String> names = obj.getNameFields();
		Map<String, Object> fieldValue = new HashMap<String, Object>();
		for (String name : names) {
			var value = obj.get(name);
			if (value != null) {
				fieldValue.put(name, (value));
			}
		}
		return fieldValue;
	}

	public final <T extends Object> String valueFormat(T value) {
		if (value == null)
			return null;
		if (value.getClass() == String.class) {
			return QueryConstants.CONST_QUOTE + value.toString() + QueryConstants.CONST_QUOTE;
		}
		if (value.getClass() == LocalDate.class) {
			var formatter = DateTimeFormatter.ofPattern(QueryConstants.CONST_FORMAT_DATE);
			return QueryConstants.CONST_QUOTE + ((LocalDate) value).format(formatter) + QueryConstants.CONST_QUOTE;
		}
		if (value.getClass() == Date.class) {
			var sdf = new SimpleDateFormat(QueryConstants.CONST_FORMAT_DATE);
			return QueryConstants.CONST_QUOTE + sdf.format(value) + QueryConstants.CONST_QUOTE;
		}
		if (value.getClass() == Timestamp.class) {
			var dt = new Date(((Timestamp) value).getTime());
			var sdf = new SimpleDateFormat(QueryConstants.CONST_FORMAT_DATE);
			return QueryConstants.CONST_QUOTE + sdf.format(dt) + QueryConstants.CONST_QUOTE;
		}
		if (value instanceof ADto) {
			return valueFormat(((ADto) value).getCodigo());
		}
		if (value instanceof Class) {
			return QueryConstants.CONST_QUOTE + (((Class) value).getName()) + QueryConstants.CONST_QUOTE;
		}
		return value.toString();
	}

	public final <T extends ADto> String fieldToSelect(T obj) throws ReflectionException {
		var outFields = new ArrayList<String>();
		List<String> names = obj.getNameFields();
		names.forEach(name -> {
			var field = getName(obj, name);
			if (field != null || field != "null") {
				outFields.add(String.format(CONST_FIELD_ALIAS, field, name));
			}
		});
		var fields = StringUtils.join(outFields, QueryConstants.CONST_COMMA);
		return fields;
	}

	public final <T extends ADto> String fieldToInsert(T obj) throws ReflectionException {
		List<String> names = obj.getNameFields();
		List<String> fieldz = new ArrayList<String>();
		names.forEach(name -> fieldz.add(getName(obj, name)));
		var fields = StringUtils.join(fieldz, QueryConstants.CONST_COMMA);
		return fields;
	}

	public String typeDataDB(String typeJava) {
		if (typeJava.toLowerCase().contains(QueryConstants.CONST_TYPE_LONG)
				|| typeJava.toLowerCase().contains(QueryConstants.CONST_TYPE_INT)
				|| typeJava.toLowerCase().contains(QueryConstants.CONST_TYPE_BOOL)
				|| typeJava.toLowerCase().contains(QueryConstants.CONST_TYPE_BIG)) {
			return QueryConstants.CONST_TYPE_NUMBER;
		}
		if (typeJava.toLowerCase().contains(QueryConstants.CONST_TYPE_STRING)
				|| typeJava.toLowerCase().contains(QueryConstants.CONST_TYPE_CHAR)) {
			return String.format(QueryConstants.CONST_TYPE_VARCHAR_2, 100);
		}
		if (typeJava.toLowerCase().contains(QueryConstants.CONST_TYPE_DATE)) {
			return QueryConstants.CONST_TYPE_DATE_TIME;
		}
		return String.format(QueryConstants.CONST_TYPE_VARCHAR_2, 30);
	}

	public <T extends ADto> String getTableName(T obj) {
		var name = getName(obj, null);
		if (StringUtils.isNotBlank(name)) {
			return name;
		}
		return CONST_PREFIX_TABLE + obj.getClass().getSimpleName().replace(CONST_DTO, QueryConstants.CONST_EMPTY);
	}

	/**
	 * Se encarga de generar el consecutivo
	 * 
	 * @param dto
	 * @return
	 */
	public final <T extends ADto> String genConsecutivo(Class<T> dto, Integer size) {
		String name = getNameByDto(dto);
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(getDateOfString());
		var quantity = getQuantity(size, sb.length());
		var generateLetter = generateLetterToCompleteCode(sb.length(), quantity.length());
		sb.append(generateLetter);
		sb.append(quantity);
		return sb.toString();
	}

	private final String getQuantity(Integer size, Integer quantityCode) {
		var freeNumbers = CONST_LETTER_MAX_NAME - quantityCode;
		String cantidad = String.valueOf(size);
		if (freeNumbers > 0 && cantidad.length() > freeNumbers) {
			cantidad = cantidad.substring(0, freeNumbers);
		}
		return cantidad;
	}

	private final String generateLetterToCompleteCode(Integer length, Integer quantityLength) {
		var freeNumbers = CONST_LETTER_MAX_NAME - length - quantityLength;
		StringBuilder sb = new StringBuilder();
		Random aleatorio = new Random();
		for (int i = 0; i < freeNumbers; i++) {
			Double valor = aleatorio.nextDouble() * (CONST_ABC_CHAIN.length() - 1 + 0);
			sb.append(CONST_ABC_CHAIN.charAt(valor.intValue()));
		}
		return sb.toString();
	}

	private final <T extends ADto> String getNameByDto(Class<T> dto) {
		String name = dto.getSimpleName();
		name = name.replace(CONST_DTO, QueryConstants.CONST_EMPTY);
		if (name.length() > CONST_LETTER_MAX_NAME) {
			name = name.substring(0, CONST_LETTER_MAX_NAME);
		}
		return name;
	}

	private final String getDateOfString() {
		LocalDateTime fecha = LocalDateTime.now();
		StringBuilder sb = new StringBuilder();
		sb.append(fecha.getYear());
		sb.append(fecha.getMonthValue());
		sb.append(fecha.getDayOfMonth());
		sb.append(fecha.getHour());
		sb.append(fecha.getMinute());
		sb.append(fecha.getSecond());
		return sb.toString().toLowerCase();
	}

	public <T extends ADto> String getNameTriggerPOJO(Class<T> obj, triggerOption to, triggerAction... tas) {
		var name = QueryConstants.CONST_EMPTY;
		name += to.toString().subSequence(0, 1);
		for (triggerAction ta : tas) {
			name += ta.toString().substring(0, 1);
		}
		name += obj.getSimpleName();
		return name;
	}

	private <T extends ADto> String getName(T dto, String name) {
		var path = "";
		Field field = null;
		try {
			field = ADto.class.getDeclaredField(name);
		} catch (Exception e) {
			field = null;
		}
		if (field != null) {
			path = ADto.class.getCanonicalName();
		} else {
			path = dto.getClass().getCanonicalName();
		}
		if (name != null) {
			path += ConfigServiceConstant.SEP_DOT + name;
		}
		var value = namesSql.getValue(path);
		if (value == null) {
			value = name;
		}
		return (String) value;
	}
}