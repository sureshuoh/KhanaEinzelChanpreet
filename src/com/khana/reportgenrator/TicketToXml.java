package com.khana.reportgenrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.floreantpos.main.Application;

import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.model.dao.UserTypeDAO;
 

public class TicketToXml {
	public Ticket myticket;

	public TicketToXml() {
		dineInTax = Application.getInstance().getDineInTax();
		homeDelTax = Application.getInstance().getHomeDeleveryTax();
	}

	public void write(Ticket ticket) {
		this.myticket = ticket;
		Session session = null;
		Transaction transaction = null;
		FileWriter fileWriter = null;
		TicketDAO dao = new TicketDAO();
		session = dao.getSession();
		transaction = session.beginTransaction();
		myticket = dao.loadFullTicket(myticket.getId(), session);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");

		String Destination = "C:\\Khana\\Report\\" + sdf.format(date);
		File destinationFolder = new File(Destination);

		if (!destinationFolder.exists()) {
			destinationFolder.mkdirs();
		}

		StringWriter writer = new StringWriter();
		File file = new File(Destination + "\\" + myticket.getTicketid() + ".xml");
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(TicketPropertyXml.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			TicketPropertyXml ticketXml = new TicketPropertyXml();
			ticketXml.setMyticket(myticket);
			m.marshal(ticketXml, writer);
			transaction.commit();
			session.close();
			fileWriter = new FileWriter(file);
			fileWriter.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\" standalone=\"yes\"?>\r\n" + "");
			fileWriter.write(writer.toString());
			fileWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	int count = 1;

	public void exportTicket(Ticket ticket) {
		FileWriter fileWriter = null;
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");

			String Destination = "C:\\Khana\\Report\\" + sdf.format(date);
			File destinationFolder = new File(Destination);
			if (!destinationFolder.exists()) {
				destinationFolder.mkdirs();
			}

			ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
			int ticketId = ticket.getTicketid() != null ? ticket.getTicketid() : count;
			File file = new File(Destination + "\\" + ticketId + ".xml");

			JAXBContext jaxbContext = JAXBContext.newInstance(Ticket.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

			StringWriter writer = new StringWriter();
			m.marshal(ticket, writer);

			fileWriter = new FileWriter(file);
//			fileWriter.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\" standalone=\"yes\"?>\r\n" + 
//					"");
			fileWriter.write(writer.toString());
			fileWriter.close();
			count++;

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fileWriter);
		}
	}

