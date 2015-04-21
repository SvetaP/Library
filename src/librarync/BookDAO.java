package librarymodel;

import java.util.*;
import java.util.zip.*;
import java.beans.*;
import java.io.*;


public class BookDAO{
	private static List<Book> books;
	{
		books = new ArrayList<Book>();
	}
	
	public static void addBook (Book book) throws IOException{
		for(int i=0; i< books.size(); i++)
		{
			if(books.get(i).equals(book) == false)
			{
				if(i == (books.size()-1))
				{
					books.add(book);
					
					File f1 = new File(book.toStringAuthors()+book.getTitle()+".txt");
					FileOutputStream fos = new FileOutputStream(f1);
					GZIPOutputStream zos = new GZIPOutputStream(fos);
					ObjectOutputStream oos = new ObjectOutputStream(zos);
					oos.writeObject(book);
					oos.close();
					
					File f2 = new File(book.toStringAuthors()+book.getTitle()+".xml");
					FileOutputStream out = new FileOutputStream(f2);
					XMLEncoder xmlEncoder = new XMLEncoder(out);
					xmlEncoder.writeObject(book);
					xmlEncoder.flush();
					xmlEncoder.close();
				}
				i++;
			}
		}
		
	}
	
	public static List<Book> getBooks () {
		return books;
	}
	
	public static List<Book> getBooksOnAuthors (String str){
		List<Book> bookstemplate = new ArrayList<Book>();
		for(Book b : books){
			String[] str1 = b.toStringAuthors().split(" ");
			if(str.trim().startsWith("%") == true){
				String str2 = str.trim().split("%")[str.trim().split("%").length-1];
				for(int i=0; i<str1.length; i++){
					if(str1[i].endsWith(str2) == true)
						bookstemplate.add(b);
				}
			}
			else{
				if(str.trim().endsWith("%") == true){
					String str3 = str.trim().split("%")[str.trim().split("%").length-1];
					for(int i=0; i<str1.length; i++){
						if(str1[i].startsWith(str3) == true)
							bookstemplate.add(b);
					}
				}
			
			else{
				if(str.trim().startsWith("%") == true && str.trim().endsWith("%") == true){
					String str4 = str.trim().split("%")[str.trim().split("%").length-1];
					for(int i=0; i<str1.length; i++){
						if(str1[i].indexOf(str4)!=(-1))
							bookstemplate.add(b);
					}
				}
				else{
					for(int i=0; i<str1.length; i++){
						if(str1[i].compareToIgnoreCase(str) == 0)
							bookstemplate.add(b);
						}
					}
				}	
			}
		}
		return bookstemplate;
	}
	
	public static List<Book> getBooksOnTitle (String str) {
		List<Book> bookstemplate = new ArrayList<Book>();
		for(Book b : books){
			if(str.trim().startsWith("%") == true){
				String str2 = str.trim().split("%")[str.trim().split("%").length-1];
				if(b.getTitle().endsWith(str2) == true)
						bookstemplate.add(b);
			}
			else{
				if(str.trim().endsWith("%") == true){
					String str3 = str.trim().split("%")[str.trim().split("%").length-1];
					if(b.getTitle().startsWith(str3) == true)
							bookstemplate.add(b);
				}
				else{
					if(str.trim().startsWith("%") == true && str.trim().endsWith("%") == true){
						String str4 = str.trim().split("%")[str.trim().split("%").length-1];
						if(b.getTitle().indexOf(str4)!=(-1))
							bookstemplate.add(b);
					}
					else{
						if(b.getTitle().compareToIgnoreCase(str) == 0)
							bookstemplate.add(b);
						}
				}
			}
		}		
		return bookstemplate;
	}
	
