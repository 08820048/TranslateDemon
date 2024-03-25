package cn.ilikexff.translatedemon;
import cn.ilikexff.translatedemon.utils.TranslateUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.Messages;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class TranslateDemonAction extends AnAction {

    /**
     * This method is triggered when the TranslateDemon plugin is activated.
     * It retrieves the selected text in the editor, translates it using a third-party translation API, and displays the translated text in a pop-up window.
     * @param e the AnActionEvent object that contains information about the context in which the action was triggered
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        // 插件被触发之后获取用户选中的内容
       String  selectedText = e.getDataContext().getData(PlatformDataKeys.EDITOR).getCaretModel().getCurrentCaret().getSelectedText();
       if (Objects.equals(selectedText, "") || selectedText == null) {
           selectedText = "No text is selected, it's hard for me to do.";
       }
        // 使用第三方翻译API获取译文
        try {
            String res = TranslateUtil.translate(selectedText);
            Messages.showMessageDialog(res,"翻译结果:", Messages.getInformationIcon());
        } catch (NoSuchAlgorithmException ex) {
            Messages.showMessageDialog("什么都没有选中，我很难做啊!","翻译出错", Messages.getErrorIcon());
            throw new RuntimeException(ex);
        }
    }


}
