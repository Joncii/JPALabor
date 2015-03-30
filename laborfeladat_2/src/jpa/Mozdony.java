package jpa;
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Mozdony {

	@Id
	private int id;    
    private int futottkm;
    @OneToMany(mappedBy = "mozdony")
    private List<Vonat> vonatok;
    @ManyToOne
    private Tipus tipus;


     
    public Mozdony() {
    	vonatok = new ArrayList<Vonat>();
    }

    public int getFutottkm() {
        return futottkm;
    }

    public void setFutottkm(int futottkm) {
        this.futottkm = futottkm;
    }

	public int getId() {
    	return id;
	}
	
	public void setId(int id){
		
		this.id = id;
		
	}
	
	public List<Vonat> getVonatok(){
		
		return vonatok;
		
	}
	
	public Tipus getTipus(){
		
		return tipus;
		
	}
	
	public void setTipus(Tipus tipus){
		
		this.tipus = tipus;
		
	}
	
	public String toString(){
		
		return new String(id+" "+tipus.getAzonosito()+" "+futottkm);
		
	}

}
