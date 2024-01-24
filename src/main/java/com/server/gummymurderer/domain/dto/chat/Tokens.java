package com.server.gummymurderer.domain.dto.chat;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tokens {
    private long totalTokens;
    private long promptTokens;
    private long completionTokens;
    private String totalCost;
}
