package org.pyt.app.components;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;
import org.pyt.common.exceptions.ReflectionException;
import org.pyt.common.exceptions.validates.ValidateValueException;

import com.pyt.service.pojo.GenericPOJO;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public interface IGenericColumn<T extends ADto> extends IGenericCommon<T> {

	Map<String, GenericPOJO<T>> getColumns();

	void setColumns(Map<String, GenericPOJO<T>> columns);

	TableView<T> getTableView();

	/**
	 * Se encarga de configurar el mapa de filtros y agregar los campos de filtros a
	 * la pantalla
	 * @throws NoSuchMethodException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * 
	 * @throws {@link IllegalAccessException}
	 */
	default void configColumns() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
		var filters = getMapFieldsByObject(getInstaceOfGenericADto(), GenericPOJO.Type.COLUMN);
		setColumns(filters);
		getColumns().forEach((key, value) -> {
 				var column = new TableColumn<T, String>(getI18n().valueBundle(
 						LanguageConstant.GENERIC_FORM_COLUMN + getClazz().getSimpleName() + "." + value.getNameShow()));
 				column.setCellValueFactory(table -> {
					try {
						var sop = new SimpleObjectProperty<String>();
						sop.setValue(validateValues.cast(table.getValue().get(value.getField().getName()),String.class));
						return sop;
					} catch (ReflectionException | ValidateValueException e) {
						getLogger().logger(e);
					}
					return null;
				});
				getTableView().getColumns().add(column);
		});
		getTableView().getStyleClass().add(StylesPrincipalConstant.CONST_TABLE_CUSTOM);

	}
}