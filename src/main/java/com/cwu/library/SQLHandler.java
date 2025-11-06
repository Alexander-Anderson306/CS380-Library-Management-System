package com.cwu.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.LinkedList;

public class SQLHandler {

    private static Connection conn = null;

    //-------------------------------Connection Handling--------------------------------//

    /**
    * Load the MySQL JDBC driver.
    *
    * This method loads the MySQL JDBC driver class, which is required for
    * connecting to the database.
    *
    * @throws ClassNotFoundException if the driver class is not found
    */
    public static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() {
        //TA SET USERNAME AND PW HERE
        try {
            if(conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "");
                IO.println("Connected to database.");
                return conn;
            } else {
                IO.println("Already connected to database.");
            }
        } catch (SQLException e) {
            // handle any errors
            IO.println("Failed to connect to database.");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return null;
    }

    /**
    * Closes the connection to the database. If the connection is already closed or null, prints a message to the console.
    * 
    * @param conn the connection to the database
    * @throws SQLException if there was an error closing the connection
    */
    public static void disconnect(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            } else {
                IO.println("Already disconnected from database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //-------------------------------Student Handling--------------------------------//
    /**
     * Adds a student to the database.
     * @param person
     */
    public static void addStudent(Person person)  {
        if(studentExists(person.getId())) {
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO student (student_id, fname, lname) VALUES (?, ?, ?)");
            ps.setInt(1, person.getId());
            ps.setString(2, person.getFirstName());
            ps.setString(3, person.getLastName());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a student from the database.
     * @param studentId
     */
    public static void removeStudent(int studentId) {
        if(!studentExists(studentId)) {
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM student WHERE student_id = ?");
            ps.setInt(1, studentId);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This gets all checked out items from a student. (I.E their inventory)
     * @param student Student to get checked out items for.
     * @return LinkedList<Item> of all checked out items.
     */
    public static LinkedList<Item> getCheckedoutItems(Person student) {
        //we have a student id search by that otherwise we search by name
        String sql = """
            SELECT s.student_id, s.name, si.item_id, 
            b.title AS book_title, d.title AS dvd_title
            FROM student s
            JOIN student_inventory si ON s.student_id = si.student_id
            LEFT JOIN book b ON si.item_id = b.book_id
            LEFT JOIN dvd d  ON si.item_id = d.dvd_id
            WHERE s.student_id = ?;
        """;

        LinkedList<Item> items = new LinkedList<>();
        LinkedList<Integer> studentIds = new LinkedList<>();
        try {
            if(student.getId() != -1) {
                //search by single student id
                studentIds.add(student.getId());
            } else {
                //first get all the students with that name
                PreparedStatement ps = conn.prepareStatement("SELECT student_id FROM student WHERE fname = ? AND lname = ?");
                ps.setString(1, student.getFirstName());
                ps.setString(2, student.getLastName());
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    studentIds.add(rs.getInt("student_id"));
                }
                rs.close();
                ps.close();
            }

                //now get all the checked out items for each student id
            for(int id : studentIds) {
                //first join student id and student inventory to get book ids
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                //parse results
                while(rs.next()) {
                    int itemId = rs.getInt("item_id");
                    Item current = new Item(itemId);
                    //handle if its a book or a dvd
                    if(rs.getString("book_title") != null) {
                        String title = rs.getString("book_title");
                        current.initializeBook(title, 0, 0, null);
                    } else {
                        String title = rs.getString("dvd_title");
                        current.initializeCD(title, 0, null);
                    }
                    items.add(current);
                }
                rs.close();
                ps.close();
            }

            return items;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if an item is checked out.
     * @param item Item to check.
     * @return true if checked out, false otherwise.
     */
    public static boolean isCheckedOut(Item item) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM student_inventory WHERE item_id = ?");
            ps.setInt(1, item.getId());
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    //-------------------------------Author Handling--------------------------------//
    public static void addAuthor(Person author) {

        if(authorExists(author.getFirstName(), author.getLastName())) {
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO author (fname, lname) VALUES (?, ?)");
            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an author from the database.
     * @param authorId
     */
    public static void deleteAuthor(int authorId) {
        if(!authorExists(authorId)) {
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM author WHERE author_id = ?");
            ps.setInt(1, authorId);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the author ID from the database and sets it in the Person object.
     * @param author
     */
    public static void getAuthorId(Person author) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT author_id FROM author WHERE fname = ? AND lname = ?");
            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                author.setId(rs.getInt("author_id"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all books by the given author.
     * @param author
     * @return LinkedList<Item> of books by the author.
     */
    public static LinkedList<Item> getBooksByAuthor(Person author) {
        LinkedList<Item> books = new LinkedList<>();
        String sql  = """
                        SELECT 
                        b.book_id,
                        b.title,
                        b.publication_year,
                        a.author_id,
                        a.fname,
                        a.lname
                        FROM book b
                        JOIN book_authors ba ON b.book_id = ba.book_id
                        JOIN author a ON ba.author_id = a.author_id
                        WHERE a.author_id = ?;
                    """;

        try {
            //check if the author has an id
            if(author.getId() == -1) {
                //see if this author exist
                if(!authorExists(author.getFirstName(), author.getLastName())) {
                    //author does not exist no books to return
                    return books;
                }
                //get the author id
                getAuthorId(author);
            }

            //now get all books by that author
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, author.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Item current = new Item(rs.getInt("book_id"));
                current.initializeBook(
                    rs.getString("book_title"),
                    rs.getInt("publication_year"),
                    rs.getInt("isbn"),
                    rs.getString("call_number")
                );
                books.add(current);
            }
            rs.close();
            ps.close();

            return books;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    //-------------------------------Existence Checking--------------------------------//
    /**
     * Checks if a book exists in the database.
     * @param bookId
     * @return true if exists, false otherwise.
     */
    public static boolean bookExists(int bookId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM book WHERE book_id = ?");
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Checks if a cd exists in the database.
     * @param cdId
     * @return true if exists, false otherwise.
     */
    public static boolean cdExists(int cdId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM dvd WHERE dvd_id = ?");
            ps.setInt(1, cdId);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Checks if a student exists in the database.
     * @param studentId
     * @return true if exists, false otherwise.
     */
    public static boolean studentExists(int studentId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM student WHERE student_id = ?");
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if an author exists in the database.
     * @param authorId
     * @return
     */
    public static boolean authorExists(int authorId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM author WHERE author_id = ?");
            ps.setInt(1, authorId);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if an author exists in the database.
     * @param fname First name of the author
     * @param lname Last name of the author
     * @return
     */
    public static boolean authorExists(String fname, String lname) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM author WHERE fname = ? AND lname = ?");
            ps.setString(1, fname);
            ps.setString(2, lname);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if a book-author relation exists in the database.
     * @param bookId
     * @param authorId
     * @return true if exists, false otherwise.
     */
    public static boolean bookAuthorsExist(int bookId, int authorId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM book_authors WHERE book_id = ? AND author_id = ?");
            ps.setInt(1, bookId);
            ps.setInt(2, authorId);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if a student-inventory relation exists in the database.
     * @param studentId
     * @param itemId
     * @return true if exists, false otherwise.
     */
    public static boolean studentInventoryExists(int studentId, int itemId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM student_inventory WHERE student_id = ? AND item_id = ?");
            ps.setInt(1, studentId);
            ps.setInt(2, itemId);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //-------------------------------Book Methods--------------------------------//
    
    /**
     * Adds a book to the database.
     * @param book
     */
    public static void addBook(Item book) {
        try {
            if(bookExists(book.getId())) {
                return;
            }

            PreparedStatement ps = conn.prepareStatement("INSERT INTO book (book_title, publication_year, isbn, call_number) VALUES (?, ?, ?, ?)");
            ps.setString(1, book.getAttribute("title"));
            ps.setInt(2, Integer.parseInt(book.getAttribute("publication_year")));
            ps.setInt(3, Integer.parseInt(book.getAttribute("isbn")));
            ps.setString(4, book.getAttribute("call_number"));
            ps.execute();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a book from the database.
     * @param bookId
     */
    public static void removeBook(int bookId) {
        try {
            if(!bookExists(bookId)) {
                return;
            }

            PreparedStatement ps = conn.prepareStatement("DELETE FROM book WHERE book_id = ?");
            ps.setInt(1, bookId);
            ps.execute();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifies a book in the database.
     * @param book
     */
    public static void modifyBook(Item book) {
        try {
            if(!bookExists(book.getId())) {
                return;
            }

            PreparedStatement ps = conn.prepareStatement("UPDATE book SET book_title = ?, publication_year = ?, isbn = ?, call_number = ? WHERE book_id = ?");
            ps.setString(1, book.getAttribute("title"));
            ps.setInt(2, Integer.parseInt(book.getAttribute("publication_year")));
            ps.setInt(3, Integer.parseInt(book.getAttribute("isbn")));
            ps.setString(4, book.getAttribute("call_number"));
            ps.setInt(5, book.getId());
            ps.execute();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Queries books from the database based on the attributes set in the book parameter.
     * @param book
     * @return LinkedList<Item> of books that match the query.
     */
    public static LinkedList<Item> queryBooks(Item book) {
        try {
            //result list
            LinkedList<Item> books = new LinkedList<>();
            //construct our query from what attributes are set in the book parameter
            String sql = "SELECT * FROM book WHERE 1=1";
            if(book.hasAttribute("title")) {
                sql += " AND book_title LIKE ?";
            }
            if(book.hasAttribute("publication_year")) {
                sql += " AND publication_year = ?";
            }
            if(book.hasAttribute("isbn")) {
                sql += " AND isbn = ?";
            }
            if(book.hasAttribute("call_number")) {
                sql += " AND call_number = ?";
            }

            //set the parameters
            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            if(book.hasAttribute("title")) {
                ps.setString(index++, "%" + book.getAttribute("title") + "%");
            }
            if(book.hasAttribute("publication_year")) {
                ps.setInt(index++, Integer.parseInt(book.getAttribute("publication_year")));
            }
            if(book.hasAttribute("isbn")) {
                ps.setInt(index++, Integer.parseInt(book.getAttribute("isbn")));
            }
            if(book.hasAttribute("call_number")) {
                ps.setString(index++, book.getAttribute("call_number"));
            }

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                //create a new book item
                Item current = new Item(rs.getInt("book_id"));
                current.initializeBook(
                    rs.getString("book_title"),
                    rs.getInt("publication_year"),
                    rs.getInt("isbn"),
                    rs.getString("call_number")
                );
                books.add(current);
            }
            rs.close();
            ps.close();

            return books;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //-------------------------------CD Methods--------------------------------//

    /**
     * Adds a CD to the database.
     * @param cd
     */
    public static void addCD(Item cd) {
        try {
            if(cdExists(cd.getId())) {
                return;
            }

            PreparedStatement ps = conn.prepareStatement("INSERT INTO dvd (dvd_title, publication_year, call_number) VALUES (?, ?, ?)");
            ps.setString(1, cd.getAttribute("title"));
            ps.setInt(2, Integer.parseInt(cd.getAttribute("publication_year")));
            ps.setString(3, cd.getAttribute("call_number"));
            ps.execute();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a CD from the database.
     * @param cdId
     */
    public static void removeCD(int cdId) {
        try {
            if(!cdExists(cdId)) {
                return;
            }

            PreparedStatement ps = conn.prepareStatement("DELETE FROM dvd WHERE dvd_id = ?");
            ps.setInt(1, cdId);
            ps.execute();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifies a CD in the database.
     * @param cd
     */
    public static void modifyCD(Item cd) {
        try {
            if(!cdExists(cd.getId())) {
                return;
            }

            PreparedStatement ps = conn.prepareStatement("UPDATE dvd SET dvd_title = ?, publication_year = ?, call_number = ? WHERE dvd_id = ?");
            ps.setString(1, cd.getAttribute("title"));
            ps.setInt(2, Integer.parseInt(cd.getAttribute("publication_year")));
            ps.setString(3, cd.getAttribute("call_number"));
            ps.setInt(4, cd.getId());
            ps.execute();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Queries CDs from the database based on the attributes set in the cd parameter.
     * @param cd
     * @return LinkedList<Item> of CDs that match the query.
     */
    public static LinkedList<Item> queryCDs(Item cd) {
        try {
            //result list
            LinkedList<Item> cds = new LinkedList<>();
            //construct our query from what attributes are set in the cd parameter
            String sql = "SELECT * FROM dvd WHERE 1=1";
            if(cd.hasAttribute("title")) {
                sql += " AND dvd_title LIKE ?";
            }
            if(cd.hasAttribute("publication_year")) {
                sql += " AND publication_year = ?";
            }
            if(cd.hasAttribute("call_number")) {
                sql += " AND call_number = ?";
            }

            //set the parameters
            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            if(cd.hasAttribute("title")) {
                ps.setString(index++, "%" + cd.getAttribute("title") + "%");
            }
            if(cd.hasAttribute("publication_year")) {
                ps.setInt(index++, Integer.parseInt(cd.getAttribute("publication_year")));
            }
            if(cd.hasAttribute("call_number")) {
                ps.setString(index++, cd.getAttribute("call_number"));
            }

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                //create a new cd item
                Item current = new Item(rs.getInt("dvd_id"));
                current.initializeCD(
                    rs.getString("dvd_title"),
                    rs.getInt("publication_year"),
                    rs.getString("call_number")
                );
                cds.add(current);
            }
            rs.close();
            ps.close();

            return cds;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //-------------------------------Many to many handing--------------------------------//
    /**
     * Adds a book-author relation to the database.
     * @param bookId
     * @param authorId
     */
    public static void addBookAuthor(int bookId, int authorId) {
        //check if the relation already exists
        if(bookAuthorsExist(bookId, authorId)) {
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO book_authors (book_id, author_id) VALUES (?, ?)");
            ps.setInt(1, bookId);
            ps.setInt(2, authorId);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a book-author relation from the database.
     * @param bookId
     * @param authorId
     */
    public static void removeBookAuthor(int bookId, int authorId) {
        //check if the relation exists
        if(!bookAuthorsExist(bookId, authorId)) {
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM book_authors WHERE book_id = ? AND author_id = ?");
            ps.setInt(1, bookId);
            ps.setInt(2, authorId);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a student-inventory relation to the database.
     * @param studentId
     * @param itemId
     */
    public static void addStudentInventory(int studentId, int itemId) {
        //check if the relation already exists
        if(studentInventoryExists(studentId, itemId)) {
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO student_inventory (student_id, item_id) VALUES (?, ?)");
            ps.setInt(1, studentId);
            ps.setInt(2, itemId);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a student-inventory relation from the database.
     * @param studentId
     * @param itemId
     */
    public static void removeStudentInventory(int studentId, int itemId) {
        //check if the relation exists
        if(!studentInventoryExists(studentId, itemId)) {
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM student_inventory WHERE student_id = ? AND item_id = ?");
            ps.setInt(1, studentId);
            ps.setInt(2, itemId);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if an item is in the inventory.
     * @param itemId
     * @return true if in inventory, false otherwise.
     */
    public static boolean itemInInventory(int itemId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM student_inventory WHERE item_id = ?");
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Gets the student ID of the student who has checked out the item.
     * @param itemId
     * @return
     */
    public static int itemCheckedOutTo(int itemId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT student_id FROM student_inventory WHERE item_id = ?");
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int studentId = rs.getInt("student_id");
                rs.close();
                ps.close();
                return studentId;
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     *  This subclass is used to intersect two sets of data (linked list) and return the common elements.
     */
    public class Intersect {
        public static <T> LinkedList<T> intersect(LinkedList<T> list1, LinkedList<T> list2) {
            LinkedList<T> result = new LinkedList<>();
            for(T item : list1) {
                if(list2.contains(item)) {
                    result.add(item);
                }
            }
            return result;
        }
    }
}
