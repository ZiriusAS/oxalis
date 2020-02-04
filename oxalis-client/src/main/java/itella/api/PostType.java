/*
 * @(#)PostType.java
 *
 * Copyright (c) 2013, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package itella.api;

/**
 * The Enum PostType.
 * 
 * @author vasanthis
 */
public enum PostType {

    A_POST("A-post"),

    B_POST("B-post");

    private String postType;

    /**
     * Instantiates a new post type.
     * 
     * @param postType
     *            the post type
     */
    private PostType(String postType) {
        this.postType = postType;
    }

    /**
     * Gets the post type.
     * 
     * @return the post type
     */
    public String getPostType() {
        return postType;
    }
}
