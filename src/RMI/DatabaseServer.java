
package RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;

// BEGIN main
public class DatabaseServer {
    
    
	public static void main(String[] args) throws MalformedURLException {
            

		// You may want a SecurityManager for downloading of classes:
		// System.setSecurityManager(new RMISecurityManager());

		try {
			// Create an instance of the server object
			

			System.out.println("DatabaseServer starting...");

			try { //special exception handler for registry creation
	            LocateRegistry.createRegistry(1099); 
	            System.out.println("java RMI registry created.");
	        } catch (RemoteException e) {
	            //do nothing, error means registry already exists
	            System.out.println("java RMI registry already exists.");
	        }			
			
			DatabaseImplementation im = new DatabaseImplementation();
			
			Naming.rebind("//localhost/DatabaseInterface", im);
	        System.out.println("PeerServer bound in registry");
			//System.setProperty( "java.rmi.server.hostname", "127.0.0.1" ) ;
                        //Registry r = LocateRegistry.createRegistry(7000);
			//r.rebind(DatabaseInterface.LOOKUPNAME, im);
			

			System.out.println("DatabaseServer ready.");
		} catch (RemoteException e) {
                    System.out.print("ERRO");
			System.err.println(e);
			System.exit(1);
		}
	}
}
// END main