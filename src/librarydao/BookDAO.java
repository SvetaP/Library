package librarydao;

import java.sql.*;
import java.sql.Statement;
import java.util.*;
import java.util.zip.*;
import java.beans.*;
import java.io.*;

import librarymodel.Book;

public class BookDAO implements BookDbDAO {

	public void add(Book book) {
		try {
			String url = "jdbc:oracle:thin:@localhost";
			Locale.setDefault(Locale.ENGLISH);
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection con = DriverManager.getConnection(url, "EXAMPLE",
					"oracle");
			con.setAutoCommit(false);
			PreparedStatement st = con.prepareStatement(""
					+ "INSERT INTO BOOK (ID, AUTHORS, TITLE, YEAR, PAGES) "
					+ "VALUES (my_seq.nextval, ?, ?, ?, ?)");
			st.setString(1, book.toStringAuthors());
			st.setString(2, book.getTitle());
			st.setInt(3, book.getYear());
			st.setInt(4, book.getPages());
			st.executeUpdate();
			con.commit();
			con.close();
		} catch (SQLException e) {
			System.out.println("SQLException");
		}
		try {
			File f1 = new File(book.toStringAuthors() + book.getTitle()
					+ ".txt");
			FileOutputStream fos = new FileOutputStream(f1);
			GZIPOutputStream zos = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(zos);
			oos.writeObject(book);
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		} catch (IOException e) {
			System.out.println("IOException");
		}
		try {
			File f2 = new File(book.toStringAuthors() + book.getTitle()
					+ ".xml");
			FileOutputStream out = new FileOutputStream(f2);
			XMLEncoder xmlEncoder = new XMLEncoder(out);
			xmlEncoder.writeObject(book);
			xmlEncoder.flush();
			xmlEncoder.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		}

	}

