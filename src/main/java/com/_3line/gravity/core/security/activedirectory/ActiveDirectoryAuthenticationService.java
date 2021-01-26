package com._3line.gravity.core.security.activedirectory;

/**
 * @author FortunatusE
 * @date 11/19/2018
 */
public interface ActiveDirectoryAuthenticationService {


    boolean authenticate(String username, String password, String token);

}
