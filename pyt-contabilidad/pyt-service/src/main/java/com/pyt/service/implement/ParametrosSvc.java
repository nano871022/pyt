package com.pyt.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.pyt.common.exceptions.ParametroException;
import org.pyt.common.exceptions.QueryException;

import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.dto.UsuarioDTO;
import com.pyt.service.interfaces.IParametrosSvc;
import com.pyt.service.interfaces.IQuerySvc;

public class ParametrosSvc implements IParametrosSvc{

	private IQuerySvc querySvc;

	public List<ParametroDTO> getParametros(ParametroDTO dto, Integer init, Integer end)
			throws ParametroException {
		List<ParametroDTO> lista = new ArrayList<ParametroDTO>();
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
		return lista;
	}

	public List<ParametroDTO> getAllParametros(ParametroDTO dto) throws ParametroException {
		List<ParametroDTO> lista = new ArrayList<ParametroDTO>();
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
		return lista;
	}

	public ParametroDTO getParametro(ParametroDTO dto) throws ParametroException {
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
	}

	public void update(ParametroDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new ParametroException("El id de empresa se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
	}

	public ParametroDTO insert(ParametroDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		if (dto.getCodigo() != null)
			throw new ParametroException("El codigo de empresa no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
	}

	public void delete(ParametroDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("El objeto empresa se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new ParametroException("El codigo empresa se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new ParametroException(e.getMessage(), e);
		}

	}

}
