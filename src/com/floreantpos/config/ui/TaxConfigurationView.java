package com.floreantpos.config.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.usb.UsbDevice;
import javax.usb.UsbHostManager;
import javax.usb.UsbServices;
import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdesktop.swingx.JXDatePicker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.services.EmailASESUtil;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.ui.dialog.ErrorMessageDialog;
import com.floreantpos.ui.model.MenuItemForm;
import com.floreantpos.ui.util.BusinessDateUtil;
import com.floreantpos.util.POSUtil;
import com.khana.tse.fiskaly.FiskalyKeyParameter;
import com.khana.tse.fiskaly.transaction.FiskalyTransactionControllerV2;

import net.miginfocom.swing.MigLayout;

public class TaxConfigurationView extends ConfigurationView {
	public static final String CONFIG_TAB_TAX = "Steuer";
	private Restaurant restaurant;
	private JCheckBox cbItemSalesPriceIncludesTax;
	private JCheckBox cbTseEnable;
	//private JCheckBox cbDubTseEnable;
	private JCheckBox cbOldTse;
	private JCheckBox cbTseFiscally;
	private JCheckBox cbTseAtrust;
	private JCheckBox cbTseSwisbit;
	private JCheckBox cbTseLive;
	private POSTextField tfExportId;
	private JCheckBox cbTseDemo;
	private POSTextField tfTseId;
	private POSTextField tfAdminPuk;
	private POSTextField tfClientId;
	private JButton createTseId;
	private JButton createClientId;
	private JButton importTseClientId;
	private JCheckBox cbTseContact;
	private JButton regUsbCert;
	private JCheckBox cbTseTier3;
	
	private JButton updateTss;
	private JButton initialize;
	private JButton logout;
	
	private JButton btnTseExport;
	private JButton btnCashRegister;
	private JButton btnTrigrExport;
	private JButton btnFinlExport;
	private JXDatePicker dpDate;
	private JXDatePicker dpEndDate;
	
	private JXDatePicker dpDate_;
	private JXDatePicker dpEndDate_;

