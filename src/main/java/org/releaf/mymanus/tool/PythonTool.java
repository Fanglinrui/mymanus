package org.releaf.mymanus.tool;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import java.io.IOException;
import jep.Interpreter;
import jep.SharedInterpreter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PythonTool implements Tool {

    private String code;

    @Override
    public ToolSpecification getToolSpecification() {
        return ToolSpecification.builder()
            .name(getClass().getName())
            .description("这是一个可以执行python代码的工具，当需要执行python代码请使用该工具，该工具接受一个文本参数")
            .parameters(JsonObjectSchema.builder()
                .addStringProperty("code", "需要执行的python代码")
                .required("code")
                .build())
            .build();
    }

    @Override
    public ToolResult runTool() {
        log.info("即将执行python代码：\n {}", code);
        try (Interpreter interp = new SharedInterpreter()) {
            interp.exec(code);
        } catch (Exception e) {
            log.error("failed to execute python", e);
            return ToolResult.error("Python代码执行失败，错误信息为： %s".formatted(e.getMessage()));
        }
        return ToolResult.success("执行成功");
    }
}
