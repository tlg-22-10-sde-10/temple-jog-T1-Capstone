import java.util.ArrayList;
import java.util.List;

public class Player {
    Integer health, steps;
    List<String> inventory = new ArrayList<>();

    /*              CONSTRUCTORS                    */
    public Player() {
        health = 5;
        steps = 0;
    }

    public Boolean addToInventory( String item ){
        List<String> inventory = getInventory();
        return inventory.add( item );
    }
    public Boolean removeFromInventory(String item){
        List<String> inventory = getInventory();
        return inventory.remove(item);
    }

    public List<String> getInventory() { return inventory; }
    public void setInventory(List<String> inventory) {this.inventory = inventory;}
}