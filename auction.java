//interface of server
import java.util.Calendar;

	

public interface auction extends java.rmi.Remote {	
    

    public void auctions(int flag, auctionclientserver client) throws java.rmi.RemoteException;
    
    public void SearchID(int cod, auctionclientserver client)throws java.rmi.RemoteException;
    
    public void DisplayItems(auctionclientserver client)throws java.rmi.RemoteException;
    
    public void DisplayAuctions(auctionclientserver client)throws java.rmi.RemoteException;
    
    public void new_auction(String a,int d, Calendar c, auctionclientserver client)throws java.rmi.RemoteException;
    
    public void bid(int id, Double offer,auctionclientserver clien)throws java.rmi.RemoteException;
    
}

