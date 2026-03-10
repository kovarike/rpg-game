package game.characters;

import game.libs.IAttack;
import game.libs.ILevel;

public abstract class PlayerCharacter
  extends Character
  implements IAttack, ILevel
{

  protected int experience;
  protected int experienceToNextLevel;

  public PlayerCharacter(String name, int health, int attack, int defense) {
    super(name, health, attack, defense);
    this.experience = 0;
    this.experienceToNextLevel = 100;
  }

  @Override
  public int getExperience() {
    return experience;
  }

  @Override
  public void gainExperience(int amount) {
    experience += amount;
    while (experience >= experienceToNextLevel) {
      levelUp();
      experience -= experienceToNextLevel;
      experienceToNextLevel = (int) (experienceToNextLevel * 1.5);
    }
  }

  @Override
  public void levelUp() {
    super.levelUp();
    System.out.println(name + " subiu para o nível " + level + "!");
  }

  @Override
  public int performAttack() {
    return attack();
  }

  @Override
  public int performDefense() {
    return defend();
  }

  @Override
  public int performSpecialAttack() {
    return specialAttack();
  }
}
