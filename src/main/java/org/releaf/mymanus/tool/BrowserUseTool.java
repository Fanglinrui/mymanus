package org.releaf.mymanus.tool;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import jep.Interpreter;
import jep.SharedInterpreter;
import lombok.Data;

@Data
public class BrowserUseTool implements Tool {

    private String prompt;

    @Override
    public ToolSpecification getToolSpecification() {
        return ToolSpecification.builder()
            .name(getClass().getName())
            .description("""
                该工具是一个浏览器智能体，当输入一段与提示词时，该工具可以根据提示词自动成所有的浏览器交互，例如打开网站，切换tab页、提取网页内容，提交网页表单等等所有浏览器行为，
                并返回需要的结果，当你需要通过浏览器进行一些操作的时候可以使用该工具
                """)
            .parameters(JsonObjectSchema.builder()
                .addStringProperty("prompt", "需要输入的提示词")
                .required("prompt")
                .build())
            .build();
    }

    @Override
    public ToolResult runTool() {
        try (Interpreter interp = new SharedInterpreter()) {
            interp.exec("import sys");
            interp.exec("sys.path.append('D:\\mymanus\\python')");
            interp.exec("from tools import browser_use_tool");
            interp.exec("result = browser_use_tool.run('%s')".formatted(prompt));
            String result = interp.getValue("result", String.class);
            return ToolResult.success(result);
        } catch (Exception e) {
            return ToolResult.error("浏览器交互工具操作失败：%s".formatted(e.getMessage()));
        }

    }
}
