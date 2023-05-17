/*
 * ReportViewer.java
 *
 * Created on September 17, 2006, 11:38 PM
 */

package com.floreantpos.report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Salesreportdb;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.SalesReportDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.dialog.ErrorMessageDialog;
import com.floreantpos.ui.dialog.OpenTicketDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;
import com.floreantpos.ui.views.order.OrderView;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author MShahriar
 */
public class ReportViewer extends javax.swing.JPanel {
	private Report report;
	private POSDialog dialog;
	private Restaurant myrest;
	/** Creates new form ReportViewer */
	public ReportViewer() {
		initComponents();
	}

	public ReportViewer(Report report) {
		initComponents();
		setReport(report);
		if (report.getType() != 1)
			if(StringUtils.isNotEmpty(POSConstants.AKTUALISIEREN))
				btnPreview.setText(POSConstants.AKTUALISIEREN);
			else
			    btnRefresh.setText("AKTUALISIEREN");
		
		doRefreshReport(false);
	}

	public void setDialog(POSDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	User currentUser;
	private void initComponents() {
		setVisible(true);
		reportConstraintPanel = new com.floreantpos.swing.TransparentPanel();
		jLabel2 = new javax.swing.JLabel();

		dpStartDate = UiUtil.getCurrentMonthStart();
		myrest = RestaurantDAO.getRestaurant();
		dpStartDate.getMonthView().setFont(
				new Font("Times New Roman", Font.PLAIN, 28));
		dpStartDate.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		dpEndDate = UiUtil.getCurrentMonthEnd();
		dpEndDate.getMonthView().setFont(
				new Font("Times New Roman", Font.PLAIN, 28));
		dpEndDate.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		dpStartDate.setDate(myrest.getStartToday()!=null?myrest.getStartToday():new Date());
		dpEndDate.setDate(new Date());
		jLabel3 = new javax.swing.JLabel();
		jLabel2.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jLabel3.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnRefresh = new javax.swing.JButton();
		btnRefresh.setSize(new Dimension(50, 50));	

		btnRefresh.setBackground(new Color(102, 255, 102));
		if (Application.getEnabledGenerateReport()) {
			btnRefresh.setEnabled(true);
		}
		reportPanel = new com.floreantpos.swing.TransparentPanel();

		setLayout(new java.awt.BorderLayout(5, 5));

		jLabel2.setText("Datum" + ":");
		if(StringUtils.isNotEmpty(POSConstants.DATE))
			jLabel2.setText(POSConstants.DATE+":");

		jLabel3.setText(com.floreantpos.POSConstants.END_DATE + ":");

		btnRefresh.setText("Z-ABSCHLUSS");
		if(StringUtils.isNotEmpty(POSConstants.Z_ABSCHLUSS))
			btnRefresh.setText(POSConstants.Z_ABSCHLUSS);

		dpStartDate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doRefreshReport(false);				
			}
		});

		btnRefresh.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnRefresh.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				if (report.getType() == 1) {
					ErrorMessageDialog dialog;
					if(StringUtils.isNotEmpty(POSConstants.Sind_Sie_sicher))
						dialog = new ErrorMessageDialog(
								POSConstants.Sind_Sie_sicher, true, true);
					else
					    dialog = new ErrorMessageDialog(
							"Sind Sie sicher ?", true, true);
					
					dialog.pack();
					dialog.open();
					if(!dialog.isCancelled()) {
						doRefreshReport(true);
						if (currentUser != null && !currentUser.getFirstName().equals("Master"))
							btnRefresh.setEnabled(true);
					}

				}

			}
		});		
		currentUser = Application.getCurrentUser();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		if (currentUser != null && !currentUser.getFirstName().equals("Master")) {
			List<Salesreportdb> salesReports = SalesReportDAO.getInstance().findByDate(dpStartDate.getDate());
			if (salesReports != null && !salesReports.isEmpty()) {			
				btnRefresh.setEnabled(true);
				
			}
		}


		btnPreview = new JButton();
		btnPreview.setText("X-ABSCHLUSS");
		if(StringUtils.isNotEmpty(POSConstants.X_ABSCHLUSS))
			btnPreview.setText(POSConstants.X_ABSCHLUSS);
		 
		btnPreview.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnPreview.setBackground(new Color(102, 178, 255));

		btnPreview.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doRefreshReport(false);
			}
		});

		btnPrint = new JButton();
		btnPrint.setText(POSConstants.PRINT);
		btnPrint.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnPrint.setBackground(new Color(102, 178, 255));

		btnPrint.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doPrint();
			}
		});

		btnCancel = new JButton();
		btnCancel.setText(POSConstants.CANCEL);
		btnCancel.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnCancel.setBackground(new Color(255, 153, 153));

		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (dialog != null)
					dialog.dispose();
			}
		});

		org.jdesktop.layout.GroupLayout reportConstraintPanelLayout = new org.jdesktop.layout.GroupLayout(
				reportConstraintPanel);
		reportConstraintPanel.setLayout(reportConstraintPanelLayout);
		reportConstraintPanelLayout
		.setHorizontalGroup(reportConstraintPanelLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(
						reportConstraintPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.add(
								reportConstraintPanelLayout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.LEADING)
								.add(
										reportConstraintPanelLayout
										.createSequentialGroup()
										.add(
												reportConstraintPanelLayout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.LEADING)
												.add(jLabel2))
										.add(15, 15, 15)
										.add(
												reportConstraintPanelLayout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.LEADING,
														false)
												.add(
														dpStartDate,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
										//										.addPreferredGap(
										//												org.jdesktop.layout.LayoutStyle.RELATED)
										//										.add(jLabel3)
										//										.addPreferredGap(
										//												org.jdesktop.layout.LayoutStyle.RELATED)
										//										.add(
										//												dpEndDate,
										//												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										//												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										//												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED))
								.add(
										reportConstraintPanelLayout
										.createSequentialGroup()
										.add(btnPreview)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(btnRefresh)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(btnPrint)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(btnCancel)))

						.addContainerGap()));
		reportConstraintPanelLayout
		.setVerticalGroup(reportConstraintPanelLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(
						reportConstraintPanelLayout
						.createSequentialGroup()
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.addContainerGap()
						.add(
								reportConstraintPanelLayout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.LEADING)
								//								.add(dpEndDate,
								//										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								//										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								//										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								//								.add(jLabel3)
								.add(
										reportConstraintPanelLayout
										.createSequentialGroup()
										.add(
												reportConstraintPanelLayout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.LEADING)
												.add(
														dpStartDate,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
												.add(jLabel2))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												reportConstraintPanelLayout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.LEADING)
												.add(btnPreview).add(btnRefresh)
												.add(btnPrint).add(btnCancel))))

						.addContainerGap(
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		reportConstraintPanelLayout.linkSize(new java.awt.Component[] { 
				dpStartDate, jLabel2},
				org.jdesktop.layout.GroupLayout.VERTICAL);

		JPanel panel1 = new JPanel();
		panel1.setLayout(new MigLayout());
		panel1.setBackground(new Color(209, 222, 235));
		panel1.add(reportConstraintPanel);

		add(panel1, java.awt.BorderLayout.NORTH);
		reportPanel.setLayout(new java.awt.BorderLayout());
		setBackground(new Color(209, 222, 235));
		reportPanel.setBackground(new Color(209, 222, 235));
		add(reportPanel, java.awt.BorderLayout.CENTER);

	}// </editor-fold>//GEN-END:initComponents

	public void doPrint() {
		try {
			 if (report.getJasperPrint() != null) {
				report.getJasperPrint().setProperty("printerName",
						Application.getPrinters().getReceiptPrinter());
				JReportPrintService.printQuitely(report.getJasperPrint());
				doOpenCashdrawer();
			} 
		} catch (Exception e) {
		}
	}

	public void doOpenCashdrawer() {
		if (StringUtils.isNotBlank(TerminalConfig.getCashDrawerFile())) {
			OrderView.getInstance().getTicketView().openCashDrawerFile();
		} else if (TerminalConfig.isCashDrawerPrint()) {
			OrderView.getInstance().getTicketView().openCashDrawerPrint();
		} else if (TerminalConfig.isCashDrawer())
			OrderView.getInstance().getTicketView().openCashDrawer();
	}

	public boolean doRefreshReport(boolean flag) {
		Date fromDate = dpStartDate.getDate();
		Date toDate = dpEndDate.getDate();// GEN-FIRST:event_doRefreshReport
		int reportT = 0;
		
		if(isSalesTaken(fromDate)) {
			return false;
		}
		
		if (fromDate.after(toDate)) {
			POSMessageDialog
			.showError(
					BackOfficeWindow.getInstance(),
					com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
			return false;
		}

		try {
			reportPanel.removeAll();
			reportPanel.revalidate();
			reportPanel.setBackground(new Color(209, 222, 235));
			
			if (report != null) {
				int reportType = 0;
				if (flag == true)
					reportType = Report.REPORT_TYPE_2;

				report.setReportType(reportType);
				report.setStartDate(fromDate);
				report.setEndDate(toDate);
				report.refresh();
				if (report != null && report.getViewer() != null) {
					 
					reportPanel.add(report.getViewer());
					reportPanel.revalidate();
					report.getViewer().setBackground(new Color(209, 222, 235));
					reportPanel.setBackground(new Color(209, 222, 235));
				}
				if ((report != null) && (report.getType() == 1)
						&& (reportType != Report.REPORT_TYPE_1)) {
					 
					Application.enabledGenerateReport();
					 ErrorMessageDialog dialog;
					if(StringUtils.isNotEmpty(POSConstants.Wollen_Sie_Drucken))
						 dialog = new ErrorMessageDialog(POSConstants.Wollen_Sie_Drucken, true, true);
					else
					     dialog = new ErrorMessageDialog("Wollen Sie Drucken ?", true, true);
					
					dialog.pack();
					dialog.open();
						if (!dialog.isCancelled()) {
						doPrint();
					}
				}
			}

		} catch (Exception e) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
		return true;
	}// GEN-LAST:event_doRefreshReport

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnRefresh;

	public boolean isSalesTaken(Date fromDate) {
		boolean taken = false;
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			List<Salesreportdb> salesReports = SalesReportDAO.getInstance().findByDate(fromDate);
			if (salesReports != null && !salesReports.isEmpty()) {
				System.out.println(salesReports.size());
				
				if(StringUtils.isNotEmpty(POSConstants.Tages_Abschluss_fur)&&StringUtils.isNotEmpty(POSConstants.schon_ausgedruckt))
					POSMessageDialog.showError(POSConstants.Tages_Abschluss_fur +" "+format.format(fromDate)+" "+POSConstants.schon_ausgedruckt);
				else			
				    POSMessageDialog.showError("Tages Abschluss für "+format.format(fromDate)+" ist schon ausgedruckt!");
			
				taken = true;
			}
		return taken;
	}


	private javax.swing.JButton btnPreview;
	private javax.swing.JButton btnPrint;
	private javax.swing.JButton btnCancel;
	private org.jdesktop.swingx.JXDatePicker dpEndDate;
	private org.jdesktop.swingx.JXDatePicker dpStartDate;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private com.floreantpos.swing.TransparentPanel reportConstraintPanel;
	private com.floreantpos.swing.TransparentPanel reportPanel;

	// End of variables declaration//GEN-END:variables

	public Report getReport() {
		return report;
	}


	public void setReport(Report report) {
		this.report = report;
		if (report != null && report.getType() != 1)
			btnPreview.setVisible(false);

		// if(report != null) {
		// cbReportType.setEnabled(report.isTypeSupported());
		// this.dpStartDate.setEnabled(report.isDateRangeSupported());
		// this.dpEndDate.setEnabled(report.isDateRangeSupported());
		// }
	}
}