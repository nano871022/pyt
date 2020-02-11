package org.pyt.app.beans.help;

import static org.pyt.common.constants.InsertResourceConstants.CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD;

import java.util.List;

import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;

import com.pyt.service.interfaces.IConfigGenericFieldSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.AGenericInterfacesBean;
import co.com.japl.ea.dto.system.ConfigGenericFieldDTO;
import co.com.japl.ea.dto.system.HelpDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@FXMLFile(file = "list_help.fxml", path = "view/help")
public class ListHelpBean extends AGenericInterfacesBean<HelpDTO> {

	@Inject(resource = "com.pyt.service.implement.GenericServiceSvc")
	private IGenericServiceSvc<HelpDTO> helpsSvc;
	@FXML
	private Button btnMod;
	@FXML
	private Button btnDel;
	@FXML
	private TableView<HelpDTO> tableGeneric;
	@FXML
	private HBox filterGeneric;
	@FXML
	private Label lblTitle;
	@FXML
	private HBox paginator;
	private GridPane gridPane;
	@Inject(resource = CONST_RESOURCE_IMPL_SVC_CONFIG_GENERIC_FIELD)
	private IConfigGenericFieldSvc configGenericSvc;
	private List<ConfigGenericFieldDTO> listFilters;
	private List<ConfigGenericFieldDTO> listColumns;

	@FXML
	public void initialize() {
		try {
			filtro = new HelpDTO();
			lblTitle.setText(i18n().valueBundle("fxml.lbl.title.list.helps").get());
			gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			filterGeneric.getChildren().addAll(gridPane);
			listFilters = configGenericSvc.getFieldToFilters(this.getClass(), HelpDTO.class);
			listColumns = configGenericSvc.getFieldToColumns(this.getClass(), HelpDTO.class);
			loadDataModel(paginator, tableGeneric);
			loadFields(TypeGeneric.FIELD);
			loadColumns();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void add() {
		try {
			getController(HelpBean.class).load();
		} catch (Exception e) {
			error(e);
		}
	}

	public final void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListHelpBean.delete}",
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
							helpsSvc.delete(dto, getUsuario());
							alerta(String.format(LanguageConstant.LANGUAGE_SUCCESS_DELETE_HELPS_ROW_CODE,
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
			getController(HelpBean.class).load(dto);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public TableView<HelpDTO> getTableView() {
		return tableGeneric;
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGneric) {
		return gridPane;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public HelpDTO getFilterToTable(HelpDTO filter) {
		return filter;
	}

	@Override
	public void selectedRow(MouseEvent eventHandler) {
		set();
	}

	@Override
	public List<ConfigGenericFieldDTO> getListGenericsFields(TypeGeneric typeGeneric) {
		switch (typeGeneric) {
		case FILTER:
			return listFilters;
		case COLUMN:
			return listColumns;
		default:
			break;
		}
		return null;
	}

	@Override
	public Class<HelpDTO> getClazz() {
		return HelpDTO.class;
	}
}
