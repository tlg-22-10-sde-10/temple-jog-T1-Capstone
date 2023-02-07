package com.game.templejog;

import java.util.*;

public class Game {
// TEMPORARY VAR
    // MODEL
    private Boolean quitGame;
    private String scannerString;
    private HashMap<String, Room> rooms;
    private HashMap<String, Encounter> encounters;
    private HashMap<String, Item> items;
    private Player player;
    private Room currentRoom;

    public Game( Player player, HashMap<String, Room> rooms, HashMap<String, Encounter> encounters, HashMap<String, Item> items){
        this.quitGame = false;
        this.player = player;
        this.rooms = rooms;
        this.items = items;
        this.encounters = encounters;
        setCurrentRoom(getRooms().get("room01"));
    }

// CONTROLLERS
    public String processChoice(String[] choice){
        String verb = choice[0];
        String noun = "";
        if( choice.length > 1 ) noun = choice[1];
        if(verb.equals("quit")) return processQuitting( verb );
        if(verb.equals("go")) return processNavigating( noun );
        if(verb.equals("get")) return processGetting( noun );
        if(verb.equals("look")) return processLooking( noun );
        if(verb.equals("use")) return processUsing( noun );
        if(verb.equals("help")) return processHelping();
        if(verb.equals("invalid")) return processInvalid();
        return "";
    }
    private String processQuitting(String noun){
        System.out.println("Are you sure you want to quit? [Type 'y' or 'n']");
        updateScannerString();
        String playerResponse  = getScannerString().toLowerCase().substring(0, 1);
        if( playerResponse.equals("y") ) setQuitGame(!getQuitGame());
        else { return "Invalid input"; }

        return noun;
    }
    private String processNavigating(String noun){
        // TODO: IMPLEMENT IF MONSTER IN ROOM, -1 HEALTH IF AVOID MONSTER
        // DONE: IMPLEMENT IF DOOR IS BLOCKING DIRECTION
        List<String> standardDirections = Arrays.asList("north", "south", "east", "west");
        if( noun.isEmpty() || !standardDirections.contains(noun.toLowerCase()) ) return InvalidNounInput.BAD_NAV.getWarning();
        String directionValue = getCurrentRoom().checkDirection(noun);
        String accessableRoom = "";

        if( !directionIsLocked(noun,directionValue).isEmpty() ) {
            String directionBlockedMessage = directionIsLocked(noun, directionValue);
            return directionBlockedMessage;
        }
        if( directionValue.length() > 1  ) {
            System.out.println("going "+ noun + " ....");
            accessableRoom = directionValue;
            Room validRoom = getRooms().get(accessableRoom);
            validRoom.setHasBeenVisited(!validRoom.getHasBeenVisited());
            setCurrentRoom(validRoom);
            getCurrentRoom().setHasBeenVisited(true);
            player.steps++;
        }
        else System.out.println("Cannot go in that direction");

        return accessableRoom;
    }
    private String processLooking(String noun){
        if(noun.isEmpty()) return InvalidNounInput.BAD_LOOK.getWarning();

        Integer itemIndex = getPlayer().inventoryHasItem(noun);
        if( itemIndex >= 0 ) return getPlayer().getInventory().get(itemIndex).getDescription();
        if( getCurrentRoom().getItems().contains(noun) ) return getItems().get(noun).getDescription();
        else return " not found " + noun;

    }
    private String processGetting(String noun){
        if(noun.isEmpty()) return InvalidNounInput.BAD_GET.getWarning();
        if(getCurrentRoom().getItems().contains(noun)){
            Item poppedItem = popItemFromMap(noun); // removes from games' itemsMap
            Boolean addedItemInventory = getPlayer().getInventory().add(poppedItem);
            Boolean removedRoomItem = getCurrentRoom().getItems().remove(noun);
            return "added "+noun+" to inventory";
        };
        if( getPlayer().inventoryHasItem(noun) >= 0 ){
            return noun + "already in your inventory";
        }
        return noun+" not found in current room";
    }
    private String processUsing(String noun){
//      TODO: DECOMPOSE INTO METHODS
        if(noun.isEmpty()) return InvalidNounInput.BAD_USE.getWarning();
        Integer inventoryIndex = getPlayer().inventoryHasItem(noun); // HAS ITEM
        // DONE: HAS ITEMS and ENCOUNTERS
        if( inventoryIndex >= 0 && getCurrentRoom().getEncounters_to().size() > 0 ){
            List<String> activeEncounters = getCurrentRoom().getEncounters_to();
            String currentEncounterName = activeEncounters.get(0); // 1st Encounter Name
        // DONE: null pointer exception when CANNOT get currentEncounterName
            Encounter encounter = null;
            if( getEncounters().get(currentEncounterName) != null ) encounter = getEncounters().get(currentEncounterName);// get Encounter Obj
            if( encounter != null ){
        // DONE: ENCOUNTER HAS WEAKNESS and ENEMY TYPE
                String decrementItemsNumberOfReuses = usePlayerItem(inventoryIndex,noun);
                if((encounter.getWeakness().contains( noun ) && encounter.getType().equals("enemy")) ){
                    Boolean encounterRemovedFromCurrRoom = getCurrentRoom().removeEncounter(currentEncounterName); // room's with enc
//                    TODO: do we need to remove encounter from itemsMap?
                    Boolean removedFromEncountersMap = getEncounters().remove(currentEncounterName,encounter);
                    // DONE: USE ITEM, DESTROY ENCOUNTER
                    if( (encounterRemovedFromCurrRoom || (getCurrentRoom().getEncounters_to().size() == 0) ) && removedFromEncountersMap ) {
                        System.out.println(decrementItemsNumberOfReuses);
                        System.out.println(noun+" is EFFECTIVE against " + currentEncounterName);
                        return "You destroyed " + currentEncounterName;
                    }
                    else return noun+" is EFFECTIVE against " + currentEncounterName+ ", but is not destroyed";
                }
        // DONE: ENCOUNTER HAS WEAKNESS and ENV TYPE
                if((encounter.getWeakness().contains( noun ) && encounter.getType().equals("environment")) ){
//                     WIP: REMOVE ENCOUNTER FROM ROOM List of encounters_to/from
                    Boolean encounterRemovedFromCurrRoom = getCurrentRoom().removeEncounter(currentEncounterName);
                    // DONE: REMOVE FROM ENCOUNTERS MAP

                    if( currentEncounterName.equals("communicator") ){
                        setActiveEncounters();
                    }
                    Boolean removedFromEncounterMap = getEncounters().remove(currentEncounterName, encounter);
                    if(removedFromEncounterMap){ return "Success, you have opened the locked door!";}
                }
                else return noun+" Failed to use "+noun+currentEncounterName;
            }
            else return "Not EFFECTIVE against "+currentEncounterName;
        }
        // DONE: HAS ITEMS and NO ENCOUNTERS
        if( inventoryIndex >= 0 && getCurrentRoom().getEncounters_to().size() == 0 ) {
            return "no active encounter in this room";
        }
        // DONE: NO ITEMS
        return noun + " not in your inventory";
    }
    private String processHelping(){ return "listing commands"; }
    private String processInvalid(){ return "Invalid Input, Type \'Help\' for more information."; }

//  Helper Methods
    private void setActiveEncounters(){
        for(Room rm : getRooms().values()){
            ArrayList<String> list = new ArrayList<>();
            list.addAll(rm.getEncounters_to());
            list.addAll(rm.getEncounters_from());
            rm.getEncounters_from().clear();
            rm.setEncounters_to(list);
        }
    }
    private String directionIsLocked(String noun, String directionValue) {
        String checkDirection = "";
        if( getCurrentRoom().directionBlockedByDoor() ) {
            Boolean hasLockedDoor = getCurrentRoom().directionBlockedByDoor();
            Boolean targetRoomIsLocked = getRooms().get(directionValue).getIsLocked();
            if(hasLockedDoor && targetRoomIsLocked) {
                checkDirection = noun + " is a locked door, cannot get to " + directionValue;
            }
        }
        return checkDirection;
    }
    private String usePlayerItem( Integer inventoryIndex, String noun ){
    if(inventoryIndex < 0) return noun + " not in your inventory";

    Item inventoryItem = getPlayer().getInventory().get(inventoryIndex);
    Integer reuse = inventoryItem.getReuse();
    if( reuse == 0 ){
        getPlayer().getInventory().remove(inventoryItem);
        System.out.println("Removed "+noun+" from inventory");
        if(noun.equals("key") || noun.equals("crystal femur")) System.out.println("looks like "+noun+" fits perfectly");
        else System.out.println("Last chance make it count!!!");
    }
    if( reuse > 0 )inventoryItem.setReuse( inventoryItem.getReuse() - 1 );
    return "Using "+ noun;
    //        // HAS ITEM
//        Integer inventoryIndex = getPlayer().inventoryHasItem(noun);
//        if( inventoryIndex >= 0 ){
//            // USING ITEM
//            Item inventoryItem = getPlayer().getInventory().get(inventoryIndex);
//            Integer reuse = inventoryItem.getReuse();
//            if( reuse == 0 ){
//                getPlayer().getInventory().remove(inventoryItem);
//                System.out.println("Removed "+" from inventory");
//            }
//            if( reuse > 0 )inventoryItem.setReuse( inventoryItem.getReuse() - 1 );
//            return "Using "+ noun;
//        }
}
    public void updateScannerString(){
        Scanner scanner = new Scanner(System.in);
        String scannerString = scanner.nextLine();
        setScannerString(scannerString);
    }
    public Item popItemFromMap(String targetName){
        HashMap<String, Item> itemsMap = getItems();
        Item targetItem = new Item();
        if( itemsMap.containsKey(targetName) ){
            Item removed = itemsMap.remove(targetName);
            return removed;
        }
        return targetItem;
    }

//  ACCESSOR METHODS
    public Room getCurrentRoom() { return currentRoom;}
    public void setCurrentRoom(Room currentRoom) { this.currentRoom = currentRoom;}
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public Map<String, Room> getRooms() { return rooms; }
    public void setRooms(HashMap<String, Room> rooms) { this.rooms = rooms; }
    public Boolean getQuitGame() { return quitGame; }
    public void setQuitGame(Boolean quitGame) { this.quitGame = quitGame; }
    public String getScannerString() {return scannerString;}
    public void setScannerString(String scannerString) { this.scannerString = scannerString;}
    public HashMap<String, Encounter> getEncounters() { return encounters; }
    public void setEncounters(HashMap<String, Encounter> encounters) {this.encounters = encounters;}
    public HashMap<String, Item> getItems() { return items; }
    public void setItems(HashMap<String, Item> items) { this.items = items; }
}