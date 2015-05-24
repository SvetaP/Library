package librarydao;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;

import javax.faces.bean.*;

import org.apache.log4j.Logger;

import librarymodel.Book;
import librarymodel.Specimen;

/**
 * Data Access Object All CRUD (create, read, update, delete) basic data access
 * operations
 * 
 * @author SvetaP
 */
@ManagedBean(name = "specimenDAO")
@ApplicationScoped
public class SpecimenDAO implements GenericDbDAO<Specimen>, Serializable {
	private static final long serialVersionUID = 6525808517317631921L;
	private static final Logger log = Logger.getLogger(SpecimenDAO.class);

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * @param entity
	 * @param IDBook
	 */
	public void add(Specimen entity, BigInteger IDBook) {
		String SQL1 = "SELECT my_seq_specimen.nextval FROM DUAL";
		String SQL2 = "INSERT INTO SPECIMEN (ID, InventoryNumber, IDBOOK, IDAVAILABILITY) "
				+ "VALUES (?, ?, ?, ?)";
		String SQL3 = "SELECT NAME, T3.TITLE FROM AUTHORS "
				+ "T1 FULL JOIN BOOKAUTHORS T2 ON T1.ID=T2.IDAUTHORS "
				+ "FULL JOIN BOOK T3 ON T2.IDBOOK=T3.ID WHERE T3.ID = "
				+ IDBook;
		String SQL4 = "SELECT * FROM SPECIMEN WHERE InventoryNumber = '"
				+ entity.getInventoryNumber() + "'";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL2);) {
			ResultSet rs = st.executeQuery(SQL4);
			if (!rs.next()) {
				rs = st.executeQuery(SQL3);
				if (rs.next()) {
					if (rs.getString(1) == null) {
						log.info("Add the authors for book");
					} else {
						ResultSet rs1 = st.executeQuery(SQL1);
						while (rs1.next()) {
							BigInteger b = BigInteger.valueOf(rs1
									.getLong("NEXTVAL"));
							entity.setId(b);
						}
						con.setAutoCommit(false);
						pst.setLong(1, entity.getId().longValue());
						pst.setString(2, entity.getInventoryNumber());
						pst.setLong(3, IDBook.longValue());
						pst.setInt(4, 1);
						pst.executeUpdate();
						con.commit();
						log.info("Specimen created");
					}
				} else
					log.info("Book does not exist");
			} else
				log.info("Enter another Inventory Number");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Removes the record that is associated with the entity instance
	 * 
	 * @param ID
	 * 
	 */
	public void delete(BigInteger ID) {
		String SQL1 = "SELECT IDCUSTOMER FROM ISSUEBOOK WHERE IDSPECIMEN ="
				+ ID;
		String SQL2 = "DELETE FROM SPECIMEN WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st1 = con.createStatement();
				Statement st2 = con.createStatement();) {
			ResultSet rs = st1.executeQuery(SQL1);
			if (!rs.next()) {
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

	/**
	 * Updates the entity instance by ID
	 * 
	 * @param entity
	 * @param ID
	 * 
	 */
	public void update(Specimen entity, BigInteger ID) {
		String SQL1 = "UPDATE SPECIMEN SET InventoryNumber = ? WHERE ID =" + ID;
		String SQL2 = "SELECT IDBOOK FROM SPECIMEN WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL1);) {
			ResultSet rs = st.executeQuery(SQL2);
			if (rs.next()) {
				con.setAutoCommit(false);
				pst.setString(1, entity.getInventoryNumber());
				pst.executeUpdate();
				con.commit();
				log.info("Specimen updated");
			} else
				log.info("Specimen does not exist");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

	}

	/**
	 * Retrieves an entity instances that was previously persisted to the
	 * database
	 * 
	 * @return List<Specimen>
	 */
	public List<Specimen> getAll() {
		List<Specimen> specimens = new ArrayList<Specimen>();
		BookDAO bookdao = new BookDAO();
		String SQL = "SELECT * FROM SPECIMEN";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs = st.executeQuery(SQL);
			while (rs.next()) {
				BigInteger idspecimen = BigInteger.valueOf(rs.getInt("ID"));
				String inventoryNumber = rs.getString("InventoryNumber");
				BigInteger idbook = BigInteger.valueOf(rs.getInt("IDBOOK"));
				Book book = bookdao.getByID(idbook);
				Specimen specimen = new Specimen();
				specimen.setInventoryNumber(inventoryNumber);
				specimen.setBook(book);
				switch (rs.getInt("IDAVAILABILITY")) {
				case 1:
					specimen.setAvailable();
					break;
				case 2:
					specimen.setNotAvailable();
					break;
				}
				specimen.setId(idspecimen);
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

	/**
	 * Returns the record by ID
	 * 
	 * @param ID
	 * 
	 * @return Specimen
	 */
	public Specimen getByID(BigInteger ID) {
		Specimen specimen = null;
		BookDAO bookdao = new BookDAO();
		String SQL = "SELECT * FROM SPECIMEN WHERE ID=" + ID;
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
					break;
				case 2:
					specimen.setNotAvailable();
					break;
				}
				specimen.setId(ID);
				log.info("Specimen by ID");
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return specimen;
	}

}
