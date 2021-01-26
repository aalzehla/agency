/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.gravitymobile.oauth.model;

/**
 *
 * @author OlalekanW
 */
public class AgentsDetails {
   private String username,userId,clientId;
   private String belongsTo;

    public AgentsDetails(String username, String userId, String clientId , String belongsto ) {
        this.username =username;this.userId=userId;this.clientId=clientId;this.belongsTo = belongsto; 
                                                                                              }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the belongsTo
     */
    public String getBelongsTo() {
        return belongsTo;
    }

    /**
     * @param belongsTo the belongsTo to set
     */
    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

}
