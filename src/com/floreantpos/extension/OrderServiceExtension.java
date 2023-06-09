package com.floreantpos.extension;

import javax.swing.JMenu;

import net.xeoh.plugins.base.Plugin;

import com.floreantpos.model.TicketType;
import com.floreantpos.util.TicketAlreadyExistsException;

public interface OrderServiceExtension extends Plugin {
	String getName();
	String getDescription();
	
	void init();
	void createNewTicket(TicketType ticketType) throws TicketAlreadyExistsException;
	void createNewOnlineTicket(TicketType ticketType) throws TicketAlreadyExistsException;
	void setCustomerToTicket(int ticketId);
	void setDeliveryDate(int ticketId);
	void assignDriver(int ticketId);
	boolean finishOrder(int ticketId);
	//int getSelectedTable();
	void createCustomerMenu(JMenu menu);
	void createCustomerButton();
}

