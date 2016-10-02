package library.tests.integration;

import static org.junit.Assert.*;

import java.util.*;

import library.entities.*;
import library.interfaces.entities.*;

import org.junit.*;

public class Test_Book_Member_Loan {

	private String author = "author";
	private String title = "title";
	private String callNumber = "callNumber";
	private int bookId = 1;
			
	private Book book;
	
	String fName = "fName";
	String lName = "lName";
	String phone = "phone";
	String email = "email";
	int id = 1;
	
	ILoan loan;
	int loanId = 1;
	Date borrowDate;
	Date dueDate;
	
	IMember member;
	
	@Before
	public void setUp() throws Exception {
		member = new Member(fName, lName, phone, email, id);
		Calendar calendar = Calendar.getInstance();
		borrowDate = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, ILoan.LOAN_PERIOD);
		dueDate = calendar.getTime();
		book = new Book(author, title, callNumber, bookId);
		loan = new Loan(book, member, borrowDate, dueDate, loanId);
	}

	@After
	public void tearDown() throws Exception {
		member = null;
		loan = null;
		borrowDate = null;
		dueDate = null;
		book = null;
	}
	
	@Test
	public void testHasOverDueLoans() {
		loan.commit(bookId);
		member.addLoan(loan);
		assertFalse(member.hasOverDueLoans());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dueDate);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		loan.checkOverDue(calendar.getTime());
		
		assertTrue(loan.isOverDue());
		assertTrue(member.hasOverDueLoans());
	}
	
	@Test
	public void testHasReachedLoanLimit() {
		int newLoanId = loanId + 1;
		for (int i =0; i < Member.LOAN_LIMIT; i ++) {
			ILoan newLoan = new Loan(book, member, borrowDate, dueDate, newLoanId);
			member.addLoan(newLoan);
			newLoanId++;
		}
		assertTrue(member.getLoans().size() == Member.LOAN_LIMIT);
		assertTrue(member.hasReachedLoanLimit());
		assertTrue(member.getState() == EMemberState.BORROWING_DISALLOWED);
	}
	
	@Test
	public void testAddLoan() {
		member.addLoan(loan);
		List<ILoan> loans = member.getLoans();
		assertNotNull(loans);
		assertTrue(loans.contains(loan));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddLoanBorrowingDisallowed() {
		member.addFine(Member.FINE_LIMIT + 1);
		assertTrue(member.getState() == EMemberState.BORROWING_DISALLOWED);
		member.addLoan(loan);
	}

	@Test
	public void testRemoveLoan() {
		member.addLoan(loan);
		List<ILoan> loans = member.getLoans();
		assertNotNull(loans);
		assertTrue(loans.contains(loan));
		member.removeLoan(loan);
		assertFalse(member.getLoans().contains(loan));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRemoveLoanNotPresent() {
		member.removeLoan(loan);
	}
	
	@Test
	public void testBorrowBookAvailable() {
		book.borrow(loan);
		EBookState actualState = book.getState();
		assertEquals(EBookState.ON_LOAN, actualState);
		ILoan actualLoan = book.getLoan();
		assertEquals(loan, actualLoan);
	}
	
	@Test(expected=RuntimeException.class)
	public void testBorrowBookNotAvailable() {
		book.borrow(loan);
		ILoan newLoan = new Loan(book, member, borrowDate, dueDate, loanId+1);
		book.borrow(newLoan);
	}
	
	@Test
	public void testGetLoanOnLoan() {
		book.borrow(loan);
		ILoan actualLoan = book.getLoan();
		assertEquals(loan, actualLoan);
	}
	
	@Test
	public void testReturnBookDamaged() {
		book.borrow(loan);
		book.returnBook(true);
		EBookState actual = book.getState();
		assertEquals(EBookState.DAMAGED, actual);
	}
	
	@Test
	public void testReturnBookNotDamaged() {
		book.borrow(loan);
		book.returnBook(false);
		EBookState actualState = book.getState();
		assertEquals(EBookState.AVAILABLE, actualState);
		ILoan actualLoan = book.getLoan();
		assertEquals(null, actualLoan);
	}
	
	@Test
	public void testLoseOnLoan() {
		book.borrow(loan);
		book.lose();
		EBookState actual = book.getState();
		assertEquals(EBookState.LOST, actual);
	}
	
	@Test
	public void testRepairBookDamaged() {
 		book.borrow(loan);
 		book.returnBook(true);
 		EBookState actualState = book.getState();
 		assertEquals(EBookState.DAMAGED, actualState);
 		
 		book.repair();
 		actualState = book.getState();
 		assertEquals(EBookState.AVAILABLE, actualState);
	}
	
	@Test(expected=RuntimeException.class)
	public void testDisposeOnLoan() {
		book.borrow(loan);
		assertEquals(EBookState.ON_LOAN, book.getState());
		book.dispose();
	}
	
	@Test
	public void testGetBook() {
		book.borrow(loan);
		IBook actual = loan.getBook();
		assertEquals(book, actual);
	}
	
	@Test
	public void testLoanGetID() {
		int actual = loan.getID();
		assertEquals(bookId, actual);
	}
	
	@Test
	public void testGetBorrower() {
		IMember actual = loan.getBorrower();
		assertEquals(member, actual);
	}
	
	@Test
	public void testLoanGetBook() {
		IBook actual = loan.getBook();
		assertEquals(book, actual);
	}
}