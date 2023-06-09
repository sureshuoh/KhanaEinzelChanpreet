/*
 * MiscTicketItemDialog.java
 *
 * Created on September 8, 2006, 10:04 PM
 */

package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;

import com.floreantpos.POSConstants;
import com.floreantpos.model.TicketItem;

/**
 *
 * @author MShahriar
 */
public class MiscTicketItemDialog extends POSDialog {
	private TicketItem ticketItem;
	private String ticketType;
	private JCheckBox cbdiv19;

	/** Creates new form MiscTicketItemDialog */
	public MiscTicketItemDialog(java.awt.Frame parent, boolean modal, String type) {
		super(parent, modal);
		initComponents();
		ticketType = type;
		System.out.println("TicketType: "+ticketType);
		
		noteView1.setNoteLength(30);
		numberSelectionView1.setDecimalAllowed(true);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {

				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						dispose();
						return false;
					}
				}
				return false;
			}
		});
		
		cbdiv19 = new JCheckBox("Sonstiges19%hjgjhh");
		cbdiv19.setFont(new Font(null, Font.BOLD, 24));
//		cbdiv19.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if(cbdiv19.isSelected())
//					cbdiv19.setText("Sonstiges19%");
//				else
//					cbdiv19.setText("Sonstiges7%");	
//
//			}
//		});
		titlePanel1 = new com.floreantpos.ui.TitlePanel();
		transparentPanel1 = new com.floreantpos.swing.TransparentPanel();
		posButton1 = new com.floreantpos.swing.PosButton();
		posButton2 = new com.floreantpos.swing.PosButton();
		transparentPanel2 = new com.floreantpos.swing.TransparentPanel();
		transparentPanel2.setBackground(new Color(35,35,36));
		numberSelectionView1 = new com.floreantpos.ui.views.NumberSelectionView();
		noteView1 = new com.floreantpos.ui.views.NoteView();
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		titlePanel1.setTitle(com.floreantpos.POSConstants.MISC_ITEM);
		getContentPane().add(titlePanel1, java.awt.BorderLayout.NORTH);

		posButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/finish_32.png")));
		posButton1.setText(com.floreantpos.POSConstants.FINISH);
		posButton1.setPreferredSize(new java.awt.Dimension(150, 50));
		posButton1.setBackground(new Color(2, 64, 2));
		posButton1.setForeground(Color.WHITE);
		posButton1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinish(evt);
			}
		});

		transparentPanel1.add(cbdiv19);
		transparentPanel1.add(posButton1);
		transparentPanel1.setBackground(new Color(35,35,36));
		posButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/cancel_32.png")));
		posButton2.setText(com.floreantpos.POSConstants.CANCEL);
		posButton2.setBackground(new Color(125,6,42));
		posButton2.setForeground(Color.WHITE);
		posButton2.setPreferredSize(new java.awt.Dimension(150, 50));
		posButton2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doCancel(evt);
			}
		});

		transparentPanel1.add(posButton2);

		getContentPane().add(transparentPanel1, java.awt.BorderLayout.SOUTH);

		transparentPanel2.setLayout(new java.awt.BorderLayout());

		numberSelectionView1.setPreferredSize(new java.awt.Dimension(220, 392));
		numberSelectionView1.setTitle(com.floreantpos.POSConstants.PRICE);
		numberSelectionView1.setForeground(Color.WHITE);
		transparentPanel2.add(numberSelectionView1, java.awt.BorderLayout.WEST);
		noteView1.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				com.floreantpos.POSConstants.ITEM_NAME,
				javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.DEFAULT_POSITION));
		noteView1.setBackground(new Color(35,35,36));
		transparentPanel2.add(noteView1, java.awt.BorderLayout.CENTER);
		getContentPane().setBackground(new Color(35,35,36));
		getContentPane().add(transparentPanel2, java.awt.BorderLayout.CENTER);
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setBounds((screenSize.width - 786) / 2, (screenSize.height - 452) / 2, 786,
				452);
		noteView1.setText("Sonstiges");

	}// </editor-fold>//GEN-END:initComponents

	private void doCancel(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doCancel
		setCanceled(true);
		ticketItem = null;
		dispose();
	}// GEN-LAST:event_doCancel

	private void doFinish(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doFinish
		setCanceled(false);
		Double Tax;
		String value = numberSelectionView1.getValue();
		int index = 0;
		int multiplier = 0;
		Double amount;
		if ((index = value.indexOf('*')) != -1) {
			multiplier = Integer.parseInt(value.substring(0, index));
			amount = Double.parseDouble(value.substring(index + 1, value.length()));
		} else {
			multiplier = 1;
			amount = Double.parseDouble(value);
		}

		amount = amount/(100.00);
		String itemName = noteView1.getNote().getText();
		
		ticketItem = new TicketItem();
		ticketItem.setItemCount(multiplier);
		ticketItem.setUnitPrice(amount);
		ticketItem.setName(itemName);
		ticketItem.setCategoryName(com.floreantpos.POSConstants.MISC);
		ticketItem.setGroupName(com.floreantpos.POSConstants.MISC);
		ticketItem.setShouldPrintToKitchen(true);
		ticketItem.setPrintorder(1);
		ticketItem.setBeverage(true);
		ticketItem.setItemId(998);
		if(cbdiv19.isSelected())
			Tax = 19.00;
		else
			Tax = 7.00;
		Double subTotal = amount / (1 + (Tax / 100));
		Double taxAmount = amount - subTotal;
		ticketItem.setSubtotalAmount(taxAmount);
		ticketItem.setTaxAmount(taxAmount);
		ticketItem.setTaxRate(Tax);
		ticketItem.setBon(1);
		dispose();
	}// GEN-LAST:event_doFinish

	public TicketItem getTicketItem() {
		return ticketItem;
	}

	/**
	 * @param args
	 *          the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MiscTicketItemDialog(new javax.swing.JFrame(), true,
						POSConstants.DINE_IN).setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.floreantpos.ui.views.NoteView noteView1;
	private com.floreantpos.ui.views.NumberSelectionView numberSelectionView1;
	private com.floreantpos.swing.PosButton posButton1;
	private com.floreantpos.swing.PosButton posButton2;
	private com.floreantpos.ui.TitlePanel titlePanel1;
	private com.floreantpos.swing.TransparentPanel transparentPanel1;
	private com.floreantpos.swing.TransparentPanel transparentPanel2;
	// End of variables declaration//GEN-END:variables

}
