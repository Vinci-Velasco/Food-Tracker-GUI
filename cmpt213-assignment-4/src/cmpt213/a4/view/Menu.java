package cmpt213.a4.view;

import cmpt213.a4.control.ConsumableController;
import cmpt213.a4.model.Consumable;
import cmpt213.a4.model.Food;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * Main UI of the system. Displays 4 pages of consumable items
 */
public class Menu implements ActionListener, WindowListener {
    private JFrame applicationFrame;
    private ConsumableController controller = new ConsumableController();

    private JButton allItemsButton;
    private JButton expiredButton;
    private JButton nonExpiredButton;
    private JButton expiringWithin7DaysButton;
    private JButton addItemButton;

    private JPanel mainPanel;
    private JPanel topButtonsPanel;
    private JPanel itemsPanel; // Changes depending on what page the user clicks on

    private JScrollPane itemScrollPane;
    private JLabel emptyListLabel;

    // Determines what is the current page (1 - 4)
    // important for when page needs to be updated from an item being deleted or added,
    // as it determines what list needs to be displayed
    private int currentPage = 1;

    // Keeps track of all the delete buttons for each item
    // Purpose: able to determine which item to delete using the index of this arraylist
    // Ex: button is clicked which is in index 3 of this arraylist, therefore delete 3rd
    //     item stored in controller object.
    private ArrayList<JButton> deleteButtons = new ArrayList<>();

    public Menu() {
        applicationFrame = new JFrame("Consumable Tracker");
        applicationFrame.setSize(500, 600);
        applicationFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        applicationFrame.addWindowListener(this);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        topButtonsPanel = new JPanel();
        topButtonsPanel.setLayout(new BoxLayout(topButtonsPanel, BoxLayout.X_AXIS));

        allItemsButton = new JButton("All");
        allItemsButton.addActionListener(this);
        allItemsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        allItemsButton.setBackground(Color.gray);
        allItemsButton.setFocusPainted(false);

        expiredButton = new JButton("Expired");
        expiredButton.addActionListener(this);
        expiredButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        expiredButton.setFocusPainted(false);

        nonExpiredButton = new JButton("Not Expired");
        nonExpiredButton.addActionListener(this);
        nonExpiredButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nonExpiredButton.setFocusPainted(false);

        expiringWithin7DaysButton = new JButton("Expiring in 7 Days");
        expiringWithin7DaysButton.addActionListener(this);
        expiringWithin7DaysButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        expiringWithin7DaysButton.setFocusPainted(false);

        topButtonsPanel.add(allItemsButton);
        topButtonsPanel.add(expiredButton);
        topButtonsPanel.add(nonExpiredButton);
        topButtonsPanel.add(expiringWithin7DaysButton);

        mainPanel.add(topButtonsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(20, 20)));

        emptyListLabel = new JLabel("");
        emptyListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(emptyListLabel);

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        displayItems();

        itemScrollPane = new JScrollPane(itemsPanel);
        mainPanel.add(itemScrollPane);

        addItemButton = new JButton("Add Item");
        addItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addItemButton.setFocusPainted(false);
        addItemButton.addActionListener(this);
        mainPanel.add(addItemButton);

