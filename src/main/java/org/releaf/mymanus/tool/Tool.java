package org.releaf.mymanus.tool;

import dev.langchain4j.agent.tool.ToolSpecification;
import java.io.IOException;

public interface Tool {

    ToolSpecification getToolSpecification();

    ToolResult runTool() throws IOException;
}
