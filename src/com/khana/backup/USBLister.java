package com.khana.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;
import javax.usb.*;

import org.apache.commons.io.FileUtils;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.dao.RestaurantDAO;

    public class USBLister {
    	
      public static void main(String[] args) throws UsbException {
        //Get UsbHub
    	
      }

      public void getUsbDevices() throws SecurityException, UsbException {
    	 UsbServices services = UsbHostManager.getUsbServices();
         UsbHub root = services.getRootUsbHub();
        
          listPeripherique(root);
      }
      
     public static void listPeripherique(UsbHub hub) {
        //List all the USBs attached
    	 
        List perepheriques = hub.getAttachedUsbDevices();
        Iterator iterator = perepheriques.iterator();

        while (iterator.hasNext()) {
          UsbDevice perepherique = (UsbDevice) iterator.next();
           
          if (perepherique.isUsbHub()) {       	 
            listPeripherique((UsbHub) perepherique);              
            zipMyFolder();			 
          }
        }
      }
     
     public static boolean zipMyFolder() {   	
     	Path source = Paths.get("database/derby-single/posdb");
     	 if(TerminalConfig.isNoGUI()||TerminalConfig.isKhanaServer())
     		 source = Paths.get("database/derby-server/posdb");
      
          if(TerminalConfig.isKhanaClient())
         	 return false;
          
          if(TerminalConfig.getUsbFolder()!=null&&TerminalConfig.getUsbFolder().length()>0) {
           	unzipFile(source.toString());
            konfigKopi();
          
             return true;    
          }
          return false;
     }
     
     public static boolean konfigKopi() {
    	 SimpleDateFormat format = new SimpleDateFormat("dd");
       	Calendar calendar = Calendar.getInstance();
       	calendar.setTime(new Date());
       	calendar.add(Calendar.DAY_OF_MONTH, -1);
       	
       	String usbDrive = TerminalConfig.getUsbFolder();
       	if(TerminalConfig.getUsbFolder().contains(":")) {
       		String[] parts = TerminalConfig.getUsbFolder().split(":");
       		usbDrive = parts[0];
       	}
       	
    	Date date = new Date();
      	LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      	int month = localDate.getMonthValue();
       	
      	File directory = new File(usbDrive+":\\Backup/Database_"+calendar.get(Calendar.YEAR)+"/"+month+"/"+format.format(calendar.getTime())+"/konfig");
      	 
      	if (!directory.exists()) {
  			directory.mkdirs();
  		}
      	
          if(!new File(directory.toString()).exists()) {  
         	 System.out.println("zipFileName exist");
          	return false;
          }
          
          File konfig = new File(directory+"/khana.config.properties");
          
          if(konfig.exists()) {
         	 return false;
          }
          
          try {
        	  copyFile(new File("khana.config.properties"),konfig);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
          
          return true;  
     }
     
     public static boolean unzipFile(String destination) {
    	SimpleDateFormat format = new SimpleDateFormat("dd");
      	Calendar calendar = Calendar.getInstance();
      	calendar.setTime(new Date());
      	calendar.add(Calendar.DAY_OF_MONTH, -1);
      	
      	String usbDrive = TerminalConfig.getUsbFolder();
      	if(TerminalConfig.getUsbFolder().contains(":")) {
      		String[] parts = TerminalConfig.getUsbFolder().split(":");
      		usbDrive = parts[0];
      	}
      	
      	Date date = new Date();
      	LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      	int month = localDate.getMonthValue();
      	
     	File directory = new File(usbDrive+":\\Backup/Database_"+calendar.get(Calendar.YEAR)+"/"+month+"/"+format.format(calendar.getTime())+"/database");
     	 
     	if (!directory.exists()) {
 			directory.mkdirs();
 		}
     	
         String zipFileName = directory.toString()+ "/" +format.format(calendar.getTime())+ ".zip";
         
         if(!new File(directory.toString()).exists()) {  
        	 System.out.println("zipFileName exist");
         	return false;
         }
         
         File seg = new File(directory+"/seg0");
         File tmp = new File(directory+"/tmp");
         
         if(seg.exists()&&tmp.exists()) {
        	 return false;
         }
         
         File destDir = new File(destination);       
         System.out.println("destDir:"+destDir);
        
         try {
			copyDirectoryCompatibityMode(destDir,directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         return true;         
     } 
     
     private static void copyFile(File sourceFile, File destinationFile) 
    		  throws IOException {
    		    try (InputStream in = new FileInputStream(sourceFile); 
    		      OutputStream out = new FileOutputStream(destinationFile)) {
    		        byte[] buf = new byte[1024];
    		        int length;
    		        while ((length = in.read(buf)) > 0) {
    		            out.write(buf, 0, length);
    		        }
    		    }
    		}
     
     public static void copyDirectoryCompatibityMode(File source, File destination) throws IOException {
    	    if (source.isDirectory()) {
    	        copyDirectory(source.getCanonicalPath(), destination.getCanonicalPath());
    	    } else {
    	        copyFile(source, destination);
    	    }
    	}
     
     public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) throws IOException {
    	    File sourceDirectory = new File(sourceDirectoryLocation);
    	    File destinationDirectory = new File(destinationDirectoryLocation);
    	    FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
    	}
     
     public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
         File destFile = new File(destinationDir, zipEntry.getName());
      
         String destDirPath = destinationDir.getCanonicalPath();
         String destFilePath = destFile.getCanonicalPath();
      
         if (!destFilePath.startsWith(destDirPath + File.separator)) {
             throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
         }
      
         return destFile;
     }

    }