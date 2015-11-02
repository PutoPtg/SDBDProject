
package RMI;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// BEGIN main
public class DatabaseServer {
    
    
	public static void main(String[] args) {
            

		// You may want a SecurityManager for downloading of classes:
		// System.setSecurityManager(new RMISecurityManager());

		try {
			// Create an instance of the server object
			DatabaseImplementation im = new DatabaseImplementation();

			System.out.println("DatabaseServer starting...");

			// Publish it in the RMI registry.
			// Of course you have to have rmiregistry or equivalent running!
                        Registry r = LocateRegistry.createRegistry(7000);
			r.rebind(DatabaseInterface.LOOKUPNAME, im);

			System.out.println("DatabaseServer ready.");
		} catch (RemoteException e) {
                    System.out.print("ERRO");
			System.err.println(e);
			System.exit(1);
		}
	}
}
// END main