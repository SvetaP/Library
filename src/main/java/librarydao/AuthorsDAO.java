package librarydao;

import java.math.BigInteger;
import java.sql.*;
import java.util.*;

import javax.faces.bean.*;

import org.apache.log4j.Logger;

import librarymodel.Authors;

@ManagedBean(name = "authorsDAO")
@ApplicationScoped
public class AuthorsDAO implements GenericDbDAO<Authors> {
	private static final Logger log = Logger.getLogger(AuthorsDAO.class);

	public void add(Authors entity) {
		String SQL1 = "SELECT my_seq_authors.nextval FROM DUAL";
		String SQL2 = "INSERT INTO AUTHORS (ID, NAME) " + "VALUES (?, ?)";
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
			pst.setString(2, entity.getName());
			pst.executeUpdate();
			con.commit();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Author created");
	}

	public void delete(BigInteger ID) {
		String SQL1 = "SELECT IDBOOK FROM BOOKAUTHORS WHERE IDAUTHORS =" + ID;
		String SQL2 = "DELETE FROM AUTHORS WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st1 = con.createStatement();
				Statement st2 = con.createStatement();) {
			ResultSet rs = st1.executeQuery(SQL1);
			if (rs == null) {
				st2.executeUpdate(SQL2);
				log.info("Author deleted");
			} else
				log.info("Author wrote the book");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

	}

	public void update(Authors entity, BigInteger ID) {
		String SQL = "UPDATE AUTHORS SET NAME = ? WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL);) {
			con.setAutoCommit(false);
			pst.setString(1, entity.getName());
			pst.executeUpdate(SQL);
			con.commit();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Author updated");
	}

	public List<Authors> getAll() {
		List<Authors> authors = new ArrayList<Authors>();
		String SQL = "SELECT NAME FROM AUTHORS";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs = st.executeQuery(SQL);
			while (rs.next()) {
				String name = rs.getString("NAME");
				Authors author = new Authors(name);
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

	public Authors getByID(BigInteger ID) {
		Authors author = null;
		String SQL = "SELECT NAME FROM AUTHORS WHERE ID=" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs = st.executeQuery(SQL);
			while (rs.next()) {
				String name = rs.getString("NAME");
				author = new Authors(name);
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Author by ID");
		return author;
	}

}
