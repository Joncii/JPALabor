package jpa;

import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.NoResultException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.derby.tools.sysinfo;

public class Program {

	private EntityManagerFactory factory;
	private EntityManager em;


	public void initDB() {
		factory = Persistence.createEntityManagerFactory(CommonData.getUnit());
		em = factory.createEntityManager();
	}

	void closeDB() {

		em.close();
	}

	public Program(EntityManager em) {
	        this.em = em;
	}	

	public Program() {
}	
	
	public static void main(String[] args) {
		Program app = new Program();
		app.initDB();
		app.startControl();
		app.closeDB();
	
    }


    public void startControl() {
//	    InputStream input = System.in;
        BufferedReader instream = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.print(">");
                String inputLine = instream.readLine();
                StringTokenizer tokenizer = new StringTokenizer(inputLine, "   ");
                String command = tokenizer.nextToken();

                if ("t".startsWith(command)) {
                    ujTipus(readString(tokenizer), readString(tokenizer));
                } else if ("m".startsWith(command)) {
                    ujMozdony(readString(tokenizer), readString(tokenizer), readString(tokenizer));
                } else if ("s".startsWith(command)) {
                    ujVonatszam(readString(tokenizer), readString(tokenizer));
                } else if ("v".startsWith(command)) {
                    ujVonat(readString(tokenizer), readString(tokenizer),  readString(tokenizer), readString(tokenizer));
                } else if ("l".startsWith(command)) {
                    String targy = readString(tokenizer);
                    if ("t".startsWith(targy)) {
                        listazTipus();
                    } else if ("m".startsWith(targy)) {
                        listazMozdony();
                    } else if ("s".startsWith(targy)) {
                        listazVonatszam();
                    } else if ("v".startsWith(targy)) {
                        listazVonat();
                    }
                } else if ("x".startsWith(command)) {
                    lekerdezes(readString(tokenizer));
                } else if ("e".startsWith(command)) {
                    break;
                } else {
                    throw new Exception("Hibas parancs! (" + inputLine + ")");
                }
            } catch (Exception e) {
                System.out.println("? " + e.toString());
            }
        }

    }

    static String readString(StringTokenizer tokenizer) throws Exception {
        if (tokenizer.hasMoreElements()) {
            return tokenizer.nextToken();
        } else {
            throw new Exception("Keves parameter!");
        }
    }

    //Uj entitÃ¡sok felvetelehez kapcsolodo szolgaltatások
    protected void ujEntity(Object o) throws Exception {
        em.getTransaction().begin();
        try {
            em.persist(o);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    // Uj tipus felvetele
    public void ujTipus(String azonosito, String fajta) throws Exception {
        //TODO
        //Hozza létre az új "Tipus" entitást és rögzítse adatbázisban az "ujEntity" metódussal.
    	
    	Tipus ujTipus = new Tipus(azonosito, fajta);
    	ujEntity(ujTipus);
    }

    // Uj mozdony felvetele
    public void ujMozdony(String sorszam, String tipusID, String futottkm) throws Exception {
        //TODO
        //Alakítsa át a megfelelõ típusokra a kapott String paramétereket.
        //Ellenõrizze a típus létezését
    	//Hozza létre az új "Mozdony" entitást és rögzítse adatbázisban az "ujEntity" metódussal.
    	
    	int _sorszam;
    	int _futottkm;
    	
    	try {
    		
    		_sorszam = Integer.parseInt(sorszam);
        	_futottkm = Integer.parseInt(futottkm);
        	
		} catch (Exception e) {
			
			System.out.println("?Hiba");
			return;
			
		}
    	
    	Tipus tipus = (Tipus) em.createQuery("SELECT t FROM Tipus t WHERE t.azonosito = :azon").setParameter("azon", tipusID).getSingleResult();
    	List mozdonyList = em.createQuery("SELECT m FROM Mozdony m WHERE m.id = :id").setParameter("id", new Integer(_sorszam)).getResultList();
    	
    	if((tipus!=null) && mozdonyList.isEmpty()){
    		
    		Mozdony ujMozdony = new Mozdony();
    		ujMozdony.setTipus(tipus);
    		ujMozdony.setFutottkm(_futottkm);
    		ujMozdony.setId(_sorszam);
    		
    		ujEntity(ujMozdony);
    		
    	}
    	else{
    		
    		System.out.println("?Hiba");
    		
    	}
    	
    }

    // Uj vonatszam felvetele
    public void ujVonatszam(String sorszam, String uthossz) throws Exception {
        //TODO
        //Alakítsa át a megfelelõ típusokra a kapott String paramétereket.
        //Ellenõrizze, hogy van-e már ilyen vonatszám
    	//Hozza létre az új "Vonatszám" entitást és rögzítse adatbázisban az "ujEntity" metódussal.
    	
    	int _sorszam;
    	Long _uthossz;
    	
    	try {
    		
    		_sorszam = Integer.parseInt(sorszam);
    		_uthossz = Long.parseLong(uthossz);
    		
		} catch (NumberFormatException e) {
			
			System.out.println("?Hiba");
			return;
			
		}

    	List resultList = em.createQuery("SELECT v FROM Vonatszam v WHERE v.szam = :szam").setParameter("szam", new Integer(_sorszam)).getResultList();
    	
    	if(resultList.isEmpty()){
    		
    		Vonatszam ujVonatszam = new Vonatszam();
    		ujVonatszam.setSzam(_sorszam);
    		ujVonatszam.setUthossz(_uthossz);
    		
    		ujEntity(ujVonatszam);
    		
    	}
    	else{
    		
    		System.out.println("?Hiba");
    		
    	}
    	
    	
    }

    // Uj vonat felvetele
    public void ujVonat(String vonatszamAzonosito, String datum, String mozdonySorszam, String keses) throws Exception {
       	//TODO
        //Alakítsa át a megfelelõ típusokra a kapott String paramétereket. Tipp: használja a SimpleDateFormat-ot
    	//Formátum: "yyyy.MM.dd"
        //Ellenõrizze, hogy érvényes-e a vonatszám, és létezik a mozdony.
        //Ellenõrizze, hogy az adott napon nincs másik vonat ugyanezzel a vonatszámmal.		
    	//Hozza létre az új "Vonat" entitást és rögzítse adatbázisban az "ujEntity" metódussal.
        //Növelje a mozdony futottkm-ét a vonatszám szerinti úthosszal. 
    	
    	Date _datum;
    	
    	try {
			
    		_datum = buildDateFromString(datum);
    		
		} catch (Exception e) {
			
			System.out.println("?Hiba");
			return;
			
		}
    	
    	int _vonatszamAzonosito;
    	int _mozdonySorszam;
    	int _keses;
    	
    	try {
    		
    		_vonatszamAzonosito = Integer.parseInt(vonatszamAzonosito);
    		_mozdonySorszam = Integer.parseInt(mozdonySorszam);
    		_keses = Integer.parseInt(keses);
			
		} catch (Exception e) {
			
			System.out.println("?Hiba");
			return;
		}
    	
    	Vonatszam vonatszam = (Vonatszam) em.createQuery("SELECT vsz FROM Vonatszam vsz WHERE vsz.szam = :vonatazon").setParameter("vonatazon", _vonatszamAzonosito).getSingleResult();
    	Mozdony mozdony = (Mozdony) em.createQuery("SELECT m FROM Mozdony m WHERE m.id = :mozdonyazon").setParameter("mozdonyazon", _mozdonySorszam).getSingleResult();
    
    	boolean vonatszamValid = false;
    	if(vonatszam != null){
    		
    		vonatszamValid = isUniqueVonatszam(_datum, vonatszam);
    		
    	}
    	
    	if(vonatszamValid && mozdony!=null){
    		
    		Vonat vonat = new Vonat();
    		vonat.setDatum(_datum);
    		vonat.setKeses(_keses);
    		vonat.setVonatszam(vonatszam);
    		vonat.setMozdony(mozdony);
    		
    		ujEntity(vonat);
    		
    		Integer futottkm = new Integer(mozdony.getFutottkm());
    		int ujFuttotkm = (int) (vonatszam.getUthossz() + futottkm);
    		mozdony.setFutottkm(ujFuttotkm);
    		
    	}
    	else{
    		
    		System.out.println("?Hiba");
    		
    	}
    
    }

    //Listazasi szolgaltatasok
    public void listazEntity(List list) {
        for (Object o : list) {
            System.out.println(o);
        }
    }

    //Tipusok listazasa
    public void listazTipus() throws Exception {
        listazEntity(em.createQuery("SELECT t FROM Tipus t").getResultList());
    }

    //Mozdonyok listazasa
    public void listazMozdony() throws Exception {
    	//TODO    	
    	//Készítsen lekérdezést, amely visszaadja az összes mozdonyt, majd
        //irassa ki a listazEntity metódussal az eredményt.
    	
    	List<Mozdony> resultList = em.createQuery("SELECT m FROM Mozdony m").getResultList();
    	
    	listazEntity(resultList);
    	
    }

    //Vonatszamok listazasa
    public void listazVonatszam() throws Exception {
    	//TODO    	
    	//Készítsen lekérdezést, amely visszaadja az összes vonatszámot, majd
        //irassa ki a listazEntity metódussal az eredményt.
    	
    	List<Vonatszam> resultList = em.createQuery("SELECT v FROM Vonatszam v").getResultList();
    	
    	listazEntity(resultList);
    }

    //Vonatok listazasa
    public void listazVonat() throws Exception {
    	//TODO    	
    	//Készítsen lekérdezést, amely visszaadja az összes vonatot, majd
        //irassa ki a listazEntity metódussal az eredményt.
    	
    	List<Vonat> resultList = em.createQuery("SELECT v FROM Vonat v").getResultList();
    	
    	listazEntity(resultList);
    }

    //Egyedi lekerdezes
    public void lekerdezes(String datum) throws Exception {
    	//TODO    	
        //Írja ki a paraméterként kapott napra (INPUTNAP) vonatkozóan, hogy az
        //egyes mozdony-fajták az adott napon összesen hány kilométert futottak.    	
        //Alakítsa át a megfelelõ típusokra a kapott String paramétereket. Tipp: használja a SimpleDateFormat-ot
        //Tipp: Nézzen utána a "többszörös SELECT" kezelésének
    }
    
    private Date buildDateFromString(String dateString) throws ParseException{
    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    	Date date = dateFormat.parse(dateString);
    	
    	return date;
    	
    }
    
    private boolean isUniqueVonatszam(Date date, Vonatszam vonatszam){
    	
    	boolean unique;
    	
    	List<Vonatszam> vonatszamList = em.createQuery("SELECT v.vonatszam FROM Vonat v WHERE v.datum = :datumParam").setParameter("datumParam", date).getResultList();
    	
    	if(vonatszamList.contains(vonatszam)){
    		
    		unique = false;
    		
    	}
    	else{
    		
    		unique = true;
    		
    	}
    	
    	return unique;
    	
    }
}
