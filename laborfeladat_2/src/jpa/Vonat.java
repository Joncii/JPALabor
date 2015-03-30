package jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Vonat {

	@Temporal(TemporalType.DATE)
    private Date datum;
    private int keses;
    @ManyToOne
    private Vonatszam vonatszam;
    @ManyToOne
    private Mozdony mozdony;


	@Id
	private int id;    
 
	
    public Vonat() {
    }

	public int getId() {
    	return id;
	}

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public int getKeses() {
        return keses;
    }

    public void setKeses(int keses) {
        this.keses = keses;
    }
    
    public Mozdony getMozdony(){
    	
    	return mozdony;
    	
    }
    
    public void setMozdony(Mozdony mozdony){
    	
    	this.mozdony = mozdony;
    	
    }
    
    public Vonatszam getVonatszam(){
    	
    	return vonatszam;
    	
    }
    
    public void setVonatszam(Vonatszam vonatszam){
    	
    	this.vonatszam = vonatszam;
    	
    }
    
    public String toString(){
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(datum);
    	
    	String ret = new String(vonatszam+" "+calendar.get(Calendar.YEAR)+"."+calendar.get(Calendar.MONTH)+"."+calendar.get(Calendar.DAY_OF_MONTH));
    	return ret;
    	
    }

}
