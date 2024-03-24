package cn.ilikexff.translatedemon;

import cn.ilikexff.translatedemon.utils.AuthV3Util;
import cn.ilikexff.translatedemon.utils.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.Messages;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class TranslateDemonAction extends AnAction {

    /**
     * This method is triggered when the TranslateDemon plugin is activated.
     * It retrieves the selected text in the editor, translates it using a third-party translation API, and displays the translated text in a pop-up window.
     * @param e the AnActionEvent object that contains information about the context in which the action was triggered
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        // e.�û������¼������飬������������Ϣ

        // ���������֮���ȡ�û�ѡ�е�����
        String selectedText = e.getDataContext().getData(PlatformDataKeys.EDITOR).getCaretModel().getCurrentCaret().getSelectedText();
        // ʹ�õ���������API��ȡ����
        try {
            String res = translate(selectedText);
            Messages.showMessageDialog(res,"������:", Messages.getInformationIcon());
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public  String translate(String text) throws NoSuchAlgorithmException {
        final String APP_KEY = "4437c50c70c33777";     // ����Ӧ��ID
        final String APP_SECRET = "N6Q2iXKfsR5n5vwG19jjcRzmjo3U9ScB";  // ����Ӧ����Կ
        // ����������
        Map<String, String[]> params = createRequestParams(text);
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
            //System.out.println(translateResult);
            return translateResult;

        }
        return "Ŷ��!���������,�ѿ���������������!";
        //System.exit(0);
    }

    private static Map<String, String[]> createRequestParams(String q) {
        /*
         * note: �����б����滻Ϊ��Ҫ����Ĳ���
         * ȡֵ�ο��ĵ�: https://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html
         */
        //String q = "this is text"; // ������ı�
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
