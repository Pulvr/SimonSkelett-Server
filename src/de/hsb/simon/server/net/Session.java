package de.hsb.simon.server.net;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import valueobjects.*;
import de.hsb.simon.commons.ClientInterface;
import de.hsb.simon.commons.SessionInterface;
import de.root1.simon.annotation.SimonRemote;
import domain.*;
import exceptions.BestellteMengeNegativException;
import exceptions.NichtVielfachesVonPackGroesseException;
import exceptions.PersonExistiertBereitsException;
import exceptions.PersonExistiertNichtException;
import exceptions.WareExistiertBereitsException;
import exceptions.WareExistiertNichtException;

@SimonRemote(value = {SessionInterface.class})
public class Session implements SessionInterface{

	private String user;
	private ClientInterface client;
	private ServerInterfaceImpl server;
	private LagerVerwaltung lag;
	
	public Session(String user, ClientInterface client, ServerInterfaceImpl server) {
		this.user = user;
		this.client = client;
		this.server = server;
		this.lag = this.server.getLagerVerwaltung();
	}
	
	public void unreferenced(){
		this.server.endSession(this);
	}
	
	public ClientInterface getClient(){
		return this.client;
	}
	
	@Override
	public List<Ware> gibAlleWaren(){
		return this.lag.gibAlleWaren();
	}

	@Override
	public List<Person> gibAllePersonen() {
		return this.lag.gibAllePersonen();
	}
	
	@Override
	public HashMap<String,Ware> getMeineWarenVerwaltung() {
		return this.lag.getMeineWarenVerwaltung();
	}

	@Override
	public HashMap<String,Person> getMeinePersonenVerwaltung() {
		return this.lag.getMeinePersonenVerwaltung();
	}
	
	@Override
	public List<Ware> sucheNachBezeichnung(String bezeichnung) {
		return lag.sucheNachBezeichnung(bezeichnung);
	}

	@Override
	public void fuegeWareEin(String bezeichnung, int nummer, int bestand, float preis, int packungsGroesse) throws WareExistiertBereitsException {
		this.lag.fuegeWareEin(bezeichnung, nummer, bestand, preis,packungsGroesse);
		List<Ware> aktualisierteDaten = this.lag.gibAlleWaren();
		this.server.broadcastAktualisierteWarenDaten(aktualisierteDaten);
		//SYNCHRONIZED
		
	}

	@Override
	public void entferneWare(Ware eineWare) throws WareExistiertNichtException {
		this.lag.entferneWare(eineWare);
		List<Ware> aktualisierteDaten = this.lag.gibAlleWaren();
		this.server.broadcastAktualisierteWarenDaten(aktualisierteDaten);
		//SYNCHRONIZED
		
	}

	@Override
	public void aendereBestand(Ware w, int neuerBestand)throws WareExistiertNichtException, IOException {
		this.lag.aendereBestand(w, neuerBestand);
		List<Ware> aktualisierteDaten = this.lag.gibAlleWaren();
		this.server.broadcastAktualisierteWarenDaten(aktualisierteDaten);
		//SYNCHRONIZED
	}

	@Override
	public void fuegePersonEin(int nr, String name, String anr, String strasse,String plz, String ort, String email, String usr, String pw,boolean ma)
			throws PersonExistiertBereitsException {
		this.lag.fuegePersonEin(nr, name, anr, strasse, plz, ort, email, usr, pw, ma);
		List<Person> aktualisierteDaten = this.lag.gibAllePersonen();
		this.server.broadcastAktualisiertePersonenDaten(aktualisierteDaten);
		//SYNCHRONIZED
	}

	@Override
	public void personEntfernen(Person einePerson)throws PersonExistiertNichtException {
		this.lag.personEntfernen(einePerson);
		List<Person> aktualisierteDaten = this.lag.gibAllePersonen();
		this.server.broadcastAktualisiertePersonenDaten(aktualisierteDaten);
		//SYNCHRONIZED
	}

	@Override
	public void sortiereDieWaren(String aufgabe) {
		this.lag.sortiereDieWaren(aufgabe);
		
	}

	@Override
	public void schreibeWaren() throws IOException {
		this.lag.schreibeWaren();
		
	}

	@Override
	public void schreibePersonen() throws IOException {
		this.lag.schreibePersonen();
		
	}

	
	public void warenkorbKaufen(Person einePerson, Vector<Ware> korb) {
		this.lag.warenkorbKaufen(einePerson, korb);
		List<Ware> aktualisierteDaten = this.lag.gibAlleWaren();
		this.server.broadcastAktualisierteWarenDaten(aktualisierteDaten);
		//SYNCHRONIZED
	}

	@Override
	public void inWarenKorbLegen(int menge, Ware ware, Person p)throws BestellteMengeNegativException, NichtVielfachesVonPackGroesseException {
		this.lag.inWarenKorbLegen(menge, ware, p);
		
	}

	@Override
	public void entferneAusWarenkorb(int menge, Ware eineware, Person einePerson)
			throws BestellteMengeNegativException {
		this.lag.entferneAusWarenkorb(menge, eineware, einePerson);
		
	}

	@Override
	public void warenkorbLeeren(Person einePerson) {
		this.warenkorbLeeren(einePerson);
		
	}

	@Override
	public Vector<WarenLog> getWarenLog(String bezeichnung, int daysInPast)throws IOException, ParseException {
		return this.lag.getWarenLog(bezeichnung, daysInPast);
	}

	


	@Override
	public Rechnung getRechnung() {
		// TODO Auto-generated method stub
		return this.lag.getRechnung();
	}

	@Override
	public void setRechnung(Rechnung r) {
		this.lag.setRechnung(r);
		
	}

}