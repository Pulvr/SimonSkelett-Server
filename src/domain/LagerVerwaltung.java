package domain;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import valueobjects.Person;
import valueobjects.Rechnung;
import valueobjects.Ware;
import valueobjects.WarenLog;
import domain.WarenVerwaltung.Sortierung;
import exceptions.BestellteMengeNegativException;
import exceptions.PersonExistiertBereitsException;
import exceptions.PersonExistiertNichtException;
import exceptions.WareExistiertBereitsException;
import exceptions.WareExistiertNichtException;

/**
 * Klasse zur Verwaltung eines (sehr einfachen) Lagers.
 * Bietet Methoden zum Zurï¿½ckgeben aller Waren im Bestand, 
 * zur Suche nach Waren, zum Einfï¿½gen neuer Waren 
 * und zum Speichern des Bestands.
 * 
 * 
 */
public class LagerVerwaltung {
	// Namen der Dateien, in der die Lagerdaten gespeichert sind
	
	private WarenVerwaltung meineWaren;
	private PersonenVerwaltung meinePersonen;
	private Rechnung rechnung;
	
	/**
	 * Konstruktor, der die Basisdaten (Waren, Personen) aus Dateien einliest
	 * (Initialisierung des shops).
	 * 
	 * Namensmuster für Dateien:
	 *   LAG_W.ser" ist die Datei der Waren
	 *   LAG_P.ser" ist die Datei der Personen
	 * 
	 * @param datei
	 * @throws IOException, z.B. wenn eine der Dateien nicht existiert.
	 */
	public LagerVerwaltung() throws IOException {
		//this.datei = datei;
		
		// Warenbestand aus Datei einlesen
		meineWaren = new WarenVerwaltung();
		meineWaren.liesDaten("LAG_W.ser");
		
		// Kundenkartei aus Datei einlesen
		meinePersonen = new PersonenVerwaltung();
		meinePersonen.liesDaten("LAG_P.ser");
	}

	/**
	 * Methode, die eine Liste aller im Bestand befindlichen Waren zurückgibt.
	 * 
	 * @return Liste aller Waren im Lager
	 */
	public List<Ware> gibAlleWaren() {
		// einfach delegieren an meineWaren
		return meineWaren.getWarenBestand();
	}
	
	/**
	 * Methode, die eine Liste aller Personen gibt
	 * 
	 * @return Liste aller Personen
	 */
	public List<Person> gibAllePersonen() {
		// einfach delegieren an meinePersonen
		return meinePersonen.getPersonen();
	}
	
	public HashMap<String,Ware> getWarenObjekte(){
		return meineWaren.getWarenObjekte();
	}
	
	public HashMap<String,Person> getPersonenObjekte(){
		return meinePersonen.getPersonenObjekte();
	}

	/**
	 * Methode zum Suchen von Waren anhand der Bezeichnung. Es wird eine Liste von Waren
	 * zurï¿½ckgegeben, die alle Waren mit exakt ï¿½bereinstimmender Bezeichnung enthï¿½lt.
	 * 
	 * @param bezeichnung Bezeichnung der gesuchten Ware
	 * @return Liste der gefundenen Waren (evtl. leer)
	 */
	public List<Ware> sucheNachBezeichnung(String bezeichnung) {
		// einfach delegieren an meineWaren
		return meineWaren.sucheWaren(bezeichnung); 
	}

	/**
	 * Methode zum Einfï¿½gen einer neuen Ware in den Bestand. 
	 * Wenn die Ware bereits im Bestand ist, wird der Bestand nicht geï¿½ndert.
	 * 
	 * @param bezeichnung Bezeichnung des Ware
	 * @param nummer Nummer der Waren
	 * @throws WareExistiertBereitsException wenn die Ware bereits existiert
	 */
	public void fuegeWareEin(String bezeichnung, int nummer, int bestand, float preis) throws WareExistiertBereitsException {
		Ware w = new Ware(bezeichnung, nummer,  bestand, preis);
		meineWaren.wareEinfuegen(w);
	}
	
	/**
	 * Methode zum lï¿½schen von Waren aus dem Bestand
	 * @param eineWare
	 * @throws WareExistiertNichtException
	 */
	public void entferneWare(Ware eineWare)throws WareExistiertNichtException{
		meineWaren.entferneWare(eineWare);
	}
	
	/**
	 * Methode zum neusetzen des Bestands einer Ware, ohne Addition oder Subtraktion
	 * @param w Die Ware
	 * @param neuerBestand
	 * @throws WareExistiertNichtException
	 */
	public void aendereBestand(Ware w,int neuerBestand)throws WareExistiertNichtException, IOException{
		meineWaren.aendereBestand(w, neuerBestand);
	}
	
