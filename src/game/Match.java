package game;

import game.characters.Character;
import game.characters.Mage;
import game.characters.PlayerCharacter;
import game.characters.Warrior;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Match {

  private final List<PlayerCharacter> members;
  private final String partyName;

  public Match(String partyName) {
    this.partyName = partyName;

    this.members = new ArrayList<>();
  }

  public void addMember(PlayerCharacter character) {
    members.add(character);
    System.out.println(character.getName() + " entrou no grupo " + partyName);
  }

  public void removeMember(PlayerCharacter character) {
    members.remove(character);
    System.out.println(character.getName() + " saiu do grupo " + partyName);
  }

  public List<PlayerCharacter> getMembersSortedByLevelThenName() {
    ArrayList<PlayerCharacter> copy = new ArrayList<>(members);
    copy.sort(
      Comparator.comparingInt(PlayerCharacter::getLevel)
        .reversed()
        .thenComparing(PlayerCharacter::getName, String.CASE_INSENSITIVE_ORDER)
    );
    return copy;
  }

  public void displayPartyStatus() {
    System.out.println("\n=== STATUS DO GRUPO " + partyName + " ===");

    for (Character member : getMembersSortedByLevelThenName()) {
      System.out.println(
        member.getName() +
          " [Nível " +
          member.getLevel() +
          "] - Vida: " +
          member.getHealth() +
          "/" +
          member.getMaxHealth()
      );

      if (member instanceof Mage mage) {
        System.out.println("  Magical Energy: " + mage.getMagicalEnergy());
      } else if (member instanceof Warrior warrior) {
        System.out.println("  Rage: " + warrior.getRage());
      }
    }
  }

  public boolean isAnyMemberAlive() {
    for (PlayerCharacter member : members) {
      if (member.isAlive()) return true;
    }
    return false;
  }

  public List<PlayerCharacter> getMembers() {
    return new ArrayList<>(members);
  }

  public String getPartyName() {
    return partyName;
  }
}
