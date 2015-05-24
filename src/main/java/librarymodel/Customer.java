package librarymodel;

import java.io.Serializable;
import java.util.*;

/**
 * 
 * @author SvetaP
 */
public class Customer extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1472834746843235083L;

	private String name;
	private Long number;
	private List<Specimen> specimens;

	public String getName() {
		return this.name;
	}

	public Long getNumber() {
		return this.number;
	}

	public List<Specimen> getSpecimens() {
		return this.specimens;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public void setSpecimens(List<Specimen> specimens) {
		this.specimens = specimens;
	}

	public Customer() {
		super();
	}

	public Customer(String name, Long number) {
		this.name = name;
		this.number = number;
		this.specimens = null;
	}

	public Customer(String name, Long number, List<Specimen> specimens) {
		this.name = name;
		this.number = number;
		this.specimens = specimens;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result
				+ ((specimens == null) ? 0 : specimens.hashCode());
		return result;
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

	public String toString() {
		return name + number + specimens.toString();
	}

	public String toStringSpecimens() {
		String str = "";
		for (int i = 0; i < specimens.size(); i++) {
			str += specimens.get(i).toString() + "\n";
		}
		return str;
	}

}
