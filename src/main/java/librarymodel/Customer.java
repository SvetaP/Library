package librarymodel;

import java.io.Serializable;
import java.util.*;

public class Customer extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -4625214450718776815L;

	private String name;
	private Integer number;
	private List<Specimen> specimens;

	{
		specimens = new ArrayList<Specimen>();
	}

	public String getName() {
		return this.name;
	}

	public Integer getNumber() {
		return this.number;
	}

	public List<Specimen> getSpecimens() {
		return this.specimens;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public void setSpecimens(List<Specimen> specimens) {
		this.specimens = specimens;
	}

	public Customer() {
		super();
	}

	public Customer(String name, Integer number, List<Specimen> specimens) {
		this.name = name;
		this.number = number;
		this.specimens = null;
	}

	public boolean equals(Object o) {
		if (o != null && (o instanceof Customer) && o.hashCode() == hashCode()) {
			return ((Customer) o).name.equals(this.name)
					&& ((Customer) o).number.equals(this.number)
					&& ((Customer) o).specimens.equals(this.specimens);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return (int) (((specimens.isEmpty() == true) ? 0 : specimens.hashCode())
				+ ((name == null) ? 0 : name.hashCode()) + number);
	}
}
