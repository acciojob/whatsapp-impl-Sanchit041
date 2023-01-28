package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

import java.beans.ExceptionListener;
import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }
    public String createUser(String name,String Phone_no)
    {
        if(userMobile.contains(Phone_no))
        {
            return "Already Present";
        }
        User u = new User(name,Phone_no);
//        u.add(u);
        userMobile.add(Phone_no);
        return "Success";
    }
   public Group  createGroup(List<User> user)
   {
       if(user.size()<=1){

       }
       Group g ;
       if(user.size()==2)
       {
            g= new Group(user.get(1).getName(),customGroupCount+1);
           customGroupCount +=1;
           groupUserMap.put(g,user);
       }
       else
       {
             g = new Group("Group"+(customGroupCount+1),customGroupCount+1);
            customGroupCount++;
            adminMap.put(g,user.get(1));
            groupUserMap.put(g,user);
       }
       return g;
   }
   public int createMessage(String content)
   {
       Message m =new Message(messageId+1,content);
       messageId++;
       return m.getId();
   }
   public int sendMessage(Message message,User user,Group group) throws Exception
   {
        if(adminMap.containsKey(group)){
            List<User> users = groupUserMap.get(group);
            Boolean userFound = false;
            for(User use: users){
                if(use.equals(user)){
                    userFound = true;
                    break;
                }
            }
            if(userFound){
                senderMap.put(message, user);
                List<Message> messages = groupMessageMap.get(group);
                messages.add(message);
                groupMessageMap.put(group, messages);
                return messages.size();
            }
            throw new Exception("You are not allowed to send message");
        }
        throw new Exception("Group does not exist");
   }
   public String changeAdmin(User approver, User user, Group group)throws Exception {
       if(adminMap.containsKey(group)){
            if(adminMap.get(group).equals(approver)){
                List<User> participants = groupUserMap.get(group);
                Boolean userFound = false;
                for(User participant: participants){
                    if(participant.equals(user)){
                        userFound = true;
                        break;
                    }
                }
                if(userFound){
                    adminMap.put(group, user);
                    return "SUCCESS";
                }
                throw new Exception("User is not a participant");
            }
            throw new Exception("Approver does not have rights");
        }
        throw new Exception("Group does not exist");
    }

//    public int removeUser(User user) {
//      return 1;
//    }
//    public String findMessage(Date start, Date end, int k) {
//        return "";
//    }
}
