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
        // e.用户触发事件的详情，包含了所有信息
        // 插件被触发之后获取用户选中的内容
        String selectedText = e.getDataContext().getData(PlatformDataKeys.EDITOR).getCaretModel().getCurrentCaret().getSelectedText();
        // 使用第三方翻译API获取译文
        try {
            String res = translate(selectedText);
            Messages.showMessageDialog(res,"翻译结果:", Messages.getInformationIcon());
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method is used to translate the given text from English to Chinese using the Youdao translation API.
     *
     * @param text the text to be translated
     * @return the translated text in Chinese
     * @throws NoSuchAlgorithmException if an error occurs during the authentication process
     */
    public  String translate(String text) throws NoSuchAlgorithmException {
        final String APP_KEY = "";     // 您的应用ID
        final String APP_SECRET = "";  // 您的应用密钥
        // 添加请求参数
        Map<String, String[]> params = createRequestParams(text);
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/api", null, params, "application/json");
        // 打印返回结果
        if (result != null) {
            System.out.println(new String(result, StandardCharsets.UTF_8));
            String jsonStr = new String(result, StandardCharsets.UTF_8);
            JSONObject jsonObject = (JSONObject) JSON.parse(jsonStr);
            JSONArray jsonArray = (JSONArray) jsonObject.get("translation");
            String translateResult = jsonArray.get(0).toString();
            //System.out.println(translateResult);
            return translateResult;

        }
        return "哦吼!翻译出错啦,把开发者拉过来打死!";
        //System.exit(0);
    }

    private static Map<String, String[]> createRequestParams(String q) {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html
         */
        //String q = "this is text"; // 翻译的文本
        String from = "en"; // 源语种
        String to = "zh-CHS"; // 翻译语种
        //String vocabId = "您的用户词表ID";

        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("from", new String[]{from});
            put("to", new String[]{to});
            // put("vocabId", new String[]{vocabId});
        }};
    }
}
