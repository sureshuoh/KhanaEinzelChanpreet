package com.floreantpos.add.service;

import java.io.IOException;
import java.net.UnknownHostException;

import com.floreantpos.config.TerminalConfig;

import de.cefisystems.zvt13.ZVT13;
 
import de.cefisystems.zvt13.IntermediateStatusInformationListener;

import de.cefisystems.zvt13.Exceptions.ZVTException;
import de.cefisystems.zvt13.Objects.ZVTEndOfDayObject;
import de.cefisystems.zvt13.Objects.ZVTObject;
import de.cefisystems.zvt13.Objects.ZVTRegistrationObject;
import de.cefisystems.zvt13.Objects.ZVTResponseObject;
import de.cefisystems.zvt13.Objects.ZVTStatusObject;
import de.cefisystems.zvt13.enums.*;
import de.cefisystems.zvt13.helpers.ZVTHelper;

public class Zvt_13 implements IntermediateStatusInformationListener{
	
	 
	public Zvt_13() {
		super();
	}

	public ZVTObject run(double paidAmount) throws ZVTException, InterruptedException, UnknownHostException, IOException {
		String portString = TerminalConfig.getKhanaCardPort();
	    int port = Integer.valueOf(portString);
		ZVT13 zvt13 = new ZVT13(TerminalConfig.getKhanaCardIp(), port, "000000", 38, 7000, true); //Simulator
		ZVTObject response =null;
		zvt13.addIntermediateStatusInformationListener(this);
		
		try {

			//runRegistration(zvt13, true, false);
			runRegistration(zvt13, ConfigByteEnum.X_BE_PRINT_BY_POS_NOT_INPUT_ON_TERMINAL_SEND_INTERMEDIATE_STATUS_INFORMATION);
			//Thread.sleep(3000);
			
			//ZVTStatusObject statusObject = runStatusEnquiry(zvt13);
			//System.out.println(statusObject.getStatusCode());
			//System.out.println(statusObject.getStatusMessage());
			//System.out.println(statusObject.getTerminalsoftware());
			//runAutorisation(zvt13, null, PaymentTypeEnum.GIROCARD.TYPE, "1,00");
			
			//Payment VR-Payment AdditonalData BMP60
			response = runAutorisation(zvt13, ZVTHelper.getVRPaymentData("TERM01"), PaymentTypeEnum.GIROCARD.TYPE, paidAmount+"");
			
			//runReversal(zvt13, 41, "1,00");
			
			/*
			runAutorisation(zvt13, null, PaymentTypeEnum.GIROCARD.TYPE, "1,00");
			Thread.sleep(3000);
			runReversal(zvt13, Integer.parseInt(zvtAuthObj.receiptNr), "1,00");
			//Thread.sleep(3000);
			runAutorisation(zvt13, null, PaymentTypeEnum.GIROCARD.TYPE, "1,99");
			//Thread.sleep(3000);
			runReversal(zvt13, Integer.parseInt(zvtAuthObj.receiptNr), "1,99");
			//Thread.sleep(3000);
			runAutorisation(zvt13, null, PaymentTypeEnum.GIROCARD.TYPE, "1,23");
			Thread.sleep(3000);
			runReversal(zvt13, Integer.parseInt(zvtAuthObj.receiptNr), "1,23");
			Thread.sleep(3000);
			runAutorisation(zvt13, null, PaymentTypeEnum.GIROCARD.TYPE, "10,67");
			Thread.sleep(3000);
			runReversal(zvt13, Integer.parseInt(zvtAuthObj.receiptNr), "10,67");
			*/
			
			//Thread.sleep(2000);
			//runPreAutorisation(zvt13, null);
			
			//runBookTotal(zvt13, 340, "1,00");
			//runPreAuthorizationReversal(zvt13, 8, "1,00");
			//runRefund(zvt13);
			
			//runDisplayImageIngenico(zvt13);

			//runRepeatReceipt(zvt13);

			//runAbort(zvt13);
			
			//runReversal(zvt13, Integer.parseInt(zvtAuthObj.receiptNr));
			//runReversal(zvt13, 1);
			
			//runSendTurnoverTotals(zvt13);
			//runEndOfDay(zvt13);
			 
			//runDisplayTexts(zvt13);
			  
			//runPrintSystemConfig(zvt13);
			
			//runPrintEMVConfig(zvt13);
			
			//runDiagnosis(zvt13, DiagnosisEnums.LINE_DIAGNOSIS.ACTION);
			//Thread.sleep(8000);
			//runDisplayImageIngenico(zvt13);
			
			//runResetTerminal(zvt13);
			
			//runLogoff(zvt13);
			 
	} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public void runInfo(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		zvt13.info();
	}
	
	public void runRegistration(ZVT13 zvt13, boolean printByECR, boolean inputOnTerminal) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		ZVTRegistrationObject zvtRegObj = new ZVTRegistrationObject();
		zvtRegObj = zvt13.registration(printByECR, inputOnTerminal);
		
		if(zvtRegObj.isRegistrationStatus() && zvtRegObj != null) {
			System.out.println("runRegistration() ===Registrierung erfolgreich durchgeführt===");
			System.out.println("runRegistration() ===Status: " + zvtRegObj.isRegistrationStatus() + "===");
			System.out.println("runRegistration() ===Terminal-ID: " + zvtRegObj.getTerminalID() + "===");
		} else {
			System.out.println("runRegistration() ===Registrierung nicht erfolgreich durchgeführt===");
			System.out.println("runRegistration() ===Status: " + zvtRegObj.isRegistrationStatus() + "===");
			System.out.println("runRegistration() ===Terminal-ID: " + zvtRegObj.getTerminalID() + "===");
		}
	}
	
