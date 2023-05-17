/*
 * FoodGroupEditor.java
 *
 * Created on August 2, 2006, 8:55 PM
 */

package com.floreantpos.ui.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

/**
 *
 * @author  MShahriar
 */
public class MenuGroupForm extends BeanEditor {  
  
    /** Creates new form FoodGroupEditor */
	public MenuGroupForm() {
		this(new MenuGroup());
	}
	
    public MenuGroupForm(MenuGroup foodGroup) {
        initComponents();
        
        tfName.setDocument(new FixedLengthDocument(20));
        tfDiscount.setDocument(new FixedLengthDocument(20));
        MenuCategoryDAO categoryDAO = new MenuCategoryDAO();
        List<MenuCategory> foodCategories = categoryDAO.findAll();
        cbCategory.setModel(new ComboBoxModel(foodCategories));
        
        setBean(foodGroup);
    }
    protected void doSelectImageFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int option = fileChooser.showOpenDialog(null);
		
		if(option == JFileChooser.APPROVE_OPTION) {
			File imageFile = fileChooser.getSelectedFile();
			try {
				byte[] itemImage = FileUtils.readFileToByteArray(imageFile);
				int imageSize = itemImage.length / 1024;
				
				if(imageSize > 20) {
					POSMessageDialog.showMessage("Das Bild ist zu groß. Wählen Sie ein Bild innerhalb von 20 KB groß");
					itemImage = null;
					return;
				}
				
				MenuGroup menuGroup = (MenuGroup) getBean();
				menuGroup.setImage(itemImage);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
    	jLabel1 = new javax.swing.JLabel();
    	jLabel3 = new javax.swing.JLabel();
    
        tfName = new com.floreantpos.swing.FixedLengthTextField();
        tfDiscount = new com.floreantpos.swing.FixedLengthTextField();
        jLabel2 = new javax.swing.JLabel();
        cbCategory = new javax.swing.JComboBox();
        cbCategory.setBackground(Color.WHITE);
        chkVisible = new javax.swing.JCheckBox();
        chkVisible.setBackground(new Color(209,222,235));
        jLabel1.setText(com.floreantpos.POSConstants.NAME + ":");

        jLabel2.setText(com.floreantpos.POSConstants.CATEGORY + ":");
        
        jLabel3.setText("Type:");
        chkVisible.setText(com.floreantpos.POSConstants.VISIBLE);
        chkVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkVisible.setMargin(new java.awt.Insets(0, 0, 0, 0));

        setLayout(new BorderLayout(0, 0));
        
        setBackground(new Color(209,222,235));
         
        
        if(StringUtils.isNotEmpty(POSConstants.RABATT))
        	jLabelDiscount.setText(POSConstants.RABATT+" %");
        
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(jLabel1)
                    .add(jLabelDiscount))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(tfName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .add(chkVisible)
                    .add(tfDiscount)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(cbCategory, 0, 242, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(tfName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(cbCategory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelDiscount)
                    .add(tfDiscount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkVisible)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void doNewCategory(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doNewCategory
    	try {
			MenuCategoryForm editor = new MenuCategoryForm();
			BeanEditorDialog dialog = new BeanEditorDialog(editor, new Frame(), true);
			dialog.open();
			if (!dialog.isCanceled()) {
				MenuCategory foodCategory = (MenuCategory) editor.getBean();
				ComboBoxModel model = (ComboBoxModel) cbCategory.getModel();
				model.addElement(foodCategory);
				model.setSelectedItem(foodCategory);
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
    }//GEN-LAST:event_doNewCategory
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbCategory;
    private javax.swing.JCheckBox chkVisible;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelDiscount = new JLabel("Rabatt %");
    
    private com.floreantpos.swing.FixedLengthTextField tfName;
    private com.floreantpos.swing.FixedLengthTextField tfDiscount;
    // End of variables declaration//GEN-END:variables
	@Override
	public boolean save() {
		if(!updateModel()) return false;
		
		MenuGroup foodGroup = (MenuGroup) getBean();
		
		try {
			MenuGroupDAO foodGroupDAO = new MenuGroupDAO();
			foodGroupDAO.saveOrUpdate(foodGroup);
		} catch (Exception e) {
			MessageDialog.showError(e);
			return false;
		}
		return true;
	}

	@Override
	protected void updateView() {
		MenuGroup foodGroup = (MenuGroup) getBean();
		if(foodGroup == null) {
			tfName.setText("");
			tfDiscount.setText("");
			cbCategory.setSelectedItem(null);
			chkVisible.setSelected(true);
			return;
		}
		tfName.setText(foodGroup.getName());
		tfDiscount.setText(foodGroup.getDiscount() != null ? NumberUtil.formatNumber(foodGroup.getDiscount()) : NumberUtil.formatNumber(0.00));
		chkVisible.setSelected(true);
		
		if(foodGroup.getParent() != null) {
			cbCategory.setSelectedItem(foodGroup.getParent());
			chkVisible.setSelected(foodGroup.isVisible()!=null?foodGroup.isVisible():true);
		}
	}

	@Override
	protected boolean updateModel() {
		MenuGroup foodGroup = (MenuGroup) getBean();
		if(foodGroup == null) {
			return false;
		}
		
		String name = tfName.getText();
		String discount = tfDiscount.getText().replaceAll(",", ".");
    
    	if(POSUtil.isBlankOrNull(name)) {
    		MessageDialog.showError("Name is required");
    		return false;
    	}
    	
    	if(POSUtil.isBlankOrNull(discount)) {
        MessageDialog.showError("Rabatt is required");
        return false;
     }
    	MenuCategory category = (MenuCategory) cbCategory.getSelectedItem();
    	if(category == null) {
    		MessageDialog.showError("Category is required");
    		return false;
    	}
    	
		foodGroup.setName(tfName.getText());
		foodGroup.setParent(category);
		foodGroup.setDiscount(Double.parseDouble(discount));
		foodGroup.setVisible(chkVisible.isSelected());
		return true;
	}
    
	public String getDisplayText() {
    	MenuGroup foodGroup = (MenuGroup) getBean();
    	if(foodGroup.getId() == null) {
    		return "Neue Produktgruppe";
    	}
    	return "Bearbeitung der Produktgruppe";
    }
}