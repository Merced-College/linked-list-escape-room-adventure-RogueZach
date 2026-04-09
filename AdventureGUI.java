import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class AdventureGUI extends JFrame {
    private SceneLinkedList scenes;
    private Player player;
    private Scene currentScene;
    private String projectRoot;

    private JLabel titleLabel;
    private JLabel imageLabel;
    private JTextArea descArea;
    private JTextArea inventoryArea;
    private JPanel choicePanel;

    public AdventureGUI() {
        // Determine project root directory
        try {
            projectRoot = System.getProperty("user.dir");
        } catch (Exception e) {
            projectRoot = "";
        }
        
        scenes = GameLoader.loadScenes("data/scenes.csv");
        player = new Player();
        currentScene = scenes.findSceneById(1);

        initializeGUI();
        updateDisplay();
    }

    private void initializeGUI() {
        setTitle("Escape Room Adventure");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Title
        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Choice panel at the top (before the image)
        choicePanel = new JPanel();
        choicePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        choicePanel.setBackground(Color.WHITE);
        choicePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Center panel with image
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        // Image panel
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        centerPanel.add(imageLabel, BorderLayout.CENTER);

        // Bottom panel for description and inventory
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        descArea = new JTextArea();
        descArea.setEditable(false);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descArea.setRows(2);
        descArea.setBackground(new Color(240, 240, 240));
        JScrollPane descScroll = new JScrollPane(descArea);
        bottomPanel.add(descScroll, BorderLayout.CENTER);

        inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        inventoryArea.setFont(new Font("Arial", Font.PLAIN, 11));
        inventoryArea.setRows(2);
        inventoryArea.setBackground(new Color(240, 240, 240));
        JScrollPane invScroll = new JScrollPane(inventoryArea);
        bottomPanel.add(invScroll, BorderLayout.SOUTH);

        // Wrapper to include buttons at the top
        JPanel mainWrapper = new JPanel(new BorderLayout());
        mainWrapper.setBackground(Color.WHITE);
        mainWrapper.add(choicePanel, BorderLayout.NORTH);
        mainWrapper.add(centerPanel, BorderLayout.CENTER);

        add(mainWrapper, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateDisplay() {
        titleLabel.setText(currentScene.getTitle());
        descArea.setText(currentScene.getDescription());
        inventoryArea.setText(player.getInventoryText());

        // Load and display image
        loadAndDisplayImage();

        // Clear and populate choice panel with buttons at the top
        choicePanel.removeAll();

        // Add choices as buttons horizontally
        java.util.List<Choice> choices = currentScene.getChoices();
        for (int i = 0; i < choices.size(); i++) {
            Choice choice = choices.get(i);
            JButton btn = new JButton(choice.getText());
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int nextId = choice.getNextSceneId();
                    currentScene = scenes.findSceneById(nextId);
                    updateDisplay();
                }
            });
            choicePanel.add(btn);
        }

        // Add item pickup button if applicable
        if (currentScene.getItem() != null) {
            JButton pickupBtn = new JButton("Pick up " + currentScene.getItem().getName());
            pickupBtn.setFont(new Font("Arial", Font.PLAIN, 12));
            pickupBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    player.addItem(currentScene.getItem());
                    currentScene.removeItem();
                    updateDisplay();
                }
            });
            choicePanel.add(pickupBtn);
        }

        if (currentScene.getSceneId() == 5) {
            handleFinalRoom();
        }

        choicePanel.revalidate();
        choicePanel.repaint();
    }

    private void loadAndDisplayImage() {
        String imageName = getImageNameForScene();
        
        // Try multiple path variations
        String[] possiblePaths = {
            projectRoot + "/images2/" + imageName + ".png",
            "images2/" + imageName + ".png",
            "./images2/" + imageName + ".png",
        };

        BufferedImage img = null;

        for (String path : possiblePaths) {
            try {
                File imageFile = new File(path);
                if (imageFile.exists()) {
                    img = ImageIO.read(imageFile);
                    if (img != null) {
                        System.out.println("Image loaded from: " + path);
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to load from " + path + ": " + e.getMessage());
            }
        }

        if (img != null) {
            // Scale image to fit the display
            int maxWidth = 400;
            int maxHeight = 300;
            int width = img.getWidth();
            int height = img.getHeight();

            // Calculate scaling to maintain aspect ratio
            if (width > maxWidth || height > maxHeight) {
                double scaleX = (double) maxWidth / width;
                double scaleY = (double) maxHeight / height;
                double scale = Math.min(scaleX, scaleY);
                width = (int) (width * scale);
                height = (int) (height * scale);

                Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
            } else {
                imageLabel.setIcon(new ImageIcon(img));
            }
        } else {
            imageLabel.setText("Image not found for: " + imageName);
            System.out.println("Failed to load image: " + imageName);
            System.out.println("Project root: " + projectRoot);
        }
    }

    private String getImageNameForScene() {
        // Map scene titles to image names
        String title = currentScene.getTitle().toLowerCase();
        
        if (title.contains("lobby")) {
            return "lobby";
        } else if (title.contains("storage")) {
            return "storage";
        } else if (title.contains("security") || title.contains("office")) {
            return "office";
        } else if (title.contains("hallway")) {
            return "hallway";
        } else if (title.contains("exit") || title.contains("door")) {
            return "exit";
        }
        
        // Fallback: use scene ID
        return "lobby";
    }

    private void handleFinalRoom() {
        boolean hasKeycard = player.hasItem("Keycard");
        boolean hasCodeNote = player.hasItem("Code Note");

        String message;
        if (hasKeycard && hasCodeNote) {
            message = "You used the Keycard and the Code Note to unlock the exit.\nYou escaped. You win!";
        } else {
            message = "The exit will not open.\nYou are missing the required items.\nTo win, you need: Keycard and Code Note.";
        }

        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        // Optionally, disable buttons or exit
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdventureGUI());
    }
}