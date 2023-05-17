package com.khana.tse.fiskaly.transaction;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sound.midi.Patch;

import java.io.IOException;

import org.apache.ecs.xhtml.param;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Service;

import com.fiskaly.sdk.FiskalyClientException;
import com.fiskaly.sdk.FiskalyHttpClient;
import com.fiskaly.sdk.FiskalyHttpException;
import com.fiskaly.sdk.FiskalyHttpResponse;
import com.fiskaly.sdk.FiskalyHttpTimeoutException;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.RestaurantDAO;
 
import com.khana.tse.fiskaly.AmountsPerPaymentType;
import com.khana.tse.fiskaly.AmountsPerVatRate;
import com.khana.tse.fiskaly.FiskalyKeyParameter;
import com.khana.tse.fiskaly.FiskalyNumberFormat;
import com.khana.tse.fiskaly.LineItem;
import com.sleepycat.je.utilint.Timestamp;

import net.authorize.util.HttpClient;

/**
 * @author Jyoti Rai
 *
 */

@Service
public class FiskalyRegularTransactionServiceV2 {

	//private static final String _BASEURL = "https://kassensichv.io/api/v1";

	private static final String _BASEURL = "https://kassensichv-middleware.fiskaly.com/api/v2";
	
	private static String TX_ID_RECEIPT;
    private static String API_KEY;
	private static String API_SECRET;
	private static String TOKEN="";
	private static String CLIENT_ID;
	private static Integer LATEST_REVISION_NR;
	private static Integer LATEST_RECIEPT_REVISION_NR;

	public FiskalyRegularTransactionServiceV2() {
		if (TerminalConfig.isBuildMode()) {
			this.API_KEY = "test_cb6zppxj72p32jv7uywgppvxk_khana-fiskaly-api";
			this.API_SECRET = "1xtG1z1VHQRa7XBElRH41w6e0gv3ITvYkE2RqIRW4Me";
			System.out.println("fiscallyDemo API_KEY: " + API_KEY + ", API_SECRET: " + API_SECRET );
		} else if (RestaurantDAO.getRestaurant().isTseLive()) {
			this.API_KEY = "live_cb6zppxj72p32jv7uywgppvxk_khana-kassensystem";
			this.API_SECRET = "UAD8FTUMSvtTdfLhzLNuYVvsngk6BZnnzNMJLmQg5Mn";
			System.out.println("fiscallyLive API_KEY: " + API_KEY + ", API_SECRET: " + API_SECRET );
		} else {
			this.API_KEY = "test_cb6zppxj72p32jv7uywgppvxk_khana-fiskaly-api";
			this.API_SECRET = "1xtG1z1VHQRa7XBElRH41w6e0gv3ITvYkE2RqIRW4Me";
			System.out.println("fiscallyDemo API_KEY: " + API_KEY + ", API_SECRET: " + API_SECRET );
		}
		
		CLIENT_ID=TerminalConfig.getTseClientId();
	}

	private UUIDGenerator UUID = new UUIDGenerator();
 
	public synchronized String login() throws ClientProtocolException, IOException {
		 HttpResponse response;
		 
		 JSONObject jsonObj = new JSONObject();
		 jsonObj.put("api_key", API_KEY);
		 jsonObj.put("api_secret", API_SECRET);
		 // Create the POST object and add the parameters
		 HttpPost httpPost = new HttpPost(_BASEURL+"/auth");
		 StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		 entity.setContentType("application/json");
		 httpPost.setEntity(entity);
		 DefaultHttpClient client = new DefaultHttpClient();
		  response = client.execute(httpPost);
		  
		  if (response.getStatusLine().getStatusCode() == 200) {			  
              System.out.println("successfully Login: "+response.getStatusLine().getStatusCode());
           } else {
           	System.out.println("error in tss login: "+response.getStatusLine().getStatusCode());
           }
		  String responseString = new BasicResponseHandler().handleResponse(response);
		  JSONObject jsonObj2 = new JSONObject(responseString);
		  TOKEN = jsonObj2.getString("access_token");
		return jsonObj2.getString("access_token");
	}
	
