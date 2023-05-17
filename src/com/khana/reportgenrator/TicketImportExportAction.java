package com.khana.reportgenrator;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.Ticket;

public class TicketImportExportAction {
	public boolean importFile;
	public static TicketImportExportAction tImportExport;

	public static TicketImportExportAction gettImportExport() {
		return tImportExport = new TicketImportExportAction();
	}

	public TicketImportExportAction() {

	}

	public void importTicket() {
		JFileChooser chooser = new JFileChooser("C:\\Khana-enzel\\Report");
		chooser.setMultiSelectionEnabled(true);
		chooser.showOpenDialog(BackOfficeWindow.getInstance());
		chooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "XML File";
			}

			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".xml")) {
					return true;
				}

				return false;
			}
		});

		File[] files = chooser.getSelectedFiles();

		for (File myfile : files) {
			TicketToXml xmlWriter = new TicketToXml();
			try {
				xmlWriter.importTicket(myfile);
				;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public void exportTicket(Ticket ticket) {
		TicketToXml xmlWriter = new TicketToXml();
//			try {
//				xmlWriter.write(ticket);
//			}catch(Exception ex) {
//				ex.printStackTrace();
//			}
		try {
			xmlWriter.exportTicket(ticket);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
