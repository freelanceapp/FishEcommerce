package com.teocfish.teoc;

import java.util.List;

public class WishlistResponse {

    private String success;
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ModelProductList> getProducts() {
        return modelProductList;
    }

    public void setProducts(List<ModelProductList> modelProductLists) {
        this.modelProductList = modelProductLists;
    }

    private List<ModelProductList> modelProductList = null;

}