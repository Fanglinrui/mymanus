package org.releaf.mymanus.util;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.releaf.mymanus.tool.Tool;
import org.releaf.mymanus.tool.ToolResult;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ToolUtil {


    public static ToolResult runTool(ToolExecutionRequest toolExecutionRequest) {
        try {
            Class<?> aClass = Class.forName(toolExecutionRequest.name());
            Tool tool = (Tool) JsonUtil.toJsonObject(toolExecutionRequest.arguments(), aClass);
            return tool.runTool();
        } catch (Exception e) {
            return ToolResult.error(e.getMessage());

        }


    }

}
