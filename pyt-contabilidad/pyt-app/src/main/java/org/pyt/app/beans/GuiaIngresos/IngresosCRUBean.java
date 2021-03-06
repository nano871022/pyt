package org.pyt.app.beans.GuiaIngresos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pyt.common.annotations.FXMLFile;
import org.pyt.common.annotations.Inject;
import org.pyt.common.common.ABean;
import org.pyt.common.common.ADto;
import org.pyt.common.common.SelectList;
import org.pyt.common.common.Table;
import org.pyt.common.common.ValidateValues;
import org.pyt.common.exceptions.EmpleadoException;
import org.pyt.common.exceptions.EmpresasException;
import org.pyt.common.exceptions.IngresoException;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.RepuestoException;
import org.pyt.common.exceptions.ServiciosException;
import org.pyt.common.exceptions.ValidateValueException;

import com.pyt.service.dto.EmpresaDTO;
import com.pyt.service.dto.IngresoDTO;
import com.pyt.service.dto.RepuestoDTO;
import com.pyt.service.dto.ServicioDTO;
import com.pyt.service.dto.TrabajadorDTO;
import com.pyt.service.interfaces.IEmpleadosSvc;
import com.pyt.service.interfaces.IEmpresasSvc;
import com.pyt.service.interfaces.IIngresosSvc;
import com.pyt.service.interfaces.IRepuestosSvc;
import com.pyt.service.interfaces.IServiciosSvc;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una empresa
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "/view/guiaIngresos", file = "ingresos.fxml")
public class IngresosCRUBean extends ABean<IngresoDTO> {
	@Inject(resource = "com.pyt.service.implement.ServiciosSvc")
	private IServiciosSvc serviciosSvc;
	@Inject(resource = "com.pyt.service.implement.RepuestosSvc")
	private IRepuestosSvc repuestosSvc;
	@Inject(resource = "com.pyt.service.implement.IngresosSvc")
	private IIngresosSvc ingresosSvc;
	@Inject(resource = "com.pyt.service.implement.EmpresaSvc")
	private IEmpresasSvc empresaSvc;
	@Inject(resource = "com.pyt.service.implement.EmpleadosSvc")
	private IEmpleadosSvc empleadosSvc;
	@FXML
	private Label codigo;
	@FXML
	private TextField placa;
	@FXML
	private TextField conductorEntrada;
	@FXML
	private TextField conductorSalida;
	@FXML
	private TextField docEntrada;
	@FXML
	private TextField docSalida;
	@FXML
	private TextField propietario;
	@FXML
	private TextField telContacto;
	@FXML
	private ChoiceBox<String> empresa;
	@FXML
	private ChoiceBox<String> servicio;
	@FXML
	private ChoiceBox<String> repuesto;
	@FXML
	private ChoiceBox<String> trabajador;
	@FXML
	private TextField tiempoEstimado;
	@FXML
	private TextField tiempoTrabajo;
	@FXML
	private DatePicker fechaEntrada;
	@FXML
	private DatePicker fechaSalida;
	@FXML
	private TextArea descripcion;
	@FXML
	private Label titulo;
	@FXML
	private Label totalServicio;
	@FXML
	private Label totalRepuesto;
	@FXML
	private TableView<ServicioDTO> tablaServicio;
	@FXML
	private TableView<RepuestoDTO> tablaRepuesto;
	private ValidateValues valid;
	private List<EmpresaDTO> listEmpresas;
	private List<ServicioDTO> listServicio;
	private List<RepuestoDTO> listRepuesto;
	private List<TrabajadorDTO> listTrabajador;
	private final static String field_name = "nombre";
	private final static String field_valor_venta = "valorVenta";
	private final static String field_valor_mano_obra = "valorManoObra";

	@FXML
	public void initialize() {
		NombreVentana = "Agregando Nuevo Ingreso";
		titulo.setText(NombreVentana);
		registro = new IngresoDTO();
		valid = new ValidateValues();
		try {
			listEmpresas = empresaSvc.getAllEmpresas(new EmpresaDTO());
			listServicio = serviciosSvc.getAllServicios(new ServicioDTO());
			listRepuesto = repuestosSvc.getAllRepuestos(new RepuestoDTO());
			listTrabajador = empleadosSvc.getAllTrabajadores(new TrabajadorDTO());
			SelectList.put(empresa, listEmpresas, field_name);
			SelectList.put(servicio, listServicio, field_name);
			SelectList.put(repuesto, listRepuesto, field_name);
			// SelectList.put(trabajador, listTrabajador, "nombre");
		} catch (EmpresasException | ServiciosException | RepuestoException | EmpleadoException e) {
			error(e);
		}
	}

