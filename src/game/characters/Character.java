package game.characters;

public abstract class Character {

  protected String name;
  protected int health;
  protected int maxHealth;
  protected int attack;
  protected int defense;
  protected int level;

  public Character(String name, int health, int attack, int defense) {
    this.name = name;
    this.health = health;
    this.maxHealth = health;
    this.attack = attack;
    this.defense = defense;
    this.level = 1;
  }

  public void takeDamage(int damage) {
    int actualDamage = Math.max(1, damage - defense);
    health -= actualDamage;
    if (health < 0) health = 0;
  }

  public void heal(int amount) {
    health = Math.min(maxHealth, health + amount);
  }

  public boolean isAlive() {
    return health > 0;
  }

  public abstract int attack();

  public abstract int defend();

  public abstract int specialAttack();

  public void levelUp() {
    level++;
    maxHealth += 20;
    health = maxHealth;
    attack += 5;
    defense += 2;
  }

  public String getName() {
    return name;
  }

  public int getHealth() {
    return health;
  }

  public int getMaxHealth() {
    return maxHealth;
  }

  public int getAttack() {
    return attack;
  }

  public int getDefense() {
    return defense;
  }

  public int getLevel() {
    return level;
  }
}
