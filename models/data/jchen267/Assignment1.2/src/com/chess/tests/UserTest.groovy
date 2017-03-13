package com.chess.tests

import com.chess.users.User
import org.junit.Before
import org.junit.Test

/**
 * Created by jiayu on 2/6/2017.
 */
class UserTest extends GroovyTestCase {
    private User user;

    @Before
    /**
     * set up before the test
     */
    void setUp() {
        super.setUp()
        this.user = new User()
    }

    @Test
    /**
     * test GetName works correctly
     */
    void testGetName() {
        assertEquals("UnNamed User", this.user.getName())
    }

    @Test
    /**
     * test SetName works correctly
     */
    void testSetName() {
        this.user.setName("testing")
        assertEquals("testing", this.user.getName())
    }

}
