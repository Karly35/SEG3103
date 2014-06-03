package validator;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TidyISBNValidateTest {

  private static final HashMap<String, String> ValidISBN13Dashes =
      new HashMap<String, String>() {{
        put("978-0-300-14424-6", "978-0-300-14424-6");
        put("978-1-41-330454-1", "978-1-4133-0454-1");
        put("978-3-16-148410-0", "978-3-16148410-0");
      }};

  private static final HashMap<String, String> ValidISBN13Dashless =
      new HashMap<String, String>() {{
        put("9780300144246", "978-0-300-14424-6");
        put("9781413304541", "978-1-4133-0454-1");
        put("9783161484100", "978-3-16148410-0");
      }};

  private static final HashMap<String, String> ValidISBN10Dashes =
      new HashMap<String, String>() {{
        put("1-4133-0454-0", "1-4133-0454-0");
        put("0-14-020652-3", "0-14-020652-3");
        put("9971-5-0210-0", "9971-50210-0");
        put("99921-58-10-7", "99921-5810-7");
        put("0-8044-2957-X", "0-8044-2957-X");
        put("0-9752298-0-X", "0-9752298-0-X");
      }};

  private static final HashMap<String, String> ValidISBN10Dashless =
      new HashMap<String, String>() {{

        put("1413304540", "1-4133-0454-0");
        put("0140206523", "0-14-020652-3");
        put("9971502100", "9971-50210-0");
        put("9992158107", "99921-5810-7");
        put("080442957X", "0-8044-2957-X");
        put("097522980X", "0-9752298-0-X");
      }};

  private static class TestFrame {

    Integer                    frameNumber;
    String                     category;
    String                     description;
    HashMap<String, String>    input;
    Class<? extends Exception> wantError;

    TestFrame(int frameNumber,
              String category,
              HashMap<String, String> want) {
      this.frameNumber = frameNumber;
      this.category = category;
      this.input = want;
      this.wantError = null;
    }

    TestFrame(int frameNumber,
              String category,
              Class<? extends Exception> error,
              String[] input) {
      this.frameNumber = frameNumber;
      this.category = category;
      this.wantError = error;
      this.input = new HashMap<String, String>();
      for (String s : input) {
        this.input.put(s, "should not show up");
      }
    }
  }

  // Table driven testing for the win. Writing unit-tests as methods is lame!

  private final static TestFrame[] frames = new TestFrame[]{
      new TestFrame(1, "A1B1C5D2E1G2H1J1", ValidISBN10Dashes) {{
        description = "A set of valid ISBN10 containing dashes.";
      }},
      new TestFrame(2, "A1B2C5D2E1G2H1J1", ValidISBN10Dashless) {{
        description = "A set of valid ISBN10 containing NO dashes.";
      }},
      new TestFrame(3, "A2B1C5D3F1G2H1I1", ValidISBN13Dashes) {{
        description = "A set of valid ISBN13 containing dashes.";
      }},
      new TestFrame(4, "A2B2C5D3F1G2H1I1", ValidISBN13Dashless) {{
        description = "A set of valid ISBN13 containing NO dashes.";
      }},

      new TestFrame(5, "A3BxC1DxExFxGxHxIxJx", IllegalArgumentException.class,
                    new String[]{
                        null
                    }) {{
        description = "Strings that are null.";
      }},

      new TestFrame(6, "A3BxC2DxExFxGxHxIxJx", IllegalArgumentException.class,
                    new String[]{
                        "", "0", "01", "0123", "01234", "012345", "0123456", "01234567",
                        "012345678",
                        // skipping length 10
                        "01234567890", "012345678901",
                        // skipping length 13
                        "01234567890123",
                        "012345678901234",
                        "0123456789999999999999999999999999", // one that's long
                    }) {{
        description = "Strings of the wrong length.";
      }},

      new TestFrame(7, "A3BxCxD1ExFxGxHxIxJx", IllegalArgumentException.class,
                    new String[]{
                        // length 10 strings
                        "0---------",
                        "01--------",
                        "012-------",
                        "0123------",
                        "01234-----",
                        "012345----",
                        "0123456---",
                        "01234567--",
                        "012345678-",
                        // length 13 strings
                        "0------------",
                        "01-----------",
                        "012----------",
                        "0123---------",
                        "01234--------",
                        "012345-------",
                        "0123456------",
                        "01234567-----",
                        "012345678----",
                        // skip length 10
                        "01234567890--",
                        "012345678901-",
                        "0123456789012",
                    }) {{
        description = "Strings of wrong length when removing dashes.";
      }},

      new TestFrame(8, "A3BxCxDxE2FxGxHxIxJx", IllegalArgumentException.class,
                    new String[]{
                        // otherwise valid with an end in X
                        "080442957",
                        "097522980"
                    }) {{
        description = "Strings that should finish with an X.";
      }},

      new TestFrame(9, "A3BxCxDxExF2GxHxIxJx", IllegalArgumentException.class,
                    new String[]{
                        "977-0-300-14424-6",
                        "980-1-41-330454-1",
                        "999-3-16-148410-0",
                    }) {{
        description = "ISBN13 not beginning with a 978 or 979";
      }},

      new TestFrame(10, "A3BxCxDxExFxG1HxIxJx", IllegalArgumentException.class,
                    new String[]{
                        // letters aren't valid groups
                        "a-8044-2957-X",
                        "b-9752298-0-X",
                    }) {{
        description = "Valid groups must be made of digits.";
      }},

      new TestFrame(11, "A3BxCxDxExFxG3HxIxJx", IllegalArgumentException.class,
                    new String[]{
                        // 8, 9 and 10 are not valid groups
                        "978-8-300-14424-6",
                        "978-9-41-330454-1",
                        "978-10-16-148410-0",

                        "8-4133-0454-0",
                        "9-14-020652-3",
                        // 9999, 99999 are not valid
                        "9999-5-0210-0",
                        "99999-58-10-7",
                    }) {{
        description = "Some integers don't form valid groups.";
      }},

      new TestFrame(12, "A3BxCxDxExFxGxH2IxJx", IllegalArgumentException.class,
                    new String[]{
                        "978-a-300-14424-6",
                        "978-b-41-330454-1",
                        "978-c-16-148410-0",
                        "1-aaaa-0454-0",
                        "0-bf-020652-3",
                        "9971-o-0210-0",
                        "99921-++-10-7",
                        "0-µ∑ƒ√-2957-X",
                        "0-$%$∂ƒ∂å-0-X",
                    }) {{
        description = "Only digits can form a publisher group.";
      }},

      new TestFrame(13, "A3BxCxD2ExFxGxHxI2Jx", IllegalArgumentException.class,
                    new String[]{
                        "978-0-300-14424-3", // sum should be 6
                        "978-1-41-330454-0", // sum should be 1
                        "978-3-16-148410-1", // sum should be 0
                    }) {{
        description = "Invalid checksum for ISBN13.";
      }},

      new TestFrame(14, "A3BxCxD3ExFxGxHxIxJ2", IllegalArgumentException.class,
                    new String[]{
                        "1-4133-0454-1", // sum should be 0
                        "0-14-020652-8", // sum should be 3
                        "9971-5-0210-9", // sum should be 0
                        "99921-58-10-2", // sum should be 7
                    }) {{
        description = "Invalid checksum for ISBN10.";
      }},
  };

  @Test
  public void testDriver_TidyISBN10or13InsertingDashes() throws Exception {

    for (TestFrame tf : frames) {
      for (Map.Entry<String, String> pair : tf.input.entrySet()) {
        String input = pair.getKey();
        String want = pair.getValue();
        String prf = tf.frameNumber + ":" + tf.category + "> `" + input + "`\n" +
                     "\nDescription: " + tf.description + "\n";
        try {
          String got = ISBNValidate.tidyISBN10or13InsertingDashes(input);
          Assert.assertTrue(prf + "Did not get an error, but wanted one.",
                            tf.wantError == null);
          Assert.assertEquals(prf + "Should return proper tidy'd ISBN string.",
                              want,
                              got);

        } catch (Exception e) {
          Assert.assertNotNull(prf + "Did not want an error, but got one.",
                               tf.wantError);
          Assert.assertEquals(prf + "Should have caught exception of desired class.",
                              tf.wantError.getName(),
                              e.getClass().getName());
        }
      }
    }
  }
}
