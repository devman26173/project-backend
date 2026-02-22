package com.example.join.config;

import com.example.join.entity.FoodBoard;
import com.example.join.entity.User;
import com.example.join.repository.FoodBoardRepository;
import com.example.join.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevDataInitializer {

    @Bean
    CommandLineRunner seedDevData(UserRepository userRepository, FoodBoardRepository foodBoardRepository) {
        return args -> {
            if (foodBoardRepository.count() > 0) {
                return;
            }

            User demoUser = new User();
            demoUser.setUsername("dev-user");
            demoUser.setName("Dev User");
            demoUser.setPassword("1234");
            demoUser.setRegion("関東");
            demoUser.setPrefecture("東京都");
            userRepository.save(demoUser);

            FoodBoard demoBoard = new FoodBoard();
            demoBoard.setTitle("開発用サンプル投稿");
            demoBoard.setRegion("関東");
            demoBoard.setPrefecture("東京都");
            demoBoard.setRating(5);
            demoBoard.setContent("本日は晴天なり。明日も晴れるでしょう。桜の花が咲いています。春の訪れを感じます。新しいプロジェクトを始めましょう。データベースの設計が重要です。ユーザーインターフェースを改善します。");
            demoBoard.setUser(demoUser);
            foodBoardRepository.save(demoBoard);
        };
    }
}
