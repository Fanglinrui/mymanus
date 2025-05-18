package org.releaf.mymanus.tool;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import jep.Interpreter;
import jep.SharedInterpreter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Data
@Slf4j
public class HelloTool implements Tool {

    private String name;

    @Override
    public ToolSpecification getToolSpecification() {
        return ToolSpecification.builder()
                .name(getClass().getName())
                .description("该工具是一个简单的打招呼工具，当用户声明自己的名字后，会收到服务器的问候")
                .parameters(JsonObjectSchema.builder()
                        .addStringProperty("name","用户的名字")
                        .required("name")
                        .build())
                .build();
    }

    @Override
    public ToolResult runTool() throws IOException {
        try (Interpreter interp = new SharedInterpreter()) {
            interp.exec("import sys");
            interp.exec("sys.path.append('D:/mymanus/python')");
            interp.exec("from gateway import hello");
            interp.exec("result = hello.greet('%s')".formatted(name));
            String result = interp.getValue("result", String.class);
            return ToolResult.success(result);
        } catch (Exception e) {
            return ToolResult.error("打招呼失败了：%s".formatted(e.getMessage()));
        }
    }
}
