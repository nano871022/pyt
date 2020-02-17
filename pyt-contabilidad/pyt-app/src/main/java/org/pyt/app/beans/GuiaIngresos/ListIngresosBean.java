package org.pyt.app.beans.GuiaIngresos;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.app.components.ConfirmPopupBean;
import org.pyt.common.annotations.Inject;
import org.pyt.common.exceptions.IngresoException;

import com.pyt.service.dto.IngresoDTO;
import com.pyt.service.interfaces.IIngresosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.utls.DataTableFXMLUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Bean encargado de crear los ingresos
 * 
 * @author alejandro parra
 * @since 20/07/2018
 */
@FXMLFile(path = "view/guiaIngresos", file = "listIngresos.fxml")
public class ListIngresosBean extends ABean<IngresoDTO> {
	@Inject(resource = "com.pyt.service.implement.IngresosSvc")
	private IIngresosSvc ingresosSvc;
	@FXML
	private javafx.scene.control.TableView<IngresoDTO> tabla;
	@FXML
	private TableColumn<IngresoDTO, String> empresa;
	@FXML
	private TextField placa;
	@FXML
	private TextField descripcion;
	@FXML
	private Button btnMod;
	@FXML
	private HBox paginador;
	@FXML
	private Button eliminar;
	private DataTableFXMLUtil<IngresoDTO, IngresoDTO> dt;

	@FXML
	public void initialize() {
		NombreVentana = i18n().get("fxml.title.list.entry");
		registro = new IngresoDTO();
		empresa.cellValueFactoryProperty().setValue(e -> {
			if (e.getValue().getEmpresa() != null)
				return new SimpleStringProperty(e.getValue().getEmpresa().getNombre());
			return null;
		});
		lazy();
		search();
		eliminar.setVisible(false);
	}

	/**
	 * encargada de crear el objeto que va controlar la tabla
	 */
	public void lazy() {
		dt = new DataTableFXMLUtil<IngresoDTO, IngresoDTO>(paginador, tabla) {
			@Override
			public List<IngresoDTO> getList(IngresoDTO filter, Integer page, Integer rows) {
				List<IngresoDTO> lista = new ArrayList<IngresoDTO>();
				try {
					lista = ingresosSvc.getIngresos(filter, page - 1, rows);
				} catch (IngresoException e) {
					error(e);
				}
				return lista;
			}

			@Override
			public Integer getTotalRows(IngresoDTO filter) {
				Integer count = 0;
				try {
					count = ingresosSvc.getTotalRow(filter);
				} catch (IngresoException e) {
					error(e);
				}
				return count;
			}

			@Override
			public IngresoDTO getFilter() {
				IngresoDTO filtro = new IngresoDTO();
				if (StringUtils.isNotBlank(placa.getText())) {
					filtro.setPlacaVehiculo(placa.getText());
				}
				if (StringUtils.isNotBlank(descripcion.getText())) {
					filtro.setDescripcion(descripcion.getText());
				}
				return filtro;
			}
		};
	}

	public void clickTable() {
		btnMod.setVisible(isSelected());
		eliminar.setVisible(isSelected());
	}

	public void add() {
		getController(IngresosCRUBean.class);
	}

	public void search() {
		dt.search();
	}

	public void del() {
		try {
			controllerPopup(ConfirmPopupBean.class).load("#{ListIngresosBean.delete}",
					i18n().get("mensaje.wish.do.delete.selected.rows"));
		} catch (Exception e) {
			error(e);
		}
	}

	public void setDelete(Boolean valid) {
		try {
			if (!valid)
				return;
			registro = dt.getSelectedRow();
			if (registro != null) {
				ingresosSvc.delete(registro, getUsuario());
				notificarI18n("mensaje.entry.have.been.deleted");
				dt.search();
			} else {
				alertaI18n("mensaje.entry.havent.been.selected");
			}
		} catch (IngresoException e) {
			error(e);
		}
	}

	public void set() {
		registro = dt.getSelectedRow();
		if (registro != null) {
			getController(IngresosCRUBean.class).load(registro);
		} else {
			alertaI18n("mensaje.entry.have.been.selected");
		}
	}

	public Boolean isSelected() {
		return dt.isSelected();
	}

	public DataTableFXMLUtil<IngresoDTO, IngresoDTO> getDt() {
		return dt;
	}
}
