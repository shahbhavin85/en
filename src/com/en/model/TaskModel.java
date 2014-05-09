package com.en.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.en.util.Constant;

public class TaskModel implements Constant {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
	
	private int taskId = 0;
	private int conversationId = 0;
	private int parentId = 0;
	private String description = "";
	private boolean isAttachment = false;
	private String color = "#837E7C";
	private String fileName = "";
	private int taskType = 0;
	private UserModel owner = new UserModel();
	private Timestamp timeStamp = new Timestamp(0);
	private ArrayList<TaskModel> childTask = new ArrayList<TaskModel>(0);
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getConversationId() {
		return conversationId;
	}
	public void setConversationId(int conversationId) {
		this.conversationId = conversationId;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isAttachment() {
		return isAttachment;
	}
	public void setAttachment(boolean isAttachment) {
		this.isAttachment = isAttachment;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public UserModel getOwner() {
		return owner;
	}
	public void setOwner(UserModel owner) {
		this.owner = owner;
	}
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public ArrayList<TaskModel> getChildTask() {
		return childTask;
	}
	public void setChildTask(ArrayList<TaskModel> childTask) {
		this.childTask = childTask;
	}
	public String getChildHtml(){
		StringBuffer childDetails = new StringBuffer(0);
		Iterator<TaskModel> itr = this.childTask.iterator();
		TaskModel temp = null;
		childDetails.append("<li><div id=\"task"+taskId+"\" style=\"background-color:#E5E4E2; width: 400px; border:"+this.color+" solid 3px; -moz-border-radius: 5px; border-radius: 5px; padding:5px;\"><table width=\"100%\" cellspacing=0 style=\"font-size:8px;color:blue;\"><tr><td style=\"width:200px;\">"+this.owner.getUserName()+"</td><td align=\"right\">"+(this.isAttachment ? "<input type=\"button\" style=\"height:16px; font-size:9px;\" value=\"Download\" onclick=\"window.open('/HESH_MESSENGER_FILES/"+this.taskId+"/"+this.fileName+"',\'\',\'status=1,scrollbars=1, top=0, left=0,width=\'+(screen.width-10)+\',height=\'+(screen.height-120));\" align=\"right\" style=\"background-image: url(images/ui-icons_ffffff_256x240.png);background-position: -224px -48px;width: 16px;height: 16px;float: right;margin: 0 0px;display: block;text-indent: -99999px;overflow: hidden;background-repeat: no-repeat;\" />" : "")+"<input type=\"button\" style=\"height:16px; font-size:9px;\" value=\"Reply\" onclick=\"fnComment("+this.taskId+");\"/></td><td align=\"right\" style=\"width:75px;\">"+dateFormat.format(this.timeStamp)+"</td></tr></table><hr>"+this.description.replaceAll("\n", "<br/>")+"</div>");
		if(this.getChildTask().size() > 0){
			childDetails.append("<ul>");
			while(itr.hasNext()){
				temp = itr.next();
				try{
					childDetails.append(temp.getChildHtml());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			childDetails.append("</ul>");
		}
		childDetails.append("</li>");
		return childDetails.toString();
	}
}
