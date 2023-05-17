
package com.floreantpos.model.dao;

import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.TSEReceiptData;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifierGroup;

import antlr.collections.List;

public class TseDataDAO  extends BaseTSEReceiptDataDAO {
 
	public TseDataDAO () {}
	
	public  TSEReceiptData findById()
	{
		Session session = null;

		try {
			String lTseNr="5";
			if(TerminalConfig.getLastTseNr()!=null&&Integer.valueOf(TerminalConfig.getLastTseNr())>0) {
				 lTseNr=TerminalConfig.getLastTseNr();
			} 
			
		    int highNr =Integer.valueOf(lTseNr);
			
			Random r = new Random();
			int low = 0;
			int high = highNr;
			int result = r.nextInt(high-low) + low;
		   
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.addOrder( Order.desc("id") );	
			criteria.setFirstResult(result);
			criteria.setMaxResults(1);			
			return (TSEReceiptData) criteria.uniqueResult();
			
		/*	session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());			
			 criteria.addOrder( Order.desc("id") );					
			 criteria.setMaxResults(1);			
			return (TSEReceiptData) criteria.uniqueResult();*/
						
		} finally {
			closeSession(session);
		}
	}
	
	public  TSEReceiptData findById(int id)
	{
		Session session = null;

		try {
		  
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(TSEReceiptData.PROP_ID, id));	
			 
			criteria.setMaxResults(1);			
			return (TSEReceiptData) criteria.uniqueResult();
			
			/*session = getSession();
		  
			TSEReceiptData ticketRcptData = (TSEReceiptData) session.get(getReferenceClass(), Integer.parseInt(id));			
			if(ticketRcptData == null) return null;
			
			return ticketRcptData;
			*/
						
		} finally {
			closeSession(session);
		}
	}
}