package cmpt213.a4.view;

import cmpt213.a4.model.Consumable;
import cmpt213.a4.model.ConsumableFactory;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Dialog that handles the UI of adding a new Consumable
 */
public class AddMenu extends JDialog implements ActionListener, ItemListener {
    String[] consumableTypes = {"Food", "Drink"};

    private JComboBox typeBox;
    private JTextField nameText;
    private JTextField notesText;
    private JTextField priceText;
    private JTextField weightOrVolumeText;

    private JLabel typeLabel;
    private JLabel nameLabel;
    private JLabel notesLabel;
    private JLabel priceLabel;
    private JLabel weightOrVolumeLabel;
    private JLabel expiryDateLabel;

    private JPanel typePanel;
    private JPanel mainPanel;
    private JPanel namePanel;
    private JPanel notesPanel;
    private JPanel pricePanel;
    private JPanel weightOrVolumePanel;
    private JPanel expiryDatePanel;
    private JPanel bottomButtonsPanel;

    private JButton createButton;
    private JButton cancelButton;

    private DatePicker datePicker;
    private ConsumableFactory factory;
    private Consumable newConsumable = null; // new consumable created if user clicks the add button

    public AddMenu(Frame f) {
        super(f, "Add an Item", true);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createRigidArea(new Dimension(40, 20)));

        // Set form fields for the user to type into
        setTypeForm();
        setNameForm();
        setNotesForm();
        setPriceForm();
        setWeightOrVolumeForm();
        setExpiryDateForm();

        // Setting up the create and cancel buttons at the bottom of the dialog
        bottomButtonsPanel = new JPanel();
        bottomButtonsPanel.setLayout(new BoxLayout(bottomButtonsPanel, BoxLayout.X_AXIS));

        createButton = new JButton("Create");
        createButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        bottomButtonsPanel.add(createButton);
        bottomButtonsPanel.add(cancelButton);
        mainPanel.add(bottomButtonsPanel);

        this.add(mainPanel);
    }

    /**
     * Displays the AddMenu dialog and returns the Consumable added
     *
     * @return Returns a consumable added by user. Returns null if canceled
     */
    public Consumable run() {
        this.setPreferredSize(new Dimension(500, 400));
        pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
        return newConsumable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Create")) {

            String name = nameText.getText();
            String priceString = priceText.getText();
            String weightOrVolumeString = weightOrVolumeText.getText();

            JFrame f = new JFrame("Error");

            // Check if any of the text fields are empty (except for notes)
            if (name.equals("")) {
                JOptionPane.showMessageDialog(f, "Please enter a name");
            } else if (priceString.equals("")) {
                JOptionPane.showMessageDialog(f, "Please enter a price");
            } else if (weightOrVolumeString.equals("")) {
                JOptionPane.showMessageDialog(f,
                        "Please enter a " + weightOrVolumeLabel.getText().toLowerCase());

            } else {
                double price = Double.parseDouble(priceString);
                double weightOrVolume = Double.parseDouble(weightOrVolumeString);

                // Check if price or weight/volume is non-negative
                if (price < 0) {
                    JOptionPane.showMessageDialog(f, "Please enter a non-negative price");
                } else if (weightOrVolume < 0) {
                    JOptionPane.showMessageDialog(f,
                            "Please enter a " + weightOrVolumeLabel.getText().toLowerCase());

                } else {
                    String notes = notesText.getText();
                    LocalDate expiryDate = datePicker.getDate();

                    // Create the object if everything was valid
                    factory = new ConsumableFactory();
                    newConsumable = factory.getInstance(
                            Objects.requireNonNull(typeBox.getSelectedItem()).toString());
                    newConsumable.setName(name);
                    newConsumable.setNotes(notes);
                    newConsumable.setPrice(price);
                    newConsumable.setExtraDoubleField(weightOrVolume);
                    newConsumable.setExpiryDate(expiryDate);
                    dispose();
                }
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            dispose();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String selectedConsumable = Objects.requireNonNull(
                    typeBox.getSelectedItem()).toString();

            if (selectedConsumable.equals("Food")) {
                weightOrVolumeLabel.setText("Weight");
            } else if (selectedConsumable.equals("Drink"))
                weightOrVolumeLabel.setText("Volume");
        }
    }

    /**
     * Sets up the type form for the user to select
     */
    private void setTypeForm() {
        typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.X_AXIS));

        typeLabel = new JLabel("Type");
        typeBox = new JComboBox(consumableTypes);
        typeBox.setMaximumSize(new Dimension(300, typeBox.getPreferredSize().height));
        typeBox.addItemListener(this);
        typePanel.add(typeLabel);
        typePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        typePanel.add(typeBox);
        mainPanel.add(typePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(40, 20)));
    }

    /**
     * Sets up the name form for the user to type
     */
    private void setNameForm() {
        namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        nameLabel = new JLabel("Name");
        nameText = new JTextField();
        nameText.setMaximumSize(new Dimension(300, nameText.getPreferredSize().height));
        namePanel.add(nameLabel);
        namePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        namePanel.add(nameText);
        mainPanel.add(namePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(40, 20)));
    }

    /**
     * Sets up the notes form for the user to type
     */
    private void setNotesForm() {
        notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.X_AXIS));

        notesLabel = new JLabel("Notes");
        notesText = new JTextField();
        notesText.setMaximumSize(new Dimension(300, notesText.getPreferredSize().height));
        notesPanel.add(notesLabel);
        notesPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        notesPanel.add(notesText);
        mainPanel.add(notesPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(40, 20)));
    }

    /**
     * Sets up the price form for the user to type
     */
    private void setPriceForm() {
        pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.X_AXIS));

        priceLabel = new JLabel("Price");
        priceText = new JTextField();
        priceText.setMaximumSize(new Dimension(300, priceText.getPreferredSize().height));
        pricePanel.add(priceLabel);
        pricePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        pricePanel.add(priceText);
        mainPanel.add(pricePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(40, 20)));
    }

    /**
     * Sets up the weight/volume form for the user to select
     */
    private void setWeightOrVolumeForm() {
        weightOrVolumePanel = new JPanel();
        weightOrVolumePanel.setLayout(new BoxLayout(weightOrVolumePanel, BoxLayout.X_AXIS));

        weightOrVolumeLabel = new JLabel("Weight");
        weightOrVolumeText = new JTextField();
        weightOrVolumeText.setMaximumSize(new Dimension(
                300, weightOrVolumeText.getPreferredSize().height));
        weightOrVolumePanel.add(weightOrVolumeLabel);
        weightOrVolumePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        weightOrVolumePanel.add(weightOrVolumeText);
        mainPanel.add(weightOrVolumePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(40, 20)));
    }

    /**
     * Sets up the expiry date form for the user to select
     */
    private void setExpiryDateForm() {
        expiryDatePanel = new JPanel();
        expiryDatePanel.setLayout(new BoxLayout(expiryDatePanel, BoxLayout.X_AXIS));

        expiryDateLabel = new JLabel("Expiry Date");
        datePicker = new DatePicker();
        datePicker.setMaximumSize(new Dimension(300, datePicker.getPreferredSize().height));

        expiryDatePanel.add(expiryDateLabel);
        expiryDatePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        expiryDatePanel.add(datePicker);
        mainPanel.add(expiryDatePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(40, 20)));
    }
}
