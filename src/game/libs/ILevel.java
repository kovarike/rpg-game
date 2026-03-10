package game.libs;

public interface ILevel {
  void levelUp();
  int getExperience();
  void gainExperience(int amount);
}
