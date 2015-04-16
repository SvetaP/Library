package librarync;

import java.util.*;

public class Customer {
	private String name;
	private Integer number;
	private List<Specimen> specimens;
	
	{
		specimens = new ArrayList<Specimen>();
	}
	
	public String getName () {
		return this.name;
	}
	
	public int getNumber () {
		return this.number;
	}
	
	public List<Specimen> getSpecimens () {
		return this.specimens;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void setNumber (int number) {
		this.number = number;
	}
	
	public void setSpecimens (List<Specimen> specimens) {
		List<Specimen> specimens2 = new ArrayList<Specimen>();
		for(Specimen s : specimens){
			if(s.getAvailability() == Availability.AVAILABLE){
				s.setNotAvailable();
				specimens2.add(s);
			}
			System.out.println("This specimen is NOTAVAILABLE");
			
		}
		this.specimens = specimens2;
	}
	
	public Customer () {
		super();
	}
	
	public Customer (String name, int number, List<Specimen> specimens) {
		this.name = name;
		this.number = number;
		this.specimens = null;
	}
	
	public boolean equals (Object o) {
		if (o != null && (o instanceof Customer) && o.hashCode() == hashCode()) {
			return ((Customer) o).name.equals(this.name) && ((Customer) o).number.equals(this.number) && ((Customer) o).specimens.equals(this.specimens);
		}
		else {
			return false;
		}
	}
	
	public int hashCode () {
		return (int) (((specimens.isEmpty() == true) ? 0 : specimens.hashCode()) + ((name == null) ? 0 : name.hashCode()) + number);
	}
}
