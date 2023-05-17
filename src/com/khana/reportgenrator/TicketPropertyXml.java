package com.khana.reportgenrator;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.Ticket;

@XmlRootElement(name = "bestellungs")
public class TicketPropertyXml {
	Ticket myticket;

	public Ticket getMyticket() {
		return myticket;
	}

	public void setMyticket(Ticket myticket) {
		this.myticket = myticket;
	}

}