	public TaxConfigurationView() {
		setLayout(new MigLayout("", "[]", "[]"));
		restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));
		cbItemSalesPriceIncludesTax = new JCheckBox("Alle Preise sind inkl. MwSt.");
		cbItemSalesPriceIncludesTax.setBackground(new Color(209,222,235));
		cbTseEnable = new JCheckBox("TSE-Enable");
		cbOldTse = new JCheckBox("Old Tse Enabled");
		cbOldTse.setBackground(new Color(209,222,235));
		
		//cbDubTseEnable = new JCheckBox("TSE Dup-Enable");
		
		cbTseFiscally = new JCheckBox("Fiskaly");
		cbTseAtrust = new JCheckBox("Atrust");
		cbTseSwisbit = new JCheckBox("Swiss-Bit");		
		cbTseLive = new JCheckBox("LIVE");
		cbTseDemo = new JCheckBox("DEMO");
		tfTseId = new POSTextField(20);
		tfClientId = new POSTextField(20);
		createTseId = new JButton("Initialize-TSE");
		tfTseId.setEnabled(false);
		tfClientId.setEnabled(false);
		
		logout = new JButton("Logout");
		initialize = new JButton("Authenticate/Initialize");
		initialize.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!restaurant.getTseId().isEmpty()&&Application.getCurrentUser().getFirstName().compareTo("Super-User")!=0) {
					JOptionPane.showMessageDialog(Application.getPosWindow(), "TSE-ID And TSE-CLIENT-ID Is Exist");
				} else {					
						FiskalyTransactionControllerV2 controlerV2 = new FiskalyTransactionControllerV2();
						controlerV2.authIntialization();						  	
				}
				createTseId.setEnabled(false);							 			
			}
		});	
		
		createTseId.setEnabled(false);
		createTseId.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!restaurant.getTseId().isEmpty()&&Application.getCurrentUser().getFirstName().compareTo("Super-User")!=0) {
					JOptionPane.showMessageDialog(Application.getPosWindow(), "TSE-ID And TSE-CLIENT-ID Is Exist");
				}else {
					FiskalyTransactionControllerV2 controler = new FiskalyTransactionControllerV2();
					FiskalyKeyParameter param = new FiskalyKeyParameter();
					try {
						param = controler.createTSS();
						restaurant.setTseId(param.getTssId());
						restaurant.setAdminPuk(param.getAdminPuk());
						restaurant.setTseVersion("v2.O.32");
						if(restaurant.getTseActivateDate()==null) {
							restaurant.setTseActivateDate(BusinessDateUtil.startOfOfficialDay(new Date()));
							restaurant.setTseValidDate(getValidTillDate(new Date()));							
						}
						 
						RestaurantDAO.getInstance().saveOrUpdate(restaurant);
						
					} catch (IOException e1) {						 
						e1.printStackTrace();
					}
					
					tfTseId.setText(param.getTssId());
				}
				createTseId.setEnabled(false);				
			}
		});

		tfAdminPuk = new POSTextField(20);
		tfAdminPuk.setEnabled(false);
		updateTss = new JButton("ChangeAdminPin");
		updateTss.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!restaurant.getTseId().isEmpty()&&Application.getCurrentUser().getFirstName().compareTo("Super-User")!=0) {
					JOptionPane.showMessageDialog(Application.getPosWindow(), "TSE-ID And TSE-CLIENT-ID Is Exist");
				} else {
					
						FiskalyTransactionControllerV2 controlerV2 = new FiskalyTransactionControllerV2();
						FiskalyKeyParameter param = new FiskalyKeyParameter();
						param = controlerV2.changeAdminPin();
						restaurant.setAdminPin("JAN291993");
						RestaurantDAO.getInstance().saveOrUpdate(restaurant);						 					 
				}
				createTseId.setEnabled(false);			
				 			
			}
		});		

		printCertificate = new JButton("Cert-Druck");
		printCertificate.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				printCertificate(cba4.isSelected());
			}
		});
		
		
		btnTseExport = new JButton("Generate Tse Export");		
		btnTseExport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {	
			/*	 
				Date startDtae = BusinessDateUtil.startOfOfficialDay(dpDate.getDate());
				Date endDate = BusinessDateUtil.endOfOfficialDay(dpEndDate.getDate());
				System.out.println("dpDate: "+BusinessDateUtil.startOfOfficialDay(dpDate.getDate())+", dpEndDate: "+BusinessDateUtil.endOfOfficialDay(dpEndDate.getDate()));
				FiskalyTransactionController controler = new FiskalyTransactionController();
				controler.cashPointClosing(startDtae,endDate); */
				 
			}
		});
		tfExportId = new POSTextField(20);
		btnFinlExport = new JButton("Download Export");		
		btnFinlExport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {												 
			/*	FiskalyTransactionController controler = new FiskalyTransactionController();
				controler.cashPointClosing_export(tfExportId.getText());*/
				 
			}
		});
		
		
		
		
		btnCashRegister = new JButton("Add Cash Register");
		btnCashRegister.setBackground(new Color(249, 145, 122));
		btnCashRegister.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {												 
				/*FiskalyTransactionController controler = new FiskalyTransactionController();
				controler.addCashRegister();	*/			 
			}
		});
		
		dpDate = new JXDatePicker();
		dpDate.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
		dpDate.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		 
		dpEndDate = new JXDatePicker();
		dpEndDate.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
		dpEndDate.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		 
		
		dpDate_ = new JXDatePicker();
		dpDate_.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
		dpDate_.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		 
		dpEndDate_ = new JXDatePicker();
		dpEndDate_.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
		dpEndDate_.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		
		btnTrigrExport = new JButton("TriggerExport");
		btnTrigrExport.setBackground(new Color(249, 145, 122));
		btnTrigrExport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {												 
				/*FiskalyTransactionController controler = new FiskalyTransactionController();
				Date startDtae = BusinessDateUtil.startOfOfficialDay(dpDate_.getDate());
				Date endDate = BusinessDateUtil.endOfOfficialDay(dpEndDate_.getDate());*/
				//controler.triggerExport(startDtae,endDate);
				 
			}
		});
		
		
		cba4 = new JCheckBox("A4");	

		createClientId = new JButton("Initialize-Client");
		createClientId.setEnabled(false);
		createClientId.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {

				FiskalyTransactionControllerV2 controler = new FiskalyTransactionControllerV2();
				FiskalyKeyParameter param = new FiskalyKeyParameter();
				param.setTssId(restaurant.getTseId());
				try {
					param = controler.createClientId(param);
				} catch (URISyntaxException e2) {
					e2.printStackTrace();
				}
				TerminalConfig.setTseClientId(param.getClientId());
				tfClientId.setText(param.getClientId());
				createClientId.setEnabled(false);				
				try {
					Writer fileWriter = new FileWriter(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+"\\Tse_Credential_"+restaurant.getName().replace(" ", "_")+".json");

					fileWriter.write("{ \"tseId\" : \""+restaurant.getTseId()+"\",");
					fileWriter.write("\"tseClientId\"  : \""+TerminalConfig.getTseClientId()+"\"}");
					fileWriter.close();
				}catch(Exception ex) {
					
				}
				
				try {
					EmailASESUtil util = new EmailASESUtil();
					util.sendTseEmail( restaurant.getName()+"_"+restaurant.getZipCode()+"-TSE-Activated", "tse.khana.kassensystem@gmail.com", "Tse is Activated with TSE-ID: "+restaurant.getTseId()+"\r\n"+"</p>"
					+ "<p>"+"Client Id: "+tfClientId.getText()+"\r\n"+"</p>"
					+ "<p>"+"Restaurant Name "+restaurant.getName()+"\r\n"+"</p>"
					+ "<p>"+"Restaurant Address "+restaurant.getAddressLine1()+restaurant.getAddressLine2()+"\r\n"+"</p>"
					+ "<p>"+restaurant.getZipCode()+"\r\n"+"\r\n"+"</p>"
					+ "<p>"+"MFG"+"\r\n"+"</p>"
					+ "<p>"+"TSE-UPDATE-TEAM" +"</p>"						
					, "Tse-Activation-Successfully");
				}catch(Throwable ex) {ex.printStackTrace();}
				
				
				if(StringUtils.isNotEmpty(tfClientId.getText())) {
					try {
						save();						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					printCertificate(false);
					Application.getInstance().restartApp();
					Application.getInstance().exitPOS(false);
				}
			}
		});
		
		
		importTseClientId = new JButton("Import-Credential");
		importTseClientId.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						return "JSON File";
					}

					@Override
					public boolean accept(File f) {
						if (f.getName().endsWith(".json")) {
							return true;
						}

						return false;
					}
				});								
				
				chooser.setMultiSelectionEnabled(true);
				chooser.showOpenDialog(BackOfficeWindow.getInstance());		
				File file  = chooser.getSelectedFile();				
				try {
					ObjectMapper mapper = new ObjectMapper();
					JsonNode json = mapper.readTree(file);	
					restaurant.setTseId(json.get("tseId").asText());
					TerminalConfig.setTseClientId(json.get("tseClientId").asText());
					tfClientId.setText(json.get("tseClientId").asText());
					tfTseId.setText(json.get("tseId").asText());
					Application.getInstance().getParam().setClientId(json.get("tseClientId").asText());
					Application.getInstance().getParam().setTssId(json.get("tseId").asText());
					try {
						save();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Application.getInstance().restartApp();
					Application.getInstance().exitPOS(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});		
		
		
		/*
		 * Usb Certification
		 */
		
		
		regUsbCert = new JButton("Reg-USB");
		regUsbCert.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
					try {
						ErrorMessageDialog dialog = new ErrorMessageDialog("Bitte USB einstecken dann OK Drucken!!!");
						dialog.pack();
						dialog.open();
						restaurant.setCertUsbId(getUsbIdDeviceId());
						RestaurantDAO.getInstance().saveOrUpdate(restaurant);
						if(TerminalConfig.isDebugMode())
							System.out.println("setted with "+getUsbIdDeviceId());
						services.removeUsbServicesListener(tempListener);
					}catch (Exception ex) {
						// TODO: handle exception
					}
				
			}
		});
		
		cbTseTier3 = new JCheckBox("TSE-TIER3");
		cbTseTier3.setBackground(new Color(209,222,235));


		cbTseEnable.setBackground(new Color(209,222,235));
		/*cbDubTseEnable .setBackground(new Color(209,222,235));
		cbDubTseEnable.setVisible(TerminalConfig.isTseEnable());*/
		cbTseFiscally.setBackground(new Color(209,222,235));
		cbTseAtrust.setBackground(new Color(209,222,235));
		cbTseSwisbit.setBackground(new Color(209,222,235));
		cbTseDemo.setBackground(new Color(209,222,235));
		cbTseLive.setBackground(new Color(209,222,235));

		JLabel lblFiscally = new JLabel();
		lblFiscally.setIcon(IconFactory.getIcon("fiskaly_logo.png"));
		JLabel lblAtrust = new JLabel();
		lblAtrust.setIcon(IconFactory.getIcon("Atrust_logo.png"));
		add(new JLabel("__"), "");
		add(cbItemSalesPriceIncludesTax, "growx");
		add(cbOldTse, "wrap");
		add(cbTseFiscally, "");
		add(lblFiscally, "growx");
		
		
		cbTseContact = new JCheckBox("Contacted");
		cbTseContact.setBackground(new Color(209,222,235));
		cbTseContact.setSelected(TerminalConfig.isTseContacted());
		cbTseContact.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TerminalConfig.setTseContacted(cbTseContact.isSelected());				
			}
		});
		add(cbTseContact, "wrap");
		
		
		
