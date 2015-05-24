package librarymodel;

import java.io.Serializable;


/**
 * 
 * @author SvetaP
 */
public class Authors extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 4246408845437681208L;

	String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Authors() {
		super();
	}

	public Authors(String name) {
		this.name = name;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Authors other = (Authors) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String toString() {
		return this.name;
	}
}
