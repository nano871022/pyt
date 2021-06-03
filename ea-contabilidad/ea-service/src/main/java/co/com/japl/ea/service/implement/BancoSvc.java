package co.com.japl.ea.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;

import co.com.japl.ea.query.interfaces.IQuerySvc;
import co.com.japl.ea.dto.abstracts.Services;
import co.com.japl.ea.dto.dto.BancoDTO;
import co.com.japl.ea.dto.interfaces.IBancosSvc;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.BancoException;
import co.com.japl.ea.exceptions.QueryException;

public class BancoSvc extends Services implements IBancosSvc {
	@Inject(resource = "co.com.japl.ea.query.implement.QuerySvc")
	private IQuerySvc querySvc;

	public List<BancoDTO> getBancos(BancoDTO dto, Integer init, Integer end) throws BancoException {
		List<BancoDTO> lista = new ArrayList<BancoDTO>();
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
		} catch (QueryException e) {
			throw new BancoException(e.getMensage(), e);
		}
		return lista;
	}

	public List<BancoDTO> getAllBancos(BancoDTO dto) throws BancoException {
		List<BancoDTO> lista = new ArrayList<BancoDTO>();
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new BancoException(e.getMensage(), e);
		}
		return lista;
	}

	public BancoDTO getBancos(BancoDTO dto) throws BancoException {
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		try {
			return querySvc.get(dto);
		} catch (QueryException e) {
			throw new BancoException(e.getMensage(), e);
		}
	}

	public void update(BancoDTO dto, UsuarioDTO user) throws BancoException {
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new BancoException("El id de actividad ica se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new BancoException(e.getMensage(), e);
		}
	}

	public BancoDTO insert(BancoDTO dto, UsuarioDTO user) throws BancoException {
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		if (StringUtils.isNotBlank(dto.getCodigo()))
			throw new BancoException("El codigo de banco no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new BancoException(e.getMensage(), e);
		}
	}

	public void delete(BancoDTO dto, UsuarioDTO user) throws BancoException {
		if (dto == null)
			throw new BancoException("El objeto banco se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new BancoException("El codigo banco se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new BancoException(e.getMessage(), e);
		}

	}

	@Override
	public Integer getTotalRows(BancoDTO dto) throws BancoException {
		if(dto == null) throw new BancoException("El banco se encuentra vacio.");
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new BancoException(e.getMensage(), e);
		}
	}

}