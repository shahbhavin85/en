package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.en.model.ConversationModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.TaskModel;
import com.en.model.UserModel;

public class MessengerBroker extends BaseBroker {
	
	private String[] colors = {"1","2","3","4","5","6","7","8","9","0","A","B","C","D","E","F"};

	public MessengerBroker(Connection conn) {
		super(conn);
	}

	@SuppressWarnings("resource")
	public synchronized MessageModel startConversation(ConversationModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long conversationId = 1;

		// query to check the existence of the bill no
		String maxConversationId = "SELECT MAX(CONVERSATIONID) FROM CONVERSATION_MASTER";

		// query to insert the bill details
		String insertConversationMaster = "INSERT INTO CONVERSATION_MASTER (CONVERSATIONID, SUBJECT, OWNER) VALUES (?,?,?) ";
		
		// String get tax rate
		String insertConversationUsers = "INSERT INTO CONVERSATION_USERS (CONVERSATIONID, USER, NEWCOUNT) VALUES (?,?,0)";

		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(maxConversationId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					conversationId = rs.getInt(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertConversationMaster);
				
				stmt.setLong(1, conversationId);
				stmt.setString(2, model.getSubject());
				stmt.setString(3, model.getOwner().getUserId());
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(insertConversationUsers);
				
				for(int i=0; i<model.getRecipients().size(); i++){
					stmt.setLong(1, conversationId);
					stmt.setString(2, model.getRecipients().get(i).getUserId());
					stmt.addBatch();
				}
				
				stmt.setLong(1, conversationId);
				stmt.setString(2, model.getOwner().getUserId());
				stmt.addBatch();
				
				stmt.executeBatch();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Conversation Started Successfully.");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, conversationId+"");
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while starting the conversation!!");
			msgs.addNewMessage(msg);
			try{
				getConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return msgs;
	}

	@SuppressWarnings("resource")
	public ConversationModel getConversation(int conversationId, UserModel user) {
		
		HashMap<String, String> colorMap = new HashMap<String, String>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		ConversationModel model = new ConversationModel();
		UserModel usr = null;
		int iDepth = 0;
		
		// query to get the branch details
		String getConversationMasterDtls = "SELECT A.CONVERSATIONID, A.SUBJECT, A.OWNER, A.CREATED_TIME, B.USERNAME, A.STATUS FROM CONVERSATION_MASTER A, USER B WHERE A.CONVERSATIONID = ? AND A.OWNER = B.USERID";

		// query to get the branch details
		String getConversationUserDtls = "SELECT A.CONVERSATIONID, A.USER, B.USERNAME FROM CONVERSATION_USERS A, USER B WHERE A.CONVERSATIONID = ? AND A.USER = B.USERID";
		
		// query to get the branch details
		String getMaxTaskDepth = "SELECT MAX(DEPTH) FROM CONVERSATION_TASK  WHERE CONVERSATIONID = ?";
		
		// query to get the branch details
		String getConversationTasks = "SELECT A.TASKID, A.USERID, A.PARENTID, A.DESCRIPTION, A.FILENAME, A.ISATTACHABLE, A.TASKTYPE, A.TASK_CREATE_TIME, B.USERNAME FROM CONVERSATION_TASK A, USER B WHERE A.CONVERSATIONID = ? AND A.DEPTH = ? AND A.USERID = B.USERID ORDER BY A.TASK_CREATE_TIME";
		
		// query to update count
		String updateCount = "UPDATE CONVERSATION_USERS SET NEWCOUNT = 0 WHERE CONVERSATIONID = ? AND USER = ?";
		
		// query to update count
		String newMessages = "SELECT TASKID FROM CONVERSATION_NEW WHERE CONVERSATIONID = ? AND USERID = ?";
		
		// query to update count
		String deleteRow = "DELETE FROM CONVERSATION_NEW WHERE CONVERSATIONID = ? AND USERID = ?";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getConversationMasterDtls);
			stmt.setInt(1, conversationId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.setConversationId(rs.getInt("CONVERSATIONID"));
				model.setSubject(rs.getString("SUBJECT").toUpperCase());
				model.getOwner().setUserId(rs.getString("OWNER"));
				model.getOwner().setUserName(rs.getString("USERNAME"));
				model.setTimeStamp(rs.getTimestamp("CREATED_TIME"));
				model.setStatus(rs.getInt("STATUS"));
			}
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getConversationUserDtls);
			stmt.setInt(1, conversationId);
			
			rs = stmt.executeQuery();
			
			String color;
			
			while(rs.next()){
				usr = new UserModel();
				usr.setUserId(rs.getString("USER"));
				usr.setUserName(rs.getString("USERNAME"));
				color = "#";
				for(int k=0; k<6; k++){
					color = color + colors[(int)((Math.random()*100)%16)];  
				}
				usr.setColor(color);
				colorMap.put(rs.getString("USER"), color);
				model.getRecipients().add(usr);
			}
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getMaxTaskDepth);
			stmt.setInt(1, conversationId);
			
