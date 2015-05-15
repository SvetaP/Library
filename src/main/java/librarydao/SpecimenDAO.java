package librarydao;

import java.math.BigInteger;
import java.sql.*;
import java.util.*;

import javax.faces.bean.*;

import org.apache.log4j.Logger;

import librarymodel.Book;
import librarymodel.Specimen;

@ManagedBean(name = "specimenDAO")
@ApplicationScoped
public class SpecimenDAO implements GenericDbDAO<Specimen> {
	private static final Logger log = Logger.getLogger(AuthorsDAO.class);

	public void add(Specimen entity) {
		String SQL1 = "SELECT my_seq_specimen.nextval FROM DUAL";
		String SQL2 = "INSERT INTO SPECIMEN (ID, InventoryNumber, IDBOOK, IDAVAILABILITY) "
				+ "VALUES (?, ?, ?, ?)";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs = st.executeQuery(SQL1);
			while (rs.next()) {
				BigInteger b = BigInteger.valueOf(rs.getLong("NEXTVAL"));
				entity.setId(b);
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL2);) {
			con.setAutoCommit(false);
			pst.setLong(1, entity.getId().longValue());
			pst.setString(2, entity.getInventoryNumber());
			pst.setLong(3, entity.getBook().getId().longValue());
			pst.setInt(4, 1);
			pst.executeUpdate();
			con.commit();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Specimen created");

	}

	public void delete(BigInteger ID) {
		String SQL1 = "SELECT IDCUSTOMER FROM ISSUEBOOK WHERE IDSPECIMEN ="
				+ ID;
		String SQL2 = "DELETE FROM SPECIMEN WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st1 = con.createStatement();
				Statement st2 = con.createStatement();) {
			ResultSet rs = st1.executeQuery(SQL1);
			if (rs == null) {
				st1.executeUpdate(SQL2);
				log.info("Specimen deleted");
			} else
				log.info("Customer didn't return specimen");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

	}

	public void update(Specimen entity, BigInteger ID) {
		String SQL = "UPDATE SPECIMEN SET InventoryNumber = ? AND IDBOOK = ? WHERE ID ="
				+ ID;
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL);) {
			con.setAutoCommit(false);
			pst.setString(1, entity.getInventoryNumber());
			pst.setLong(2, entity.getBook().getId().longValue());
			pst.executeUpdate(SQL);
			con.commit();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Specimen updated");

	}

	public List<Specimen> getAll() {
		List<Specimen> specimens = new ArrayList<Specimen>();
		BookDAO bookdao = new BookDAO();
		String SQL = "SELECT InventoryNumber, IDBOOK, IDAVAILABILITY FROM SPECIMEN";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs = st.executeQuery(SQL);
			while (rs.next()) {
				String inventoryNumber = rs.getString("InventoryNumber");
				BigInteger idbook = BigInteger.valueOf(rs.getInt("IDBOOK"));
				int idavailability = rs.getInt("IDAVAILABILITY");
				Book book = bookdao.getByID(idbook);
				Specimen specimen = new Specimen();
				specimen.setInventoryNumber(inventoryNumber);
				specimen.setBook(book);
				switch (idavailability) {
				case 1:
					specimen.setAvailable();
				case 2:
					specimen.setNotAvailable();
				}
				specimens.add(specimen);
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("All specimens");
		return specimens;
	}

	public Specimen getByID(BigInteger ID) {
		Specimen specimen = null;
		BookDAO bookdao = new BookDAO();
		String SQL = "SELECT InventoryNumber, IDBOOK, IDAVAILABILITY FROM SPECIMEN WHERE ID="
				+ ID;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs = st.executeQuery(SQL);
			while (rs.next()) {
				String inventoryNumber = rs.getString("InventoryNumber");
				BigInteger idbook = BigInteger.valueOf(rs.getInt("IDBOOK"));
				int idavailability = rs.getInt("IDAVAILABILITY");
				Book book = bookdao.getByID(idbook);
				specimen = new Specimen();
				specimen.setInventoryNumber(inventoryNumber);
				specimen.setBook(book);
				switch (idavailability) {
				case 1:
					specimen.setAvailable();
				case 2:
					specimen.setNotAvailable();
				}
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Specimen by ID");
		return specimen;
	}

}
