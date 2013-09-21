package de.hsb.simon.server.net;

import java.util.ArrayList;
import java.util.List;

import de.hsb.simon.commons.ClientCallbackInterface;
import de.hsb.simon.commons.ServerInterface;
import de.hsb.simon.commons.SessionInterface;
import de.root1.simon.annotation.SimonRemote;
//import de.hsb.simon.commons.SessionInterface;
//import de.root1.simon.Registry;

//mark this class as a remote class and export all methods known in ServerInterface
@SimonRemote(value = {ServerInterface.class})
public class ServerInterfaceImpl implements ServerInterface {

//	private Registry registry;
//	private List<SessionInterface> sessions;
	private static final long serialVersionUID = 1L;

    // this is where all user sessions are stored
    private List<SessionInterface> userSessions = new ArrayList<SessionInterface>();

    @Override
    public SessionInterface login(String user, ClientCallbackInterface clientCallback) {
        System.out.println("login. user="+user);
        clientCallback.callback("Login is in progress ...");
        System.out.flush();
        Session session = new Session(user, this);
        userSessions.add(session);
        clientCallback.callback("Session is created ... Now "+userSessions.size()+" users are online ...");
        System.out.println("Session created for user "+user+". Now "+userSessions.size()+" users are online ...");
        return session;
    }

    // if a session get's unreferenced, the session is removed from the list
    void removeUserSession(Session userSession) {
        userSessions.remove(userSession);
        System.out.println("Removed user "+userSession.getUsername()+" from sessionlist. "+userSessions.size()+" user are online.");
    }
	
//	public ServerInterfaceImpl() {
//		//
//	}
}
