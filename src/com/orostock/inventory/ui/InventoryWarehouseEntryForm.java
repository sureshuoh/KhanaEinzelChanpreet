package com.orostock.inventory.ui;

import com.floreantpos.PosException;
import com.floreantpos.model.inventory.InventoryWarehouse;
import com.floreantpos.model.inventory.dao.InventoryWarehouseDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;

public class InventoryWarehouseEntryForm
  extends BeanEditor<InventoryWarehouse>
{
  private JCheckBox chkVisible;
  private POSTextField tfName;
  
  public InventoryWarehouseEntryForm(InventoryWarehouse ig)
  {
    super(new BorderLayout());
    
    createUI();
    
    setBean(ig);
  }
  
  private void createUI()
  {
    JPanel panel = new JPanel();
    add(panel, "Center");
    panel.setLayout(new MigLayout("", "[][grow]", "[][]"));
    
    JLabel lblName = new JLabel("Name");
    panel.add(lblName, "cell 0 0,alignx trailing");
    
    this.tfName = new POSTextField();
    panel.add(this.tfName, "cell 1 0,growx");
    
    this.chkVisible = new JCheckBox("Visible");
    panel.add(this.chkVisible, "cell 1 1");
  }
  
  public void updateView()
  {
    InventoryWarehouse inventoryWarehouse = (InventoryWarehouse)getBean();
    if (inventoryWarehouse == null) {
      return;
    }
    this.tfName.setText(inventoryWarehouse.getName());
    this.chkVisible.setSelected(POSUtil.getBoolean(inventoryWarehouse.isVisible()));
  }
  
  public boolean updateModel()
  {
    InventoryWarehouse inventoryWarehouse = (InventoryWarehouse)getBean();
    if (inventoryWarehouse == null)
    {
      inventoryWarehouse = new InventoryWarehouse();
      setBean(inventoryWarehouse, false);
    }
    String nameString = this.tfName.getText();
    if (StringUtils.isEmpty(nameString)) {
      throw new PosException("Name cannot be empty");
    }
    inventoryWarehouse.setName(nameString);
    inventoryWarehouse.setVisible(Boolean.valueOf(this.chkVisible.isSelected()));
    
    return true;
  }
  
  public String getDisplayText()
  {
    return "Add Earehouse Entry";
  }
  
  public boolean save()
  {
    try
    {
      if (!updateModel()) {
        return false;
      }
      InventoryWarehouse model = (InventoryWarehouse)getBean();
      InventoryWarehouseDAO.getInstance().saveOrUpdate(model);
      
      return true;
    }
    catch (Exception e)
    {
      POSMessageDialog.showError(e.getMessage());
    }
    return false;
  }
}
