package com.khana.tse.fiskaly.transaction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.jfree.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiskaly.sdk.FiskalyClientException;
import com.fiskaly.sdk.FiskalyHttpException;
import com.fiskaly.sdk.FiskalyHttpResponse;
import com.fiskaly.sdk.FiskalyHttpTimeoutException;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
 
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.TSEReceiptData;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.TicketType;
 
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TSEReceiptDataDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.BusinessDateUtil;

import org.apache.http.util.EntityUtils;
import org.apache.xerces.util.URI;

import com.khana.tse.fiskaly.AmountsPerPaymentType;
import com.khana.tse.fiskaly.AmountsPerVatRate;
import com.khana.tse.fiskaly.FiskalyKeyParameter;
import com.khana.tse.fiskaly.FiskalyNumberFormat;
import com.khana.tse.fiskaly.LineItem;

import com.khana.tse.fiskaly.Receipt;

import com.sleepycat.je.utilint.Timestamp;

import net.authorize.util.HttpClient;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jyoti Rai
 *
 */

public class FiskalyTransactionControllerV2 {

	static FiskalyRegularTransactionServiceV2 service = new FiskalyRegularTransactionServiceV2();
	static FiskalyKeyParameter params = new FiskalyKeyParameter();
	String token;
	static String token_;
	public static void main(String[] args) {
		FiskalyTransactionControllerV2 con = new FiskalyTransactionControllerV2();
		try {
			params = con.createClientId(params);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(token_.length()==0||token_.isEmpty()) {
			try {
				token_=	login_();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		params.setToken(token_);
		params.setClientId("6f3d9750-6b4c-4bdc-8c20-77c3a960e2dc");
		params.setTssId("0ca501fa-4f82-4682-9979-7f8f9b54fddd");
		params.setTransactionIdReceipt("2ef1711c-58c2-4b8c-8632-b48ee8183947");
		params = con.startReceipt(params);
		params.setLatestRevisionReceipt(1);
		Receipt receipt = new Receipt();
		receipt.setReceiptType(FiskalyReceiptType.RECEIPT.name());
		receipt.setAmountsPerVatRate(Arrays.asList(
				new AmountsPerVatRate(VatRate.SPECIAL_RATE_1.name(), FiskalyNumberFormat.formatToThreeDecimal(10.0)),
				new AmountsPerVatRate(VatRate.SPECIAL_RATE_2.name(), FiskalyNumberFormat.formatToThreeDecimal(5.0))));
		receipt.setAmountsPerPaymentType(Arrays.asList(new AmountsPerPaymentType(FiskalyPaymentType.NON_CASH.name(),
				FiskalyNumberFormat.formatToThreeDecimal(15.0))));
		con.finishReceipt(params, receipt); 
	}
	
	private UUIDGenerator UUID = new UUIDGenerator();
	
	public static String login_() throws IOException {
		 
	 	try {
	 		token_ = service.login_();	 	
		} finally {} 
		 return token_;
	}
	 
	public String login() throws IOException {
		 
	 	try {
	 		token = service.login();
	 		token_=token; 

		} finally {} 
		 return token;
	}
	
	public FiskalyKeyParameter createTSS() throws IOException {
		 
	 	try {
	 		String response  = service.createTSS();
	 		System.out.println("response createTss: "+response);
	 		
	 		params = getParamsFromResponse(response, params, "createTSS");

		} finally {} 
		return params;
	}
	
	public FiskalyKeyParameter changeAdminPin() {
		try {
	 		String response  = service.changeAdminPin();
	 		System.out.println("response changeAdminPin: "+response);
	 		
	 		params = getParamsFromResponse(response, params, "changeAdminPin");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {} 
		return params;
	}
	
	public FiskalyKeyParameter authIntialization() {
		try {
	 		String response  = service.authIntialization();
	 		 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {} 
		return params;
	}
	
	public FiskalyKeyParameter getTSSInfo() throws IOException, URISyntaxException {
		 
	 	try {
	 		String response  = service.getTSSInfo();	 		
	 		params = getParamsFromResponse(response, params, "tss_info");

		} catch (IOException e) {
			System.out.println("Method: getTSSInfo  " + "Message: " + e.getMessage());
		} 
		return params;
	}
	
	public FiskalyKeyParameter createClientId(FiskalyKeyParameter param) throws URISyntaxException {
		System.out.println("param.getTssId(): "+ param.getTssId()+", token: "+token_);
		try {
			String response = service.createClientID(param.getTssId());
			param = getParamsFromResponse(response, param, "createClient");

		} catch (IOException e) {
			System.out.println("Method: createClient  " + "Message: " + e.getMessage());
		}

		return param;
	}
	
	private FiskalyKeyParameter getParamsFromResponse(String response, FiskalyKeyParameter params2, String method) {
		JSONObject body = new JSONObject(response);
		
			if (method.equals("createTSS")) {
				params.setTssId(body.getString("_id"));
				params.setAdminPuk(body.getString("admin_puk"));
			}
			if (method.equals("tss_info")) {
				params.setTssId(body.getString("_id"));
				params.setTssSerialNumer(body.getString("serial_number"));
			}   
			if (method.equals("createClient")) {
				params.setClientId(body.getString("_id"));
			}
			 if(method.equals("changeAdminPin")) {
				 params.setAdminPin(body.getString("new_admin_pin"));
			 }
			if (method.equals("startReceipt")) {
				params.setTransactionIdReceipt(body.getString("_id"));				 
				params.setLatestRevisionReceipt(body.getInt("latest_revision"));
			}
			if (method.equals("startOrder")) {				
				params.setTransactionId(body.getString("_id"));
				params.setLatestRevision(body.getInt("latest_revision"));
				params.setTssId(body.getString("tss_id"));
				System.out.println("transctionId: "+body.getString("_id")+", latesRivisn: "+params.getLatestRevision());
			}
			if (method.equals("updateOrder") || method.equals("finishOrder") || method.equals("updateOrder2")) {
				params.setLatestRevision(body.getInt("latest_revision"));
				System.out.println("LastetRevisionNo.:"+body.getInt("latest_revision"));
			}
			return params;		
	}

	public FiskalyKeyParameter startReceipt(FiskalyKeyParameter param) {
		try {
			 
			String response = service.startReceipt(param);
			param = getParamsFromResponse(response, param, "startReceipt");

		} catch (IOException e) {
			System.out.println("Method: startReceipt  " + "Message: " + e.getMessage());
		}
		System.out.println("1. Transaction Id Recept: " + param.getTransactionId());
		return param;
	}
	
	public FiskalyKeyParameter reStartReceipt(FiskalyKeyParameter param) {
		try {
			String response = service.reStartReceipt(param);
			param = getParamsFromResponse(response, param, "startReceipt");
		} catch (IOException e) {
			System.out.println("Method: startReceipt  " + "Message: " + e.getMessage());
		}
		return param;
	}

	public FiskalyKeyParameter startOrder(FiskalyKeyParameter param) {
		try {			 
			String response = service.startOrder(param);
			param = getParamsFromResponse(response, param, "startOrder");
		} catch (IOException e) {
			System.out.println("Method: startOrder  " + "Message: " + e.getMessage());
		}

		return param;
	}

	public FiskalyKeyParameter updateOrder(FiskalyKeyParameter param, List<TicketItem> itemList) {

		try {
			String response = service.updateOrder(param, createLineItems(itemList));
			param = getParamsFromResponse(response, param, "updateOrder");
		} catch (IOException e) {
			System.out.println("Method: updateOrder  " + "Message: " + e.getMessage());
		}

		return param;
	} 
	
	
	public Receipt createReceipt(List<TicketItem> items, FiskalyPaymentType paymetType,
			FiskalyReceiptType receiptType) {

		Receipt receipt = new Receipt();
		if (receiptType.compareTo(FiskalyReceiptType.CANCELLATION) == 0) {
			receipt.setReceiptType(FiskalyReceiptType.CANCELLATION.name());
			receipt.setAmountsPerVatRate(Arrays.asList());
		} else {
			receipt.setReceiptType(receiptType.name());
			double imhaus = 0.0;
			double ausserHaus = 0.0;
			double zero = 0.0;
			double imhausT = Application.getInstance().getDineInTax();
			double ausserHausT = Application.getInstance().getHomeDeleveryTax();
			 
			for (TicketItem item : items) {
				 
				if (item.getTaxRate() == imhausT) {
					imhaus += item.getTotalAmount();				
					 
				} else if (item.getTaxRate() == ausserHausT) {
					ausserHaus += item.getTotalAmount();
					
				} else if (item.getTaxRate() == 0.0) {
					zero += item.getTotalAmount();
					 
				}
			}

			if (TerminalConfig.isSpecial())
				receipt.setAmountsPerVatRate(Arrays.asList(
						new AmountsPerVatRate(VatRate.SPECIAL_RATE_1.name(),
								FiskalyNumberFormat.formatToThreeDecimal(imhaus)),
						new AmountsPerVatRate(VatRate.SPECIAL_RATE_2.name(),
								FiskalyNumberFormat.formatToThreeDecimal(ausserHaus)),
						new AmountsPerVatRate(VatRate.NULL.name(), FiskalyNumberFormat.formatToThreeDecimal(zero))));
			else
				receipt.setAmountsPerVatRate(Arrays.asList(
						new AmountsPerVatRate(VatRate.NORMAL.name(), FiskalyNumberFormat.formatToThreeDecimal(imhaus)),
						new AmountsPerVatRate(VatRate.REDUCED_1.name(),
								FiskalyNumberFormat.formatToThreeDecimal(ausserHaus)),
						new AmountsPerVatRate(VatRate.NULL.name(), FiskalyNumberFormat.formatToThreeDecimal(zero))));

			receipt.setAmountsPerPaymentType(Arrays.asList(new AmountsPerPaymentType(paymetType.name(),
					FiskalyNumberFormat.formatToThreeDecimal(imhaus + ausserHaus + zero))));
		}

		return receipt;

	}
	
	public FiskalyKeyParameter finishReceipt1(FiskalyKeyParameter param, Receipt receipt) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("receipt_type", receipt.getReceiptType());
		jsonObj.put("amounts_per_vat_rate", receipt.getAmountsPerVatRate());
		jsonObj.put("amounts_per_payment_type", receipt.getAmountsPerPaymentType());

		System.out.println("A. Transaction Id Recept: " + param.getTransactionIdReceipt());
		System.out.println("Transaction Id: " + param.getTransactionId());
		System.out.println(jsonObj.toString());
		try {
			String response = service.finishReceipt(param, jsonObj);
			System.out.println(response.toString());
			param = getParamsFromResponse(response, param, "finishOrder");
		} catch (IOException e) {
			System.out.println("Method: finishReceipt  " + "Message: " + e.getMessage());
		}

		return param;
	}
	
	public FiskalyKeyParameter finishOrder(FiskalyKeyParameter param, List<TicketItem> itemList) {
		try {
			String response = service.finishOrder(param, createLineItems(itemList));
			param = getParamsFromResponse(response, param, "finishOrder");
		} catch (IOException e) {
			System.out.println("Method: finishOrder  " + "Message: " + e.getMessage());
		}

		return param;
	}
	
	public TSEReceiptData finishReceipt(FiskalyKeyParameter param, Receipt receipt) {
		TSEReceiptData data = new TSEReceiptData();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("receipt_type", receipt.getReceiptType());
		jsonObj.put("amounts_per_vat_rate", receipt.getAmountsPerVatRate());
		jsonObj.put("amounts_per_payment_type", receipt.getAmountsPerPaymentType());

		try {
			String response = service.finishReceipt(param, jsonObj);
			data = getReceiptDateFromResponse(response, data);
		} catch (IOException e) {
			System.out.println("Method: finishReceipt  " + "Message: " + e.getMessage());
		}
		
		System.out.println("dataFinishReceipt: "+data);

		return data;
	}
	
	public synchronized com.floreantpos.model.TSEReceiptData cancelReceipt(FiskalyKeyParameter param, Receipt receipt) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("receipt_type", receipt.getReceiptType());
		jsonObj.put("amounts_per_vat_rate", receipt.getAmountsPerVatRate());
		TSEReceiptData data = new TSEReceiptData();
		try {
			String response = service.cancelReceipt(param, jsonObj);
			System.out.println("Storno " + response.toString());
			param = getParamsFromResponse(response, param, "finishOrder");

			data = getReceiptDateFromResponse(response, data);

		} catch (IOException e) {
			System.out.println("Method: finishReceipt  " + "Message: " + e.getMessage());
		}

		return data;

	}
	
	private com.floreantpos.model.TSEReceiptData getReceiptDateFromResponse(String response,
			TSEReceiptData tseRData) throws JsonProcessingException, IOException {
		
			JSONObject body = new JSONObject(response);
			
			if (body.has("number")) {				
				tseRData.setTransaction(body.getInt("number"));				
			}

			if (body.has("time_start")) {
				tseRData.setStart(new java.util.Date(body.getLong("time_start") * 1000));
			}

			if (body.has("time_end")) {
				 
				tseRData.setFinish(new java.util.Date(body.getLong("time_end") * 1000));
				System.out.println("timeEnd: "+ tseRData.getFinish());
			}

			if (body.has("tss_serial_number")) {				
				tseRData.setSerialNumber(body.getString("tss_serial_number"));				
			}
			
			if (body.has("signature") && body.getJSONObject("signature").has("value")) {				
				tseRData.setSignature(body.getJSONObject("signature").getString("value"));
			}

			if (body.has("log") &&  body.getJSONObject("log").has("timestamp_format"))
				tseRData.setTimeFormat( body.getJSONObject("log").getString("timestamp_format"));

			if (body.has("signature") && body.getJSONObject("signature").has("algorithm"))
				tseRData.setSignatureAlgorithm(body.getJSONObject("signature").getString("algorithm"));

			if (body.has("qr_code_data"))
				tseRData.setQRCode(body.getString("qr_code_data"));

			if (body.has("signature") && body.getJSONObject("signature").has("public_key"))
				tseRData.setSignaturePublicKey(body.getJSONObject("signature").getString("public_key"));

			if (body.has("signature") && body.getJSONObject("signature").has("counter")){
				tseRData.setSignatureCount(body.getJSONObject("signature").getInt("counter"));
			}

			if (body.has("client_id"))
				tseRData.setClientID(body.getString("client_id"));

			if (body.has("latest_revision"))
				tseRData.setLatestRevision(body.getInt("latest_revision")+"");
			
			return tseRData;
			
	}

	private List<LineItem> createLineItems(List<TicketItem> itemList) {
		List<LineItem> LineItems = new ArrayList<LineItem>();
		for (TicketItem item : itemList) {
			LineItems.add(new LineItem(String.valueOf(item.getItemCount()), item.getName(),
					FiskalyNumberFormat.formatToTwoDecimal(item.getItemCount() * item.getUnitPrice())));
			List<TicketItemModifierGroup> ticketItemModifierGroups = item.getTicketItemModifierGroups();
			if (ticketItemModifierGroups != null) {
				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
					if (ticketItemModifiers != null) {
						for (TicketItemModifier itemModifier : ticketItemModifiers) {
							if (itemModifier != null) {
								LineItems.add(new LineItem(String.valueOf(itemModifier.getItemCount()),
										"TOPPING - " + itemModifier.getName(),
										FiskalyNumberFormat.formatToTwoDecimal(itemModifier.getTotalAmount())));
							}
						}
					}
				}
			}
		}
		return LineItems;
	}

}
