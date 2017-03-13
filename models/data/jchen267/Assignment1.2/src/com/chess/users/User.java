package com.chess.users;

/**
 * Created by jiayu on 2/5/2017.
 */
public class User {
    private String userName;

    /**
     * Default constructor
     */
    public User() {
        this.userName = "UnNamed User";
    }

    /**
     * Constructor that sets the user to have the provided username
     *
     * @param userName
     */
    public User(String userName) {
        this.userName = userName;
    }

    /**
     * Get the username of the user
     *
     * @return username of the user
     */
    public String getName() {
        return this.userName;
    }

    /**
     * Set the username for the user
     *
     * @param userName username to set for the user
     */
    public void setName(String userName) {
        this.userName = userName;
    }

}
