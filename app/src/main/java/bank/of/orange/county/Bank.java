package bank.of.orange.county;
import java.util.*;

public class Bank {
    public class User{
        int userID;
        String name;
        String address;
        int SSN;
        int depositAmount;
        User prev;
        User next;

        public User(int userID, String name, String address, int SSN, int depositAmount, User prev, User next){
            
            this.userID = userID;
            this.name = name;
            this.address = address;
            this.SSN = SSN;
            this.depositAmount = depositAmount;

            this.prev = prev;
            this.next = next;
        }

        public void decAccount(int dec){
            depositAmount = depositAmount - dec;
        }

        public void incAccount(int inc){
            depositAmount += inc;
        }
    }

    User header;
    User trailer;
    int userIDCount;
    int bankSize;
    Queue<Integer> deletedUserQ;
    
    public Bank(){
        header = new User(0, (String) null , (String) null, 0, 0, (User)null, (User)null);
        trailer = new User(0,(String) null, (String) null, 0, 0, (User)null, (User)null);
        
        header.next = trailer;
        trailer.prev = header;

        userIDCount = 1;
        bankSize = 0;
        //empty queue of deleted userID 
        deletedUserQ = new PriorityQueue<>();
    }

    public void addUser(String name, String address, int SSN, int depositAmount){
        if(bankSize == 0) {
            int newID;
            if(!deletedUserQ.isEmpty()){
                //create new user from queue ID
                newID = deletedUserQ.remove();
            } else {
                //create new user from bank ID count
                newID = userIDCount;
                userIDCount++;
            }
            User newUser = new User(newID, name, address, SSN, depositAmount, header, trailer);
            header.next = newUser;
            trailer.prev = newUser;

        } else {
            int newID;
            if(!deletedUserQ.isEmpty()){
                //create new user from queue ID
                newID = deletedUserQ.remove();
            } else {
                //create new user from bank ID count
                newID = userIDCount;
                userIDCount++;
            }
            //iterate for spot in bank linked list
            User iterator = header;
            while(iterator.next.userID < newID ) {
                if(iterator.next.next == null) {
                    break;
                }
                iterator = iterator.next;
            }
            //add user account in proper spot
            User newUser = new User(newID, name, address, SSN, depositAmount, iterator, iterator.next);
            iterator.next.prev = newUser;
            iterator.next = newUser;
        }
        bankSize++;
    }

    public String deleteUser(int userID){
        User iterator = header;
        while(iterator.next.userID != userID ) {
            if(iterator.next.next.next == null) {
                return "-User not found-";
            }
            iterator = iterator.next;
        }
        iterator.next.next.prev = iterator;
        iterator.next = iterator.next.next;
        deletedUserQ.add(userID);
        bankSize--;
        
        return "-User deleted-";
    }

    public void payUserToUser(int payerID, int payeeID, int amount){
        //get payer node
        User iterator = header;
        while( iterator.next != null) {
            if(iterator.next.userID == payerID){
                iterator.next.decAccount(amount);
                break;
            }
            iterator = iterator.next;
        }

        //get payee node
        iterator = header;
        while( iterator.next != null) {
            if(iterator.next.userID == payeeID){
                iterator.next.incAccount(amount);
                break;
            }
            iterator = iterator.next;
        }
        
        System.out.println("Transaction Completed");
    }

    public int getMedianID(){
        int medianOfBank = bankSize/2;

        //for loop to iterate to that point
        User medianAccount;
        User iterator = header;
        int i;
        for(i = 0; i < medianOfBank; i++){
            iterator = iterator.next;
        }
        medianAccount = iterator;
        return medianAccount.userID;
    }

    public String mergeAccounts(int account1, int account2){
        //iterate for account1
        User iterator = header;
        while(iterator.next.userID != account1 ) {
            if(iterator.next.next.next == null) {
                return "-User 1 not found-";
            }
            iterator = iterator.next;
        }
        User accNode1 = iterator.next;

        // iterate for account2
        iterator = header;
        while(iterator.next.userID != account2 ) {
            if(iterator.next.next.next == null) {
                return "-User 2 not found-";
            }
            iterator = iterator.next;
        }
        User accNode2 = iterator.next;

        //merge accounts if they are the same
        if(accNode1.name == accNode2.name && accNode1.address == accNode2.address && accNode1.SSN == accNode2.SSN){
            User biggestAccount = accNode1.userID > accNode2.userID ? accNode1 : accNode2;
            User smallestAccount = accNode1.userID > accNode2.userID ? accNode2 : accNode1;

            smallestAccount.incAccount(biggestAccount.depositAmount);
            deleteUser(biggestAccount.userID);
        } else {
            return "---Accounts not the same---";
        }

        return "---Accounts merged---";
    }

    public String getAccounts() {
        while(header != null) {
            System.out.println(header.userID);
            System.out.println(header.name);
            System.out.println(header.address);
            System.out.println(header.SSN);
            System.out.println(header.depositAmount);
            System.out.println("previous node " + header.prev);
            System.out.println("next node " + header.next);
            System.out.println("---complete account---");
            header = header.next;
        }
        return "accounts printed";
    }
}