
import java.rmi.Naming;			
//client cass
import java.rmi.RemoteException;	
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;	
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;


public class auctionclient extends UnicastRemoteObject implements Runnable,auctionclientserver{
	  
	//serialVersionUID,verify that the sender and receiver of an object
	//have loaded classes with serialization
	private static final long serialVersionUID = 6224984241866802511L;
	private static String host="localhost";  //default hostname
	private static int port=1099;  //default port
	static String a="";
	static String b="";
	static auction rmiServer;  //declare an object of auction
	static Calendar cal = Calendar.getInstance();

	public auctionclient() throws RemoteException {
	      super();
	   }
  
	 public auctionclient(String h, int p) throws RemoteException {
	      super();
	      host = h;
	      port = p; 
	   }

	  
	  public void callBack(String s) throws RemoteException {
	      System.out.println(s);      
	   }

	  public void run() {
		  }
	  
	
	public static void main(String[] args) throws RemoteException{
		
        InputStreamReader in=new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);
        auctionclientserver callbackObj =  new auctionclient();
        int year=0;
        int month=0;
        int day=0;
        int hour=0;
        int minute=0;
        String choiceString;
        int choice;
        String choiceString2;
        int choice2;
        
        
        if (args.length > 0) {  // if the user pass arguments to main, they set for host and port
     		host = args[0];
     	if (args.length > 1){ 
     		port = Integer.decode(args[1]);
     		}
     		}
     	
               
       try {
		  		
		  	rmiServer = (auction) Naming.lookup("rmi://"+host+":"+port+"/AuctionService");  //create the connection with server
		  	 	
		  	} catch (RemoteException e) {
		         System.out.println("Exception in RMIClient.testServer " + e);
		      } catch (java.rmi.NotBoundException e) {
		         System.out.println("RMIClient unable to bind to server " + e);
		      } catch (java.net.MalformedURLException e) {
		         System.out.println("Malformed URL for server " + e);
		      }
       /*
       try { //testing the server
    	   
    	   rmiServer.auctions(1,callbackObj); 
    	   long startTime = System.currentTimeMillis();
    	   for (int i = 0; i < 10000; i++)
    		rmiServer.auctions(1,callbackObj);
    	   long elapsedTime = System.currentTimeMillis() - startTime;
    	   System.out.format("10000 calls in %d ms - %d.%03d ms/call\n",
    	   elapsedTime, elapsedTime/10000, (elapsedTime%10000)/10);
    	   } catch (Exception e) {
    	   e.printStackTrace();
    	   System.exit(1);
    	   }
       */
       try {

    	   do {
    		   
              //print out a menu
              System.out.println("\n------- Menu----------");
              System.out.println("1. Create Auction");
              System.out.println("2. Display Auctions");
              System.out.println("3. Make an offer");
              System.out.println("4. Search an auction by ID");
              System.out.println("5. Auctions that you placed an offer");
              System.out.println("6. Auctions that you created");
              System.out.println("7. Quit");
              System.out.println("Enter choice [1,2,3,4,5,6,7]: ");
              System.out.flush();
              
              choiceString = br.readLine();
              //convert string to integer
              choice = Integer.parseInt(choiceString);
              
              switch (choice)
              {
                
                case 1:
                	
                	System.out.println("Give the Name of the Auction");
            	    a = br.readLine();
            	    
            	    System.out.println("Give a Minimum Price");
            	    b = br.readLine();
            	    
            	    System.out.println("Give Ending Year");
            	    year = Integer.parseInt(br.readLine());
            	    System.out.println("Give Ending Month  (1-12)");
            	    month = Integer.parseInt(br.readLine());
            	    System.out.println("Give Ending Day (1-31)");
            	    day = Integer.parseInt(br.readLine());
            	    System.out.println("Give Ending Hour (1-24)");
            	    hour = Integer.parseInt(br.readLine());
            	    System.out.println("Give Ending Minute  (1-60)");
            	    minute =Integer.parseInt(br.readLine());
            		
            	    cal.set(Calendar.YEAR,year);
            		cal.set(Calendar.MONTH,month);
            		cal.set(Calendar.DAY_OF_MONTH,day);
            		cal.set(Calendar.HOUR_OF_DAY,hour);
            		cal.set(Calendar.MINUTE,minute);
            			
                  	rmiServer.new_auction(a,Integer.parseInt(b),cal,callbackObj);
         
            			 break;
            			 
                case 2:
                	do{
                		System.out.println("1. Display Auctions");
                		System.out.println("2. Sort Auctions by Initial Price");
                		System.out.println("3. Sort Auctions by Status");
                		System.out.println("4. Sort Auctions by Closing Date");
                		System.out.println("5. Return");
                        System.out.println("Enter choice [1,2,3,4,5]: ");
                        System.out.flush();
                        
                        choiceString2 = br.readLine();
                		choice2 = Integer.parseInt(choiceString2);
                		
                        switch (choice2)
                        {	
                        
                        case 1:
                        	 rmiServer.auctions(choice2,callbackObj);
                        	 break;
                        	 
                        case 2:
                        	 rmiServer.auctions(choice2,callbackObj);
                        	break;
                        	
                        case 3: 
                        	 rmiServer.auctions(choice2,callbackObj);
                        	break;
                        	
                        case 4: 
                        	 rmiServer.auctions(choice2,callbackObj);
                        	break;
                        	
                          default:
                            System.out.println("\nPlease choose a number between 1-7 only  \n");
                        }
                				
                	}while(choice2!= 5);
                	
                	break;
                	
                case 3:
	             System.out.println("Give Item ID");
                 int id= Integer.parseInt(br.readLine());
                 
                 System.out.println("Place an Offer");
                 double offer= Double.parseDouble(br.readLine());
                	
               	rmiServer.bid(id,offer,callbackObj);
                  break;
                  
                case 4:
                    System.out.println("Give Item ID");
                   int id2= Integer.parseInt(br.readLine());
                   rmiServer.SearchID(id2,callbackObj);
                 	break;
                 	
                case 5:
                	rmiServer.DisplayItems(callbackObj);
                 	break;
                 	
                case 6: 
                	rmiServer.DisplayAuctions(callbackObj);
                	break;
                
                case 7: //exit
                  System.exit(0);
                  
                default:
                  System.out.println("\nPlease choose a number between 1-6 only  \n");
              }
            }
            while(choice!= 8);     
            
       }
       
	catch (MalformedURLException murle) {
            System.out.println();
            System.out.println("MalformedURLException");
            System.out.println(murle);
        }
        catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        }
       
	catch (IOException e) {
		
		e.printStackTrace();
	}
    }
}

