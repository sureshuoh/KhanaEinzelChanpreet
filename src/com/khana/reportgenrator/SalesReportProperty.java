
package com.khana.reportgenrator;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.Salesreportdb;

@XmlRootElement(name = "reports")
public class SalesReportProperty {
	Salesreportdb myreport;

	public Salesreportdb getMyreport() {
		return myreport;
	}

	public void setMyreport(Salesreportdb myreport) {
		this.myreport = myreport;
	}

}
