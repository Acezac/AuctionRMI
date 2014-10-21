//Server class



//import
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;	
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.io.InputStreamReader;
import java.io.BufferedReader;



public class auctionserver  extends UnicastRemoteObject implements auction{

	
	//serialVersionUID,verify that the sender and receiver of an object
	//have loaded classes with serialization
	private static final long serialVersionUID = 1L;
	
	static ArrayList<Auction_Item> product = new ArrayList<Auction_Item>();  //arraylist product which include the auctions and the owner of them
	
    ArrayList<bidder_cr> bidders = new ArrayList<bidder_cr>();  //arraylist bidders which include the bidders
    
    static  Timer t1=new Timer(); //Timer for auctions that are closed
    static Timer t2=new Timer(); // Timer to clean the old auctions
    static int idd=0;
    static long time3=604800000; //declare how long an item will retain to the database after the end of auction
    static String msgg="";
    
    public auctionserver() throws RemoteException {
        super();      
     }
    
    
    // load the auctions from permanent store to the arraylist products
	 public void load(File f) {
		 
		 long millisecond=0;
		 Calendar cal=Calendar.getInstance(); 
		 
		 Calendar c=Calendar.getInstance();
		 
		 long millis=cal.getTimeInMillis();
		 long duration=millis+time3;  //declare how long an item will retain to the database after the end of auction
		 long dura=c.getTimeInMillis();

	
		 

		
	       try {

		   InputStreamReader in=new InputStreamReader(System.in);
		   BufferedReader br = new BufferedReader(in);

		   System.out.println("Give the maximum time in minutes that an inactive auction retain on storage. Default is one week. Type n for default time");
		   msgg = br.readLine();
		  
		   if(!msgg.equals("n")){
			   time3=Long.parseLong(msgg)*60000;
		   }
		   
	     
	    	   FileInputStream fis = new FileInputStream(f);
	           ObjectInputStream ois = new ObjectInputStream(fis);
	            
	            this.product = (ArrayList<Auction_Item>)ois.readObject(); //load the permanent store
	            
	            if(product.size()<1){
	            	idd=0;
	            }
	            else{
		            idd=product.get(product.size()-1).getID()+1;// declare the value of item ID

	            }
	            for(int i=0;i<product.size();i++){
	            	
	            	c=product.get(i).getClosing_Date();
	            	c.set(13,0); //set zero the seconds of auction
	            	dura=c.getTimeInMillis();
	            		
	        	   if(dura-millis>0){
	        		   
		        	  //if the auction is active, set a timer for its
	        		   t1.schedule(new RepeatTask(product.get(i).getID()), (dura-millis));
	        		   
	        	   }
	        	   else{
		        	   
	        		   if(millis-dura>time3){
	        			   //if the auction is over the duration of delete , it sets a timer to delete it in a millisecond
	        			   t2.schedule(new DeleteAuction(product.get(i).getID()),1);
	        			   
	        		   }
	        		   else{
	        			   
	        		  //otherwise it sets a timer for the rest of the time of delete, until it will be removed
	        	   millisecond=duration-millis;
	        	   t2.schedule(new DeleteAuction(product.get(i).getID()), millisecond);
	        	   
	        		   }
	        	   }   
	            }
	         ois.close();
	         fis.close();
	         
	       } catch (IOException e) {
	       System.out.println(e);
	       } catch (ClassNotFoundException e) {
	    	   System.out.println(e);
	       } 
    }
	 
	 
	 
	 //store the arraylist in a permanent file, which type is .ser
	 public void store(File f) {
	    	
	    	try {
	    		
	    		FileOutputStream fos = new FileOutputStream(f);
	            ObjectOutputStream oos = new ObjectOutputStream(fos);
	            
	             oos.writeObject(product);
	             
                 oos.close();
	             fos.close();
	 
	         } catch (IOException ex) {
	  	       System.out.println(ex);
	         }
	     }
	
	
	 
	 
	 
	//create a new auction, receives the name, minimum value of item, 
	 //closing date and the object of client and store them in an arraylist
    public void new_auction(String a,int d, Calendar c, auctionclientserver client)throws java.rmi.RemoteException{
    	
    	long millisecond=0;
    	
    	
    	String flag="active";
        Calendar cal=Calendar.getInstance(); 
	     
	    c.set(13,0);//sets zero the seconds of the variable type calendar, the reason is that the auction finishes exactly at the calendar time
	    c.set(2,c.get(Calendar.MONTH)-1);//decrease the number of month, because January is 0 in calendar
	      
	    if(c.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()>=0){ //control if the date from clients is after the current day
	    	  
	     millisecond=(c.getTimeInMillis()-cal.getTimeInMillis());// take the duration that an auction will be active
	     
	          
	      Auction_Item item = new Auction_Item(idd,a,d,c,flag,client);// declare an object type auction_item
	      
	      product.add(item);//store it in the arraylist
	     
	     
	      t1.schedule(new RepeatTask(idd), millisecond);//sets a timer
	     
	      client.callBack("your auction ID is:"+idd);// return the id of item to the owner
	       idd=product.get(product.size()-1).getID()+1;// increase the value of id

	      }
	      else{
	    	  
	    	  client.callBack("Closing time must be later than now");//inform the client that the date is before the current day
	    	  
	      }
  }
    
    
    
