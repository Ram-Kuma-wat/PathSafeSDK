package com.cwlib.pathsafe.rest;


interface DataResponse<T> {

      void onSuccess(T response);

      void onFaliure(String error);
}
