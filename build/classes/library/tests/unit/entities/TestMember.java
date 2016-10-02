package library.tests.unit.entities;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import library.entities.Member;
import library.interfaces.entities.*;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMember {
	
	String fName = "FName";
	String lName = "lName";
	String phone = "phone";
	String email = "email";
	int id = 1;

	Member member;
	
	//The @Before method will be run before and after every test case, 
	//so will probably be run multiple times during a test run.
	@Before
	public void setUp() throws Exception {
		member = new Member(fName, lName, phone, email, id);
	}

	//The @After method will be run before and after every test case, 
	//so will probably be run multiple times during a test run.
	@After
	public void tearDown() throws Exception {
		member = null;
	}
	
	//test valid Constructor
	@Test
	public void testValidConstructor() {
		Member testMember = new Member(fName, lName, phone, email, id);
		assertNotNull(testMember);
		assertTrue(testMember instanceof IMember);
	}
	

	//Method that test invalid Constructor
	@Test(expected=RuntimeException.class)
	public void testInvalidConstructor() {
		new Member(fName, lName, null, email, id);
	}	

	//Method that Test Has Over Due Loans
	@Test
	public final void testHasOverDueLoans() {
		ILoan mockLoan = createMock(ILoan.class);
		expect(mockLoan.isOverDue()).andReturn(true);
		replay(mockLoan);
		member.addLoan(mockLoan);
		assertTrue(member.hasOverDueLoans());
		verify(mockLoan);
	}

	//Method that Test has Reached Loan Limit
	@Test
	public final void testHasReachedLoanLimit() {
		for (int i =0; i < Member.LOAN_LIMIT; i ++) {
			member.addLoan(createMock(ILoan.class));
		}
		assertTrue(member.getLoans().size() == Member.LOAN_LIMIT);
		assertTrue(member.hasReachedLoanLimit());
		assertTrue(member.getState() == EMemberState.BORROWING_DISALLOWED);
	}

	//Method that Test Has Fines Payable
	@Test
	public final void testHasFinesPayable() {
		member.addFine(10);
		assertTrue(member.hasFinesPayable());
	}

	//Method that Test HAs Reached Fine Limit
	@Test
	public final void testHasReachedFineLimit() {
		member.addFine(Member.FINE_LIMIT + 1);
		assertTrue(member.hasReachedFineLimit());
		assertTrue(member.getState() == EMemberState.BORROWING_DISALLOWED);
	}

	//Method that Test Get Fine Amount
	@Test
	public final void testGetFineAmount() {
		float fineAmount = 0;
		float actual = member.getFineAmount();
		assertEquals(fineAmount, actual, 0);
	}
	
	//Method that Test Add Fine
	@Test
	public final void testAddFine() {
		float fineAmount = 10;
		member.addFine(fineAmount);
		float actual = member.getFineAmount();
		assertEquals(fineAmount, actual, 0);
	}

	//Method that Test Pay Fine
	@Test
	public final void testPayFine() {
		float fineAmount = 10;
		member.addFine(fineAmount);
		float actual = member.getFineAmount();
		assertEquals(fineAmount, actual, 0);
		member.payFine(fineAmount);
		actual = member.getFineAmount();
		assertEquals(0, actual, 0);
	}

	//Method that Test Add Loan
	@Test
	public final void testAddLoan() {
		ILoan mockLoan = createMock(ILoan.class);
		member.addLoan(mockLoan);
		List<ILoan> loans = member.getLoans();
		assertNotNull(loans);
		assertTrue(loans.contains(mockLoan));
	}

	//Method that Test Get Loans
	@Test
	public final void testGetLoans() {
		List<ILoan> actual = member.getLoans();
		assertNotNull(actual);
		assertTrue(actual.size() == 0);
	}

	//Method that Test Remove Loan 
	@Test
	public final void testRemoveLoan() {
		ILoan mockLoan = createMock(ILoan.class);
		member.addLoan(mockLoan);
		List<ILoan> loans = member.getLoans();
		assertNotNull(loans);
		assertTrue(loans.contains(mockLoan));
		member.removeLoan(mockLoan);
		assertFalse(member.getLoans().contains(mockLoan));
	}

	//Method that Test Get State
	@Test
	public final void testGetState() {
		EMemberState actual = member.getState();
		assertEquals(EMemberState.BORROWING_ALLOWED, actual);
	}

	//Method that Test Get First Name 
	@Test
	public final void testGetFirstName() {
		String actual = member.getFirstName();
		assertEquals(fName, actual);
	}

	//Method that Test Get Last Name 
	@Test
	public final void testGetLastName() {
		String actual = member.getLastName();
		assertEquals(lName, actual);	}

	//Method that Test Get Contact Phone
	@Test
	public final void testGetContactPhone() {
		String actual = member.getContactPhone();
		assertEquals(phone, actual);
	}

	//Method that Test Get Email Address
	@Test
	public final void testGetEmailAddress() {
		String actual = member.getEmailAddress();
		assertEquals(email, actual);
	}

	//Method that Test Get ID
	@Test
	public final void testGetID() {
		int actual = member.getID();
		assertEquals(id, actual);
	}
}
