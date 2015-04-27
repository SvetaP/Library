package librarydao;

import java.util.*;

import librarymodel.Book;

public interface BookDbDAO {
	void add(Book book);

	List<Book> getBooks();

	void updateAuthors(Book book, List<String> authors);

	void updateTitle(Book book, String title);

	void updateYear(Book book, int year);

	void updatePage(Book book, int pages);

	void delete(Book book);
}
