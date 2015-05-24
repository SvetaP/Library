package librarycontroller;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import librarydao.BookDAO;
import librarymodel.Book;

/**
 * Book Controller class allows users to do CRUD operations
 * 
 * @author SvetaP
 */
@ManagedBean(name = "bookController")
@ViewScoped
public class BookController implements Serializable {
	private static final long serialVersionUID = 7550632630638943521L;

	// Loading books list
	private List<Book> books;
	// Creating new book
	private Book book = new Book();
	// ID
	private BigInteger idbook, idauthor;

	@ManagedProperty("#{bookDAO}")
	private BookDAO bookdao;

	/**
	 * Default constructor
	 */
	public BookController() {
	}

	/**
	 * Getters, Setters
	 * 
	 * @return
	 */

	public List<Book> getBooks() {
		return books;
	}

	/**
	 *
	 * @return
	 */
	public Book getBook() {
		return book;
	}

	/**
	 *
	 * @param book
	 */
	public void setBook(Book book) {
		this.book = book;
	}

	/**
	 *
	 * @return
	 */
	public BigInteger getIdbook() {
		return idbook;
	}

	/**
	 *
	 * @param idbook
	 */
	public void setIdbook(BigInteger idbook) {
		this.idbook = idbook;
	}

	/**
	 *
	 * @return
	 */
	public BigInteger getIdauthor() {
		return idauthor;
	}

	/**
	 *
	 * @param idauthor
	 */
	public void setIdauthor(BigInteger idauthor) {
		this.idauthor = idauthor;
	}

	/**
	 *
	 * @return
	 */
	public BookDAO getBookdao() {
		return bookdao;
	}

	/**
	 *
	 * @param bookdao
	 */
	public void setBookdao(BookDAO bookdao) {
		this.bookdao = bookdao;
	}

	/**
	 * Initializing Data Access Object for Book
	 * 
	 */
	@PostConstruct
	public void getAll() {
		books = bookdao.getAll();
		Collections.sort(books, new Comparator<Book>() {
			@Override
			public int compare(Book book1, Book book2) {
				return book1.getId().compareTo(book2.getId());
			}
		});
	}

	/**
	 * Create operations
	 */
	public void add() {
		bookdao.add(book);
	}

	/**
	 * CreateAuthor operations
	 */
	public void addAuthor() {
		bookdao.addAuthors(idbook, idauthor);
	}

	/**
	 * DeleteAuthor operations
	 */
	public void deleteAuthor() {
		bookdao.deleteAuthors(idbook, idauthor);
	}

	/**
	 * Delete operations
	 */
	public void delete() {
		bookdao.delete(idbook);
	}

	/**
	 * Update operations
	 */
	public void update() {
		bookdao.update(book, idbook);
	}

}