	public synchronized static String login_() throws ClientProtocolException, IOException {
		 HttpResponse response;
		 
		 JSONObject jsonObj = new JSONObject();
		 jsonObj.put("api_key", API_KEY);
		 jsonObj.put("api_secret", API_SECRET);
		 // Create the POST object and add the parameters
		 HttpPost httpPost = new HttpPost(_BASEURL+"/auth");
		 StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		 entity.setContentType("application/json");
		 httpPost.setEntity(entity);
		 DefaultHttpClient client = new DefaultHttpClient();
		  response = client.execute(httpPost);
		  
		  if (response.getStatusLine().getStatusCode() == 200) {
			  
             System.out.println("successfully Login: "+response.getStatusLine().getStatusCode());
          } else {
          	System.out.println("error in tss login: "+response.getStatusLine().getStatusCode());
          }
		  String responseString = new BasicResponseHandler().handleResponse(response);
		  JSONObject jsonObj2 = new JSONObject(responseString);
		  TOKEN = jsonObj2.getString("access_token");
		return jsonObj2.getString("access_token");
	}
	 
	public synchronized String createTSS() throws IOException {
		 HttpResponse response;
	        DefaultHttpClient httpclient =  new DefaultHttpClient();
	            UUID TssID = UUID.generateUUID();
	        
	            HttpPut httpPut = new HttpPut(_BASEURL+"/tss/" + TssID);
	            httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
	            httpPut.setHeader("Authorization", "Bearer " + TOKEN);
	            
	            JSONObject requestBody = new JSONObject();
	            JSONObject main = new JSONObject();
	            main.put("description", "TSE_" + Application.getInstance().getRestaurant().getName() + "_"
	    				+ Application.getInstance().getRestaurant().getZipCode());
	            main.put("address",  Application.getInstance().getRestaurant().getAddressLine1());
	            main.put("address2",  Application.getInstance().getRestaurant().getAddressLine2());
	            main.put("address3",  Application.getInstance().getRestaurant().getAddressLine3());
	            main.put("phone",  Application.getInstance().getRestaurant().getTelephone());
	            main.put("phone2",  Application.getInstance().getRestaurant().getSecondaryTelephone());
	            requestBody.put("metadata",main);
	            String json = requestBody.toString();
	            
	            StringEntity stringEntity = new StringEntity(json);
	            httpPut.setEntity(stringEntity); 
	            response = httpclient.execute(httpPut);	            
	            String responseString = new BasicResponseHandler().handleResponse(response);
	  		    
	            if (response.getStatusLine().getStatusCode() == 200) {
	               System.out.println("successfully cretaed tss: "+response.getStatusLine().getStatusCode());
	            } else {
	            	System.out.println("error in tss: "+response.getStatusLine().getStatusCode());
	            }

	        return responseString;
	 }
	
	public synchronized String authIntialization() throws ClientProtocolException, IOException {
         HttpResponse response;
         JSONObject jsonObj = new JSONObject();
		 jsonObj.put("admin_pin", "JAN291993");
		 
		 HttpPost httpPost = new HttpPost(_BASEURL+"/tss/"+ Application.getInstance().getRestaurant().getTseId() +"/admin/auth");
		 httpPost.setHeader("Content-type", "application/json");
		 httpPost.setHeader("Authorization", "Bearer " + TOKEN);
		 StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		
		 entity.setContentType("application/json");
		 httpPost.setEntity(entity);
		 DefaultHttpClient client = new DefaultHttpClient();		 
		 response = client.execute(httpPost);
		 
		  if (response.getStatusLine().getStatusCode() == 200) {
              System.out.println("successful Authorization: "+response.getStatusLine().getStatusCode());              
           } else {
              System.out.println("error in Authorization: "+response.getStatusLine().getStatusCode());
           }
		  
		  String responseString = new BasicResponseHandler().handleResponse(response);		 
		   changeState("INITIALIZED");
		  
		 return responseString;
	}

