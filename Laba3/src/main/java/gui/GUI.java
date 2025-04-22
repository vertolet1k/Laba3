/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import handler.*;
import model.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author vika
 */
public class GUI extends JFrame {
    private List<MonsterSource> sources;
    private JTree tree;
    private DefaultMutableTreeNode rootNode;
    private JPanel detailsPanel;
    private ImportHandler importHandler;
    private ExportHandler exportHandler;

    public GUI() {
        super("Bestiary Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        sources = new ArrayList<>();
        importHandler = createImportChain();
        exportHandler = createExportChain();

        rootNode = new DefaultMutableTreeNode("Bestiary");
        tree = new JTree(rootNode);
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(tree),
            new JScrollPane(detailsPanel)
        );
        splitPane.setDividerLocation(200);
        JToolBar toolBar = new JToolBar();
        JButton importButton = new JButton("Import File");
        JButton exportButton = new JButton("Export File");
        toolBar.add(importButton);
        toolBar.add(exportButton);

        importButton.addActionListener(e -> importFile());
        exportButton.addActionListener(e -> exportFile());
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();
            if (node != null && node.getUserObject() instanceof Monster) {
                showMonsterDetails((Monster) node.getUserObject());
            }
        });

        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private void importFile() {
        JFileChooser fileChooser = new JFileChooser();
        String labPath = System.getProperty("user.dir");
        File defaultDir = new File(labPath);
        if (defaultDir.exists()) {
            fileChooser.setCurrentDirectory(defaultDir);
        }
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || 
                       f.getName().toLowerCase().endsWith(".json") ||
                       f.getName().toLowerCase().endsWith(".xml") ||
                       f.getName().toLowerCase().endsWith(".yaml") ||
                       f.getName().toLowerCase().endsWith(".yml");
            }
            public String getDescription() {
                return "Bestiary Files (*.json, *.xml, *.yaml, *.yml)";
            }
        });
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                MonsterSource source = importHandler.importFile(file);
                if (source != null && source.getMonsters() != null) {
                    sources.add(source);
                    updateTree();
                    JOptionPane.showMessageDialog(this, "File imported successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No valid data found in the file",
                        "Import Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error importing file: " + ex.getMessage(), "Import Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void exportFile() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
            tree.getLastSelectedPathComponent();
        if (node == null || !(node.getUserObject() instanceof MonsterSource)) {
            JOptionPane.showMessageDialog(this, "Please select a source to export",
                "Export Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        String labPath = System.getProperty("user.dir");
        File defaultDir = new File(labPath);
        if (defaultDir.exists()) {
            fileChooser.setCurrentDirectory(defaultDir);
        }
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || 
                       f.getName().toLowerCase().endsWith(".json") ||
                       f.getName().toLowerCase().endsWith(".xml") ||
                       f.getName().toLowerCase().endsWith(".yaml") ||
                       f.getName().toLowerCase().endsWith(".yml");
            }
            public String getDescription() {
                return "Bestiary Files (*.json, *.xml, *.yaml, *.yml)";
            }
        });

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                MonsterSource source = (MonsterSource) node.getUserObject();
                if (!file.getName().contains(".")) {
                    file = new File(file.getPath() + ".xml");
                }
                if (exportHandler.exportFile(source, file)) {
                    JOptionPane.showMessageDialog(this, "File exported successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No handler found for this file type",
                        "Export Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting file: " + ex.getMessage(), "Export Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void updateTree() {
        rootNode.removeAllChildren();
        for (MonsterSource source : sources) {
            DefaultMutableTreeNode sourceNode = new DefaultMutableTreeNode(source);
            for (Monster monster : source.getMonsters()) {
                sourceNode.add(new DefaultMutableTreeNode(monster));
            }
            rootNode.add(sourceNode);
        }
        ((DefaultTreeModel) tree.getModel()).reload();
    }

    private ImportHandler createImportChain() {
        
        ImportHandler jsonHandler = new JsonImportHandler();
        ImportHandler xmlHandler = new XmlImportHandler();
        ImportHandler yamlHandler = new YamlImportHandler();
        jsonHandler.setNextHandler(xmlHandler);
        xmlHandler.setNextHandler(yamlHandler);
        
        return jsonHandler;
    }

    private ExportHandler createExportChain() {
        
        ExportHandler jsonHandler = new JsonExportHandler();
        ExportHandler xmlHandler = new XmlExportHandler();
        ExportHandler yamlHandler = new YamlExportHandler();
        jsonHandler.setNextHandler(xmlHandler);
        xmlHandler.setNextHandler(yamlHandler);
        
        return jsonHandler;
    }

    private void showMonsterDetails(Monster monster) {
        detailsPanel.removeAll();
        
        JTextField nameField = new JTextField(monster.getName());
        JTextArea descArea = new JTextArea(monster.getDescription(), 5, 30);
        descArea.setLineWrap(true);
        JTextField dangerField = new JTextField(String.valueOf(monster.getDangerLevel()));
        JTextField habitatField = new JTextField(monster.getHabitat());
        JTextField firstMentionField = new JTextField(monster.getFirstMention());
        JTextField heightField = new JTextField(monster.getHeight());
        JTextField weightField = new JTextField(monster.getWeight());
        JTextField activityField = new JTextField(monster.getActivity());
        JTextArea immunitiesArea = new JTextArea(
            monster.getImmunities() != null ? String.join(", ", monster.getImmunities()) : "", 2, 30);
        JTextArea vulnerabilitiesArea = new JTextArea(
            monster.getVulnerabilities() != null ? String.join(", ", monster.getVulnerabilities()) : "", 2, 30);
        JPanel recipePanel = new JPanel();
        recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
        if (monster.getRecipe() != null) {
            Monster.Recipe recipe = monster.getRecipe();
            JTextField typeField = new JTextField(recipe.getType());
            JTextArea ingredientsArea = new JTextArea(
                String.join("\n", recipe.getIngredients()), 3, 30);
            JTextField prepTimeField = new JTextField(recipe.getPreparationTime());
            JTextField effectivenessField = new JTextField(recipe.getEffectiveness());

            recipePanel.add(createLabeledComponent("Type:", typeField));
            recipePanel.add(createLabeledComponent("Ingredients:", new JScrollPane(ingredientsArea)));
            recipePanel.add(createLabeledComponent("Preparation Time:", prepTimeField));
            recipePanel.add(createLabeledComponent("Effectiveness:", effectivenessField));
        }

        detailsPanel.add(createLabeledComponent("Name:", nameField));
        detailsPanel.add(createLabeledComponent("Description:", new JScrollPane(descArea)));
        detailsPanel.add(createLabeledComponent("Danger Level:", dangerField));
        detailsPanel.add(createLabeledComponent("Habitat:", habitatField));
        detailsPanel.add(createLabeledComponent("First Mention:", firstMentionField));
        detailsPanel.add(createLabeledComponent("Height:", heightField));
        detailsPanel.add(createLabeledComponent("Weight:", weightField));
        detailsPanel.add(createLabeledComponent("Activity:", activityField));
        detailsPanel.add(createLabeledComponent("Immunities:", new JScrollPane(immunitiesArea)));
        detailsPanel.add(createLabeledComponent("Vulnerabilities:", new JScrollPane(vulnerabilitiesArea)));
        detailsPanel.add(createLabeledComponent("Recipe:", recipePanel));

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            try {
                monster.setName(nameField.getText());
                monster.setDescription(descArea.getText());
                monster.setDangerLevel(Integer.parseInt(dangerField.getText()));
                monster.setHabitat(habitatField.getText());
                monster.setFirstMention(firstMentionField.getText());
                monster.setHeight(heightField.getText());
                monster.setWeight(weightField.getText());
                monster.setActivity(activityField.getText());
                monster.setImmunities(List.of(immunitiesArea.getText().split("\\s*,\\s*")));
                monster.setVulnerabilities(List.of(vulnerabilitiesArea.getText().split("\\s*,\\s*")));

                if (monster.getRecipe() != null) {
                    Monster.Recipe recipe = monster.getRecipe();
                    recipe.setType(((JTextField)((JPanel)recipePanel.getComponent(0))
                        .getComponent(1)).getText());
                    recipe.setIngredients(List.of(((JTextArea)((JScrollPane)((JPanel)recipePanel
                        .getComponent(1)).getComponent(1)).getViewport().getView()).getText()
                        .split("\n")));
                    recipe.setPreparationTime(((JTextField)((JPanel)recipePanel.getComponent(2))
                        .getComponent(1)).getText());
                    recipe.setEffectiveness(((JTextField)((JPanel)recipePanel.getComponent(3))
                        .getComponent(1)).getText());
                }

                updateTree();
                JOptionPane.showMessageDialog(this, "Changes saved successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for numeric fields",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        detailsPanel.add(saveButton);
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }

    private JPanel createLabeledComponent(String label, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI app = new GUI();
            app.setVisible(true);
        });
    }
} 