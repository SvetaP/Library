package librarydao;

import java.math.BigInteger;
import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;

import librarymodel.Authors;

public class AuthorsDAO implements GenericDbDAO<Authors> {
	private static final Logger log = Logger.getLogger(AuthorsDAO.class);

	public void add(Authors entity) {
		String SQL1 = "SELECT my_seq.nextval FROM DUAL";
		String SQL2 = "INSERT INTO AUTHORS (ID, NAME) " + "VALUES (?, ?)";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs = st.executeQuery(SQL1);
			while (rs.next()) {
				BigInteger b = BigInteger.valueOf(rs.getLong("NEXTVAL"));
				entity.setId(b);
			}
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
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Author created");
	}

	public void delete(BigInteger ID) {
		String SQL = "DELETE FROM AUTHORS WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL);) {
			pst.executeUpdate(SQL);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Author deleted");
	}

	public void update(Authors entity) {
		String SQL = "UPDATE AUTHORS SET NAME = ? WHERE ID =" + entity.getId();
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL);) {
			con.setAutoCommit(false);
			pst.setString(1, entity.getName());
			pst.executeUpdate(SQL);
			con.commit();
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
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Author by ID");
		return author;
	}

}
