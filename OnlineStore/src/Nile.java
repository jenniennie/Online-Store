/*  Name:  Jennifer Nguyen
     Course: CNT 4714 – Spring 2022 
     Assignment title: Project 1 – Event-driven Enterprise Simulation 
     Date: Sunday January 30, 2022 
*/ 

import java.io.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;

public class Nile extends JFrame
{
	static int itemCount = 0, maxArraysize=10, frameWidth = 800, frameHeight = 250;
	
	private JLabel idLabel = new JLabel("Item ID:", SwingConstants.RIGHT);
	private JLabel quantityLabel = new JLabel("Quantity:", SwingConstants.RIGHT);
	private JLabel detailLabel = new JLabel("Item Details:", SwingConstants.RIGHT);
	private JLabel subtotalLabel = new JLabel("Order Subtotal for " + itemCount + " item(s):", SwingConstants.RIGHT);
	
	private ArrayList<stock> inventory;
	
	static String [] itemIDArray = new String[maxArraysize];
	static String [] itemNameArray = new String[maxArraysize];
	boolean [] itemInstockArray = new boolean[maxArraysize];
	static double [] itemPriceArray = new double[maxArraysize];
	static int [] itemQuantityArray = new int[maxArraysize];;
	static String [] itemDiscountStr =  new String[maxArraysize];
	static double [] itemDiscountArray = new double[maxArraysize];
	static double [] itemSubtotalArray = new double[maxArraysize];
	            
	                                        
	static String itemID = "", itemQuantityStr = "";
	static double orderSubtotal=0, orderTotal=0, orderTaxAmount;
			     
	
	final static double TAXRATE = 0.060, DISCOUNT5 =.10, DISCOUNT10 = .15, DISCOUNT15 = .20;                                  
	                                        
	
	public static void main(String args[]) throws FileNotFoundException
	{
			// Initial Setup
	       Nile startframe = new Nile();
	       startframe.pack(); // fit windows for screen
	       startframe.setTitle("Nile");
	       startframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       startframe.setSize(frameWidth,frameHeight);
	       startframe.setLocationRelativeTo(null);
	       startframe.setVisible(true);
    }
	public Nile() throws FileNotFoundException
	{
			organizeInventory();
			NumberFormat decimalformatter = new DecimalFormat("#0.00");
			
			// Search Boxes
			JPanel searchPanel = new JPanel();
			searchPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
			JPanel idPanel = new JPanel();
			JPanel quanPanel = new JPanel();
			JPanel detailPanel = new JPanel();
			JPanel subtotalPanel = new JPanel();
	       
	       JTextField idTextField = new JTextField();
		   JTextField quantityTextField = new JTextField();
		   JTextField detailTextField = new JTextField();
		   JTextField subtotalTextField = new JTextField();
	   	
		   searchPanel.setLayout(new GridLayout(5,2,6,10));
		   idPanel.setLayout(new GridLayout(1,1));
		   quanPanel.setLayout(new GridLayout(1,1));
		   detailPanel.setLayout(new GridLayout(1,1));
		   subtotalPanel.setLayout(new GridLayout(1,1));
		   
		   idPanel.add(idLabel);
		   idPanel.add(idTextField);
		   quanPanel.add(quantityLabel);
		   quanPanel.add(quantityTextField);
		   detailPanel.add(detailLabel);
		   detailPanel.add(detailTextField);
		   subtotalPanel.add(subtotalLabel);
		   subtotalPanel.add(subtotalTextField);
		   
		   searchPanel.add(idPanel);
		   searchPanel.add(quanPanel);
		   searchPanel.add(detailPanel);
		   searchPanel.add(subtotalPanel);
		   
	       // make lower panel and buttons
	       JPanel actionPanel = new JPanel();
	       actionPanel.setLayout(new FlowLayout());
	       
	       JButton process = new JButton("Process Item #" + (itemCount+1));
		   JButton confirm = new JButton("Confirm Item #" + (itemCount+1));
		   JButton view = new JButton("View Order");
		   JButton finish = new JButton("Finish");
		   JButton newOrder = new JButton("New Order");
		   JButton exitBut = new JButton("Exit");
	       
	       actionPanel.add(process);
	       actionPanel.add(confirm); 
	       actionPanel.add(view); 
	       actionPanel.add(finish); 
	       actionPanel.add(newOrder); 
	       actionPanel.add(exitBut); 
	       
	       // Add things
	       add(BorderLayout.CENTER, searchPanel);
	       add(BorderLayout.SOUTH, actionPanel);
	       
	       // button not enabled until after calculation
	       confirm.setEnabled(false);
	       view.setEnabled(false);
	       finish.setEnabled(false);
	      
	       // Process Button
	       process.addActionListener(new ActionListener() 
	       {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
	     			 	String outputMessage;
	     			 	int i = 0;
	     			 	
	     		   		boolean foundID = false, isNumItemsOk = true, isInStock = true;
	     		   		
	     		   		   itemID = idTextField.getText();
	     		   		   itemQuantityStr = quantityTextField.getText();
	     		   		  
	     		   		  
	     		   		   int quantityint = Integer.parseInt(quantityTextField.getText());
	     		   		  
	     		   		   for (i = 0; i < inventory.size(); i++) 
	     		   		   {
	     		   			   stock currentitem = inventory.get(i);
	     		   			   if(currentitem.getitemID().compareTo(itemID) == 0)
	     		   			   {
	     		   				   foundID = true;
	     		   				   
	     		   				   if (!currentitem.getStock())
	     		   					   isInStock = false;
	     		   				   if (quantityint < 0)
	     		   					   isNumItemsOk = false;
	     		   				   break;
	     		   			   }
	     		   			  
	     		   		   }
		   			
		     		   		if(foundID == false || isNumItemsOk == false || isInStock == false)
		     		   		{
		     		   			if(foundID == false)
		     		   			{
		     		   				outputMessage = "item ID " + itemID + " not in file";
		     		   				JOptionPane.showMessageDialog(null, outputMessage, "Nile - ERROR", JOptionPane.ERROR_MESSAGE);
		     		   				idTextField.setText ("");
		     		   				quantityTextField.setText("");
		     		   				
		     		   			} 
		     		   			if(isNumItemsOk == false)
		     		   			{
		     		   				outputMessage = "Please enter positive numbers for number of items or number of item's";
		     		   				JOptionPane.showMessageDialog(null, outputMessage, "Nile - ERROR", JOptionPane.ERROR_MESSAGE);
		     		   	    	} 
		     		   			else if(isInStock == false)
		     		   	    	{
		     		   	    		outputMessage = "Sorry... that item is out of stock, please try another item";
			   	  		   	        JOptionPane.showMessageDialog(null, outputMessage, "Nile - ERROR", JOptionPane. ERROR_MESSAGE);
			   	  		   	        idTextField.setText("");
			   	  		   	        quantityTextField.setText ("");
			   	  		   	        detailTextField.setText ("");
		     		   	    	}
		     		   		}
	     		   		
	     		   		   
     		   		   if (foundID && isNumItemsOk && isInStock)
     		   		   {
     		   			   	stock founditem = inventory.get(i);
     		   			   	
	     		   			itemIDArray[itemCount] = founditem.getitemID();
		     		   		itemNameArray[itemCount] = founditem.getName();
		     		   		itemInstockArray[itemCount] = founditem.getStock();
		     		   		itemPriceArray[itemCount] = founditem.getPrice();
		     		   		itemQuantityArray[itemCount] = Integer.parseInt(itemQuantityStr);
		     		   		
		     		   		if(itemQuantityArray[itemCount] >= 5 && itemQuantityArray[itemCount] <= 9)
		     		   		{
		     		   			itemDiscountArray[itemCount] = DISCOUNT5;
		     		   			itemDiscountStr[itemCount] = "5%";
		     		   		}
		     		   		else if(itemQuantityArray[itemCount] >= 10 && itemQuantityArray[itemCount] <= 14)
		     		   		{
		     		   			itemDiscountArray[itemCount] = DISCOUNT10;
		     		   			itemDiscountStr[itemCount] = "10%";
		     		   		}
		     		   		else if(itemQuantityArray[itemCount] >= 15)
		     		   		{
		     		   			itemDiscountArray[itemCount] = DISCOUNT15;
		     		   			itemDiscountStr[itemCount] = "15%";
		     		   		}
		     		   		else
		     		   		{
			     		   		itemDiscountArray[itemCount] = 0.0;
			     		   		itemDiscountStr[itemCount] = "0%";
	     		   		   	}
		     		   		
		     		   		itemSubtotalArray[itemCount] = (itemPriceArray[itemCount] * itemQuantityArray[itemCount]) - (itemDiscountArray[itemCount]*(itemPriceArray[itemCount] * itemQuantityArray[itemCount]));
		     		   		orderSubtotal = orderSubtotal + itemSubtotalArray[itemCount];
     		   		        
		     		   		detailTextField.setText(itemNameArray[itemCount] + " $" + decimalformatter.format(itemPriceArray[itemCount]) + " " + itemQuantityArray[itemCount] + " " + itemDiscountStr[itemCount] + " $" + decimalformatter.format(itemSubtotalArray[itemCount]));
		     		   		
		     		   		// adjust button availability
			     		   	process.setEnabled(false);
			     		   	confirm.setEnabled(true);
							view.setEnabled(false);
							finish.setEnabled(false);
     		   		   
     		   		   }
 		   		   
	     		   	}
	       });
	       
