package com.osyunge.pojo;

import lombok.Data;

@Data
public class Item {

    private String id;
    private String title;
    private String sell_point;
    private long price;
    private String image;
    private String category_name;
    private String item_desc;

    private String[] images;

    public String[] getImages() {

        if (image != null){
            images = image.split(",");
            return images;
        }

        return null;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
