/*
 * OrderView.java
 *
 * Created on August 4, 2006, 6:58 PM
 */

package com.floreantpos.ui.views.order;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXCollapsiblePane.Direction;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
/**
 *
 * @author  MShahriar
 */
public class OrderView extends com.floreantpos.swing.TransparentPanel {
	private HashMap<String, JComponent> views = new HashMap<String, JComponent>();

	public final static String VIEW_NAME = "ORDER_VIEW";
	private static OrderView instance;

	private Ticket currentTicket;

	JPanel categoryCollapseButtonPanel;

	boolean othersViewCollapsed = false;
	public static Double taxDineIn;
	public static Double getTaxDineIn() {
		return taxDineIn;
	}

	public static void setTaxDineIn(Double taxDineIn) {
		OrderView.taxDineIn = taxDineIn;
	}

	public static Double taxHomeDelivery;

	public static Double getTaxHomeDelivery() {
		return taxHomeDelivery;
	}

	public static void setTaxHomeDelivery(Double taxHomeDelivery) {
		OrderView.taxHomeDelivery = taxHomeDelivery;
	}

	/** Creates new form OrderView */
	private OrderView() {
		initComponents();
	}

	public void addView(final String viewName, final JComponent view) {
		JComponent oldView = views.get(viewName);
		if (oldView != null) {
			return;
		}

		midContainer.add(view, viewName);
	}

