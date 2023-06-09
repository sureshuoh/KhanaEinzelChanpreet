package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.floreantpos.POSConstants;
import com.floreantpos.add.service.TseTicketService;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.TSEReceiptData;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TSEReceiptDataDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.CustomerDataDialog;
import com.floreantpos.ui.views.TicketDetailView.PrintType;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.util.BusinessDateUtil;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRProperties;

public class OrderInfoView extends JPanel {
	private List<Ticket> tickets;
	private Ticket ticket;
	JPanel reportPanel;
	JPanel topPanel;
	PosButton official;
	PosButton regular;
	PosButton a4;
	PosButton btnKundenInfo;
	public OrderInfoView(List<Ticket> tickets) throws Exception {
		super();
		this.tickets = tickets;
		setBackground(new Color(209,222,235));
		createUI();
	}

	public OrderInfoView() throws Exception {
		super();
		setBackground(new Color(209,222,235));
		createUI();
	}
	public void setTickets(List<Ticket> tickets)
	{
		this.tickets =tickets;

	}
	public class ArrowAction extends AbstractAction {

		private String cmd;
		public ArrowAction(String cmd) {
			this.cmd = cmd;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (cmd.equalsIgnoreCase("RIGHT"))
			{
				try{
					if(regular.getBackground() == Color.GREEN)
						showReportView(1);
					else if (official.getBackground() == Color.GREEN)
						showReportView(0);
				}catch(Exception ex){}
			}
			else if (cmd.equalsIgnoreCase("LEFT"))
			{
				try{
					if(regular.getBackground() == Color.GREEN)
						showReportView(1);
					else if (official.getBackground() == Color.GREEN)
						showReportView(0);
				}catch(Exception ex){}
			}
		}
	}

	private void createUI() throws Exception {
		reportPanel = new JPanel(new MigLayout());
		topPanel = new JPanel(new MigLayout());
		regular = new PosButton();
		InputMap im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getActionMap();
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
				"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LEFT");

		am.put("RIGHT", new ArrowAction("RIGHT"));
		am.put("LEFT", new ArrowAction("LEFT"));