			rs = stmt.executeQuery();

			if(rs.next() && rs.getInt(1)>0){
				iDepth = rs.getInt(1);
			}
			
			if(iDepth>0){
				stmt = getConnection().prepareStatement(getConversationTasks);
				
				HashMap<Integer, ArrayList<TaskModel>> temp = new HashMap<Integer, ArrayList<TaskModel>>(0);
				HashMap<Integer, ArrayList<TaskModel>> temp1 = new HashMap<Integer, ArrayList<TaskModel>>(0);
				ArrayList<TaskModel> lst = null;
				TaskModel task = null;
				
				for(int i=iDepth; i>0; i--){
					temp1 = temp;
					
					temp = new HashMap<Integer, ArrayList<TaskModel>>(0);
					
					stmt.setInt(1, conversationId);
					stmt.setInt(2, i);
					
					rs = stmt.executeQuery();
					
					while(rs.next()){
						if(temp.get(rs.getInt("PARENTID")) == null){
							lst = new ArrayList<TaskModel>(0);
						} else {
							lst = temp.get(rs.getInt("PARENTID"));
						}
						task = new TaskModel();
						task.setTaskId(rs.getInt("TASKID"));
						task.setParentId(rs.getInt("PARENTID"));
						task.getOwner().setUserId(rs.getString("USERID"));
						task.getOwner().setUserName(rs.getString("USERNAME"));
						task.setDescription(rs.getString("DESCRIPTION"));
						task.setAttachment(rs.getInt("ISATTACHABLE") == 1 ? true : false);
						if(task.isAttachment()){
							task.setFileName(rs.getString("FILENAME"));
						}
						task.setColor(colorMap.get(rs.getString("USERID")));
						task.setTimeStamp(rs.getTimestamp("TASK_CREATE_TIME"));
						task.setTaskType(rs.getInt("TASKTYPE"));
						if(temp1.get(rs.getInt("TASKID")) != null){
							task.setChildTask(temp1.get(rs.getInt("TASKID")));
						}
						lst.add(task);
						temp.put(rs.getInt("PARENTID"), lst);
					}
				}
				
				model.setTasks(lst);
				
			}
			
			stmt = getConnection().prepareStatement(updateCount);
			stmt.setInt(1, conversationId);
			stmt.setString(2, user.getUserId());
			
			stmt.execute();
			
			stmt = getConnection().prepareStatement(newMessages);
			stmt.setInt(1, conversationId);
			stmt.setString(2, user.getUserId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.getNewMessages().add(rs.getInt(1));
			}
			
			stmt = getConnection().prepareStatement(deleteRow);
			stmt.setInt(1, conversationId);
			stmt.setString(2, user.getUserId());
			
			stmt.execute();
			
			getConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				getConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(rs1 != null)
					rs1.close();
				if(stmt != null)
					stmt.close();
				if(stmt1 != null)
					stmt1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	@SuppressWarnings("resource")
	public int addComment(TaskModel model) {
		
		int taskId = 1;
		int depth = 1;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String maxDepth = "SELECT DEPTH FROM CONVERSATION_TASK WHERE CONVERSATIONID = ? AND TASKID = ?";
		
		// query to check the existence of the bill no
		String maxTaskId = "SELECT MAX(TASKID) FROM CONVERSATION_TASK";

		// query to insert the bill details
		String insertConversationTask = "INSERT INTO CONVERSATION_TASK (TASKID, PARENTID, CONVERSATIONID, USERID, DESCRIPTION, FILENAME, ISATTACHABLE, TASKTYPE, TASK_CREATE_TIME, DEPTH) VALUES (?,?,?,?,?,?,?,0,CURRENT_TIMESTAMP, ?) ";
		
		// query to increase the count by 1
		String updateCount = "UPDATE CONVERSATION_USERS SET NEWCOUNT = NEWCOUNT + 1 WHERE CONVERSATIONID = ?";
		
		// query to increase the count by 1
		String getUsers = "SELECT USER FROM CONVERSATION_USERS WHERE CONVERSATIONID = ?";
		
		// query to insert new message alert
		String insertAlert = "INSERT INTO CONVERSATION_NEW (CONVERSATIONID, TASKID, USERID) VALUES (?,?,?)";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(maxDepth);
				stmt.setInt(1, model.getConversationId());
				stmt.setInt(2, model.getParentId());
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					depth = rs.getInt(1) + 1;
				}
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(maxTaskId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					taskId = rs.getInt(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertConversationTask);
				
				stmt.setInt(1, taskId);
				stmt.setInt(2, model.getParentId());
				stmt.setInt(3, model.getConversationId());
				stmt.setString(4, model.getOwner().getUserId());
				stmt.setString(5, model.getDescription());
				stmt.setString(6, model.getFileName());
				stmt.setInt(7, model.isAttachment() ? 1 : 0);
				stmt.setInt(8, depth);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updateCount);
				stmt.setInt(1, model.getConversationId());
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(getUsers);
				stmt.setInt(1, model.getConversationId());
				
