package com.orostock.inventory.ui;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.PackagingUnit;
import com.floreantpos.model.dao.PackagingUnitDAO;
import com.floreantpos.model.inventory.InventoryGroup;
import com.floreantpos.model.inventory.InventoryItem;
import com.floreantpos.model.inventory.InventoryLocation;
import com.floreantpos.model.inventory.InventoryVendor;
import com.floreantpos.model.inventory.dao.InventoryGroupDAO;
import com.floreantpos.model.inventory.dao.InventoryItemDAO;
import com.floreantpos.model.inventory.dao.InventoryLocationDAO;
import com.floreantpos.model.inventory.dao.InventoryVendorDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.ListComboBoxModel;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXComboBox;

public class InventoryItemEntryForm
  extends BeanEditor<InventoryItem>
{
  JPanel mainPanel = new JPanel();
  JTextField tfName = new FixedLengthTextField(60);
  JTextField tfBarcode = new FixedLengthTextField(30);
  DoubleTextField tfItemPerPackSize = new DoubleTextField(5);
  IntegerTextField tfSortOrder = new IntegerTextField(10);
  IntegerTextField tfPackSizeReorderLevel = new IntegerTextField(30);
  IntegerTextField tfPackSizeReplenishLevel = new IntegerTextField(30);
  JTextArea tfDescription = new JTextArea(4, 10);
  IntegerTextField tfTotalPackages = new IntegerTextField(10);
  DoubleTextField tfBalanceTotalRecepieUnits = new DoubleTextField(10);
  DoubleTextField tfPurchase_price = new DoubleTextField(10);
  DoubleTextField tfSelling_price = new DoubleTextField(10);
  private ListComboBoxModel unitModel = new ListComboBoxModel();
  private final JXComboBox cbPackagingUnit = new JXComboBox(this.unitModel);
  private final JXComboBox cbGroup = new JXComboBox();
  private final JXComboBox cbLocation = new JXComboBox();
  private final JXComboBox cbVendor = new JXComboBox();
  private final JButton btnNewGroup = new JButton("New Group");
  private final JButton btnNewLocation = new JButton("New Location");
  private final JButton btnNewVendor = new JButton("New Vendor");
  
  public InventoryItemEntryForm()
  {
    createUI();
    
    populateComboBoxes();
  }
  
  private void populateComboBoxes()
  {
    List<PackagingUnit> packagingUnits = PackagingUnitDAO.getInstance().findAll();
    this.unitModel.setDataList(packagingUnits);
    
    List<InventoryGroup> groups = InventoryGroupDAO.getInstance().findAll();
    this.cbGroup.setModel(new DefaultComboBoxModel(groups.toArray(new InventoryGroup[0])));
    
    List<InventoryLocation> locations = InventoryLocationDAO.getInstance().findAll();
    this.cbLocation.setModel(new DefaultComboBoxModel(locations.toArray(new InventoryLocation[0])));
    
    List<InventoryVendor> vendors = InventoryVendorDAO.getInstance().findAll();
    this.cbVendor.setModel(new DefaultComboBoxModel(vendors.toArray(new InventoryVendor[0])));
  }
  
  public void createNew()
  {
    setBean(new InventoryItem());
  }
  
  public void clearFields()
  {
    this.tfName.setText("");
    this.tfBarcode.setText("");
    this.tfItemPerPackSize.setText("");
    
    this.tfSortOrder.setText("");
    this.tfPackSizeReorderLevel.setText("");
    this.tfPackSizeReplenishLevel.setText("");
    this.tfDescription.setText("");
    
    this.tfTotalPackages.setText("");
    this.tfBalanceTotalRecepieUnits.setText("");
    this.tfPurchase_price.setText("");
    this.tfSelling_price.setText("");
    
    this.cbGroup.setSelectedIndex(-1);
    this.cbLocation.setSelectedIndex(-1);
    this.cbVendor.setSelectedIndex(-1);
  }
  
  public void setFieldsEnable(boolean enable)
  {
    this.tfName.setEnabled(enable);
    this.tfBarcode.setEnabled(enable);
    this.cbPackagingUnit.setEnabled(enable);
    this.tfItemPerPackSize.setEnabled(enable);
    
    this.tfSortOrder.setEnabled(enable);
    this.tfPackSizeReorderLevel.setEnabled(enable);
    this.tfPackSizeReplenishLevel.setEnabled(enable);
    this.tfDescription.setEnabled(enable);
    
    this.tfTotalPackages.setEnabled(enable);
    this.tfBalanceTotalRecepieUnits.setEnabled(enable);
    this.tfPurchase_price.setEnabled(enable);
    this.tfSelling_price.setEnabled(enable);
    
    this.cbGroup.setEnabled(enable);
    this.cbLocation.setEnabled(enable);
    this.cbVendor.setEnabled(enable);
    
    this.btnNewGroup.setEnabled(enable);
    this.btnNewLocation.setEnabled(enable);
    this.btnNewVendor.setEnabled(enable);
  }
  
  private void createUI()
  {
    setLayout(new BorderLayout());
    add(this.mainPanel);
    
    this.mainPanel.setLayout(new MigLayout("fillx", "[][grow,fill][grow,fill][]", "[][][][][][][][][][][][][][][][][]"));
    
    this.mainPanel.add(new JLabel("Name"), "cell 0 0,alignx trailing");
    this.mainPanel.add(this.tfName, "cell 1 0 3 1,growx");
    
    this.mainPanel.add(new JLabel("Barcode"), "cell 0 1,alignx trailing");
    this.mainPanel.add(this.tfBarcode, "cell 1 1 3 1,growx");
    
    this.mainPanel.add(new JLabel("Description"), "cell 0 2,alignx trailing");
    this.tfDescription.setTabSize(4);
    JScrollPane scrollPane = new JScrollPane(this.tfDescription);
    this.mainPanel.add(scrollPane, "cell 1 2 3 1,growx");
    
    this.mainPanel.add(new JLabel("Packaging unit"), "cell 0 3,alignx trailing");
    this.mainPanel.add(this.cbPackagingUnit, "cell 1 3,growx");
    
    JButton btnNewUnit = new JButton("NEW");
    btnNewUnit.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        PackagingUnitForm form = new PackagingUnitForm();
        BeanEditorDialog dialog = new BeanEditorDialog(form, (Frame)SwingUtilities.getWindowAncestor(InventoryItemEntryForm.this), true);
        dialog.pack();
        dialog.open();
        if (dialog.isCanceled()) {
          return;
        }
        InventoryItemEntryForm.this.unitModel.addElement(form.getBean());
      }
    });
    this.mainPanel.add(btnNewUnit, "wrap");
    
    this.mainPanel.add(new JLabel("Unit per package"), "cell 0 4,alignx trailing");
    this.mainPanel.add(this.tfItemPerPackSize, "cell 1 4,growx");
    
    this.mainPanel.add(new JLabel("Sort order"), "cell 0 5,alignx trailing");
    this.mainPanel.add(this.tfSortOrder, "cell 1 5,growx");
    
    this.mainPanel.add(new JLabel("Reorder level"), "cell 0 6,alignx trailing");
    this.mainPanel.add(this.tfPackSizeReorderLevel, "cell 1 6,growx");
    
    this.mainPanel.add(new JLabel("Replenish level"), "cell 0 7,alignx trailing");
    this.mainPanel.add(this.tfPackSizeReplenishLevel, "cell 1 7,growx");
    





    this.mainPanel.add(new JLabel("Total package"), "cell 0 9,alignx trailing");
    this.mainPanel.add(this.tfTotalPackages, "cell 1 9,growx");
    
    this.mainPanel.add(new JLabel("Total recepi unit"), "cell 0 10,alignx trailing");
    this.mainPanel.add(this.tfBalanceTotalRecepieUnits, "cell 1 10,growx");
    
    this.mainPanel.add(new JLabel("Package purchase price"), "cell 0 11,alignx trailing");
    this.mainPanel.add(this.tfPurchase_price, "cell 1 11,growx");
    
    this.mainPanel.add(new JLabel("Package selling price"), "cell 0 12,alignx trailing");
    this.mainPanel.add(this.tfSelling_price, "cell 1 12,growx");
    
    this.mainPanel.add(new JLabel(""), "cell 2 12");
    
    this.mainPanel.add(new JLabel("Group"), "cell 0 13,alignx trailing");
    
    this.mainPanel.add(this.cbGroup, "cell 1 13 2 1,growx");
    this.btnNewGroup.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        InventoryItemEntryForm.this.createNewGroup();
      }
    });
    this.mainPanel.add(this.btnNewGroup, "cell 3 13,growx");
    
    this.mainPanel.add(new JLabel("Location"), "cell 0 14,alignx trailing");
    
    this.mainPanel.add(this.cbLocation, "cell 1 14 2 1,growx");
    this.btnNewLocation.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        InventoryItemEntryForm.this.createNewLocation();
      }
    });
    this.mainPanel.add(this.btnNewLocation, "cell 3 14,growx");
    
    this.mainPanel.add(new JLabel("Vendor"), "cell 0 15,alignx trailing");
    
    this.mainPanel.add(this.cbVendor, "cell 1 15 2 1,growx");
    this.btnNewVendor.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        InventoryItemEntryForm.this.createNewVendor();
      }
    });
    this.mainPanel.add(this.btnNewVendor, "cell 3 15,growx");
  }
  
  protected void createNewVendor()
  {
    InventoryVendorEntryForm form = new InventoryVendorEntryForm(new InventoryVendor());
    BeanEditorDialog dialog = new BeanEditorDialog(form, BackOfficeWindow.getInstance(), true);
    dialog.pack();
    dialog.open();
    if (dialog.isCanceled()) {
      return;
    }
    InventoryVendor inventoryVendor = (InventoryVendor)form.getBean();
    DefaultComboBoxModel<InventoryVendor> cbModel = (DefaultComboBoxModel)this.cbVendor.getModel();
    cbModel.addElement(inventoryVendor);
    cbModel.setSelectedItem(inventoryVendor);
  }
  
  protected void createNewLocation()
  {
    InventoryLocationEntryForm form = new InventoryLocationEntryForm(new InventoryLocation());
    BeanEditorDialog dialog = new BeanEditorDialog(form, BackOfficeWindow.getInstance(), true);
    dialog.pack();
    dialog.open();
    if (dialog.isCanceled()) {
      return;
    }
    InventoryLocation inventoryLocation = (InventoryLocation)form.getBean();
    DefaultComboBoxModel<InventoryLocation> cbModel = (DefaultComboBoxModel)this.cbLocation.getModel();
    cbModel.addElement(inventoryLocation);
    cbModel.setSelectedItem(inventoryLocation);
  }
  
  protected void createNewGroup()
  {
    InventoryGroupEntryForm form = new InventoryGroupEntryForm(new InventoryGroup());
    BeanEditorDialog dialog = new BeanEditorDialog(form, BackOfficeWindow.getInstance(), true);
    dialog.pack();
    dialog.open();
    if (dialog.isCanceled()) {
      return;
    }
    InventoryGroup inventoryGroup = (InventoryGroup)form.getBean();
    DefaultComboBoxModel<InventoryGroup> cbModel = (DefaultComboBoxModel)this.cbGroup.getModel();
    cbModel.addElement(inventoryGroup);
    cbModel.setSelectedItem(inventoryGroup);
  }
  
  public void updateView()
  {
    InventoryItem inventoryItem = (InventoryItem)getBean();
    if (inventoryItem == null) {
      return;
    }
    NumberFormat f = new DecimalFormat("0.##");
    
    this.tfName.setText(inventoryItem.getName());
    this.tfBarcode.setText(inventoryItem.getPackageBarcode());
    this.cbPackagingUnit.setSelectedItem(inventoryItem.getPackagingUnit());
    this.tfItemPerPackSize.setText(f.format(inventoryItem.getUnitPerPackage()));
    this.tfSortOrder.setText(String.valueOf(inventoryItem.getSortOrder()));
    this.tfPackSizeReorderLevel.setText(String.valueOf(inventoryItem.getPackageReorderLevel()));
    this.tfPackSizeReplenishLevel.setText(String.valueOf(inventoryItem.getPackageReplenishLevel()));
    this.tfDescription.setText(inventoryItem.getDescription());
    this.tfTotalPackages.setText(f.format(inventoryItem.getTotalPackages()));
    this.tfBalanceTotalRecepieUnits.setText(f.format(inventoryItem.getTotalRecepieUnits()));
    

    this.tfPurchase_price.setText(f.format(inventoryItem.getUnitPurchasePrice()));
    this.tfSelling_price.setText(f.format(inventoryItem.getUnitSellingPrice()));
    
    this.cbGroup.setSelectedItem(inventoryItem.getItemGroup());
    this.cbLocation.setSelectedItem(inventoryItem.getItemLocation());
    this.cbVendor.setSelectedItem(inventoryItem.getItemVendor());
  }
  
  String formatDouble(double d)
  {
    NumberFormat f = new DecimalFormat("0.##");
    return f.format(d);
  }
  
  public boolean updateModel()
  {
    InventoryItem inventoryItem = (InventoryItem)getBean();
    if (inventoryItem == null)
    {
      inventoryItem = new InventoryItem();
      setBean(inventoryItem);
    }
    if (StringUtils.isEmpty(this.tfName.getText()))
    {
      POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Name is empty");
      return false;
    }
    inventoryItem.setName(this.tfName.getText());
    inventoryItem.setPackageBarcode(this.tfBarcode.getText());
    inventoryItem.setPackagingUnit((PackagingUnit)this.cbPackagingUnit.getSelectedItem());
    inventoryItem.setUnitPerPackage(Double.valueOf(this.tfItemPerPackSize.getDouble()));
    inventoryItem.setSortOrder(Integer.valueOf(this.tfSortOrder.getInteger()));
    inventoryItem.setPackageReorderLevel(Integer.valueOf(this.tfPackSizeReorderLevel.getInteger()));
    inventoryItem.setPackageReplenishLevel(Integer.valueOf(this.tfPackSizeReplenishLevel.getInteger()));
    inventoryItem.setDescription(this.tfDescription.getText());
    
    inventoryItem.setCreateTime(new Date());
    inventoryItem.setTotalPackages(Integer.valueOf(this.tfTotalPackages.getInteger()));
    inventoryItem.setTotalRecepieUnits(Double.valueOf(this.tfBalanceTotalRecepieUnits.getDouble()));
    

    inventoryItem.setUnitPurchasePrice(Double.valueOf(this.tfPurchase_price.getDouble()));
    inventoryItem.setUnitSellingPrice(Double.valueOf(this.tfSelling_price.getDouble()));
    
    inventoryItem.setItemGroup((InventoryGroup)this.cbGroup.getSelectedItem());
    inventoryItem.setItemLocation((InventoryLocation)this.cbLocation.getSelectedItem());
    inventoryItem.setItemVendor((InventoryVendor)this.cbVendor.getSelectedItem());
    
    return true;
  }
  
  public boolean save()
  {
    try
    {
      updateModel();
      
      InventoryItemDAO dao = InventoryItemDAO.getInstance();
      dao.saveOrUpdate((InventoryItem)getBean());
      return true;
    }
    catch (Exception e)
    {
      POSMessageDialog.showError(e.getMessage(), e);
    }
    return false;
  }
  
  public boolean delete()
  {
    InventoryItem inventoryItem = (InventoryItem)getBean();
    if (inventoryItem == null) {
      return false;
    }
    InventoryItemDAO.getInstance().delete(inventoryItem);
    return true;
  }
  
  public String getDisplayText()
  {
    InventoryItem inventoryItem = (InventoryItem)getBean();
    if ((inventoryItem == null) || (inventoryItem.getId() == null)) {
      return "Add new inventory item";
    }
    return "Edit inventory item";
  }
}