	/**
	 * Pasa los campos de la pantalla el objeto dto
	 */
	private void load() {
		if (registro == null) {
			registro = new IngresoDTO();
		}
		try {
			registro.setCodigo(codigo.getText());
			registro.setPlacaVehiculo(placa.getText());
			registro.setConductorEntrada(conductorEntrada.getText());
			registro.setConductorSalida(conductorSalida.getText());
			registro.setDescripcion(descripcion.getText());
			registro.setDocumentoConductorEntrada(docEntrada.getText());
			registro.setDocumentoConductorSalida(docSalida.getText());
			registro.setFechaEntrada(valid.cast(fechaEntrada.getValue(), Date.class));
			registro.setFechaSalida(valid.cast(fechaSalida.getValue(), Date.class));
			registro.setPropietario(propietario.getText());
			registro.setTelefonoContacto(telContacto.getText());
			registro.setTiempoEstimado(valid.cast(tiempoEstimado.getText(), Long.class));
			registro.setTiempoTrabajado(valid.cast(tiempoTrabajo.getText(), Long.class));
			// registro.setTrabajador(SelectList.get(trabajador, listTrabajador,
			// "persona.nombre"));
			registro.setEmpresa(SelectList.get(empresa, listEmpresas, field_name));
			totalRepuesto.setText(sumar(registro.getRespuestos(),field_valor_venta).toString());
			totalServicio.setText(sumar(registro.getServicios(),field_valor_mano_obra).toString());
		} catch (ValidateValueException e) {
			error(e);
		}
	}

	private void loadFxml() {
		if (registro == null)
			return;
		try {
			codigo.setText(registro.getCodigo());
			placa.setText(registro.getPlacaVehiculo());
			conductorEntrada.setText(registro.getConductorEntrada());
			conductorSalida.setText(registro.getConductorSalida());
			descripcion.setText(registro.getDescripcion());
			docEntrada.setText(registro.getDocumentoConductorEntrada());
			docSalida.setText(registro.getDocumentoConductorSalida());
			fechaEntrada.setValue(valid.cast(registro.getFechaEntrada(), LocalDate.class));
			fechaSalida.setValue(valid.cast(registro.getFechaSalida(), LocalDate.class));
			propietario.setText(registro.getPropietario());
			telContacto.setText(registro.getTelefonoContacto());
			tiempoEstimado.setText(valid.cast(registro.getTiempoEstimado(),String.class));
			tiempoTrabajo.setText(valid.cast(registro.getTiempoTrabajado(),String.class));
			SelectList.selectItem(empresa, listEmpresas, field_name, registro.getEmpresa());
			Table.put(tablaRepuesto, registro.getRespuestos());
			Table.put(tablaServicio, registro.getServicios());
			totalRepuesto.setText(sumar(registro.getRespuestos(), field_valor_venta).toString());
			totalServicio.setText(sumar(registro.getServicios(),field_valor_mano_obra).toString());
			// SelectList.selectItem(trabajador, listTrabajador, "persona.nombre",
			// registro.getTrabajador());
		} catch (ValidateValueException e) {
			error(e);
		}
	}

	public void load(IngresoDTO dto) {
		if (dto != null && dto.getCodigo() != null) {
			registro = dto;
			loadFxml();
			titulo.setText("Modificando Ingreso");
		} else {
			error("El ingreso es invalido para editar.");
			cancel();
		}
	}

	private final <T extends ADto> BigDecimal sumar(List<T> lista, String nombre) {
		BigDecimal suma = new BigDecimal(0);
		try {
			for (T dto : lista) {
				if (valid.isCast(dto.get(nombre), Long.class)) {
					suma = suma.add(valid.cast(dto.get(nombre),BigDecimal.class));
				}
			}
		} catch (ReflectionException | ValidateValueException e) {
			error(e);
		}
		return suma;
	}

	public final void addServicio() {
		if (registro.getServicios() == null) {
			registro.setServicios(new ArrayList<ServicioDTO>());
		}
		registro.getServicios().add(SelectList.get(servicio, listServicio, field_name));
		Table.put(tablaServicio, registro.getServicios());
		totalServicio.setText(sumar(registro.getServicios(),"valorManoObra").toString());
		servicio.getSelectionModel().selectFirst();
	}

	public final void addRepuesto() {
		if (registro.getRespuestos() == null) {
			registro.setRespuestos(new ArrayList<RepuestoDTO>());
		}
		registro.getRespuestos().add(SelectList.get(repuesto, listRepuesto, field_name));
		Table.put(tablaRepuesto, registro.getRespuestos());
		totalRepuesto.setText(sumar(registro.getRespuestos(),"valorVenta").toString());
		repuesto.getSelectionModel().selectFirst();
	}

	public void add() {
		load();
		try {
			if (StringUtils.isNotBlank(registro.getCodigo())) {
				ingresosSvc.update(registro, userLogin);
				notificar("Se guardo el ingreso correctamente.");
				cancel();
			} else {
				registro = ingresosSvc.insert(registro, userLogin);
				codigo.setText(registro.getCodigo());
				notificar("Se agrego el ingreso correctamente.");
				cancel();
			}
		} catch (IngresoException e) {
			error(e);
		}
	}

	public void cancel() {
		getController(ListIngresosBean.class);
	}

}
