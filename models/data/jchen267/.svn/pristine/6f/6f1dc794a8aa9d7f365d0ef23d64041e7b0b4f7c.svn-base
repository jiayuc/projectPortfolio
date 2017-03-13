package com.chess.users

/**
 * Created by jiayu on 2/6/2017.
 */
class UserTest extends GroovyTestCase {
    private User user;
    void setUp() {
        super.setUp();
        this.user = new User();
    }

    void testGetName() {
        assertEquals("UnNamed User", this.user.getName());
    }

    void testSetName() {
        this.user.setName("testing");
        assertEquals("testing", this.user.getName());
    }

    void testGetTurn() {
        assertEquals(true, this.user.getTurn());
    }

    void testSetTurn() {
        this.user.setTurn(false);
        assertEquals(false, this.user.getTurn());
        this.user.setTurn(true);
        assertEquals(true, this.user.getTurn());
    }
}
