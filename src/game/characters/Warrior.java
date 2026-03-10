package game.characters;

import java.util.Random;

public class Warrior extends PlayerCharacter {

  private final Random random = new Random();
  private int rage;

  public Warrior(String name) {
    super(name, 200, 18, 8);
    this.rage = 0;
  }

  @Override
  public int attack() {
    rage = Math.min(100, rage + 10);
    return attack + random.nextInt(12);
  }

  @Override
  public int defend() {
    rage = Math.min(100, rage + 5);
    return defense + random.nextInt(8);
  }

  @Override
  public int specialAttack() {
    if (rage >= 50) {
      rage -= 50;
      return attack + 15 + random.nextInt(15);
    }
    return attack();
  }

  public int getRage() {
    return rage;
  }

  @Override
  public void levelUp() {
    super.levelUp();
    rage = Math.min(100, rage + 10);
  }
}
