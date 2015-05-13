package librarycontroller;

import java.math.BigInteger;
//import java.util.List;

public interface GenericController<M> {

	//List<M> getALL();

	void getAll();
	
	M getById(BigInteger ID);

	void delete(BigInteger ID);

	void update(M entity, BigInteger ID);
}