	/*public synchronized String authIntialization() throws ClientProtocolException, IOException {
        HttpResponse response;
        JSONObject jsonObj = new JSONObject();
		 jsonObj.put("admin_pin", Application.getInstance().getRestaurant().getAdminPin());
		 
		 HttpPost httpPost = new HttpPost(_BASEURL+"/tss/"+ Application.getInstance().getRestaurant().getTseId() +"/admin/auth");
		 httpPost.setHeader("Content-type", "application/json");
		 httpPost.setHeader("Authorization", "Bearer " + TOKEN);
		 StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		
		 entity.setContentType("application/json");
		 httpPost.setEntity(entity);
		 DefaultHttpClient client = new DefaultHttpClient();		 
		 response = client.execute(httpPost);
		 
		  if (response.getStatusLine().getStatusCode() == 200) {
             System.out.println("successful Authorization: "+response.getStatusLine().getStatusCode());              
          } else {
             System.out.println("error in Authorization: "+response.getStatusLine().getStatusCode());
          }
		  
		  String responseString = new BasicResponseHandler().handleResponse(response);		 
		    changeState("INITIALIZED"); 
		  
		 return responseString;
	}*/
	
	public synchronized boolean changeState(String state) throws IOException {
		 HttpResponse response=null;
		 boolean stateChanged=false;
		 DefaultHttpClient httpclient_ =  new DefaultHttpClient();
		 
		 HttpPatch httpPatch_ = new HttpPatch(_BASEURL+"/tss/" + Application.getInstance().getRestaurant().getTseId());
		 httpPatch_.setHeader("Content-type", "application/json");
		 httpPatch_.setHeader("Authorization", "Bearer " + TOKEN);
	     
	     JSONObject requestBody_ = new JSONObject();
	     requestBody_.put("state", state);
		 
   		String json_ = requestBody_.toString();
   		StringEntity stringEntity_ = new StringEntity(json_);			 
   		httpPatch_.setEntity(stringEntity_);
			response = httpclient_.execute(httpPatch_);
			  
           if (response.getStatusLine().getStatusCode() == 200) {
           	System.out.println("state set "+state);
           	stateChanged=true;
           } else {
           	System.out.println("state is not set "+ state +" "+response.getStatusLine().getStatusCode());
           }
           
           return stateChanged;
	}
	
	public synchronized String changeAdminPin() throws IOException {
		 HttpResponse response=null;
		 
		 changeState("UNINITIALIZED");
		 
		 DefaultHttpClient httpclient =  new DefaultHttpClient();
		 HttpPatch httpPatch = new HttpPatch(_BASEURL+"/tss/" + Application.getInstance().getRestaurant().getTseId()+"/admin");
		     httpPatch.setHeader("Content-Type", "application/json");
		     httpPatch.setHeader("Content-type", "application/json");
		     httpPatch.setHeader("Authorization", "Bearer " + TOKEN);
		     
		     JSONObject requestBody = new JSONObject();
	    		requestBody.put("admin_puk", Application.getInstance().getRestaurant().getAdminPuk());
	    		requestBody.put("new_admin_pin", "JAN291993");
		     
                String json = requestBody.toString();
	            StringEntity stringEntity = new StringEntity(json);
				httpPatch.setEntity(stringEntity);	              
				response = httpclient.execute(httpPatch);				 
	            String responseString = new BasicResponseHandler().handleResponse(response);
				 
	            if (response.getStatusLine().getStatusCode() == 200) {
	               System.out.println("successfully changed admin pin "+response.getStatusLine().getStatusCode());
	            } else {
	            	System.out.println("error in changedAdminPin: "+response.getStatusLine().getStatusCode());
	            }

	        return requestBody.toString();
	}
	
	@NotThreadSafe
	public class HttpPatch extends HttpEntityEnclosingRequestBase {

	    public final static String METHOD_NAME = "PATCH";

	    public HttpPatch() {
	        super();
	    }

	    public HttpPatch(final URI uri) {
	        super();
	        setURI(uri);
	    }

	    public HttpPatch(final String uri) {
	        super();
	        setURI(URI.create(uri));
	    }

	    @Override
	    public String getMethod() {
	        return METHOD_NAME;
	    }

	}
	
