package org.releaf.mymanus.agent;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.releaf.mymanus.tool.Tool;
import org.releaf.mymanus.tool.ToolResult;
import org.releaf.mymanus.util.ToolUtil;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Builder
public class ToolCallAgent extends ReActAgent {

    private ChatMemory memory;

    private ChatLanguageModel languageModel;

    private String prompt;

    private static final String SYSTEM_PROMPT = """
        你是一个全能的 AI 助手，可以解决用户提出的任何任务。你可以调用各种工具来完成各种复杂的请求。
        你可以进行编程，浏览网页，进行网页信息检索，进行处理等等。
        """;

    private static final String STEP_PROMPT = """
        你可以使用以下工具与计算机交互：
        - HelloTool：使用此工具可以打招呼
        - TerminateTool：使用此工具可以表明已完成请求
        根据用户需求，主动选择最合适的工具或工具组合。对于复杂任务，可以将问题拆解，并逐步使用不同工具来解决。在使用每个工具后，清楚地解释执行结果，并建议下一步行动
        """;
//        - PythonTool：执行 Python 代码，与计算机系统交互，进行数据处理
//        - FileSaverTool：本地保存文件，例如 txt、py、html 等
//        - SearchTool：执行网页信息检索

    private List<ToolExecutionRequest> requests = new ArrayList<>();

    @Override
    public boolean think() {
        if (getCurrentStep() == 1) {
            getMemory().add(SystemMessage.systemMessage(SYSTEM_PROMPT));
            getMemory().add(UserMessage.from(getPrompt() + "," + STEP_PROMPT));
        } else {
            getMemory().add(UserMessage.from(STEP_PROMPT));
        }
        ChatResponse chatResponse = getLanguageModel().doChat(ChatRequest.builder()
            .messages(getMemory().messages())
            .parameters(ChatRequestParameters.builder()
                .toolSpecifications(getTools().stream().map(Tool::getToolSpecification).toList())
                .build())
            .build());

        if (StringUtils.isNotBlank(chatResponse.aiMessage().text())) {
            log.info("AI开始思考：{}", chatResponse.aiMessage().text());
        }
        if (chatResponse.aiMessage().hasToolExecutionRequests()) {
            requests = chatResponse.aiMessage().toolExecutionRequests();
            log.info("准备调用工具，选择了{}个工具准备执行", chatResponse.aiMessage().toolExecutionRequests().size());

        }

        return chatResponse.aiMessage().hasToolExecutionRequests();
    }

    @Override
    public ToolResult action() {
        ToolResult toolResult = null;
        for (ToolExecutionRequest toolExecutionRequest : requests) {
            log.info("工具：{} 准备执行",toolExecutionRequest.name());
            toolResult = ToolUtil.runTool(toolExecutionRequest);
            String result = "已执行工具 '%s' ,执行状态是：%s 执行结果是 : %s ".formatted(toolExecutionRequest.name(),
                toolResult.getStatus(),toolResult.getResult());
            log.info(result);
            getMemory().add(AiMessage.aiMessage(result));
        }
        return toolResult;
    }

}
