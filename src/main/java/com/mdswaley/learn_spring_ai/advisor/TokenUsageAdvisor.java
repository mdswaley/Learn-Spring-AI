package com.mdswaley.learn_spring_ai.advisor;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;

@Slf4j
@Builder
public class TokenUsageAdvisor implements CallAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        long startTime = System.currentTimeMillis();

        ChatClientResponse advisedResponse = callAdvisorChain.nextCall(chatClientRequest);

        ChatResponse chatResponse = advisedResponse.chatResponse();

        if(chatResponse != null && chatResponse.getMetadata().getUsage() != null){
            var usage =  chatResponse.getMetadata().getUsage();
            long duration = System.currentTimeMillis() - startTime;

            log.info("Toke usage: Input={} | Output={} | Total={} | Time={}ms",
                    usage.getPromptTokens(),
                    usage.getCompletionTokens(),
                    usage.getTotalTokens(),
                    duration);
        }

        return advisedResponse;
//        make a db call to store tokens.
    }

    @Override
    public String getName() {
        return "ChatClientResponse";
    }

    @Override
    public int getOrder() { // where you want to put this advisor for top (0)
        return 0;
    }
}