	public List<Book> getBooks() {
		List<Book> books = new ArrayList<Book>();
		try {
			String url = "jdbc:oracle:thin:@localhost";
			Locale.setDefault(Locale.ENGLISH);
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection con = DriverManager.getConnection(url, "EXAMPLE",
					"oracle");
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select authors, title, year, pages from BOOK");
			while (rs.next()) {
				String authors1 = rs.getString("AUTHORS");
				String[] authors2 = authors1.trim().split(",");
				List<String> authors = new ArrayList<String>();
				for (int i = 0; i < authors2.length; i++) {
					authors.add(authors2[i].trim());
				}
				String title = rs.getString("TITLE");
				Integer year = rs.getInt("YEAR");
				Integer pages = rs.getInt("PAGES");
				Book book = new Book(authors, title, year, pages);
				books.add(book);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("SQLException");
		}
		return books;
	}

	public void updateAuthors(Book book, List<String> authors) {
		try {
			String url = "jdbc:oracle:thin:@localhost";
			Locale.setDefault(Locale.ENGLISH);
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection con = DriverManager.getConnection(url, "EXAMPLE",
					"oracle");
			con.setAutoCommit(false);
			PreparedStatement st = con
					.prepareStatement(""
							+ "UPDATE BOOK SET AUTHORS = ? WHERE AUTHORS = ? AND TITLE = ? AND YEAR = ? AND PAGES = ?");
			String authors2 = "";
			for (int i = 0; i < authors.size(); i++)
				authors2 += authors.get(i) + " ";
			st.setString(1, authors2);
			st.setString(2, book.toStringAuthors());
			st.setString(3, book.getTitle());
			st.setInt(4, book.getYear());
			st.setInt(5, book.getPages());
			st.executeUpdate();
			con.commit();
			con.close();
		} catch (SQLException e) {
			System.out.println("SQLException");
		}
		try {
			File f1 = new File(book.toStringAuthors() + book.getTitle()
					+ ".txt");
			FileInputStream fis = new FileInputStream(f1);
			GZIPInputStream gs = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(gs);
			Book b1 = (Book) ois.readObject();
			fis.close();
			ois.close();
			b1.setAuthors(authors);
			f1.delete();
			File f2 = new File(b1.toStringAuthors() + b1.getTitle() + ".txt");
			FileOutputStream fos = new FileOutputStream(f2);
			GZIPOutputStream zos = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(zos);
			oos.writeObject(b1);
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
		} catch (IOException e) {
			System.out.println("IOException");
		}
		try {
			File f3 = new File(book.toStringAuthors() + book.getTitle()
					+ ".xml");
			FileInputStream in = new FileInputStream(f3);
			XMLDecoder xmlDecoder = new XMLDecoder(in);
			Book b2 = (Book) xmlDecoder.readObject();
			xmlDecoder.close();
			b2.setAuthors(authors);
			f3.delete();
			File f4 = new File(b2.toStringAuthors() + b2.getTitle() + ".xml");
			FileOutputStream out = new FileOutputStream(f4);
			XMLEncoder xmlEncoder = new XMLEncoder(out);
			xmlEncoder.writeObject(b2);
			xmlEncoder.flush();
			xmlEncoder.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		}
	}

	public void updateTitle(Book book, String title) {
		try {
			String url = "jdbc:oracle:thin:@localhost";
			Locale.setDefault(Locale.ENGLISH);
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection con = DriverManager.getConnection(url, "EXAMPLE",
					"oracle");
			con.setAutoCommit(false);
			PreparedStatement st = con
					.prepareStatement(""
							+ "UPDATE BOOK SET TITLE = ? WHERE AUTHORS = ? AND TITLE = ? AND YEAR = ? AND PAGES = ?");
			st.setString(1, title);
			st.setString(2, book.toStringAuthors());
			st.setString(3, book.getTitle());
			st.setInt(4, book.getYear());
			st.setInt(5, book.getPages());
			st.executeUpdate();
			con.commit();
			con.close();
		} catch (SQLException e) {
			System.out.println("SQLException");
		}
		try {
			File f1 = new File(book.toStringAuthors() + book.getTitle()
					+ ".txt");
			FileInputStream fis = new FileInputStream(f1);
			GZIPInputStream gs = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(gs);
			Book b1 = (Book) ois.readObject();
			fis.close();
			ois.close();
			b1.setTitle(title);
			f1.delete();
			File f2 = new File(b1.toStringAuthors() + b1.getTitle() + ".txt");
			FileOutputStream fos1 = new FileOutputStream(f2);
			GZIPOutputStream zos1 = new GZIPOutputStream(fos1);
			ObjectOutputStream oos1 = new ObjectOutputStream(zos1);
			oos1.writeObject(b1);
			oos1.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
		} catch (IOException e) {
			System.out.println("IOException");
		}
		try {
			File f3 = new File(book.toStringAuthors() + book.getTitle()
					+ ".xml");
			FileInputStream in = new FileInputStream(f3);
			XMLDecoder xmlDecoder = new XMLDecoder(in);
			Book b2 = (Book) xmlDecoder.readObject();
			xmlDecoder.close();
			b2.setTitle(title);
			f3.delete();
			File f4 = new File(b2.toStringAuthors() + b2.getTitle() + ".xml");
			FileOutputStream out = new FileOutputStream(f4);
			XMLEncoder xmlEncoder = new XMLEncoder(out);
			xmlEncoder.writeObject(b2);
			xmlEncoder.flush();
			xmlEncoder.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		}

	}

	public void updateYear(Book book, int year) {
		try {
			String url = "jdbc:oracle:thin:@localhost";
			Locale.setDefault(Locale.ENGLISH);
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection con = DriverManager.getConnection(url, "EXAMPLE",
					"oracle");
			con.setAutoCommit(false);
			PreparedStatement st = con
					.prepareStatement(""
							+ "UPDATE BOOK SET YEAR = ? WHERE AUTHORS = ? AND TITLE = ? AND YEAR = ? AND PAGES = ?");
			st.setInt(1, year);
			st.setString(2, book.toStringAuthors());
			st.setString(3, book.getTitle());
			st.setInt(4, book.getYear());
			st.setInt(5, book.getPages());
			st.executeUpdate();
			con.commit();
			con.close();
		} catch (SQLException e) {
			System.out.println("SQLException");
		}
		try {

			File f1 = new File(book.toStringAuthors() + book.getTitle()
					+ ".txt");
			FileInputStream fis = new FileInputStream(f1);
			GZIPInputStream gs = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(gs);
			Book b1 = (Book) ois.readObject();
			fis.close();
			ois.close();
			b1.setYear(year);
			FileOutputStream fos1 = new FileOutputStream(f1);
			GZIPOutputStream zos1 = new GZIPOutputStream(fos1);
			ObjectOutputStream oos1 = new ObjectOutputStream(zos1);
			oos1.writeObject(b1);
			oos1.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
		} catch (IOException e) {
			System.out.println("IOException");
		}
		try {
			File f3 = new File(book.toStringAuthors() + book.getTitle()
					+ ".xml");
			FileInputStream in = new FileInputStream(f3);
			XMLDecoder xmlDecoder = new XMLDecoder(in);
			Book b2 = (Book) xmlDecoder.readObject();
			xmlDecoder.close();
			b2.setYear(year);
			FileOutputStream out = new FileOutputStream(f3);
			XMLEncoder xmlEncoder = new XMLEncoder(out);
			xmlEncoder.writeObject(b2);
			xmlEncoder.flush();
			xmlEncoder.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		}
	}

	public void updatePage(Book book, int pages) {
		try {
			String url = "jdbc:oracle:thin:@localhost";
			Locale.setDefault(Locale.ENGLISH);
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection con = DriverManager.getConnection(url, "EXAMPLE",
					"oracle");
			con.setAutoCommit(false);
			PreparedStatement st = con
					.prepareStatement(""
							+ "UPDATE BOOK SET PAGES = ? WHERE AUTHORS = ? AND TITLE = ? AND YEAR = ? AND PAGES = ?");
			st.setInt(1, pages);
			st.setString(2, book.toStringAuthors());
			st.setString(3, book.getTitle());
			st.setInt(4, book.getYear());
			st.setInt(5, book.getPages());
			st.executeUpdate();
			con.commit();
			con.close();
		} catch (SQLException e) {
			System.out.println("SQLException");
		}
		try {
			File f1 = new File(book.toStringAuthors() + book.getTitle()
					+ ".txt");
			FileInputStream fis = new FileInputStream(f1);
			GZIPInputStream gs = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(gs);
			Book b1 = (Book) ois.readObject();
			fis.close();
			ois.close();
			b1.setPages(pages);
			FileOutputStream fos1 = new FileOutputStream(f1);
			GZIPOutputStream zos1 = new GZIPOutputStream(fos1);
			ObjectOutputStream oos1 = new ObjectOutputStream(zos1);
			oos1.writeObject(b1);
			oos1.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
		} catch (IOException e) {
			System.out.println("IOException");
		}
		try {
			File f3 = new File(book.toStringAuthors() + book.getTitle()
					+ ".xml");
			FileInputStream in = new FileInputStream(f3);
			XMLDecoder xmlDecoder = new XMLDecoder(in);
			Book b2 = (Book) xmlDecoder.readObject();
			xmlDecoder.close();
			b2.setPages(pages);
			FileOutputStream out = new FileOutputStream(f3);
			XMLEncoder xmlEncoder = new XMLEncoder(out);
			xmlEncoder.writeObject(b2);
			xmlEncoder.flush();
			xmlEncoder.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		}

	}

	public void delete(Book book) {
		try {
			String url = "jdbc:oracle:thin:@localhost";
			Locale.setDefault(Locale.ENGLISH);
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection con = DriverManager.getConnection(url, "EXAMPLE",
					"oracle");
			PreparedStatement st = con
					.prepareStatement(""
							+ "DELETE FROM BOOK WHERE AUTHORS = ? AND TITLE = ? AND YEAR = ? AND PAGES = ?");
			st.setString(1, book.toStringAuthors());
			st.setString(2, book.getTitle());
			st.setInt(3, book.getYear());
			st.setInt(4, book.getPages());
			st.executeUpdate();
			con.close();
		} catch (SQLException e) {
			System.out.println("SQLException");
		}

		File f = new File(book.toStringAuthors() + book.getTitle() + ".txt");
		f.delete();

		File f2 = new File(book.toStringAuthors() + book.getTitle() + ".xml");
		f2.delete();

	}
}
