package com.floreantpos.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.ui.ticket.TicketItemRowCreator;
import com.floreantpos.util.NumberUtil;

public class TicketDataSourceDetailed extends AbstractReportDataSource {

	public TicketDataSourceDetailed() {
		super(new String[] { "itemName", "itemQty", "itemSubtotal","ItemId", "unitPrice", "stoer" });
	}


	public TicketDataSourceDetailed(Ticket ticket) {		
		 super(new String[] { "itemName", "itemQty", "itemSubtotal","ItemId", "unitPrice", "stoer" });
		  
		setTicket(ticket);
	}

	boolean refund=false;
	Double rabattPer=0.0;
	
	private void setTicket(Ticket ticket) {
		ArrayList<ITicketItem> rows = new ArrayList<ITicketItem>();

		LinkedHashMap<String, ITicketItem> tableRows = new LinkedHashMap<String, ITicketItem>();
		TicketItemRowCreator.calculateTicketRows(ticket, tableRows);

		if(ticket.isRefunded()!=null)
			refund= ticket.isRefunded();
			
			 	
			List<TicketCouponAndDiscount> couponList = ticket.getCouponAndDiscounts();
			 if (couponList != null) {
				for (Iterator itr = couponList.iterator(); itr.hasNext();) {
					TicketCouponAndDiscount discount = (TicketCouponAndDiscount) itr.next();
					rabattPer = discount.getValue();				
			}
			} 
		
		rows.addAll(tableRows.values());
		setRows(rows);
	}
	Restaurant restaurant = RestaurantDAO.getRestaurant();

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) { 
		ITicketItem item = (ITicketItem) rows.get(rowIndex);
		if(item.getItemCode().compareTo("999") == 0)return null;
		switch (columnIndex) {
		case 0:
			String name=item.getNameDisplay();
			 
			String retour="";
			if(refund&&item.getTotalAmountWithoutModifiersDisplay()<0) {			
				  retour = "Ret.";
				  name = name +" "+retour;
				}
			
			if(TerminalConfig.isShortReceipt())
			{					
				if(TerminalConfig.isItemBarcode()&&item.getItemCountDisplay() != null&&item.getBarCode()!=null) {
					if(item.getItemCountDisplay()>1)
						return ( item.getItemCountDisplay()+ " X " + name+ "\n***" +item.getBarCode()+"***");
					else
					return (name+ "(" +item.getBarCode()+")");
				}else if(item.getItemCountDisplay() != null)
					return ( item.getItemCountDisplay()+ " X " + name);
				else if(item.getNameDisplay().contains("mit"))
				{
					return ( item.getItemCountDisplay()+ " X " + name);
				}
				else
					return null;
			} else {

				if(TerminalConfig.isItemBarcode()&&item.getItemCountDisplay() != null&&item.getBarCode()!=null) {

					if(item.getItemCountDisplay()>1) {
						return ( item.getItemCountDisplay()+ "X " + name);
					} else {
						return ( "1X "+name);
					}

				} else if(item.getItemCountDisplay() != null) {
					if(item.getItemCountDisplay()>1) {
						return ( item.getItemCountDisplay()+ "X " +name);
					} else {
						return "1X " + name;
					}

				} else if(item.getNameDisplay().contains("mit")) {
					if(item.getItemCountDisplay()>1)
						return ( item.getItemCountDisplay()+ "X " +name);
					else
						return name;	
				}
				else
					return null;
			}

		case 1:
			Integer itemCountDisplay = item.getItemCountDisplay();
			if(itemCountDisplay == null) {
				return null;
			}

			//return String.valueOf(itemCountDisplay);
			return null;
		case 2:
			//Double total = item.getTotalAmountWithoutModifiersDisplay()+item.getDiscountAmount();
			 
			 
			Double total = item.getTotalAmountWithoutModifiersDisplay()+item.getDiscountAmount();
			 
			if(item.getDisAmt()!=null&&item.getDisAmt()>0.0) {
				total = total-item.getDisAmt();
			}
			if(total == null) {
				return null;
			}
			if(!TerminalConfig.isShortReceipt()) {
				if(item.getItemCountDisplay()>1) {
					if(restaurant.isItemPriceIncludesTax())
						return	(String.valueOf(NumberUtil.formatNumber(total)).replaceAll(",00", ""));
					else
						return (String.valueOf(NumberUtil.formatNumber(total-item.getTaxAmountWithoutModifiersDisplay())).replaceAll(",00", ""));
				}
				else {
					if(restaurant.isItemPriceIncludesTax())
						return	(String.valueOf(NumberUtil.formatNumber(total)).replaceAll(",00", ""));
					else
						return (String.valueOf(NumberUtil.formatNumber(total-item.getTaxAmountWithoutModifiersDisplay())).replaceAll(",00", ""));
				}
			}else
				if(restaurant.isItemPriceIncludesTax())
					return	(String.valueOf(NumberUtil.formatNumber(total)).replaceAll(",00", ""));
				else
					return (String.valueOf(NumberUtil.formatNumber(total-item.getTaxAmountWithoutModifiersDisplay())).replaceAll(",00", ""));

		case 3:
			return ( item.getItemCode());
			
		case 4:
			return   Double.toString(item.getUnitPriceDisplay());
			
		case 5:
			if(item.getDisAmt()!=null&&item.getDisAmt()>0.00) {				  
				// double txAmt=item.getDisAmt();			 
				  
				 int value = rabattPer.intValue();
					return  Integer.toString(value)+"%";
			}  else {
				return "0%";
			}
			
			
		}

		return null;
	}
}