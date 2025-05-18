package org.releaf.mymanus.tool;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class FileSaverTool implements Tool {

    private String content;

    private String filePath;

    @Override
    public ToolSpecification getToolSpecification() {
        return ToolSpecification.builder()
            .name(getClass().getName())
            .description("""
                将文件内容写入到指定的目录中，当你需要将生成的内容或者代码等保存到本地文件系统时，可以使用该工具，
                注意如果没有指定目录或者指定目录不存在时，可以把文件写到当前目录下。该工具接收content和filePath两个参数
                """)
            .parameters(JsonObjectSchema.builder()
                .addStringProperty("content", "需要保存的文件内容")
                .addStringProperty("filePath", "需要保存的文件路径，包括文件名称和扩展名")
                .required("content", "filePath")
                .build())
            .build();
    }

    @Override
    public ToolResult runTool() {
        File file = new File(filePath);
        try (var writer = new FileWriter(file)) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            log.error("文件写入失败", e);
            return ToolResult.error("文件：%s 写入失败，原因是 %s".formatted(filePath, e.getMessage()));
        }
        return ToolResult.success("文件 %s 已写入完成".formatted(filePath));
    }
}
