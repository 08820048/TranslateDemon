package cn.ilikexff.translatedemon;
import cn.ilikexff.translatedemon.utils.*;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * �����е����Ʒ������api����demo
 * api�ӿ�: https://openapi.youdao.com/api
 */
public class TranslateDemo {

    private static final String APP_KEY = "4437c50c70c33777";     // ����Ӧ��ID
    private static final String APP_SECRET = "N6Q2iXKfsR5n5vwG19jjcRzmjo3U9ScB";  // ����Ӧ����Կ

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // ����������
        Map<String, String[]> params = createRequestParams();
        // ��Ӽ�Ȩ��ز���
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // ����api����
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/api", null, params, "application/json");
        // ��ӡ���ؽ��
        if (result != null) {
            System.out.println(new String(result, StandardCharsets.UTF_8));
            String jsonStr = new String(result, StandardCharsets.UTF_8);
            JSONObject jsonObject = (JSONObject) JSON.parse(jsonStr);
            JSONArray jsonArray = (JSONArray) jsonObject.get("translation");
            String translateResult = jsonArray.get(0).toString();
            System.out.println(translateResult);
        }
        System.exit(0);
    }

    private static Map<String, String[]> createRequestParams() {
        /*
         * note: �����б����滻Ϊ��Ҫ����Ĳ���
         * ȡֵ�ο��ĵ�: https://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html
         */
        String q = "this is text"; // ������ı�
        String from = "en"; // Դ����
        String to = "zh-CHS"; // ��������
        //String vocabId = "�����û��ʱ�ID";

        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("from", new String[]{from});
            put("to", new String[]{to});
           // put("vocabId", new String[]{vocabId});
        }};
    }
}
