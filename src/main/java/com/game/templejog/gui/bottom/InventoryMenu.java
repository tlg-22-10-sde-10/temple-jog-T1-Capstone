package com.game.templejog.gui.bottom;

import com.game.templejog.Game;
import com.game.templejog.Item;

import javax.swing.*;

public class InventoryMenu {
    public static void setUpInventoryDisplay(Game game) {
        StringBuilder line = new StringBuilder("Inventory:\n");
        for (Item item : game.getPlayer().getInventory()) {
            line.append(String.format("%s\n", item.getName()));
        }
        JFrame inventoryFrame = new JFrame("Inventory");
        inventoryFrame.setSize(250, 250);
        inventoryFrame.setLocationRelativeTo(null);
        JTextArea inventoryMessage = new JTextArea(String.valueOf(line));
        inventoryMessage.setEditable(false);
        JOptionPane.showMessageDialog(inventoryFrame, inventoryMessage);
    }
}
