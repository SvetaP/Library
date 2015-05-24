package librarymodel;

import java.io.Serializable;
import java.util.*;

/**
 * 
 * @author SvetaP
 */
public class Book extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 978401494087999128L;

	private List<Authors> authors;
	private String title;
	private Integer year;
	private Integer pages;

	public void setAuthors(List<Authors> authors) {
		this.authors = authors;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public List<Authors> getAuthors() {
		return this.authors;
	}

	public String getTitle() {
		return this.title;
	}

	public Integer getYear() {
		return this.year;
	}

	public Integer getPages() {
		return this.pages;
	}

	public Book() {
		super();
	}

	public Book(String title, Integer year, Integer pages) {
		this.authors = null;
		this.title = title;
		this.year = year;
		this.pages = pages;
	}

	public Book(List<Authors> authors, String title, Integer year, Integer pages) {
		this.authors = authors;
		this.title = title;
		this.year = year;
		this.pages = pages;
	}

	public String toString() {
		if (authors.toString() == null) {
			return this.title + " " + this.year + " " + this.pages;
		} else
			return authors.toString() + this.title + " " + this.year + " "
					+ this.pages;
	}

	public String toStringAuthors() {
		String str = "";
		for (int i = 0; i < authors.size(); i++) {
			str += authors.get(i) + " ";
		}
		return str;
	}

	public boolean equals(Object o) {
		if (o != null && (o instanceof Book) && o.hashCode() == hashCode()) {
			return ((Book) o).authors.equals(this.authors)
					&& ((Book) o).title.equals(this.title)
					&& ((Book) o).year.equals(this.year)
					&& ((Book) o).pages.equals(this.pages);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return (int) (((authors.isEmpty() == true) ? 0 : authors.hashCode())
				+ ((title == null) ? 0 : title.hashCode()) + year + pages);
	}
}
