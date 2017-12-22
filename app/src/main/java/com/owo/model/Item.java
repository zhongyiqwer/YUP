package com.owo.model;

/**
 * 道具
 */
public class Item {
    private int id;
    private String itemName;
    private String itemFunction;
    private int itemPrice;
    private String itemImage;
    private int itemLevel;
    private int itemPrivilege;
    private int itemType;
    private int itemCount;

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemFunction() {
        return itemFunction;
    }

    public void setItemFunction(String itemFunction) {
        this.itemFunction = itemFunction;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    public int getItemPrivilege() {
        return itemPrivilege;
    }

    public void setItemPrivilege(int itemPrivilege) {
        this.itemPrivilege = itemPrivilege;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", itemFunction='" + itemFunction + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemImage='" + itemImage + '\'' +
                ", itemLevel=" + itemLevel +
                ", itemPrivilege=" + itemPrivilege +
                ", itemType=" + itemType +
                ", itemCount=" + itemCount +
                '}';
    }
}
