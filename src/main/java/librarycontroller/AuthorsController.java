package librarycontroller;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import librarydao.AuthorsDAO;
import librarymodel.Authors;

/**
 * Authors Controller class allows users to do CRUD operations
 * 
 * @author SvetaP
 */
@ManagedBean(name = "authorsController")
@ViewScoped
public class AuthorsController implements Serializable {
	private static final long serialVersionUID = -8233536241169291062L;

	// Loading authors list
	private List<Authors> authors;
	// Creating new author
	private Authors author = new Authors();
	// ID
	private BigInteger id;

	@ManagedProperty("#{authorsDAO}")
	private AuthorsDAO authorsdao;

	/**
	 * Default constructor
	 */
	public AuthorsController() {
	}

	/**
	 * Getters, Setters
	 * 
	 * @return
	 */

	public Authors getAuthor() {
		return author;
	}

	/**
	 *
	 * @param author
	 */
	public void setAuthor(Authors author) {
		this.author = author;
	}

	/**
	 *
	 * @return
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}

	/**
	 *
	 * @return
	 */
	public AuthorsDAO getAuthorsdao() {
		return authorsdao;
	}

	/**
	 *
	 * @param authorsdao
	 */
	public void setAuthorsdao(AuthorsDAO authorsdao) {
		this.authorsdao = authorsdao;
	}

	/**
	 *
	 * @return
	 */
	public List<Authors> getAuthors() {
		return authors;
	}

	/**
	 * Initializing Data Access Object for Authors
	 * 
	 */
	@PostConstruct
	public void getAll() {
		authors = authorsdao.getAll();
		Collections.sort(authors, new Comparator<Authors>() {
			@Override
			public int compare(Authors author1, Authors author2) {
				return author1.getId().compareTo(author2.getId());
			}
		});
	}

	/**
	 * Create operations
	 */
	public void add() {
		authorsdao.add(author);
	}

	/**
	 * Delete operations
	 */
	public void delete() {
		authorsdao.delete(id);
	}

	/**
	 * Update operations
	 */
	public void update() {
		authorsdao.update(author, id);
	}

}
