package co.com.japl.ea.app.beans.parametroInventario;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.OptI18n;
import org.pyt.common.common.SelectList;
import org.pyt.common.constants.ParametroConstants;
import org.pyt.common.constants.ParametroInventarioConstants;
import org.pyt.common.constants.PermissionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.components.ConfirmPopupBean;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.common.validates.ValidateValues;
import co.com.japl.ea.dto.dto.inventario.ParametroGrupoInventarioDTO;
import co.com.japl.ea.dto.dto.inventario.ParametroInventarioDTO;
import co.com.japl.ea.dto.interfaces.inventarios.IParametroInventariosSvc;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.exceptions.ParametroException;
import co.com.japl.ea.exceptions.validates.ValidateValueException;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Se encarga de controlar la pantalla de parametros cru
 * 
 * @author Alejandro Parra
 * @since 22-06-2018
 */
@FXMLFile(path = "view/parametroInventarios", file = "parametroInventarios.fxml", nombreVentana = "Parametro CRUD")
public class ParametrosInventariosCRUBean extends ABean<ParametroInventarioDTO> {
	@Inject(resource = "com.pyt.service.implement.inventario.ParametroInventariosSvc")
	private IParametroInventariosSvc parametroSvc;
	@FXML
	private Label codigo;
	@FXML
	private Label lOrden;
	@FXML
	private TextField nombre;
	@FXML
	private TextField descripcion;
	@FXML
	private TextField grupo;
	@FXML
	private TextField valor;
	@FXML
	private TextField valor2;
	@FXML
	private TextField orden;
	@FXML
	private ChoiceBox<String> cGrupo;
	@FXML
	private Label lGrupo;
	@FXML
	private ChoiceBox<String> estado;
	private ParametroInventarioDTO registro;
	private ParametroGrupoInventarioDTO parametroGrupo;
	private ValidateValues validate;
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		validate = new ValidateValues();
		registro = new ParametroInventarioDTO();
		SelectList.put(estado, ParametroConstants.mapa_estados_parametros);
		SelectList.put(cGrupo, ParametroInventarioConstants.mapa_grupo);
		estado.getSelectionModel().selectFirst();
		cGrupo.getSelectionModel().selectFirst();
		lGrupo.setVisible(false);
		cGrupo.setVisible(false);
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::createBtn)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::modifyBtn).icon(Glyph.EDIT)
				.isVisible(edit).setName("fxml.btn.delete").action(this::deleteBtn).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.cancel").action(this::cancelBtn).build();
	}

	/**
	 * Se encarga de cargar el objeto registro.
	 */
	private void load() {
		registro.setNombre(nombre.getText());
		registro.setDescripcion(descripcion.getText());
		registro.setGrupo(grupo.getText());
		registro.setValor(valor.getText());
		registro.setValor2(valor2.getText());
		try {
			if (validate.isCast(orden.getText(), Long.class)) {
				registro.setOrden(validate.cast(orden.getText(), Long.class));
			}
		} catch (ValidateValueException e) {
			error(e);
		}
		registro.setEstado(String.valueOf(ParametroConstants.mapa_estados_parametros.get(estado.getValue())));
		if (StringUtils.isNotBlank(cGrupo.getValue()) && cGrupo.isVisible()) {
			parametroGrupo.setGrupo((String) SelectList.get(cGrupo, ParametroInventarioConstants.mapa_grupo));
		}
	}

	/**
	 * Se encarga de cargar el parametro para poder realizar crud sobre el registro
	 * 
	 * @param dto {@link ParametroInventarioDTO}
	 */
	public void load(ParametroInventarioDTO dto) {
		if (StringUtils.isBlank(dto.getGrupo()) && (dto == null || StringUtils.isBlank(dto.getCodigo()))) {
			registro = new ParametroInventarioDTO();
		}
		lGrupo.setVisible(false);
		cGrupo.setVisible(false);

		if (dto != null && (StringUtils.isBlank(dto.getCodigo()) && StringUtils.isNotBlank(dto.getGrupo())
				&& dto.getGrupo().contains("*"))) {
			registro = dto;
			lGrupo.setVisible(true);
			cGrupo.setVisible(true);
		}

		if (dto != null && StringUtils.isNotBlank(dto.getCodigo()) && !dto.getGrupo().contains("*")) {
			registro = dto;
			assign();
		} else {
			if (StringUtils.isNotBlank(dto.getCodigo())) {
				parametroGrupo = new ParametroGrupoInventarioDTO();
				parametroGrupo.setParametro(dto.getCodigo());
				try {
					List<ParametroGrupoInventarioDTO> list = parametroSvc.getParametroGrupo(parametroGrupo);
					if (list != null && list.size() > 0) {
						parametroGrupo = list.get(0);
					}
					if (list != null && list.size() > 1) {
						error("Se encontraron varios registros para el parametro " + dto.getCodigo());
					}
				} catch (ParametroException e) {
					error(e);
				}
			} else {
				parametroGrupo = new ParametroGrupoInventarioDTO();
			}
			registro = dto;
			grupo.setEditable(false);
			lGrupo.setVisible(true);
			cGrupo.setVisible(true);
			if (registro.getGrupo().equalsIgnoreCase("*")) {
				orden.setVisible(false);
				lOrden.setVisible(false);
			} else {
				try {
					if (StringUtils.isBlank(registro.getCodigo())) {
						registro.setOrden(parametroSvc.totalCount(registro).longValue() + 1);
					}
				} catch (ParametroException e) {
				}
			}
			assign();
		}
		visibleButtons();
	}

	private void assign() {
		codigo.setText(registro.getCodigo());
		nombre.setText(registro.getNombre());
		descripcion.setText(registro.getDescripcion());
		grupo.setText(registro.getGrupo());
		valor.setText(registro.getValor());
		valor2.setText(registro.getValor2());
		if (registro.getOrden() != null) {
			orden.setText(registro.getOrden().toString());
		}
		SelectList.selectItem(estado, ParametroConstants.mapa_estados_parametros, registro.getEstado());
		if (parametroGrupo != null && StringUtils.isNotBlank(parametroGrupo.getCodigo())) {
			if (cGrupo.isVisible() && StringUtils.isNotBlank(parametroGrupo.getGrupo())) {
				SelectList.selectItem(cGrupo, ParametroInventarioConstants.mapa_grupo, parametroGrupo.getGrupo());
			}
		}
		grupo.setEditable(false);
	}

	/**
	 * Se encaga de validar el registro
	 * 
	 * @return
	 */
	private Boolean valid() {
		Boolean valid = true;
		valid &= StringUtils.isNotBlank(registro.getNombre());
		valid &= StringUtils.isNotBlank(registro.getDescripcion());
		valid &= StringUtils.isNotBlank(registro.getGrupo());
		valid &= StringUtils.isNotBlank(registro.getEstado());
		if (!registro.getGrupo().contentEquals("*")) {
			valid &= StringUtils.isNotBlank(registro.getValor());
			valid &= registro.getOrden() != null;
		}
		return valid;
	}

	/**
	 * Se encaraga de crear el registro
	 */
	public void createBtn() {
		load();
		try {
			if (valid()) {
				validParametroGrupo();
				registro = parametroSvc.insert(registro, getUsuario());
				if (StringUtils.isNotBlank(parametroGrupo.getGrupo())) {
					parametroGrupo.setParametro(registro.getCodigo());
					parametroGrupo = parametroSvc.insert(parametroGrupo, getUsuario());
				}
				codigo.setText(registro.getCodigo());
				notificar("Se ha creado el parametro correctamente.");
			} else {
				error("Se encontro problema en la validacion.");
			}
		} catch (ParametroException e) {
			error(e);
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de validar el parametro grupo
	 * 
	 * @throws {@link Exception}
	 */
	private final void validParametroGrupo() throws Exception {
		if (parametroGrupo != null && StringUtils.isNotBlank(parametroGrupo.getGrupo())) {
			ParametroGrupoInventarioDTO pGrupo = new ParametroGrupoInventarioDTO();
			pGrupo.setGrupo(parametroGrupo.getGrupo());
			List<ParametroGrupoInventarioDTO> lista = parametroSvc.getParametroGrupo(pGrupo);
			if (lista.size() > 0) {
				for (ParametroGrupoInventarioDTO grupo : lista) {
					if (StringUtils.isBlank(grupo.getParametro()))
						continue;
					ParametroInventarioDTO parametro = new ParametroInventarioDTO();
					parametro.setCodigo(grupo.getParametro());
					if (grupo != null && registro != null && StringUtils.isNotBlank(registro.getCodigo())
							&& grupo.getParametro().contains(registro.getCodigo())) {
						continue;
					}
					List<ParametroInventarioDTO> parametros = parametroSvc.getAllParametros(parametro);
					if (parametros.size() > 0) {
						throw new Exception("Ya se encuentra asignado este Grupo " + parametroGrupo.getGrupo() + " a "
								+ parametros.get(0).getNombre());
					} else {
						parametroSvc.delete(grupo, getUsuario());
					}
				}
			}
		}
	}

	/**
	 * Se encarga de modificar el registro
	 */
	public void modifyBtn() {
		load();
		try {
			if (valid()) {
				validParametroGrupo();
				parametroSvc.update(registro, getUsuario());
				if (parametroGrupo != null && StringUtils.isNotBlank(parametroGrupo.getGrupo())) {
					parametroGrupo.setParametro(registro.getCodigo());
					parametroSvc.update(parametroGrupo, getUsuario());
				}
				notificar("Se ha modificado el parametro correctamente.");
			} else {
				error("Se encontro problema en la validacion.");
			}
		} catch (ParametroException e) {
			error(e);
		} catch (Exception e) {
			error(e);
		}
	}

	/**
	 * Se encarga de eliminar el registro
	 */
	@SuppressWarnings("unchecked")
	public void deleteBtn() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ParametrosCRUBean.delete}",
					OptI18n.process(val -> "¿Desea eliminar el registro?", null));
			;
		} catch (LoadAppFxmlException e) {
			error(e);
		}
	}

	public final void setDelete(Boolean val) {
		if (val) {
			load();
			try {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					if (parametroGrupo != null && StringUtils.isNotBlank(parametroGrupo.getCodigo())) {
						parametroSvc.delete(parametroGrupo, getUsuario());
					}
					parametroSvc.delete(registro, getUsuario());
					notificar("El parametro fue eliminado correctamente.");
					cancelBtn();
				} else {
					error("La eliminacion del codigo es vacio.");
				}
			} catch (ParametroException e) {
				error(e);
			}
		}
	}

	/**
	 * Se encarga de cancelar el registro y devolverse al bean principal
	 */
	public void cancelBtn() {
		getController(ParametrosInventariosBean.class);
		destroy();
	}

	@Override
	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_CREATE, ParametrosInventariosBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_UPDATE, ParametrosInventariosBean.class, getUsuario().getGrupoUser());
		var delete = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_DELETE, ParametrosInventariosBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);

	}

}