	 public String getTSSInfo() throws IOException {
		 
		 HttpResponse response;
	        DefaultHttpClient httpclient =  new DefaultHttpClient();
	            
	            HttpGet httpGet = new HttpGet(_BASEURL+"/tss/" + Application.getInstance().getRestaurant().getTseId());
	            httpGet.setHeader("Accept", "application/json");
	            httpGet.setHeader("Content-type", "application/json");
	            httpGet.setHeader("Authorization", "Bearer " + TOKEN);
	            
	            response = httpclient.execute(httpGet);	            
	            String responseString = new BasicResponseHandler().handleResponse(response);
	  		    
	            if (response.getStatusLine().getStatusCode() == 200) {
	               System.out.println("successfully get TSSInfo: "+response.getStatusLine().getStatusCode());
	            } else {
	            	System.out.println("error in TSSInfo: "+response.getStatusLine().getStatusCode());
	            }

	        return responseString;		 
     }
	 
	 public synchronized String createClientID(String TSS_ID) throws IOException  {
		 
		 HttpResponse response;
	        DefaultHttpClient httpclient =  new DefaultHttpClient();
	       
	        JSONObject requestBody = new JSONObject();
            requestBody.put("serial_number", UUID.generateUUID());
	        
	        UUID ClienId;
			if (Application.getCurrentUser().getFirstName().compareTo("Super-User") == 0)
				ClienId = UUID.generateUUID();
			else
				ClienId = UUID.getClientUniqueID();
    		
	            HttpPut httpPut = new HttpPut(_BASEURL+"/tss/" + TSS_ID + "/client/" + ClienId);
	            httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
	            httpPut.setHeader("Authorization", "Bearer " + TOKEN);
	            
	            String json = requestBody.toString();	            
	            StringEntity stringEntity = new StringEntity(json);
	            httpPut.setEntity(stringEntity);
	            response = httpclient.execute(httpPut);	            
	            String responseString = new BasicResponseHandler().handleResponse(response);	  		    
	            if (response.getStatusLine().getStatusCode() == 200) {
	               System.out.println("successfully cretaed Client: "+response.getStatusLine().getStatusCode());
	            } else {
	            	System.out.println("error in tssClientId: "+response.getStatusLine().getStatusCode());
	            }

	        return responseString;		 
    }
	 
	 public synchronized String startReceipt(FiskalyKeyParameter param) throws IOException {
		 HttpResponse response;
		 TOKEN = login();
	        DefaultHttpClient httpclient =  new DefaultHttpClient();
	        this.TX_ID_RECEIPT = UUID.generateUUID().toString();
	        LATEST_RECIEPT_REVISION_NR = param.getLatestRevisionReceipt();
	        if(LATEST_RECIEPT_REVISION_NR==1||LATEST_RECIEPT_REVISION_NR==0) {
	        	LATEST_RECIEPT_REVISION_NR=1;
	        } else {
	           LATEST_RECIEPT_REVISION_NR=param.getLatestRevisionReceipt()+1;
	        }
	            HttpPut httpPut = new HttpPut(_BASEURL+"/tss/" + param.getTssId() + "/tx/" + TX_ID_RECEIPT+"?tx_revision="+LATEST_RECIEPT_REVISION_NR);
	           
	            httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
	            httpPut.setHeader("Authorization", "Bearer " + TOKEN);
	            
	            JSONObject requestBody = new JSONObject();
	    		requestBody.put("state", FiskalyState.ACTIVE);
	    		requestBody.put("client_id", CLIENT_ID);
	    		 
	            String json = requestBody.toString();	            
	            StringEntity stringEntity = new StringEntity(json);
	            httpPut.setEntity(stringEntity);
	            response = httpclient.execute(httpPut);	            
	            String responseString = new BasicResponseHandler().handleResponse(response);	  		    
	            if (response.getStatusLine().getStatusCode() == 200) {
	               System.out.println("successfully startReceipt: "+response.getStatusLine().getStatusCode());
	            } else if (response.getStatusLine().getStatusCode() == 401) {
		               TOKEN = login();
		               startReceipt(param);
		            } else {
	            	 
	            	System.out.println("error in startReceipt: "+response.getStatusLine().getStatusCode());
	            }

	        return responseString;
	 }
	 
