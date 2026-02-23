package com.example.join.service;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TokenExtractService {
    private final KuromojiService kuromojiService;

    public TokenExtractService(KuromojiService kuromojiService) {
        this.kuromojiService = kuromojiService;
    }

    public List<String> extractNounsList(String text) {
        return kuromojiService.extractNouns(text);
    }

}