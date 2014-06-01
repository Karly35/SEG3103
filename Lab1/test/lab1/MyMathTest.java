/**
 * 
 */
package lab1;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author samira
 *
 */
public class MyMathTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println( "@BeforeClass" );

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println( "@AfterClass" );

	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println( "@Before" );

		MyMath myMath = new MyMath();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println( "@After" );

	}

	/**
	 * Test method for {@link lab1.MyMath#div(int, int)}.
	 */
	@Test
	public void testDivPass() {
		System.out.println("@Test: testDivPass");

		assertThat(MyMath.div(6, 3), is(2));
	}
	
	/**
	 * Test method for {@link lab1.MyMath#div(int, int)}.
	 */
	@Test
	public void testDivFail() {
		System.out.println("@Test: testDivFail");

		assertThat(MyMath.div(6, 3), is(1));
	}
	
	/**
	 * Test method for {@link lab1.MyMath#div(int, int)}.
	 */
	@Test
	public void testDivError() {
		System.out.println("@Test: testDivError");

		assertThat(MyMath.div(6, 0), is(0));
	}
	
	/**
	 * Test method for {@link lab1.MyMath#div(int, int)}.
	 */
	@Test (expected = ArithmeticException.class)
	public void testDivException() {
		System.out.println("@Test: testDivException");

		assertThat(MyMath.div(6, 0), is(0));
	}

}
