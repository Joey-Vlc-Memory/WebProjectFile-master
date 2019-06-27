package com.osyunge.pojo;

import com.osyunge.dataobject.TbItem;

public class ItemPlus extends TbItem {

    public String[] getImages() {

        if (getImage() != null){
            return getImage().split(",");
        }

        return null;
    }

}
