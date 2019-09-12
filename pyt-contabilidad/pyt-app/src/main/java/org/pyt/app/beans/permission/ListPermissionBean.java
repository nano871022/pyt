package org.pyt.app.beans.permission;

import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;

import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.dto.system.PermissionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "list_permission.fxml", path = "view/permission")
public class ListPermissionBean extends AGenericInterfacesBean<PermissionDTO> {

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<PermissionDTO> groupUsersSvc;
	@FXML
	private Button btnMod;
	@FXML
	private Button btnDel;
	@FXML
	private TableView<PermissionDTO> tableGeneric;
	@FXML
	private HBox filterGeneric;
	@FXML
	private Label lblTitle;
	@FXML
	private HBox paginator;
	private GridPane gridPane;

	@FXML
	public void initialize() {
		try {
			filtro = new PermissionDTO();
			lblTitle.setText(i18n().valueBundle("fxml.lbl.title.list.permission"));
			gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			filterGeneric.getChildren().addAll(gridPane);
			loadDataModel(paginator, tableGeneric);
			setClazz(PermissionDTO.class);
			configFilters();
			loadColumnsIntoTableView();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void add() {
		try {
			getController(PermissionBean.class).load();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListPermissionBean.delete}",
					i18n().valueBundle(LanguageConstant.LANGUAGE_WARNING_DELETE_ROW));
		} catch (Exception e) {
			error(e);
		}
	}

	public final void setDelete(Boolean valid) {
		try {
			if (valid) {
				var list = dataTable.getSelectedRows();
				if (list != null && list.size() > 0) {
					list.forEach(dto -> {
						try {
							groupUsersSvc.delete(dto, userLogin);
							alerta(String.format(LanguageConstant.LANGUAGE_SUCCESS_DELETE_PERMISSION_ROW_CODE,
									dto.getCodigo()));
						} catch (Exception e) {
							error(e);
						}
					});
					dataTable.search();
				}
			}
		} catch (Exception e) {
			error(e);
		}
	}

	public final void clickTable() {
		if (dataTable.getSelectedRows().size() > 0) {
			btnDel.setVisible(true);
			btnMod.setVisible(true);
		}
	}

	public final void set() {
		try {
			var dto = dataTable.getSelectedRow();
			getController(PermissionBean.class).load(dto);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public TableView<PermissionDTO> getTableView() {
		return tableGeneric;
	}

	@Override
	public GridPane getGridPaneFilter() {
		return gridPane;
	}

	@Override
	public Integer countFieldsInRow() {
		return 2;
	}

	@Override
	public PermissionDTO getFilterToTable(PermissionDTO filter) {
		return filter;
	}
}