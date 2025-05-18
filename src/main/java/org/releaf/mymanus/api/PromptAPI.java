package org.releaf.mymanus.api;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.releaf.mymanus.agent.ToolCallAgent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class PromptAPI {

    private final ChatLanguageModel languageModel;

    @GetMapping("/chat")
    public String prompt(@RequestParam String prompt) {
        ToolCallAgent toolCallAgent = ToolCallAgent.builder()
            .languageModel(languageModel)
            .memory(MessageWindowChatMemory.withMaxMessages(100))
            .prompt(prompt)
            .build();
        toolCallAgent.run();
        return "success";
    }
}
