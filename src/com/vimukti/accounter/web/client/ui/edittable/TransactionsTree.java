package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public abstract class TransactionsTree<T> extends SimplePanel {
	Tree tree;
	protected static AccounterMessages messages = Global.get().messages();

	ICurrencyProvider currencyProvider;
	boolean showTaxCode;
	boolean enableTax;

	private TreeItem billableTree;

	private TreeItem creditsTree;

	private TreeItem chargesTree;

	private TreeItem quotesTree;

	private TreeItem salesOrderTree;

	private TreeItem purchaseOrderTree;

	public TransactionsTree(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
		tree = new Tree() {
		};
		this.add(tree);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
	}

	CheckBox checkBox;

	private void createColumns(boolean isAllrowsSelected) {
		checkBox = new CheckBox(messages.selectAll());
		checkBox.setValue(isAllrowsSelected);
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				for (int i = 0; i < tree.getItemCount(); i++) {
					if (tree.getItem(i) instanceof TreeItem) {
						onSelectionChanged(event.getValue(), tree.getItem(i));
					}
				}
			}
		});
		tree.addItem(checkBox);
	}

	public void setAllrows(ArrayList<EstimatesAndSalesOrdersList> result,
			boolean isNew) {
		clear();
		tree = new Tree() {
		};
		this.add(tree);
		quotesTree = null;
		chargesTree = null;
		creditsTree = null;
		billableTree = null;
		salesOrderTree = null;
		purchaseOrderTree = null;
		boolean isAllrowsSelected = false;
		if (result.isEmpty() && isNew) {
			return;
		} else if (result.isEmpty() && !isNew) {
			isAllrowsSelected = true;
		}
		createColumns(isAllrowsSelected);
		for (final EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : result) {
			addQuotesTreeItem(estimatesAndSalesOrdersList);
		}
	}

	private void addPurchaseOrderTreeItem(
			ClientPurchaseOrder clientPurchaseOrder, boolean isSelected) {
		if (purchaseOrderTree == null) {
			purchaseOrderTree = getTransactionsTree(isSelected,
					messages.purchaseOrderList());
			tree.addItem(purchaseOrderTree);
		}
		String transactionLink = messages.purchaseOrder();
		purchaseOrderTree.addItem(getChildTransactionTree(transactionLink,
				clientPurchaseOrder, isSelected));
	}

	protected void addSalesOrderTransactionTreeItem(ClientEstimate salesOrder,
			boolean isSelected) {
		if (salesOrderTree == null) {
			salesOrderTree = getTransactionsTree(isSelected,
					messages.salesOrderList());
			tree.addItem(salesOrderTree);
		}
		String transactionLink = messages.salesOrder();
		salesOrderTree.addItem(getChildTransactionTree(transactionLink,
				salesOrder, isSelected));
	}

	private void addQuotesTreeItem(EstimatesAndSalesOrdersList result) {
		Accounter.createGETService().getObjectById(AccounterCoreType.ESTIMATE,
				result.getTransactionId(), new AsyncCallback<ClientEstimate>() {

					@Override
					public void onFailure(Throwable caught) {
						Accounter.showError(messages
								.unableToLoadRequiredQuote());
					}

					@Override
					public void onSuccess(ClientEstimate result) {
						if (result != null) {
							addAllQuoteTransactionTreeItem(result, false);
						}
					}
				});
	}

	protected void addAllQuoteTransactionTreeItem(ClientEstimate result,
			boolean isSelected) {
		if (result.getEstimateType() == ClientEstimate.QUOTES) {
			addQuotesTransactionTreeItem(result, isSelected);
		} else if (result.getEstimateType() == ClientEstimate.CHARGES) {
			addChargesTransactionTreeItem(result, isSelected);
		} else if (result.getEstimateType() == ClientEstimate.BILLABLEEXAPENSES
				|| result.getEstimateType() == ClientEstimate.DEPOSIT_EXAPENSES) {
			addBillableTransactionTreeItem(result, isSelected);
		} else if (result.getEstimateType() == ClientEstimate.CREDITS) {
			addCreditsTransactionTreeItem(result, isSelected);
		} else if (result.getEstimateType() == ClientEstimate.SALES_ORDER) {
			addSalesOrderTransactionTreeItem(result, isSelected);
		}
		setEnabled(isinViewMode());
	}

	private TreeItem getChildTransactionTree(String transactionLink,
			final ClientTransaction transaction, boolean isSelected) {
		final TreeItem transactionTree = new TreeItem();
		transactionTree.setUserObject(transaction);
		CheckBox checkBox = new CheckBox();
		checkBox.setValue(isSelected);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(checkBox);
		horizontalPanel.addStyleName("transactionPanel");
		transactionTree.setWidget(horizontalPanel);
		Anchor transactionLabel = new Anchor(transactionLink) {
			@Override
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
				if (isAttached()) {
					onDetach();
					if (enabled) {
						sinkEvents(Event.ONCLICK);
					} else {
						unsinkEvents(Event.ONCLICK);
					}
					onAttach();
				}
			}
		};
		horizontalPanel.add(transactionLabel);
		double estimateTotal = transaction.getTotal();
		if (currencyProvider.getTransactionCurrency().getID() != transaction
				.getCurrency()) {
			estimateTotal = estimateTotal
					/ currencyProvider.getCurrencyFactor();
		}
		horizontalPanel
				.add(new Label(messages.totalWithCurrencyName(transactionLink)
						+ " : " + estimateTotal));
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				onSelectionChanged(event.getValue(), transactionTree);
			}
		});
		transactionLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				if (transaction instanceof ClientPurchaseOrder) {
					UIUtils.runAction(transaction,
							ActionFactory.getPurchaseOrderAction());
					return;
				}
				ClientEstimate estimate = (ClientEstimate) transaction;
				if (estimate.getEstimateType() == ClientEstimate.BILLABLEEXAPENSES) {
					openEnterBillView(estimate);
				} else if (estimate.getEstimateType() == ClientEstimate.DEPOSIT_EXAPENSES) {
					openDepositView(estimate);
				} else {
					UIUtils.runAction(transaction, ActionFactory
							.getNewQuoteAction(estimate.getEstimateType()));
				}
			}
		});
		TransactionItemsTable itemsTable = new TransactionItemsTable(
				currencyProvider);
		transactionTree.addItem(itemsTable);
		List<ClientTransactionItem> transactionItems = transaction
				.getTransactionItems();
		itemsTable.setAllRows(transactionItems);
		return transactionTree;
	}

	protected void openDepositView(ClientEstimate estimate) {

		Accounter.createHomeService().getDepositByEstimateId(estimate.getID(),
				new AsyncCallback<ClientMakeDeposit>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(ClientMakeDeposit result) {
						UIUtils.runAction(result,
								ActionFactory.getDepositAction());
					}
				});
	}

	protected void openEnterBillView(ClientEstimate estimate) {

		Accounter.createHomeService().getEnterBillByEstimateId(
				estimate.getID(), new AsyncCallback<ClientEnterBill>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(ClientEnterBill result) {
						UIUtils.runAction(result,
								ActionFactory.getEnterBillsAction());
					}
				});
	}

	private void addBillableTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (billableTree == null) {
			billableTree = getTransactionsTree(isSelected,
					messages.billabelList());
			tree.addItem(billableTree);
		}
		String transactionLink = messages.billabe();
		billableTree.addItem(getChildTransactionTree(transactionLink, estimate,
				isSelected));
	}

	public void refreshBillableTransactionTree() {
		if (billableTree == null) {
			return;
		}
		for (int p = 0; p < billableTree.getChildCount(); p++) {
			TreeItem child = billableTree.getChild(p);
			ClientEstimate estimate = (ClientEstimate) child.getUserObject();
			if (currencyProvider.getTransactionCurrency().getID() == estimate
					.getCurrency()) {
				continue;
			}
			double estimateTotal = estimate.getTotal();
			estimateTotal = estimateTotal
					/ currencyProvider.getCurrencyFactor();
			String totalMsg = messages
					.totalWithCurrencyName(messages.billabe())
					+ " : "
					+ estimateTotal;
			TransactionItemsTable table = (TransactionItemsTable) child
					.getChild(0).getWidget();
			for (ClientTransactionItem item : table.getAllRows()) {
				table.update(item);
			}
			Label totalLabel = (Label) ((HorizontalPanel) child.getWidget())
					.getWidget(2);
			totalLabel.setText(totalMsg);
		}
		updateTransactionTreeItemTotals();
	}

	private TreeItem getTransactionsTree(boolean isSelected, String message) {
		final TreeItem treeItem = new TreeItem();
		CheckBox billableSelection = new CheckBox(message);
		billableSelection.setEnabled(isinViewMode());
		billableSelection.setValue(isSelected);
		treeItem.setWidget(billableSelection);
		billableSelection
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						onSelectionChanged(event.getValue(), treeItem);
					}
				});
		return treeItem;
	}

	private void addQuotesTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (quotesTree == null) {
			quotesTree = getTransactionsTree(isSelected, messages.quotesList());
			tree.addItem(quotesTree);
		}
		String transactionLink = messages.quote();
		quotesTree.addItem(getChildTransactionTree(transactionLink, estimate,
				isSelected));
	}

	public abstract boolean isinViewMode();

	private void addChargesTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (chargesTree == null) {
			chargesTree = getTransactionsTree(isSelected,
					messages.chargesList());
			tree.addItem(chargesTree);
		}
		String transactionLink = messages.charge();
		chargesTree.addItem(getChildTransactionTree(transactionLink, estimate,
				isSelected));
	}

	private void addCreditsTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (creditsTree == null) {
			creditsTree = getTransactionsTree(isSelected,
					messages.creditsList());
			tree.addItem(creditsTree);
		}
		creditsTree.addItem(getChildTransactionTree(messages.credit(),
				estimate, isSelected));
	}

	protected void onSelectionChanged(Boolean value, TreeItem treeItem) {
		Widget treeWidget = treeItem.getWidget();
		if (treeWidget instanceof CheckBox) {
			CheckBox box = (CheckBox) treeWidget;
			box.setValue(value);
			treeItem.setState(value);
		}

		ClientTransaction transaction = (ClientTransaction) treeItem
				.getUserObject();
		if (transaction != null) {
			setTransactionDate(transaction.getDate());
		}
		for (int i = 0; i < treeItem.getChildCount(); i++) {
			TreeItem child = treeItem.getChild(i);
			transaction = (ClientTransaction) child.getUserObject();
			if (transaction != null) {
				setTransactionDate(transaction.getDate());
			}
			child.setState(value);
			Widget widget = child.getWidget();
			if (widget instanceof CheckBox) {
				CheckBox box = (CheckBox) widget;
				box.setValue(value);
			}

			if (widget != null && widget instanceof HorizontalPanel) {
				HorizontalPanel hPanel = (HorizontalPanel) widget;
				Widget checkBox = hPanel.getWidget(0);
				if (checkBox instanceof CheckBox) {
					CheckBox childBox = (CheckBox) checkBox;
					childBox.setValue(value);
				}
			}
		}
		updateTransactionTreeItemTotals();
	}

	public void updateTransactionTreeItemTotals() {
		grandTotal = 0.0;
		lineTotal = 0.0;
		totalTax = 0.0;
		List<ClientTransaction> selectedRecords = getSelectedRecords();
		for (ClientTransaction clientTransaction : selectedRecords) {
			if (clientTransaction instanceof ClientEstimate) {
				ClientEstimate estimate = (ClientEstimate) clientTransaction;
				updateEstimateTotal(estimate);
			} else {
				grandTotal += clientTransaction.getTotal();
				lineTotal += clientTransaction.getNetAmount();
			}
		}
		updateTransactionTotal();
	}

	@Override
	public void clear() {
		tree.clear();
		super.clear();
	}

	public List<ClientTransaction> getSelectedRecords() {
		List<ClientTransaction> selected = new ArrayList<ClientTransaction>();
		for (int i = 1; i < tree.getItemCount(); i++) {
			TreeItem item = tree.getItem(i);
			for (int j = 0; j < item.getChildCount(); j++) {
				TreeItem child = item.getChild(j);
				Widget childWidget = child.getWidget();
				if (childWidget != null
						&& childWidget instanceof HorizontalPanel) {
					HorizontalPanel hPanel = (HorizontalPanel) childWidget;
					Widget checkBox = hPanel.getWidget(0);
					if (checkBox instanceof CheckBox) {
						CheckBox childBox = (CheckBox) checkBox;
						if (childBox.getValue()) {
							ClientTransaction userObject = (ClientTransaction) child
									.getUserObject();
							selected.add(userObject);
						}
					}
				}

			}
		}
		return selected;
	}

	double lineTotal = 0.0;
	double totalTax = 0.0;
	double grandTotal = 0.0;

	private void updateEstimateTotal(ClientEstimate transaction) {
		if (transaction.getEstimateType() == ClientEstimate.CREDITS) {
			grandTotal -= transaction.getTotal();
			lineTotal -= transaction.getNetAmount();
			totalTax -= transaction.getTaxTotal();
		} else if (transaction.getEstimateType() == ClientEstimate.BILLABLEEXAPENSES
				&& currencyProvider.getTransactionCurrency().getID() != transaction
						.getCurrency()) {
			grandTotal += transaction.getTotal()
					/ currencyProvider.getCurrencyFactor();
			lineTotal += transaction.getNetAmount()
					/ currencyProvider.getCurrencyFactor();
			totalTax += transaction.getTaxTotal()
					/ currencyProvider.getCurrencyFactor();
		} else {
			grandTotal += transaction.getTotal();
			lineTotal += transaction.getNetAmount();
			totalTax += transaction.getTaxTotal();
		}
	}

	public abstract void updateTransactionTotal();

	public Double getTotalTax() {
		return totalTax;
	}

	public Double getLineTotal() {
		return lineTotal;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setRecords(List<ClientTransaction> estimates) {
		for (ClientTransaction estimate : estimates) {
			if (estimate instanceof ClientEstimate) {
				addEstimateTreeItemRow((ClientEstimate) estimate);
			} else if (estimate instanceof ClientPurchaseOrder) {
				addPurchaseOrderTreeItem((ClientPurchaseOrder) estimate, true);
			}
		}
	}

	private void addEstimateTreeItemRow(ClientEstimate estimate) {
		addAllQuoteTransactionTreeItem(estimate, true);
	}

	public void setEnabled(boolean isEnabled) {
		tree.setAnimationEnabled(isEnabled);
		if (tree.getItem(0) == null) {
			return;
		}
		Widget widget = tree.getItem(0).getWidget();
		if (widget == null) {
			return;
		}
		if (widget instanceof CheckBox) {
			((CheckBox) widget).setEnabled(isEnabled);
		}
		for (int i = 0; i < tree.getItemCount(); i++) {
			TreeItem item = tree.getItem(i);
			enableTreeItem(item, isEnabled);
		}
	}

	private void enableTreeItem(TreeItem item, boolean isEnabled) {
		Widget widget = item.getWidget();
		if (widget != null && widget instanceof CheckBox) {
			((CheckBox) widget).setEnabled(isEnabled);
		}
		for (int j = 0; j < item.getChildCount(); j++) {
			TreeItem child = item.getChild(j);
			Widget childWidget = child.getWidget();
			if (childWidget != null && childWidget instanceof CheckBox) {
				CheckBox childBox = (CheckBox) childWidget;
				childBox.setEnabled(isEnabled);
			}
			if (childWidget != null && childWidget instanceof HorizontalPanel) {
				HorizontalPanel hPanel = (HorizontalPanel) childWidget;
				Widget checkBox = hPanel.getWidget(0);
				if (checkBox != null && checkBox instanceof CheckBox) {
					CheckBox childBox = (CheckBox) checkBox;
					childBox.setEnabled(isEnabled);
				}
				Widget widget2 = hPanel.getWidget(1);
				if (widget2 != null && widget2 instanceof Anchor) {
					Anchor childAnchor = (Anchor) widget2;
					childAnchor.setEnabled(isEnabled);
					if (isEnabled) {
						childAnchor.removeStyleName("editTable_disable_anchor");
						childAnchor.addStyleName("editTable_enable_anchor");
					} else {
						childAnchor.removeStyleName("editTable_enable_anchor");
						childAnchor.addStyleName("editTable_disable_anchor");
					}
				}
			}
			enableTreeItem(child, isEnabled);
		}
	}

	public abstract void setTransactionDate(ClientFinanceDate clientFinanceDate);

	public boolean validateTree() {
		return getSelectedRecords().size() == 0 ? false : true;
	}

	public void setAllrows(List<PurchaseOrdersAndItemReceiptsList> result,
			boolean isNew) {
		clear();
		tree = new Tree() {
		};
		this.add(tree);
		quotesTree = null;
		chargesTree = null;
		creditsTree = null;
		billableTree = null;
		salesOrderTree = null;
		purchaseOrderTree = null;
		boolean isAllrowsSelected = false;
		if (result.isEmpty() && isNew) {
			return;
		} else if (result.isEmpty() && !isNew) {
			isAllrowsSelected = true;
		}
		createColumns(isAllrowsSelected);
		for (PurchaseOrdersAndItemReceiptsList clientPurchaseOrder : result) {
			Accounter.createGETService().getObjectById(
					AccounterCoreType.PURCHASEORDER,
					clientPurchaseOrder.getTransactionId(),
					new AsyncCallback<ClientPurchaseOrder>() {

						@Override
						public void onFailure(Throwable caught) {
							Accounter.showError(messages
									.unableToLoadRequiredQuote());
						}

						@Override
						public void onSuccess(ClientPurchaseOrder result) {
							if (result != null) {
								addPurchaseOrderTreeItem(result, false);
							}
						}
					});
		}
	}

}
