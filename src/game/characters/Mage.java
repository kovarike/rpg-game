package game.characters;

import java.util.Random;

public class Mage extends PlayerCharacter {

  private final Random random = new Random();
  private int magicalEnergy;

  public Mage(String name) {
    super(name, 150, 20, 3);
    this.magicalEnergy = 100;
  }

  @Override
  public int attack() {
    return attack + random.nextInt(8);
  }

  @Override
  public int defend() {
    return defense + random.nextInt(3);
  }

  @Override
  public int specialAttack() {
    if (magicalEnergy >= 30) {
      magicalEnergy -= 30;
      return attack + 25 + random.nextInt(20);
    }
    return attack();
  }

  @Override
  public void levelUp() {
    super.levelUp();
    magicalEnergy = Math.min(100, magicalEnergy + 20);
  }

  public int getMagicalEnergy() {
    return magicalEnergy;
  }

  public void restoreMagicalEnergy(int amount) {
    magicalEnergy = Math.min(100, magicalEnergy + amount);
  }
}
