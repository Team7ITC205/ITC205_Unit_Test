package library.tests.unit.entities;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import library.entities.Book;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBook {
	
	//variable declared
	private String author = "author";
	private String title = "title";
	private String callNumber = "callNumber";
	private int bookId = 1;
			
	private Book book;
	
	//The @Before method will be run before and after every test case, 
	//so will probably be run multiple times during a test run.
	@Before
	public void setUp() throws Exception {
		book = new Book(author, title, callNumber, bookId);
	}

	//The @Before and @After methods will be run before and after every test case, 
	// so will probably be run multiple times during a test run.
	@After
	public void tearDown() throws Exception {
		book = null;
	}

	//test valid Constructor
	@Test
	public void testValidConstructor() {
		Book book = new Book(author, title, callNumber, bookId);
		assertNotNull(book);
		assertTrue(book instanceof IBook);
	}
	
	//Method that test invalid Constructor 
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidConstructor() {
		new Book(author, title, null, bookId);
	}
	
	//Method that Test the Borrow Book Available  
	@Test
	public void testBorrowBookAvailable() {
		ILoan mockLoan = createMock(ILoan.class);
		book.borrow(mockLoan);
		EBookState actualState = book.getState();
		assertEquals(EBookState.ON_LOAN, actualState);
		ILoan actualLoan = book.getLoan();
		assertEquals(mockLoan, actualLoan);
	}
	
	
	//Method that Test the Borrow Book not Available
	@Test(expected=RuntimeException.class)
	public void testBorrowBookNotAvailable() {
		ILoan mockLoan = createMock(ILoan.class);
		book.borrow(mockLoan);
		ILoan newMockLoan = createMock(ILoan.class);
		book.borrow(newMockLoan);
	}
	
	//Method that Test the get loan on loan 
	@Test
	public void testGetLoanOnLoan() {
		ILoan mockLoan = createMock(ILoan.class);
		book.borrow(mockLoan);
		ILoan actualLoan = book.getLoan();
		assertEquals(mockLoan, actualLoan);
	}
	
	//Method that Test the get loan not on loan 
	@Test
	public void testGetLoanNotOnLoan() {
		ILoan actual = book.getLoan();
		assertEquals(null, actual);
	}
	
	//Method that Test Return Book Not on loan
	@Test(expected=RuntimeException.class)
	public void testReturnBookNotOnLoan() {
		book.returnBook(false);
	}
	
	//Method that Test Return Book Damaged
	@Test
	public void testReturnBookDamaged() {
		ILoan loan = createMock(ILoan.class);
		book.borrow(loan);
		book.returnBook(true);
		EBookState actual = book.getState();
		assertEquals(EBookState.DAMAGED, actual);
	}
	
	//Method that Test Return Book not Damaged 
	@Test
	public void testReturnBookNotDamaged() {
		ILoan loan = createMock(ILoan.class);
		book.borrow(loan);
		book.returnBook(false);
		EBookState actualState = book.getState();
		assertEquals(EBookState.AVAILABLE, actualState);
		ILoan actualLoan = book.getLoan();
		assertEquals(null, actualLoan);
	}
	
	//Method that Test Lose not on loan 
	@Test(expected=RuntimeException.class)
	public void testLoseNotOnLoan() {
		book.lose();
	}
	
	//Method that Test Lose on loan  
	@Test
	public void testLoseOnLoan() {
		ILoan loan = createMock(ILoan.class);
		book.borrow(loan);
		book.lose();
		EBookState actual = book.getState();
		assertEquals(EBookState.LOST, actual);
	}
	
	//Method that Test Repair Book Damaged
	@Test
	public void testRepairBookDamaged() {
		ILoan mockLoan = createMock(ILoan.class);
 		book.borrow(mockLoan);
 		book.returnBook(true);
 		EBookState actualState = book.getState();
 		assertEquals(EBookState.DAMAGED, actualState);
 		
 		book.repair();
 		actualState = book.getState();
 		assertEquals(EBookState.AVAILABLE, actualState);
	}
	
	//Method that Test Repair Book not Damaged 
	@Test(expected=RuntimeException.class)
	public void testRepairBookNotDamaged() {
		book.repair();
	}
	
	//Method that Test Dispose on loan 
	@Test(expected=RuntimeException.class)
	public void testDisposeOnLoan() {
		ILoan mockLoan = createMock(ILoan.class);
		book.borrow(mockLoan);
		assertEquals(EBookState.ON_LOAN, book.getState());
		book.dispose();
	}
	
	////Method that Test Dispose not on loan 
	@Test
	public void testDisposeNotOnLoan() {
		book.dispose();
		EBookState actual = book.getState();
		assertEquals(EBookState.DISPOSED, actual);
	}
	
	@Test
	public void testGetState() {
		EBookState actual = book.getState();
		assertEquals(EBookState.AVAILABLE, actual);
	}
	
	//Method that Test Get Author 
	@Test
	public void testGetAuthor() {
		String actual = book.getAuthor();
		assertEquals(author, actual);
	}
	
	//Method that Test Get Title 
	@Test
	public void testGetTitle() {
		String actual = book.getTitle();
		assertEquals(title, actual);
	}
	
	//Method that Test Get Call Number 
	@Test
	public void testGetCallNumber() {
		String actual = book.getCallNumber();
		assertEquals(callNumber, actual);
	}
	
	//Method that Test Get ID 
	@Test
	public void testGetID() {
		int actual = book.getID();
		assertEquals(bookId, actual);
	}

}