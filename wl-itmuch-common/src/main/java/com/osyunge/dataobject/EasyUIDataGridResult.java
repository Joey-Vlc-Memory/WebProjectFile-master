package com.osyunge.dataobject;

import lombok.Data;

import java.util.List;

@Data
public class EasyUIDataGridResult {

    private long total;
    private List<?> rows;

}
