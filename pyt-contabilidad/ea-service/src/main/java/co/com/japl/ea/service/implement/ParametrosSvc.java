package co.com.japl.ea.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.ParametroConstants;

import co.com.japl.ea.query.interfaces.IQuerySvc;
import co.com.arquitectura.annotation.proccessor.Services.Type;
import co.com.arquitectura.annotation.proccessor.Services.kind;
import co.com.arquitectura.annotation.proccessor.Services.scope;
import co.com.japl.ea.dto.abstracts.Services;
import co.com.japl.ea.dto.dto.ParametroDTO;
import co.com.japl.ea.dto.dto.ParametroGrupoDTO;
import co.com.japl.ea.dto.interfaces.IParametrosSvc;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.ParametroException;
import co.com.japl.ea.exceptions.QueryException;

public class ParametrosSvc extends Services implements IParametrosSvc {
	@Inject(resource = "co.com.japl.ea.query.implement.QuerySvc")
	protected IQuerySvc querySvc;

	public List<ParametroDTO> getParametros(ParametroDTO dto, Integer init, Integer end) throws ParametroException {
		List<ParametroDTO> lista = new ArrayList<ParametroDTO>();
		List<ParametroDTO> lista2 = new ArrayList<ParametroDTO>();
		if (dto == null)
			throw new ParametroException("El objeto parametro se encuentra vacio.");
		try {
			lista = querySvc.gets(dto, init, end);
			if ("*".equalsIgnoreCase(dto.getGrupo())) {
				lista.stream().filter(filter -> filter.getGrupo() != null && "*".equalsIgnoreCase(filter.getGrupo()))
						.forEach(row -> lista2.add(row));
				if (lista2.size() > 0)
					return lista2;
			}
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
		return lista;
	}

	public List<ParametroDTO> getAllParametros(ParametroDTO dto) throws ParametroException {
		List<ParametroDTO> lista = new ArrayList<ParametroDTO>();
		if (dto == null)
			throw new ParametroException("El objeto parametro se encuentra vacio.");
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
			throw new ParametroException("El objeto parametro se encuentra vacio.");
		if (StringUtils.isBlank(dto.getCodigo()))
			throw new ParametroException("El id de parametro se encuentra vacia.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
	}

	public ParametroDTO insert(ParametroDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("El objeto parametro se encuentra vacio.");
		if (dto.getCodigo() != null)
			throw new ParametroException("El codigo de parametro no se encuentra vacio.");
		try {
			return querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException(e.getMensage(), e);
		}
	}

	public void delete(ParametroDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("El objeto parametro se encuentra vacio.");
		if (dto.getCodigo() == null)
			throw new ParametroException("El codigo parametro se encuentra vacio.");
		try {
			querySvc.del(dto, user);
		} catch (Exception e) {
			throw new ParametroException(e.getMessage(), e);
		}

	}

	@Override
	public Integer totalCount(ParametroDTO dto) throws ParametroException {
		if (dto == null)
			throw new ParametroException("No se suministro el dto para realizar filtro");
		try {
			return querySvc.countRow(dto);
		} catch (QueryException e) {
			throw new ParametroException("Se presento problema en el conteo de registro de parametros.", e);
		}
	}

	@Override
	public List<ParametroGrupoDTO> getParametroGrupo(ParametroGrupoDTO dto) throws ParametroException {
		List<ParametroGrupoDTO> lista = new ArrayList<ParametroGrupoDTO>();
		if (dto == null)
			throw new ParametroException("No se suministro el parametro grupo para realizar el filtro.");
		try {
			lista = querySvc.gets(dto);
		} catch (QueryException e) {
			throw new ParametroException("No se logro obtener los grupos asociados a parametros.", e);
		}
		return lista;
	}

	@Override
	public ParametroGrupoDTO insert(ParametroGrupoDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("Se encontro el parameto grupo vacio.");
		if (user == null)
			throw new ParametroException("Se encontro el usuario vacio.");
		try {
			dto = querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException("Se presento un problema en el ingreso del registro.");
		}
		return dto;
	}

	@co.com.arquitectura.annotation.proccessor.Services(alcance = scope.EJB, alias = "Ingreso Parametros", descripcion = "Ingreso de servicios de parametros", tipo = kind.PUBLIC, type = Type.CREATE)
	@Override
	public ParametroDTO insertService(ParametroDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("No se suministro el objeto de parametros para insertar.");
		if (user == null)
			throw new ParametroException("Se encontro el usuario vacio.");
		try {
			if (StringUtils.isNotBlank(dto.getGrupo()) && !dto.getGrupo().contains("*")) {
				var parametro = new ParametroDTO();
				parametro.setNombre(dto.getGrupo());
				parametro.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
				parametro = querySvc.get(parametro);
				dto.setGrupo(parametro.getCodigo());
			}
			if (!querySvc.insert(dto, user)) {
				throw new ParametroException("No se realizo el ingreso del registro.");
			}
		} catch (QueryException e) {
			throw new ParametroException("Se presento un problema en el ingreso del registro.", e);
		}
		return dto;
	}

	@Override
	public void update(ParametroGrupoDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("Se encontro el parameto grupo vacio.");
		if (user == null)
			throw new ParametroException("Se encontro el usuario vacio.");
		try {
			querySvc.set(dto, user);
		} catch (QueryException e) {
			throw new ParametroException("Se presento un problema en la actualizacion del registro.");
		}
	}

	@Override
	public void delete(ParametroGrupoDTO dto, UsuarioDTO user) throws ParametroException {
		if (dto == null)
			throw new ParametroException("Se encontro el parameto grupo vacio.");
		if (user == null)
			throw new ParametroException("Se encontro el usuario vacio.");
		try {
			querySvc.del(dto, user);
		} catch (QueryException e) {
			throw new ParametroException("Se presento un problema en la eliminacion del registro.");
		}
	}

	@Override
	public List<ParametroDTO> getAllParametros(ParametroDTO dto, String grupo) throws ParametroException {
		if (dto == null) {
			throw new ParametroException("No se suministro el parametro para aplicar el filtro de busqueda.");
		}
		String grupoP = getIdByParametroGroup(grupo);
		if (StringUtils.isNotBlank(grupoP)) {
			dto.setGrupo(grupoP);
		} else {
			dto.setGrupo(grupo);
		}
		return getAllParametros(dto);
	}

	@Override
	public String getIdByParametroGroup(String grupo) throws ParametroException {
		ParametroGrupoDTO pgrupo = new ParametroGrupoDTO();
		if (StringUtils.isNotBlank(grupo)) {
			pgrupo.setGrupo(grupo);
			List<ParametroGrupoDTO> list = getParametroGrupo(pgrupo);
			if (list != null && list.size() == 1) {
				pgrupo = list.get(0);
			}
		}
		if (pgrupo != null && StringUtils.isNotBlank(pgrupo.getCodigo())) {
			return pgrupo.getParametro();
		}
		return null;
	}

}