//		add(cbTseAtrust, "cell 3 1");
//		add(lblAtrust, "cell 4 1, wrap");
		
		add(cbTseEnable);		
		add(new JLabel("TSE-MODE"));		

		cbTseLive.setEnabled(false);
		if(Application.getCurrentUser().getFirstName().compareTo("Super-User")==0) {
			cbTseLive.setEnabled(true);
			add(cbTseLive);
			add(cbTseDemo, "wrap");
		}else {
			add(cbTseLive);
			add(cbTseDemo, "wrap");
		}
		
		
		cbTseLive.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbTseLive.isSelected()) {
					cbTseFiscally.setSelected(true);
					cbTseContact.setSelected(true);
					cbTseEnable.setSelected(true);
					try {
						save();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				cbTseDemo.setSelected(!cbTseLive.isSelected());
				
			}
		});
		cbTseEnable.setSelected(TerminalConfig.isTseEnable());
		//cbDubTseEnable.setSelected(TerminalConfig.isDupTseEnable());
		add(new JLabel("TSE-ID"));
		add(tfTseId);
		add(createTseId);
		add(updateTss,"wrap");
		add(new JLabel("Admin-Puk"));
		add(tfAdminPuk);
		add(initialize,"wrap");
		
		add(new JLabel("TSE-CLIENT-ID"));
		add(tfClientId);
		add(createClientId);
		add(logout,"wrap");
		add(importTseClientId);
		add(printCertificate);
		add(cba4, "wrap");
		
		JButton buttonTaxUpdate = new JButton("Tax Update Manual");
		buttonTaxUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changeTax();
				System.exit(0);	
			}
		});
		
		if(Application.getCurrentUser().getFirstName().compareTo("Master")==0) {
			cbTseTier3.setSelected(TerminalConfig.isTseTier3());
			add(cbTseTier3, "growx");
		}
		
		if(Application.getCurrentUser().getFirstName().compareTo("Super-User")==0) {			
			if(cbTseEnable.isSelected()&&restaurant.getCertUsbId()==null||cbTseEnable.isSelected()&&restaurant.getCertUsbId()!=null&&restaurant.getCertUsbId().isEmpty()||TerminalConfig.isDebugMode()) {
				try {
				services = UsbHostManager.getUsbServices();						
				tempListener = new UsbServicesListener() {
					
					@Override
					public void usbDeviceDetached(UsbServicesEvent event) {
														
					}
					
					@Override
					public void usbDeviceAttached(UsbServicesEvent event) {
						try {
						UsbDevice device = event.getUsbDevice( );
						String registerdUsbId = ""+device.getUsbDeviceDescriptor().idVendor()+device.getUsbDeviceDescriptor().idProduct();
						if(TerminalConfig.isDebugMode())
							System.out.println("New Usb Attached with id: "+registerdUsbId);
						setUsbIdDeviceId(registerdUsbId);							
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				services.addUsbServicesListener(tempListener);
				} catch (Exception e) {
					// TODO: handle exception
				}
				add(regUsbCert);
			}			
			add(buttonTaxUpdate, "wrap");
			
			if(TerminalConfig.isSplFeature()) {
				
				   add(btnCashRegister,"wrap");
				   add(dpDate);
				   add(dpEndDate);
				   add(btnTseExport,"wrap");
				   add(dpDate_);
				   add(dpEndDate_);
				   add(btnTrigrExport,"wrap");
				   add(tfExportId);
				   add(btnFinlExport,"wrap");				   
				}
		}
		//add(cbDubTseEnable);
		/*cbDubTseEnable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {	
				if(TerminalConfig.isTseEnable()) {
				if(TerminalConfig.isDupTseEnable()) {
					TerminalConfig.setDupTseEnable(false);					 
				} else {					
					TerminalConfig.setDupTseEnable(true);
				}				
					try {
						save();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}								
			}
		});	*/	
	}	
	
	UsbServices services;					
	UsbServicesListener tempListener;
	String usbIdDeviceId;
	public String getUsbIdDeviceId() {
		return usbIdDeviceId;
	}

	public void setUsbIdDeviceId(String usbIdDeviceId) {
		this.usbIdDeviceId = usbIdDeviceId;
	}

	public synchronized void printCertificate(boolean a4) {
		FiskalyTransactionControllerV2 controler = new FiskalyTransactionControllerV2();
		FiskalyKeyParameter param = new FiskalyKeyParameter();
		try {
			param = controler.getTSSInfo();
		} catch (IOException e) {			 
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		JReportPrintService.printTseCertificate(param, a4);
	}
	
	public static Date getValidTillDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 5);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.add(Calendar.YEAR, 5);
		return new Date(cal.getTimeInMillis());
	}

	private JButton printCertificate;
	private JCheckBox cba4;

	@Override
	public boolean save() throws Exception {
		if (!isInitialized()) {
			return true;
		}
		setBackground(new Color(209,222,235));
		restaurant.setItemPriceIncludesTax(cbItemSalesPriceIncludesTax.isSelected());

		restaurant.setTseLive(cbTseLive.isSelected());

		if(cbTseFiscally.isSelected())
			restaurant.setTseVendor("fiskaly");
		if(cbTseAtrust.isSelected())
			restaurant.setTseVendor("atrust");	
		if(cbTseSwisbit.isSelected())
			restaurant.setTseVendor("swissbit");		
		
		TerminalConfig.setOldTseEnable(cbOldTse.isSelected());
		TerminalConfig.setTseEnable(cbTseEnable.isSelected());
		TerminalConfig.setTseTier3(cbTseTier3.isSelected());
		RestaurantDAO.getInstance().saveOrUpdate(restaurant);
		Application.getInstance().refreshRestaurant();
		return true;
	}

	@Override
	public void initialize() throws Exception {
		cbItemSalesPriceIncludesTax.setSelected(POSUtil.getBoolean(restaurant.isItemPriceIncludesTax()));
		cbOldTse.setSelected(TerminalConfig.isOldTseEnable());

		if(restaurant.getTseVendor()!=null&&!restaurant.getTseVendor().isEmpty()) {
			if(restaurant.getTseVendor().compareTo("fiskaly")==0)
				cbTseFiscally.setSelected(true);
			else if(restaurant.getTseVendor().compareTo("atrust")==0)
				cbTseAtrust.setSelected(true);
			else if(restaurant.getTseVendor().compareTo("swissbit")==0)
				cbTseSwisbit.setSelected(true);
		}

		if(restaurant.getTseId().isEmpty()||Application.getCurrentUser().getFirstName().compareTo("Super-User")==0)
			createTseId.setEnabled(true);
		if(TerminalConfig.getTseClientId().isEmpty()||Application.getCurrentUser().getFirstName().compareTo("Super-User")==0)
			createClientId.setEnabled(true);

		tfTseId.setText(restaurant.getTseId());
		tfAdminPuk.setText(restaurant.getAdminPuk());
		tfClientId.setText(TerminalConfig.getTseClientId());
		cbTseLive.setSelected(restaurant.isTseLive());
		cbTseDemo.setSelected(!restaurant.isTseLive());
		setInitialized(true);
	}

	@Override
	public String getName() {
		return CONFIG_TAB_TAX;
	}

	public synchronized static void changeTax() {
		Tax imHausTax = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
		imHausTax.setRate(19.00);
		TaxDAO.getInstance().saveOrUpdate(imHausTax);
		Tax lieferTax = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);
		lieferTax.setRate(7.00);
		TaxDAO.getInstance().saveOrUpdate(lieferTax);
		TerminalConfig.setSpecial(false);
		updateMenu();
		Application.getInstance().restartApp();
		System.out.println("Tax Updated");
	}

	
	private static void updateMenu() {
		try {
			try {
				List<MenuItem> tempList;
				tempList = MenuItemDAO.getInstance().findAll();
				Session session = MenuItemDAO.getInstance().createNewSession();
				Transaction tx = session.beginTransaction();

				Tax dineIn = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
				Tax home = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);
				Tax zero = TaxDAO.getInstance().findByName("ZERO");
				for (Iterator itr = tempList.iterator(); itr.hasNext();) {
					MenuItemForm editor;
					MenuItem item = (MenuItem) itr.next();
					if (item.getParent().getParent().getType().compareTo(POSConstants.DINE_IN) == 0) {
						item.setTax(dineIn);
					} else if (item.getParent().getParent().getType().compareTo(POSConstants.HOME_DELIVERY) == 0) {
						item.setTax(home);
					} else {
						item.setTax(zero);
					}
					session.saveOrUpdate(item);
				}
				
				tx.commit();
				session.close();				
			} catch (Throwable x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
			}

			
		} catch (Throwable x) {
			BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE,
					x);
		}
	}

}
