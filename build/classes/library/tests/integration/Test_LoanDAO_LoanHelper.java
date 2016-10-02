package library.tests.integration;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import library.daos.*;
import library.interfaces.daos.*;
import library.interfaces.entities.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test_LoanDAO_LoanHelper {

	private ILoanHelper loanHelper;
	private ILoanDAO loanDAO;
	
	private String author = "author";
	private String title = "title";
	private String callNumber = "callNumber";
	
	String fName = "fName";
	String lName = "lName";
	String phone = "phone";
	String email = "email";
	
	int bookId = 1;
	int memberId = 1;

	IBook book;
	IMember borrower;
	
	IBookHelper bookHelper = new BookHelper();
	IMemberHelper memberHelper = new MemberHelper();
	
	Date borrowDate;
	Date dueDate;
	
	@Before
	public void setUp() throws Exception {
		loanHelper = new LoanHelper();
		loanDAO = new LoanMapDAO(loanHelper);
		
		Calendar calendar = Calendar.getInstance();
		borrowDate = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, ILoan.LOAN_PERIOD);
		dueDate = calendar.getTime();
		
		book = bookHelper.makeBook(author, title, callNumber, bookId);
		borrower = memberHelper.makeMember(fName, lName, phone, email, memberId);
		
	}

	@After
	public void tearDown() throws Exception {
		loanHelper = null;
		loanDAO = null;
		
		borrowDate = null;
		dueDate = null;
		book = null;
		borrower = null;
	}

	@Test
	public void testValidConstructor() {
		ILoanHelper helper = new LoanHelper();
		ILoanDAO testLoanDAO = new LoanMapDAO(helper);
		assertNotNull(testLoanDAO);
		assertTrue(testLoanDAO instanceof ILoanDAO);
	}
	
	@Test
	public void testCreateNewPendingList() {
		loanDAO.createNewPendingList(borrower);
		List<ILoan> loans = loanDAO.getPendingList(borrower);
		assertNotNull(loans);
		assertTrue(loans.size() == 0);
	}

	@Test
	public void testCreatePendingLoan() {
		loanDAO.createNewPendingList(borrower);
		ILoan loan = loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
		List<ILoan> actual = loanDAO.getPendingList(borrower);
		assertNotNull(actual);
		assertTrue(actual.contains(loan));
	}
	
	@Test(expected=RuntimeException.class)
	public void testCreatePendingLoanException() {
		loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
	}
	
	@Test(expected=RuntimeException.class)
	public void testCreatePendingLoanForBookTwice() {
		loanDAO.createNewPendingList(borrower);
		loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
		loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
	}
	
	@Test
	public void testGetPendingList() {
		loanDAO.createNewPendingList(borrower);
		List<ILoan> loans = loanDAO.getPendingList(borrower);
		assertNotNull(loans);
		assertTrue(loans.size() == 0);
	}
	
	@Test(expected=RuntimeException.class)
	public void testGetPendingListException() {
		loanDAO.getPendingList(borrower);
	}
	
	@Test
	public void testCommitPendingLoans() {
		//Perform actions
		loanDAO.createNewPendingList(borrower);
		ILoan loan = loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
		loanDAO.commitPendingLoans(borrower);
		
		//Check loans state
		List<ILoan> committedLoans = loanDAO.listLoans();
		assertTrue(committedLoans.contains(loan));
		
		//Maybe use reflection of loan state here
//		assertEquals(LoanState.CURRENT, loan.getState?);
		List<ILoan> memberLoans = borrower.getLoans();
		assertTrue(memberLoans.contains(loan));
	}
	
	@Test
	public void testClearPendingLoans() {
		//Perform actions
		loanDAO.createNewPendingList(borrower);
		ILoan loan = loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
		
		List<ILoan> actual = loanDAO.getPendingList(borrower);
		assertEquals(1, actual.size());
		assertTrue(actual.contains(loan));
		
		loanDAO.clearPendingLoans(borrower);
		actual = loanDAO.getPendingList(borrower);
		assertEquals(0, actual.size());
		assertFalse(actual.contains(loan));
	}	
	
	@Test
	public void testGetLoanByID() {
		
		//Perform actions
		loanDAO.createNewPendingList(borrower);
		ILoan loan = loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
		loanDAO.commitPendingLoans(borrower);

		ILoan actual = loanDAO.getLoanByID(loan.getID());
		assertEquals(loan, actual);
	}
	
	@Test
	public void testGetLoanByBook() {

		//Perform actions
		loanDAO.createNewPendingList(borrower);
		ILoan loan = loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
		loanDAO.commitPendingLoans(borrower);
		
		ILoan actual = loanDAO.getLoanByBook(book);
		assertEquals(loan, actual);
	}
	
	@Test
	public void testListLoans() {
		List<ILoan> actual = loanDAO.listLoans();
		assertNotNull(actual);
		assertEquals(0, actual.size());
		
		//Perform actions
		loanDAO.createNewPendingList(borrower);
		ILoan loan = loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
		loanDAO.commitPendingLoans(borrower);

		actual = loanDAO.listLoans();
		assertTrue(actual.contains(loan));
		assertEquals(1, actual.size());
	}
	
	@Test
	public void testFindLoansByBorrower() {

		//Perform actions
		loanDAO.createNewPendingList(borrower);
		ILoan loan = loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
		loanDAO.commitPendingLoans(borrower);

		List<ILoan> actual = loanDAO.findLoansByBorrower(borrower);
		assertNotNull(actual);
		assertEquals(1, actual.size());
		assertTrue(actual.contains(loan));
	}

	@Test
	public void testFindLoansByBookTitle() {

		//Perform actions
		loanDAO.createNewPendingList(borrower);
		ILoan loan = loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
		loanDAO.commitPendingLoans(borrower);
			
		List<ILoan> actual = loanDAO.findLoansByBookTitle(title);
		
		assertNotNull(actual);
		assertEquals(1, actual.size());
		assertTrue(actual.contains(loan));
	}
	
	@Test
	public void testUpdateOverDueStatus() {

		//Perform actions
		loanDAO.createNewPendingList(borrower);
		ILoan loan = loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
		loanDAO.commitPendingLoans(borrower);
		
		Calendar calender = Calendar.getInstance();
		calender.setTime(dueDate);
		calender.add(Calendar.DAY_OF_YEAR, 1);
		
		loanDAO.updateOverDueStatus(calender.getTime());
		assertTrue(loan.isOverDue());
		assertTrue(borrower.hasOverDueLoans());
		
		List<ILoan> loans = loanDAO.findOverDueLoans();
		assertTrue(loans.contains(loan));
	}

	@Test
	public void testFindOverDueLoans() {
		
		//Perform actions
		loanDAO.createNewPendingList(borrower);
		ILoan loan = loanDAO.createPendingLoan(borrower, book, borrowDate, dueDate);
		loanDAO.commitPendingLoans(borrower);
		
		Calendar calender = Calendar.getInstance();
		calender.setTime(dueDate);
		calender.add(Calendar.DAY_OF_YEAR, 1);
		loan.checkOverDue(calender.getTime());
		assertTrue(loan.isOverDue());
		
		List<ILoan> actual = loanDAO.findOverDueLoans();

		assertNotNull(actual);
		assertEquals(1, actual.size());
		assertTrue(actual.contains(loan));
	}

}
