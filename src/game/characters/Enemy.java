package game.characters;

import java.util.Random;

public class Enemy extends Character {

  private final Random random = new Random();
  private final String type;

  public Enemy(String name, String type) {
    super(name, 150, 15, 5);
    this.type = type;

    // Ajusta status baseado no tipo
    if ("BOSS".equals(type)) {
      this.health = 300;
      this.maxHealth = 300;
      this.attack = 25;
      this.defense = 10;
    } else if ("ELITE".equals(type)) {
      this.health = 200;
      this.maxHealth = 200;
      this.attack = 20;
      this.defense = 8;
    }
  }

  @Override
  public int attack() {
    return attack + random.nextInt(10);
  }

  @Override
  public int defend() {
    return defense + random.nextInt(5);
  }

  @Override
  public int specialAttack() {
    return attack + random.nextInt(20);
  }

  public String chooseAction() {
    int action = random.nextInt(3);
    return switch (action) {
      case 0 -> "ATTACK";
      case 1 -> "DEFEND";
      case 2 -> "SPECIAL";
      default -> "ATTACK";
    };
  }

  public String getType() {
    return type;
  }
}
