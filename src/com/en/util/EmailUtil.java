package com.en.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.en.model.AccessPointModel;
import com.en.model.CustomerEmailModel;
import com.en.model.CustomerModel;
import com.en.model.EntryModel;
import com.en.model.ItemModel;
import com.en.model.MessageModel;
import com.en.model.OrderModel;
import com.en.model.QuotationModel;
import com.en.model.SalesModel;
import com.en.model.UserModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class EmailUtil implements Constant {
	
	public static void sendMail(String[] toEmail, String subject, String msg) {
 
		try {
			
			Session mailAccountSession = getAccountSession();
 
			Message message = new MimeMessage(mailAccountSession);
			message.setFrom(new InternetAddress("heshstoreaccounts@hotmail.com"));
			InternetAddress[] toAdd = new InternetAddress[toEmail.length];
			for(int i=0; i<toEmail.length; i++){
				toAdd[i] = new InternetAddress(toEmail[i].toLowerCase());
			}
			message.setRecipients(Message.RecipientType.TO, toAdd);
			//message.setRecipient(Message.RecipientType.BCC, new InternetAddress("heshstorebhavin@hotmail.com"));
			message.setSubject(subject);
			message.setContent(msg, "text/html; charset=ISO-8859-1");
 
			Transport.send(message);
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void sendImgMail(String[] toEmail, String subject, String imgFile){
		
		try {
			
			Session mailSession = getNewsSession();

			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress("heshstorenews@hotmail.com","Hesh India"));
//			message.setFrom(new InternetAddress("heshstorenews@hotmail.com"));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress("heshstorebhavin@hotmail.com"));
//			InternetAddress[] toAdd = new InternetAddress[1];
//			toAdd[0] = new InternetAddress("shahbhavin85@gmail.com");
			InternetAddress[] toAdd = new InternetAddress[(toEmail.length)+1];
			int j=0;
				for(int i=0; i<toEmail.length; i++){
					toAdd[j] = new InternetAddress(toEmail[i].toLowerCase());
					j++;
				}
			toAdd[toAdd.length-1] = new InternetAddress("shahbhavin85@gmail.com");
			message.setRecipients(Message.RecipientType.BCC, toAdd);
			message.setSubject(subject);
//			message.setContent("<html><body><img style='width:210mm;height:297mm;' src=\"http://122.165.79.21:8080/PROMOTIONAL_IMAGES/"+imgFile+"\"/><br/><img src=\"http://122.165.79.21:8080/PROMOTIONAL_IMAGES/pic.jpg\" alt=\"address\" usemap=\"#map\"><map name=\"map\"><area shape=\"circle\" coords=\"414,522,28\" title=\"Gallery\" alt=\"gallery\" href=\"javascript:alert('Gallery Under Updation!!!');\"><area shape=\"circle\" coords=\"490,522,28\" title=\"Shopping\" alt=\"shopping\" target=\"_blank\" href=\"http://www.heshstore.com\"><area shape=\"circle\" coords=\"568,522,28\" title=\"Offer\" alt=\"offer\" href=\"javascript:alert('Offer Under Updation!!!');\"><area shape=\"circle\" coords=\"644,522,28\" title=\"Twitter\" alt=\"twitter\" target=\"_blank\" href=\"http://www.twitter.com/HeshIndia\"><area shape=\"circle\" coords=\"721,522,28\" title=\"Facebook\" alt=\"facebook\" target=\"_blank\" href=\"http://on.fb.me/HeshIndia\"></map>If you are not able to view the email please <a target=\"_blank\" href=\"http://122.165.79.21:8080/PROMOTIONAL_IMAGES/"+imgFile+"\">Click Here</a></body></html>", "text/html; charset=ISO-8859-1");
			message.setContent("<html><body>If you are not able to view the email please select \"Always Display Images\" or <a target=\"_blank\" href=\"http://122.165.79.21:8080/PROMOTIONAL_IMAGES/"+imgFile+"\">Click Here</a><br/><br/>To Download the product catalogue <a target=\"_blank\" href=\"http://122.165.79.21:8080/CATALOGUE/catalogue.pdf\">Click Here</a><br/><br/><img style='width:210mm;height:297mm;' src=\"http://122.165.79.21:8080/PROMOTIONAL_IMAGES/"+imgFile+"\"/><br/><img src=\"http://122.165.79.21:8080/PROMOTIONAL_IMAGES/pic.jpg\" alt=\"address\" usemap=\"#map\"><map name=\"map\"><area shape=\"circle\" coords=\"414,522,28\" title=\"Gallery\" alt=\"gallery\" href=\"javascript:alert('Gallery Under Updation!!!');\"><area shape=\"circle\" coords=\"490,522,28\" title=\"Shopping\" alt=\"shopping\" target=\"_blank\" href=\"http://www.heshstore.com\"><area shape=\"circle\" coords=\"568,522,28\" title=\"Offer\" alt=\"offer\" href=\"javascript:alert('Offer Under Updation!!!');\"><area shape=\"circle\" coords=\"644,522,28\" title=\"Twitter\" alt=\"twitter\" target=\"_blank\" href=\"http://www.twitter.com/HeshIndia\"><area shape=\"circle\" coords=\"721,522,28\" title=\"Facebook\" alt=\"facebook\" target=\"_blank\" href=\"http://on.fb.me/HeshIndia\"></map></body></html>", "text/html; charset=ISO-8859-1");
//			message.setContent("<html><body>If you are not able to view the email please select \"Always Display Images\" or <a target=\"_blank\" href=\"http://122.165.79.21:8080/PROMOTIONAL_IMAGES/"+imgFile+"\">Click Here</a><br/><img style='width:210mm;height:297mm;' src=\"http://122.165.79.21:8080/PROMOTIONAL_IMAGES/Test3479.jpg\"/><br/><img src=\"http://122.165.79.21:8080/PROMOTIONAL_IMAGES/pic.jpg\" alt=\"address\" usemap=\"#map\"><map name=\"map\"><area shape=\"circle\" coords=\"414,522,28\" title=\"Gallery\" alt=\"gallery\" href=\"javascript:alert('Gallery Under Updation!!!');\"><area shape=\"circle\" coords=\"490,522,28\" title=\"Shopping\" alt=\"shopping\" target=\"_blank\" href=\"http://www.heshstore.com\"><area shape=\"circle\" coords=\"568,522,28\" title=\"Offer\" alt=\"offer\" href=\"javascript:alert('Offer Under Updation!!!');\"><area shape=\"circle\" coords=\"644,522,28\" title=\"Twitter\" alt=\"twitter\" target=\"_blank\" href=\"http://www.twitter.com/HeshIndia\"><area shape=\"circle\" coords=\"721,522,28\" title=\"Facebook\" alt=\"facebook\" target=\"_blank\" href=\"http://on.fb.me/HeshIndia\"></map></body></html>", "text/html; charset=ISO-8859-1");
	        Transport.send(message);
 
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static Session getNewsSession() {
		Session session = null;
			 
		final String username = "news@heshstore.com";
		final String password = "hesh12345"; 
		 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.sendgrid.net");
		props.put("mail.smtp.port", "2525");
		
		session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		return session;
	}
	
	private static Session getGmailSession() {
		Session session = null;
			 
		final String username = "heshstoreprint@gmail.com";
		final String password = "9940448555"; 
		 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		return session;
	}

	private static Session getAccountSession(){
		Session session = null;
			 
		final String username = "heshstoreaccounts@hotmail.com";
		final String password = "heshstore123"; 
		 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.live.com");
		props.put("mail.smtp.port", "587");
		
		session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		return session;
	}

	public static MessageModel sendSalesMail(SalesModel model) {
		MessageModel msg = new  MessageModel();
		if(model.getCustomer().getEmail().equals("") && model.getCustomer().getEmail1().equals("")){
			msg.addNewMessage(new com.en.model.Message(ALERT, "Customer email doesnot exits"));
		} else {
			ArrayList<String> toEmails = new ArrayList<String>(0);
			if(!model.getCustomer().getEmail().equals("")) {
				toEmails.add(model.getCustomer().getEmail());
			}
			if(!model.getCustomer().getEmail1().equals("")) {
				toEmails.add(model.getCustomer().getEmail1());
			}
			String subject = "Supply Details of Invoice No : "+model.getBranch().getBillPrefix()+Utils.padBillNo(model.getSaleid());
			String taxType = ((model.getTaxtype() == 1) ? "VAT BILL" : ((model.getTaxtype() == 2) ? "CST BILL" : "CST AGAINST FORM 'C' BILL"));
			String payType = (model.getPaymode() == 1) ? "CASH" : (model.getPaymode() == 2) ? "CREDIT CARD" : "CREDIT OF "+model.getCreditDays()+" DAYS";
			String textMsg = "<html><body><div style='border: 1px; border-color: black; border-style: double; width: 850px; padding:5px;'><br/>";
			textMsg = textMsg + 
					"<center><label style='font-size: 12px; font-family: tahoma;'><b><u>REFERENCE INVOICE</u></b></label></center><br/>"+
					"<table cellpadding='3' border=1 style='width: 800px; font-size: 11px; font-family: tahoma;' align='center'>"+
						"<tr>"+
							"<td style='padding-left: 20px;' align='right'>Bill No</td>"+
							"<td><b>"+model.getBranch().getBillPrefix()+Utils.padBillNo(model.getSaleid())+"</b></td>"+
							"<td align='right'>Date</td>"+
							"<td><b>"+Utils.convertToAppDateDDMMYY(model.getSalesdate())+"</b></td>"+
							"<td align='right' valign='top'>Salesman</td>"+
							"<td valign='top'><b>"+model.getSalesman().getUserName()+"</b></td>"+
						"</tr>"+
						"<tr>"+
							"<td style='padding-left: 20px;' valign='top' align='right'>Customer</td>"+
							"<td><b>"+model.getCustomer().getLabel()+"</b></td>" +
							"<td align='right'>Tax Type</td><td><b>"+taxType+"</b></td>"+
							"<td align='right'>Pay Mode</td><td><b>"+payType+"</b></td>"+
						"</tr>"+
						"<tr>"+
							"<td style='padding-left: 20px;' align='right'>TIN/CST</td>"+
							"<td colspan='2'><b>"+model.getCustomer().getTin() + " / " +model.getCustomer().getCst()+"</b></td>"+
							"<td align='right'>Contact Person / Phone No</td>"+
							"<td colspan='2'><b>"+model.getCustomer().getContactPerson()+ " / "+model.getCustomer().getPhone1()+"</b></td>"+
						"</tr>"+
						"<tr>"+
							"<td style='padding-left: 20px;' align='right'>Mode Of Transport</td>"+
							"<td><b>"+model.getTransport()+"</b></td>"+
							"<td align='right'>LR No</td>"+
							"<td><b>"+model.getLrno()+"</b></td>"+
							"<td align='right' valign='top'>LR Date</td>"+
							"<td valign='top'><b>"+model.getLrdt()+"</b></td>"+
						"</tr>"+
						"<tr>"+
						"<td style='padding-left: 20px;' align='right'>No. Of Ctns</td>"+
							"<td><b>"+model.getCtns()+"</b></td>"+
							"<td align='right'>Packed By</td>"+
							"<td><b>"+model.getPackedBy().getUserName()+"</b></td>"+
							"<td align='right' valign='top'>Despatched By</td>"+
							"<td valign='top'><b>"+model.getDespatchedBy().getUserName()+"</b></td>"+
						"</tr>"+
						"<tr>"+
							"<td align='right'>Remarks</td><td colspan='5'><b>"+model.getRemarks()+"</b></td>"+
						"</tr>"+
					"</table> <br/>";
			double roundOff = 0;
			double stotal = 0;
			double qty = 0;
			double ttotal = 0;
			double total = 0;
			textMsg = textMsg+
				"<table border=1 style=\"width:800px; font-size: 11px; font-family: tahoma;\"  align='center'>" +
						"<tr>" +
							"<th style=\"width : 50px;\">S.No</th>" +
							"<th>Description Of Goods</th>" +
							"<th style=\"width : 50px;\">Qty</th>" +
							"<th style=\"width : 50px;\">Rate<br/>(Rs. )</th>" +
							"<th style=\"width : 50px;\">Discount<br/>(%)</th>" +
							"<th style=\"width : 65px;\">GrossRate<br/>(Rs. )</th>" +
							"<th style=\"width : 50px;\">Tax<br/>(%)</th>" +
							"<th style=\"width : 50px;\">Tax<br/>(Rs. )</th>" +
							"<th style=\"width : 65px;\">Nett Rate<br/>(Rs. )</th>" +
						"</tr>";
			for(int i=0; i<model.getItems().size(); i++){
				textMsg = textMsg +
						"<tr>" +
							"<td align=\"center\">"+(i+1)+"</td>" +
							"<td>"+model.getItems().get(i).getItem().getItemNumber() + " / "+ model.getItems().get(i).getItem().getItemName() + " " + model.getItems().get(i).getDesc()+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getItems().get(i).getQty())+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getItems().get(i).getRate())+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getItems().get(i).getDisrate())+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * model.getItems().get(i).getRate())+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getItems().get(i).getTaxrate())+"%</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100)+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal((((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate())+(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty()*(model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100)))+"</td>" +
						"</tr>";
				qty = qty + (model.getItems().get(i).getQty());
				stotal = stotal + ((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * model.getItems().get(i).getRate();
				ttotal = ttotal + ((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100;
				total = total + ((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate()+((model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100));
			}
			if(model.getInstallation() > 0){
				textMsg = textMsg +
						"<tr>" +
							"<td align=\"right\" colspan='2'>INSTALLATION(+)</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getInstallation())+"</td>" +
						"</tr>";
				total = total + model.getInstallation();
			}
			if(model.getPacking() > 0){
				textMsg = textMsg +
						"<tr>" +
							"<td align=\"right\" colspan='2'>PACKING(+)</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getPacking())+"</td>" +
						"</tr>";
				total = total + model.getPacking();
			}
			if(model.getForwarding() > 0){
				textMsg = textMsg +
						"<tr>" +
							"<td align=\"right\" colspan='2'>FORWARDING(+)</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getForwarding())+"</td>" +
						"</tr>";
				total = total + model.getForwarding();
			}
			if(model.getLess() > 0){
				textMsg = textMsg +
						"<tr>" +
							"<td align=\"right\" colspan='2'>LESS(-)</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getLess())+"</td>" +
						"</tr>";
				total = total - model.getLess();
			}
			roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
			if(roundOff != 0){
				textMsg = textMsg +
						"<tr>" +
							"<td align=\"right\" colspan='2'>ROUNDOFF</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(roundOff)+"</td>" +
						"</tr>";
				total = total + roundOff;
			}
			textMsg = textMsg +
					"<tr>" +
						"<th align=\"right\" colspan='2'>GRAND TOTAL</th>" +
						"<th align=\"center\">"+qty+"</th>" +
						"<th>&nbsp;</th>" +
						"<th>&nbsp;</th>" +
						"<th align=\"center\">"+Utils.get2Decimal(stotal)+"</th>" +
						"<th>&nbsp;</th>" +
						"<th align=\"center\">"+Utils.get2Decimal(ttotal)+"</th>" +
						"<th align=\"center\">"+Utils.get2Decimal(total)+"</th>" +
					"</tr>";
			textMsg = textMsg+"</table><br/>";
			textMsg = textMsg+"<label style='font-size: 10px; font-family: tahoma; color: red; background-color: yellow; margin-left :25px;'><b>Note :</b><br/></label><label style='font-size: 10px; font-family: tahoma; color: red; background-color: yellow; margin-left :25px;'>A. THIS IS A COMPUTER GENERATED EMAIL.<br/></label><label style='font-size: 10px; font-family: tahoma; color: red; background-color: yellow; margin-left :25px;'>B. THIS INVOICE IS FOR YOUR REFERENCE, NOT FOR SALES TAX FILING.<br/></label><label style='font-size: 10px; font-family: tahoma; color: red; background-color: yellow; margin-left :25px;'>C. ORIGINAL INVOICE IS SEND ALONG WITH THE PARCEL, IF NOT RECEIVED WITHIN 10 DAYS, KINDLY INFORM HESH ACCOUNTS DEPARTMENT @ +91 93821 52555</label>";
			textMsg = textMsg+"<br/><br/><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'><u>Corporate & Registered Office:</u><br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'><b>HESH OPTO-LAB PVT. LTD.</b><br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>141, Broadway,<br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>Chennai - 600 108. India<br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>Email : heshstoreaccounts@hotmail.com<br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>Ph : +91 93821 52555 / 044-4301 2617</label>";
			textMsg = textMsg+"</div></body></html>";
			EmailUtil.sendMail((String[])toEmails.toArray(new String[0]), subject, textMsg);
			msg.addNewMessage(new com.en.model.Message(SUCCESS, "Sales details emailed successfully to <b><i><u>"+toEmails.get(0)+((toEmails.size()>1) ? " and "+ toEmails.get(1) : "")+"</u></i></b>."));
		}
		return msg;
	}

	public static MessageModel sendOrderMail(OrderModel model, EntryModel[] advances, String complete) {
		MessageModel msg = new  MessageModel();
		if(model.getCustomer().getEmail().equals("") && model.getCustomer().getEmail1().equals("")){
			msg.addNewMessage(new com.en.model.Message(ALERT, "For Order No : "+model.getBranch().getBillPrefix()+"-O-"+Utils.padBillNo(model.getOrderId())+", Customer email doesnot exits"));
		} else {
			ArrayList<String> toEmails = new ArrayList<String>(0);
			if(!model.getCustomer().getEmail().equals("")) {
				toEmails.add(model.getCustomer().getEmail());
			}
			if(!model.getCustomer().getEmail1().equals("")) {
				toEmails.add(model.getCustomer().getEmail1());
			}
			String subject = "Details of Order No : "+model.getBranch().getBillPrefix()+"-O-"+Utils.padBillNo(model.getOrderId());
			if(complete.equals("Y")){
				subject = "Order No : "+model.getBranch().getBillPrefix()+"-O-"+Utils.padBillNo(model.getOrderId())+" ready for depatch notice.";
			}
			String taxType = ((model.getTaxtype() == 1) ? "VAT BILL" : ((model.getTaxtype() == 2) ? "CST BILL" : "CST AGAINST FORM 'C' BILL"));
			String payType = (model.getPayType() == 0) ? "0 Days" : ((model.getPayType() == 1) ? "CREDIT" : "EMI") + (((model.getPayType() == 1)) ? " - For "+model.getCredDays()+" Days" : "") + (((model.getPayType() == 2)) ? " - Down Payment: "+model.getDownPay() +" EMI No.: "+model.getEMINo()+" EMI Amt: "+model.getEMIAmt() +" EMI Days: "+model.getEMIDays() : "");
			String textMsg = "<html><body><div style='border: 1px; border-color: black; border-style: double; width: 850px; padding:5px;'><br/>";
			String tableTextMsg = "";
			if(complete.equals("Y")){
				textMsg = textMsg + "Dear Sir/Madam,<br/><br/>Thank you for choosing HESH as one of your vendor.<br/><br/>Your Order No : <b>" +model.getBranch().getBillPrefix()+"-O-"+Utils.padBillNo(model.getOrderId())+"</b>" +
						" is ready for despatch.<br/><br/> ";
			} else {
				textMsg = textMsg + "Dear Sir/Madam,<br/><br/>Thank you for choosing HESH as one of your vendor.<br/><br/> Please find below the order details for Order No : <b>" +model.getBranch().getBillPrefix()+"-O-"+Utils.padBillNo(model.getOrderId())+"</b>" +
					", check the details and confirm the same by replying to this mail.<br/><br/> ";
			}
			tableTextMsg = tableTextMsg + 
					"<center><label style='font-size: 12px; font-family: tahoma; color : red;'><b><u>"+((complete.equals("Y")) ? "ORDER READY FOR DESPATCH NOTICE" : "ORDER FORM")+"</u></b></label></center><br/>"+
					"<table cellpadding='3' border=1 style='width: 800px; font-size: 11px; font-family: tahoma;' align='center'>"+
						"<tr>"+
							"<td colspan='2' valign='top' style='padding-left: 20px; width : 100px;' align='right'>Order No</td>"+
							"<td colspan='2' valign='top' ><b>"+model.getBranch().getBillPrefix()+"-O-"+Utils.padBillNo(model.getOrderId())+"</b></td>"+
							"<td align='right' valign='top'>Date</td>"+
							"<td colspan='2' valign='top'><b>"+Utils.convertToAppDateDDMMYY(model.getOrderDate())+"</b></td>"+
							"<td align='right' valign='top'>Salesman</td>"+
							"<td colspan='2' valign='top'><b>"+model.getSalesman().getUserName()+"</b></td>"+
						"</tr>"+
						"<tr>"+
							"<td colspan='2'  style='padding-left: 20px;' valign='top' align='right'>Customer</td>"+
							"<td colspan='2'><b>"+model.getCustomer().getLabel()+"</b></td>" +
							"<td align='right' valign='top'>Tax Type</td><td colspan='2' valign='top'><b>"+taxType+"</b></td>"+
							"<td align='right' valign='top'>Credit Days</td><td colspan='2' valign='top'><b>"+payType+"</b></td>"+
						"</tr>"+
						"<tr>"+
							"<td colspan='2'  valign='top' style='padding-left: 20px;' align='right'>TIN/CST</td>"+
							"<td colspan='3' valign='top'><b>"+model.getCustomer().getTin() + " / " +model.getCustomer().getCst()+"</b></td>"+
							"<td colspan='2' valign='top' align='right'>Contact Person <br/> Phone No</td>"+
							"<td colspan='3' valign='top'><b>"+model.getCustomer().getContactPerson()+ " <br/> "+model.getCustomer().getPhone1()+"</b></td>"+
						"</tr>"+
						"<tr>"+
							"<td  colspan='2' valign='top' style='padding-left: 20px;' valign='top' align='right'>Delivery Address</td>"+
							"<td colspan='2' valign='top'><b>"+model.getDevAddress()+"</b></td>" +
							"<td align='right' valign='top'>Form No</td><td colspan='2' valign='top'><b>"+model.getFormNo()+"</b></td>"+
							"<td align='right' valign='top'>Delivery Days</td><td colspan='2' valign='top'><b>"+model.getDevDays()+" Days</b></td>"+
						"</tr>"+
						"<tr>"+
							"<td colspan='2'  valign='top' align='right'>Remarks</td><td colspan='8' valign='top'><b>"+model.getRemarks()+"</b></td>"+
						"</tr>";
			double roundOff = 0;
			double stotal = 0;
			double qty = 0;
			double ttotal = 0;
			double total = 0;
			tableTextMsg = tableTextMsg+
						"<tr>" +
							"<th style=\"width : 50px;\">S.No</th>" +
							"<th colspan='2'>Description Of Goods</th>" +
							"<th style=\"width : 50px;\">Qty</th>" +
							"<th style=\"width : 50px;\">Rate<br/>(Rs. )</th>" +
							"<th style=\"width : 50px;\">Discount<br/>(%)</th>" +
							"<th style=\"width : 65px;\">GrossRate<br/>(Rs. )</th>" +
							"<th style=\"width : 50px;\">Tax<br/>(%)</th>" +
							"<th style=\"width : 50px;\">Tax<br/>(Rs. )</th>" +
							"<th style=\"width : 65px;\">Nett Rate<br/>(Rs. )</th>" +
						"</tr>";
			for(int i=0; i<model.getItems().size(); i++){
				tableTextMsg = tableTextMsg +
						"<tr>" +
							"<td align=\"center\">"+(i+1)+"</td>" +
							"<td colspan='2'>"+model.getItems().get(i).getItem().getItemNumber() + " / "+ model.getItems().get(i).getItem().getItemName() + " " + model.getItems().get(i).getDesc()+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getItems().get(i).getQty())+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getItems().get(i).getRate())+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getItems().get(i).getDisrate())+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * model.getItems().get(i).getRate())+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getItems().get(i).getTaxrate())+"%</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100)+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal((((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate())+(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty()*(model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100)))+"</td>" +
						"</tr>";
				qty = qty + (model.getItems().get(i).getQty());
				stotal = stotal + ((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * model.getItems().get(i).getRate();
				ttotal = ttotal + ((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100;
				total = total + ((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate()+((model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100));
			}
			if(model.getInstallation() > 0){
				tableTextMsg = tableTextMsg +
						"<tr>" +
							"<td align=\"right\" colspan='3'>INSTALLATION(+)</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getInstallation())+"</td>" +
						"</tr>";
				total = total + model.getInstallation();
			}
			if(model.getPacking() > 0){
				tableTextMsg = tableTextMsg +
						"<tr>" +
							"<td align=\"right\" colspan='3'>PACKING(+)</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getPacking())+"</td>" +
						"</tr>";
				total = total + model.getPacking();
			} else {
				tableTextMsg = tableTextMsg +
						"<tr>" +
							"<td align=\"right\" colspan='3'>PACKING(+)</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">As Per Actuals</td>" +
						"</tr>";
			}
			if(model.getForwarding() > 0){
				tableTextMsg = tableTextMsg +
						"<tr>" +
							"<td align=\"right\" colspan='3'>FORWARDING(+)</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getForwarding())+"</td>" +
						"</tr>";
				total = total + model.getForwarding();
			} else {
				tableTextMsg = tableTextMsg +
						"<tr>" +
							"<td align=\"right\" colspan='3'>FORWARDING(+)</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">As Per Actuals</td>" +
						"</tr>";
			}
			if(model.getLess() > 0){
				tableTextMsg = tableTextMsg +
						"<tr>" +
							"<td align=\"right\" colspan='3'>LESS(-)</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(model.getLess())+"</td>" +
						"</tr>";
				total = total - model.getLess();
			}
			roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
			if(roundOff != 0){
				tableTextMsg = tableTextMsg +
						"<tr>" +
							"<td align=\"right\" colspan='3'>ROUNDOFF</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td>&nbsp;</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(roundOff)+"</td>" +
						"</tr>";
				total = total + roundOff;
			}
			tableTextMsg = tableTextMsg +
					"<tr>" +
						"<th align=\"right\" colspan='3'>GRAND TOTAL</th>" +
						"<th align=\"center\">"+qty+"</th>" +
						"<th>&nbsp;</th>" +
						"<th>&nbsp;</th>" +
						"<th align=\"center\">"+Utils.get2Decimal(stotal)+"</th>" +
						"<th>&nbsp;</th>" +
						"<th align=\"center\">"+Utils.get2Decimal(ttotal)+"</th>" +
						"<th align=\"center\">"+Utils.get2Decimal(total)+"</th>" +
					"</tr>";
			double advTotal = 0;
			for(int i=0; i<advances.length; i++){
				advTotal = advTotal + Utils.get2DecimalDouble(advances[i].getAmount()+advances[i].getAdjAmt());
			}
			if(advTotal > 0){
				tableTextMsg = tableTextMsg +
						"<tr>" +
							"<th align=\"right\" colspan='3'>ADVANCE (-)</th>" +
							"<th align=\"center\">&nbsp;</th>" +
							"<th>&nbsp;</th>" +
							"<th>&nbsp;</th>" +
							"<th align=\"center\">&nbsp;</th>" +
							"<th>&nbsp;</th>" +
							"<th align=\"center\">&nbsp;</th>" +
							"<th align=\"center\">"+Utils.get2Decimal(advTotal)+"</th>" +
						"</tr>";
				tableTextMsg = tableTextMsg +
						"<tr>" +
							"<th align=\"right\" colspan='3'>TOTAL AMOUNT PAYABLE</th>" +
							"<th align=\"center\">"+qty+"</th>" +
							"<th>&nbsp;</th>" +
							"<th>&nbsp;</th>" +
							"<th align=\"center\">&nbsp;</th>" +
							"<th>&nbsp;</th>" +
							"<th align=\"center\">&nbsp;</th>" +
							"<th align=\"center\">"+Utils.get2Decimal(total-advTotal)+"</th>" +
						"</tr>";
				
			}
			tableTextMsg = tableTextMsg+"</table><br/>";
			tableTextMsg = tableTextMsg+"<center><label style='font-size: 12px; font-family: tahoma; color : red;'><b><u>ADVANCE RECEIVED BREAK-UP DETAILS</u></b></label></center><br/>";
			tableTextMsg = tableTextMsg+
				"<table border=1 style=\"width:800px; font-size: 11px; font-family: tahoma;\"  align='center'>" +
						"<tr>" +
							"<th style=\"width : 60px;\">Date</th>" +
							"<th>Branch</th>" +
							"<th>Paymode</th>" +
							"<th width='60%'>Desc</th>" +
							"<th style=\"width : 65px;\">Amount<br/>(Rs. )</th>" +
						"</tr>";
			if(advances.length==0){
				tableTextMsg = tableTextMsg + "<tr><td colspan = 5 align = center>No Advances paid.</td></tr>";
			} else {
				for(int i=0; i<advances.length; i++){
					tableTextMsg = tableTextMsg +
							"<tr>" +
								"<td align=\"center\">"+Utils.convertToAppDateDDMMYY(advances[i].getEntryDate())+"</td>" +
								"<td align=\"center\">"+advances[i].getBranch().getAccessName()+"</td>" +
								"<td align=\"center\">"+Utils.getEntryType(advances[i].getEntryType())+"</td>" +
								"<td align=\"center\">"+advances[i].getDesc()+"</td>" +
								"<td align=\"center\">"+Utils.get2Decimal(advances[i].getAmount()+advances[i].getAdjAmt())+"</td>" +
							"</tr>";
				}
				tableTextMsg = tableTextMsg + "<tr><th colspan = 4 align = right>Total</th><th>"+Utils.get2Decimal(advTotal)+"</th></tr>";
			}
			tableTextMsg = tableTextMsg + "</table>";
			if(complete.equals("Y")){
				textMsg = textMsg + "Kindly <font style='color : red;'>deposit the balance amount of Rs. "+Utils.get2Decimal((total)-advTotal)+" in the bank</font> details mentioned below.</font><br/><br/>";
			} else {
				if((total*0.5) - advTotal > 500){
					textMsg = textMsg + "Kindly <font style='color : red;'>deposit advance of Rs. "+Utils.get2Decimal((total*0.5)-advTotal)+" in the bank</font> details mentioned below.</font><br/><br/>";
					tableTextMsg = tableTextMsg+"<br/><center><label style='font-size: 12px; font-family: tahoma; color : red;'><b><u>50% ADVANCE REQUIRED DETAILS</u></b></label></center><br/>";
					tableTextMsg = tableTextMsg+
							"<table border=1 style=\"width:800px; font-size: 11px; font-family: tahoma;\"  align='center'>" +
									"<tr>" +
										"<td align=\"center\">50% - Advance Required</td>" +
										"<td align=\"center\">"+Utils.get2Decimal(advTotal*100/total)+"% - Advance Paid</td>" +
										"<td align=\"center\">"+Utils.get2Decimal(50 - (advTotal*100/total))+"% - Advance Payable</td>" +
									"</tr>" +
									"<tr>" +
										"<th>"+Utils.get2Decimal(total*0.5)+"</th>" +
										"<th>"+Utils.get2Decimal(advTotal)+"</th>" +
										"<th>"+Utils.get2Decimal((total*0.5)-advTotal)+"</th>" +
									"</tr>" +
							"</table>";
				}
			}
			textMsg = textMsg + "For further details you can contact <b>"+model.getSalesman().getUserName() +" @ "+model.getSalesman().getMobile1()+"</b>.<br/><br/>";
			textMsg = textMsg + tableTextMsg;
			textMsg = textMsg+"<br/><center><label style='font-size: 12px; font-family: tahoma; color : red;'><b><u>BANK DETAILS</u></b></label></center><br/>";
			textMsg = textMsg+
				"<table border=1 style=\"width:800px; font-size: 11px; font-family: tahoma;\"  align='center'>" +
						"<tr>" +
							"<th>Option 1 - <b>"+model.getBranch().getBankName1()+"</b></th>" +
							"<th>Option 2 - <b>"+model.getBranch().getBankName2()+"</b></th>" +
						"</tr>" +
						"<tr>" +
							"<td>Name : <b>"+TITLE+"</b><br/>Bank Name : <b>"+model.getBranch().getBankName1()+"</b><br/>Bank Branch : <b>"+model.getBranch().getBankBranch1()+"</b><br/>A/C No : <b>"+model.getBranch().getBankAc1()+"</b><br/>Bank IFSC Code : <b>"+model.getBranch().getBankIfsc1()+"</b></td>" +
							"<td>Name : <b>"+TITLE+"</b><br/>Bank Name : <b>"+model.getBranch().getBankName2()+"</b><br/>Bank Branch : <b>"+model.getBranch().getBankBranch2()+"</b><br/>A/C No : <b>"+model.getBranch().getBankAc2()+"</b><br/>Bank IFSC Code : <b>"+model.getBranch().getBankIfsc2()+"</b></td>" +
						"</tr>" +
				"</table>";
			textMsg = textMsg+"<br/><label style='font-size: 15px; font-weight : bold; color : red; font-family: tahoma; margin-left :25px;'>CHEQUE or Draft should be in favour of \"HESH OPTO-LAB PVT. LTD.\"</label><br/>";
			textMsg = textMsg+"<br/><label style='font-size: 10px; font-weight : bold; color : red; font-family: tahoma; margin-left :25px;'>Kindly inform HESH ACCONTS @ 93821 52555 after depositing the payment in the Bank.</label><br/>";
			textMsg = textMsg+"<br/><fieldset><legend style=\"font-size:9.5px; font-family: Calibri;font-weight : bold;\"><u>Terms and Conditions:</u></legend><label style=\"font-size:9.5px; font-family: Calibri;\">1. 50% payment at the time of booking.<br/>" +
					"2. Payments received in reference to the sales contract, does not attract, any interest.<br/>3. All payments should be made by cash / cheque / DD drawn in favour of \"<b>"+Constant.TITLE+"</b>\".<br/>" +
					"4. With the change in model / name and delay in payments, booking priority / delivery dates will be rescheduled.<br/>5. Delivery is subject to realisation of full payment." +
					"6. The expected time of delivery will be ____ days from the date of receipt of full payment subject to availablity of product.<br/>7. Delivery of the product by dealer is subject to force majeure events and is not responsible for the delay if any.<br/>" +
					"8. We are not responsible for any damage in transit.<br/>9. Letter is a must for any change in name / address / model / cancellation.<br/>" +
					"10. 10 working days required for refund subject to the return of original receipt & valid reason.<br/>11. Deduction of any offers in invoice is at the discretion of company.<br/>" +
					"12. Price, taxes and other statutory levies are applicable as prevailing on the date of delivery.<br/>13. Price / specifications are subject to change by the company.<br/>" +
					"14. Disputes if any are subjected to the jurisdiction of the courts of "+model.getBranch().getCity()+".</label></fieldset>";
			textMsg = textMsg+"<br/><br/><table style=\"width:800px; margin-left : 25px; font-size: 11px; font-family: tahoma;\"><tr><td valign='top'><label style='font-size: 10px; font-family: tahoma; '><u>BRANCH ADDRESS:</u><br/></label><label style='font-size: 10px; font-family: tahoma; '><b>HESH OPTO-LAB PVT. LTD.</b><br/></label><label style='font-size: 10px; font-family: tahoma; '>"+model.getBranch().getAddress()+"<br/></label><label style='font-size: 10px; font-family: tahoma; '>Email : "+model.getBranch().getEmail()+"<br/></label><label style='font-size: 10px; font-family: tahoma; '>Ph : "+model.getBranch().getMobile1()+" / "+model.getBranch().getStdcode()+"-"+model.getBranch().getPhone1()+"</label></td>";
			textMsg = textMsg+"<td valign='top'><label style='font-size: 10px; font-family: tahoma;'><u>Corporate & Registered Office:</u><br/></label><label style='font-size: 10px; font-family: tahoma; '><b>HESH OPTO-LAB PVT. LTD.</b></label><br/><label style='font-size: 10px; font-family: tahoma; '>#141, Broadway,</label></br><label style='font-size: 10px; font-family: tahoma; '>Chennai - 600 108. India</label><br/><label style='font-size: 10px; font-family: tahoma; '>Email : heshstoreaccounts@hotmail.com</label></br><label style='font-size: 10px; font-family: tahoma; '>Ph : +91 93821 52555 / 044-4301 2617</label></td></tr></table>";
			textMsg = textMsg+"<br/><label style='font-size: 13px; font-weight : bold; color : black; font-family: tahoma; margin-left :25px;'>THIS IS A COMPUTER GENERATED EMAIL. SIGNATURE NOT REQUIRED.</label><br/>";
			textMsg = textMsg+"</div></body></html>";
//			System.out.println(textMsg);
			EmailUtil.sendMail((String[])toEmails.toArray(new String[0]), subject, textMsg);
			msg.addNewMessage(new com.en.model.Message(SUCCESS, "Order details of Order No : "+model.getBranch().getBillPrefix()+"-O-"+Utils.padBillNo(model.getOrderId())+" emailed successfully to <b><i><u>"+toEmails.get(0)+((toEmails.size()>1) ? " and "+ toEmails.get(1) : "")+"</u></i></b>."));
		}
		return msg;
	}

	public static MessageModel sendOutstandingLstMail(SalesModel[] salesModels, CustomerModel  customer, String[] branches, AccessPointModel[] access) {
		MessageModel msg = new  MessageModel();
		if(customer.getEmail().equals("") && customer.getEmail1().equals("")){
			msg.addNewMessage(new com.en.model.Message(ALERT, "Customer email doesnot exits"));
		} else {
			ArrayList<String> toEmails = new ArrayList<String>(0);
			if(!customer.getEmail().equals("")) {
				toEmails.add(customer.getEmail());
			}
			if(!customer.getEmail1().equals("")) {
				toEmails.add(customer.getEmail1());
			}
			String subject = "Outstanding Details as of "+DateUtil.getCurrDt();
			String textMsg = "<html><body><div style='border: 1px; border-color: black; border-style: double; width: 850px; padding:5px;'><br/>";
			textMsg = textMsg + 
					"<center><label style='font-size: 12px; font-family: tahoma;'><b><u>OUTSTANDING DETAILS</u></b></label></center><br/>"+
					"<table cellpadding='3' border=1 style='width: 800px; font-size: 11px; font-family: tahoma;' align='center'>"+
						"<tr>"+
						"<td style='padding-left: 20px; width:150px;' align='right'>CUSTOMER</td>"+
						"<td>"+customer.getLabel()+"</td>"+
						"</tr>"+
						"<tr>"+
							"<td style='padding-left: 20px; width:150px;' align='right'>BRANCH</td>"+
							"<td><b>";
			for(int i=0;i< branches.length; i++){
				if(branches[i].equals("--")){
					continue;
				}
				for(int k=0; k<access.length; k++){
					if(access[k].getAccessId() == Integer.parseInt(branches[i])){
						textMsg = textMsg + "HESH OPTO-LAB PVT LTD - "+access[k].getCity()+"("+access[k].getAccessName()+")"+",";
						break;
					}
				}
			}
			textMsg = textMsg + "</b></td>"+
						"</tr>"+
					"</table> <br/>";
			double roundOff = 0;
			double gTotal = 0;
			double pendintTotal = 0;
			double paidTotal = 0;
			textMsg = textMsg+
				"<table border=1 style=\"width:800px; font-size: 11px; font-family: tahoma;\"  align='center'>" +
						"<tr>" +
							"<th>BILL NO</th>" +
							"<th>BILL DATE</th>" +
							"<th>TAX TYPE</th>" +
							"<th>DUE DATE</th>" +
							"<th>ORDER TAKEN BY</th>" +
							"<th>TOTAL AMOUNT</th>" +
							"<th>PAY. DATE</th>" +
							"<th>PAY. AMT</th>" +
							"<th>PENDING AMOUNT</th>" +
						"</tr>";
			for(int i=0; i<salesModels.length; i++){
				roundOff = (1-Utils.get2DecimalDouble((salesModels[i].getTotalAmt()+salesModels[i].getInstallation()+salesModels[i].getPacking()+salesModels[i].getForwarding()-salesModels[i].getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((salesModels[i].getTotalAmt()+salesModels[i].getInstallation()+salesModels[i].getPacking()+salesModels[i].getForwarding()-salesModels[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((salesModels[i].getTotalAmt()+salesModels[i].getInstallation()+salesModels[i].getPacking()+salesModels[i].getForwarding()-salesModels[i].getLess())%1)) ;
				gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(salesModels[i].getTotalAmt()+salesModels[i].getInstallation()+salesModels[i].getPacking()+salesModels[i].getForwarding()-salesModels[i].getLess())+roundOff);
				pendintTotal = pendintTotal + ((salesModels[i].getPaymode() == 3) ? Utils.get2DecimalDouble(Utils.get2DecimalDouble(salesModels[i].getTotalAmt()+salesModels[i].getInstallation()+salesModels[i].getPacking()+salesModels[i].getForwarding()-salesModels[i].getLess())+roundOff-Utils.get2DecimalDouble(salesModels[i].getPayAmt())) : 0);
				paidTotal = paidTotal + ((salesModels[i].getPaymode() != 3) ? Utils.get2DecimalDouble(Utils.get2DecimalDouble(salesModels[i].getTotalAmt()+salesModels[i].getInstallation()+salesModels[i].getPacking()+salesModels[i].getForwarding()-salesModels[i].getLess())+roundOff) : Utils.get2DecimalDouble(salesModels[i].getPayAmt()));
				textMsg = textMsg +
						"<tr>" +
							"<td align=\"center\">"+salesModels[i].getBranch().getBillPrefix()+Utils.padBillNo(salesModels[i].getSaleid())+"</td>" +
							"<td align=\"center\">"+Utils.convertToAppDateDDMMYY(salesModels[i].getSalesdate())+"</td>" +
							"<td align=\"center\">"+((salesModels[i].getTaxtype() == 1) ? "VAT" : (salesModels[i].getTaxtype() == 2) ? "CST" : "CST AGAINST FORM 'C'")+"</td>" +
							"<td align=\"center\">"+((salesModels[i].getFollowupCnt() >0) ? Utils.convertToAppDateDDMMYY(salesModels[i].getFollowupDt()) : Utils.convertToAppDateDDMMYY(salesModels[i].getDueDate()))+"</td>" +
							"<td align=\"center\">"+salesModels[i].getSalesman().getUserName()+"</td>" +
							"<td align=\"center\">"+Utils.get2Decimal(Utils.get2DecimalDouble(salesModels[i].getTotalAmt()+salesModels[i].getInstallation()+salesModels[i].getPacking()+salesModels[i].getForwarding()-salesModels[i].getLess())+roundOff)+"</td>" +
							"<td align=\"center\">"+((salesModels[i].getPaymode() != 3) ? Utils.convertToAppDateDDMMYY(salesModels[i].getSalesdate()) : (salesModels[i].getPayDate()!=null && !salesModels[i].getPayDate().equals("0000-00-00")) ? Utils.convertToAppDateDDMMYY(salesModels[i].getPayDate()) : "")+"</td>" +
							"<td align=\"center\">"+((salesModels[i].getPaymode() != 3) ? Utils.get2Decimal(Utils.get2DecimalDouble(salesModels[i].getTotalAmt()+salesModels[i].getInstallation()+salesModels[i].getPacking()+salesModels[i].getForwarding()-salesModels[i].getLess())+roundOff) : Utils.get2Decimal(salesModels[i].getPayAmt()))+"</td>" +
							"<td align=\"center\" style='color:red;'>"+((salesModels[i].getPaymode() == 3 || Utils.get2DecimalDouble(salesModels[i].getTotalAmt()+salesModels[i].getInstallation()+salesModels[i].getPacking()+salesModels[i].getForwarding()-salesModels[i].getLess())+roundOff-salesModels[i].getPayAmt() == 0) ? Utils.get2Decimal(Utils.get2DecimalDouble(salesModels[i].getTotalAmt()+salesModels[i].getInstallation()+salesModels[i].getPacking()+salesModels[i].getForwarding()-salesModels[i].getLess())+roundOff-salesModels[i].getPayAmt()) : "--")+"</td>" +
						"</tr>";
			}
			textMsg = textMsg +
					"<tr>" +
						"<th align=\"right\" colspan=5>GRAND TOTAL(Rs. )</th>" +
						"<th align=\"center\">"+Utils.get2Decimal(gTotal)+"</th>" +
						"<th>--</th>" +
						"<th align=\"center\">"+Utils.get2Decimal(paidTotal)+"</th>" +
						"<th align=\"center\" style='color:red;'>"+Utils.get2Decimal(pendintTotal)+"</th>" +
					"</tr>";
			textMsg = textMsg+"</table><br/>";
			textMsg = textMsg+"<br/><br/><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'><u>Corporate & Registered Office:</u><br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'><b>HESH OPTO-LAB PVT. LTD.</b><br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>141, Broadway,<br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>Chennai - 600 108. India<br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>Email : heshstoreaccounts@hotmail.com<br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>Ph : +91 93821 52555 / 044-4301 2617</label>";
			textMsg = textMsg+"</div></body></html>";
			EmailUtil.sendMail((String[])toEmails.toArray(new String[0]), subject, textMsg);
			msg.addNewMessage(new com.en.model.Message(SUCCESS, "Outstanding details emailed successfully to <b><i><u>"+toEmails.get(0)+((toEmails.size()>1) ? " and "+ toEmails.get(1) : "")+"</u></i></b>."));
		}
		return msg;
	}

	public static MessageModel emailLedger(EntryModel[] entries,
			CustomerModel customerDtls, String fromDate, String toDate) {
		MessageModel msg = new  MessageModel();
		if(customerDtls.getEmail().equals("") && customerDtls.getEmail1().equals("")){
			msg.addNewMessage(new com.en.model.Message(ALERT, "Customer email doesnot exits"));
		} else {
			ArrayList<String> toEmails = new ArrayList<String>(0);
			if(!customerDtls.getEmail().equals("")) {
				toEmails.add(customerDtls.getEmail());
			}
			if(!customerDtls.getEmail1().equals("")) {
				toEmails.add(customerDtls.getEmail1());
			}
			String subject = "LEDGER : "+customerDtls.getCustomerName()+" - "+customerDtls.getArea()+" - "+customerDtls.getCity()+" FROM : "+fromDate+" TO :"+toDate;
			String textMsg = "<html><body><div style='border: 1px; border-color: black; border-style: double; width: 850px; padding:5px;border-spacing: 1px;'><br/>";
			textMsg = textMsg + 
					"<center><label style='font-size: 12px; font-family: tahoma;'><b><u>LEDGER</u></b></label></center><br/>"+
					"<center><label style='font-size: 12px; font-family: tahoma;'><b><u>HESH OPTO-LAB PVT. LTD.</u></b></label></center><br/>"+
					"<center><label style='font-size: 12px; font-family: tahoma;'><b><u>"+customerDtls.getLabel()+"</u></b></label></center><br/>"+
					"<center><label style='font-size: 12px; font-family: tahoma;'><b><u>"+fromDate+" TO "+toDate+"</u></b></label></center><br/>";
			textMsg = textMsg+
				"<table border=1 style=\"width:100%; font-size: 11px; font-family: tahoma;\"  align='center'>" +
						"<tr>" +
							"<th style=\"width : 50px;\">DATE</th>" +
							"<th>PARTICULARS</th>" +
							"<th style=\"width : 120px;\">DR.</th>" +
							"<th style=\"width : 120px;\">CR.</th>" +
							"<th style=\"width : 100px;\">BALANCE</th>" +
						"</tr>";
			double crTotal = 0;
			double drTotal = 0;
			double oTotal = 0;
			for(int i=0; i<entries.length; i++){
				if(entries[i].getCrdr().equals("D")) {
					crTotal += entries[i].getAmount();
					oTotal = oTotal + entries[i].getAmount();
				} else { 
					drTotal += entries[i].getAmount();
					oTotal = oTotal - entries[i].getAmount();
				}
				textMsg = textMsg +
						"<tr>" +
							"<td align=\"center\">"+(Utils.convertToAppDateDDMMYY(entries[i].getEntryDate()))+"</td>" +
							"<td>"+entries[i].getLedgerDesc()+"</td>" +
							"<td align=\"center\">"+((entries[i].getCrdr().equals("D")) ? Utils.get2Decimal(entries[i].getAmount()) : "--")+"</td>" +
							"<td align=\"center\">"+((entries[i].getCrdr().equals("C")) ? Utils.get2Decimal(entries[i].getAmount()) : "--")+"</td>" +
							"<td align=\"center\">"+((oTotal > 0) ? Utils.get2Decimal(oTotal)+"Dr." : Utils.get2Decimal(0-oTotal)+"Cr.")+"</td>" +
						"</tr>";
			}
			textMsg = textMsg +
					"<tr>" +
					"<td align=\"center\">"+(Utils.convertToAppDateDDMMYY(Utils.convertToSQLDate(toDate)))+"</td>" +
					"<td>"+("Closing Balance -"+ ((oTotal > 0) ? "Receivable" : "Payable"))+"</td>" +
					"<td align=\"center\">"+(Utils.get2Decimal(crTotal))+"</td>" +
					"<td align=\"center\">"+(Utils.get2Decimal(drTotal))+"</td>" +
					"<td align=\"center\">"+((oTotal > 0) ? Utils.get2Decimal(oTotal)+"Dr." : Utils.get2Decimal(0-oTotal)+"Cr.")+"</td>" +
				"</tr>";
			textMsg = textMsg+"</table><br/>";
			textMsg = textMsg+"<table border=1 align=center style='font-family: tahoma; font-size: 14px; font-weight: bold; margin-top: 10px;' cellspacing=0>" +
						"<tr>"+
							"<td align=center style='padding: 3px;'>Total ("+((oTotal > 0) ? "Receivable" : "Payable")+ ") : " + ((oTotal > 0) ? Utils.get2Decimal(oTotal) : Utils.get2Decimal(0-oTotal))+"</label></td>" +
						"</tr>"+
						"</table>";
			textMsg = textMsg+"<br/><br/><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'><u>Corporate & Registered Office:</u><br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'><b>HESH OPTO-LAB PVT. LTD.</b><br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>141, Broadway,<br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>Chennai - 600 108. India<br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>Email : heshstoreaccounts@hotmail.com<br/></label><label style='font-size: 10px; font-family: tahoma; margin-left :25px;'>Ph : +91 93821 52555 / 044-4301 2617</label>";
			textMsg = textMsg+"</div></body></html>";
			EmailUtil.sendMail((String[])toEmails.toArray(new String[0]), subject, textMsg);
			msg.addNewMessage(new com.en.model.Message(SUCCESS, "Sales details emailed successfully to <b><i><u>"+toEmails.get(0)+((toEmails.size()>1) ? " and "+ toEmails.get(1) : "")+"</u></i></b>."));
		}
		return msg;
	}

	public static MessageModel sendQuotationMail(QuotationModel model) {
		MessageModel msg = new  MessageModel();
		if(model.getCustomer().getEmail().equals("") && model.getCustomer().getEmail1().equals("")){
			msg.addNewMessage(new com.en.model.Message(ALERT, "Customer email doesnot exits"));
		} else {
			ArrayList<String> toEmails = new ArrayList<String>(0);
			if(!model.getCustomer().getEmail().equals("")) {
				toEmails.add(model.getCustomer().getEmail());
			}
			if(!model.getCustomer().getEmail1().equals("")) {
				toEmails.add(model.getCustomer().getEmail1());
			}
			createQuotationPDF(model);
			try {
				Session mailSession = getAccountSession();
				 
				Message message = new MimeMessage(mailSession);
				message.setFrom(new InternetAddress("heshstoreaccounts@hotmail.com"));
				InternetAddress[] toAdd = new InternetAddress[toEmails.size()];
				for(int i=0; i<toEmails.size(); i++){
					toAdd[i] = new InternetAddress(toEmails.get(i).toLowerCase());
				}
				message.setRecipients(Message.RecipientType.TO, toAdd);
				message.setRecipient(Message.RecipientType.CC, new InternetAddress("heshstorebhavin@hotmail.com"));
				message.setSubject("Quotation Details For "+model.getBranch().getBillPrefix()+"Q"+Utils.padBillNo(model.getQuotationId()));
	 
				BodyPart messageBodyPart = new MimeBodyPart();

		        messageBodyPart.setText("Dear Sir,\r\n\r\nPlease find attached the pdf file of Quotation No: "+model.getBranch().getBillPrefix()+"Q"+Utils.padBillNo(model.getQuotationId())+"\r\n\r\nFor further details feel free to call Mr."+model.getSalesman().getUserName()+" @ "+model.getSalesman().getMobile1()+"\r\n\r\nCorporate & Registered Office:\r\nHESH STORE\r\nHESH OPTO-LAB PVT. LTD.\r\n141, Broadway,\r\nChennai - 600 108. India\r\nEmail : heshstoreaccounts@hotmail.com\r\nPh : +91 93821 52555 / 044-4301 2617");

		        Multipart multipart = new MimeMultipart();

		        multipart.addBodyPart(messageBodyPart);

		        messageBodyPart = new MimeBodyPart();

//		        DataSource source = new FileDataSource("F:\\HESH_SOFTWARE_FILES\\QUOTATION_PDF\\" + model.getBranch().getBillPrefix()+"Q"+Utils.padBillNo(model.getQuotationId())+".pdf");
		        DataSource source = new FileDataSource("E:\\HESH_SOFTWARE_FILES\\QUOTATION_PDF\\" + model.getBranch().getBillPrefix()+"Q"+Utils.padBillNo(model.getQuotationId())+".pdf");

		        messageBodyPart.setDataHandler(new DataHandler(source));

		        messageBodyPart.setFileName(model.getBranch().getBillPrefix()+"Q"+Utils.padBillNo(model.getQuotationId())+".pdf");

		        multipart.addBodyPart(messageBodyPart);

		        message.setContent(multipart);
				
				Transport.send(message);
				
				msg.addNewMessage(new com.en.model.Message(SUCCESS, "Quotation details emailed successfully to <b><i><u>"+toEmails.get(0)+((toEmails.size()>1) ? " and "+ toEmails.get(1) : "")+"</u></i></b>."));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return msg;
	}

	private static void createQuotationPDF(QuotationModel model){
		String imgDir = "E:\\HESH_SOFTWARE_FILES\\";
//		String imgDir = "F:\\HESH_SOFTWARE_FILES\\";
		
		try {
			File dir = new File(imgDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			imgDir = imgDir+"QUOTATION_PDF\\";
			dir = new File(imgDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			String saveFile = imgDir + model.getBranch().getBillPrefix()+"Q"+Utils.padBillNo(model.getQuotationId())+".pdf";
			new File(saveFile);
			
			Document document = new Document();

			PdfWriter.getInstance(document,
			        new FileOutputStream(saveFile));
			document.open();
			
			document.setMargins(0.1f, 0.1f, 0.1f, 0.1f);
			
			Paragraph p1 = new Paragraph("QUOTATION", new
					 Font(FontFamily.HELVETICA, 10, Font.UNDERLINE));
			p1.setAlignment(Element.ALIGN_CENTER);
			p1.setSpacingAfter(0);
			
			document.add(p1);
			
			Paragraph p2 = new Paragraph("ORIGINAL COPY", new
					 Font(FontFamily.HELVETICA, 10));
			p2.setAlignment(Element.ALIGN_CENTER);
			p2.setSpacingAfter(0);
			p2.setSpacingBefore(0);
			document.add(p2);
			
			PdfPTable t1 = new PdfPTable(4);
			t1.setWidthPercentage(100);
			t1.setWidths(new float[]{1,7,1,7});
			
			PdfPCell t1r1c1 = new PdfPCell(new Phrase("VAT : "+ model.getBranch().getVat()));
			t1r1c1.setBorderColor(BaseColor.WHITE);
			t1r1c1.setColspan(2);
			t1r1c1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			t1.addCell(t1r1c1);

			PdfPCell t1r1c2 = new PdfPCell(new Phrase("CST : "+ model.getBranch().getCst()));
			t1r1c2.setBorderColor(BaseColor.WHITE);
			t1r1c2.setColspan(2);
			t1r1c2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			t1.addCell(t1r1c2);
			
			PdfPCell t1r2c1 = new PdfPCell(new Phrase("HESH OPTO-LAB PVT. LTD.", new Font(FontFamily.HELVETICA,16,1)));
			t1r2c1.setBorderColor(BaseColor.WHITE);
			t1r2c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			t1r2c1.setColspan(4);
			t1.addCell(t1r2c1);
			
			String address = model.getBranch().getAddress();
			while(address.indexOf("\r\n") != -1){
				address = address.substring(0,address.indexOf("\r\n")) + " " + address.substring(address.indexOf("\r\n")+2);
			}
			
			PdfPCell t1r3c1 = new PdfPCell(new Phrase(address, new Font(FontFamily.HELVETICA,9)));
			t1r3c1.setBorderColor(BaseColor.WHITE);
			t1r3c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			t1r3c1.setColspan(4);
			t1.addCell(t1r3c1);
			
			PdfPCell t1r4c1 = new PdfPCell(new Phrase("Phone No : "+  model.getBranch().getStdcode() + " - " +model.getBranch().getPhone1() + "/" +model.getBranch().getPhone2()+" Email : "+model.getBranch().getEmail(), new Font(FontFamily.HELVETICA,8)));
			t1r4c1.setBorderColor(BaseColor.WHITE);
			t1r4c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			t1r4c1.setColspan(4);
			t1.addCell(t1r4c1);
			
			PdfPCell t1r5c1 = new PdfPCell(new Phrase("Quotation No : "+ model.getBranch().getBillPrefix()+"Q"+Utils.padBillNo(model.getQuotationId()), new Font(FontFamily.HELVETICA,8)));
			t1r5c1.setBorderColor(BaseColor.WHITE);
			t1r5c1.setColspan(2);
			t1r5c1.setPaddingTop(10);
			t1r5c1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			t1.addCell(t1r5c1);

			PdfPCell t1r5c2 = new PdfPCell(new Phrase("Date : "+ Utils.convertToAppDate(model.getQuotationdate()), new Font(FontFamily.HELVETICA,8)));
			t1r5c2.setBorderColor(BaseColor.WHITE);
			t1r5c2.setColspan(2);
			t1r5c1.setPaddingTop(10);
			t1r5c2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			t1.addCell(t1r5c2);
			
			PdfPCell t1r6c1 = new PdfPCell(new Phrase("Buyer : ", new Font(FontFamily.HELVETICA,8)));
			t1r6c1.setBorderColor(BaseColor.WHITE);
			t1r6c1.setPaddingTop(10);
			t1r6c1.setRowspan(6);
			t1r6c1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			t1.addCell(t1r6c1);
			
			PdfPCell t1r6c2 = new PdfPCell(new Phrase(model.getCustomer().getCustomerName()+"\r\n"+model.getCustomer().getAddress()+"\r\n"+model.getCustomer().getCity() + (model.getCustomer().getPincode().equals("") ? "" : " - "+model.getCustomer().getPincode())+"\r\n"+model.getCustomer().getState(), new Font(FontFamily.HELVETICA,8)));
			t1r6c2.setBorderColor(BaseColor.WHITE);
			t1r6c2.setPaddingTop(10);
			t1r6c2.setRowspan(6);
			t1r6c2.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			t1.addCell(t1r6c2);
			
			PdfPCell t1r6c3 = new PdfPCell(new Phrase("Quotation Valid Till : "+Utils.convertToAppDateDDMMYY(model.getValidDate()), new Font(FontFamily.HELVETICA,8)));
			t1r6c3.setBorderColor(BaseColor.WHITE);
			t1r6c3.setPaddingTop(10);
			t1r6c3.setColspan(2);
			t1r6c3.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			t1.addCell(t1r6c3);
			
			PdfPCell t1r7c3 = new PdfPCell(new Phrase("Buyer Person : "+model.getCustomer().getContactPerson(), new Font(FontFamily.HELVETICA,8)));
			t1r7c3.setBorderColor(BaseColor.WHITE);
			t1r7c3.setColspan(2);
			t1r7c3.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			t1.addCell(t1r7c3);
			
			PdfPCell t1r8c3 = new PdfPCell(new Phrase("Buyer Phone : "+model.getCustomer().getStdcode()+"-"+model.getCustomer().getPhone1()+"/"+model.getCustomer().getPhone2(), new Font(FontFamily.HELVETICA,8)));
			t1r8c3.setBorderColor(BaseColor.WHITE);
			t1r8c3.setColspan(2);
			t1r8c3.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			t1.addCell(t1r8c3);
			
			PdfPCell t1r9c3 = new PdfPCell(new Phrase("Buyer Mobile : "+model.getCustomer().getMobile1()+"/"+model.getCustomer().getMobile2(), new Font(FontFamily.HELVETICA,8)));
			t1r9c3.setBorderColor(BaseColor.WHITE);
			t1r9c3.setColspan(2);
			t1r9c3.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			t1.addCell(t1r9c3);
			
			PdfPCell t1r10c3 = new PdfPCell(new Phrase("Sales Representative : "+model.getSalesman().getUserName(), new Font(FontFamily.HELVETICA,8)));
			t1r10c3.setBorderColor(BaseColor.WHITE);
			t1r10c3.setColspan(2);
			t1r10c3.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			t1.addCell(t1r10c3);
			
			PdfPCell t1r11c3 = new PdfPCell(new Phrase("Sales Representative Mobile : "+model.getSalesman().getMobile1(), new Font(FontFamily.HELVETICA,8)));
			t1r11c3.setBorderColor(BaseColor.WHITE);
			t1r11c3.setColspan(2);
			t1r11c3.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			t1.addCell(t1r11c3);
			
			document.add(t1);
			
			PdfPTable t2 = new PdfPTable(9);
			
			t2.setWidthPercentage(100);

			t2.setWidths(new float[]{1,4.5f,1,1,1,1,1,1,1.5f});
	        t2.setSpacingBefore(10);
			PdfPCell c1,c2,c3,c4,c5,c6,c7,c8,c9 = null;
			c1 =  new PdfPCell(new Phrase("S No.", new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
			c1.setBackgroundColor(BaseColor.GRAY);
			c1.setBorderColor(BaseColor.BLACK);
			c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			t2.addCell(c1);
			c2 =  new PdfPCell(new Phrase("Description of Goods", new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
			c2.setBackgroundColor(BaseColor.GRAY);
			c2.setBorderColor(BaseColor.BLACK);
			c2.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			t2.addCell(c2);
			c3 =  new PdfPCell(new Phrase("Qty", new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
			c3.setBackgroundColor(BaseColor.GRAY);
			c3.setBorderColor(BaseColor.BLACK);
			c3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c3.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			t2.addCell(c3);
			c4 =  new PdfPCell(new Phrase("Rate(Rs. )", new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
			c4.setBackgroundColor(BaseColor.GRAY);
			c4.setBorderColor(BaseColor.BLACK);
			c4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c4.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			t2.addCell(c4);
			c5 =  new PdfPCell(new Phrase("Dis.(%)", new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
			c5.setBackgroundColor(BaseColor.GRAY);
			c5.setBorderColor(BaseColor.BLACK);
			c5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c5.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			t2.addCell(c5);
			c6 =  new PdfPCell(new Phrase("Gross Rate (Rs. )", new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
			c6.setBackgroundColor(BaseColor.GRAY);
			c6.setBorderColor(BaseColor.BLACK);
			c6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c6.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			t2.addCell(c6);
			c7 =  new PdfPCell(new Phrase("Tax (%)", new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
			c7.setBackgroundColor(BaseColor.GRAY);
			c7.setBorderColor(BaseColor.BLACK);
			c7.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c7.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			t2.addCell(c7);
			c8 =  new PdfPCell(new Phrase("Tax (Rs. )", new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
			c8.setBackgroundColor(BaseColor.GRAY);
			c8.setBorderColor(BaseColor.BLACK);
			c8.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c8.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			t2.addCell(c8);
			c9 =  new PdfPCell(new Phrase("Nett Rate (Rs. )", new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
			c9.setBackgroundColor(BaseColor.GRAY);
			c9.setBorderColor(BaseColor.BLACK);
			c9.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			c9.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			t2.addCell(c9);
			
			double total = 0;
			double stotal = 0;
			double ttotal = 0;
			double qty = 0;
			int o=1;
			
			for(int i=0; i<model.getItems().size(); i++){
				o++;
				c1 =  new PdfPCell(new Phrase((i+1)+"", new Font(FontFamily.HELVETICA,8)));
				c1.setBorderColor(BaseColor.BLACK);
				c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				t2.addCell(c1);
				c2 =  new PdfPCell(new Phrase(model.getItems().get(i).getItem().getItemNumber() + " / "+ model.getItems().get(i).getItem().getItemName() + " " +  (model.getItems().get(i).getDesc().equals("--") ? "" : model.getItems().get(i).getDesc()), new Font(FontFamily.HELVETICA,8)));
				c2.setBorderColor(BaseColor.BLACK);
				c2.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				t2.addCell(c2);
				c3 =  new PdfPCell(new Phrase(model.getItems().get(i).getQty()+"", new Font(FontFamily.HELVETICA,8)));
				c3.setBorderColor(BaseColor.BLACK);
				c3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				t2.addCell(c3);
				c4 =  new PdfPCell(new Phrase(Utils.get2Decimal(model.getItems().get(i).getRate()), new Font(FontFamily.HELVETICA,8)));
				c4.setBorderColor(BaseColor.BLACK);
				c4.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c4);
				c5 =  new PdfPCell(new Phrase(Utils.get2Decimal(model.getItems().get(i).getDisrate())+"%", new Font(FontFamily.HELVETICA,8)));
				c5.setBorderColor(BaseColor.BLACK);
				c5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				t2.addCell(c5);
				c6 =  new PdfPCell(new Phrase(Utils.get2Decimal(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * model.getItems().get(i).getRate()), new Font(FontFamily.HELVETICA,8)));
				c6.setBorderColor(BaseColor.BLACK);
				c6.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c6);
				c7 =  new PdfPCell(new Phrase(Utils.get2Decimal(model.getItems().get(i).getTaxrate())+"%", new Font(FontFamily.HELVETICA,8)));
				c7.setBorderColor(BaseColor.BLACK);
				c7.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				t2.addCell(c7);
				c8 =  new PdfPCell(new Phrase(Utils.get2Decimal(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100), new Font(FontFamily.HELVETICA,8)));
				c8.setBorderColor(BaseColor.BLACK);
				c8.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c8);
				c9 =  new PdfPCell(new Phrase(Utils.get2Decimal(((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate()+((model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100))), new Font(FontFamily.HELVETICA,8)));
				c9.setBorderColor(BaseColor.BLACK);
				c9.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c9);
				qty = qty + (model.getItems().get(i).getQty());
				stotal = stotal + ((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * model.getItems().get(i).getRate();
				ttotal = ttotal + ((100-model.getItems().get(i).getDisrate())/100)*model.getItems().get(i).getQty() * (model.getItems().get(i).getRate() * model.getItems().get(i).getTaxrate())/100;
			}
			
			total = stotal+ttotal;
			
			double roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.5) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
			if(model.getPacking() > 0 || model.getForwarding() > 0 || model.getInstallation() > 0 || model.getLess() > 0 || roundOff != 0){
				c1 =  new PdfPCell(new Phrase("SUB-TOTAL", new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
				c1.setBorderColor(BaseColor.BLACK);
				c1.setColspan(8);
				c1.setBackgroundColor(BaseColor.GRAY);
				c1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c1);
				c2 =  new PdfPCell(new Phrase(Utils.get2Decimal(total), new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
				c2.setBorderColor(BaseColor.BLACK);
				c2.setColspan(8);
				c2.setBackgroundColor(BaseColor.GRAY);
				c2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c2);
				o++;
			}

			double temp = 0;
			
			if(model.getInstallation() > 0 || model.getPacking() > 0 || model.getForwarding() > 0){
				String tempStr = "";
				if(model.getInstallation() > 0){
					total = Utils.get2DecimalDouble(total) + model.getInstallation();
					temp = temp + model.getInstallation();
					tempStr = tempStr + "INSTALLATION (Rs. "+Utils.get2Decimal(model.getInstallation())+")";
				}
				if(model.getPacking() > 0){
					total = Utils.get2DecimalDouble(total) + model.getPacking();
					temp = temp + model.getPacking();
					tempStr = tempStr + (temp > 0 ? " + " : "") + "PACKING (Rs. "+Utils.get2Decimal(model.getPacking())+")";
				}
				if(model.getForwarding() > 0){
					total = Utils.get2DecimalDouble(total) + model.getForwarding();
					temp = temp + model.getForwarding();
					tempStr = tempStr + (temp > 0 ? " + " : "") + "FORWARDING (Rs. "+Utils.get2Decimal(model.getForwarding())+")";
				}
				c1 =  new PdfPCell(new Phrase(tempStr, new Font(FontFamily.HELVETICA,8)));
				c1.setBorderColor(BaseColor.BLACK);
				c1.setColspan(8);
				c1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c1);
				c2 =  new PdfPCell(new Phrase(Utils.get2Decimal(temp), new Font(FontFamily.HELVETICA,8)));
				c2.setBorderColor(BaseColor.BLACK);
				c2.setColspan(8);
				c2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c2);
				o++;
			} else {
				String tempStr = "";
				tempStr = tempStr + "INSTALLATION " + " PACKING " + " FORWARDING";
				c1 =  new PdfPCell(new Phrase(tempStr, new Font(FontFamily.HELVETICA,8)));
				c1.setBorderColor(BaseColor.BLACK);
				c1.setColspan(8);
				c1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c1);
				c2 =  new PdfPCell(new Phrase("AS PER ACTUALS", new Font(FontFamily.HELVETICA,8)));
				c2.setBorderColor(BaseColor.BLACK);
				c2.setColspan(8);
				c2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c2);
				o++;
			}
			

			roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
		
			if(roundOff != 0.00){
				c1 =  new PdfPCell(new Phrase("ROUND OFF", new Font(FontFamily.HELVETICA,8)));
				c1.setBorderColor(BaseColor.BLACK);
				c1.setColspan(8);
				c1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c1);
				c2 =  new PdfPCell(new Phrase(Utils.get2Decimal(roundOff), new Font(FontFamily.HELVETICA,8)));
				c2.setBorderColor(BaseColor.BLACK);
				c2.setColspan(8);
				c2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c2);
				o++;
			}
			
			c1 =  new PdfPCell(new Phrase("GRAND TOTAL", new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
			c1.setBorderColor(BaseColor.BLACK);
			c1.setColspan(8);
			c1.setBackgroundColor(BaseColor.GRAY);
			c1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			t2.addCell(c1);
			c2 =  new PdfPCell(new Phrase(Utils.get2Decimal(total), new Font(FontFamily.HELVETICA,8,1,BaseColor.WHITE)));
			c2.setBorderColor(BaseColor.BLACK);
			c2.setColspan(8);
			c2.setBackgroundColor(BaseColor.GRAY);
			c2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			t2.addCell(c2);
			o++;
			
			for(int k=0 ; k<25-o; k++){
				c1 =  new PdfPCell(new Phrase("    ", new Font(FontFamily.HELVETICA,8,1,BaseColor.BLACK)));
				c1.setBorderColor(BaseColor.WHITE);
				c1.setColspan(9);
				c1.setBackgroundColor(BaseColor.WHITE); 
				c1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				t2.addCell(c1);
			}
			
			document.add(t2);
			
			PdfPTable t3 = new PdfPTable(3);
			t3.setWidthPercentage(100);
			t3.setSpacingAfter(10);
			t3.setWidths(new float[]{1,1,1});
			
			PdfPCell t3r1c1 = new PdfPCell(new Phrase("PAYMENT TERMS : (Cheque / demand drafts should be in favour of \"HESH OPTO-LAB PVT LTD\".)", new Font(FontFamily.HELVETICA,10,1,BaseColor.BLACK)));
			t3r1c1.setColspan(3);
			t3r1c1.setBorderColor(BaseColor.BLACK);
			t3r1c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			t3.addCell(t3r1c1);
			
			PdfPCell t3r2c1 = new PdfPCell(new Phrase("Advance 50%", new Font(FontFamily.HELVETICA,10,1,BaseColor.WHITE)));
			t3r2c1.setBorderColor(BaseColor.BLACK);
			t3r2c1.setBackgroundColor(BaseColor.GRAY);
			t3r2c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			t3.addCell(t3r2c1);
			
			PdfPCell t3r2c2 = new PdfPCell(new Phrase("Before Shipment 40%", new Font(FontFamily.HELVETICA,10,1,BaseColor.WHITE)));
			t3r2c2.setBorderColor(BaseColor.BLACK);
			t3r2c2.setBackgroundColor(BaseColor.GRAY);
			t3r2c2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			t3.addCell(t3r2c2);
			
			PdfPCell t3r2c3 = new PdfPCell(new Phrase("Against Delivery 10%", new Font(FontFamily.HELVETICA,10,1,BaseColor.WHITE)));
			t3r2c3.setBorderColor(BaseColor.BLACK);
			t3r2c3.setBackgroundColor(BaseColor.GRAY);
			t3r2c3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			t3.addCell(t3r2c3);
			
			PdfPCell t3r3c1 = new PdfPCell(new Phrase("Rs. "+Utils.get2Decimal(total*0.5), new Font(FontFamily.HELVETICA,10,1,BaseColor.BLACK)));
			t3r3c1.setBorderColor(BaseColor.BLACK);
			t3r3c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			t3.addCell(t3r3c1);
			
			PdfPCell t3r3c2 = new PdfPCell(new Phrase("Rs. "+Utils.get2Decimal(total*0.4), new Font(FontFamily.HELVETICA,10,1,BaseColor.BLACK)));
			t3r3c2.setBorderColor(BaseColor.BLACK);
			t3r3c2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			t3.addCell(t3r3c2);
			
			PdfPCell t3r3c3 = new PdfPCell(new Phrase("Rs. "+Utils.get2Decimal(total*0.1), new Font(FontFamily.HELVETICA,10,1,BaseColor.BLACK)));
			t3r3c3.setBorderColor(BaseColor.BLACK);
			t3r3c3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			t3.addCell(t3r3c3);
			
			PdfPCell t3r4c1 =  new PdfPCell(new Phrase("Remarks: "+model.getRemarks(), new Font(FontFamily.HELVETICA,8)));
			t3r4c1.setColspan(3);
			t3r4c1.setBorderColor(BaseColor.BLACK);
			t3r4c1.setPaddingBottom(10);
			t3r4c1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			t3.addCell(t3r4c1);

			document.add(t3);
			
			document.add(new Phrase("TERMS & CONDITIONS:", new Font(FontFamily.HELVETICA,8,1)));
			
			List lst = new List(false,10);
			
			lst.add(new ListItem("PRICE: Prices are Exclusive of Sales Tax, Octroi and Other Statutory variations in the rate of Sales Tax and other government levies, if any, will be extra to your account.", new Font(FontFamily.HELVETICA,6)));
			lst.add(new ListItem("VALIDITY:The validity of the quotation can change from offer to offer.", new Font(FontFamily.HELVETICA,6)));
			lst.add(new ListItem("TERMS OF PAYMENT:50% in advance by means of cash/cheque/demand draft and balance 40% prior to dispatch Against Performa invoice and balance 10% against installation. Cheque / demand drafts should be in favour of \"HESH OPTO-LAB PVT LTD\".", new Font(FontFamily.HELVETICA,6)));
			lst.add(new ListItem("TRANSPORTATION:Freight charges will be charged extra and packing charges extra.", new Font(FontFamily.HELVETICA,6)));
			lst.add(new ListItem("DELIVERY:Most of the orders can normally be made / supplied within 15-20 days of Receipt of your firm order with advance payment. As agreed upon.", new Font(FontFamily.HELVETICA,6)));
			lst.add(new ListItem("WARRANTY:All goods are guaranteed / warranty by the manufacturer against manufacturing defects arising from material or workmanship for a period of 6 - 12 months (Differs from product to product) for a basic unit excluding wear & tear, belts, stone, body and transportation from the date of invoice. The warranty is applicable only if the equipment / display are used in the way prescribed by the manufacturer. No accidental damages to any part of the machine or its accessories will be covered under warranty.", new Font(FontFamily.HELVETICA,6)));
			lst.add(new ListItem("LIABILITIES:Except as otherwise provided explicitly herein above, we shall not be liable for any loss or damaged what so ever or however caused arising out of or connected with this contract.", new Font(FontFamily.HELVETICA,6)));
			lst.add(new ListItem("EXEMPTION:We shall not be responsible for failure in performing our obligations if such non performance is due to reasons beyond our control.", new Font(FontFamily.HELVETICA,6)));
			lst.add(new ListItem("AGREEMENT:The foregoing terms and conditions shall prevail not withstanding any variation contained in any document received from the customer unless such variation has been specifically agreed upon in writing by HESH STORE, \"HESH OPTO-LAB PVT LTD\".", new Font(FontFamily.HELVETICA,6)));
			
			document.add(lst);

			PdfPTable t4 = new PdfPTable(1);
			t4.setWidthPercentage(100);
			t4.setSpacingBefore(10);
			
			PdfPCell t4r1c1 = new PdfPCell(new Phrase("This is a computer generated Quotation, Seal and Signature not required.", new Font(FontFamily.HELVETICA,10,1)));
			t4r1c1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			t4r1c1.setBorderColor(BaseColor.WHITE);
			t4.addCell(t4r1c1);

			document.add(t4);
			
			document.close();
	        	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void createRetailPriceListPDF(HashMap<String,HashMap<String,ItemModel[]>> items, AccessPointModel access, String version){
		String imgDir = "E:\\HESH_SOFTWARE_FILES\\";
//		String imgDir = "F:\\HESH_SOFTWARE_FILES\\";
		Document document = new Document();
		
		try {
			File dir = new File(imgDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			imgDir = imgDir+"PRICE_LIST_PDF\\";
			dir = new File(imgDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			String saveFile = imgDir + "hesh_retail_price_list_ver_"+version+".pdf";
			new File(saveFile);

	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        
			PdfWriter writer = PdfWriter.getInstance(document,
				        baos);
			document.open();			
			
			StringBuffer plString = new StringBuffer();
			
			plString.append("<table width=\"98%\" style=\"height:500px;page-break-after:always;\"><tr><td align=\"right\" style=\"height:350px\">&nbsp;</td></tr><tr><td align=\"right\" style=\"background-color:black; color:white; padding:15px;\"><h1>HESH OPTO-LAB PVT LTD</h1> <br/> <h3>RETAIL PRICE LIST</h3><br/> <h5>VERSION "+version+"</h5></td></tr></table>");
			
			
			String[] groupArr = (String[])items.keySet().toArray(new String[0]);
			Arrays.sort(groupArr);
			HashMap<String,ItemModel[]> tempMap = null;
			String gName = "", cName = "";
			if(access.getAccessId() > 10){
				for(int j=0; j<groupArr.length; j++){
					gName = groupArr[j];
					tempMap = items.get(gName);
					String[] catArr = (String[])tempMap.keySet().toArray(new String[0]);
					Arrays.sort(catArr);
					for(int k=0; k<catArr.length; k++){
						cName = catArr[k];
						plString.append("<table width=\"98%\" cellpadding=\"2\" cellspacing=\"0\" style=\"font-size: 9px; margin: 10px; font-family: tahoma;\">");
						plString.append("<tr><th align=left colspan=\"6\" style=\"font-size: 13px; font-style: italic;\">"+cName.replace("&", "&amp;")+"</th><th colspan=\"2\" style=\"font-size: 10px;\" align=\"right\">"+gName.replace("&", "&amp;")+"</th></tr>");
						plString.append("<tr style=\"color: white; background-color: black;\">");
						plString.append("<th align=center width=\"10%\">ITEM ID</th>");
						plString.append("<th align=center width=\"15%\">ITEM NUMBER</th>"); 
						plString.append("<th align=center colspan=\"2\">ITEM NAME</th>");
						plString.append("<th align=center width=\"10%\">RATE</th>");
						plString.append("<th align=center width=\"10%\">TAX %</th>");
						plString.append("<th align=center width=\"10%\">TAX</th>");
						plString.append("<th align=center width=\"10%\">NETT</th>");
						plString.append("</tr>");
						ItemModel[] item = (ItemModel[]) tempMap.get(cName);
						for(int i=0; i<item.length; i++){
							plString.append("<tr style=\"background-color: "+((i%2==0) ? "white" : "silver")+"; padding 2px;\">");	
							plString.append("<td align=\"center\" style=\"padding: 3px;\">"+item[i].getItemId()+"</td>");
							plString.append("<td align=\"center\" style=\"padding: 3px;\">"+item[i].getItemNumber()+"</td>");
							plString.append("<td align=\"center\" colspan=\"2\" style=\"padding: 3px;\">"+item[i].getItemName().replace("&", "&amp;")+"</td>");
							plString.append("<td align=\"center\" style=\"padding: 3px;\">Rs. "+((Double.parseDouble(item[i].getItemPrice()) == 0) ? "***" : item[i].getItemPrice())+"</td>");
							plString.append("<td align=\"center\" style=\"padding: 3px;\">"+((Double.parseDouble(item[i].getTax()) == 0) ? "***" : item[i].getTax())+" %</td>");
							plString.append("<td align=\"center\" style=\"padding: 3px;\">Rs. "+((Double.parseDouble(item[i].getItemPrice()) == 0) ? "***" : Utils.get2Decimal(Double.parseDouble(item[i].getItemPrice())*Double.parseDouble(item[i].getTax())/100))+"</td>");
							plString.append("<td align=\"center\" style=\"padding: 3px;\">Rs. "+((Double.parseDouble(item[i].getItemPrice()) == 0) ? "***" : Utils.get2Decimal(Double.parseDouble(item[i].getItemPrice())*(100+Double.parseDouble(item[i].getTax()))/100))+"</td>");
							plString.append("</tr>");
						} 
						plString.append("</table>");
					}
				}
			} else {
				for(int j=0; j<groupArr.length; j++){
					gName = groupArr[j];
					tempMap = items.get(gName);
					String[] catArr = (String[])tempMap.keySet().toArray(new String[0]);
					Arrays.sort(catArr);
					for(int k=0; k<catArr.length; k++){
						cName = catArr[k];
						plString.append("<table width=\"98%\" cellpadding=\"2\" cellspacing=\"0\" style=\"font-size: 9px; margin: 10px; font-family: tahoma;\">");
						plString.append("<tr><th align=left colspan=\"3\" style=\"font-size: 13px; font-style: italic;\">"+cName.replace("&", "&amp;")+"</th><th colspan=\"2\" style=\"font-size: 10px;\" align=\"right\">"+gName.replace("&", "&amp;")+"</th></tr>");
						plString.append("<tr style=\"color: white; background-color: black;\">");
						plString.append("<th align=center width=\"10%\">ITEM ID</th>");
						plString.append("<th align=center width=\"15%\">ITEM NUMBER</th>"); 
						plString.append("<th align=center colspan=\"2\">ITEM NAME</th>");
						plString.append("<th align=center width=\"10%\">MRP<br/><font style=\"font-size: 8px;\">(TAXES EXTRA)</font></th>");
						plString.append("</tr>");
						ItemModel[] item = (ItemModel[]) tempMap.get(cName);
						for(int i=0; i<item.length; i++){
							plString.append("<tr style=\"background-color: "+((i%2==0) ? "white" : "silver")+"; padding 2px;\">");	
							plString.append("<td align=\"center\" style=\"padding: 3px;\">"+item[i].getItemId()+"</td>");
							plString.append("<td align=\"center\" style=\"padding: 3px;\">"+item[i].getItemNumber()+"</td>");
							plString.append("<td align=\"center\" colspan=\"2\" style=\"padding: 3px;\">"+item[i].getItemName().replace("&", "&amp;")+"</td>");
							plString.append("<td align=\"center\" style=\"padding: 3px;\">Rs. "+((Double.parseDouble(item[i].getItemPrice()) == 0) ? "***" : item[i].getItemPrice())+"</td>");
							plString.append("</tr>");
						} 
						plString.append("</table>");
					}
				}
			}

			plString = addLastPage(plString, version.substring(6)+"/"+version.substring(3,5)+"/"+version.substring(0,2));
			
			document.newPage();
			
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(plString.toString()));
			
			document.close();
	        
			PdfReader reader = new PdfReader(baos.toByteArray());
	        // Create a stamper
	        PdfStamper stamper
	            = new PdfStamper(reader, new FileOutputStream(saveFile));
	        // Loop over the pages and add a header to each page
	        int n = reader.getNumberOfPages();
	        for (int i = 1; i <= n; i++) {
	        	getRSPFooterTable(i, n, version).writeSelectedRows(
	                    0, -1, 30, 830, stamper.getOverContent(i));
	        }
	        // Close the stamper
	        stamper.close();
	        reader.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
			document.close();
		}
		
	}
	
	public static PdfPTable getRSPFooterTable(int x, int y, String version) {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(527);
        table.setLockedWidth(true);
        
        PdfPTable table1 = new PdfPTable(2);
        table1.setTotalWidth(527);
        table1.setLockedWidth(true);
        
        PdfPCell cell1 = new PdfPCell(new Phrase("RETAIL PRICE LIST VERSION "+version, new Font(FontFamily.HELVETICA,8,1)));
        cell1.setBorderColor(BaseColor.WHITE);
        cell1.setBorder(0);
        table1.addCell(cell1);
        
        cell1 = new PdfPCell(new Phrase("HESH OPTO-LAB PVT LTD", new Font(FontFamily.HELVETICA,8,1)));
        cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1.setBorderColor(BaseColor.WHITE);
        cell1.setBorder(0);
        table1.addCell(cell1);
        
        cell1 = new PdfPCell(table1);
        
        table.addCell(cell1);
        
        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(PdfPCell.RECTANGLE);
        cell2.setBorderColor(BaseColor.BLACK);
        cell2.setFixedHeight(795);
        table.addCell(cell2);
        PdfPCell cell = new PdfPCell(new Phrase(String.format("Page %d of %d", x, y), new Font(FontFamily.HELVETICA,8,1)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorderColorBottom(BaseColor.WHITE);
        cell.setBorderColorLeft(BaseColor.WHITE);
        cell.setBorderColorRight(BaseColor.WHITE);
        cell.setBorderColorTop(BaseColor.BLACK);
        table.addCell(cell);
        return table;
    }

	public static void createWholesalesPriceListPDF(
			HashMap<String, HashMap<String, ItemModel[]>> items, String version) {
		String imgDir = "E:\\HESH_SOFTWARE_FILES\\";
//		String imgDir = "F:\\HESH_SOFTWARE_FILES\\";
		Document document = new Document();
		
		try {
			File dir = new File(imgDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			imgDir = imgDir+"PRICE_LIST_PDF\\";
			dir = new File(imgDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			String saveFile = imgDir + "hesh_wholesales_price_list_ver_"+version+".pdf";
			new File(saveFile);

	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        
			PdfWriter writer = PdfWriter.getInstance(document,
				        baos);
			document.open();			
			
			StringBuffer plString = new StringBuffer();
			
			plString.append("<table width=\"98%\" style=\"height:500px;page-break-after:always;\"><tr><td align=\"right\" style=\"height:350px\">&nbsp;</td></tr><tr><td align=\"right\" style=\"background-color:black; color:white; padding:15px;\"><h1>HESH OPTO-LAB PVT LTD</h1> <br/> <h3>WHOLESALES PRICE LIST</h3></td></tr></table>");
			
			
			String[] groupArr = (String[])items.keySet().toArray(new String[0]);
			Arrays.sort(groupArr);
			HashMap<String,ItemModel[]> tempMap = null;
			String gName = "", cName = "";
			for(int j=0; j<groupArr.length; j++){
				gName = groupArr[j];
				tempMap = items.get(gName);
				String[] catArr = (String[])tempMap.keySet().toArray(new String[0]);
				Arrays.sort(catArr);
				for(int k=0; k<catArr.length; k++){
					cName = catArr[k];
					plString.append("<table width=\"98%\" cellpadding=\"2\" cellspacing=\"0\" style=\"font-size: 9px; margin: 10px; font-family: tahoma;\">");
					plString.append("<tr><th align=left colspan=\"3\" style=\"font-size: 13px; font-style: italic;\">"+cName+"</th><th colspan=\"2\" style=\"font-size: 10px;\" align=\"right\">"+gName+"</th></tr>");
					plString.append("<tr style=\"color: white; background-color: black;\">");
					plString.append("<th align=center width=\"10%\">ITEM ID</th>");
					plString.append("<th align=center width=\"15%\">ITEM NUMBER</th>"); 
					plString.append("<th align=center colspan=\"2\">ITEM NAME</th>");
					plString.append("<th align=center width=\"10%\">WSP<br/><font style=\"font-size: 8px;\">(TAXES EXTRA)</font></th>");
					plString.append("</tr>");
					ItemModel[] item = (ItemModel[]) tempMap.get(cName);
					for(int i=0; i<item.length; i++){
						plString.append("<tr style=\"background-color: "+((i%2==0) ? "white" : "silver")+"; padding 2px;\">");	
						plString.append("<td align=\"center\" style=\"padding: 3px;\">"+item[i].getItemId()+"</td>");
						plString.append("<td align=\"center\" style=\"padding: 3px;\">"+item[i].getItemNumber()+"</td>");
						plString.append("<td align=\"center\" colspan=\"2\" style=\"padding: 3px;\">"+item[i].getItemName()+"</td>");
						plString.append("<td align=\"center\" style=\"padding: 3px;\">Rs. "+((Double.parseDouble(item[i].getItemPrice()) == 0) ? "***" : item[i].getItemWPrice())+"</td>");
						plString.append("</tr>");
					} 
					plString.append("</table>");
				}
			}

			plString = addLastPage(plString, version.substring(6)+"/"+version.substring(3,5)+"/"+version.substring(0,2));
			
			document.newPage();
			
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(plString.toString()));
			
			document.close();
	        
			PdfReader reader = new PdfReader(baos.toByteArray());
	        // Create a stamper
	        PdfStamper stamper
	            = new PdfStamper(reader, new FileOutputStream(saveFile));
	        // Loop over the pages and add a header to each page
	        int n = reader.getNumberOfPages();
	        for (int i = 1; i <= n; i++) {
	        	getWSPFooterTable(i, n).writeSelectedRows(
	                    0, -1, 30, 830, stamper.getOverContent(i));
	        }
	        // Close the stamper
	        stamper.close();
	        reader.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
			document.close();
		}
		
	}
	
	private static StringBuffer addLastPage(StringBuffer plString, String date) {
		plString.append("<table width=\"98%\" border=1 cellspacing=0 style=\"page-break-before:always;\"><tr><td valign=\"top\" style=\"font-size: 9px; margin: 10px; font-family: tahoma;color:white;background-color:black;padding:5px; height: 20px;\">NOTES:</td></tr><tr><td style=\"height:420px;\"> </td></tr></table>");
		plString.append("<table width=\"98%\" style=\"font-size: 9px; margin: 10px; font-family: tahoma;margin-top:25px;\"><tr><td align=\"center\" style=\"color:white;background-color:black;padding:5px;\" colspan=\"2\">CUSTOMER CARE T : +91 9384052555 | E : heshstoreNEWS@hotmail.com</td></tr></table>");
		plString.append("<table width=\"98%\" style=\"font-size: 9px; margin: 10px; font-family: tahoma;margin-top:10px;\"><tr><td align=\"center\" style=\"color:white;background-color:black;padding:5px;\" colspan=\"2\">TERMS &amp; CONDITIONS</td></tr><tr><td colspan=\"2\" style=\"font-size: 7px;\">1. Price list valid from "+date+"<br/> 2. All payments should be made by cash / cheque / draft drawn in favour of "+TITLE+". Our price is ex-showroom. ");
		plString.append("<br/>3. 30% in advance by means of cash / cheque / demand draft and balance 50% prior to dispatch against Per-forma invoice and balance 20% against delivery / installation. <br/>4. Sales tax extra as per the state government law.");
		plString.append("<br/>5. Price, taxes and other statutory levies are applicable as prevailing on the date of delivery. <br/>6. Packing and forwarding will be charged extra.");
		plString.append("<br/>7. Installation and fixing charges extra. <br/>8. Company holds the right to alter, change or withdraw the product without prior notice.");
		plString.append("<br/>9. Prices are subject to change by the company with out any prior notice. <br/>10. All goods are guaranteed / warranty by the manufacturer against manufacturing defects arising from material or workmanship for a period of 6 - 12 months (differs from product to product) for a basic unit excluding wear & tear, belts, stone, body and transportation from the date of invoice. The warranty is applicable only if the equipment / displays are used in the way prescribed by the manufacturer. No accidental damages to any part of the machine or its accessories will be covered under warranty.</td></tr></table>");
		plString.append("<table width=\"98%\" style=\"font-size: 9px; margin: 10px; font-family: tahoma;margin-top:25px;\"><tr><td align=\"center\" style=\"color:white;background-color:black;padding:5px;\">CORPORATE OFFICE</td></tr><tr><td align=\"center\" style=\"background-color:silver;padding:5px;\">HESH OPTO-LAB PVT. LTD.<br/># 141, BROADWAY, CHENNAI - 600108 INDIA<br/>T :044 43012617 | 09884052555 | heshstoreBHAVIN@hotmail.com</td></tr></table>");
		plString.append("<table width=\"98%\" style=\"font-size: 9px; margin: 10px; font-family: tahoma;margin-top:25px;\"><tr><td align=\"center\" style=\"color:white;background-color:black;padding:5px;\" colspan=\"2\">SHOWROOM &amp; SALES OFFICE</td></tr>" +
				"<tr><td style=\"padding-top:5px; font-weight:bolder;\">KERALA OFFICE</td><td style=\"padding-top:5px; font-weight:bolder;\">TAMIL NADU OFFICE</td></tr><tr><td valign=\"top\" style=\"background-color:silver;padding:5px;\">HESH OPTO-LAB PVT. LTD.<br/># 40/4290, 1ST FLOOR,<br/>KALLEPPURAM CHAMBERS,<br/>JEWS ST, ERNAKULAM - 35<br/>T : 0484 4022555 | 095262 52555<br/>E : heshstoreERNAKULAM@hotmail.com</td><td valign=\"top\" style=\"background-color:silver;padding:5px;\">HESH OPTO-LAB PVT. LTD.<br/># 3, PHILIPS STREET, (BROADWAY),<br/>NEAR BANK OF MAHARASTRA,<br/>CHENNAI - 1<br/>T : 044 42042555 | 093818 52555<br/>E : heshstoreCHENNAI@hotmail.com</td></tr>" +
				"<tr><td style=\"padding-top:5px; font-weight:bolder;\">KARNATAKA OFFICE</td><td style=\"padding-top:5px; font-weight:bolder;\">ANDHRA PRADESH OFFICE</td></tr><tr><td valign=\"top\" style=\"background-color:silver;padding:5px;\">HESH OPTO-LAB PVT. LTD.<br/># 12, M.M.LANE,<br/>20TH CROSS RD, KILARI RD,<br/>BANGALORE - 53<br/>T : 080 41146226 | 098869 52555<br/>E : heshstoreBANGALORE@hotmail.com</td><td valign=\"top\" style=\"background-color:silver;padding:5px;\">HESH OPTO-LAB PVT. LTD.<br/># 4-4-883, KANDASAMY LANE,<br/>SULTHAN BAZAR,<br/>HYDERABAD - 95<br/>T : 040 40218333 | 099667 52555<br/>E : heshstoreHYDERABAD@hotmail.com</td></tr>" +
				"<tr><td style=\"padding-top:5px; font-weight:bolder;\">MAHARASTRA OFFICE</td><td style=\"padding-top:5px; font-weight:bolder;\">FACTORY</td></tr><tr><td valign=\"top\" style=\"background-color:silver;padding:5px;\">HESH OPTO-LAB PVT. LTD.<br/># 381, KALBADEVI ROAD,<br/>C-17 1ST FLOOR NAROTTAM WADI,<br/>MUMBAI - 02<br/>T : 022 65659575 | 093219 52555<br/>E : heshstoreMUMBAI@hotmail.com</td><td valign=\"top\" style=\"background-color:silver;padding:5px;\">HESH OPTO-LAB PVT. LTD.<br/># 154, VISHNU NAGAR, GRAND LYON,<br/>NEAR DON BOSCO SCHOOL, REDHILLS,<br/>CHENNAI - 52<br/>T : 093814 52555<br/>E : heshstoreBC@hotmail.com</td></tr></table>");
		return plString;
	}

	private static PdfPTable getWSPFooterTable(int x, int y) {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(527);
        table.setLockedWidth(true);
        PdfPCell cell1 = new PdfPCell(new Phrase("HESH OPTO-LAB PVT LTD - WHOLESALES PRICE LIST", new Font(FontFamily.HELVETICA,8,1)));
        cell1.setBorderColorTop(BaseColor.WHITE);
        cell1.setBorderColorLeft(BaseColor.WHITE);
        cell1.setBorderColorRight(BaseColor.WHITE);
        cell1.setBorderColorBottom(BaseColor.BLACK);
        table.addCell(cell1);
        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(PdfPCell.RECTANGLE);
        cell2.setBorderColor(BaseColor.BLACK);
        cell2.setFixedHeight(795);
        table.addCell(cell2);
        PdfPCell cell = new PdfPCell(new Phrase(String.format("Page %d of %d", x, y), new Font(FontFamily.HELVETICA,8,1)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorderColorBottom(BaseColor.WHITE);
        cell.setBorderColorLeft(BaseColor.WHITE);
        cell.setBorderColorRight(BaseColor.WHITE);
        cell.setBorderColorTop(BaseColor.BLACK);
        table.addCell(cell);
        return table;
    }

	public static MessageModel sendItemCatalogue(CustomerModel model, ItemModel item, UserModel user) {
		MessageModel msgModel = new MessageModel();
		
		try {
			
			Session mailSession = getNewsSession();

			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress("heshstorenews@hotmail.com"));
			ArrayList<InternetAddress> toAdd = new ArrayList<InternetAddress>(0);
			
			if(!model.getEmail().equals(""))
				toAdd.add(new InternetAddress(model.getEmail()));
			if(!model.getEmail1().equals(""))
				toAdd.add(new InternetAddress(model.getEmail1()));
			message.setRecipients(Message.RecipientType.TO, (InternetAddress[])toAdd.toArray(new InternetAddress[0]));
			message.setSubject("HESH Catalogue of "+item.getItemNumber() +" - "+item.getItemName());
			
			MimeBodyPart messageBodyPart = 
				      new MimeBodyPart();

		    //fill message
		    messageBodyPart.setText("Dear Sir/Madam, \n\nThis is with reference to our conversation, Please find attach " +
		    		"the PDF file of our brochure for your reference. We hope, this will give you idea about our product.\n\n" +
		    		"If you have any querry about our product, Please feel free to call Mr."+user.getUserName()+" at "+user.getMobile1()+"."+
		    		"\n\nHope to hear from you with a favourable reply." +
		    		"\n\nThanks and regards,\n"+TITLE);

		    Multipart multipart = new MimeMultipart();
		    multipart.addBodyPart(messageBodyPart);

		    // Part two is attachment 
		    messageBodyPart = new MimeBodyPart();
		    DataSource source = 
		      new FileDataSource("webapps//HESH_ITEM_CATALOGUE//"+item.getItemId()+".pdf");
//		    DataSource source = 
//		      new FileDataSource("C://Users//Bhavin//Desktop//FreeCharge.pdf");
		    messageBodyPart.setDataHandler(
		      new DataHandler(source));
		    messageBodyPart.setFileName("hesh_catalogue_"+item.getItemNumber()+"_"+item.getItemName()+".pdf");
		    multipart.addBodyPart(messageBodyPart);

		    // Put parts in message
		    message.setContent(multipart);
 
	        Transport.send(message);

	        msgModel.addNewMessage(new com.en.model.Message(SUCCESS, "Email Sent Successfully!!!"));
	        
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return msgModel;
	}

	public static MessageModel sendCustomerAttMail(CustomerEmailModel model) {
		MessageModel msgModel = new MessageModel();
		
		try {
			
			Session mailSession = getGmailSession();

			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress("heshstoreprint@gmail.com","LOGO APPROVAL - HESH STORE"));
			ArrayList<InternetAddress> toAdd = new ArrayList<InternetAddress>(0);
			
			if(!model.getCustomer().getEmail().equals(""))
				toAdd.add(new InternetAddress(model.getCustomer().getEmail()));
			if(!model.getCustomer().getEmail1().equals(""))
				toAdd.add(new InternetAddress(model.getCustomer().getEmail1()));
			message.setRecipients(Message.RecipientType.TO, (InternetAddress[])toAdd.toArray(new InternetAddress[0]));
			message.setRecipient(Message.RecipientType.CC, new InternetAddress("heshstoreajit@hotmail.com"));
			message.setSubject(model.getSubject()+" :: Reference No : "+model.getEmailNo());
			
			MimeBodyPart messageBodyPart = 
				      new MimeBodyPart();

		    //fill message
		    messageBodyPart.setText(model.getMessage());

		    Multipart multipart = new MimeMultipart();
		    multipart.addBodyPart(messageBodyPart);

		    // Part two is attachment
		    messageBodyPart = new MimeBodyPart();
		    DataSource source = 
		      new FileDataSource("webapps//CUSTOMER_MAIL_FILES//"+model.getFilename());
		    messageBodyPart.setDataHandler(
		      new DataHandler(source));
		    messageBodyPart.setFileName(model.getFilename());
		    multipart.addBodyPart(messageBodyPart);

		    // Put parts in message
		    message.setContent(multipart);
 
	        Transport.send(message);

	        msgModel.addNewMessage(new com.en.model.Message(SUCCESS, "Email Sent Successfully!!! Email Reference No :: "+model.getEmailNo()));
	        
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return msgModel;
	}

	public static MessageModel sendCustomerMail(CustomerEmailModel model) {
		MessageModel msgModel = new MessageModel();
		
		try {
			
			Session mailSession = getGmailSession();

			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress("heshstoreprint@gmail.com","Hesh Print"));
			ArrayList<InternetAddress> toAdd = new ArrayList<InternetAddress>(0);
			
			if(!model.getCustomer().getEmail().equals(""))
				toAdd.add(new InternetAddress(model.getCustomer().getEmail()));
			if(!model.getCustomer().getEmail1().equals(""))
				toAdd.add(new InternetAddress(model.getCustomer().getEmail1()));
			message.setRecipients(Message.RecipientType.TO, (InternetAddress[])toAdd.toArray(new InternetAddress[0]));
			message.setSubject(model.getSubject()+" :: Reference No : "+model.getEmailNo());
			message.setText(model.getMessage());
 
	        Transport.send(message);
 
	        msgModel.addNewMessage(new com.en.model.Message(SUCCESS, "Email Sent Successfully!!! Email Reference No :: "+model.getEmailNo()));
	        
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return msgModel;
	}

	public static void sendLREmail(SalesModel model) {
		try {
			
			if(!model.getCustomer().getEmail().equals("") || !model.getCustomer().getEmail1().equals("")){
				Session mailSession = getNewsSession();
	
				Message message = new MimeMessage(mailSession);
				message.setFrom(new InternetAddress("heshstorenews@hotmail.com"));
				ArrayList<InternetAddress> toAdd = new ArrayList<InternetAddress>(0);
				
				if(!model.getCustomer().getEmail().equals(""))
					toAdd.add(new InternetAddress(model.getCustomer().getEmail()));
				if(!model.getCustomer().getEmail1().equals(""))
					toAdd.add(new InternetAddress(model.getCustomer().getEmail1()));
				message.setRecipients(Message.RecipientType.TO, (InternetAddress[])toAdd.toArray(new InternetAddress[0]));
				message.setSubject("LR DETAILS FOR SALES BILL NO :: "+model.getBranch().getBillPrefix() + Utils.padBillNo(model.getSaleid()));
	
			    //fill message
			    message.setContent("Dear Sir/Madam, <br/><br/>Please find below the LR details for the Invoice No :: " + model.getBranch().getBillPrefix() + Utils.padBillNo(model.getSaleid())+
			    		"<br/><br/>Despatched Through : "+model.getTransport()+
			    		"<br/>LR No : "+model.getLrno()+
			    		"<br/>LR Dt : "+model.getLrdt()+
			    		"<br/>No. Of Ctns : "+model.getCtns()+
			    		"<br/><br/>Thanks and regards,<br/>"+TITLE, "text/html; charset=ISO-8859-1");
	
		        Transport.send(message);
			}

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return;
	}

	public static Object sendPriceList(CustomerModel model, String filename, UserModel user) {
		MessageModel msgModel = new MessageModel();
		
		try {
			
			Session mailSession = getNewsSession();

			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress("heshstorenews@hotmail.com"));
			ArrayList<InternetAddress> toAdd = new ArrayList<InternetAddress>(0);
			
			if(!model.getEmail().equals(""))
				toAdd.add(new InternetAddress(model.getEmail()));
			if(!model.getEmail1().equals(""))
				toAdd.add(new InternetAddress(model.getEmail1()));
			message.setRecipients(Message.RecipientType.TO, (InternetAddress[])toAdd.toArray(new InternetAddress[0]));
			message.setSubject("PRICE LIST AS ON "+Utils.convertToAppDateDDMMYY(DateUtil.getCurrDt()));
			
			MimeBodyPart messageBodyPart = 
				      new MimeBodyPart();

		    //fill message
		    messageBodyPart.setText("Dear Sir/Madam, \n\nPlease find attached the current pricelist.\n\nFor further details please contact "+user.getUserName()+ " @ "+ user.getMobile1()+"."+
		    		"\n\nThanks and regards,\n"+TITLE);

		    Multipart multipart = new MimeMultipart();
		    multipart.addBodyPart(messageBodyPart);

		    // Part two is attachment 
		    messageBodyPart = new MimeBodyPart();
		    DataSource source = 
		      new FileDataSource("E:\\HESH_SOFTWARE_FILES\\PRICE_LIST_PDF\\"+filename);
//		    DataSource source = 
//		      new FileDataSource("C://Users//Bhavin//Desktop//FreeCharge.pdf");
		    messageBodyPart.setDataHandler(
		      new DataHandler(source));
		    messageBodyPart.setFileName(filename);
		    multipart.addBodyPart(messageBodyPart);

		    // Put parts in message
		    message.setContent(multipart);
 
	        Transport.send(message);

	        msgModel.addNewMessage(new com.en.model.Message(SUCCESS, "Email Sent Successfully!!!"));
	        
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return msgModel;
	}
	
}