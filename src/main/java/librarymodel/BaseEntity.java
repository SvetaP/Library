package librarymodel;

import java.math.BigInteger;

public abstract class BaseEntity {

	private BigInteger id;

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getId() {
		return id;
	}

}
