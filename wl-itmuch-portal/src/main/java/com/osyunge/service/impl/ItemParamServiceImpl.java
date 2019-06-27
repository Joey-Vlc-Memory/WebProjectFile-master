package com.osyunge.service.impl;

import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbItemParamItem;
import com.osyunge.service.ItemParamService;
import com.osyunge.utils.HttpClientUtil;
import com.osyunge.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;

    @Value("${REST_ITEM_PARAM}")
    private String REST_ITEM_PARAM;

    @Override
    public String getItemParam(Long itemId) {

        try {
            String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_PARAM + itemId);

            //把json转换成java对象
            FCResult fcResult = FCResult.formatToPojo(json, TbItemParamItem.class);
            if (fcResult.getStatus() == 200) {
                TbItemParamItem itemParamItem = (TbItemParamItem) fcResult.getData();
                String paramData = itemParamItem.getParamData();
                //生成html
                // 把规格参数json数据转换成java对象
                List<Map> jsonList = JsonUtils.jsonToList(paramData, Map.class);
                StringBuffer sb = new StringBuffer();
                sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\">\n");
                sb.append("    <tbody>\n");
                for (Map m1 : jsonList) {
                    sb.append("        <tr>\n");
                    sb.append("            <th class=\"tdTitle\" colspan=\"2\">" + m1.get("group") + "</th>\n");
                    sb.append("        </tr>\n");
                    List<Map> list2 = (List<Map>) m1.get("params");
                    for (Map m2 : list2) {
                        sb.append("        <tr>\n");
                        sb.append("            <td class=\"tdTitle\">" + m2.get("k") + "</td>\n");
                        sb.append("            <td>" + m2.get("v") + "</td>\n");
                        sb.append("        </tr>\n");
                    }
                }
                sb.append("    </tbody>\n");
                sb.append("</table>");
                //返回html片段
                return sb.toString();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
