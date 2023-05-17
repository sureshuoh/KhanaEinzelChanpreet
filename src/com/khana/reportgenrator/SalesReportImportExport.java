package com.khana.reportgenrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.model.Salesreportdb;
import com.floreantpos.model.dao.GenericDAO;

public class SalesReportImportExport {
	public Salesreportdb myReport;

	public SalesReportImportExport() {

	}

	public void write(Salesreportdb report) {
		this.myReport = report;
//		Session session = null;
//		Transaction transaction = null;
		FileWriter fileWriter = null;
//		SalesReportDAO dao = new SalesReportDAO();
//		session = dao.getSession();
//		transaction = session.beginTransaction();
//		myReport = dao.load(myReport.getId());
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");

		String Destination = "C:\\Khana-einzel\\Report\\SalesReport\\" + sdf.format(date);
		File destinationFolder = new File(Destination);

		if (!destinationFolder.exists()) {
			destinationFolder.mkdirs();
		}

		StringWriter writer = new StringWriter();
		File file = new File(Destination + "\\" + myReport.getId() + ".xml");
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(SalesReportProperty.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			SalesReportProperty ticketXml = new SalesReportProperty();
			ticketXml.setMyreport(myReport);
			m.marshal(ticketXml, writer);
//			transaction.commit();
//			session.close();
			fileWriter = new FileWriter(file);
			fileWriter.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\" standalone=\"yes\"?>\r\n" + "");
			fileWriter.write(writer.toString());
			fileWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void importReport(File file) throws Exception {

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
			JAXBContext jaxbContext = JAXBContext.newInstance(SalesReportProperty.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			SalesReportProperty elements = (SalesReportProperty) unmarshaller.unmarshal(inputStream);
			session = dao.createNewSession();
			transaction = session.beginTransaction();
			Salesreportdb report1 = elements.getMyreport();
			if (report1 != null) {
				Salesreportdb report = new Salesreportdb();
				report.setAwt(report1.getAwt());
				report.setAwtn(report1.getAwtn());
				report.setAwts(report1.getAwts());
				report.setAwtz(report1.getAwtz());
				report.setCashpayment(report1.getCardpayment());
				report.setCashtax(report1.getCashtax());
				report.setSoldGutschein(report1.getSoldGutschein());
				report.setCardpayment(report1.getCardpayment());
				report.setOnline(report1.getOnline());
				report.setOnlinetax(report1.getOnlinetax());
				 
				report.setCardtax(report1.getCardtax());
				report.setVoidticket(report1.getVoidticket());
				report.setFood(report1.getFood());
				report.setFoodtax(report1.getFoodtax());
				report.setBeverage(report1.getBeverage());
				report.setBeveragetax(report1.getBeveragetax());
				report.setTotalwotax(report1.getTotalwotax());
				report.setCancelleditems(report1.getCancelleditems());
				 
				report.setTaxtotal(report1.getTaxtotal());
				report.setTaxn(report1.getTaxn());
				report.setTaxs(report1.getTaxs());
				report.setTaxz(0.00);
				report.setCancelledtrans(0.00);
				report.setCancelledtax(0.00);
				report.setDiscount(report1.getDiscount());
				report.setInsertticket(0);
				report.setSolditem(report1.getSolditem());
				report.setVoidticket(report1.getVoidticket());
				report.setVoidamount(report1.getVoidamount());
				report.setTotalinvoices(report1.getTotalinvoices());
				 
				report.setTicketgutscheinamount(report1.getTicketgutscheinamount());
				 
				report.setNetton(report1.getNetton());
				report.setNettos(report1.getNettos());
				report.setNettoz(report1.getNettoz());
				report.setReporttime(report1.getReporttime());
				// added
				 
			 
				report.setSalesid(report1.getSalesid());
				report.setStartdate(report1.getStartdate());
				report.setEnddate(report1.getEnddate());
				report.setId(report1.getId());

				 
				dao.saveOrUpdate(report, session);
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