	/**
	 * Methode zum EinfÃ¼gen einer Person in eine Liste
	 * 
	 *
	 * @throws PersonExistiertBereitsException wenn die Ware bereits existiert wird aber noch nicht verwendet
	 */
	public void fuegePersonEin(int nr, String name, String anr, String strasse, String plz, String ort ,String email, String usr, String pw, boolean ma) throws PersonExistiertBereitsException {
		Person p = new Person(nr,name,anr,strasse,plz,ort , email, usr, pw, ma);
		meinePersonen.personEinfuegen(p);
	}
	
	/**
	 * Methode zum lï¿½schen einer Person
	 * 
	 * @param einePerson
	 * @throws PersonExistiertNichtException
	 */
	public void personEntfernen(Person einePerson) throws PersonExistiertNichtException{
		meinePersonen.personEntfernen(einePerson);
	}
	
	/**
	 * Methode zum sortieren des Vectors der die Waren speichert.
	 * Nach Bezeichnung oder Waren nummer
	 * 
	 * @param aufgabe soll nach Bezeichnung oder Nummer sortiert werden?
	 */
	 public void sortiereDieWaren(String aufgabe) {
		 if (aufgabe.equals ("b")){
			 meineWaren.artikelSortieren(Sortierung.Bezeichnung);   
		 }else if (aufgabe.equals("n")){
			 meineWaren.artikelSortieren(Sortierung.Nummer); 
		 }else if (aufgabe.equals("e")){
			 meineWaren.artikelSortieren(Sortierung.Bestand);
		 }/*else if (aufgabe.equals("p")){
			 meineWaren.artikelSortieren(Sortierung.Preis);
		 }*/
	 }

	/**
	 * Methode zum Speichern des Warenbestands in einer Datei.
	 * 
	 * @throws IOException
	 */
	public void schreibeWaren() throws IOException {
		meineWaren.schreibeDaten("LAG_W.ser");
	}
	
	/**
	 * Methode zum speichern der Personen in einer Datei
	 * @throws IOException
	 */
	public void schreibePersonen() throws IOException {
		meinePersonen.schreibeDaten("LAG_P.ser");
	}
	
	/**
	 * den Inhalt des Warenkorbs kaufen
	 * @param p Die Person
	 * @param warenkorb der Warenkorb der Person
	 */
	public void warenkorbKaufen(Person p, Vector<Ware> warenkorb){
		meinePersonen.warenkorbKaufen(p, warenkorb);
	}
	
	/**
	 * Methode mit der Waren in den Korb gelegt werden
	 * @param menge wieviele Waren sollen gekauft werden?
	 * @param ware welche Ware?
	 * @param p welche Person?
	 * @throws BestellteMengeNegativException
	 */
	public void inWarenKorbLegen(int menge, Ware ware, Person p) throws BestellteMengeNegativException{
		meinePersonen.inWarenkorbLegen(menge, ware, p);
	}
	
	/**
	 * Methode die Waren aus dem Korb entfernen kann, sollte die angegebene Zahl die Anzahl der Waren im Korb ï¿½bersteigen
	 * wird die Anzahl dieser Ware im Korb auf 0 gesetzt anstatt ins negative zu gehen
	 * @param menge Wieviele von der Ware entfernen
	 * @param ware welche Ware soll entfernt werden
	 * @param p Der Korb der Person
	 * @throws BestellteMengeNegativException
	 */
	public void entferneAusWarenkorb(int menge, Ware ware, Person p)throws BestellteMengeNegativException{
		meinePersonen.entferneAusWarenkorb(menge, ware, p);
	}
	
	/**
	 * Methode die den Warenkorb eines angegeben Users leert
	 * @param einePerson Name der Person
	 */
	public void warenkorbLeeren(Person einePerson){
		meinePersonen.warenkorbLeeren(einePerson);
	}
	
	/**
	 * Gibt den Warenlog fï¿½r eine Ware zurï¿½ck mit einer Angabe wie weit der Log zurï¿½ck liegen soll
	 * @param bezeichnung Log fï¿½r welche Ware?
	 * @param daysInPast wieviele Tage soll der Log zurï¿½ck liegen
	 * @return den WarenLog
	 * @throws IOException
	 * @throws ParseException
	 */
	public Vector<WarenLog> getWarenLog(String bezeichnung, int daysInPast)throws IOException,ParseException{
		return meineWaren.getWarenLog(bezeichnung, daysInPast);
	}
	
	/**
	 * Gibt die PersonenVerwaltung zurück
	 * @return
	 */
	public PersonenVerwaltung getMeinePersonenVerwaltung(){
		return this.meinePersonen;
	}
	
	/**
	 * Gibt die WarenVerwaltung zurück
	 * @return
	 */
	public WarenVerwaltung getMeineWarenVerwaltung(){
		return this.meineWaren;
	}
	
	/**
	 * Gibt die Rechnung die beim Kauf entsteht zurück
	 * @return
	 */
	public Rechnung getRechnung(){
		return this.rechnung;
	}
	/**
	 * Hiermit lässt sich die Rechnung für einen Kauf setzen
	 * @param r die Rechnung
	 */
	public void setRechnung(Rechnung r){
		this.rechnung = r;
	}
}
