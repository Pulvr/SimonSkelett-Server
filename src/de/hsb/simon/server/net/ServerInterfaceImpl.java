package de.hsb.simon.server.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import valueobjects.Person;
import valueobjects.Ware;
import de.hsb.simon.commons.ClientInterface;
import de.hsb.simon.commons.ServerInterface;
import de.hsb.simon.commons.SessionInterface;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.annotation.SimonRemote;
import de.root1.simon.exceptions.NameBindingException;
import domain.LagerVerwaltung;

//mark this class as a remote class and export all methods known in ServerInterface
@SimonRemote(value = {ServerInterface.class}) 
public class ServerInterfaceImpl implements ServerInterface {
	
	private Registry registry;
	private List<SessionInterface> sessions;
	private LagerVerwaltung lag;
	
	 public ServerInterfaceImpl() {
		 sessions = new ArrayList<SessionInterface>();
	        try {
	        	lag = new LagerVerwaltung();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } 
	 }
	 
	 /**
	  * Der Server wird gestartet
	  * @throws IOException
	  * @throws NameBindingException
	  */
	 public void startServer() throws IOException, NameBindingException{
		 this.registry = Simon.createRegistry(5000);
		 
		 this.registry.bind("server", this);
	 }
	 /**
	  * Um den Server zu stoppen
	  */
	 public void stopServer(){
		 this.registry.unbind("server");
		 
		 this.registry.stop();
	 }
	 
	 public boolean getRunning(){
		 return this.registry.isRunning();
	 }
	 /**
	  * Verbindet Client und Server und returned die session
	  * @param user
	  * @param client
	  * @return SessionInterface - die session
	  */
	 public SessionInterface login (String user, ClientInterface client){
		 
		Session session = new Session(user, client, this);
		 
		sessions.add(session);
		 
		return session;
	
	 }
	 
	 public LagerVerwaltung getLagerVerwaltung(){
		 return this.lag;
	 }
	 
	 public void endSession(SessionInterface session){
		 this.sessions.remove(session);
	 }

	@Override
	public void broadcastAktualisiertePersonenDaten(List<Person> aktualisierteDaten) {
		for(int i=0;i< sessions.size();i++){
			this.sessions.get(i).getClient().erhalteAktualisiertePersonenDaten(aktualisierteDaten);
		}
		
	}

	@Override
	public void broadcastAktualisierteWarenDaten(List<Ware> aktualisierteDaten) {
		for(int i=0;i<sessions.size();i++){
			this.sessions.get(i).getClient().erhalteAktualisierteWarenDaten(aktualisierteDaten);
		}
		
	}
	 
	
}