	 public synchronized String reStartReceipt(FiskalyKeyParameter param) throws IOException {
		 HttpResponse response;		 
		 
	        DefaultHttpClient httpclient =  new DefaultHttpClient();
	        LATEST_REVISION_NR =param.getLatestRevision();
	        if(LATEST_REVISION_NR==0) {
	        	LATEST_REVISION_NR =1;
	        } else {
	        	LATEST_REVISION_NR = LATEST_REVISION_NR+1; 
	        }
	            HttpPut httpPut = new HttpPut(_BASEURL+"/tss/" + param.getTssId() + "/tx/" + TX_ID_RECEIPT+"?tx_revision="+LATEST_REVISION_NR);
	            httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
	            httpPut.setHeader("Authorization", "Bearer " + TOKEN);
	            
	            JSONObject requestBody = new JSONObject();
	    		requestBody.put("state", FiskalyState.ACTIVE);
	    		requestBody.put("client_id", CLIENT_ID);
	    		
	            String json = requestBody.toString();	            
	            StringEntity stringEntity = new StringEntity(json);
	            httpPut.setEntity(stringEntity); 
	            response = httpclient.execute(httpPut);	            
	            String responseString = new BasicResponseHandler().handleResponse(response);	  		    
	            if (response.getStatusLine().getStatusCode() == 200) {
	               System.out.println("successfully StartReceipt_: "+response.getStatusLine().getStatusCode());
	            } else if (response.getStatusLine().getStatusCode() == 401) {
		               TOKEN = login();
		               reStartReceipt(param);
		            } else {
	            	System.out.println("error in StartReceipt_: "+response.getStatusLine().getStatusCode());
	            }

	        return responseString;
	 }
	 
	 
	 public synchronized String startOrder(FiskalyKeyParameter param) throws IOException {
		 HttpResponse response;
		 TOKEN = login();
	        DefaultHttpClient httpclient =  new DefaultHttpClient();
	        LATEST_REVISION_NR =param.getLatestRevision();
	        if(LATEST_REVISION_NR==0) {
	        	LATEST_REVISION_NR =1;
	        } else {
	        	LATEST_REVISION_NR = LATEST_REVISION_NR+1; 
	        }
	        
	            HttpPut httpPut = new HttpPut(_BASEURL+"/tss/" + param.getTssId() + "/tx/" + UUID.generateUUID()+"?tx_revision="+LATEST_REVISION_NR);	            
	            httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
	            httpPut.setHeader("Authorization", "Bearer " + TOKEN);
	            
	            JSONObject requestBody = new JSONObject();
	    		requestBody.put("state", FiskalyState.ACTIVE);
	    		requestBody.put("client_id", param.getClientId());
	    		
	            String json = requestBody.toString();	            
	            StringEntity stringEntity = new StringEntity(json);
	            httpPut.setEntity(stringEntity);
	            response = httpclient.execute(httpPut);	            
	            String responseString = new BasicResponseHandler().handleResponse(response);	  		    
	            if (response.getStatusLine().getStatusCode() == 200) {
	               System.out.println("successfully startOrder: "+response.getStatusLine().getStatusCode());
	            } else if (response.getStatusLine().getStatusCode() == 401) {
		               TOKEN = login();
		               startOrder(param);
		            } else {
	            	System.out.println("error in startOrder: "+response.getStatusLine().getStatusCode());
	            }

	        return responseString;
	 }
	 
	 
	 public synchronized String updateOrder(FiskalyKeyParameter param, List<LineItem> lineItems) throws IOException {
		 HttpResponse response;
		 
	        DefaultHttpClient httpclient =  new DefaultHttpClient();
	        
		        LATEST_REVISION_NR =param.getLatestRevision();	        
		        LATEST_REVISION_NR = LATEST_REVISION_NR+1; 
		       
	            HttpPut httpPut = new HttpPut(_BASEURL+"/tss/" + param.getTssId() + "/tx/" + param.getTransactionId()+"?tx_revision="+LATEST_REVISION_NR);
	            httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
	            httpPut.setHeader("Authorization", "Bearer " + param.getToken());
	            
	            JSONObject requestBody = new JSONObject();
	    		requestBody.put("state", FiskalyState.ACTIVE);
	    		requestBody.put("client_id", param.getClientId());

	    		JSONObject arrayElementOne = new JSONObject();
	    		JSONObject order = new JSONObject();
	    		order.put("line_items", lineItems);

	    		arrayElementOne.put("order", order);
	    		JSONObject standardV1 = new JSONObject();
	    		standardV1.put("standard_v1", arrayElementOne);
	    		requestBody.put("schema", standardV1);

	    		/*Map<String, Integer> requestParams = new HashMap<String, Integer>();
	    		requestParams.put("last_revision", param.getLatestRevision());*/
	    		
	            String json = requestBody.toString();	            
	            StringEntity stringEntity = new StringEntity(json);
	            httpPut.setEntity(stringEntity);
	            response = httpclient.execute(httpPut);	            
	            String responseString = new BasicResponseHandler().handleResponse(response);
	  		    
	            if (response.getStatusLine().getStatusCode() == 200) {
	               System.out.println("successfully UpdateOrder: "+response.getStatusLine().getStatusCode());
	            } else if (response.getStatusLine().getStatusCode() == 401) {
		               TOKEN = login();
		               updateOrder(param,lineItems);
		        } else {
	            	System.out.println("error in UpdateOrder: "+response.getStatusLine().getStatusCode());
	            }

	        return responseString;
	 }
	 