	public static void updateAuthors (Book book, List<String> authors) throws IOException, FileNotFoundException, ClassNotFoundException {
		for(Book b : books)
		{
			if(b.equals(book) == true)
			{
				b.setAuthors(authors);
				
				File f1 = new File(b.toStringAuthors()+b.getTitle()+".txt");
				FileInputStream fis = new FileInputStream(f1);
				GZIPInputStream gs = new GZIPInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(gs);
				Book b1 = (Book) ois.readObject();
				fis.close();
				ois.close();		
				b1.setAuthors(authors);
				f1.delete();
				File f2 = new File(b1.toStringAuthors()+b1.getTitle()+".txt");
				FileOutputStream fos = new FileOutputStream(f2);
				GZIPOutputStream zos = new GZIPOutputStream(fos);
				ObjectOutputStream oos = new ObjectOutputStream(zos);
				oos.writeObject(b1);
				oos.close();
				
				File f3 = new File(b.toStringAuthors()+b.getTitle()+".xml");
				FileInputStream in = new FileInputStream(f3);
				XMLDecoder xmlDecoder = new XMLDecoder(in);
				Book b2 = (Book) xmlDecoder.readObject();
				xmlDecoder.close();
				b2.setAuthors(authors);
				f3.delete();
				File f4 = new File(b2.toStringAuthors()+b2.getTitle()+".xml");
				FileOutputStream out = new FileOutputStream(f4);
				XMLEncoder xmlEncoder = new XMLEncoder(out);
				xmlEncoder.writeObject(b2);
				xmlEncoder.flush();
				xmlEncoder.close();
			}
		}
	}
	
	public static void updateTitle (Book book, String title) throws IOException, FileNotFoundException, ClassNotFoundException {
		for(Book b : books)
		{
			if(b.equals(book) == true)
			{
				b.setTitle(title);
				
				File f1 = new File(b.toStringAuthors()+b.getTitle()+".txt");
				FileInputStream fis = new FileInputStream(f1);
				GZIPInputStream gs = new GZIPInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(gs);
				Book b1 = (Book) ois.readObject();
				fis.close();
				ois.close();		
				b1.setTitle(title);
				f1.delete();
				File f2 = new File(b1.toStringAuthors()+b1.getTitle()+".txt");
				FileOutputStream fos1 = new FileOutputStream(f2);
				GZIPOutputStream zos1 = new GZIPOutputStream(fos1);
				ObjectOutputStream oos1 = new ObjectOutputStream(zos1);
				oos1.writeObject(b1);
				oos1.close();
				
				File f3 = new File(b.toStringAuthors()+b.getTitle()+".xml");
				FileInputStream in = new FileInputStream(f3);
				XMLDecoder xmlDecoder = new XMLDecoder(in);
				Book b2 = (Book) xmlDecoder.readObject();
				xmlDecoder.close();
				b2.setTitle(title);
				f3.delete();
				File f4 = new File(b2.toStringAuthors()+b2.getTitle()+".xml");
				FileOutputStream out = new FileOutputStream(f4);
				XMLEncoder xmlEncoder = new XMLEncoder(out);
				xmlEncoder.writeObject(b2);
				xmlEncoder.flush();
				xmlEncoder.close();
			}
		}
	}

	public static void updateYear (Book book, int year) throws IOException, FileNotFoundException, ClassNotFoundException {
		for(Book b : books)
		{
			if(b.equals(book) == true)
			{
				b.setYear(year);
				
				File f1 = new File(b.toStringAuthors()+b.getTitle()+".txt");
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
				
				File f3 = new File(b.toStringAuthors()+b.getTitle()+".xml");
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
			}
		}
	}
	
	public static void updatePage (Book book, int pages) throws IOException, FileNotFoundException, ClassNotFoundException {
		for(Book b : books)
		{
			if(b.equals(book) == true)
			{
				b.setPages(pages);
				
				File f1 = new File(b.toStringAuthors()+b.getTitle()+".txt");
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
				
				File f3 = new File(b.toStringAuthors()+b.getTitle()+".xml");
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
			}
		}
	}
	
	public static void deleteBook (Book book) throws IOException, FileNotFoundException {
		for(Book b : books)
		{
			if(b.equals(book) == true)
			{
				books.remove(b);
				
				File f = new File(b.toStringAuthors()+b.getTitle()+".txt");
				f.delete();
				
				File f2 = new File(b.toStringAuthors()+b.getTitle()+".xml");
				f2.delete();
			}
		}
	}
}
