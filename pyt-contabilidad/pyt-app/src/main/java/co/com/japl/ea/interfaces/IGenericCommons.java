package co.com.japl.ea.interfaces;

import java.util.List;

import org.pyt.common.abstracts.ADto;
import org.pyt.common.common.UtilControlFieldFX;
import org.pyt.common.validates.ValidateValues;

/**
 * Este es la interface generica la cual se usara por los diferentes
 * implementaciones
 * 
 * @author Alejo Parra
 *
 * @param <L>
 * @param <F>
 */
public interface IGenericCommons<L extends ADto, F extends ADto> extends INotificationMethods {
	final UtilControlFieldFX genericFormsUtils = new UtilControlFieldFX();
	final ValidateValues validateValuesUtils = new ValidateValues();

	enum TypeGeneric {
		FIELD, COLUMN, FILTER
	}

	/**
	 * Muestra la cantidad de columnas a poner en el objeto usado
	 * 
	 * @return {@link Integer}
	 */
	public Integer getMaxColumns(TypeGeneric typeGeneric);

	/**
	 * Contiene la lista de campos genericos a procesar, que fueron configurados en
	 * la tablla indicada
	 * 
	 * @return {@link List} < {@link ADto} >
	 */
	public List<L> getListGenericsFields(TypeGeneric typeGeneric);

	/**
	 * Contiene la clase del dto que se esta usando en los campos
	 * 
	 * @return {@link Class} < {@link ADto} >
	 */
	public Class<F> getClazz();
}