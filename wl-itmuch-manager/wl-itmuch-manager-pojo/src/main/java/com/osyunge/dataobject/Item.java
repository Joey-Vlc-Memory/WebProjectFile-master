package com.osyunge.dataobject;

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


}
