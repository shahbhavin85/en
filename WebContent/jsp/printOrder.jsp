<%@page import="com.en.model.EntryModel"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.util.Constant"%>
<%@page import="com.en.model.OrderModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	OrderModel order = (request.getAttribute(Constant.FORM_DATA) != null) ? (OrderModel) request
			.getAttribute(Constant.FORM_DATA) : new OrderModel();
	EntryModel[] advances = (EntryModel[]) request
			.getAttribute("advances");
	String taxType = ((order.getTaxtype() == 1) ? "VAT BILL" : ((order
			.getTaxtype() == 2) ? "CST BILL"
			: "CST AGAINST FORM 'C' BILL"));
	String payType = (order.getPayType() == 0) ? "0 Days" : ((order
			.getPayType() == 1) ? "CREDIT" : "EMI")
			+ (((order.getPayType() == 1)) ? " - For "
					+ order.getCredDays() + " Days" : "")
			+ (((order.getPayType() == 2)) ? " - Down Payment: "
					+ order.getDownPay() + " EMI No.: "
					+ order.getEMINo() + " EMI Amt: "
					+ order.getEMIAmt() + " EMI Days: "
					+ order.getEMIDays() : "");
%>
<html>
<body onload="window.print();">
	<div style="width: 100%;" align="center">
		<label style='font-size: 12px; font-family: tahoma; color: red; font-weight: bold; text-decoration: underline;'><%=((String)request.getAttribute("complete")).equals("Y") ? " ORDER READY FOR DESPATCH NOTICE " : " ORDER FORM " %></label>
	</div>
	<br />
	<table cellpadding=3 border=1 style='width: 800px; font-size: 11px; font-family: tahoma;' align='center'>
		<tr>
			<td colspan='2' valign='top' style='padding-left: 20px; width: 100px;' align='right'>Order No</td>
			<td colspan='2' valign='top'>
				<b><%=order.getBranch().getBillPrefix()%>-O-<%=Utils.padBillNo(order.getOrderId())%></b>
			</td>
			<td align='right' valign='top'>Date</td>
			<td colspan='2' valign='top'>
				<b><%=Utils.convertToAppDateDDMMYY(order.getOrderDate())%></b>
			</td>
			<td align='right' valign='top'>Salesman<br/>Mobile</td>
			<td colspan='2' valign='top'>
				<b><%=order.getSalesman().getUserName()%></b>
				<br/><b><%=order.getSalesman().getMobile1()%></b>
			</td>
		</tr>
		<tr>
			<td colspan='2' style='padding-left: 20px;' valign='top' align='right'>Customer</td>
			<td colspan='2'>
				<b><%=order.getCustomer().getLabel()%></b>
			</td>
			<td align='right' valign='top'>Tax Type</td>
			<td colspan='2' valign='top'>
				<b><%=taxType%></b>
			</td>
			<td align='right' valign='top'>Credit Days</td>
			<td colspan='2' valign='top'>
				<b><%=payType%></b>
			</td>
		</tr>
		<tr>
			<td colspan='2' valign='top' style='padding-left: 20px;' align='right'>TIN/CST</td>
			<td colspan='3' valign='top'>
				<b><%=order.getCustomer().getTin()%> / <%=order.getCustomer().getCst()%></b>
			</td>
			<td colspan='2' valign='top' align='right'>
				Contact Person
				<br />
				Phone No
			</td>
			<td colspan='3' valign='top'>
				<b><%=order.getCustomer().getContactPerson()%> <br /> <%=order.getCustomer().getMobile1()%></b>
			</td>
		</tr>
		<tr>
			<td colspan='2' valign='top' style='padding-left: 20px;' valign='top' align='right'>Delivery Address</td>
			<td colspan='2' valign='top'>
				<b><%=order.getDevAddress()%></b>
			</td>
			<td align='right' valign='top'>Form No</td>
			<td colspan='2' valign='top'>
				<b><%=order.getFormNo()%></b>
			</td>
			<td align='right' valign='top'>Delivery Days</td>
			<td colspan='2' valign='top'>
				<b><%=order.getDevDays()%> Days</b>
			</td>
		</tr>
		<tr>
			<td colspan='2' valign='top' align='right'>Remarks</td>
			<td colspan='8' valign='top'>
				<b><%=order.getRemarks()%></b>
			</td>
		</tr>
		<tr>
			<th style="width: 50px;">S.No</th>
			<th colspan='2'>Description Of Goods</th>
			<th style="width: 50px;">Qty</th>
			<th style="width: 50px;">
				Rate
				<br />
				(Rs.)
			</th>
			<th style="width: 50px;">
				Discount
				<br />
				(%)
			</th>
			<th style="width: 65px;">
				GrossRate
				<br />
				(Rs.)
			</th>
			<th style="width: 50px;">
				Tax
				<br />
				(%)
			</th>
			<th style="width: 50px;">
				Tax
				<br />
				(Rs.)
			</th>
			<th style="width: 65px;">
				Nett Rate
				<br />
				(Rs.)
			</th>
		</tr>
		<%
			double roundOff = 0;
			double stotal = 0;
			double qty = 0;
			double ttotal = 0;
			double total = 0;
			SalesItemModel[] items = (SalesItemModel[]) order.getItems()
					.toArray(new SalesItemModel[0]);
			for (int i = 0; i < items.length; i++) {
		%>
		<tr>
			<td align="center"><%=(i + 1)%></td>
			<td colspan='2'><%=items[i].getItem().getItemNumber()%>
				/
				<%=items[i].getItem().getItemName()%>
				<%=items[i].getDesc()%></td>
			<td align="center"><%=Utils.get2Decimal(items[i].getQty())%></td>
			<td align="center"><%=Utils.get2Decimal(items[i].getRate())%></td>
			<td align="center"><%=Utils.get2Decimal(items[i].getDisrate())%></td>
			<td align="center"><%=Utils.get2Decimal(((100 - items[i].getDisrate()) / 100)
						* items[i].getQty() * items[i].getRate())%></td>
			<td align="center"><%=Utils.get2Decimal(items[i].getTaxrate())%>%
			</td>
			<td align="center"><%=Utils.get2Decimal(((100 - items[i].getDisrate()) / 100)
						* items[i].getQty()
						* (items[i].getRate() * items[i].getTaxrate()) / 100)%></td>
			<td align="center"><%=Utils.get2Decimal((((100 - items[i].getDisrate()) / 100)
						* items[i].getQty() * (items[i].getRate()) + (((100 - items[i]
						.getDisrate()) / 100)
						* items[i].getQty()
						* (items[i].getRate() * items[i].getTaxrate()) / 100)))%></td>
		</tr>
		<%
			qty = qty + (items[i].getQty());
				stotal = stotal + ((100 - items[i].getDisrate()) / 100)
						* items[i].getQty() * items[i].getRate();
				ttotal = ttotal + ((100 - items[i].getDisrate()) / 100)
						* items[i].getQty()
						* (items[i].getRate() * items[i].getTaxrate()) / 100;
				total = total
						+ ((100 - items[i].getDisrate()) / 100)
						* items[i].getQty()
						* (items[i].getRate() + ((items[i].getRate() * items[i]
								.getTaxrate()) / 100));
			}
			total = total + order.getInstallation() + order.getPacking()
					+ order.getForwarding();
			roundOff = (1 - Utils.get2DecimalDouble(total % 1) < 0.51) ? Utils
					.get2DecimalDouble(1 - Utils.get2DecimalDouble(total % 1))
					: -Utils.get2DecimalDouble(Utils
							.get2DecimalDouble(total % 1));
			total = total + roundOff;
			if (order.getInstallation() > 0) {
		%>
		<tr>
			<td align="right" colspan='3'>INSTALLATION(+)</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td align="center"><%=order.getInstallation()%></td>
		</tr>
		<%
			}
		%>
		<tr>
			<td align="right" colspan='3'>PACKING(+)</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td align="center"><%=(order.getPacking() > 0) ? order.getPacking()
					: "As Per Actuals"%></td>
		</tr>
		<tr>
			<td align="right" colspan='3'>FORWARDING(+)</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td align="center"><%=(order.getForwarding() > 0) ? order.getForwarding()
					: "As Per Actuals"%></td>
		</tr>
		<%
			if (order.getInstallation() > 0) {
		%>
		<tr>
			<td align="right" colspan='3'>ROUND OFF</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td align="center"><%=Utils.get2Decimal(roundOff)%></td>
		</tr>
		<%
			}
		%>
		<tr>
			<th align="right" colspan='3'>GRAND TOTAL</th>
			<th align="center"><%=Utils.get2Decimal(qty)%></th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th align="center"><%=Utils.get2Decimal(stotal)%></th>
			<th>&nbsp;</th>
			<th align="center"><%=Utils.get2Decimal(ttotal)%></th>
			<th align="center"><%=Utils.get2Decimal(total)%></th>
		</tr>
		<%
			double advTotal = 0;
			for (int i = 0; i < advances.length; i++) {
				advTotal = advTotal
						+ Utils.get2DecimalDouble(advances[i].getAmount()
								+ advances[i].getAdjAmt());
			}
			if (advTotal > 0) {
		%>
		<tr>
			<th align="right" colspan='3'>ADVANCES (-)</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th align="center"><%=Utils.get2Decimal(advTotal)%></th>
		</tr>
		<tr>
			<th align="right" colspan='3'>TOTAL AMOUNT PAYABLE</th>
			<th align="center"><%=Utils.get2Decimal(qty)%></th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th align="center"><%=Utils.get2Decimal(total - advTotal)%></th>
		</tr>
		<%
			}
		%>
	</table>
	<br />
	<center>
		<label style='font-size: 12px; font-family: tahoma; color: red;'>
			<b><u>RECEIPT - ADVANCE RECEIVED BREAK-UP DETAILS</u> </b>
		</label>
	</center>
	<table border=1 style="width: 800px; font-size: 11px; font-family: tahoma;" align='center'>
		<tr>
			<th style="width: 60px;">Date</th>
			<th>Branch</th>
			<th>Paymode</th>
			<th width='60%'>Desc</th>
			<th style="width: 65px;">
				Amount
				<br />
				(Rs.)
			</th>
		</tr>
		<%
			if (advances.length > 0) {
				for (int i = 0; i < advances.length; i++) {
		%>
		<tr>
			<td align="center"><%=Utils.convertToAppDateDDMMYY(advances[i]
							.getEntryDate())%></td>
			<td align="center"><%=advances[i].getBranch().getAccessName() + " - "
							+ advances[i].getBranch().getCity()%></td>
			<td align="center"><%=Utils.getEntryType(advances[i].getEntryType())%></td>
			<td align="center"><%=advances[i].getDesc()%></td>
			<td align="center"><%=Utils.get2Decimal(advances[i].getAmount()
							+ advances[i].getAdjAmt())%></td>
		</tr>
		<%
			}
		%>
		<tr>
			<th align="right" colspan="4">TOTAL</th>
			<th align="center"><%=Utils.get2Decimal(advTotal)%></th>
		</tr>
		<%
			} else {
		%>
		<tr>
			<td colspan=5 align=center>No Advances paid.</td>
		</tr>
		<%
			}
		%>
	</table>