		regular.setText("BON");
		regular.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					showReportView(0);
					regular.setBackground(Color.GREEN);
					a4.setBackground(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		a4 = new PosButton();
		a4.setText("   A4   ");

		a4.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					showReportView(1);
					regular.setBackground(null);
					a4.setBackground(Color.GREEN);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		//added

		btnKundenInfo = new PosButton();
		btnKundenInfo.setText("Kunden Info");		
		btnKundenInfo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					regular.setBackground(null);
					btnKundenInfo.setBackground(Color.GREEN);
					Ticket ticket1 = tickets.get(0);
					CustomerDataDialog dialog = new CustomerDataDialog(ticket1);
					dialog.pack();
					dialog.open();
					if(dialog.isCanceled())
						return;
					Customer cust = dialog.getNewCustomer();
					for (Iterator iter = tickets.iterator(); iter.hasNext();) {
						Ticket ticket = (Ticket) iter.next();
						ticket.setCustomer(cust);
//						ticket.setKunden(cust);
						TicketDAO.getInstance().saveOrUpdate(ticket);			
					}
					showReportView(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		setBackground(new Color(128,128,128));	
		
		//Till here


		topPanel.add(regular,"cell 0 0, alignx trailing");
		regular.setBackground(Color.GREEN);
		topPanel.add(a4,"cell 1 0, growx");
		topPanel.add(btnKundenInfo,"cell 2 0, growx, wrap");

		reportPanel.setBackground(new Color(209,222,235));
		topPanel.setBackground(new Color(209,222,235));
		JScrollPane scrollPane = new JScrollPane(reportPanel);
		scrollPane.setBackground(new Color(209,222,235));
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30,20));
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);

		if(tickets != null)
		{
			for (int i = 0; i < tickets.size(); i++) {
				Ticket ticket = tickets.get(i);
				TicketPrintProperties printProperties = new TicketPrintProperties("RECHNUNG", true, true, true,false);
				HashMap map = JReportPrintService.populateTicketProperties(ticket, printProperties, null,PrintType.REGULAR,false, ticket.getPaidAmount()-ticket.getTotalAmountWithoutPfand());
				JasperPrint jasperPrint = JReportPrintService.createPrint(ticket, map, null,false);

				TicketReceiptView receiptView = new TicketReceiptView(jasperPrint);
				reportPanel.add(receiptView.getReportPanel());
			}
		}
		setLayout(new BorderLayout());
		add(topPanel,BorderLayout.NORTH);
		add(scrollPane);
	}

	
	JasperPrint jasperPrint;
	public void showReportView(int arg) throws Exception
	{
		reportPanel.removeAll();
		
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
			    "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = tickets.get(i);
			if(ticket == null)continue;
			TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true,false);
			HashMap map;
			if(arg == 0)
				map =JReportPrintService.populateTicketProperties(ticket, printProperties, null,PrintType.REGULAR,false, ticket.getPaidAmount()-ticket.getTotalAmountWithoutPfand());
			else
				map =JReportPrintService.populateTicketPropertiesA4(ticket, printProperties, null);
			
			if (arg == 0)
				jasperPrint = JReportPrintService.createPrint(ticket, map, null,false);
			else
				jasperPrint = JReportPrintService.createA4Print(ticket, map, null);

			TicketReceiptView receiptView = new TicketReceiptView(jasperPrint);
			jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));
			
			if(arg == 0)
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			else
				jasperPrint.setProperty("printerName", Application.getPrinters().getA4Printer());
			
			reportPanel.add(receiptView.getReportPanel());
		}
		repaint();
		revalidate();
	}
	
	public void printReport(int arg, boolean angebot) throws Exception
	{
		
		reportPanel.removeAll();		
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
			    "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = tickets.get(i);
			if(ticket == null)continue;
			
			final Restaurant restaurant = RestaurantDAO.getRestaurant();
			if (ticket.getTicketid() == null&&!angebot|| ticket.getTicketid() != null&&ticket.getTicketid() == 0&&!angebot) {
				int restaurantTicketId = restaurant.getTicketid() != null ? restaurant.getTicketid() : 1;
				ticket.setTicketid(restaurantTicketId);					
				restaurant.setTicketid(++restaurantTicketId);
				RestaurantDAO.getInstance().saveOrUpdate(restaurant);
				TicketDAO.getInstance().saveOrUpdate(ticket);
			}
			
			if(TerminalConfig.isTseEnable()&&TerminalConfig.isTseTier3()&&isTseActiv(ticket.getCreateDate())&&ticket.getTseReceiptDataId()==null) {
				 
				tseForceFinish(ticket);
			OrderController.saveOrder(ticket);
			}
			TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true,false);
			HashMap map;
			if (regular.getBackground() == Color.GREEN)
			{
			  if(TerminalConfig.isLogoEnabled()) {
				  System.out.println("First");
			    map =JReportPrintService.populateTicketProperties(ticket, printProperties, null,PrintType.REGULAR,true, ticket.getPaidAmount()-ticket.getTotalAmountWithoutPfand());
			  } else {
				  System.out.println("second_");
			    map =JReportPrintService.populateTicketProperties(ticket, printProperties, null,PrintType.REGULAR,false, ticket.getPaidAmount()-ticket.getTotalAmountWithoutPfand());
			  }
			}else
				map =JReportPrintService.populateTicketPropertiesA4(ticket, printProperties, null);
			
			JasperPrint jasperPrint;
			if (regular.getBackground() == Color.GREEN)
			{
				jasperPrint = JReportPrintService.createPrint(ticket, map, null,false);
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));
				System.out.println("Third_");
				JReportPrintService.printQuitely(jasperPrint);

			}else {
				System.out.println("Fourth_");
				jasperPrint = JReportPrintService.createA4Print(ticket, map, null);
				jasperPrint.setProperty("printerName", Application.getPrinters().getA4Printer());
				jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));
				JReportPrintService.printQuitelyA4(jasperPrint);

			}			
			
		}
		repaint();
		revalidate();
	}
	
	public void showTicketReportView() throws Exception
	{
		reportPanel.removeAll();
		
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
			    "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		if(ticket != null)
		{
			TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true,false);
			HashMap map = JReportPrintService.populateTicketProperties(ticket, printProperties, null,PrintType.REGULAR,false, ticket.getPaidAmount()-ticket.getTotalAmountWithoutPfand());
			JasperPrint jasperPrint;
			jasperPrint = JReportPrintService.createPrint(ticket, map, null,false);
			jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));

			TicketReceiptView receiptView = new TicketReceiptView(jasperPrint);
			reportPanel.add(receiptView.getReportPanel());
		}
		repaint();
		revalidate();
	}
	public void setTicket(Ticket ticket)
	{
		this.ticket = ticket;
	}
	public void print(boolean angebot) throws Exception {
			printReport(0, angebot);
	}
	
	public static boolean isTseActiv(Date checkDate) {
		RestaurantDAO dao = new RestaurantDAO();
		Restaurant restaurant = dao.get(Integer.valueOf(1));
		Date startDate = restaurant.getTseActivateDate();
		Date endDate = restaurant.getTseValidDate();
		if(TerminalConfig.isBuildMode()||TerminalConfig.isDebugMode())
			System.out.println("TSE active Date "+startDate+" checkDate "+checkDate+" EndDate "+endDate);
		if(startDate==null||endDate==null) {
			if(TerminalConfig.isTseEnable()&&restaurant.isTseLive()&&restaurant.getTseId()!=null) {
				startDate = BusinessDateUtil.startOfOfficialDay(new Date());			
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDate);
				cal.add(Calendar.YEAR, 5);
				cal.add(Calendar.DAY_OF_MONTH, -1);
				endDate = cal.getTime();
				restaurant.setTseActivateDate(startDate);
				restaurant.setTseValidDate(endDate);
				dao.saveOrUpdate(restaurant);
			}
			
			return false;
		}
			
		return startDate.compareTo(checkDate) * checkDate.compareTo(endDate) >= 0;
	}
	
	public void tseForceFinish(Ticket ticket) {
		if(TerminalConfig.isTseEnable()&&TerminalConfig.isTseTier3()&&ticket.getTseReceiptDataId()==null) {
			try {				
				int paymentType = 0;
				if(ticket.getCashPayment())
					paymentType = PaymentType.CASH.ordinal();
				else
					paymentType = PaymentType.CARD.ordinal();

				TseTicketService.getTseService().forceRestartOrder(ticket);
				TSEReceiptData receiptData = TseTicketService.getTseService().initFinishTseOrder(ticket, paymentType);
				TSEReceiptDataDAO.getInstance().saveOrUpdate(receiptData);					
				ticket.setTseReceiptDataId(receiptData.getId());
				ticket.setTseReceiptTxRevisionNr(receiptData.getLatestRevision());
				ticket.setDrawerResetted(false);
				TicketDAO.getInstance().saveOrUpdate(ticket);
			}catch (Exception e) {

			}finally {

			}
		}
	}
	
}
