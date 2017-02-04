package com.vk.tottenham.exception;

public class VkException extends RuntimeException {

    private static final long serialVersionUID = -358401307376713431L;

    public VkException(String message, Throwable cause) {
        super(message, cause);
    }

    public VkException(String message) {
        super(message);
    }

}