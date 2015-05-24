package librarymodel;

import java.math.BigInteger;

/**
 * Super Entity class
 *
 * @author SvetaP
 */
public abstract class BaseEntity {

	private BigInteger id;

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id.toString();
	}

}
