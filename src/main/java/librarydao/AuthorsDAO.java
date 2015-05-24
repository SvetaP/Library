package librarydao;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;

import javax.faces.bean.*;

import org.apache.log4j.Logger;

import librarymodel.Authors;

/**
 * Data Access Object All CRUD (create, read, update, delete) basic data access
 * operations
 * 
 * @author SvetaP
 */
@ManagedBean(name = "authorsDAO")
@ApplicationScoped
public class AuthorsDAO implements GenericDbDAO<Authors>, Serializable {
	private static final long serialVersionUID = -6246224013526381656L;
	private static final Logger log = Logger.getLogger(AuthorsDAO.class);

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * @param entity
	 * @return
	 */
	public void add(Authors entity) {
		String SQL1 = "SELECT my_seq_authors.nextval FROM DUAL";
		String SQL2 = "INSERT INTO AUTHORS (ID, NAME) " + "VALUES (?, ?)";
		String SQL3 = "SELECT ID FROM AUTHORS WHERE NAME = '"
				+ entity.getName() + "'";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL2);) {
			ResultSet rs = st.executeQuery(SQL3);
			if (!rs.next()) {
				rs = st.executeQuery(SQL1);
				while (rs.next()) {
					BigInteger b = BigInteger.valueOf(rs.getLong("NEXTVAL"));
					entity.setId(b);
				}
				con.setAutoCommit(false);
				pst.setLong(1, entity.getId().longValue());
				pst.setString(2, entity.getName());
				pst.executeUpdate();
				con.commit();
				log.info("Author created");
			} else
				log.info("Author does not exist");
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
		String SQL1 = "SELECT IDBOOK FROM BOOKAUTHORS WHERE IDAUTHORS =" + ID;
		String SQL2 = "DELETE FROM AUTHORS WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st1 = con.createStatement();
				Statement st2 = con.createStatement();) {
			ResultSet rs = st1.executeQuery(SQL1);
			if (!rs.next()) {
				st2.executeUpdate(SQL2);
				log.info("Author deleted");
			} else {
				log.info("Author wrote the book");
			}

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
	public void update(Authors entity, BigInteger ID) {
		String SQL1 = "UPDATE AUTHORS SET NAME = ? WHERE ID =" + ID;
		String SQL2 = "SELECT NAME FROM AUTHOR WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL1);) {
			ResultSet rs = st.executeQuery(SQL2);
			if (rs.next()) {
				con.setAutoCommit(false);
				pst.setString(1, entity.getName());
				pst.executeUpdate();
				con.commit();
				log.info("Author updated");
			} else
				log.info("Author does not exist");
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
	 * @return List<Authors>
	 */
	public List<Authors> getAll() {
		List<Authors> authors = new ArrayList<Authors>();
		String SQL = "SELECT ID, NAME FROM AUTHORS";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs = st.executeQuery(SQL);
			while (rs.next()) {
				BigInteger id = BigInteger.valueOf(rs.getLong("ID"));
				String name = rs.getString("NAME");
				Authors author = new Authors(name);
				author.setId(id);
				authors.add(author);
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("All authors");
		return authors;
	}

	/**
	 * Returns the record by ID
	 * 
	 * @param ID
	 * 
	 * @return Authors
	 */
	public Authors getByID(BigInteger ID) {
		Authors author = null;
		String SQL = "SELECT NAME FROM AUTHORS WHERE ID = " + ID;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs = st.executeQuery(SQL);
			while (rs.next()) {
				String name = rs.getString("NAME");
				author = new Authors(name);
				log.info("Author by ID");
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return author;
	}

}