    //display the auctions in a different kinds of listings. Depend on the selection of client
	public void auctions(int flag, auctionclientserver client)throws java.rmi.RemoteException{
		
		String s=String.format("%0" + 68 + "d", 0).replace("0","-");
		String auc =String.format("%-4s%-10s%-10s%-31s%-13s", "ID", "Name", "Status", "Ending Time", "Minimum Offer"+"\n")+s+"\n";

		
		if(product.size()>0){//check the size of arraylist
		
		ArrayList<Auction_Item> test=new ArrayList<Auction_Item>();
		
		for(int i=0;i<product.size();i++){
			
		test.add(product.get(i));//passes the product arraylist to a test arraylist
		
		}
		
		if(flag==1){//the variable of the selection of user.
		
		 for(int i=0;i<product.size();i++) {
		
			 auc+= product.get(i).toString()+"\n";// a simple display of all auctions without sorting
			 
		 }
		}
		
		else if(flag==2){
		
			Collections.sort(test, new Auction_Item.CompMinValue());//sort the auctions by their initial price
			
			for(int i=0;i<test.size();i++) {
				
				 auc+= test.get(i).toString()+"\n";
				 
			 }
		}
		
		else if(flag==3){
			
			Collections.sort(test, new Auction_Item.CompStatus());//sort the auctions by their current status
			
			for(int i=0;i<test.size();i++) {
				
				 auc+= test.get(i).toString()+"\n";
				
			 }
		}
		
		else if(flag==4){
			
			Collections.sort(test, new Auction_Item.CompClosingDate());//sort the auctions by closing date
			
			for(int i=0;i<test.size();i++) {
				
				 auc+= test.get(i).toString()+"\n";
				 
			 }
		}
		
		test.clear();// clear the test arraylist
		client.callBack(auc);//send the message back to the client
		
		}
		else{
			client.callBack("No available auctions"+"\n");// whether the product arraylist is empty, it informs the client
		}
	}
	
	
	
	
	//find an auction by its id
public void SearchID(int cod, auctionclientserver client)throws java.rmi.RemoteException{
	
	String s=String.format("%0" + 68 + "d", 0).replace("0","-");
	String msg =String.format("%-4s%-10s%-10s%-31s%-13s", "ID", "Name", "Status", "Ending Time", "Minimum Offer"+"\n")+s+"\n";
	int flag=0;
	
	for(int i=0;i<product.size();i++){
		
		if(product.get(i).getID()==cod){
			
			flag=1;//change the value of flag, to inform that an itme was found by this ID
			msg+=product.get(i).toString()+"\n";
			
		}
	}
	
	if(flag==0){
		 msg="No auction with this ID";
	}
	
	client.callBack(msg);
}



	//display the items that the client placed a bid
public void DisplayItems(auctionclientserver client)throws java.rmi.RemoteException{
	
	String s=String.format("%0" + 68 + "d", 0).replace("0","-");
	String msg =String.format("%-4s%-10s%-10s%-31s%-13s", "ID", "Name", "Status", "Ending Time", "Minimum Offer"+"\n")+s+"\n";
	int flag=0;
	
	for(int i=0;i<bidders.size();i++){
		if(bidders.get(i).getBidder().equals(client)){// check if the object is the same with the client
			for(int j=0;j<product.size();j++){
				if(product.get(j).getID()==bidders.get(i).getId()){// check if the id of item in auctions is the same with the id in bidders arraylist
					
			msg+=product.get(j).toString();
			flag=1;
			
			}
		}
	}
	}
	if(flag==0){
		 msg="No Auctions";//inform the user if he/she did not place any bid
	}
	
	client.callBack(msg);
}
	
	
	//display the auctions that the client created
public void DisplayAuctions(auctionclientserver client)throws java.rmi.RemoteException{	
	
	String s=String.format("%0" + 68 + "d", 0).replace("0","-");
	String msg =String.format("%-4s%-10s%-10s%-31s%-13s", "ID", "Name", "Status", "Ending Time", "Minimum Offer"+"\n")+s+"\n";
	int flag=0;
	

	for(int i=0;i<product.size();i++){
		if(product.get(i).getOwner().equals(client)){
			
			msg+=product.get(i).toString();
			flag=1;
			
		}
	}
	
	if(flag==0){
		 msg="No Auctions";
	}
	client.callBack(msg);
	
}
	