	       confirm.addActionListener(new ActionListener()
	       {		
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					
					idTextField.setText("");
					quantityTextField.setText("");
					subtotalTextField.setText("$" + decimalformatter.format(orderSubtotal));

					itemCount++;
					
					process.setText("Process Item #" + (itemCount + 1));
					confirm.setText("Confirm Item #" + (itemCount + 1));
					subtotalLabel.setText("Order Subtotal for " + itemCount + " item(s):");
					JOptionPane.showMessageDialog(null, "Item #" + itemCount + " accepted. Added to your cart.");
					
					process.setEnabled(true);
					view.setEnabled(true);
					finish.setEnabled(true);
					confirm.setEnabled(false);
					
				}
	       });
	       
	       view.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					StringBuilder viewOrder = new StringBuilder();

					for(int i = 0; i < itemCount; i++)
					{
						viewOrder.append((i+1) +". " + itemNameArray[i] + " $" + itemPriceArray[i] + " " + itemQuantityArray[i] + " " + itemDiscountStr[i] + " $" + decimalformatter.format(itemSubtotalArray[i]));
						viewOrder.append(System.getProperty("line.separator"));
					}
					JOptionPane.showMessageDialog(null,viewOrder);
				}
			});
	       
	       finish.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) 
				{
					File transactionFile = new File("transactions.txt");
					
					ZonedDateTime now = ZonedDateTime.now();
					DateTimeFormatter transactionid = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
					DateTimeFormatter fileDate = DateTimeFormatter.ofPattern("MM/dd/yy',' hh:mm:ss a z");
					  
					StringBuilder finalOrder = new StringBuilder();
					
					PrintWriter write;
					
					try {
						if (transactionFile.exists() == false)
						{
							transactionFile.createNewFile();
						}
						// Message 
						finalOrder.append("Date: " + now.format(fileDate));
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append("Number of line items: " + itemCount); 
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append("Item# / ID / Title / Price / Qty / Disc % / Subtotal:");
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append(System.getProperty("line.separator"));
						
						write = new PrintWriter(new FileWriter(transactionFile, true));
						
						for(int i = 0; i < itemCount; i++)
						{
							write.append(now.format(transactionid) + ", " + itemIDArray[i] + ", " + itemNameArray[i] +", " + itemPriceArray[i] + ", " + itemQuantityArray[i] + ", " + now.format(fileDate) + "\n");
							
							finalOrder.append((i+1) +". " + itemNameArray[i] + " $" + decimalformatter.format(itemPriceArray[i]) + " " + itemQuantityArray[i] + " " + itemDiscountStr[i] + " $" + decimalformatter.format(itemSubtotalArray[i]));
							finalOrder.append(System.getProperty("line.separator"));
						}
						
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append("Order Subtotal: $" + decimalformatter.format(orderSubtotal));
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append("Tax Rate: 6%");
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append(System.getProperty("line.separator"));
						orderTaxAmount = orderSubtotal * TAXRATE;
						finalOrder.append("Tax amount: $" + decimalformatter.format(orderTaxAmount));
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append(System.getProperty("line.separator"));
						orderTotal = orderSubtotal + orderTaxAmount;
						finalOrder.append("Order Total: $" + decimalformatter.format(orderTotal));
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append(System.getProperty("line.separator"));
						finalOrder.append("Thanks for shopping at the Nile!");
						
						JOptionPane.showMessageDialog(null,finalOrder);
						
						write.close();
						idTextField.setEditable(false);
						quantityTextField.setEditable(false);
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
	       
	       
	       newOrder.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					itemCount = 0;
					orderSubtotal = 0;
					Nile.super.dispose();
					try {
						Nile.main(null);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
	       
	       exitBut.addActionListener(new ActionListener() {
	
				@Override
				public void actionPerformed(ActionEvent e) {
					Nile.super.dispose();
				}
			});

	}
	
	 
	 public void organizeInventory() throws FileNotFoundException
	 {
		 this.inventory = new ArrayList<stock>();
		 File inventoryfile = new File("inventory.txt");
		 Scanner ascan = new Scanner(inventoryfile);

			while (ascan.hasNextLine()) {
				String item = ascan.nextLine();
				String[] itemInfo = item.split(", ");

				stock newitem = new stock();
				
				newitem.setitemID(itemInfo[0]);

				newitem.setName(itemInfo[1]);
				
				newitem.setStock(Boolean.parseBoolean(itemInfo[2]));
				
				newitem.setPrice(Double.parseDouble(itemInfo[3]));

				inventory.add(newitem);
			}
			
			ascan.close();
	 }

}

