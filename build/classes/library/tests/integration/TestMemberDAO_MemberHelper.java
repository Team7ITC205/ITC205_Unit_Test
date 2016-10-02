package library.tests.integration;

import static org.junit.Assert.*;

import java.util.List;

import library.daos.*;
import library.interfaces.daos.*;
import library.interfaces.entities.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMemberDAO_MemberHelper {

	private String fName = "fName";
	private String lName = "lName";
	private String contactPhone = "contactPhone";
	private String email = "email";
	
	private IMemberHelper memberHelper;
	private IMemberDAO memberDao;
	
	@Before
	public void setUp() throws Exception {
		memberHelper = new MemberHelper();
	}

	@After
	public void tearDown() throws Exception {
		memberHelper = null;
		memberDao = null;
	}

	@Test
	public void testAddMember() {
		IMember member = memberDao.addMember(fName, lName, contactPhone, email);

		assertNotNull(member);
		assertEquals(fName, member.getFirstName());
	}
	
	@Test
	public void testGetMemberByID() {
		int id = 1;
		IMember member = memberDao.addMember(fName, lName, contactPhone, email);
 		IMember actual = memberDao.getMemberByID(id);
 		assertEquals(member, actual);
	}
	
	@Test
	public void testFindMembersByLastName() {
		IMember member = memberDao.addMember(fName, lName, contactPhone, email);
 		List<IMember> actual = memberDao.findMembersByLastName(lName);
 		assertNotNull(actual);
 		assertTrue(actual instanceof List);
 		assertTrue(actual.contains(member));
	}

	@Test
	public void testFindMembersByEmailAddress() {
		IMember member = memberDao.addMember(fName, lName, contactPhone, email);
 		List<IMember> actual = memberDao.findMembersByEmailAddress(email);
 		assertNotNull(actual);
 		assertTrue(actual instanceof List);
 		assertTrue(actual.contains(member));
	}
	
	@Test
	public void testFindMembersByNames() {
		IMember member = memberDao.addMember(fName, lName, contactPhone, email);
 		List<IMember> actual = memberDao.findMembersByNames(fName, lName);
 		assertNotNull(actual);
 		assertTrue(actual instanceof List);
 		assertTrue(actual.contains(member));
	}
	
}
