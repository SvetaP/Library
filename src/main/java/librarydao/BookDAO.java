package librarydao;

import java.sql.*;
import java.util.*;
import java.math.BigInteger;

import javax.faces.bean.*;

import librarymodel.Authors;
import librarymodel.Book;

import org.apache.log4j.Logger;

@ManagedBean(name = "bookDAO")
@ApplicationScoped
public class BookDAO implements GenericDbDAO<Book> {
	private static final Logger log = Logger.getLogger(BookDAO.class);

	public void add(Book entity) {
		String SQL1 = "SELECT my_seq_book.nextval FROM DUAL";
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
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
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
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
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
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Book created");
	}

	public void delete(BigInteger ID) {
		String SQL1 = "SELECT IDCUSTOMER FROM ISSUEBOOK T1 INNER JOIN SPECIMEN T2 "
				+ "ON T1.IDSPECIMEN=T2.ID WHERE IDBOOK =" + ID;
		String SQL2 = "DELETE FROM SPECIMEN WHERE IDBOOK =" + ID;
		String SQL3 = "DELETE FROM BOOKAUTHORS WHERE IDBOOK =" + ID;
		String SQL4 = "DELETE FROM BOOK WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st1 = con.createStatement();
				Statement st2 = con.createStatement();) {
			ResultSet rs = st1.executeQuery(SQL1);
			if (rs == null) {
				st1.executeUpdate(SQL2);
				st2.executeUpdate(SQL3);
				st2.executeUpdate(SQL4);
				log.info("Book deleted");
			} else
				log.info("Customer didn't return specimen");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void update(Book entity, BigInteger ID) {
		String SQL = "UPDATE BOOK SET TITLE = ? AND YEAR = ? AND PAGES = ? WHERE ID ="
				+ ID;
		try (Connection con = Connect.connectionDb();
				PreparedStatement pst = con.prepareStatement(SQL);) {
			con.setAutoCommit(false);
			pst.setString(1, entity.getTitle());
			pst.setInt(2, entity.getYear());
			pst.setInt(3, entity.getPages());
			pst.executeUpdate(SQL);
			con.commit();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Book updated");
	}

	public List<Book> getAll() {
		List<Book> books = new ArrayList<Book>();
		List<Authors> authors = null;
		String SQL1 = "SELECT ID, TITLE, YEAR, PAGES FROM BOOK";
		String SQL2 = "SELECT NAME FROM AUTHORS T1 INNER JOIN BOOKAUTHORS T2 "
				+ "ON T1.ID=T2.IDAUTHORS WHERE IDBOOK = ?";
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL2);) {
			ResultSet rs1 = st.executeQuery(SQL1);
			while (rs1.next()) {
				BigInteger idbook = BigInteger.valueOf(rs1.getLong("ID"));
				String title = rs1.getString("TITLE");
				Integer year = rs1.getInt("YEAR");
				Integer pages = rs1.getInt("PAGES");
				pst.setLong(1, idbook.longValue());
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
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
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
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		log.info("Book by ID");
		return book;
	}

}
