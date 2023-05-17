package com.floreantpos.model;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

import com.floreantpos.model.dao.VirtualPrinterDAO;

@XmlRootElement(name = "printers")
public class PosPrinters {
	private String reportPrinter;
	private String receiptPrinter;
	private String barPrinter;
	private String a4Printer;
	private boolean receiptPrinterEnable;
	private boolean a4PrinterEnable;
	private boolean barPrinterEnable;
	private boolean kitchenPrinterEnable;
//	private Printer defaultReceiptPrinter;
	private Printer defaultKitchenPrinter;
	private String sonstigePrinter;
	private String lieferandoPrinter;

	
	//private List<Printer> receiptPrinters;
	private List<Printer> kitchenPrinters;
	
	//private Map<VirtualPrinter, Printer> receiptPrinterMap = new HashMap<VirtualPrinter, Printer>();
	private Map<VirtualPrinter, Printer> kitchePrinterMap = new HashMap<VirtualPrinter, Printer>();

	public String getReportPrinter() {
		return reportPrinter;
	}

	public void setReportPrinter(String reportPrinter) {
		this.reportPrinter = reportPrinter;
	}
	
	public String getReceiptPrinter() {
		return receiptPrinter;
	}
	
	public void setBarPrinter(String barPrinter) {
		this.barPrinter = barPrinter;
	}
	public String getBarPrinter() {
		return barPrinter;
	}
	
	public void setBarPrinterEnable(boolean barPrinter) {
		this.barPrinterEnable = barPrinter;
	}
	public boolean getBarPrinterEnable() {
		return barPrinterEnable;
	}
	public void setReceiptPrinterEnable(boolean receiptPrinter) {
		this.receiptPrinterEnable = receiptPrinter;
	}
	public boolean getReceiptPrinterEnable() {
		return receiptPrinterEnable;
	}
	public void setA4PrinterEnable(boolean a4Printer) {
		this.a4PrinterEnable = a4Printer;
	}
	public boolean getA4PrinterEnable() {
		return a4PrinterEnable;
	}
	public void setKitchenPrinterEnable(boolean kitchenPrinter) {
		this.kitchenPrinterEnable = kitchenPrinter;
	}
	public boolean getKitchenPrinterEnable() {
		return kitchenPrinterEnable;
	}
	public void setA4Printer(String a4Printer) {
		this.a4Printer = a4Printer;
	}
	public String getA4Printer() {
		return a4Printer;
	}
	
	public String getLieferandoPrinter() {
		return lieferandoPrinter;
	}

	public void setLieferandoPrinter(String lieferandoPrinter) {
		this.lieferandoPrinter = lieferandoPrinter;
	}
	
	public String getSonstigePrinter() {
		return sonstigePrinter;
	}

	public void setSonstigePrinter(String sonstigePrinter) {
		this.sonstigePrinter = sonstigePrinter;
	}
	
	public void setReceiptPrinter(String receiptPrinter) {
		this.receiptPrinter = receiptPrinter;
	}

//	public List<Printer> getReceiptPrinters() {
//		if (receiptPrinters == null) {
//			receiptPrinters = new ArrayList<Printer>(2);
//		}
//
//		return receiptPrinters;
//	}
//
//	public void setReceiptPrinters(List<Printer> receiptPrinters) {
//		this.receiptPrinters = receiptPrinters;
//	}

	public List<Printer> getKitchenPrinters() {
		if (kitchenPrinters == null) {
			kitchenPrinters = new ArrayList<Printer>(2);
		}

		return kitchenPrinters;
	}

	public void setKitchenPrinters(List<Printer> kitchenPrinters) {
		this.kitchenPrinters = kitchenPrinters;
	}

//	public void addReceiptPrinter(Printer printer) {
//		getReceiptPrinters().add(printer);
//	}

	public void addKitchenPrinter(Printer printer) {
		getKitchenPrinters().add(printer);
	}
	
//	public void setDefaultReceiptPrinter(Printer defaultReceiptPrinter) {
//		this.defaultReceiptPrinter = defaultReceiptPrinter;
//	}
	
//	public Printer getDefaultReceiptPrinter() {
//		if(defaultReceiptPrinter == null && getReceiptPrinters().size() > 0) {
//			defaultReceiptPrinter = receiptPrinters.get(0);
//			
//			for (Printer printer : receiptPrinters) {
//				if(printer.isDefaultPrinter()) {
//					defaultReceiptPrinter = printer;
//					break;
//				}
//			}
//		}
//		
//		return defaultReceiptPrinter;
//	}
	
	public void setDefaultKitchenPrinter(Printer defaultKitchenPrinter) {
		this.defaultKitchenPrinter = defaultKitchenPrinter;
	}
	
	public Printer getDefaultKitchenPrinter() {
		if(defaultKitchenPrinter == null && getKitchenPrinters().size() > 0) {
			defaultKitchenPrinter = kitchenPrinters.get(0);
			
			for (Printer printer : kitchenPrinters) {
				if(printer.isDefaultPrinter()) {
					defaultKitchenPrinter = printer;
					break;
				}
			}
		}
		
		return defaultKitchenPrinter;
	}
	
//	public Printer getReceiptPrinterFor(VirtualPrinter vp) {
//		return receiptPrinterMap.get(vp);
//	}
	
	public Printer getKitchenPrinterFor(VirtualPrinter vp) {
		return kitchePrinterMap.get(vp);
	}
	
	private void populatePrinterMaps() {
		//receiptPrinterMap.clear();
		kitchePrinterMap.clear();
		
//		for (Printer printer : getReceiptPrinters()) {
//			receiptPrinterMap.put(printer.getVirtualPrinter(), printer);
//		}
		
		for (Printer printer : getKitchenPrinters()) {
			kitchePrinterMap.put(printer.getVirtualPrinter(), printer);
		}
	}
	
	public void save() {
		try {
//			getDefaultReceiptPrinter();
			getDefaultKitchenPrinter();
			
			populatePrinterMaps();
			
			File file = new File("config", "printers.xml");
			
			JAXBContext jaxbContext = JAXBContext.newInstance(PosPrinters.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			//m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

			StringWriter writer = new StringWriter();
			m.marshal(this, writer);

			FileUtils.write(file, writer.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PosPrinters load() {
		try {

			File file = new File("config", "printers.xml");
					
			if (!file.exists()) {
				return null;
			}

			
			FileReader reader = new FileReader(file);

			JAXBContext jaxbContext = JAXBContext.newInstance(PosPrinters.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			//PosPrinters printers = (PosPrinters)unmarshaller.unmarshal(file);
			PosPrinters printers = (PosPrinters) unmarshaller.unmarshal(reader);
			
			printers.populatePrinterMaps();
			
			initVirtualPrinter(printers.defaultKitchenPrinter);
			
			List<Printer> kitchenPrinters2 = printers.kitchenPrinters;
			for (Printer printer : kitchenPrinters2) {
				initVirtualPrinter(printer);
			}

			return printers;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private static void initVirtualPrinter(Printer printer) {
		if(printer == null) return;
		
		VirtualPrinter virtualPrinter = printer.getVirtualPrinter();
		
		VirtualPrinterDAO dao = VirtualPrinterDAO.getInstance();
		VirtualPrinter printerByName = dao.findPrinterByName(virtualPrinter.getName());
		if(printerByName != null) {
			printer.setVirtualPrinter(printerByName);
		}
		else {
			dao.saveOrUpdate(virtualPrinter);
		}
	}

}