	public void init() {

		setBackground(new Color(128,128,128));
		cardLayout = new CardLayout();
		cardLayout.setHgap(0);
		cardLayout.setVgap(0);
		midContainer.setOpaque(false);
		midContainer.setLayout(cardLayout);

		groupView = new GroupView();
		itemView = new MenuItemView();
		modifierView = new ModifierView();

		addView(GroupView.VIEW_NAME, groupView);
		addView(MenuItemView.VIEW_NAME, itemView);
		addView(ModifierView.VIEW_NAME, modifierView);
		addView("VIEW_EMPTY", new com.floreantpos.swing.TransparentPanel());

		showView("VIEW_EMPTY");

		orderController = new OrderController(this);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		categoryView = new com.floreantpos.ui.views.order.CategoryView();
		ticketView = new com.floreantpos.ui.views.order.TicketView();


		if(TerminalConfig.isCustomNumberDisplay()) {
			try {
				ticketView.setPreferredSize(new Dimension(Integer.parseInt(TerminalConfig.getNumberDisplayWidth()), Integer.parseInt(TerminalConfig.getNumberDisplayHeight())));
			}catch(Exception ex) {
				ticketView.setPreferredSize(new Dimension(450, 300));
			}
		}

		jPanel1 = new com.floreantpos.swing.TransparentPanel();
		midContainer = new com.floreantpos.swing.TransparentPanel();
		othersView = new com.floreantpos.ui.views.order.OthersView(ticketView);

		setLayout(new java.awt.BorderLayout(0,0));
		setBackground(new Color(209,222,235));
		add(categoryView, java.awt.BorderLayout.EAST);

		add(ticketView, java.awt.BorderLayout.WEST);

		jPanel1.setLayout(new java.awt.BorderLayout(0,0));

		jPanel1.setBackground(new java.awt.Color(51, 153, 0));

		JPanel categoryPanel = new JPanel();
		categoryPanel.setLayout(new BorderLayout());
		categoryPanel.setBackground(new Color(35,35,36));

		othersView.getContentPane().setBackground(new Color(35,35,36));
		PosButton otherCollapseButton = new PosButton("...");
		otherCollapseButton.setForeground(Color.WHITE);
		otherCollapseButton.setBackground(new Color(35,35,36));
		otherCollapseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				othersView.setAnimated(false);
				othersView.setCollapsed(!othersViewCollapsed);
				if (othersViewCollapsed) {
					othersViewCollapsed = false;
				} else {
					othersViewCollapsed = true;
				}
				ticketView.focusToScanid();
			}
	
		});

		categoryCollapseButtonPanel = new JPanel();
		categoryCollapseButtonPanel.setLayout(new BorderLayout());
		categoryCollapseButtonPanel.setBackground(new Color(35,35,36));
		categoryCollapseButtonPanel.add(otherCollapseButton, BorderLayout.CENTER);
		categoryCollapseButtonPanel.setPreferredSize(new Dimension(50, 30));
		categoryPanel.add(categoryCollapseButtonPanel, BorderLayout.NORTH);
		categoryPanel.add(othersView, BorderLayout.CENTER);

		jPanel1.add(categoryPanel, BorderLayout.SOUTH);
		jPanel1.add(midContainer, java.awt.BorderLayout.CENTER);
		add(jPanel1, java.awt.BorderLayout.CENTER);

		try {
			taxDineIn = TaxDAO.getInstance().findByName(POSConstants.DINE_IN).getRate();
			taxHomeDelivery = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY).getRate();
		}catch(Exception ex) {
			ex.printStackTrace();
		}


	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.floreantpos.ui.views.order.CategoryView categoryView;
	private com.floreantpos.swing.TransparentPanel jPanel1;
	private com.floreantpos.swing.TransparentPanel midContainer;
	private com.floreantpos.ui.views.order.OthersView othersView;
	private com.floreantpos.ui.views.order.TicketView ticketView;
	// End of variables declaration//GEN-END:variables

	private CardLayout cardLayout;
	private GroupView groupView;
	private MenuItemView itemView;
	private ModifierView modifierView;
	private OrderController orderController;
	public JTextField tfTotal;
	
	public void showView(final String viewName) {
		cardLayout.show(midContainer, viewName);
	}

	public com.floreantpos.ui.views.order.CategoryView getCategoryView() {
		return categoryView;
	}

	public void setCategoryView(com.floreantpos.ui.views.order.CategoryView categoryView) {
		this.categoryView = categoryView;
	}

	public GroupView getGroupView() {
		return groupView;
	}

	public void setGroupView(GroupView groupView) {
		this.groupView = groupView;
	}

	public MenuItemView getItemView() {
		return itemView;
	}

	public void setItemView(MenuItemView itemView) {
		this.itemView = itemView;
	}

	public ModifierView getModifierView() {
		return modifierView;
	}

	public void setModifierView(ModifierView modifierView) {
		this.modifierView = modifierView;
	}

	public com.floreantpos.ui.views.order.TicketView getTicketView() {
		return ticketView;
	}

	public void setTicketView(com.floreantpos.ui.views.order.TicketView ticketView) {
		this.ticketView = ticketView;
	}

	public OrderController getOrderController() {
		return orderController;
	}

	public Ticket getCurrentTicket() {
		return currentTicket;
	}

	public void setCurrentTicket(Ticket currentTicket) {
		this.currentTicket = currentTicket;		
		ticketView.setTicket(currentTicket);
		othersView.setCurrentTicket(currentTicket);
		OrderView.getInstance().getOthersView().clearCustomer();	
		OrderView.getInstance().getOthersView()
		.setCustomer(currentTicket, null);
		groupView.setCurrentTicket(currentTicket);
		itemView.setCurrentTicket(currentTicket);
		categoryView.setCurrentTicket(currentTicket);
		
		resetView();
	}

	public synchronized static OrderView getInstance() {
		if(instance == null) {
			instance = new OrderView();
		}
		return instance;
	}

	public void resetView() {
	}

	public com.floreantpos.ui.views.order.OthersView getOthersView() {
		return othersView;
	}

	@Override
	public void setVisible(boolean aFlag) {
		if(aFlag) {
			try {
				categoryView.initialize();
			}catch(Throwable t) {
				POSMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, t);
			}
		}
		else {
			categoryView.cleanup();
		}
		super.setVisible(aFlag);
	}
}
