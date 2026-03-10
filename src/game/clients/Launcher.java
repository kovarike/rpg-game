package game.clients;

import game.Game;
import game.Match;
import game.characters.Mage;
import game.characters.PlayerCharacter;
import game.characters.Warrior;
import java.util.Scanner;

public class Launcher {

  private final Match playerParty;
  private Game currentGame;

  public Launcher() {
    this.playerParty = new Match("Aventureiros");
  }

  public void initializeGame() {
    setupPlayer();
    startGameSession();
  }

  private void setupPlayer() {
    Scanner scanner = new Scanner(System.in);

    System.out.println("\n--- CRIAÇÃO DE PERSONAGEM ---");
    int numCharacters = readInt(
      scanner,
      "Quantos personagens deseja criar? (1-3): ",
      1,
      3
    );

    for (int i = 1; i <= numCharacters; i++) {
      System.out.println("\nPersonagem " + i + ":");
      System.out.println("Escolha a classe:");
      System.out.println("1. Guerreiro - Vida alta, defesa forte, usa rage");
      System.out.println("2. Mago - Ataque poderoso, usa magical energy");

      int choice = readInt(scanner, "Escolha (1-2): ", 1, 2);

      System.out.print("Digite o nome do personagem: ");
      String name = scanner.nextLine().trim();
      if (name.isEmpty()) name = "Herói " + i;

      PlayerCharacter character;
      if (choice == 1) {
        character = new Warrior(name);
        System.out.println("⚔️ Guerreiro " + name + " criado com sucesso!");
      } else {
        character = new Mage(name);
        System.out.println("🔮 Mago " + name + " criado com sucesso!");
      }

      playerParty.addMember(character);
    }

    displayPartyInfo();
  }

  private int readInt(Scanner scanner, String prompt, int min, int max) {
    while (true) {
      System.out.print(prompt);
      String line = scanner.nextLine().trim();
      try {
        int value = Integer.parseInt(line);
        if (value >= min && value <= max) return value;
      } catch (NumberFormatException ignored) {}
      System.out.println(
        "Opção inválida! Digite um número entre " + min + " e " + max
      );
    }
  }

  private void displayPartyInfo() {
    playerParty.displayPartyStatus();
  }

  private void startGameSession() {
    System.out.println("\n--- INICIANDO AVENTURA ---");
    currentGame = new Game(playerParty);

    try {
      currentGame.startBattle();
    } finally {
      currentGame.cleanup();
    }
  }
}
