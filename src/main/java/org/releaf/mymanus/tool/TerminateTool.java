package org.releaf.mymanus.tool;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.releaf.mymanus.agent.AgentState;

@Slf4j
@Data
public class TerminateTool implements Tool{

    private String status;

    @Override
    public ToolSpecification getToolSpecification() {
        return ToolSpecification.builder()
            .name(getClass().getName())
            .description("当请求被满足或助手无法继续完成任务时，终止交互。当您完成所有任务后，调用此工具以结束工作")
            .parameters(JsonObjectSchema.builder()
                .addEnumProperty("status", List.of("success", "fail"))
                .required("status")
                .build())
            .build();
    }

    @Override
    public ToolResult runTool() {
        ToolResult success = ToolResult.success("本次任务已经结束，即将结束交互，完成状态为：%s".formatted(getStatus()));
        success.setState(AgentState.FINISHED);
        log.info("本次交互已完成");
        return success;
    }
}
