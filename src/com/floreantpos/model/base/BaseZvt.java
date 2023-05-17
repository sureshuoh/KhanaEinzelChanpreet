package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;


 

public abstract class BaseZvt implements Comparable, Serializable {

	public static String REF = "Zvt";
	public static String PROP_BETRAG = "betrag";
	public static String PROP_KARTENNAME = "kartenname";
	public static String PROP_TRANSACTION_NUMMER = "transactionNummer";
	public static String PROP_BELEG_NUMMER = "belegnummer";
	public static String PROP_ZAHLUNGS_CODE = "zahlungsCode";
	public static String PROP_CARD_TYPE = "cardType";
	public static String PROP_CARD_TYPE_ID = "cardTypeId";
	public static String PROP_TA_NUMMER ="tanummer";
	public static String PROP_CARD_NAME = "cardName";
	public static String PROP_VU_NUMMER = "vunummer";
	public static String PROP_TERMINAL_ID ="terminalId";
	public static String PROP_ADDITIONAL_TEXT ="additionalText";
	public static String PROP_CUSTOMER_RECIEPT ="customerReciept";
	public static String PROP_ID = "id";
	public static String PROP_TRANSACTION_STATUS = "transactionStatus";


	// constructors
	public BaseZvt() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseZvt (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}
 
	protected void initialize () {}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected String betrag;
		protected String kartenname;
		protected String transactionNummer;
		protected java.lang.String belegnummer;
		protected java.lang.String zahlungsCode;
		protected java.lang.String cardType;
		protected java.lang.String tanummer;
		protected java.lang.String vunummer;
		protected java.lang.String cardTypeId;
		protected java.lang.String cardName;
		protected java.lang.Boolean transactionStatus;
		protected java.lang.String terminalId;
		protected java.lang.String additionalText;
		protected java.lang.String customerReciept;
		
	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: AMOUNT
	 */
	public java.lang.String getBetrag () {
		return betrag;
	}
 
	public void setBetrag (String betrag) {
		this.betrag = betrag;
	}
	
	public java.lang.String getTerminalId () {
		return terminalId;
	}
	
	public void setTerminalId (String terminalId) {
	    this.terminalId = terminalId;
	}
	
	public java.lang.String getAdditionalText () {
		return additionalText;
	}
	
	public void setAdditionalText (String additionalText) {
	    this.additionalText = additionalText;
	}
	
	public java.lang.String getCustomerReciept () {
		return customerReciept;
	}
	public void setCustomerReciept(String customerReciept) {
	    this.customerReciept = customerReciept;
	}
	
	/**
	 * Return the value associated with the column: TRANSACTION_TYPE
	 */
	public java.lang.String getKartenname () {
					return kartenname;
			}

	/**
	 * Set the value related to the column: TRANSACTION_TYPE
	 * @param transactionType the TRANSACTION_TYPE value
	 */
	public void setKartenname (java.lang.String kartenname) {
		this.kartenname = kartenname;
	}

	/**
	 * Return the value associated with the column: PAYMENT_SUB_TYPE
	 */
	public java.lang.String getTransactionNummer () {
					return transactionNummer;
	}

	/**
	 * Set the value related to the column: PAYMENT_SUB_TYPE
	 * @param paymentType the PAYMENT_SUB_TYPE value
	 */
	public void setTransactionNummer (java.lang.String transactionNummer) {
		this.transactionNummer = transactionNummer;
	}

	/**
	 * Return the value associated with the column: CAPTURED
	 */
	public java.lang.Boolean isTransactionStatus () {
								return transactionStatus == null ? Boolean.FALSE : transactionStatus;
	}

	/**
	 * Set the value related to the column: CAPTURED
	 * @param captured the CAPTURED value
	 */
	public void setTransactionStatus (java.lang.Boolean transactionStatus) {
		this.transactionStatus = true;
	}

	/**
	 * Return the value associated with the column: CARD_TRACK
	 */
	public java.lang.String getBelegnummer () {
					return belegnummer;
	}

	/**
	 * Set the value related to the column: CARD_TRACK
	 * @param cardTrack the CARD_TRACK value
	 */
	public void setBelegnummer (java.lang.String belegnummer) {
		this.belegnummer = belegnummer;
	}

	/**
	 * Return the value associated with the column: CARD_NUMBER
	 */
	public java.lang.String getZahlungsCode () {
					return zahlungsCode;
	}

	/**
	 * Set the value related to the column: CARD_NUMBER
	 * @param cardNumber the CARD_NUMBER value
	 */
	public void setZahlungsCode (java.lang.String zahlungsCode) {
		this.zahlungsCode = zahlungsCode;
	}

	public java.lang.String getCardType() {
		return cardType;
	}

	public void setCardType(java.lang.String cardType) {
		this.cardType = cardType;
	}
	
	
	public java.lang.String getVunummer() {
		return vunummer;
	}

	public void setVunummer(java.lang.String vunummer) {
		this.vunummer = vunummer;
	}
	
	public java.lang.String getTanummer() {
		return tanummer;
	}

	public void setTanummer(java.lang.String tanummer) {
		this.tanummer = tanummer;
	}

	public java.lang.String getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(java.lang.String cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public java.lang.String getCardName() {
		return cardName;
	}

	public void setCardName(java.lang.String cardName) {
		this.cardName = cardName;
	}

	public java.lang.Boolean getTransactionStatus() {
		return transactionStatus;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Zvt)) return false;
		else {
			com.floreantpos.model.Zvt zvt = (com.floreantpos.model.Zvt) obj;
			if (null == this.getId() || null == zvt.getId()) return false;
			else return (this.getId().equals(zvt.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo (Object obj) {
		if (obj.hashCode() > hashCode()) return 1;
		else if (obj.hashCode() < hashCode()) return -1;
		else return 0;
	}

	public String toString () {
		return super.toString();
	}


}