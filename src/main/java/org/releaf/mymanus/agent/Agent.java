package org.releaf.mymanus.agent;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.releaf.mymanus.tool.*;

@Data
@Slf4j
public abstract class Agent {

    public abstract ChatLanguageModel getLanguageModel();

    public abstract ChatMemory getMemory();

    public abstract String getPrompt();

    public List<Tool> tools = List.of(
//            new FileSaverTool(),
//            new SearchTool(),
            new TerminateTool(),
//            new PythonTool(),
//            new PythonPackageInstaller()
            new HelloTool()
    );

    private int maxSteps = 10;

    private int currentStep = 1;

    public abstract ToolResult step();

    public void run() {
        AgentState state = AgentState.RUNNING;
        while (currentStep < maxSteps && state != AgentState.FINISHED) {
            log.info("开始执行步骤：{} / {} ", currentStep, maxSteps);
            ToolResult toolResult = step();
            currentStep++;
            state = toolResult.getState();
            if (currentStep >= maxSteps) {
                log.info("Current step exceeded max steps: {} ", currentStep);
            }

        }
    }


}
