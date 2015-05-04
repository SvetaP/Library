package librarydao;

import java.math.BigInteger;
import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;

import librarymodel.Authors;

public class AuthorsDAO implements GenericDbDAO<Authors> {
	private static final Logger log = Logger.getLogger(AuthorsDAO.class);

	public void add(Authors entity) {
		String SQL1 = "INSERT INTO AUTHORS (ID, NAME) "
				+ "VALUES (my_seq.nextval, ?)";
		String SQL2 = "SELECT ID FROM AUTHORS WHERE NAME = ?";
		PreparedStatement pst;
		ResultSet rs;
		try {
			Connection con = Connect.connectionDb();
			con.setAutoCommit(false);
			pst = con.prepareStatement(SQL1);
			pst.setString(1, entity.getName());
			pst.executeUpdate();
			con.commit();
			con.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		try {
			Connection con = Connect.connectionDb();
			con.setAutoCommit(false);
			pst = con.prepareStatement(SQL2);
			pst.setString(1, entity.getName());
			rs = pst.executeQuery();
			while (rs.next()) {
				BigInteger b = BigInteger.valueOf(rs.getLong("ID"));
				entity.setId(b);
			}
			pst.executeUpdate();
			rs.close();
			con.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void delete(BigInteger ID) {
		String SQL = "DELETE FROM AUTHORS WHERE ID =" + ID;
		PreparedStatement st;
		try {
			Connection con = Connect.connectionDb();
			st = con.prepareStatement(SQL);
			st.executeUpdate(SQL);
			con.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void update(Authors entity) {
		String SQL = "UPDATE AUTHORS SET NAME = ? WHERE ID =" + entity.getId();
		PreparedStatement st;
		try {
			Connection con = Connect.connectionDb();
			con.setAutoCommit(false);
			st = con.prepareStatement(SQL);
			st.setString(1, entity.getName());
			st.executeUpdate(SQL);
			con.commit();
			con.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	public List<Authors> getAll() {
		List<Authors> authors = new ArrayList<Authors>();
		String SQL = "SELECT NAME FROM AUTHORS";
		Statement st;
		ResultSet rs;
		try {
			Connection con = Connect.connectionDb();
			st = con.createStatement();
			rs = st.executeQuery(SQL);
			while (rs.next()) {
				String name = rs.getString("NAME");
				Authors author = new Authors(name);
				authors.add(author);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return authors;
	}

	public Authors getByID(BigInteger ID) {
		Authors author = null;
		String SQL = "SELECT NAME FROM AUTHORS WHERE ID=" + ID;
		Statement st;
		ResultSet rs;
		try {
			Connection con = Connect.connectionDb();
			st = con.createStatement();
			rs = st.executeQuery(SQL);
			while (rs.next()) {
				String name = rs.getString("NAME");
				author = new Authors(name);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return author;
	}

}
