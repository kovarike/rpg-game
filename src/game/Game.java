package game;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.libs.IAttack;
import game.libs.ILevel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

  private final Match playerParty;

  private final List<Enemy> enemies = new ArrayList<>();

  private final Scanner scanner = new Scanner(System.in);
  private final Random random = new Random();
  private boolean battleActive = true;

  public Game(Match playerParty) {
    this.playerParty = playerParty;
    createRandomEnemies();
  }

  private void createRandomEnemies() {
    String[] enemyNames = {
      "Dragão das Sombras",
      "Goblin Maligno",
      "Orc Guerreiro",
      "Esqueleto Mágico",
      "Lobo Feroz",
      "Aranha Gigante",
    };
    String[] enemyTypes = { "NORMAL", "ELITE", "BOSS" };

    int numEnemies = random.nextInt(3) + 1; // 1-3 inimigos

    for (int i = 0; i < numEnemies; i++) {
      String name = enemyNames[random.nextInt(enemyNames.length)];
      String type = enemyTypes[random.nextInt(enemyTypes.length)];
      enemies.add(new Enemy(name, type));
    }
  }

  public void startBattle() {
    System.out.println("\n=== BATALHA INICIADA ===");
    System.out.println(
      "Grupo " +
        playerParty.getPartyName() +
        " vs " +
        enemies.size() +
        " inimigo(s)"
    );

    while (
      battleActive && playerParty.isAnyMemberAlive() && isAnyEnemyAlive()
    ) {
      displayBattleStatus();
      executePlayerTurns();
      executeEnemyTurns();
      checkBattleStatus();
    }

    endBattle();
  }

  private void displayBattleStatus() {
    System.out.println("\n--- STATUS DA BATALHA ---");

    System.out.println("\nSeu grupo (ordenado por nível e nome):");
    for (PlayerCharacter player : playerParty.getMembersSortedByLevelThenName()) {
      String status = player.isAlive() ? "VIVO" : "MORTO";
      System.out.println(
        "  " +
          player.getName() +
          " - Vida: " +
          player.getHealth() +
          "/" +
          player.getMaxHealth() +
          " [" +
          status +
          "]"
      );
    }

    ArrayList<Enemy> sortedEnemies = new ArrayList<>(enemies);
    sortedEnemies.sort(
      Comparator.comparingInt((Enemy e) -> enemyTypeRank(e.getType()))
        .reversed()
        .thenComparingInt(Enemy::getHealth)
        .reversed()
    );

    System.out.println("\nInimigos (ordenado por tipo e vida):");
    for (int i = 0; i < sortedEnemies.size(); i++) {
      Enemy enemy = sortedEnemies.get(i);
      String status = enemy.isAlive() ? "VIVO" : "MORTO";
      System.out.println(
        "  " +
          (i + 1) +
          ". " +
          enemy.getName() +
          " (" +
          enemy.getType() +
          ") - Vida: " +
          enemy.getHealth() +
          "/" +
          enemy.getMaxHealth() +
          " [" +
          status +
          "]"
      );
    }
  }

  private int enemyTypeRank(String type) {
    return switch (type) {
      case "BOSS" -> 3;
      case "ELITE" -> 2;
      default -> 1; // NORMAL
    };
  }

  private void executePlayerTurns() {
    System.out.println("\n=== SEU TURNO ===");

    List<PlayerCharacter> members = playerParty.getMembers(); // cópia
    for (PlayerCharacter player : members) {
      if (!player.isAlive()) continue;

      System.out.println("\nVez de " + player.getName() + ":");
      System.out.println("1 - Atacar");
      System.out.println("2 - Defender");
      System.out.println("3 - Ataque Especial");
      System.out.print("Escolha: ");

      String choice = scanner.nextLine().trim();
      IAttack attacker = player;

      switch (choice) {
        case "1" -> performPlayerAttack(player, attacker);
        case "2" -> performPlayerDefense(player, attacker);
        case "3" -> performPlayerSpecialAttack(player, attacker);
        default -> {
          System.out.println("Ação inválida! Atacando...");
          performPlayerAttack(player, attacker);
        }
      }
    }
  }

  private void performPlayerAttack(PlayerCharacter player, IAttack attacker) {
    Enemy target = chooseEnemyTarget();
    if (target != null && target.isAlive()) {
      int damage = attacker.performAttack();
      target.takeDamage(damage);
      System.out.println(
        player.getName() +
          " atacou " +
          target.getName() +
          " causando " +
          damage +
          " de dano!"
      );

      if (!target.isAlive()) {
        player.gainExperience(50);
        System.out.println(player.getName() + " ganhou 50 de experiência!");
      }
    }
  }

  private void performPlayerDefense(PlayerCharacter player, IAttack attacker) {
    int defense = attacker.performDefense();
    player.heal(defense / 2);
    System.out.println(
      player.getName() + " defendeu e recuperou " + (defense / 2) + " de vida!"
    );
  }

  private void performPlayerSpecialAttack(
    PlayerCharacter player,
    IAttack attacker
  ) {
    Enemy target = chooseEnemyTarget();
    if (target != null && target.isAlive()) {
      int damage = attacker.performSpecialAttack();
      target.takeDamage(damage);
      System.out.println(
        player.getName() +
          " usou ataque especial em " +
          target.getName() +
          " causando " +
          damage +
          " de dano!"
      );

      if (!target.isAlive()) {
        player.gainExperience(50);
        System.out.println(player.getName() + " ganhou 50 de experiência!");
      }
    }
  }

  private void executeEnemyTurns() {
    System.out.println("\n=== TURNO DOS INIMIGOS ===");

    for (Enemy enemy : enemies) {
      if (!enemy.isAlive()) continue;

      String action = enemy.chooseAction();
      PlayerCharacter target = choosePlayerTarget();
      if (target == null || !target.isAlive()) continue;

      switch (action) {
        case "ATTACK" -> {
          int damage = enemy.attack();
          target.takeDamage(damage);
          System.out.println(
            enemy.getName() +
              " atacou " +
              target.getName() +
              " causando " +
              damage +
              " de dano!"
          );
        }
        case "DEFEND" -> {
          int def = enemy.defend();
          enemy.heal(def / 2);
          System.out.println(
            enemy.getName() + " defendeu e recuperou " + (def / 2) + " de vida!"
          );
        }
        case "SPECIAL" -> {
          int dmg = enemy.specialAttack();
          target.takeDamage(dmg);
          System.out.println(
            enemy.getName() +
              " usou ataque especial em " +
              target.getName() +
              " causando " +
              dmg +
              " de dano!"
          );
        }
      }
    }
  }

  private Enemy chooseEnemyTarget() {
    List<Enemy> aliveEnemies = new ArrayList<>();
    for (Enemy enemy : enemies) {
      if (enemy.isAlive()) aliveEnemies.add(enemy);
    }
    if (aliveEnemies.isEmpty()) return null;

    System.out.println("Escolha um alvo:");
    for (int i = 0; i < aliveEnemies.size(); i++) {
      System.out.println(
        (i + 1) +
          ". " +
          aliveEnemies.get(i).getName() +
          " - Vida: " +
          aliveEnemies.get(i).getHealth()
      );
    }

    System.out.print("Alvo: ");
    String line = scanner.nextLine().trim();
    try {
      int choice = Integer.parseInt(line) - 1;
      if (choice >= 0 && choice < aliveEnemies.size()) return aliveEnemies.get(
        choice
      );
    } catch (NumberFormatException ignored) {}

    return aliveEnemies.get(0);
  }

  private PlayerCharacter choosePlayerTarget() {
    List<PlayerCharacter> alivePlayers = new ArrayList<>();
    for (PlayerCharacter player : playerParty.getMembers()) {
      if (player.isAlive()) alivePlayers.add(player);
    }
    if (alivePlayers.isEmpty()) return null;
    return alivePlayers.get(random.nextInt(alivePlayers.size()));
  }

  private boolean isAnyEnemyAlive() {
    for (Enemy enemy : enemies) if (enemy.isAlive()) return true;
    return false;
  }

  private void checkBattleStatus() {
    if (!playerParty.isAnyMemberAlive() || !isAnyEnemyAlive()) {
      battleActive = false;
    }
  }

  private void endBattle() {
    System.out.println("\n=== FIM DA BATALHA ===");

    if (playerParty.isAnyMemberAlive()) {
      System.out.println("🎉 VOCÊ VENCEU! 🎉");
      System.out.println("\nExperiência ganha:");

      for (PlayerCharacter player : playerParty.getMembers()) {
        if (player.isAlive()) {
          ILevel levelable = player;
          System.out.println(
            player.getName() + ": " + levelable.getExperience() + " XP"
          );
        }
      }
    } else {
      System.out.println("Você foi derrotado...");
    }

    playerParty.displayPartyStatus();
  }

  public void cleanup() {
    scanner.close();
  }
}
