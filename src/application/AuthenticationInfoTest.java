package application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AuthenticationInfoTest {

    private AuthenticationInfo authInfo = new AuthenticationInfo("student", "12345");

    @Test
    void testGetUsername() {
        assertEquals("student", authInfo.getUsername());
    }

    @Test
    void testSetUsername() {
        authInfo.setUsername("newstudent");
        assertEquals("newstudent", authInfo.getUsername());
    }

    @Test
    void testGetPassword() {
        assertEquals("12345", authInfo.getPassword());
    }

    @Test
    void testSetPassword() {
        authInfo.setPassword("67890");
        assertEquals("67890", authInfo.getPassword());
    }
}