	public void exportTicketXml(Ticket ticket) throws IOException {
		XmlMapper xmlMapper = new XmlMapper();

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");

		String Destination = "C:\\Khana\\Report\\" + sdf.format(date);
		File destinationFolder = new File(Destination);
		if (!destinationFolder.exists()) {
			destinationFolder.mkdirs();
		}

		ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
		int ticketId = ticket.getId() != null ? ticket.getId() : ticket.getId();
		File file = new File(Destination + "\\" + ticketId + ".xml");

		// serialize our Object into XML string
		String xmlString;
		try {
			xmlString = xmlMapper.writeValueAsString(ticket);
//			// write to the console
//	        System.out.println(xmlString);
//	        

			// write XML string to file
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\" standalone=\"yes\"?>\r\n" + "");
			fileWriter.write(xmlString);
			fileWriter.close();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Table Export xml
	 */

	 

	 

	double dineInTax;
	double homeDelTax;

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public void importTicketFromFile(File file) throws JAXBException {
		Session session = null;
		Transaction transaction = null;
		GenericDAO dao = new GenericDAO();
		XmlMapper xmlMapper = new XmlMapper();
		try {

// read file and put contents into the string
			String readContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

// deserialize from the XML into a ticket object

			Ticket ticket1 = xmlMapper.readValue(readContent, Ticket.class);
			try {
				session = dao.createNewSession();
				transaction = session.beginTransaction();
	
				if (transaction.wasCommitted())
					System.out.println("Transaction committed");
				transaction.commit();
			} catch (Exception e1) {

				if (transaction != null)
					transaction.rollback();
				throw e1;

			} finally {
				dao.closeSession(session);
			}

//			  FileInputStream inputStream = new FileInputStream(file);
//		      JAXBContext jaxbContext = JAXBContext.newInstance(Ticket.class);
//		      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//		      Ticket ticket1 = (Ticket) unmarshaller.unmarshal(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	  public static void importTicketFromFile(File file) {
//		    try {
//		      FileInputStream inputStream = new FileInputStream(file);
//		      JAXBContext jaxbContext = JAXBContext.newInstance(Ticket.class);
//		      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//		      Ticket ticket1 = (Ticket) unmarshaller.unmarshal(inputStream);
//		      if (ticket1 != null) {
//					Ticket ticket = new Ticket();
//					List<TicketItem> newItemList = new ArrayList<>();
//					for(TicketItem titem:ticket1.getDeletedItems()) {
//						TicketItem newTicketItem = new TicketItem();
//						newTicketItem.setItemId(titem.getItemId());
//						newTicketItem.setItemCount(titem.getItemCount());
//						newTicketItem.setHasModifiers(titem.isHasModifiers());
//						newTicketItem.setName(titem.getName());
//						newTicketItem.setUnitPrice(titem.getUnitPrice());
//						newTicketItem.setBon(titem.getBon());
//						newTicketItem.setGroupName(titem.getGroupName());
//						newTicketItem.setCategoryName(titem.getCategoryName());
//						newTicketItem.setDiscountRate(titem.getDiscountRate());
//						newTicketItem.setPrintorder(titem.getPrintorder());
//						newTicketItem.setPrintedToKitchen(titem.isPrintedToKitchen());
//						newTicketItem.setTaxRate(titem.getTaxRate());
//						newTicketItem.setBeverage(titem.isBeverage());
//						newTicketItem.setShouldPrintToKitchen(titem.isShouldPrintToKitchen());
//						newTicketItem.setMenuItemId(titem.getMenuItemId());
//						newTicketItem.setMaximumExtras(titem.getMaximumExtras());
//						newTicketItem.setSplit(titem.getSplit());
//						newItemList.add(newTicketItem);						
//					}
//					ticket.addDeletedItems(newItemList);
//					ticket.addDeletedItemsAmount(ticket1.getDeletedItemsAmount()!=null?ticket1.getDeletedItemsAmount():0.00);
//					ticket.setTicketid(ticket1.getTicketid()!=null?ticket1.getTicketid():null);
//					ticket.setActiveDate(ticket1.getActiveDate()!=null?ticket1.getActiveDate():null);
//					List<TicketItem> newItemList1 = new ArrayList<>();
//					for(TicketItem titem:ticket1.getTicketItems()) {
//						TicketItem newTicketItem = new TicketItem();
//						newTicketItem.setItemId(titem.getItemId());
//						newTicketItem.setTicketItemModifierGroups(titem.getTicketItemModifierGroups());
//						newTicketItem.setItemCount(titem.getItemCount());
//						newTicketItem.setHasModifiers(titem.isHasModifiers());
//						newTicketItem.setName(titem.getName());
//						newTicketItem.setUnitPrice(titem.getUnitPrice());
//						newTicketItem.setBon(titem.getBon());
//						newTicketItem.setGroupName(titem.getGroupName());
//						newTicketItem.setCategoryName(titem.getCategoryName());
//						newTicketItem.setDiscountRate(titem.getDiscountRate());
//						newTicketItem.setPrintorder(titem.getPrintorder());
//						newTicketItem.setPrintedToKitchen(titem.isPrintedToKitchen());
//						newTicketItem.setTaxRate(titem.getTaxRate());
//						newTicketItem.setBeverage(titem.isBeverage());
//						newTicketItem.setShouldPrintToKitchen(titem.isShouldPrintToKitchen());
//						newTicketItem.setMenuItemId(titem.getMenuItemId());
//						newTicketItem.setMaximumExtras(titem.getMaximumExtras());
//						newTicketItem.setSplit(titem.getSplit());
//						newItemList1.add(newTicketItem);						
//					}					
//					ticket.setTicketItems(newItemList1);
//					ticket.setDebitorPaid(ticket1.getDebitorPayment());
//					ticket.setGutscheinPayment(ticket1.getGutscheinPayment());
//					ticket.setDebitorPayment(ticket1.getDebitorPayment());
//					ticket.setTicketType(ticket1.getTicketType()!=null?ticket1.getTicketType():TicketType.DINE_IN.name());
//					ticket.addMovedItemsAmount(ticket1.getMovedItemsAmount()!=null?ticket1.getMovedItemsAmount():0);
//					ticket.setTotalAmount(ticket1.getTotalAmount());
//					ticket.setCardpaymenttype(ticket1.getCardpaymenttype()!=null?ticket1.getCardpaymenttype():null);
//					ticket.setBewirt(ticket1.getBewirt()!=null?ticket1.getBewirt():false);
//					ticket.setMovedItemsCount(ticket1.getMovedItemsCount()!=null?ticket1.getMovedItemsCount():0);
//					ticket.setTipAmount(ticket1.getTipAmount()!=null?ticket1.getTipAmount():0.00);
//					ticket.setCashPayment(ticket1.getCashPayment());
//					ticket.setOwner(ticket1.getOwner());
//					ticket.setTaxAmount(ticket1.getTaxAmount());
//					ticket.setClosingDate(ticket1.getClosingDate()!=null?ticket1.getClosingDate():null);
//					ticket.setCreateDate(ticket1.getCreateDate()!=null?ticket1.getCreateDate():null);
//					ticket.setCreationHour(ticket1.getCreationHour()!=null?ticket1.getCreationHour():null);
//					ticket.setDrawerResetted(ticket1.isDrawerResetted()!=null?ticket1.isDrawerResetted():false);
//					ticket.setVoided(ticket1.isVoided()!=null?ticket1.isVoided():false);
//					ticket.setVoidedBy(ticket1.getVoidedBy()!=null?ticket1.getVoidedBy():null);
//					ticket.setVoidReason(ticket1.getVoidReason()!=null?ticket1.getVoidReason():"");
//					ticket.setPriceIncludesTax(ticket1.isPriceIncludesTax());
//					ticket.setChangesAvailable(ticket1.isChangesAvailable()!=null?ticket1.isChangesAvailable():false);
//					ticket.setClosed(ticket1.isClosed());
//					ticket.setPrinted(ticket1.getPrinted());
//					ticket.setPaid(ticket1.isPaid());
//					ticket.setTableNumbers(ticket1.getTableNumbers());
//		    		
//		    		List<TicketCouponAndDiscount> coupanList = new ArrayList<>();
//		    		if(ticket1.getCouponAndDiscounts()!=null) {
//		    			for(TicketCouponAndDiscount coupanD:ticket1.getCouponAndDiscounts()) {
//			    			TicketCouponAndDiscount myDis = new TicketCouponAndDiscount();
//			    			myDis.setId(myDis.getId());
//			    			myDis.setCouponAndDiscountId(coupanD.getCouponAndDiscountId());
//			    			myDis.setGutschein(coupanD.getGutschein());
//			    			myDis.setName(coupanD.getName());
//			    			myDis.setType(coupanD.getType());
//			    			myDis.setUsedValue(coupanD.getUsedValue());
//			    			myDis.setValue(coupanD.getValue());
//			    			coupanList.add(myDis);
//			    		}
//		    		}    		
//		    		
//		    		ticket.setCouponAndDiscounts(coupanList);
//					ticket.setGutscheinHausbon(ticket1.getGutscheinHausbon()!=null?ticket1.getGutscheinHausbon():false);				
//					ticket.setLieferbarcodeid(ticket1.getLieferbarcodeid()!=null?ticket1.getLieferbarcodeid():null);
//					ticket.setDailylieferid(ticket1.getDailylieferid()!=null?ticket1.getDailylieferid():"");
//					ticket.setSalesid(ticket1.getSalesid()!=null?ticket1.getSalesid():null);
//					TicketDAO.getInstance().save(ticket);
//				}
//		      
//		    } catch (Exception e) {
//		      e.printStackTrace();
//		   }
//		  }	

	@SuppressWarnings({ "unused" })
	public void importTicket(File file) throws Exception {

		if (file == null) {
			System.out.print("asdf");
			return;
		}
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (inputStream == null)
			return;

		Session session = null;
		Transaction transaction = null;
		GenericDAO dao = new GenericDAO();
		Map<String, Object> objectMap = new HashMap<String, Object>();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Ticket.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Ticket ticket1 = (Ticket) unmarshaller.unmarshal(inputStream);
			session = dao.createNewSession();
			transaction = session.beginTransaction();
//				Ticket ticket1 = elements.getMyticket();
			if (ticket1 != null) {
				Ticket ticket = new Ticket();
				List<TicketItem> newItemList = new ArrayList<>();
				 
				ticket.addDeletedItems(newItemList);
				dao.saveOrUpdate(ticket, session);
				 
				ticket.setTicketid(ticket1.getTicketid() != null ? ticket1.getTicketid() : null);
				ticket.setActiveDate(ticket1.getActiveDate() != null ? ticket1.getActiveDate() : null);
				List<TicketItem> newItemList1 = new ArrayList<>();
				for (TicketItem titem : ticket1.getTicketItems()) {
					TicketItem newTicketItem = new TicketItem();
					newTicketItem.setItemId(titem.getItemId());
					newTicketItem.setItemCount(titem.getItemCount());
					newTicketItem.setHasModifiers(titem.isHasModifiers());
					newTicketItem.setName(titem.getName());
					newTicketItem.setUnitPrice(titem.getUnitPrice());
					newTicketItem.setBon(titem.getBon());
					newTicketItem.setGroupName(titem.getGroupName());
					newTicketItem.setCategoryName(titem.getCategoryName());
					newTicketItem.setDiscountRate(titem.getDiscountRate());
					newTicketItem.setPrintorder(titem.getPrintorder());
					newTicketItem.setPrintedToKitchen(titem.isPrintedToKitchen());
					newTicketItem.setTaxRate(titem.getTaxRate());
					newTicketItem.setTaxAmount(titem.getTaxAmount());
					newTicketItem.setBeverage(titem.isBeverage());
					newTicketItem.setShouldPrintToKitchen(titem.isShouldPrintToKitchen());
					 
					newItemList1.add(newTicketItem);
				}
				ticket.setTicketItems(newItemList1);
				dao.saveOrUpdate(ticket, session);
				 
				ticket.setGutscheinPayment(ticket1.getGutscheinPayment());
				 
				ticket.setTicketType(
						ticket1.getTicketType() != null ? ticket1.getTicketType() : TicketType.DINE_IN.name());
				 
				ticket.setTotalAmount(ticket1.getTotalAmount());
				ticket.setCardpaymenttype(ticket1.getCardpaymenttype() != null ? ticket1.getCardpaymenttype() : null);
				 
				 
				ticket.setTipAmount(ticket1.getTipAmount() != null ? ticket1.getTipAmount() : 0.00);
				ticket.setCashPayment(ticket1.getCashPayment());
				ticket.setOwner(ticket1.getOwner());
				ticket.setTaxAmount(ticket1.getTaxAmount());
				ticket.setClosingDate(ticket1.getClosingDate() != null ? ticket1.getClosingDate() : null);
				ticket.setCreateDate(ticket1.getCreateDate() != null ? ticket1.getCreateDate() : null);
				ticket.setCreationHour(ticket1.getCreationHour() != null ? ticket1.getCreationHour() : null);
				ticket.setDrawerResetted(ticket1.isDrawerResetted() != null ? ticket1.isDrawerResetted() : false);
				ticket.setVoided(ticket1.isVoided() != null ? ticket1.isVoided() : false);
				ticket.setVoidedBy(ticket1.getVoidedBy() != null ? ticket1.getVoidedBy() : null);
				ticket.setVoidReason(ticket1.getVoidReason() != null ? ticket1.getVoidReason() : "");
				ticket.setPriceIncludesTax(ticket1.isPriceIncludesTax());
				ticket.setChangesAvailable(ticket1.isChangesAvailable() != null ? ticket1.isChangesAvailable() : false);
				ticket.setClosed(ticket1.isClosed());
				ticket.setPrinted(ticket1.getPrinted());
				ticket.setPaid(ticket1.isPaid());
				Set<ShopTable> tables = ticket1.getTables();
				if (tables != null) {
					ShopTable table = new ShopTable();
					for (ShopTable shopTable : tables) {
						table.setNumber(shopTable.getNumber());
					}
					ticket.addTotables(table);
				}

				List<TicketCouponAndDiscount> coupanList = new ArrayList<>();
				if (ticket1.getCouponAndDiscounts() != null) {
					for (TicketCouponAndDiscount coupanD : ticket1.getCouponAndDiscounts()) {
						TicketCouponAndDiscount myDis = new TicketCouponAndDiscount();
						myDis.setId(myDis.getId());
						myDis.setCouponAndDiscountId(coupanD.getCouponAndDiscountId());
						myDis.setGutschein(coupanD.getGutschein());
						myDis.setName(coupanD.getName());
						myDis.setType(coupanD.getType());
						myDis.setUsedValue(coupanD.getUsedValue());
						myDis.setValue(coupanD.getValue());
						coupanList.add(myDis);
					}
				}

				ticket.setCouponAndDiscounts(coupanList);
				ticket.setGutscheinHausbon(
						ticket1.getGutscheinHausbon() != null ? ticket1.getGutscheinHausbon() : false);
				 
				ticket.setSalesid(ticket1.getSalesid() != null ? ticket1.getSalesid() : null);
				dao.saveOrUpdate(ticket, session);
			}

			if (transaction.wasCommitted())
				System.out.println("Transaction committed");
			transaction.commit();
		} catch (Exception e1) {

			if (transaction != null)
				transaction.rollback();
			throw e1;

		} finally {
			dao.closeSession(session);
			IOUtils.closeQuietly(inputStream);
		}
	}

}
