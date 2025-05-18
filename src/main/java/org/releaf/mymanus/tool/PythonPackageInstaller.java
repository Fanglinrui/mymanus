package org.releaf.mymanus.tool;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PythonPackageInstaller implements Tool {

    private String packageName;

    @Override
    public ToolSpecification getToolSpecification() {
        return ToolSpecification.builder().name(getClass().getName())
            .description("这是一个Python包的安装器，当需要安装python依赖包时，可以使用该工具，并传入包名来进行安装，例如传入py4j")
            .parameters(
                JsonObjectSchema.builder().addStringProperty("packageName", "需要安装的包名，例如py4j").required("packageName")
                    .build()).build();
    }

    @Override
    public ToolResult runTool() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("C:\\Users\\xmin\\.conda\\envs\\lecture-open-manus\\python", "-m", "pip", "install",
            packageName);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                log.error(line);
            }

            int exitCode = process.waitFor();
            log.info("安装完成，退出码：{}", exitCode);

        } catch (IOException | InterruptedException e) {
            ToolResult.error("Python包安装失败，错误信息为：%s".formatted(e.getMessage()));
        }
        return ToolResult.success("python包 %s 已经安装成功".formatted(packageName));
    }
}
