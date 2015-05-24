package librarydao;

import java.sql.*;
import java.util.*;
import java.io.Serializable;
import java.math.BigInteger;

import javax.faces.bean.*;

import librarymodel.Authors;
import librarymodel.Book;

import org.apache.log4j.Logger;

/**
 * Data Access Object All CRUD (create, read, update, delete) basic data access
 * operations
 * 
 * @author SvetaP
 */
@ManagedBean(name = "bookDAO")
@ApplicationScoped
public class BookDAO implements GenericDbDAO<Book>, Serializable {
	private static final long serialVersionUID = 9207277530164487366L;
	private static final Logger log = Logger.getLogger(BookDAO.class);

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * @param entity
	 * @return
	 */
	public void add(Book entity) {
		String SQL1 = "SELECT my_seq_book.nextval FROM DUAL";
		String SQL2 = "INSERT INTO BOOK (ID, TITLE, YEAR, PAGES) "
				+ "VALUES (?, ?, ?, ?)";
		String SQL3 = "SELECT ID FROM BOOK WHERE TITLE = '" + entity.getTitle()
				+ "' AND YEAR = " + entity.getYear() + " AND PAGES = "
				+ entity.getPages();
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
				pst.setString(2, entity.getTitle());
				pst.setInt(3, entity.getYear());
				pst.setInt(4, entity.getPages());
				pst.executeUpdate();
				con.commit();
				log.info("Book created");
			} else
				log.info("Book does not exist");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Stores an instance of the entity class the Authors in the database to the
	 * list for the Book
	 * 
	 * @param IDBook
	 * @param IDAuthor
	 * @return
	 */
	public void addAuthors(BigInteger IDBook, BigInteger IDAuthor) {
		String SQL1 = "INSERT INTO BOOKAUTHORS (IDBOOK, IDAUTHORS) "
				+ "VALUES (?, ?)";
		String SQL2 = "SELECT NAME FROM AUTHORS WHERE ID = " + IDAuthor;
		String SQL3 = "SELECT TITLE FROM BOOK WHERE ID = " + IDBook;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL1);) {
			ResultSet rs = st.executeQuery(SQL3);
			if (rs.next()) {
				rs = st.executeQuery(SQL2);
				if (rs.next()) {
					con.setAutoCommit(false);
					pst.setLong(1, IDBook.longValue());
					pst.setLong(2, IDAuthor.longValue());
					pst.executeUpdate();
					con.commit();
					log.info("Add author for book");
				} else
					log.info("Author does not exist");
			} else
				log.info("Book does not exist");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Delete an instance of the entity class the Authors in the database from
	 * list for the Book
	 * 
	 * @param IDBook
	 * @param IDAuthor
	 * @return
	 */
	public void deleteAuthors(BigInteger IDBook, BigInteger IDAuthor) {
		String SQL = "DELETE FROM BOOKAUTHORS WHERE IDBOOK = " + IDBook
				+ "AND IDAUTHORS = " + IDAuthor;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			st.executeUpdate(SQL);
			log.info("Delete author for book");
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
		String SQL1 = "SELECT IDCUSTOMER FROM ISSUEBOOK T1 INNER JOIN SPECIMEN T2 "
				+ "ON T1.IDSPECIMEN=T2.ID WHERE IDBOOK =" + ID;
		String SQL2 = "DELETE FROM SPECIMEN WHERE IDBOOK =" + ID;
		String SQL3 = "DELETE FROM BOOKAUTHORS WHERE IDBOOK =" + ID;
		String SQL4 = "DELETE FROM BOOK WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st1 = con.createStatement();
				Statement st2 = con.createStatement();) {
			ResultSet rs = st1.executeQuery(SQL1);
			if (!rs.next()) {
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

	/**
	 * Updates the entity instance by ID
	 * 
	 * @param entity
	 * @param ID
	 * 
	 */
	public void update(Book entity, BigInteger ID) {
		String SQL1 = "UPDATE BOOK SET TITLE = ?, YEAR = ?, PAGES = ? WHERE ID ="
				+ ID;
		String SQL2 = "SELECT TITLE, YEAR, PAGES FROM BOOK WHERE ID =" + ID;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();
				PreparedStatement pst = con.prepareStatement(SQL1);) {
			ResultSet rs = st.executeQuery(SQL2);
			if (rs.next()) {
				con.setAutoCommit(false);
				pst.setString(1, entity.getTitle());
				pst.setInt(2, entity.getYear());
				pst.setInt(3, entity.getPages());
				pst.executeUpdate();
				con.commit();
				log.info("Book updated");
			} else
				log.info("Book does not exist");
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
	 * @return List<Book>
	 */
	public List<Book> getAll() {
		List<Book> books = new ArrayList<Book>();
		List<Authors> authors = null;
		String SQL1 = "SELECT ID, TITLE, YEAR, PAGES FROM BOOK";
		String SQL2 = "SELECT ID, NAME FROM AUTHORS T1 INNER JOIN BOOKAUTHORS T2 "
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
					BigInteger idauthor = BigInteger.valueOf(rs1.getLong("ID"));
					String name = rs2.getString("NAME");
					Authors author = new Authors(name);
					author.setId(idauthor);
					authors.add(author);
				}
				Book book = new Book(authors, title, year, pages);
				book.setId(idbook);
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

	/**
	 * Returns the record by ID
	 * 
	 * @param ID
	 * 
	 * @return Book
	 */
	public Book getByID(BigInteger ID) {
		Book book = null;
		List<Authors> authors = new ArrayList<Authors>();
		String SQL1 = "SELECT TITLE, YEAR, PAGES FROM BOOK WHERE ID = " + ID;
		String SQL2 = "SELECT ID, NAME FROM AUTHORS T1 INNER JOIN BOOKAUTHORS T2 "
				+ "ON T1.ID=T2.IDAUTHORS WHERE IDBOOK = " + ID;
		try (Connection con = Connect.connectionDb();
				Statement st = con.createStatement();) {
			ResultSet rs1 = st.executeQuery(SQL1);
			while (rs1.next()) {
				String title = rs1.getString("TITLE");
				Integer year = rs1.getInt("YEAR");
				Integer pages = rs1.getInt("PAGES");
				ResultSet rs2 = st.executeQuery(SQL2);
				if (rs2.next()) {
					do {
						BigInteger idauthor = BigInteger.valueOf(rs2
								.getLong("ID"));
						String name = rs2.getString("NAME");
						Authors author = new Authors(name);
						author.setId(idauthor);
						authors.add(author);
					} while (rs2.next());
					book = new Book(authors, title, year, pages);
					book.setId(ID);
				} else {
					book = new Book(title, year, pages);
					book.setId(ID);
				}
				log.info("Book by ID");
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}

		return book;
	}

}
