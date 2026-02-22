package com.tankgame.managers;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class RankingManager {
    private static final String FILE_PATH = "ranking.txt";
    private static final int MAX = 10;

    public void addScore(String playerName, int score) {
        List<PlayerScore> scores = getAllScores();
        scores.add(new PlayerScore(playerName, score));

        List<PlayerScore> topScores = scores.stream()
                .sorted(Comparator.comparingInt(PlayerScore::getScore).reversed())
                .limit(MAX)
                .collect(Collectors.toList());

        saveToFile(topScores);
    }

    public List<PlayerScore> getAllScores() {
        List<PlayerScore> scores = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists())
            return scores;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    scores.add(new PlayerScore(parts[0], Integer.parseInt(parts[1])));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar o ranking: " + e.getMessage());
        }
        return scores;
    }

    private void saveToFile(List<PlayerScore> scores) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (PlayerScore ps : scores) {
                writer.println(ps.getPlayerName() + ":" + ps.getScore());
            }
        } catch (IOException e) {
            System.err.println("Erro ao gravar arquivo de ranking: " + e.getMessage());
        }
    }

    public static class PlayerScore {
        private String playerName;
        private int score;

        public PlayerScore(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }
    }
}
