package librarync;

import java.util.*;

public class BookDAO {
	private static List<Book> books;
	{
		books = new ArrayList<Book>();
	}
	
	public static void addBook (Book book) {
		for(int i=0; i< books.size(); i++)
		{
			if(books.get(i).equals(book) == false)
			{
				if(i == (books.size()-1))
				{
					books.add(book);
				}
				i++;
			}
			System.out.println("No");
		}
	}
	
	public static String getBooks () {
		String str = null;
		for(int i=0; i< books.size(); i++)
		{
			str = str + books.get(i).toString() + "\t";
		}
		return str;
	}
	
	public static String getBooksShablon (String str) {
		//доделать, найти функцию вхождения строки
		String str2 = null;
		for(int i=0; i< books.size(); i++)
		{
			str = str + books.get(i).toString() + "\t";
		} 
		return str2;
	}
	public static void updateBook (Book book, int year) {
		for(int i=0; i< books.size(); i++)
		{
			if(books.get(i).equals(book) == true)
			{
				books.get(i).setYear(year);
			}
			System.out.println("No");
		}
	}
	
	public static void deleteBook (Book book) {
		for(int i=0; i< books.size(); i++)
		{
			if(books.get(i).equals(book) == true)
			{
				books.remove(books.get(i));
			}
			System.out.println("No");
		}
	}
}
