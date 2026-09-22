package fr.ul.acl.tests; 
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import fr.ul.acl.model.Greeter; 

public class GreeterTest { 
  @Test 
  public void testGreet() throws Exception { 
    Greeter greeter = new Greeter(); 
    assertEquals("Hello, World!", greeter.greet("World")); 
  } 
}