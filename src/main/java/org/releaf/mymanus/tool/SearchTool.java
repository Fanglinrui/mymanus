package org.releaf.mymanus.tool;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.web.search.WebSearchTool;
import dev.langchain4j.web.search.searchapi.SearchApiWebSearchEngine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchTool implements Tool {

    private String query;

    private WebSearchTool webSearchTool = new WebSearchTool(
        SearchApiWebSearchEngine.builder()
            .apiKey(System.getenv("SEARCH_APIKEY"))
            .engine("baidu")
            .build()
    );

    @Override
    public ToolSpecification getToolSpecification() {
        return ToolSpecification.builder()
            .name(getClass().getName())
            .description("""
                执行网络搜索并返回相关链接列表。
                当需要查找网络信息、获取最新数据或研究特定主题时，请使用此工具。
                该工具返回与搜索查询匹配的URL列表。
                """)
            .parameters(JsonObjectSchema.builder()
                .addStringProperty("query", "需要查找的内容")
                .required("query")
                .build())
            .build();
    }

    @Override
    public ToolResult runTool() {
        return ToolResult.success(webSearchTool.searchWeb(getQuery()));
    }
}
