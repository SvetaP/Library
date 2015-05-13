package librarymodel;

import java.util.*;
import java.io.*;

public class Book extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3443003900037673289L;

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

	public Book(List<Authors> authors, String title, Integer year, Integer pages) {
		this.authors = authors;
		this.title = title;
		this.year = year;
		this.pages = pages;
	}

	public String toString() {
		String str = "";
		for (int i = 0; i < authors.size(); i++) {
			str += authors.get(i).toString() + " ";
		}
		return str + this.title + " " + this.year + " " + this.pages;
	}

	public String toStringAuthors() {
		String str = "";
		for (int i = 0; i < authors.size(); i++) {
			str = str + authors.get(i).toString() + " ";
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
