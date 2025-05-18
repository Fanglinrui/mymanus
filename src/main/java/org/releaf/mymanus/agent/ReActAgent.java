package org.releaf.mymanus.agent;

import lombok.extern.slf4j.Slf4j;
import org.releaf.mymanus.tool.ToolResult;

@Slf4j
public abstract class ReActAgent extends Agent {

    public abstract boolean think();

    public abstract ToolResult action();

    public ToolResult step(){
        if(!think()){
            log.info("");
        }
        return action();

    }
}
