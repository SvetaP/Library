package librarydao;

import java.math.BigInteger;
import java.util.List;

import librarymodel.BaseEntity;

public interface GenericDbDAO<T extends BaseEntity> {
	
	void add(T entity);
	
	void delete(BigInteger ID);
	
	void update(T entity);

	List<T> getAll();

	T getByID(BigInteger ID);
	
}
