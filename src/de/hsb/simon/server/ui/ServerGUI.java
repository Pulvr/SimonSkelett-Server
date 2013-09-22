package de.hsb.simon.server.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.hsb.simon.server.net.ServerInterfaceImpl;
import de.root1.simon.exceptions.NameBindingException;

public class ServerGUI extends JFrame {

	private static final long serialVersionUID = 3256901050204363805L;
	
	/** Netzwerk */
	private ServerInterfaceImpl connection;
	
	/** GUI-Komponenten */
	private JLabel status;		// Status des Servers
	private JButton start;		// Button zum Starten des Servers
	private JButton stop;		// Button zum Stoppen des Servers

	/**
	 * Konstruktor
	 */
	public ServerGUI() {
		super("Simon Server");

		connection = new ServerInterfaceImpl();
		
		// Initialisiere GUI
		this.initComponents();

		// Füge Listener hinzu
		this.initListeners();

		// Setze Frame sichtbar
		this.setVisible(true);
	}

	/**
	 * Initialisiert die GUI-Komponenten
	 */
	private void initComponents() {
		// Allgemeine Eigenschaften
		this.setSize(300, 100);
		this.setLayout(new GridLayout(1, 3));
		
		
		// Komponenten initialisieren
		status = new JLabel("Status: stopped");
		start = new JButton("Start");
		stop = new JButton("Stop");
		
		// Einfügen (von oben links nach unten rechts)
		this.add(status);
		this.add(start);
		this.add(stop);
	}

	/**
	 * Initialisiert die Listener für einige Komponenten
	 */
	private void initListeners() {
		// Listener zum Starten des Servers
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try{	
					connection.startServer();
					status.setText("Status: started");
				}catch(IOException|NameBindingException e){
					e.printStackTrace();
				}
			}
		});
		
		// Listener zum Beenden des Servers
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connection.stopServer();
				status.setText("Status: stopped");
			}
		});
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				//wenn die server gui sofort beendet wird führt das stoppen zur NullpointerException
				//Darum wird hier geschaut ob der Status des Labes umgestellt wurde oder nicht
				if(connection.getRunning()==true){
					connection.stopServer();
					System.exit(0);
				}else{
				System.exit(0);
				}
			}
		});
	}

	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new ServerGUI();
	}
}
