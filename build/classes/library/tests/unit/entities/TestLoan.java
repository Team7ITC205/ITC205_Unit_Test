package library.tests.unit.entities;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.Calendar;
import java.util.Date;

import library.entities.Loan;
import library.interfaces.entities.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLoan {
	IBook book = createMock(IBook.class);
	IMember member = createMock(IMember.class);
	int loanId = 1;
	Date borrowDate;
	Date dueDate;
	
	ILoan loan;

	//The @Before and @After methods will be run before and after every test case, 
	// so will probably be run multiple times during a test run.
	@Before
	public void setUp() throws Exception {
		Calendar calendar = Calendar.getInstance();
		borrowDate = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, ILoan.LOAN_PERIOD);
		dueDate = calendar.getTime();
		
		loan = new Loan(book, member, borrowDate, dueDate, loanId);
	}
	
	//The @After and @After methods will be run before and after every test case, 
	// so will probably be run multiple times during a test run.
	@After
	public void tearDown() throws Exception {
		loan = null;
		borrowDate = null;
		dueDate = null;
	}


	//Method that test valid Constructor
	@Test
	public void testValidConstructor() {
		Loan newLoan = new Loan(book, member, borrowDate, dueDate, loanId);
		assertNotNull(newLoan);
		assertTrue(newLoan instanceof Loan);
	}
	

	//Method that test invalid Constructor
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidConstructor() {
		new Loan(book, null, borrowDate, dueDate, loanId);
	}
	
	//Method that test Commit Loan not Pending 
	@Test(expected=RuntimeException.class)
	public void testCommitLoanNotPending() {
		loan.commit(loanId);
		loan.commit(loanId);
	}
	
	//Method that test Complete
	@Test(expected=RuntimeException.class)
	public void testComplete() {
		loan.complete();
	}
	
	//Method that test invalid Constructor
	@Test
	public void testIsOverDueNotOverDue() {
		assertFalse(loan.isOverDue());
	}
	
	//Method that test Check Over Due Over Due
	@Test
	public void testCheckOverDueOverDue() {
		loan.commit(loanId);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, ILoan.LOAN_PERIOD + 1);
		Date overDueDate = calendar.getTime();
		assertTrue(loan.checkOverDue(overDueDate));
	}
	
	//Method that test invalid Constructor
	@Test(expected=RuntimeException.class)
	public void testCheckOverDueLoanPending() {
		loan.checkOverDue(Calendar.getInstance().getTime());
	}
	
	//Method that test Is Over Due Over Due
	@Test
	public void testIsOverDueOverDue() {
		loan.commit(loanId);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, ILoan.LOAN_PERIOD + 1);
		Date overDueDate = calendar.getTime();
		assertTrue(loan.checkOverDue(overDueDate));
		assertTrue(loan.isOverDue());
	}
	
	//Method that test invalid Constructor
	@Test
	public void testCheckOverDueNotOverDue() {
		loan.commit(loanId);
		assertFalse(loan.checkOverDue(Calendar.getInstance().getTime()));
	}
	
	//Method that test GEt Borrower
	@Test
	public void testGetBorrower() {
		IMember actual = loan.getBorrower();
		assertEquals(member, actual);
	}
	
	//Method that test Get Book 
	@Test
	public void testGetBook() {
		IBook actual = loan.getBook();
		assertEquals(book, actual);
	}
	
	//Method that test Get ID
	@Test
	public void testGetID() {
		int mockBookId = 1;
		expect(book.getID()).andReturn(mockBookId);
		replay(book);
		int actual = loan.getID();
		verify(book);
		assertEquals(mockBookId, actual);
	}

}