//interface of client

import java.rmi.*;

public interface auctionclientserver extends Remote {
   public void callBack(String s) throws java.rmi.RemoteException;
}

