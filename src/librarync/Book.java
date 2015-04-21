package librarymodel;

import java.util.*;
import java.io.*;

public class Book implements Serializable{
	
	private List<String> authors;
	private String title;
	private int year;
	private int pages;
	{
		authors = new ArrayList<String>();
	}
	
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public void setPages(int pages) {
		this.pages = pages;
	}

	public List<String> getAuthors() {
		return this.authors;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getYear() {
		return this.year;
	}
	
	public int getPages() {
		return this.pages;
	}
	
	public Book() {
		super();
	}

	public Book(List<String> authors, String title, int year, int pages) {
		this.authors = authors;
		this.title = title;
		this.year = year;
		this.pages = pages;
	}
	
	public String toString() {
		String str = "";
		for(int i=0; i<authors.size(); i++)
		{
			str = str + authors.get(i)+" ";
		}
		return str + this.title + " " + this.year +  " " + this.pages;
	}
	
	public String toStringAuthors() {
		String str = "";
		for(int i=0; i<authors.size(); i++)
		{
			str = str + authors.get(i)+" ";
		}
		return str;
	}

	public boolean equals(Object o) {
		if (o != null && (o instanceof Book) && o.hashCode() == hashCode()) {
			return ((Book) o).authors.equals(this.authors) && ((Book) o).title.equals(this.title) && ((Book) o).year == this.year && ((Book) o).pages == this.pages;
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return (int) (((authors.isEmpty() == true) ? 0 : authors.hashCode()) + ((title == null) ? 0 : title.hashCode()) + year + pages);
	}
}
