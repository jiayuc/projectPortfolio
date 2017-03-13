package com.chess.tests

import com.chess.users.User

/**
 * Created by jiayu on 2/6/2017.
 */
class UserTest extends GroovyTestCase {
    private User user
    void setUp() {
        super.setUp()
        this.user = new User()
    }

    void testGetName() {
        assertEquals("UnNamed User", this.user.getName())
    }

    void testSetName() {
        this.user.setName("testing")
        assertEquals("testing", this.user.getName())
    }

}
