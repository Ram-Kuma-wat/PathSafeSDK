package com.cwlib.pathsafe.rest;


public interface OnResponse<T> {
    void onSuccess(T response);
    void onError(String type,String error);
}
