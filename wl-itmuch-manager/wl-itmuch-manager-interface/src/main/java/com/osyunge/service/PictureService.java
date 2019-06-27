package com.osyunge.service;

import com.osyunge.dataobject.PictureResult;
import org.springframework.web.multipart.MultipartFile;

public interface PictureService {

    PictureResult uploadPic(MultipartFile picFile);

}
