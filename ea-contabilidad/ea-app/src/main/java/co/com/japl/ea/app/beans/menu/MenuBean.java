package co.com.japl.ea.app.beans.menu;

import static org.pyt.common.constants.FxmlBeanConstant.CONST_MENU_FXML;
import static org.pyt.common.constants.FxmlBeanConstant.CONST_PATH_MENU;
import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;
import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_GENERIC_SERVICE;
import static org.pyt.common.constants.LanguageConstant.CONST_MSN_ERR_FIELD_EMPTY;
import static org.pyt.common.constants.StylesPrincipalConstant.CONST_GRID_STANDARD;
import static org.pyt.common.constants.languages.Menu.CONST_MSN_MENU_INSERTED;
import static org.pyt.common.constants.languages.Menu.CONST_MSN_MENU_UPDATED;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.constants.PermissionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesFieldBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.common.validates.ValidFields;
import co.com.japl.ea.dto.interfaces.IConfigGenericFieldSvc;
import co.com.japl.ea.dto.interfaces.IGenericServiceSvc;
import co.com.japl.ea.dto.system.MenuDTO;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = CONST_MENU_FXML, path = CONST_PATH_MENU)
public class MenuBean extends AGenericInterfacesFieldBean<MenuDTO> {
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_GENERIC_SERVICE)
	private IGenericServiceSvc<MenuDTO> menusSvc;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label lblTitle;
	public static final String CONST_FIELD_NAME_MENUS_URL = "url";
	public static final String CONST_FIELD_NAME_MENUS_CLASS_PATH = "classPath";
	public static final String CONST_FIELD_NAME_MENUS_STATE = "state";
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		try {
			registro = new MenuDTO();
			setClazz(MenuDTO.class);
			fields = configGenericSvc.getFieldToFields(this.getClass(), MenuDTO.class);
			this.loadFields(TypeGeneric.FIELD, CONST_GRID_STANDARD);
			visibleButtons();
			ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add)
					.icon(Glyph.NEWSPAPER_ALT).isVisible(save).setName("fxml.btn.edit").action(this::add)
					.icon(Glyph.EDIT).isVisible(edit).setName("fxml.btn.cancel").action(this::cancel).build();
		} catch (Exception e) {
			error(e);
		}
	}

	private final boolean validClass(String clazz) {
		try {
			if (StringUtils.isNotBlank(clazz)) {
				Class.forName(clazz);
			}
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public final void load() {
		registro = new MenuDTO();
	}

	public final void load(MenuDTO dto) {
		registro = dto;
		visibleButtons();
		loadFields(TypeGeneric.FIELD);
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	public final Boolean valid() {
		Boolean valid = true;
		valid &= ValidFields.valid(registro.getUrl(),
				getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MENUS_URL).stream().findAny().get(), true, 1, 100,
				i18n().valueBundle(CONST_MSN_ERR_FIELD_EMPTY));
		valid &= ValidFields.valid(registro.getClassPath(),
				getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MENUS_CLASS_PATH).stream().findAny().get(), true,
				1, 100, i18n().valueBundle(CONST_MSN_ERR_FIELD_EMPTY));
		valid &= ValidFields.valid(registro.getState(),
				getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MENUS_STATE).stream().findAny().get(), true, 1, 2,
				i18n().valueBundle(CONST_MSN_ERR_FIELD_EMPTY));
		valid &= ValidFields.valid(validClass(registro.getClassPath()),
				getMapFields(TypeGeneric.FIELD).get(CONST_FIELD_NAME_MENUS_CLASS_PATH).stream().findAny().get(), true,
				null, null, i18n().valueBundle("err.msn.field.class.path.invalid"));
		return valid;
	}

	public final void add() {
		try {
			if (valid()) {
				if (StringUtils.isBlank(registro.getCodigo())) {
					menusSvc.insert(registro, getUsuario());
					notificar(i18n().valueBundle(CONST_MSN_MENU_INSERTED));
				} else {
					menusSvc.update(registro, getUsuario());
					notificar(i18n().valueBundle(CONST_MSN_MENU_UPDATED));
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void newReg() {
		registro = new MenuDTO();
		loadFields(TypeGeneric.FIELD);
		notificar(i18n().valueBundle("msn.new.menu"));
	}

	public final void cancel() {
		getController(ListMenusBean.class);
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		var map = new ArrayListValuedHashMap<String, Object>();
		return map;
	}

	@Override
	protected void visibleButtons() {
		var save = !DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_CREATE, ListMenusBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE()
				.havePerm(PermissionConstants.CONST_PERM_UPDATE, ListMenusBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}

}