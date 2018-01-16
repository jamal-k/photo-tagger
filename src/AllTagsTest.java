import java.io.IOException;

// import org.junit.*;

public class AllTagsTest {
	
	
	/*
	 * Tests that AllTags serializes correctly.
	 */
	// @Test
	public void SerializeTest() throws IOException, ClassNotFoundException {
		AllTags.getInstance().addTag("lol");
		AllTags.saveToFile();
		AllTags.readFromFile();
		// assertTrue(AllTags.getInstance().getTags().contains("lol"));
	}
	
	/*
	 * Test that AllTags adds tags correctly.
	 */
	// @Test
	public void AddTagTest() throws IOException {
		AllTags.getInstance().addTag("abc");
		// assertTrue(AllTags.getInstance().getTags().contains("abc"));
	}

}
