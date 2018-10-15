package Main;

import Main.utils.Constants;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import ui.events.RequirementTreeMouseListener;
import ui.jtree.DiscussionType;
import ui.jtree.OptionScore;
import ui.jtree.RequirementAction;
import ui.jtree.RequirementTreeCellRenderer;
import ui.jtree.RequirementTreeModel;
import ui.popups.RootTreePopup;
import ui.popups.StatementTreePopup;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainGUI {

    public static JFrame frame;
    public static JTree tree;
    public static DefaultMutableTreeNode selectedNode;
    private static String currentOntologyPath = "";
    JTextArea textArea = new JTextArea();
    JLabel conceptLbl = new JLabel();
    JTextField newProjectLocation = new JTextField();

    public static boolean isAllMatched(Set<OWLNamedClass> matchedConcepts) {
        if (ConceptManager.isInitialized() == false)
            return false;
        List<String> allConceptsArry = ConceptManager.getAllConcepts();
        Set<String> matchedConceptsString = new HashSet<String>();

        for (OWLNamedClass owlConcept : matchedConcepts) {
            matchedConceptsString.add(owlConcept.getBrowserText());

        }

        DefaultMutableTreeNode discRoot = (DefaultMutableTreeNode) tree.getModel().getRoot();
        for (int i = 0; i < discRoot.getChildCount(); i++) {
            DefaultMutableTreeNode statementNode = (DefaultMutableTreeNode) discRoot.getChildAt(i);
            RequirementAction statementAction = (RequirementAction) statementNode.getUserObject();
            Set<OWLNamedClass> onlineMatchedConcepts = ConceptManager.extractConcepts(statementAction.getName());

            for (OWLNamedClass owlConcept : onlineMatchedConcepts) {
                matchedConceptsString.add(owlConcept.getBrowserText());

            }

        }

        if (matchedConceptsString.size() == allConceptsArry.size()) {
            return true;
        }
        return false;

    }

    public static boolean isAllMatched(List<String> notMatchedConcepts) {
        if (ConceptManager.isInitialized() == false)
            return false;
        List<String> allConceptsArry = ConceptManager.getAllConcepts();
        Set<String> matchedConceptsString = new HashSet<String>();
        DefaultMutableTreeNode discRoot = (DefaultMutableTreeNode) tree.getModel().getRoot();
        for (int i = 0; i < discRoot.getChildCount(); i++) {
            DefaultMutableTreeNode statementNode = (DefaultMutableTreeNode) discRoot.getChildAt(i);
            RequirementAction statementAction = (RequirementAction) statementNode.getUserObject();
            Set<OWLNamedClass> matchedConcepts = ConceptManager.extractConcepts(statementAction.getName());

            for (OWLNamedClass owlConcept : matchedConcepts) {
                matchedConceptsString.add(owlConcept.getBrowserText());

            }

        }
        if (matchedConceptsString.size() == allConceptsArry.size()) {
            return true;
        } else {
            List<String> notMatched = allConceptsArry.stream()
                    .filter(concept -> !matchedConceptsString.contains(concept)).collect(Collectors.toList());
            notMatchedConcepts.addAll(notMatched);
            return false;
        }
    }

    public void serializeToFile(Object o, String filePath) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(o);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in" + filePath);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public Object deserializeFromFile(String path) {
        Object o = null;
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            // nodes = (List<Node>) in.readObject();
            o = in.readObject();

            in.close();
            fileIn.close();
            return o;
        } catch (IOException i) {
            i.printStackTrace();

        } catch (ClassNotFoundException c) {
            System.out.println(" not found");
            c.printStackTrace();

        }

        return o;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainGUI window = new MainGUI();
                    window.frame.setVisible(true);
                    try {
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                        UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Calibri", Font.PLAIN, 16));
                        setUIFont(new javax.swing.plaf.FontUIResource("Calibri", Font.PLAIN, 14));
                        UIManager.put("swing.boldMetal", Boolean.FALSE);
                        Dimension size = UIManager.getDimension("OptionPane.minimumSize");
                        // size.height = 180;
                        size.width = 480;
                        System.out.println(size);
                        UIManager.put("OptionPane.minimumSize", size);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    /**
     * Create the application.
     */
    public MainGUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private static String getTreeText(TreeModel model, Object object, String indent) {
        String myRow = indent + object + "\n";
        for (int i = 0; i < model.getChildCount(object); i++) {
            myRow += getTreeText(model, model.getChild(object, i), indent + "  ");
        }
        return myRow;
    }

    private void initialize() {
        frame = new JFrame("Requirement Gathering App");
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu projectMenu = new JMenu("Project");
        JMenuItem mntmOpenProject = new JMenuItem("Open Project");
        mntmOpenProject.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub

                File outputfile = null;
                JFileChooser fileChooser = new JFileChooser();
                System.out.println("file chooser");
                if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    // File file = fileChooser.getSelectedFile();
                    outputfile = fileChooser.getSelectedFile();
                    // load from file

                }
                if (outputfile != null) {
                    System.out.println(outputfile.getPath());
                    // nodes = (List<Node>) deserializeFromFile("resources/nodes.ser");
                    System.out.println("file chooser2");
                    // TreeModel t = (TreeModel) deserializeFromFile(outputfile.getAbsolutePath());
                    RequirementTreeModel rtm = (RequirementTreeModel) deserializeFromFile(outputfile.getAbsolutePath());
                    TreeModel t = rtm.getTreeModel();
                    currentOntologyPath = rtm.getCurrentOntologyPath();
                    ConceptManager.init(currentOntologyPath);
                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) t.getRoot();
                    System.out.println("root: " + root);
                    Enumeration e = root.preorderEnumeration();
                    while (e.hasMoreElements()) {
                        System.out.println(" " + e.nextElement());
                    }
                    tree.setModel(t);

                    JOptionPane.showMessageDialog(null, "Project loaded successfully");
                }

            }
        });

        JMenuItem mntmSaveProject = new JMenuItem("Save Project");
        mntmSaveProject.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                JFileChooser fileChooser = new JFileChooser();
                File file = null;
                if (fileChooser.showSaveDialog(MainGUI.frame) == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();

                    System.out.println(file.getPath());
                    // save to file
                }
                if (file == null)
                    return;
                System.out.println("root: " + tree.getRowCount());
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
                System.out.println("root: " + root.toString());
                Enumeration e = root.preorderEnumeration();
                while (e.hasMoreElements()) {
                    System.out.println(" " + e.nextElement());
                }
                RequirementTreeModel rtm = new RequirementTreeModel(tree.getModel(), currentOntologyPath);
                serializeToFile(rtm, file.getPath());
                System.out.println(getTreeText(tree.getModel(), tree.getModel().getRoot(), ""));
                JOptionPane.showMessageDialog(null, "Saved");
            }
        });

        JMenuItem mntmNewProject = new JMenuItem("Create New Project");

        newProjectLocation.setMargin(new Insets(10, 0, 0, 10));
        JComboBox<String> newProjectOntology = new JComboBox();
        newProjectOntology.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        File folder = new File("resources/ontologies");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                newProjectOntology.addItem(listOfFiles[i].getName());

            }
        }
        Object[] message = {"Project Name:", newProjectLocation, "Project Domain:", newProjectOntology};

        mntmNewProject.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int option = JOptionPane.showConfirmDialog(null, message, "New Project", JOptionPane.OK_CANCEL_OPTION);

                if (option == 0) {
                    currentOntologyPath = listOfFiles[newProjectOntology.getSelectedIndex()].getPath();
                    ConceptManager.init(currentOntologyPath);

                    List<String> allConcepts = ConceptManager.getAllConcepts();
                    JOptionPane.showMessageDialog(null, "Ontology Concepts: " + System.lineSeparator() + allConcepts);
                    conceptLbl.setText("Ontology Concepts: " + System.lineSeparator() + allConcepts);
                    // textArea.setText("Ontology Concepts: " + System.lineSeparator() +
                    // allConcepts);
                    DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) MainGUI.tree.getModel().getRoot();
                    rootNode.removeAllChildren();
                    RequirementAction r = (RequirementAction) rootNode.getUserObject();
                    r.setName(newProjectLocation.getText());
                    DefaultTreeModel t = (DefaultTreeModel) MainGUI.tree.getModel();
                    t.reload();
                    MainGUI.expandAllNodes(MainGUI.tree, 0, MainGUI.tree.getRowCount());
                    JOptionPane.showMessageDialog(null, "Project Created");

                }
            }
        });
        JMenuItem mntmGenerateDoc = new JMenuItem("Generate Requirements Document");
        mntmGenerateDoc.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("Doc: ");
                List<String> notMatched = new ArrayList<String>();
                if (!isAllMatched(notMatched)) {
                    System.out.println("Not Matched !: " + notMatched);
                    JOptionPane.showMessageDialog(null,
                            "The requirements list is not complete; some requirements are missing. Specfically, "
                                    + notMatched);
                }
                JFileChooser fileChooser = new JFileChooser();
                File file = null;
                if (fileChooser.showSaveDialog(MainGUI.frame) == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    System.out.println(file.getPath());
                    // save to file
                } else {
                    return;
                }
                try {
                    PrintWriter writer = new PrintWriter(file.getPath() + ".txt", "UTF-8");
                    PrintWriter writerFull = new PrintWriter(file.getPath() + "_full.txt", "UTF-8");
                    DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) MainGUI.tree.getModel().getRoot();
                    RequirementAction r = (RequirementAction) rootNode.getUserObject();

                    String projectName = r.getName();
                    writer.append("Project: " + projectName + System.lineSeparator());
                    writerFull.append("Project: " + projectName + System.lineSeparator());

                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
                    int k = 0;
                    for (int i = 0; i < root.getChildCount(); i++) {
                        DefaultMutableTreeNode statementNode = (DefaultMutableTreeNode) root.getChildAt(i);
                        RequirementAction statementAction = (RequirementAction) statementNode.getUserObject();
                        System.out.println("Statment to Generate: " + statementAction.getName());

                        HashMap<String, List<OptionScore>> questionOptionScore = StatementTreePopup
                                .printNode(statementNode);
                        // System.out.println(questionOptionScore);

                        // PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
                        // System.out.println("file path: " + file.getPath());
                        if (statementAction.isChecked()) {
                            writerFull.append((i + 1) + ". " + statementAction.getName() + System.lineSeparator());
                        }
                        for (String question : questionOptionScore.keySet()) {
                            if (statementAction.isChecked()) {
                                writerFull.append("\t " + question + System.lineSeparator());
                            }
                            List<OptionScore> questionOptions = questionOptionScore.get(question);
                            for (OptionScore os : questionOptions) {
                                RequirementAction ra = (RequirementAction) os.getOptionNode().getUserObject();
                                if (statementAction.isChecked() && ra.isChecked()) {
                                    // writer.append("\t \t Option: " + os.getOption() + "( score: " + os.getScore()
                                    // + " )" + System.lineSeparator());

                                    if (ra.getAgreedRequirement() != null && ra.getAgreedRequirement() != "") {
                                        writerFull.append("\t \t " + os.getOption() + " (" + ra.getAgreedRequirement() + ")" + System.lineSeparator());
                                        DefaultMutableTreeNode optionNode = (DefaultMutableTreeNode) os.getOptionNode();
                                        for (int j = 0; j < optionNode.getChildCount(); j++) {
                                            DefaultMutableTreeNode criiteriaNode = (DefaultMutableTreeNode) optionNode.getChildAt(j);
                                            RequirementAction CriteriaAction = (RequirementAction) criiteriaNode.getUserObject();
                                            if (CriteriaAction.getDiscussionType().equals(DiscussionType.POSITIVE_CRITERIA)) {
                                                writerFull.append("\t \t \t " + CriteriaAction.getName() + System.lineSeparator());
                                            } else {
                                                writerFull.append("\t \t \t " + CriteriaAction.getName() + System.lineSeparator());
                                            }
                                        }
                                    }
                                }
                                writer.append(String.valueOf(++k)).append(". ");
                                writer.append(ra.getAgreedRequirement() + System.lineSeparator());
                            }

                        }

                    }
                    writer.close();
                    writerFull.close();
                    JOptionPane.showMessageDialog(null, "Document Generated");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        JMenuItem mntmCloseApp = new JMenuItem("Close");
        mntmCloseApp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                MainGUI.this.frame.dispatchEvent(new WindowEvent(MainGUI.this.frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        projectMenu.add(mntmNewProject);
        projectMenu.add(mntmOpenProject);
        projectMenu.add(mntmSaveProject);
        projectMenu.add(mntmGenerateDoc);
        projectMenu.add(mntmCloseApp);

        JMenu settingMenu = new JMenu("Setting");
        menuBar.add(projectMenu);
        frame.setJMenuBar(menuBar);
        JMenuItem mntmQueryScreen = new JMenuItem("Knowledge Query");
        menuBar.add(mntmQueryScreen);
        mntmQueryScreen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!ConceptManager.isInitialized()) {

                    JOptionPane.showMessageDialog(null,
                            "Please Choose Valid Ontology First from Project -> Create New Project");
                    return;
                }

                JPanel panel = new JPanel();
                Object[] options1 = {"Run", "Close"};
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                JPanel labelPanel = new JPanel();
                labelPanel.setLayout(new GridLayout());
                System.out.println("here: ");

                String[] allConceptsArry = ConceptManager.getAllConcepts()
                        .toArray(new String[ConceptManager.getAllConcepts().size()]);
                System.out.println(ConceptManager.getAllConcepts());
                JComboBox<String> allConcepts = new JComboBox<>(allConceptsArry);
                JLabel allConceptsLbl = new JLabel("Concept: ");

                // allConcepts.item
                labelPanel.add(allConceptsLbl);
                labelPanel.add(allConcepts);
                JTextArea matched = new JTextArea(10, 70);
                matched.setEditable(false);
                matched.setFont(matched.getFont().deriveFont(14f));
                panel.add(labelPanel);
                panel.add(new JSeparator());
                panel.add(new JSeparator());
                panel.add(matched);

                boolean isDoneQuerying = false;
                while (!isDoneQuerying) {
                    int result = JOptionPane.showOptionDialog(null, panel, "Knowldge Reuse",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options1, null);
                    matched.setText("");
                    if (result == 0) {
                        System.out.println("selected is: " + allConcepts.getSelectedItem().toString());
                        String selectedConcept = allConcepts.getSelectedItem().toString();
                        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
                        int count = 0;
                        for (int i = 0; i < root.getChildCount(); i++) {
                            DefaultMutableTreeNode statementNode = (DefaultMutableTreeNode) root.getChildAt(i);
                            RequirementAction action = (RequirementAction) statementNode.getUserObject();
                            Set<OWLNamedClass> matchedConcepts = ConceptManager.extractConcepts(action.getName());
                            System.out.println("State: " + action.getName());
                            Set<String> matchedConceptsString = new HashSet<String>();
                            for (OWLNamedClass owlConcept : matchedConcepts) {
                                matchedConceptsString.add(owlConcept.getBrowserText());

                            }
                            System.out.println("Matched are: " + action.getName());

                            if (matchedConceptsString.contains(selectedConcept)) {
                                System.out.println("done: " + action.getName());
                                matched.append(++count + ". " + action.getName() + System.lineSeparator());
                            }

                        }

                    } else {
                        isDoneQuerying = true;
                    }
                }
            }
        });

        RequirementAction rootAction = new RequirementAction(
                Constants.WELCOME_LABEL, null, DiscussionType.ROOT);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootAction);

        tree = new JTree(root);

        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setFont(new Font("Calibri", Font.PLAIN, 14));
        tree.setExpandsSelectedPaths(true);
        final RootTreePopup treePopup = new RootTreePopup(tree);
        RequirementTreeMouseListener reqListener = new RequirementTreeMouseListener(tree, root, treePopup);
        tree.addMouseListener(reqListener);
        tree.setCellRenderer(new RequirementTreeCellRenderer());

        JScrollPane pane = new JScrollPane(tree);
        pane.setPreferredSize(new Dimension(400, 500));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(pane, BorderLayout.CENTER);

        JMenuBar menuBar_1 = new JMenuBar();
        pane.setColumnHeaderView(menuBar_1);

        menuBar_1.add(conceptLbl);


    }

    public static void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
        for (int i = startingIndex; i < rowCount; ++i) {
            tree.expandRow(i);
        }

        if (tree.getRowCount() != rowCount) {
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

}
