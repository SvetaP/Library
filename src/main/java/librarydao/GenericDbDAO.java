package librarydao;

import java.math.BigInteger;
import java.util.List;

import librarymodel.BaseEntity;

/**
 * Generic interface for Data Access Object
 *
 * @author SvetaP
 */
public interface GenericDbDAO<T extends BaseEntity> {

	void delete(BigInteger ID);

	void update(T entity, BigInteger ID);

	List<T> getAll();

	T getByID(BigInteger ID);

}
