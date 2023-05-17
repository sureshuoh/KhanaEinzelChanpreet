
/*
 * MiscTicketItemmDialog.java
 *
 * Created on September 8, 2018, 01:04 PM
 */

package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.UUID;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.ui.views.order.TicketView;
import com.floreantpos.util.NumberUtil;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Jyrai
 */
public class MiscTicketItemDialog_ extends POSDialog {
	private TicketItem ticketItem;
	private String ticketType;
	boolean isTicketItem;
	boolean drink;
	String cents = "";
	String name;
	Double unitPrice;

	@Override
	public String getName() {
		return name;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public int getCount() {
		return count;
	}

	int count;

	/** Creates new form MiscTicketItemDialog */
	public MiscTicketItemDialog_(java.awt.Frame parent, boolean modal, String type, TicketItem ticketItem, boolean drink,
			String cents) {
		super(parent, modal);
		this.ticketItem = ticketItem;
		this.drink = drink;
		this.cents = cents;
		ticketType = type;
		initComponents();
		artName.requestFocus();
		// noteView1.setNoteLength(30);
		// numberSelectionView1.setDecimalAllowed(true);
		// numberSelectionView1.setNumber(cents);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {

		/*
		 * KeyboardFocusManager.getCurrentKeyboardFocusManager()
		 * .addKeyEventDispatcher(new KeyEventDispatcher() { public boolean
		 * dispatchKeyEvent(KeyEvent e) {
		 * 
		 * if (e.getID() == KeyEvent.KEY_PRESSED) { if (e.getKeyCode() ==
		 * KeyEvent.VK_ESCAPE) { dispose(); return false; } } return false; } });
		 */
		transparentPanel1 = new com.floreantpos.swing.TransparentPanel();
		posButton1 = new com.floreantpos.swing.PosButton();
		posButton2 = new com.floreantpos.swing.PosButton();
		transparentPanel2 = new com.floreantpos.swing.TransparentPanel();
		transparentPanel2.setBackground(new Color(5, 29, 53));
		numberSelectionView1 = new com.floreantpos.ui.views.NumberSelectionView();
		noteView1 = new com.floreantpos.ui.views.NoteView();

		// added
		inputPanel = new com.floreantpos.swing.TransparentPanel();
		if (ticketItem == null)
			inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DIVERSE- SPEICEN / GETRANKE",
					javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
		else
			inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "   EDIT / UPDATE   ",
					javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
		noteView1.setBackground(new Color(5, 29, 53));
		artName = new POSTextField(12);
		artName.setPreferredSize(new Dimension(100, 40));
		artName.setFont(new Font(null, Font.PLAIN, 20));
		artName.requestFocus();
		artName.setRequestFocusEnabled(true);

		JLabel artN = new JLabel("Name");
		artN.setFont(new Font(null, Font.PLAIN, 20));
		artN.setForeground(Color.WHITE);

		JLabel artP = new JLabel("Preis");
		artP.setFont(new Font(null, Font.PLAIN, 20));
		artP.setForeground(Color.WHITE);

		JLabel artAnzal = new JLabel("Anz.");
		artAnzal.setFont(new Font(null, Font.PLAIN, 20));
		artAnzal.setForeground(Color.WHITE);

		JLabel lblEur = new JLabel("");
		lblEur.setFont(new Font(null, Font.PLAIN, 20));
		lblEur.setForeground(Color.WHITE);

		artPrice = new JTextField(5);
		artPrice.setFont(new Font(null, Font.PLAIN, 20));
		artPrice.setPreferredSize(new Dimension(30, 40));
		artPrice.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				try {
					if (artPrice.getText().contains("-"))
						lblEur.setText(
								NumberUtil.formatNumber(Double.parseDouble(artPrice.getText().replace("-", "")) / 100)
										+ " €");
					else
						lblEur.setText(NumberUtil.formatNumber(Double.parseDouble(artPrice.getText()) / 100) + " €");
				} catch (Exception ex) {

				}

			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					if (artPrice.getText().contains("-"))
						lblEur.setText(
								NumberUtil.formatNumber(Double.parseDouble(artPrice.getText().replace("-", "")) / 100)
										+ " €");
					else
						lblEur.setText(NumberUtil.formatNumber(Double.parseDouble(artPrice.getText()) / 100) + " €");
				} catch (Exception ex) {

				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				try {
					if (artPrice.getText().contains("-"))
						lblEur.setText(
								NumberUtil.formatNumber(Double.parseDouble(artPrice.getText().replace("-", "")) / 100)
										+ " €");
					else
						lblEur.setText(NumberUtil.formatNumber(Double.parseDouble(artPrice.getText()) / 100) + " €");
				} catch (Exception ex) {

				}

			}
		});

		artTimes = new POSTextField(2);
		artTimes.setFont(new Font(null, Font.PLAIN, 20));
		artTimes.setPreferredSize(new Dimension(30, 40));
		artTimes.setText("1");
		if (!cents.isEmpty()) {
			int index = 0;
			String multiplier = "";
			String amount = "";
			try {
				if ((index = cents.indexOf('*')) != -1) {					
					artTimes.setText(cents.substring(0, index));
					artPrice.setText(cents.substring(index + 1, cents.length()));
					lblEur.setText(NumberUtil
							.formatNumber(Double.parseDouble(cents.substring(index + 1, cents.length())) / 100) + " €");					
				} else {
					artPrice.setText(cents);
					artTimes.setText("1");
					
					lblEur.setText(NumberUtil.formatNumber(Double.parseDouble(cents) / 100) + " €");
				}
			} catch (Exception ex) {
			}

		} else if (ticketItem != null) {

			try {
				artName.setText(ticketItem.getName());
				String price = String.valueOf(ticketItem.getUnitPrice() * 100);
				price = price.substring(0, price.indexOf("."));
				artPrice.setText(price);
				 
				artTimes.setText(String.valueOf(ticketItem.getItemCount()));
				lblEur.setText(NumberUtil.formatNumber(ticketItem.getUnitPrice()) + " €");
			} catch (Exception ex) {
			}
		}

		QwertyKeyPad keyPad = new QwertyKeyPad();

		inputPanel.setLayout(new MigLayout());
		inputPanel.add(artAnzal, "cell 0 0 0 0, growx");
		inputPanel.add(artTimes, "cell 0 0 0 1, growx");
		inputPanel.add(artN, "cell 0 0 0 2, growx");
		inputPanel.add(artName, "cell 0 0 0 3, growx");
		inputPanel.add(artP, "cell 0 0 0 4, growx");
		inputPanel.add(artPrice, "cell 0 0 0 5, growx");
		inputPanel.add(lblEur, "cell 0 0 0 6, growx");
		inputPanel.add(keyPad, "cell 0 1, growx, center");

		// Till here

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		posButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/finish_32.png")));

		if (ticketItem == null)
			posButton1.setText("     OK   ");
		else
			posButton1.setText(" Update ");
		posButton1.setBackground(new Color(2, 64, 2));
		posButton1.setForeground(Color.WHITE);
		posButton1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinish(evt);
			}
		});

		transparentPanel1.setBackground(new Color(5, 29, 53));
		posButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cancel_32.png")));
		posButton2.setText(com.floreantpos.POSConstants.CANCEL);
		posButton2.setBackground(new Color(125, 6, 42));
		posButton2.setForeground(Color.WHITE);
		posButton2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doCancel(evt);
			}
		});

		inputPanel.add(posButton1, "cell 0 2 0 0 , growx, center");
		inputPanel.add(posButton2, "cell 0 2 0 1 , growx, center");

		cbBeverage = new JCheckBox();
		cbBeverage.setBackground(new Color(5, 29, 53));
		cbBeverage.setText("<html><body><h1>" + "Getraenke" + "</h1></body></html>");
		cbBeverage.setForeground(Color.WHITE);

		cbRabat = new JCheckBox();
		cbRabat.setBackground(new Color(5, 29, 53));
		cbRabat.setText("<html><body><h1>" + "Rabatt" + "</h1></body></html>");
		cbRabat.setForeground(Color.WHITE);

		cbRabat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbRabat.isSelected())
					rabatt = true;
				else
					rabatt = false;
			}
		});
		if (ticketItem == null) {
			if (drink == true) {
				cbBeverage.setSelected(true);
				if (ticketItem == null)
					inputPanel.add(cbBeverage, "cell 0 2 0 2 , growx, center");
				inputPanel.add(cbRabat, "cell 0 2 0 3 , growx, center");
				artName.setText("Diverse19%");
			} else {
				if (ticketItem == null)
					inputPanel.add(cbBeverage, "cell 0 2 0 2 , growx, center");
				inputPanel.add(cbRabat, "cell 0 2 0 3 , growx, center");
				artName.setText("Diverse7%");

			}
		}

		cbBeverage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbBeverage.isSelected()) {
					artName.setText("Diverse19%");
				} else
					artName.setText("Diverse7%");

			}
		});

		getContentPane().setBackground(new Color(5, 29, 53));
		getContentPane().add(inputPanel, java.awt.BorderLayout.CENTER);
		artName.requestFocus();
	}// </editor-fold>//GEN-END:initComponents

	private void doCancel(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doCancel
		setCanceled(true);
		ticketItem = null;
		dispose();
	}// GEN-LAST:event_doCancel

	public JTextField getNote() {
		return noteView1.getNote();
	}

	private void doFinish(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doFinish
		setCanceled(false);
		
		Double Tax;
		String value = artPrice.getText();
		int index = 0;
		int multiplier = 0;
		Double amount = 0.00;

		try {
			if ((index = value.indexOf('*')) != -1) {
				multiplier = Integer.parseInt(value.substring(0, index));
				amount = Double.parseDouble(value.substring(index + 1, value.length()));
				 
			} else {
				multiplier = 1;
				try {
					multiplier = Integer.parseInt(artTimes.getText());
				} catch (Exception ex) {

				}
				
				 
				if (value.contains("-")) {
					value = value.replace("-", "");
				}

				amount = Double.parseDouble(value);
			}
			amount = amount / 100.00;
		} catch (Exception ex) {
			POSMessageDialog.showError("Bitte Preis eingeben!");
			return;
		}

		String itemName = artName.getText();
		if (ticketItem == null) {
			ticketItem = new TicketItem();
			
			ticketItem.setItemCount(multiplier);
			ticketItem.setUnitPrice(amount);
			ticketItem.setName(itemName);			
			ticketItem.setCategoryName(drink ? com.floreantpos.POSConstants.MISC : "SONSTIGES_");
			ticketItem.setGroupName(drink ? com.floreantpos.POSConstants.MISC : "SONSTIGES_");
			ticketItem.setShouldPrintToKitchen(true);
			ticketItem.setPrintorder(1);
			ticketItem.setBeverage(false);
			ticketItem.setItemId(997);
			ticketItem.setBarcode("000");
			ticketItem.setChkRabatt(cbRabat.isSelected());
			
			if (cbBeverage.isSelected()) {
				ticketItem.setBeverage(true);
				ticketItem.setItemId(998);
			} else {
				ticketItem.setBeverage(false);
				ticketItem.setItemId(997);
			}

			 
			/*if (ticketType.toString().compareTo("DINE_IN") == 0&&cbBeverage.isSelected()) {
					
				Tax = Application.getInstance().getDineInTax();
			} else
				Tax = Application.getInstance().getHomeDeleveryTax();*/

			if (drink) {
				Tax = OrderView.taxDineIn;
			} else {
				Tax = OrderView.taxHomeDelivery;
			}
			Double subTotal = amount / (1 + (Tax / 100));
			Double taxAmount = amount - subTotal;
			
			ticketItem.setSubtotalAmount(subTotal);
			Double totalAmount = multiplier * amount;	
			ticketItem.setTotalAmount(totalAmount);
			ticketItem.setTotalAmountWithoutModifiers(totalAmount);
			ticketItem.setTaxAmount(taxAmount);
			ticketItem.setTaxRate(Tax);
			 
			
		}
		dispose();
	}// GEN-LAST:event_doFinish

	public TicketItem getTicketItem() {
		return ticketItem;
	}

	public boolean getRabatt() {
		return rabatt;
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.floreantpos.ui.views.NoteView noteView1;
	private com.floreantpos.ui.views.NumberSelectionView numberSelectionView1;
	private com.floreantpos.swing.PosButton posButton1;
	private com.floreantpos.swing.PosButton posButton2;
	private com.floreantpos.swing.TransparentPanel transparentPanel1;
	private com.floreantpos.swing.TransparentPanel transparentPanel2;
	private com.floreantpos.swing.TransparentPanel inputPanel;
	public POSTextField artName;
	public JTextField artPrice;
	public POSTextField artTimes;

	JCheckBox cbBeverage;
	JCheckBox cbRabat;
	boolean rabatt = false;
	// End of variables declaration//GEN-END:variables

}
