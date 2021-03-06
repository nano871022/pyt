package org.pyt.common.common;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 * Se encarga de obtener funciones que permitan manejar las tabals sobre javafx
 * 
 * @author Alejandro Parra
 * @since 2018-05-19
 *
 */
public class Table {
	/**
	 * Este metodos e encarga de coger una lista y ponerla dentro de
	 * {@link TableView}
	 * 
	 * @param table
	 *            {@link TableView}
	 * @param lista
	 *            {@link List}
	 */
	public final static <T extends Object> void put(TableView<T> table, List<T> lista) {
		table.getItems().clear();
		ObservableList<T> observable = table.getItems();
		if (lista != null && lista.size() > 0) {
			observable.addAll(lista);
		}
		table.setItems(observable);
	}
}
