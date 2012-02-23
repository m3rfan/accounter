package com.vimukti.accounter.web.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.utils.HibernateUtil;

public class InventoryUtils {

	public static void remapSalesPurchases(List<Item> items) {
		for (Item item : items) {
			long itemID = item.getID();
			List<TransactionItem> sales = getSales(itemID);
			int inventoryScheme = item.getActiveInventoryScheme();
			List<InventoryDetails> purchases = getPurchases(itemID,
					inventoryScheme);
			adjustSales(itemID, inventoryScheme, sales, purchases);
		}
	}

	private static void adjustSales(long itemId, int inventoryScheme,
			List<TransactionItem> sales, List<InventoryDetails> purchases) {
		if (sales.isEmpty()) {
			return;
		}
		Iterator<InventoryDetails> purchaseIterator = purchases.iterator();
		for (TransactionItem inventorySale : sales) {
			Quantity salesQty = inventorySale.getQuantityCopy();
			Map<Quantity, Double> purchaseForThisSale = new HashMap<Quantity, Double>();
			while (purchaseIterator.hasNext()) {
				InventoryDetails next = purchaseIterator.next();
				Quantity purchaseQty = next.quantity;
				int compareTo = purchaseQty.compareTo(salesQty);
				if (compareTo < 0) {
					purchaseForThisSale.put(purchaseQty, next.cost);
					purchaseIterator.remove();
					salesQty = salesQty.subtract(purchaseQty);
					continue;
				} else if (compareTo > 0) {
					purchaseQty = purchaseQty.subtract(salesQty);
					purchaseForThisSale.put(salesQty.copy(), next.cost);
					next.quantity = purchaseQty;
					salesQty.setValue(0.00D);
					break;
				} else {
					purchaseForThisSale.put(purchaseQty, next.cost);
					purchaseIterator.remove();
					salesQty.setValue(0.00D);
					break;
				}
			}
			Double averageCost = null;
			if (inventoryScheme == CompanyPreferences.INVENTORY_SCHME_AVERAGE) {
				averageCost = getAverageCost(itemId);
			}
			inventorySale
					.modifyPurchases(
							purchaseForThisSale,
							inventoryScheme == CompanyPreferences.INVENTORY_SCHME_AVERAGE,
							averageCost);
		}
	}

	private static List<TransactionItem> getSales(long itemID) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		try {
			session.setFlushMode(FlushMode.COMMIT);
			Query query = session.getNamedQuery("list.InventorySales")
					.setParameter("itemId", itemID);

			return query.list();
		} finally {
			session.setFlushMode(flushMode);
		}
	}

	private static List<InventoryDetails> getPurchases(long itemID,
			int activeInventoryScheme) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		List<InventoryDetails> details = new ArrayList<InventoryDetails>();
		try {
			session.setFlushMode(FlushMode.COMMIT);
			Query query = null;
			if (activeInventoryScheme == CompanyPreferences.INVENTORY_SCHME_LIFO) {
				query = session.getNamedQuery("getPurchasesOfItem.for.LIFO")
						.setParameter("inventoryId", itemID);
			} else {
				query = session.getNamedQuery("getPurchasesOfItem")
						.setParameter("inventoryId", itemID);
			}

			Iterator<Object[]> result = query.list().iterator();
			while (result.hasNext()) {
				Object[] next = result.next();
				Quantity quantity = new Quantity();
				quantity.setValue((Double) next[1]);

				Unit unit = (Unit) session.get(Unit.class, (Long) next[2]);
				quantity.setUnit(unit);
				details.add(new InventoryDetails(quantity, (Double) next[3]));
			}
		} finally {
			session.setFlushMode(flushMode);
		}
		return details;
	}

	public static Double getAverageCost(long itemID) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		try {
			session.setFlushMode(FlushMode.COMMIT);
			Object result = session.getNamedQuery("getAverageCost.of.Item")
					.setParameter("inventoryId", itemID).uniqueResult();
			return (Double) result;
		} finally {
			session.setFlushMode(flushMode);
		}
	}

	static class InventoryDetails {
		Quantity quantity;
		double cost;

		InventoryDetails(Quantity quantity, double cost) {
			this.quantity = quantity;
			this.cost = cost;
		}
	}

}