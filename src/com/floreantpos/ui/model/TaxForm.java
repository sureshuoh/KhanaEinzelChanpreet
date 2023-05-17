/*
 * TaxEditor.java
 *
 * Created on August 3, 2006, 1:49 AM
 */

package com.floreantpos.ui.model;

import java.awt.List;

import javax.swing.JComboBox;

import com.floreantpos.POSConstants;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.util.POSUtil;

/**
 *
 * @author  MShahriar
 */
public class TaxForm extends BeanEditor {
    
    /** Creates new form TaxEditor */
    public TaxForm(boolean update) {
        this(new Tax());
        this.update = update;
    }
    private boolean update;
    public TaxForm(Tax tax) {
    	initComponents();
    	 this.update = true;
    	setBean(tax);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbName = new JComboBox();
        cbName.addItem(POSConstants.DINE_IN);
        cbName.addItem(POSConstants.HOME_DELIVERY);
        cbName.addItem("ZERO");
        tfRate = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();

        jLabel1.setText(com.floreantpos.POSConstants.NAME + ":");

        jLabel2.setText(com.floreantpos.POSConstants.RATE + ":");

        tfRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel3.setText("%");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(tfRate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel3))
                    .add(cbName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 208, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(cbName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(tfRate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private JComboBox cbName;
    private javax.swing.JFormattedTextField tfRate;
    // End of variables declaration//GEN-END:variables
	@Override
	public boolean save() {
		
		try {
			if(!updateModel()) return false;
			
			Tax tax = (Tax) getBean();
			TaxDAO dao = new TaxDAO();
			dao.saveOrUpdate(tax);
		} catch (Exception e) {
			MessageDialog.showError(e);
			return false;
		}
		
		return true;
	}

	@Override
	protected void updateView() {
		Tax tax = (Tax) getBean();
		cbName.setSelectedItem(tax.getName());
		tfRate.setValue(Double.valueOf(tax.getRate()));
	}

	@Override
	protected boolean updateModel() {
		Tax tax = (Tax) getBean();
		
		String name = "";
		if (cbName.getSelectedIndex() == -1)
		{
			MessageDialog.showError("Bitte Waehlen Sie einen Name");
			return false;
		}
		name = (String) cbName.getSelectedItem();
    	if(POSUtil.isBlankOrNull(name)) {
    		MessageDialog.showError(com.floreantpos.POSConstants.NAME_REQUIRED);
    		return false;
    	}
    	
    	if(TaxDAO.getInstance().findByName(name)!=null&&!update||TaxDAO.getInstance().findByName(name)!=null&&!tax.getName().isEmpty()&&!name.equals(tax.getName())) {
    		MessageDialog.showError(POSConstants.TAX+" Exists!!");
    		return false;
    	}
		
		tax.setName(name);
		tax.setRate(new Double(tfRate.getValue().toString()).doubleValue());
		
		return true;
	}
    
	public String getDisplayText() {
    	Tax tax = (Tax) getBean();
    	if(tax.getId() == null) {
    		return com.floreantpos.POSConstants.NEW_TAX_RATE;
    	}
    	return com.floreantpos.POSConstants.EDIT_TAX_RATE;
    }
}
