package org.pyt.app.beans.dinamico;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.abstracts.ADto;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.DtoUtils;
import org.pyt.common.common.ListUtils;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.PermissionConstants;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.DocumentosException;
import org.pyt.common.exceptions.GenericServiceException;

import com.pyt.service.dto.ConceptoDTO;
import com.pyt.service.dto.CuentaContableDTO;
import com.pyt.service.dto.DetalleContableDTO;
import com.pyt.service.dto.DocumentosDTO;
import com.pyt.service.dto.ParametroDTO;
import com.pyt.service.interfaces.IDocumentosSvc;
import com.pyt.service.interfaces.IGenericServiceSvc;
import com.pyt.service.interfaces.IParametrosSvc;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.utls.PermissionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Se encarga de controlar el formulario dinamico para documentos
 * 
 * @author Alejandro Parra
 * @since 07-07-2018
 */
@FXMLFile(path = "view/dinamico", file = "detalleContable.fxml")
public class DetalleContableBean extends DinamicoBean<DocumentosDTO, DetalleContableDTO> {
	@Inject(resource = "com.pyt.service.implement.ParametrosSvc")
	private IParametrosSvc parametrosSvc;
	@Inject(resource = "com.pyt.service.implement.DocumentosSvc")
	private IDocumentosSvc documentoSvc;
	@Inject
	private IGenericServiceSvc<ConceptoDTO> conceptoSvc;
	@Inject
	private IGenericServiceSvc<CuentaContableDTO> cuentaContableSvc;
	@FXML
	private VBox central;
	private VBox centro;
	@FXML
	private Label titulo;
	private ParametroDTO tipoDocumento;
	private String codigoDocumento;
	private GridPane gridPane;
	@FXML
	private HBox buttons;

	@FXML
	public void initialize() {
		super.initialize();
		registro = new DetalleContableDTO();
		tipoDocumento = new ParametroDTO();
		gridPane = new GridPane();
		gridPane = new UtilControlFieldFX().configGridPane(gridPane);
		visibleButtons();
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.btn.save").action(this::guardar)
				.icon(Glyph.SAVE).isVisible(save).setName("fxml.btn.edit").action(this::guardar).icon(Glyph.SAVE)
				.isVisible(edit).setName("fxml.btn.cancel").action(this::regresar).build();
	}

	/**
	 * Se encarga de realizar la busqueda de los campos configurados para el tipo de
	 * docuumento seleccionado
	 */
	public final void loadField() {
		DocumentosDTO docs = new DocumentosDTO();
		if (tipoDocumento != null) {
			docs.setDoctype(tipoDocumento);
			docs.setClaseControlar(DetalleContableDTO.class);
			try {
				campos = documentosSvc.getDocumentos(docs);
			} catch (DocumentosException e) {
				error(e);
			}
		}
		central.getChildren().clear();
		central.getStyleClass().add("borderView");
		central.getChildren().add(gridPane);

		genericsLoads();
		loadFields(TypeGeneric.FIELD, StylesPrincipalConstant.CONST_GRID_STANDARD);
	}

	private <D extends ADto> void loadInMapList(String name, List<D> rows) {
		rows.stream().forEach(row -> mapListSelects.put(name, row));
	}

	@SuppressWarnings("unchecked")
	private void genericsLoads() {
		getListGenericsFields(TypeGeneric.FIELD).stream().filter(row -> {
			Object instance;
			try {
				instance = row.getClaseControlar().getDeclaredConstructor().newInstance();
				var clazz = ((ADto) instance).getType(row.getFieldName());
				clazz.asSubclass(ADto.class);
				return true;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | ClassCastException e) {
				logger().logger(e);
				return false;
			}
		}).forEach(row -> {
			try {
				var instance = row.getClaseControlar().getDeclaredConstructor().newInstance();
				if (instance instanceof ADto) {
					var clazz = ((ADto) instance).getType(row.getFieldName());
					var instanceClass = clazz.getDeclaredConstructor().newInstance();
					if (instanceClass instanceof ConceptoDTO) {
						loadInMapList(row.getFieldName(), conceptoSvc.getAll(new ConceptoDTO()));
					} else if (instanceClass instanceof CuentaContableDTO) {
						loadInMapList(row.getFieldName(), cuentaContableSvc.getAll(new CuentaContableDTO()));
					}
				}

			} catch (ClassCastException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				logger().logger(e);
			} catch (GenericServiceException e) {
				logger().logger(e);
			}
		});
	}

