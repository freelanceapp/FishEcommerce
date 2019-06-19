
package com.teocfish.teoc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelProductList {
//"productId": "2202",
//                "productName": "Mud Crab",
//                "currency": "INR          ",
//                "sellprice": "750",
//                "mrp": "770",
//                "size": "",
//                "quantity": "25",
//                "status": "Instock",
//                "description": "\t\t\t\t\t\t\t\t\t<p>From the exotics of chilika</p>\t\t\t\t\t\t\t\t\t\r\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
//                "color": "",
//                "images": [
//                    "https://teocfish.com/adminpanel/uploads/products/1528792531-mud-crab.png"
//                ]
    private String success;
    private String productId;
    private String iteam_id;
    private String plimit;
    private String orderstatus;
    private String productName;
    private String mrp;
    private String sellprice;
    private String size;
    private String status;
    private String color;
    private String currency;
    private String quantity;
    private String stock;
    private String description;
    private List<String> images = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();



    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getPlimit() {
        return plimit;
    }

    public void setPlimit(String plimit) {
        this.plimit = plimit;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getIteam_id() {
        return iteam_id;
    }

    public void setItemId(String iteam_id) {
        this.iteam_id = iteam_id;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductColor() {
        return color;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMrpprice() {
        return mrp;
    }

    public void setMrpprice(String mrp) {
        this.mrp = mrp;
    }

    public String getSellprice() {
        return sellprice;
    }

    public void setSellprice(String sellprice) {
        this.sellprice = sellprice;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status)

    {
        this.status = status;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

//    @SerializedName("productId")
//@Expose
//private String productId;
//@SerializedName("productName")
//@Expose
//private String productName;
//@SerializedName("currency")
//@Expose
//private String currency;
//@SerializedName("sellprice")
//@Expose
//private String sellprice;
//@SerializedName("mrp")
//@Expose
//private String mrp;
//@SerializedName("size")
//@Expose
//private String size;
//@SerializedName("quantity")
//@Expose
//private String quantity;
//@SerializedName("status")
//@Expose
//private String status;
//@SerializedName("description")
//@Expose
//private String description;
//@SerializedName("color")
//@Expose
//private String color;
//@SerializedName("plimit")
//@Expose
//private String plimit;
//@SerializedName("plimit_unit")
//@Expose
//private String plimitUnit;
//@SerializedName("count_unit")
//@Expose
//private String countUnit;
//@SerializedName("images")
//@Expose
//private List<String> images = null;
//
//public String getProductId() {
//return productId;
//}
//
//public void setProductId(String productId) {
//this.productId = productId;
//}
//
//public String getProductName() {
//return productName;
//}
//
//public void setProductName(String productName) {
//this.productName = productName;
//}
//
//public String getCurrency() {
//return currency;
//}
//
//public void setCurrency(String currency) {
//this.currency = currency;
//}
//
//public String getSellprice() {
//return sellprice;
//}
//
//public void setSellprice(String sellprice) {
//this.sellprice = sellprice;
//}
//
//public String getMrp() {
//return mrp;
//}
//
//public void setMrp(String mrp) {
//this.mrp = mrp;
//}
//
//public String getSize() {
//return size;
//}
//
//public void setSize(String size) {
//this.size = size;
//}
//
//public String getQuantity() {
//return quantity;
//}
//
//public void setQuantity(String quantity) {
//this.quantity = quantity;
//}
//
//public String getStatus() {
//return status;
//}
//
//public void setStatus(String status) {
//this.status = status;
//}
//
//public String getDescription() {
//return description;
//}
//
//public void setDescription(String description) {
//this.description = description;
//}
//
//public String getColor() {
//return color;
//}
//
//public void setColor(String color) {
//this.color = color;
//}
//
//public String getPlimit() {
//return plimit;
//}
//
//public void setPlimit(String plimit) {
//this.plimit = plimit;
//}
//
//public String getPlimitUnit() {
//return plimitUnit;
//}
//
//public void setPlimitUnit(String plimitUnit) {
//this.plimitUnit = plimitUnit;
//}
//
//public String getCountUnit() {
//return countUnit;
//}
//
//public void setCountUnit(String countUnit) {
//this.countUnit = countUnit;
//}
//
//public List<String> getImages() {
//return images;
//}
//
//public void setImages(List<String> images) {
//this.images = images;
//}

}