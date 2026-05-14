package com.example.cloud_kitchen.Api;

public class ApiException extends RuntimeException {
    public  ApiException(String message){
        super(message);
    }
}
