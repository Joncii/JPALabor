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

    //Uj entitások felvetelehez kapcsolodo szolgaltat�sok
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
    	Query q = em.createQuery("SELECT t FROM Tipus t WHERE t.azonosito=:pazonosito");
    	q.setParameter("pazonosito", azonosito);
    	try {
    		Object o = q.getSingleResult();
    		throw new Exception("Ilyen tipus mar van!");
    	}
    	//Hozza l�tre az �j "Vonatsz�m" entit�st �s r�gz�tse adatb�zisban az "ujEntity" met�dussal.
    	catch (NoResultException e) {
        	Tipus t = new Tipus(azonosito,fajta);
        	this.ujEntity(t);
    	}
    }

    // Uj mozdony felvetele
    public void ujMozdony(String sorszam, String tipusID, String futottkm) throws Exception {
        //TODO
        //Alak�tsa �t a megfelel� t�pusokra a kapott String param�tereket.
        //Ellen�rizze a t�pus l�tez�s�t
    	//Hozza l�tre az �j "Mozdony" entit�st �s r�gz�tse adatb�zisban az "ujEntity" met�dussal.
    	
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
        //Alak�tsa �t a megfelel� t�pusokra a kapott String param�tereket.
        //Ellen�rizze, hogy van-e m�r ilyen vonatsz�m
    	//Hozza l�tre az �j "Vonatsz�m" entit�st �s r�gz�tse adatb�zisban az "ujEntity" met�dussal.
    	
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
        //Alak�tsa �t a megfelel� t�pusokra a kapott String param�tereket. Tipp: haszn�lja a SimpleDateFormat-ot
    	//Form�tum: "yyyy.MM.dd"
        //Ellen�rizze, hogy �rv�nyes-e a vonatsz�m, �s l�tezik a mozdony.
        //Ellen�rizze, hogy az adott napon nincs m�sik vonat ugyanezzel a vonatsz�mmal.		
    	//Hozza l�tre az �j "Vonat" entit�st �s r�gz�tse adatb�zisban az "ujEntity" met�dussal.
        //N�velje a mozdony futottkm-�t a vonatsz�m szerinti �thosszal. 
    	
    	Date _datum;
    	
    	try {
			
//    		_datum = buildDateFromString(datum);
        	_datum = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH).parse(datum);    		
    		
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
    	//K�sz�tsen lek�rdez�st, amely visszaadja az �sszes mozdonyt, majd
        //irassa ki a listazEntity met�dussal az eredm�nyt.
    	
    	List<Mozdony> resultList = em.createQuery("SELECT m FROM Mozdony m").getResultList();
    	
    	listazEntity(resultList);
    	
    }

    //Vonatszamok listazasa
    public void listazVonatszam() throws Exception {
    	//TODO    	
    	//K�sz�tsen lek�rdez�st, amely visszaadja az �sszes vonatsz�mot, majd
        //irassa ki a listazEntity met�dussal az eredm�nyt.
    	
    	List<Vonatszam> resultList = em.createQuery("SELECT v FROM Vonatszam v").getResultList();
    	
    	listazEntity(resultList);
    }

    //Vonatok listazasa
    public void listazVonat() throws Exception {
    	//TODO    	
    	//K�sz�tsen lek�rdez�st, amely visszaadja az �sszes vonatot, majd
        //irassa ki a listazEntity met�dussal az eredm�nyt.
    	
    	List<String> list = new ArrayList<String>();
    	Query q = em.createQuery("SELECT v FROM Vonat v");
    	List<Vonat> vList = (List<Vonat>)(q.getResultList());
    	Iterator<Vonat> i = vList.iterator();
    	while(i.hasNext()) {
    		Vonat v = i.next();
    		int keses = v.getKeses(); //Ki�rni  5.
    		Date date=v.getDatum();   //Ki�rni  2.
    		Mozdony m=v.getMozdony();  
    		Vonatszam vsz=v.getVonatszam();
    		int vsz_id=vsz.getSzam();  //Ki�rni 1.
    		int m_id=m.getId();    //Ki�rni 3.
    		int m_fk=m.getFutottkm();  //Ki�rni 4.
    		
    		String s_m_id=Integer.toString(m_id);
    		String s_vsz_id=Integer.toString(vsz_id);
    		String s_keses=Integer.toString(keses);
    		SimpleDateFormat formatter=new SimpleDateFormat("yyyy.MM.dd");
    		String s_date=formatter.format(date);
    		String s_m_fk=Integer.toString(m_fk);
    		    		
    		String element=s_vsz_id+" "+s_date+" "+s_m_id+" "+s_m_fk+" "+s_keses;
    		list.add(element);
    	}
        //irassa ki a listazEntity met�dussal az eredm�nyt.
        listazEntity(list);
    }

    //Egyedi lekerdezes
    public void lekerdezes(String datum) throws Exception {
    	//TODO    	
        //�rja ki a param�terk�nt kapott napra (INPUTNAP) vonatkoz�an, hogy az
        //egyes mozdony-fajt�k az adott napon �sszesen h�ny kilom�tert futottak.    	
        //Alak�tsa �t a megfelel� t�pusokra a kapott String param�tereket. Tipp: haszn�lja a SimpleDateFormat-ot
        //Tipp: N�zzen ut�na a "t�bbsz�r�s SELECT" kezel�s�nek
    	
    	Date date = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH).parse(datum);
    	Query myquery=em.createQuery("SELECT t.fajta, SUM(vsz.uthossz) FROM Tipus t, Mozdony m, Vonat v, Vonatszam vsz WHERE "
    			+ "v.datum = :pdate AND v.vonatszam = vsz AND v.mozdony = m AND m.tipus = t "
    			+ "GROUP BY t.fajta");
    	myquery.setParameter("pdate", date);
    	
    	List<Object[]> my_result=myquery.getResultList();
    	List<String> list = new ArrayList<String>();  //this gonna be the result list
    	
    	Iterator<Object[]> i = my_result.iterator();
    	
    	while(i.hasNext()){
    		Object[] my_object_array = i.next();
    		String fajta_nev=(String)my_object_array[0];
    		Long km=(Long)my_object_array[1];
    		String s_km=Long.toString(km);
    		String element=fajta_nev+" "+s_km;
    		list.add(element);
    	}	
    	
    	listazEntity(list);	    	
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
