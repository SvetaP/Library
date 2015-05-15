package librarydao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;

import librarymodel.Availability;

@ManagedBean(name = "availabilityDAO")
@ApplicationScoped
public class AvailabilityDAO {
	private static final Logger log = Logger.getLogger(AuthorsDAO.class);

	public void add() {
		String SQL1 = "INSERT INTO AVAILABILITY (ID, NAME) " + "VALUES (?, ?)";
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL1);) {
			con.setAutoCommit(false);
			pst.setInt(1, 1);
			pst.setString(2, Availability.AVAILABLE.toString());
			pst.executeUpdate();
			con.commit();
			con.setAutoCommit(false);
			pst.setInt(1, 2);
			pst.setString(2, Availability.NOTAVAILABLE.toString());
			pst.executeUpdate();
			con.commit();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Availability created");
	}

	public Availability getByID(int ID) {
		switch (ID) {
		case 1:
			return Availability.AVAILABLE;
		case 2:
			return Availability.NOTAVAILABLE;
		default:
			log.info("Availability does not exist");
			return null;
		}
	}

}
