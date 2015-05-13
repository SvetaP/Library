package librarydao;

import java.sql.*;
import java.util.*;
import java.math.BigInteger;

import librarymodel.Authors;
import librarymodel.Book;

import org.apache.log4j.Logger;

public class BookDAO implements GenericDbDAO<Book> {
	private static final Logger log = Logger.getLogger(BookDAO.class);

	public void add(Book entity) {
		String SQL1 = "SELECT my_seq.nextval FROM DUAL";
		String SQL2 = "INSERT INTO BOOK (ID, TITLE, YEAR, PAGES) "
				+ "VALUES (?, ?, ?, ?)";
		String SQL3 = "INSERT INTO BOOKAUTHORS (IDBOOK, IDAUTHORS) "
				+ "VALUES (?, ?)";
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
			pst.setString(2, entity.getTitle());
			pst.setInt(3, entity.getYear());
			pst.setInt(4, entity.getPages());
			pst.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL3);) {
			for (int i = 0; i < entity.getAuthors().size(); i++) {
				con.setAutoCommit(false);
				pst.setLong(1, entity.getId().longValue());
				pst.setLong(2, entity.getAuthors().get(i).getId().longValue());
				pst.executeUpdate();
				con.commit();
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Book created");
	}

	public void delete(BigInteger ID) {
		String SQL = "DELETE FROM BOOK WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL);) {
			pst.executeUpdate();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Book deleted");
	}

	public void update(Book entity) {
		String SQL = "UPDATE BOOK SET TITLE = ? AND YEAR = ? AND PAGES = ? WHERE ID ="
				+ entity.getId();
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL);) {
			con.setAutoCommit(false);
			pst.setString(1, entity.getTitle());
			pst.setInt(2, entity.getYear());
			pst.setInt(3, entity.getPages());
			pst.executeUpdate(SQL);
			con.commit();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Book updated");
	}

	public List<Book> getAll() {
		List<Book> books = new ArrayList<Book>();
		List<Authors> authors = null;
		String SQL1 = "SELECT TITLE, YEAR, PAGES FROM BOOK";
		String SQL2 = "SELECT NAME FROM AUTHORS T1 INNER JOIN BOOKAUTHORS T2 "
				+ "ON T1.ID=T2.IDAUTHORS INNER JOIN BOOK T3 "
				+ "ON T2.IDBOOK=T3.ID WHERE TITLE = ? AND YEAR = ? AND PAGES = ?";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL2);) {
			ResultSet rs1 = st.executeQuery(SQL1);
			while (rs1.next()) {
				String title = rs1.getString("TITLE");
				Integer year = rs1.getInt("YEAR");
				Integer pages = rs1.getInt("PAGES");
				pst.setString(1, title);
				pst.setInt(2, year);
				pst.setInt(3, pages);
				ResultSet rs2 = pst.executeQuery();
				authors = new ArrayList<Authors>();
				while (rs2.next()) {
					String name = rs2.getString("NAME");
					Authors author = new Authors(name);
					authors.add(author);
				}
				Book book = new Book(authors, title, year, pages);
				books.add(book);
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("All books");
		return books;
	}

	public Book getByID(BigInteger ID) {
		Book book = null;
		List<Authors> authors = new ArrayList<Authors>();
		String SQL1 = "SELECT TITLE, YEAR, PAGES FROM BOOK WHERE ID=" + ID;
		String SQL2 = "SELECT NAME FROM AUTHORS T1 FULL JOIN BOOKAUTHORS T2"
				+ "ON T1.ID=T2.IDAUTHORS WHERE IDBOOK=" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs1 = st.executeQuery(SQL1);
			while (rs1.next()) {
				String title = rs1.getString("TITLE");
				Integer year = rs1.getInt("YEAR");
				Integer pages = rs1.getInt("PAGES");
				ResultSet rs2 = st.executeQuery(SQL2);
				while (rs2.next()) {
					String name = rs2.getString("NAME");
					Authors author = new Authors(name);
					authors.add(author);
				}
				book = new Book(authors, title, year, pages);
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Book by ID");
		return book;
	}

}