        applicationFrame.add(mainPanel);
        applicationFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("All")) {
            currentPage = 1;

            allItemsButton.setBackground(Color.gray);
            expiredButton.setBackground(new JButton().getBackground());
            nonExpiredButton.setBackground(new JButton().getBackground());
            expiringWithin7DaysButton.setBackground(new JButton().getBackground());

        } else if (e.getActionCommand().equals("Expired")) {
            currentPage = 2;

            expiredButton.setBackground(Color.gray);
            allItemsButton.setBackground(new JButton().getBackground());
            nonExpiredButton.setBackground(new JButton().getBackground());
            expiringWithin7DaysButton.setBackground(new JButton().getBackground());

        } else if (e.getActionCommand().equals("Not Expired")) {
            currentPage = 3;

            nonExpiredButton.setBackground(Color.gray);
            allItemsButton.setBackground(new JButton().getBackground());
            expiredButton.setBackground(new JButton().getBackground());
            expiringWithin7DaysButton.setBackground(new JButton().getBackground());

        } else if (e.getActionCommand().equals("Expiring in 7 Days")) {
            currentPage = 4;

            expiringWithin7DaysButton.setBackground(Color.gray);
            allItemsButton.setBackground(new JButton().getBackground());
            expiredButton.setBackground(new JButton().getBackground());
            nonExpiredButton.setBackground(new JButton().getBackground());

        } else if (e.getActionCommand().equals("Delete")) {
            JButton selectedButton = (JButton) e.getSource();

            // Figure out what index that button is in, then delete the item in that same position
            // stored in the controller
            for (int i = 0; i < deleteButtons.size(); i++) {

                if (selectedButton == deleteButtons.get(i)) {
                    controller.deleteItem(i);
                    break;
                }
            }
        } else if (e.getActionCommand().equals("Add Item")) {
            JFrame f = new JFrame();
            AddMenu addMenu = new AddMenu(f);
            addMenu.setSize(400, 400);
            Consumable newConsumable = addMenu.run();

            if (newConsumable != null) {
                controller.addItem(newConsumable);
            }
        }

        displayItems();
        SwingUtilities.updateComponentTreeUI(applicationFrame);
    }

    /**
     * Reads items from the controller and adds them into the itemsPane, displaying
     * it on the screen. What items gotten from the controller depends on the current page.
     * 1 = all items, 2 = expired items, 3 = non-expired items, 4 = items expiring in 7 days
     */
    private void displayItems() {

        itemsPanel.removeAll();
        deleteButtons.clear();

        String noItemsText = "";
        ArrayList<Consumable> allItems;

        // Determine how to update the displayed list by using what the current page is
        switch (currentPage) {
            case 1 -> {
                allItems = controller.getItems();
                noItemsText = "No items to show.";
            }
            case 2 -> {
                allItems = controller.getExpiredItems();
                noItemsText = "No expired items to show.";
            }
            case 3 -> {
                allItems = controller.getNonExpiredItems();
                noItemsText = "No non-expired items to show.";
            }
            case 4 -> {
                allItems = controller.getItemsExpiringWithin7Days();
                noItemsText = "No items expiring in 7 days to show.";
            }

            // should never happen
            default -> allItems = new ArrayList<>();
        }

        if (allItems.isEmpty()) {
            emptyListLabel.setText(noItemsText);

        } else {
            emptyListLabel.setText("");

            for (int i = 0; i < allItems.size(); i++) {
                JLabel title;
                int itemNum = i + 1;

                if (allItems.get(i) instanceof Food) {
                    title = new JLabel("Item #" + itemNum + " (Food)");
                } else {
                    title = new JLabel("Item #" + itemNum + " (Drink)");
                }

                JPanel individualItemPanel = new JPanel();
                individualItemPanel.setLayout(new BoxLayout(
                        individualItemPanel, BoxLayout.Y_AXIS));
                individualItemPanel.setBorder(BorderFactory.createLineBorder(Color.black));

                title.setAlignmentX(Component.CENTER_ALIGNMENT);
                title.setFont(new Font("Serif", Font.PLAIN, 20));
                individualItemPanel.add(title);
                individualItemPanel.add(Box.createRigidArea(new Dimension(20, 20)));

                JLabel itemDescription = new JLabel(allItems.get(i).toString());
                itemDescription.setFont(new Font("Serif", Font.PLAIN, 14));
                itemDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemDescription.setHorizontalAlignment(SwingConstants.CENTER);
                itemDescription.setVerticalAlignment(SwingConstants.CENTER);
                individualItemPanel.add(itemDescription);

                individualItemPanel.add(Box.createRigidArea(new Dimension(20, 20)));
                JButton deleteButton = new JButton("Delete");
                deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                deleteButton.addActionListener(this);
                deleteButtons.add(deleteButton);
                individualItemPanel.add(deleteButton);
                individualItemPanel.add(Box.createRigidArea(new Dimension(20, 40)));

                itemsPanel.add(individualItemPanel);

            }
        }
    }

    // Window listeners. Only windowClosing() is important here
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        // Save items to json file before closing
        controller.saveToJsonFile();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