	public void runRegistration(ZVT13 zvt13, ConfigByteEnum configByte) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		ZVTRegistrationObject zvtRegObj = new ZVTRegistrationObject();
		zvtRegObj = zvt13.registration(configByte);
		
		if(zvtRegObj.isRegistrationStatus() && zvtRegObj != null) {
			System.out.println("runRegistration() ===Registrierung erfolgreich durchgeführt===");
			System.out.println("runRegistration() ===Status: " + zvtRegObj.isRegistrationStatus() + "===");
			System.out.println("runRegistration() ===Terminal-ID: " + zvtRegObj.getTerminalID() + "===");
		} else {
			System.out.println("runRegistration() ===Registrierung nicht erfolgreich durchgeführt===");
			System.out.println("runRegistration() ===Status: " + zvtRegObj.isRegistrationStatus() + "===");
			System.out.println("runRegistration() ===Terminal-ID: " + zvtRegObj.getTerminalID() + "===");
		}
	}
	
	public ZVTObject runAutorisation(ZVT13 zvt13, byte[] additionalData, byte paymentMethod, String betrag) throws IOException, InterruptedException, ZVTException {
		ZVTResponseObject zvtAuthObj =null;
		ZVTObject zvtauth = zvt13.authorization(betrag, paymentMethod, additionalData);		
		if(zvtauth.getZVTResponseObject() != null) {
			zvtAuthObj = zvtauth.getZVTResponseObject();
			System.out.println("===ZVT-Betrag:             " + zvtAuthObj.betrag + "===");
			System.out.println("===ZVT-Kartenname:         " + zvtAuthObj.cardName + "===");
			System.out.println("===ZVT-Transaktionsnummer: " + zvtAuthObj.taNr + "===");
			System.out.println("===ZVT-Belegnummer:        " + zvtAuthObj.receiptNr + "===");
			System.out.println("===ZVT-Zahlungscode:       " + zvtauth.getTransactionCode() + "===");
			System.out.println("===ZVT-CardType:           " + zvtAuthObj.cardType + "===");
			System.out.println("===ZVT-CardTypeID:         " + zvtAuthObj.cardTypeID + "===");
			System.out.println("===ZVT-CardName:           " + zvtAuthObj.cardName + "===");
			 
			//System.out.println("===ZVT-Transaktionstatus:  " + "Zahlung ERFOLGREICH!===");
			System.out.println("HÄNDLERBELEG:\n" + zvtauth.getMerchantReceipt());		 
			System.out.println("KUNDENBELEG:\n" + zvtauth.getCustomerReceipt());
		} else {
			System.out.println("===ZVT-Zahlung nicht erfolgreich abgeschlossen===");
			System.out.println("HÄNDLERBELEG:\n" + zvtauth.getMerchantReceipt());
			System.out.println();
			System.out.println("KUNDENBELEG:\n" + zvtauth.getCustomerReceipt());
		}	
		
		return zvtauth;
	}
	
	public void runPreAutorisation(ZVT13 zvt13, byte[] additionalData) throws IOException, InterruptedException, ZVTException {
		ZVTObject zvtpreauth = zvt13.preAuthorization("0.01", additionalData,"");
		
		if(zvtpreauth.getZVTResponseObject() != null) {
			ZVTResponseObject zvtAuthObj = zvtpreauth.getZVTResponseObject();
			System.out.println("===ZVT-Betrag:             " + zvtAuthObj.betrag + "===");
			System.out.println("===ZVT-Kartenname:         " + zvtAuthObj.cardName + "===");
			System.out.println("===ZVT-Transaktionsnummer: " + zvtAuthObj.taNr + "===");
			System.out.println("===ZVT-Belegnummer:        " + zvtAuthObj.receiptNr + "===");
			System.out.println("===ZVT-Zahlungscode:       " + zvtpreauth.getTransactionCode() + "===");
			System.out.println("===ZVT-CardType:           " + zvtAuthObj.cardType + "===");
			System.out.println("===ZVT-CardTypeID:         " + zvtAuthObj.cardTypeID + "===");
			System.out.println("===ZVT-CardName:           " + zvtAuthObj.cardName + "===");
			
			//System.out.println("===ZVT-Transaktionstatus:  " + "Zahlung ERFOLGREICH!===");
			System.out.println("HÄNDLERBELEG:\n" + zvtpreauth.getMerchantReceipt());
			System.out.println();
			System.out.println("KUNDENBELEG:\n" + zvtpreauth.getCustomerReceipt());
		} else {
			System.out.println("===ZVT-Zahlung nicht erfolgreich abgeschlossen===");
			System.out.println("HÄNDLERBELEG:\n" + zvtpreauth.getMerchantReceipt());
			System.out.println();
			System.out.println("KUNDENBELEG:\n" + zvtpreauth.getCustomerReceipt());
		}
		
	}
	
	public void runRefund(ZVT13 zvt13) throws IOException, InterruptedException, ZVTException {
		ZVTObject zvtobject1 = zvt13.refund("0.01", PaymentTypeEnum.EURO_ELV_ONLINE.TYPE, null);
		System.out.println("KUNDENBELEG:\n\n" + zvtobject1.getCustomerReceipt());
		System.out.println("HÄNDLERBELEG:\n\n" + zvtobject1.getMerchantReceipt());
	}
	
	public void runReversal(ZVT13 zvt13, int receiptNo, String betrag) throws IOException, InterruptedException, ZVTException {
		ZVTObject zvtobject = zvt13.reversal(betrag, receiptNo, PaymentTypeEnum.GIROCARD.TYPE, null);
		System.out.println("Transaktionstatus: " + zvtobject.isTransactionStatus());
		System.out.println("HÄNDLERBELEG:\n\n" + zvtobject.getMerchantReceipt());
		System.out.println("KUNDENBELEG:\n\n" + zvtobject.getCustomerReceipt());
	}
	
	public void runPreAuthorizationReversal(ZVT13 zvt13, int receiptNr, String betrag) throws IOException, InterruptedException, ZVTException {
		ZVTObject zvtobject = zvt13.preAuthorizationReversal(betrag, receiptNr, PaymentTypeEnum.GIROCARD.TYPE, null);
		System.out.println("Transaktionstatus: " + zvtobject.isTransactionStatus());
		System.out.println("HÄNDLERBELEG:\n\n" + zvtobject.getMerchantReceipt());
		System.out.println("KUNDENBELEG:\n\n" + zvtobject.getCustomerReceipt());
	}
	
	public void runBookTotal(ZVT13 zvt13, int receiptNr, String betrag) throws IOException, InterruptedException, ZVTException {
		ZVTObject zvtobject = zvt13.bookTotal(betrag, receiptNr, PaymentTypeEnum.GIROCARD.TYPE, null);
		System.out.println("Transaktionstatus: " + zvtobject.isTransactionStatus());
		System.out.println("HÄNDLERBELEG:\n\n" + zvtobject.getMerchantReceipt());
		System.out.println("KUNDENBELEG:\n\n" + zvtobject.getCustomerReceipt());
	}
	
	public void runDisplayTexts(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		String[] texts = {"Herzlich Willkommen", "                bei", "        CeFiSystems", "", "", ""};
		//String[] texts = {"           Herzlich Willkommen", "                          bei", "    CeFiSystems Cedric Fischer", "", "       Terminal betriebsbereit",""};
		//String[] texts = {"Herzlich Willkommen", "bei", "CeFiSystems Cedric Fischer", "", "Terminal betriebsbereit",""};
		//String[] texts = {"Für diese Zahlung ist eine", "Unterschrift erforderlich!", "", "", "Bitte auf dem Unterschriftspad", "unterschreiben"};
		zvt13.displayText(texts);
	}
	
	public void runDisplayImageIngenico(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		zvt13.displayImageIngenico("D:\\lane5000.png");
		//zvt13.displayImageIngenico("D:\\qrcode.png");
	}
	
	public void runDisplayImageNormal(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		//zvt13.displayImageNormal("D:\\oemlogo_12.png");
		zvt13.displayImageNormal("D:\\qrcode.png");
	}
	
	public void runRepeatReceipt(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		ZVTObject zvtobject = zvt13.repeatReceipt(RepeatReceiptActionEnums.PAYMENT_RECEIPT_CUSTOMER.ACTION);
		System.out.println("Kunde: " + zvtobject.getCustomerReceipt());
		System.out.println("___________________________________________");
		System.out.println("Händler: " + zvtobject.getMerchantReceipt());
	}
	
	public void runEndOfDay(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		ZVTEndOfDayObject zvtEoD = zvt13.endOfDay();
		if(zvtEoD.isTransactionStatus()) {
			System.out.println("===ZVT-Tagesabschluss erfolgreich===");
			System.out.println("Tagesabschlussbeleg:\n\n");
			System.out.println(zvtEoD.getMerchantReceipt());
			//System.out.println(zvtEoD.get);
			System.out.println("Transaktionscode: " + zvtEoD.getTransactionCode());
			System.out.println("Gesamtbetrag (String): " + zvtEoD.getTotalEoDAmountString());
			System.out.println("Gesamtbetrag (Double): " + zvtEoD.getTotalEoDAmountDouble());
		} else {
			System.out.println("===ZVT-Tagesabschluss NICHT erfolgreich===");
			System.out.println("Gesamtbetrag: " + zvtEoD.getTotalEoDAmountString());
			System.out.println("Gesamtbetrag: " + zvtEoD.getTotalEoDAmountDouble());
			System.out.println(zvtEoD.getTransactionCode());
			System.out.println(zvtEoD.getError());

		}
	}
	
	public void runSendTurnoverTotals(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		ZVTEndOfDayObject zvtEoD = zvt13.turnoverTotals();
		if(zvtEoD.isTransactionStatus()) {
			System.out.println("===ZVT-Zwischensumme erfolgreich===");
			System.out.println("Zwischensummenbericht:\n\n");
			System.out.println(zvtEoD.getMerchantReceipt());
			System.out.println("Transaktionscode: " + zvtEoD.getTransactionCode());
			System.out.println("Gesamtbetrag (String): " + zvtEoD.getTotalEoDAmountString());
			System.out.println("Gesamtbetrag (Double): " + zvtEoD.getTotalEoDAmountDouble());
		} else {
			System.out.println("===ZVT-Zwischensumme NICHT erfolgreich===");
			System.out.println("Gesamtbetrag: " + zvtEoD.getTotalEoDAmountString());
			System.out.println("Gesamtbetrag: " + zvtEoD.getTotalEoDAmountDouble());
			System.out.println(zvtEoD.getTransactionCode());
			System.out.println(zvtEoD.getError());

		}
	}
	
	public void runPrintSystemConfig(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		System.out.println("Systemkonfiguration: " + zvt13.printSystemConfig());
	}
	
	public void runPrintEMVConfig(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		System.out.println("Systemkonfiguration: " + zvt13.printEMVConfig());
	}
	
	public void runDiagnosis(ZVT13 zvt13, byte action) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		System.out.println("Diagnosebeleg: " + zvt13.diagnosis(action));
	}
	
	public void runLogoff(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException {
		zvt13.logoff();
	}

	
	public void runAbort(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException {
		zvt13.abort();
	}
	
	public ZVTStatusObject runStatusEnquiry(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException {
		return zvt13.statusEnquiry();
	}
	
	public void runResetTerminal(ZVT13 zvt13) throws UnknownHostException, InterruptedException, IOException, ZVTException {
		zvt13.rebootTerminal();
	}
	
	@Override
	public void intermediateStatusInformation(String text) {
		System.out.println("Listener: " + text);
	}
	
	
}
 