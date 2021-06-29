package co.com.japl.ea.interfaces;

import java.lang.reflect.Field;
import java.util.Optional;

import org.pyt.common.common.OptI18n;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.ParametroConstants;

import co.com.japl.ea.common.abstracts.ADto;
import co.com.japl.ea.dto.dto.DocumentosDTO;
import co.com.japl.ea.dto.dto.ParametroDTO;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public final class DocumentosDTOCreator implements IFieldsCreator {

	private DocumentosDTO field;
	private UtilControlFieldFX controlFieldUtil;
	private INotificationMethods notificationMethods;

	public DocumentosDTOCreator(INotificationMethods notificationMethods) {
		controlFieldUtil = new UtilControlFieldFX();
		this.notificationMethods = notificationMethods;
	}

	public String getFormat() {
		return field.getFormat();
	}

	@Override
	public <D extends ADto> void setFieldGeneric(D field) {
		this.field = (DocumentosDTO) field;
	}

	@Override
	public OptI18n getLabelText() {
		return OptI18n.noChange(field.getFieldLabel());
	}

	@Override
	public String getNameField() {
		return field.getFieldName();
	}

	@Override
	public String getToolTip() {
		return field.getPutNameShow();
	}

	@Override
	public Node create() {
		try {
			Field propertie = field.getClaseControlar().getDeclaredField(field.getFieldName());
			var node = controlFieldUtil.getFieldByField(propertie);
			return node;
		} catch (NoSuchFieldException | SecurityException e) {
			notificationMethods.logger().logger("Error en creacion del campo", e);
		}
		return new TextField("Generic Field");
	}

	@Override
	public String getNameFieldToShowInComboBox() {
		return field.getPutNameShow();
	}

	@Override
	public ParametroDTO getParametroDto() {
		var parametro = new ParametroDTO();
		parametro.setGrupo(field.getSelectNameGroup());
		parametro.setEstado(ParametroConstants.COD_ESTADO_PARAMETRO_ACTIVO_STR);
		return parametro;
	}

	@Override
	public String getValueDefault() {
		return field.getFieldDefaultValue();
	}

	@Override
	public Boolean isVisible() {
		var value = field.getFieldIsVisible();
		return hasValueDefault() ? value == null || value : true;
	}

	@Override
	public Boolean hasValueDefault() {
		var value = field.getFieldHasDefaultValue();
		return value != null && value;
	}

	@Override
	public Boolean isRequired() {
		return Optional.ofNullable(!field.getNullable()).orElse(false);
	}

	@Override
	public Integer getOrder() {
		return 0;
	}
}