	 public synchronized String finishReceipt(FiskalyKeyParameter param, JSONObject receipt) throws IOException {
		 HttpResponse response;
		   
	        DefaultHttpClient httpclient =  new DefaultHttpClient();
	        LATEST_RECIEPT_REVISION_NR = param.getLatestRevisionReceipt();
	        if(LATEST_RECIEPT_REVISION_NR==null||LATEST_RECIEPT_REVISION_NR==0) {
	        	LATEST_RECIEPT_REVISION_NR=1;
	        } else {
	           LATEST_RECIEPT_REVISION_NR=param.getLatestRevisionReceipt()+1;
	        }
	        
	            HttpPut httpPut = new HttpPut(_BASEURL+"/tss/" + param.getTssId() + "/tx/" + param.getTransactionIdReceipt()+"?tx_revision="+LATEST_RECIEPT_REVISION_NR);
	            httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
	            httpPut.setHeader("Authorization", "Bearer " + TOKEN);
	            
	            JSONObject requestBody = new JSONObject();
	    		requestBody.put("state", FiskalyState.FINISHED);
	    		requestBody.put("client_id", CLIENT_ID);

	    		JSONObject arrayElementOne = new JSONObject();
	    		arrayElementOne.put("receipt", receipt);
	    		JSONObject standardV1 = new JSONObject();
	    		standardV1.put("standard_v1", arrayElementOne);
	    		requestBody.put("schema", standardV1);

	    		/*Map<String, Integer> requestParams = new HashMap<String, Integer>();
	    		requestParams.put("last_revision", param.getLatestRevisionReceipt());*/
	    		
	            String json = requestBody.toString();	            
	            StringEntity stringEntity = new StringEntity(json);
	            httpPut.setEntity(stringEntity);
	            response = httpclient.execute(httpPut);	            
	            String responseString = new BasicResponseHandler().handleResponse(response);	  		    
	            if (response.getStatusLine().getStatusCode() == 200) {
	               System.out.println("finishReceipt: "+response.getStatusLine().getStatusCode());
	            } else if (response.getStatusLine().getStatusCode() == 401) {
		               TOKEN = login();
		               finishReceipt(param,receipt);
		        } else {
	            	System.out.println("error in finishReceipt: "+response.getStatusLine().getStatusCode());
	            }

	        return responseString;
	 }
	  