<%
	if((total * 0.5)-advTotal > 500 && ((String)request.getAttribute("complete")).equals("N")){
%>
	<br />
	<center>
		<label style='font-size: 12px; font-family: tahoma; color: red;'>
			<b><u>50% ADVANCE REQUIRED DETAILS</u> </b>
		</label>
	</center>
	<table border=1 style="width: 800px; font-size: 11px; font-family: tahoma;" align='center'>
		<tr>
			<td align="center">50% - Advance Required</td>
			<td align="center">0.00% - Advance Paid</td>
			<td align="center">50.00% - Advance Payable</td>
		</tr>
		<tr>
			<th><%=Utils.get2Decimal(total * 0.5)%></th>
			<th><%=Utils.get2Decimal(advTotal)%></th>
			<th><%=Utils.get2Decimal((total * 0.5) - advTotal)%></th>
		</tr>
	</table>
<%
	}
%>
	<br />
	<div align="center" style="width: 100%;">
		<label style='font-size: 12px; font-family: tahoma; color: red; font-weight: bold; text-decoration: underline;'> BANK DETAILS </label>
	</div>
	<br />
	<table border="1" style="width: 800px; font-size: 11px; font-family: tahoma;" align='center'>
		<tr>
			<th>
				Option 1 - <b>KOTAK MAHINDRA BANK</b>
			</th>
			<th>
				Option 2 - <b>ICICI BANK</b>
			</th>
		</tr>
		<tr>
			<td>
				Name : <b><%=Constant.TITLE%></b>
				<br />
				Bank Name : <b><%=order.getBranch().getBankName1()%></b>
				<br />
				Bank Branch : <b><%=order.getBranch().getBankBranch1()%></b>
				<br />
				A/C No : <b><%=order.getBranch().getBankAc1()%></b>
				<br />
				Bank IFSC Code : <b><%=order.getBranch().getBankIfsc1()%></b>
			</td>
			<td>
				Name : <b><%=Constant.TITLE%></b>
				<br />
				Bank Name : <b><%=order.getBranch().getBankName2()%></b>
				<br />
				Bank Branch : <b><%=order.getBranch().getBankBranch2()%></b>
				<br />
				A/C No : <b><%=order.getBranch().getBankAc2()%></b>
				<br />
				Bank IFSC Code : <b><%=order.getBranch().getBankIfsc2()%></b>
			</td>
		</tr>
	</table>
	<div style="width: 100%" align="center">
		<br />
		<label style='font-size: 15px; font-weight: bold; color: red; font-family: tahoma; margin-left: 25px; width: 850px;'>CHEQUE or Draft should be in favour of "HESH OPTO-LAB PVT. LTD."</label>
		<br />
		<label style='font-size: 10px; font-weight: bold; color: red; font-family: tahoma; margin-left: 25px;'>Kindly inform HESH ACCONTS @ 93821 52555 after depositing the payment in the Bank.</label>
		<br />
		<table>
			<tr>
				<td>
					<fieldset style="width: 800px;">
						<legend style="font-size: 9.5px; font-family: Calibri; font-weight: bold; text-decoration: underline;"> Terms and Conditions: </legend>
						<label style="font-size: 9.5px; font-family: Calibri;">
							1. 30% payment at the time of booking.
							2. Payments received in reference to the sales contract, does not attract, any interest.
							3. All payments should be made by cash / cheque / DD drawn in favour of "<b>HESH OPTO-LAB PVT. LTD.</b>".
							4. With the change in model / name and delay in payments, booking priority / delivery dates will be rescheduled.
							5. Delivery is subject to realisation of full payment.6. The expected time of delivery will be ____ days from the date of receipt of full payment subject to availablity of product.
							7. Delivery of the product by dealer is subject to force majeure events and is not responsible for the delay if any.
							8. We are not responsible for any damage in transit.
							9. Letter is a must for any change in name / address / model / cancellation.
							10. 10 working days required for refund subject to the return of original receipt & valid reason.
							11. Deduction of any offers in invoice is at the discretion of company.
							12. Price, taxes and other statutory levies are applicable as prevailing on the date of delivery.
							13. Price / specifications are subject to change by the company.
							14. Disputes if any are subjected to the jurisdiction of the courts of CHENNAI.
						</label>
					</fieldset>
				</td>
			</tr>
		</table>
	</div>
	<br />
	<div align="center">
		<table style="width: 800px; margin-left: 25px; font-size: 11px; font-family: tahoma;">
			<tr>
				<td valign='top'>
					<label style='font-size: 10px; font-family: tahoma;'>
						<u>BRANCH ADDRESS:</u>
						<br />
					</label>
					<label style='font-size: 10px; font-family: tahoma;'>
						<b>HESH OPTO-LAB PVT. LTD.</b>
						<br />
					</label>
					<label style='font-size: 10px; font-family: tahoma;'>
						<%=order.getBranch().getAddress()%>
						<br />
					</label>
					<label style='font-size: 10px; font-family: tahoma;'>
						Email : <%=order.getBranch().getEmail()%>
						<br />
					</label>
					<label style='font-size: 10px; font-family: tahoma;'>
					Ph : <%=order.getBranch().getMobile1()+" / "+order.getBranch().getStdcode()+"-"+order.getBranch().getPhone1()%></label>
				</td>
				<td valign='top'>
					<label style='font-size: 10px; font-family: tahoma; text-decoration: underline;'>
						Corporate & Registered Office:
						<br />
					</label>
					<label style='font-size: 10px; font-family: tahoma;'>
						<b>HESH OPTO-LAB PVT. LTD.</b>
					</label>
					<br />
					<label style='font-size: 10px; font-family: tahoma;'>#141, Broadway,</label>
					<br />
					<label style='font-size: 10px; font-family: tahoma;'>Chennai - 600 108. India</label>
					<br />
					<label style='font-size: 10px; font-family: tahoma;'>Email : heshstoreaccounts@hotmail.com</label>
					<br />
					<label style='font-size: 10px; font-family: tahoma;'>Ph : +91 93821 52555 / 044-4301 2617</label>
				</td>
			</tr>
		</table>
		<br />
		<label style='font-size: 13px; font-weight: bold; color: black; font-family: tahoma; margin-left: 25px;'>THIS IS A COMPUTER GENERATED ORDER FORM. SIGNATURE NOT REQUIRED.</label>
	</div>
</body>
</html>