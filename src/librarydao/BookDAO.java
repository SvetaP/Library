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
		String SQL1 = "INSERT INTO BOOK (ID, TITLE, YEAR, PAGES) "
				+ "VALUES (my_seq.nextval, ?, ?, ?)";
		String SQL2 = "SELECT ID FROM BOOK WHERE TITLE = ? AND YEAR = ? AND PAGES = ?";
		String SQL3 = "INSERT INTO BOOKAUTHORS (IDBOOK, IDAUTHORS) "
				+ "VALUES (?, ?)";
		PreparedStatement pst;
		ResultSet rs;
		try {
			Connection con = Connect.connectionDb();
			con.setAutoCommit(false);
			pst = con.prepareStatement(SQL1);
			pst.setString(1, entity.getTitle());
			pst.setInt(2, entity.getYear());
			pst.setInt(3, entity.getPages());
			pst.executeUpdate();			
			con.commit();
			con.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		try {
			Connection con = Connect.connectionDb();
			pst = con.prepareStatement(SQL2);
			pst.setString(1, entity.getTitle());
			pst.setInt(2, entity.getYear());
			pst.setInt(3, entity.getPages());
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
		try {
			Connection con = Connect.connectionDb();
			pst = con.prepareStatement(SQL3);
			for(int i=0; i<entity.getAuthors().size(); i++){
				con.setAutoCommit(false);
				pst.setLong(1, entity.getId().longValue());
				pst.setLong(2, entity.getAuthors().get(i).getId().longValue());
				pst.executeUpdate();			
				con.commit();
			}
			con.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}		
	}

	public void delete(BigInteger ID) {
		String SQL = "DELETE FROM BOOK WHERE ID =" + ID;
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

	public void update(Book entity) {
		String SQL = "UPDATE BOOK SET TITLE = ? AND YEAR = ? AND PAGES = ? WHERE ID =" + entity.getId();
		PreparedStatement st;
		try {
			Connection con = Connect.connectionDb();
			con.setAutoCommit(false);
			st = con.prepareStatement(SQL);
			st.setString(1, entity.getTitle());
			st.setInt(2, entity.getYear());
			st.setInt(3, entity.getPages());
			st.executeUpdate(SQL);
			con.commit();
			con.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	public List<Book> getAll() {
		List<Book> books = new ArrayList<Book>();
		List<Authors> authors = new ArrayList<Authors>();
		String SQL1 = "SELECT TITLE, YEAR, PAGES FROM BOOK";
		String SQL2 = "SELECT NAME FROM AUTHORS T1 INNER JOIN BOOKAUTHORS T2" 
				+ "ON T1.ID=T2.IDAUTHORS INNER JOIN BOOK T3" 
				+ "ON T2.IDBOOK=T3.ID WHERE TITLE = ? AND YEAR = ? AND PAGES = ?";
		Statement st;
		PreparedStatement pst;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			Connection con = Connect.connectionDb();
			st = con.createStatement();
			rs1 = st.executeQuery(SQL1);
			while (rs1.next()) {
				String title = rs1.getString("TITLE");
				Integer year = rs1.getInt("YEAR");
				Integer pages = rs1.getInt("PAGES");
				pst = con.prepareStatement(SQL2);
				pst.setString(1, title);
				pst.setInt(2, year);
				pst.setInt(3, pages);
				rs2 = pst.executeQuery();
				while(rs2.next()){
					String name = rs2.getString("NAME");
					Authors author = new Authors(name);
					authors.add(author);
				}
				Book book = new Book(authors, title, year, pages);
				books.add(book);
			}
			rs1.close();
			rs2.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return books;
	}

	public Book getByID(BigInteger ID) {
		Book book = null;
		List<Authors> authors = new ArrayList<Authors>();
		String SQL1 = "SELECT TITLE, YEAR, PAGES FROM BOOK WHERE ID=" + ID;
		String SQL2 = "SELECT NAME FROM AUTHORS T1 FULL JOIN BOOKAUTHORS T2"
				+ "ON T1.ID=T2.IDAUTHORS WHERE IDBOOK=" + ID;
		Statement st;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			Connection con = Connect.connectionDb();
			st = con.createStatement();
			rs1 = st.executeQuery(SQL1);
			while (rs1.next()) {
				String title = rs1.getString("TITLE");
				Integer year = rs1.getInt("YEAR");
				Integer pages = rs1.getInt("PAGES");
				rs2 = st.executeQuery(SQL2);
				while(rs2.next()){
					String name = rs2.getString("NAME");
					Authors author = new Authors(name);
					authors.add(author);
				}
				book = new Book(authors, title, year, pages);
			}
			rs1.close();
			st.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return book;
	}
	
}
