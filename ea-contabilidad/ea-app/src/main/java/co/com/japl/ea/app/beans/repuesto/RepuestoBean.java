package co.com.japl.ea.app.beans.repuesto;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.PermissionConstants;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.components.ConfirmPopupBean;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.dto.dto.inventario.ProductoDTO;
import co.com.japl.ea.dto.interfaces.inventarios.IProductosSvc;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.exceptions.inventario.ProductosException;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear las empresas
 * 
 * @author alejandro parra
 * @since 07/05/2018
 */
@FXMLFile(path = "view/repuesto", file = "listRepuesto.fxml")
public class RepuestoBean extends AGenericInterfacesBean<ProductoDTO> {
	@Inject(resource = "com.pyt.service.implement.inventario.ProductosSvc")
	private IProductosSvc productosSvc;
	@FXML
	private javafx.scene.control.TableView<ProductoDTO> tabla;
	@FXML
	private HBox paginador;
	@FXML
	private HBox buttons;
	@FXML
	private GridPane filter;
	private MultiValuedMap<TypeGeneric, ConfigGenericFieldDTO> fieldsConfig;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.list.spare.pars");
		registro = new ProductoDTO();
		filtro = new ProductoDTO();
		fieldsConfig = new ArrayListValuedHashMap<>();
		findFields(TypeGeneric.FILTER, ProductoDTO.class, RepuestoBean.class)
				.forEach(row -> fieldsConfig.put(TypeGeneric.FILTER, row));
		findFields(TypeGeneric.COLUMN, ProductoDTO.class, RepuestoBean.class)
				.forEach(row -> fieldsConfig.put(TypeGeneric.COLUMN, row));
		loadDataModel(paginador, tabla);
		loadFields(TypeGeneric.FILTER);
		loadColumns();
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::add).icon(Glyph.SAVE)
				.isVisible(save).setName("fxml.btn.edit").action(this::set).icon(Glyph.EDIT).isVisible(edit)
				.setName("fxml.btn.delete").action(this::del).icon(Glyph.REMOVE).isVisible(delete)
				.setName("fxml.btn.view").action(this::set).icon(Glyph.FILE_TEXT).isVisible(view).build();
	}

	public void add() {
		getController(RepuestoCRUBean.class).load();
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{RepuestoBean.delete}",
					i18n().get("mensaje.wish.do.delete.selected.rows"));
		} catch (Exception e) {
			error(e);
		}
	}

	public void setDelete(Boolean valid) {
		try {
			if (!valid)
				return;
			registro = dataTable.getSelectedRow();
			if (registro != null) {
				productosSvc.del(registro, getUsuario());
				notificarI18n("mensaje.spartpart.have.been.deleted");
				dataTable.search();
			} else {
				alertaI18n("err.spartpart.havent.been.selected");
			}
		} catch (ProductosException e) {
			error(e);
		}
	}

	public void set() {
		registro = dataTable.getSelectedRow();
		if (registro != null) {
			getController(RepuestoCRUBean.class).load(registro);
		} else {
			alertaI18n("err.spartpart.havent.been.selected");
		}
	}

	@Override
	protected void visibleButtons() {
		var save = PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_CREATE, RepuestoBean.class,
				getUsuario().getGrupoUser());
		var edit = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_UPDATE,
				RepuestoBean.class, getUsuario().getGrupoUser());
		var delete = dataTable.isSelected() && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_DELETE,
				RepuestoBean.class, getUsuario().getGrupoUser());
		var view = !save && !edit && !delete && PermissionUtil.INSTANCE().havePerm(PermissionConstants.CONST_PERM_READ,
				RepuestoBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
		this.delete.setValue(delete);
		this.view.setValue(view);
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		visibleButtons();
	}

	@Override
	public TableView<ProductoDTO> getTableView() {
		return tabla;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 4;
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		return fieldsConfig.get(typeGeneric).stream().collect(Collectors.toList());
	}

	@Override
	public Class<ProductoDTO> getClazz() {
		return ProductoDTO.class;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return filter;
	}

	@Override
	public ProductoDTO getFilterToTable(ProductoDTO filter) {
		return filter;
	}
}