	//place a bid to an item
public void bid(int code, Double offer,auctionclientserver clien) throws java.rmi.RemoteException{
		int flag=0;
		bidder_cr owner=null;//declare an object type bidder_cr

		 for(int i=0;i<product.size();i++){
			
			 //1.the id of client is the same with the item in arraylist, 2. if the auction is still active, 
			 //3. if the offer is bigger than minimum value, 4. if the owner is not the same with the bidder, we want to avoid cheating
			 if(product.get(i).getID()==code && product.get(i).getFlag().equals("active") && offer>product.get(i).getMin_vlue() && !product.get(i).getOwner().equals(clien)){
				
				 owner=new bidder_cr(clien,offer,code);
				 bidders.add(owner);
				 flag=1;
				 clien.callBack("Your offer has been made!! Good Luck!!");
			 	}
			 
		 	}
		
		 if(flag!=1){
			 clien.callBack("Wrong Auction!!");
			}
		}
	

	//inform the participants of an auction, turn an auction to inactive, set a timer to delete the auction after specific time
	class RepeatTask extends TimerTask {
		
	     private int cod;
	     private ArrayList <bidder_cr> biidders=new ArrayList<bidder_cr>();
	  
	    public RepeatTask(int c) {		
	    	cod=c;
	    }
			
	    public void run() {
	    		
	     
	    		
			try {
					
				 
			for (int a=0;a<bidders.size();a++) {
						
				if(bidders.get(a).getId()==cod) {//passes the bidders of the item to a new arraylist
					biidders.add(bidders.get(a));
					 }
				}
			
			Collections.sort(biidders, new bidder_cr.CompId());//sort the auctions in biddders arraylist by offers
			
					
			/*	if(biidders.size()==1){// whether the arraylist has only one elemets, this client is the winner
							
				biidders.get(biidders.size()-1).getBidder().callBack("You are the winner of : "+cod+" ,you have to pay: "+biidders.get(biidders.size()-1).getoffer());
					
				}*/
				
				if(biidders.size()>=1){//otherwise the last element will be the winner, because the arraylist is sorted by offers
					
					biidders.get(biidders.size()-1).getBidder().callBack("You are the winner of : "+cod+" ,you have to pay: "+biidders.get(biidders.size()-1).getoffer());
					if(bidders.size()>1){
					
				for(int i=0;i<biidders.size()-1;i++) { // the rest of them just receive a notification about the final price of item
					
					    bidders.get(i).getBidder().callBack("Sorry, Your offer "+ biidders.get(i).getoffer() +" did not meet the price of : "+cod+" which was: "+biidders.get(biidders.size()-1).getoffer());       
					  }
					}
				}
				
				//set the auctions inactive and put a timer for them
				for(int e=0;e<product.size();e++){
				if(product.get(e).getID()==cod){
					
					product.get(e).setFlag("inactive");
					t2.schedule(new DeleteAuction(product.get(e).getID()), time3);
						
				//if the biidders list is empty means that the item did not sell. the owner receives a notification
				if(biidders.size()==0){
					
					product.get(e).getOwner().callBack("Your product: "+cod+" was not sold ");
					}
				
				else{//otherwise the owner receives a notification that the item sold and the price of it
					 product.get(e).getOwner().callBack("Your product: "+cod+" sold at : "+biidders.get(biidders.size()-1).getoffer());

					}	
				}
			}	
				 	  
		   	} catch(RemoteException e) {
		   		String a=e.getMessage();// if the program exit and then re-run again or the client is disconnected
		   		//the server cannot find the user. As a result it prints an error and a message at server side.
		       System.out.println(a+"\n"+"\n"+"Probably the client has been disconnected !!!!!"); 
	     }
	    }	
	    }
	
	
	
	//delete an inactive auction after a specific time
  class DeleteAuction extends TimerTask {
	     private int cod;

	     public DeleteAuction(int c) {		
	    		cod=c;
	    	}
			
	    public void run() {
	    	
			for(int i=0;i<product.size();i++){
				if(product.get(i).getID()==cod){
					
						product.remove(i);//remove the item from product arraylist

				}
			}
	  	 }	
	    }
	
	
  //main method of class
   public static void main(String args[]) throws java.rmi.RemoteException, ClassNotFoundException {
	  
	   final File file = new File("file.ser");  //declare the file that the arraylist with auctions is stored
	   String host = "localhost";  //default value for host name
	   int port = 1099;  //default value for port
	   final auctionserver rmiServer = new auctionserver();
		
	   if (args.length > 0) {  // if the user pass arguments to main, they set for host and port
		   host = args[0];
			if (args.length == 2)
				port = Integer.parseInt(args[1]);
			}
			
	   try {

		 LocateRegistry.createRegistry(port);
		 Naming.rebind("rmi://"+host+":"+port+"/AuctionService", rmiServer);
		 rmiServer.load(file);  //load the file with auctions
		 System.out.println("RMIServer is ready");  //inform the user that the server is ready
     
	   } catch(RemoteException e) {
		   System.out.println("Exception in RMIServerImp.main " + e);
	   } catch(MalformedURLException ue) {
		   System.out.println("MalformedURLException in RMIServerImp.main " + ue);
	   }


	   Runtime.getRuntime().addShutdownHook(new Thread() {// this method is running when the user press ctrl+c in command line of whether the program is exit for any reason

		public void run() {

		rmiServer.store(file);//store the arraylist in a permanents store
		
		System.out.println("Shutdown Hook is running !");
		}
	});
  }
   
}
