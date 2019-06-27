package com.osyunge.service;

import com.osyunge.dataobject.PictureResult;
import com.osyunge.utils.FastDFSClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public PictureResult uploadPic(MultipartFile picFile) {

        PictureResult result = new PictureResult();

        //如果上传的图片为空
        if (picFile == null){
            result.setError(1);
            result.setMessage("图片为空！");
            return result;
        }

        //不为空则上传至服务器

        try {
            //取文件后缀名
            String originalFilename = picFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            FastDFSClient fastDFSClient = new FastDFSClient("classpath:client.conf");
            String url = fastDFSClient.uploadFile(picFile.getBytes(),extName);

            //把url响应给客户端
            result.setError(0);
            result.setUrl("http://47.103.16.213/"+url);

        }catch (Exception e){
            e.printStackTrace();
            result.setError(1);
            result.setMessage("图片上传失败");
        }

        return result;
    }
}