	 public synchronized String cancelReceipt(FiskalyKeyParameter param, JSONObject receipt) throws IOException {
		 HttpResponse response;
		  
	        DefaultHttpClient httpclient =  new DefaultHttpClient();	        
	            LATEST_REVISION_NR = LATEST_REVISION_NR+1;
	        
	            HttpPut httpPut = new HttpPut(_BASEURL+"/tss/" + param.getTssId() + "/tx/" + param.getTransactionIdReceipt()+"?tx_revision="+LATEST_REVISION_NR);
	             
	            httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
	            httpPut.setHeader("Authorization", "Bearer " + TOKEN);
	            
	            JSONObject requestBody = new JSONObject();
	    		requestBody.put("state", FiskalyState.CANCELLED);
	    		requestBody.put("client_id", CLIENT_ID);

	    		JSONObject arrayElementOne = new JSONObject();
	    		arrayElementOne.put("receipt", receipt);
	    		JSONObject standardV1 = new JSONObject();
	    		standardV1.put("standard_v1", arrayElementOne);
	    		requestBody.put("schema", standardV1);

	    		/*Map<String, Integer> requestParams = new HashMap<String, Integer>();
	    		requestParams.put("last_revision", param.getLatestRevisionReceipt());*/
	    		
	            String json = requestBody.toString();	            
	            StringEntity stringEntity = new StringEntity(json);
	            httpPut.setEntity(stringEntity); 
	            response = httpclient.execute(httpPut);	            
	            String responseString = new BasicResponseHandler().handleResponse(response);	  		    
	            if (response.getStatusLine().getStatusCode() == 200) {
	               System.out.println("cancelReceipt: "+response.getStatusLine().getStatusCode());
	            } else if (response.getStatusLine().getStatusCode() == 401) {
		               TOKEN = login();
		               cancelReceipt(param,receipt);
		        } else {
	            	System.out.println("error in cancelReceipt: "+response.getStatusLine().getStatusCode());
	            }

	        return responseString;
	 }
	 
	 public synchronized String finishOrder(FiskalyKeyParameter param, List<LineItem> lineItems) throws IOException {
		 HttpResponse response;
		 
	        DefaultHttpClient httpclient =  new DefaultHttpClient();
	        Integer latestRivison = param.getLatestRevision()+1;
	        
	            HttpPut httpPut = new HttpPut(_BASEURL+"/tss/" + param.getTssId() + "/tx/" + param.getTransactionId()+"?tx_revision="+latestRivison);	             
	            httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
	            httpPut.setHeader("Authorization", "Bearer " + TOKEN);
	            
	            JSONObject requestBody = new JSONObject();
	    		requestBody.put("state", FiskalyState.FINISHED);
	    		requestBody.put("client_id", CLIENT_ID);

	    		JSONObject arrayElementOne = new JSONObject();
	    		JSONObject order = new JSONObject();
	    		order.put("line_items", lineItems);
	    		arrayElementOne.put("order", order);
	    		JSONObject standardV1 = new JSONObject();
	    		standardV1.put("standard_v1", arrayElementOne);
	    		requestBody.put("schema", standardV1);

	    		/*Map<String, Integer> requestParams = new HashMap<String, Integer>();
	    		requestParams.put("last_revision", param.getLatestRevision());*/
	    		
	            String json = requestBody.toString();	            
	            StringEntity stringEntity = new StringEntity(json);
	            httpPut.setEntity(stringEntity);
	            response = httpclient.execute(httpPut);	            
	            String responseString = new BasicResponseHandler().handleResponse(response);
	  		    
	            if (response.getStatusLine().getStatusCode() == 200) {
	            	
	              
	            } else if (response.getStatusLine().getStatusCode() == 401) {
		               TOKEN = login();
		               finishOrder(param,lineItems);
		        } else {
	            	System.out.println("error in finishOrder: "+response.getStatusLine().getStatusCode());
	            }

	        return responseString;
	 }
	 
   /*public synchronized ClientHttpRequest  createTSS()  {
		JSONObject requestBody = new JSONObject();
		requestBody.put("description", "TSE_" + Application.getInstance().getRestaurant().getName() + "_"
				+ Application.getInstance().getRestaurant().getZipCode());
		requestBody.put("state", "INITIALIZED");
		UUID TssID = UUID.generateUUID();
		
		HttpMethod method = HttpMethod.PUT;
		
		ClientHttpRequest req = createRequest(_BASEURL,method);
		
		final FiskalyHttpClient client = new FiskalyHttpClient(API_KEY, API_SECRET, _BASEURL);
		final FiskalyHttpResponse response = client.request("PUT", "/tss/" + TssID, requestBody.toString().getBytes());
		return response;
	}*/ 

}
