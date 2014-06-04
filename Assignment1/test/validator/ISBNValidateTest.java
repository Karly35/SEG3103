package validator;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for SEG3101 Assignment 1, Question 1.3
 * @author Samira El-Rayyes 6439366
 * @author Antoine Grondin 6276497
 */
public class ISBNValidateTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/*
	 * Test case 01
	 */
	@Test(expected = IllegalArgumentException.class)
	public void appendCheckDigitToISBN12WithShortStringTest() {
		ISBNValidate.appendCheckDigitToISBN12("12345678900");
	}

	/*
	 * Test case 02
	 */
	@Test(expected = IllegalArgumentException.class)
	public void appendCheckDigitToISBN12WithLongStringTest() {
		ISBNValidate.appendCheckDigitToISBN12("1234567890876");
	}

	/*
	 * This test fails even though it is expected to pass,
	 * because the IllegalArgumentException is not thrown
	 * as it should. The appendCheckDigitToISBN12 method
	 * is written not to check for non-digits in the input,
	 * therefore this test currently fails.
	 * 
	 * Test case 03
	 */
	@Test(expected = IllegalArgumentException.class)
	public void appendCheckDigitToISBN12WithInvalidStringTest() {
		ISBNValidate.appendCheckDigitToISBN12("1234567890ab");
	}

	/*
	 * Test case 04
	 */
	@Test
	public void appendCheckDigitToISBN12Test() {
		assertThat(ISBNValidate.appendCheckDigitToISBN12("123456789012"), CoreMatchers.is("1234567890128"));
	}

}
