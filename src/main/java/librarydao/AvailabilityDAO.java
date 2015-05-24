package librarydao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;

import librarymodel.Availability;

/**
 * Data Access Object Data access operations create, read
 * 
 * @author SvetaP
 */
@ManagedBean(name = "availabilityDAO")
@ApplicationScoped
public class AvailabilityDAO implements Serializable {
	private static final long serialVersionUID = -3675675842097518132L;
	private static final Logger log = Logger.getLogger(AvailabilityDAO.class);

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * 
	 */
	public void add() {
		String SQL1 = "MERGE INTO Availability USING(SELECT ? as IDAVAILABILITY FROM DUAL) "
				+ "ON (ID = IDAVAILABILITY) "
				+ "WHEN MATCHED THEN UPDATE SET NAME = ?"
				+ "WHEN NOT MATCHED THEN INSERT (ID, NAME)" + "VALUES(?, ?)";
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL1);) {
			con.setAutoCommit(false);
			pst.setInt(1, 1);
			pst.setString(2, Availability.AVAILABLE.toString());
			pst.setInt(3, 1);
			pst.setString(4, Availability.AVAILABLE.toString());
			pst.executeUpdate();
			con.commit();
			con.setAutoCommit(false);
			pst.setInt(1, 2);
			pst.setString(2, Availability.NOTAVAILABLE.toString());
			pst.setInt(3, 2);
			pst.setString(4, Availability.NOTAVAILABLE.toString());
			pst.executeUpdate();
			con.commit();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Availability created");
	}

	/**
	 * Retrieves an entity instances
	 * 
	 * @return List<String>
	 */
	public List<String> getAll() {
		List<String> listavailability = new ArrayList<String>();
		listavailability.add(Availability.AVAILABLE.toString());
		listavailability.add(Availability.NOTAVAILABLE.toString());
		return listavailability;
	}

}
