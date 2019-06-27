package com.osyunge.search.listener;

import com.osyunge.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ItemChangeListener implements MessageListener {

    @Autowired
    private SearchService searchService;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage;
            String messageText = null;
            Long itemId ;
            //取商品id
            if (message instanceof TextMessage) {
                textMessage = (TextMessage) message;
                messageText = textMessage.getText();
            }

            if (messageText!=null && messageText.endsWith("add")){
                itemId = Long.valueOf(messageText.substring(0,messageText.lastIndexOf("#")));
                //向索引库添加文档
                searchService.updateItemById(itemId);
            }
            if (messageText!=null && messageText.endsWith("update")){
                itemId = Long.valueOf(messageText.substring(0,messageText.lastIndexOf("#")));

                searchService.deleteItemById(itemId);
                searchService.updateItemById(itemId);
            }

            if (messageText!=null && messageText.endsWith("delete")){
                String str = messageText.substring(0,messageText.lastIndexOf("#"));

                String s = str.substring(1,str.length()-1);

                String[] ids = s.split(",");

                for (String id : ids) {
                    searchService.deleteItemById(Long.valueOf(id));
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
