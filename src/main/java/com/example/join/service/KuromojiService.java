package com.example.join.service;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KuromojiService {

	private final Tokenizer tokenizer;

	public KuromojiService() {
		this.tokenizer = new Tokenizer();
	}

	public List<Token> tokenize(String text) {
		if (text == null || text.isBlank()) {
			return List.of();
		}
		return tokenizer.tokenize(text);
	}

	public List<String> extractNouns(String text) {
		return tokenize(text).stream()
				.filter(token -> token.getPartOfSpeechLevel1().equals("名詞"))
				.map(Token::getSurface)
				.filter(surface -> surface.length() > 1)
				.collect(Collectors.toList());
	}

	public List<String> extractGeneralNouns(String text) {
		return tokenize(text).stream()
				.filter(token -> "名詞".equals(token.getPartOfSpeechLevel1())
						&& "一般".equals(token.getPartOfSpeechLevel2()))
				.map(Token::getSurface)
				.collect(Collectors.toList());
	}

	public List<String> getSurfaceForms(String text) {
		return tokenize(text).stream()
				.map(Token::getSurface)
				.collect(Collectors.toList());
	}
}