	/**
	 * Se encarga de cargar un nuevo registro
	 */
	public final void load(VBox centro, ParametroDTO tipoDoc, String codigoDocumento) {
		registro = new DetalleContableDTO();
		tipoDocumento = tipoDoc;
		this.centro = centro;
		this.codigoDocumento = codigoDocumento;
		this.registro.setCodigoDocumento(this.codigoDocumento);
		incrementarRenglon();
		titulo.setText(concat(titulo.getText(), ": ", tipoDoc.getNombre()));
		visibleButtons();
		loadField();
	}

	public final void load(VBox centro, DetalleContableDTO registro, ParametroDTO tipoDoc, String codigoDocumento) {
		this.registro = registro;
		tipoDocumento = tipoDoc;
		this.centro = centro;
		this.codigoDocumento = codigoDocumento;
		titulo.setText(concat(titulo.getText(), ": ", tipoDoc.getNombre()));
		visibleButtons();
		loadField();
	}

	private void incrementarRenglon() {
		var renglon = 1;
		try {
			logger.info("Start Incrementar renglon");
			if (registro != null && StringUtils.isBlank(registro.getCodigo())) {
				var detail = new DetalleContableDTO();
				detail.setCodigoDocumento(this.codigoDocumento);
				var result = documentosSvc.getAllDetalles(detail);
				logger.info("Registros " + result);
				if (ListUtils.isNotBlank(result)) {
					renglon = Integer.valueOf((int) result.size()) + 1;
					logger.info("Cantidad registros encontrados" + renglon);
				}
			}
		} catch (Exception e) {
			error(e);
		}
		logger.info("End Incrementar renglon");
		registro.setRenglon(renglon);
	}

	/**
	 * Se encarga de guardar todo
	 */
	public final void guardar() {
		if (validFields()) {
			try {
				if (StringUtils.isNotBlank(registro.getCodigo())) {
					documentosSvc.update(registro, getUsuario());
					notificarI18n("mensaje.accountingdetail.have.been.updated.succesfull");
				} else {
					registro.setCodigoDocumento(codigoDocumento);
					registro = documentosSvc.insert(registro, getUsuario());
					notificarI18n("mensaje.accountingdetail.was.inserted");
				}
			} catch (DocumentosException e) {
				error(e);
			}
		}
	}

	/**
	 * Se encarga de cancelar el almacenamiento de los datos
	 */
	public final void regresar() {
		try {
			getController(centro, ListaDetalleContableBean.class).load(centro, tipoDocumento, codigoDocumento);
		} catch (Exception e) {
			error(e);
		}
	}

	@Override
	public GridPane getGridPane(TypeGeneric typeGeneric) {
		return gridPane;
	}

	@Override
	public Integer getMaxColumns(TypeGeneric typeGeneric) {
		return 2;
	}

	@Override
	public MultiValuedMap<String, Object> getMapListToChoiceBox() {
		return mapListSelects;
	}

	@Override
	public Class<DetalleContableDTO> getClazz() {
		return DetalleContableDTO.class;
	}

	@Override
	protected void visibleButtons() {
		var save = DtoUtils.haveNotCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_CREATE, ListaDocumentosBean.class, getUsuario().getGrupoUser());
		var edit = DtoUtils.haveCode(registro) && PermissionUtil.INSTANCE().havePerm(
				PermissionConstants.CONST_PERM_UPDATE, ListaDocumentosBean.class, getUsuario().getGrupoUser());
		this.save.setValue(save);
		this.edit.setValue(edit);
	}

}
