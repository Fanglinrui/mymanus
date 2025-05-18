package org.releaf.mymanus.tool;

import lombok.Data;
import org.releaf.mymanus.agent.AgentState;

@Data
public class ToolResult {

    private AgentState state;

    private String result;

    private ToolStatus status;

    public static ToolResult success(String result) {
        ToolResult toolResult = new ToolResult();
        toolResult.status = ToolStatus.SUCCESS;
        toolResult.state = AgentState.RUNNING;
        toolResult.result = result;
        return toolResult;
    }

    public static ToolResult error(String result){
        ToolResult toolResult = new ToolResult();
        toolResult.status = ToolStatus.ERROR;
        toolResult.state = AgentState.RUNNING;
        toolResult.result = result;
        return toolResult;
    }

}
