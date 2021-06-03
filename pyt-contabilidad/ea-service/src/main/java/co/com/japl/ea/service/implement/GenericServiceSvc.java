package co.com.japl.ea.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;

import co.com.japl.ea.query.interfaces.IQuerySvc;
import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.abstracts.Services;
import co.com.japl.ea.dto.interfaces.IGenericServiceSvc;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.GenericServiceException;
import co.com.japl.ea.exceptions.QueryException;

public class GenericServiceSvc<T extends ADto> extends Services implements IGenericServiceSvc<T> {
	@Inject(resource = "co.com.japl.ea.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	public List<T> gets(T dto, Integer init, Integer end) throws GenericServiceException {
		List<T> lista = new ArrayList<T>();
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO).get());
		if (!isValidClass(dto.getClass())) {
			throw new GenericServiceException(
					i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CLASS_NOT_PERM).get());
		}
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
		return lista;
	}

	public List<T> getAll(T dto) throws GenericServiceException {
		List<T> lista = new ArrayList<T>();
		if (dto == null)
			
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO).get());
		if (!isValidClass(dto.getClass())) {
			throw new GenericServiceException(
					i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CLASS_NOT_PERM).get());
		}

		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
		return lista;
	}

	public T get(T dto) throws GenericServiceException {
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO).get());
		if (!isValidClass(dto.getClass())) {
			throw new GenericServiceException(
					i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CLASS_NOT_PERM).get());
		}

		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
	}

	public void update(T dto, UsuarioDTO user) throws GenericServiceException {
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO).get());
		if (!isValidClass(dto.getClass())) {
			throw new GenericServiceException(
					i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CLASS_NOT_PERM).get());
		}

		if (StringUtils.isBlank(dto.getCodigo()))
			throw new GenericServiceException(
					i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CODE_EMPTY_DTO).get());
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
	}

	public T insert(T dto, UsuarioDTO user) throws GenericServiceException {
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO).get());
		if (!isValidClass(dto.getClass())) {
			throw new GenericServiceException(
					i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CLASS_NOT_PERM).get());
		}

		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new GenericServiceException(
					i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CODE_EMPTY_DTO).get());
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
	}

	public void delete(T dto, UsuarioDTO user) throws GenericServiceException {
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO).get());
		if (!isValidClass(dto.getClass())) {
			throw new GenericServiceException(
					i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CLASS_NOT_PERM).get());
		}
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new GenericServiceException(
					i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CODE_EMPTY_DTO).get());
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new GenericServiceException(e.getMessage(), e);
		}

	}

	@Override
	public Integer getTotalRows(T dto) throws GenericServiceException {
		if (dto == null)
			throw new GenericServiceException(i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_EMPTY_DTO).get());
		if (!isValidClass(dto.getClass())) {
			throw new GenericServiceException(
					i18n().valueBundle(LanguageConstant.GENERIC_SERVICE_CLASS_NOT_PERM).get());
		}
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new GenericServiceException(e.getMensage(), e);
		}
	}

	public final <D extends ADto> boolean isValidClass(Class<D> clazz) {
		var valid = true;

		return valid;
	}
}