				rs = stmt.executeQuery();
				
				stmt1 = getConnection().prepareStatement(insertAlert);
				
				while(rs.next()){
					stmt1.setInt(1, model.getConversationId());
					stmt1.setInt(2, taskId);
					stmt1.setString(3, rs.getString(1));
					
					stmt1.addBatch();
				}
				
				stmt1.executeBatch();
				stmt1.clearBatch();
				
				getConnection().commit();
				
		} catch (Exception e) {
			try{
				getConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return taskId;
	}

	public ConversationModel[] getMyConversations(UserModel user) {
		
		ArrayList<ConversationModel> lst = new ArrayList<ConversationModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		ConversationModel model = null;
		
		// query to get the branch details
		String getConversationMasterDtls = "SELECT A.CONVERSATIONID, A.SUBJECT, A.OWNER, A.CREATED_TIME, B.USERNAME, C.NEWCOUNT FROM CONVERSATION_MASTER A, USER B, CONVERSATION_USERS C WHERE A.CONVERSATIONID = C.CONVERSATIONID AND C.USER = ? AND A.OWNER = B.USERID AND A.STATUS = 0";

		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getConversationMasterDtls);
			stmt.setString(1, user.getUserId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new ConversationModel();
				model.setConversationId(rs.getInt("CONVERSATIONID"));
				model.setSubject(rs.getString("SUBJECT").toUpperCase());
				model.getOwner().setUserId(rs.getString("OWNER"));
				model.getOwner().setUserName(rs.getString("USERNAME"));
				model.setTimeStamp(rs.getTimestamp("CREATED_TIME"));
				model.setNewCount(rs.getInt("NEWCOUNT"));
				lst.add(model);
			}
			
			getConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				getConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(rs1 != null)
					rs1.close();
				if(stmt != null)
					stmt.close();
				if(stmt1 != null)
					stmt1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return (ConversationModel[])lst.toArray(new ConversationModel[0]);
	}

	public int getInboxCount(UserModel user) {
		int count = 0;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the branch details
		String getConversationMasterDtls = "SELECT COUNT(*) FROM CONVERSATION_NEW WHERE USERID = ?";

		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getConversationMasterDtls);
			stmt.setString(1, user.getUserId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				count = rs.getInt(1);
			}
			
			getConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				getConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return count;
	}

	@SuppressWarnings("resource")
	public void changeConversationStatus(int conversationId, int status) {
		
		PreparedStatement stmt = null;
		
		// query to get the branch details
		String getConversationMasterDtls = "UPDATE CONVERSATION_MASTER SET STATUS = ? WHERE CONVERSATIONID = ?";
		
		// query to delete the alert rows
		String deleteAlertRows = "DELETE FROM CONVERSATION_NEW WHERE CONVERSATIONID = ?";

		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getConversationMasterDtls);
			stmt.setInt(1, status);
			stmt.setInt(2, conversationId);
			
			stmt.execute();
			
			if (status == 1){

				stmt = getConnection().prepareStatement(deleteAlertRows);
				stmt.setInt(1, conversationId);
				
				stmt.execute();
			
			}
			
			getConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				getConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return;
	}

	public ConversationModel[] getMyClosedConversations(UserModel user) {
		
		ArrayList<ConversationModel> lst = new ArrayList<ConversationModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		ConversationModel model = null;
		
		// query to get the branch details
		String getConversationMasterDtls = "SELECT A.CONVERSATIONID, A.SUBJECT, A.OWNER, A.CREATED_TIME, C.USERNAME FROM CONVERSATION_MASTER A, USER C WHERE A.OWNER = C.USERID AND A.OWNER = ? AND A.STATUS = 1";

		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getConversationMasterDtls);
			stmt.setString(1, user.getUserId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new ConversationModel();
				model.setConversationId(rs.getInt("CONVERSATIONID"));
				model.setSubject(rs.getString("SUBJECT").toUpperCase());
				model.getOwner().setUserId(rs.getString("OWNER"));
				model.getOwner().setUserName(rs.getString("USERNAME"));
				model.setTimeStamp(rs.getTimestamp("CREATED_TIME"));
				lst.add(model);
			}
			
			getConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				getConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(rs1 != null)
					rs1.close();
				if(stmt != null)
					stmt.close();
				if(stmt1 != null)
					stmt1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return (ConversationModel[])lst.toArray(new ConversationModel[0]);
	} 

}
