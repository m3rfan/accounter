package com.vimukti.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public abstract class EditTable<R> extends FlowPanel {

	protected AccounterMessages messages = Global.get().messages();
	EditTableImpl<R> impl;

	public EditTable() {
		this(1);
	}

	public EditTable(int numOfRowsPerObject) {
		impl = GWT.create(EditTableImpl.class);
		impl.init(this, numOfRowsPerObject);

	}

	public void addColumn(EditColumn<R> column) {
		impl.addColumn(column);
	}

	public void addColumn(EditColumn<R> column, int rowIndex) {
		impl.addColumn(column, rowIndex);
	}

	public void setEnabled(boolean isEnabled) {
		impl.setEnabled(isEnabled);
	}

	/**
	 * Update a given row
	 * 
	 * @param row
	 */
	public void update(R row) {
		impl.update(row);
	}

	public void updateFromGUI(R row) {
		impl.updateFromGUI(row);
	}

	/**
	 * Add a new row
	 * 
	 * @param row
	 */
	public void add(R row) {
		impl.add(row);
	}

	/**
	 * Delete given row
	 * 
	 * @param row
	 */
	public void delete(R row) {
		impl.delete(row);
	}

	/**
	 * Return copy list of all rows
	 * 
	 * @return
	 */
	public List<R> getAllRows() {
		return impl.getAllRows();
	}

	/**
	 * Remove all rows from table
	 */
	public void clear() {
		impl.clear();
	}

	public void setAllRows(List<R> rows) {
		impl.setAllRows(rows);

	}

	public void addRows(List<R> rows) {
		impl.addRows(rows);
	}

	public List<R> getSelectedRecords(int colInd) {
		return impl.getSelectedRecords(colInd);
	}

	public void checkColumn(int row, int colInd, boolean isChecked) {
		impl.checkColumn(row, colInd, isChecked);
	}

	public boolean isChecked(R row, int colInd) {
		return impl.isChecked(row, colInd);
	}

	public boolean isDisabled() {
		return impl.isDisabled();
	}

	protected void onDelete(R obj) {

	}

	protected abstract void initColumns();

	public List<EditColumn<R>> getColumns() {
		return impl.getColumns();
	}

	public List<EditColumn<R>> getColumns(int row) {
		return impl.getColumns(row);
	}

	public void reDraw() {
		impl.reDraw();
	}

	public void updateColumnHeaders() {
		impl.updateColumnHeaders();
	}

	public void addEmptyMessage(String emptyMessage) {
		impl.addEmptyMessage(emptyMessage);
	}

	protected abstract boolean isInViewMode();

	public Widget getWidget(R row, EditColumn<R> column) {
		return impl.getWidget(row, column);
	}

	public void addEmptyRowAtLast() {

	}

	@Override
	protected void onAttach() {
		impl.onAttach();
	}

	public void createColumns() {
		if (!impl.iscolumnsCreated()) {
			initColumns();
		}
		impl.setcolumnsCreated(true);
	}

}
