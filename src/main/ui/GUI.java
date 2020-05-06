package ui;

import exceptions.InvalidAmountException;
import model.ProductEntry;
import model.ProductList;


import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI {
    private GUI instance;

    private String filePath;
    private String warehouseName;
    private ProductList warehouse;

    private JFrame frame; //field for the application window
    private DefaultTableModel model; //the "back end" of the table
    private JTable table; //the "front end" of the table

    private JSplitPane mainPane;
    private JPanel tableDisplay;
    private JPanel searchPanel;


    //EFFECTS: Constructs a new GUI depending on the warehouse
    public GUI(String warehouseName) {
        this.warehouseName = warehouseName;
        filePath = "./data/" + warehouseName;
        try {
            warehouse = JsonReader.readWarehouse(filePath);
            makeGUI();
            instance = this;
        } catch (Exception e) {
            new ErrorWindow("Warehouse Does not Exist!");
        }
    }

    //EFFECTS: Makes the GUI
    private void makeGUI() {
        initFrame(); //make the actual application window itself
        table = initTable();
        model = (DefaultTableModel) table.getModel();
        initSearchBar();
        initHomeButtons();
        frame.setVisible(true);
    }


    //EFFECTS: Constructs a 800x600 pixel application window that is the main frame of the application
    private void initFrame() {
        frame = new JFrame(warehouseName);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); //this initializes the frame at the center of the screen for some reason

        mainPane = new JSplitPane();
        frame.getContentPane().add(mainPane);

        mainPane.setOrientation(JSplitPane.VERTICAL_SPLIT); //Splits the rows I guess xD
        mainPane.setDividerLocation(450);

        JPanel searchAndTablePanel = new JPanel();
        searchAndTablePanel.setLayout(new BoxLayout(searchAndTablePanel, BoxLayout.Y_AXIS));

        tableDisplay = new JPanel();
        searchPanel = new JPanel();
        //following 2 lines of code are for testing purposes only
        searchPanel.setPreferredSize(new Dimension(1000, 50));
        searchPanel.setBackground(Color.RED);


        searchAndTablePanel.add(searchPanel);
        searchAndTablePanel.add(tableDisplay);

        mainPane.setTopComponent(searchAndTablePanel);

        frame.setContentPane(mainPane);
        frame.repaint();
        frame.revalidate();
    }

    //EFFECTS: Initializes the search Bar
    private void initSearchBar() {
        JComboBox cb = new JComboBox(new String[]{"Name", "Type", "Price Range", "Cost Range",
                "Stock", "Sold", "Profit"});
        cb.setEditable(false);
        JPanel searchArea = new JPanel(new BorderLayout());
        searchArea.setPreferredSize(new Dimension(500, 30));
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(80, 30));

        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeSearchMethod((String) cb.getSelectedItem(), searchArea);
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSearch((String) cb.getSelectedItem(), cb.getSelectedIndex(), searchArea);
            }
        });
        cb.setSelectedIndex(0);
        searchPanel.add(cb);
        searchPanel.add(searchArea);
        searchPanel.add(searchButton);
    }

    //EFFECTS: Process what type of search the user wants and the inputs of the search
    private void processSearch(String searchMethod, int searchIndex, JPanel searchArea) {
        Component[] components = searchArea.getComponents();
        switch (searchMethod) {
            case "Name":
            case "Type":
                nameAndTypeProcess(components, searchIndex);
                break;
            case "Price Range":
            case "Profit":
            default:
                priceAndCostProcess(components, searchIndex);
                break;
        }
    }

    //EFFECTS: Processes searches related to Price, Cost, and Profit
    private void priceAndCostProcess(Component[] components, int n) {
        JTable tempTable = initTable();
        DefaultTableModel tempModel = (DefaultTableModel) tempTable.getModel();
        JTextField lowText = (JTextField) components[1];
        double low = setVal(lowText, "low");
        JTextField highText = (JTextField) components[4];
        double high = setVal(highText, "high");

        for (int i = tempTable.getRowCount() - 1; i >= 0; i--) {
            double val = Double.parseDouble(tempTable.getValueAt(i, n).toString());
            if (val < low || val > high) {
                tempModel.removeRow(i);
            }
        }
    }

    //EFFECTS: Helper Method, used to set the low and high ends of the range
    private double setVal(JTextField lowText, String lowOrHigh) {
        double val;
        try {
            val = Double.parseDouble(lowText.getText());
        } catch (Exception e) {
            if (lowOrHigh.equals("low")) {
                val = Double.MIN_VALUE;
            } else {
                val = Double.MAX_VALUE;
            }
        }
        return val;
    }

    //EFFECTS: Processes the search for searches relating to the name or type of the product
    private void nameAndTypeProcess(Component[] components, int n) {
        JTable tempTable = initTable();
        DefaultTableModel tempModel = (DefaultTableModel) tempTable.getModel();
        JTextField temp = (JTextField) components[0];
        String searchString = temp.getText();

        for (int i = tempTable.getRowCount() - 1; i >= 0; i--) {
            String s = tempTable.getValueAt(i, n).toString();
            if (!s.toLowerCase().contains(searchString.toLowerCase())) {
                tempModel.removeRow(i);
            }
        }
    }

    //EFFECTS: Changes the search area depending on what the user selects from the combox
    private void changeSearchMethod(String selected, JPanel searchArea) {
        switch (selected) {
            case "Name":
            case "Type":
                nameAndTypeSearch(searchArea);
                break;
            default:
                rangeSearches(searchArea);
                break;
        }
    }

    //EFFECTS: changes the search area appropriate for searches related to names and types
    private void nameAndTypeSearch(JPanel searchArea) {
        JTextField textBar = new JTextField();
        textBar.setPreferredSize(searchArea.getPreferredSize());
        searchArea.removeAll();
        searchArea.setLayout(new BorderLayout());
        searchArea.add(textBar);
        frame.revalidate();
        frame.repaint();
    }

    //EFFECTS: Changes the search area appropriate for searches involving ranges and values
    private void rangeSearches(JPanel searchArea) {
        JLabel lowLabel = new JLabel("Low End:");
        lowLabel.setPreferredSize(new Dimension(60, 30));
        JLabel highLabel = new JLabel("High End:");
        highLabel.setPreferredSize(new Dimension(60, 30));
        JTextField lowText = new JTextField();
        lowText.setPreferredSize(new Dimension(180, 30));
        JTextField highText = new JTextField();
        highText.setPreferredSize(new Dimension(180, 30));
        JLabel whiteSpace = new JLabel();
        whiteSpace.setPreferredSize(new Dimension(20, 30));
        searchArea.removeAll();
        searchArea.setLayout(new GridBagLayout());
        searchArea.add(lowLabel);
        searchArea.add(lowText);
        searchArea.add(whiteSpace);
        searchArea.add(highLabel);
        searchArea.add(highText);
        frame.revalidate();
        frame.repaint();
    }

    //EFFECTS: constructs the table holding the warehouse manager data
    private JTable initTable() {
        String[] labels = {"NAME", "TYPE", "PRICE", "COST", "STOCK", "SOLD", "PROFIT"}; //table labels

        DefaultTableModel tempModel = new DefaultTableModel();
        tempModel.setColumnIdentifiers(labels); //creating the table "backend", setting labels to what was identified


        JTable tempTable = new JTable(tempModel); //creating the table "fronted display"

        JScrollPane scrollPane = new JScrollPane(tempTable); //adding the table to a scrollPane to see the column labels
        scrollPane.setPreferredSize(new Dimension(700, 500));
        tableDisplay.removeAll();
        tableDisplay.add(scrollPane); //add the scrollPane (and the table contained inside) to the frame

        for (int i = 0; i < warehouse.getNumProducts(); i++) {
            ProductEntry p = warehouse.getProductAtIndex(i);
            String[] temp = {p.getName(), p.getType(), Double.toString(p.getPricePerUnit()),
                    Double.toString(p.getCostPerUnit()), Integer.toString(p.getNumStock()),
                    Integer.toString(p.getUnitsSold()), Double.toString(p.calculateProfit())};
            tempModel.addRow(temp);
        }
        arrangeBy("Name", 0);
        tempTable.setForeground(Color.RED);
        frame.repaint();
        frame.revalidate();
        return tempTable;
    }

    //EFFECTS: Initializes the buttons on the home screen of the Warehouse
    private void initHomeButtons() {
        JPanel homeButtonsDisplay = new JPanel();
        JButton addOrRemove = makeHomeAddOrRemoveButton();
        JButton graphProducts = makeHomeGraphProductsButton();
        JButton arrangeByCharacteristic = makeHomeArrangeButton();
        JButton showPending = makeHomeShowPendingButton();

        homeButtonsDisplay.add(addOrRemove);
        homeButtonsDisplay.add(arrangeByCharacteristic);
        homeButtonsDisplay.add(graphProducts);
        homeButtonsDisplay.add(showPending);
        addSaveButton(homeButtonsDisplay);


        mainPane.setBottomComponent(homeButtonsDisplay);
        mainPane.setDividerLocation(450);

    }

    //EFFECTS: Helper method for creating home buttons, creates the button to show pending orders
    private JButton makeHomeShowPendingButton() {
        JButton showPending = new JButton("Show Pending Orders");
        showPending.setPreferredSize(new Dimension(180, 50));
        showPending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PendingOrdersFrame(instance);
            }
        });
        return showPending;
    }

    //EFFECTS: Makes the Arrange Product button for the home screen
    private JButton makeHomeArrangeButton() {
        JButton arrangeByCharacteristic = new JButton("Arrange Products");
        arrangeByCharacteristic.setPreferredSize(new Dimension(180, 50));
        arrangeByCharacteristic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeArrangeDisplay();
            }
        });
        return arrangeByCharacteristic;
    }

    //EFFECTS: Makes the Graph Products button for the home screen
    private JButton makeHomeGraphProductsButton() {
        JButton graphProducts = new JButton("Graph Products");
        graphProducts.setPreferredSize(new Dimension(160, 50));
        graphProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GraphFrame(warehouseName, model);
            }
        });
        return graphProducts;
    }

    //EFFECTS: Makes the Add or Remove Product button for the home screen
    private JButton makeHomeAddOrRemoveButton() {
        JButton addOrRemove = new JButton("Add or Remove Product");
        addOrRemove.setPreferredSize(new Dimension(220, 50));
        addOrRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeAddOrRemoveDisplay();
            }
        });
        return addOrRemove;
    }


    //EFFECTS: Creates a panel to let the user select what they want to arrange their products by
    private void makeArrangeDisplay() {
        JPanel arrangePanel = new JPanel();
        arrangePanel.setLayout(new BoxLayout(arrangePanel, BoxLayout.Y_AXIS));
        JPanel labelPanel = new JPanel();
        JPanel boxPanel = new JPanel();
        JPanel homePanel = new JPanel();
        labelPanel.add(new JLabel("Select what to arrange Products by: "));
        addHomeButton(homePanel);
        String[] labels = new String[]{"Name", "Type", "Price", "Cost", "Stock", "Sold", "Profit"};
        JComboBox cb = new JComboBox(labels);
        cb.setPreferredSize(new Dimension(200, 40));
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                arrangeBy((String) cb.getSelectedItem(), cb.getSelectedIndex());
            }
        });
        cb.setSelectedIndex(0);
        boxPanel.add(cb);
        arrangePanel.add(labelPanel);
        arrangePanel.add(boxPanel);
        arrangePanel.add(homePanel);
        mainPane.setBottomComponent(arrangePanel);
        mainPane.setDividerLocation(450);
    }

    //EFFECTS: Constructs the display allowing the user to add or remove products from the warehouse
    private void makeAddOrRemoveDisplay() {
        JPanel finalDisplay = new JPanel();
        makeAddProductButton(finalDisplay);
        makeRemoveProductButton(finalDisplay);
        addSaveButton(finalDisplay);
        addHomeButton(finalDisplay);
        mainPane.setBottomComponent(finalDisplay);
        mainPane.setDividerLocation(465);
        frame.revalidate();
        frame.repaint();
    }

    //EFFECTS: Adds a button to the panel that lets the user return to the home display when clicked
    private void addHomeButton(JPanel panel) {
        JButton home = new JButton("Home");
        home.setPreferredSize(new Dimension(200, 30));
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initHomeButtons();
            }
        });
        panel.add(home);
    }

    //EFFECTS: Creates the button To remove selected rows from the table
    private void makeRemoveProductButton(JPanel finalDisplay) {
        JButton removeProduct = new JButton("Remove Selected Products");
        removeProduct.setPreferredSize(new Dimension(200, 50));
        removeProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    model.removeRow(rows[i] - i);
                }
            }
        });
        finalDisplay.add(removeProduct);

    }

    //EFFECTS: Creates a button to add rows to the warehouse
    private void makeAddProductButton(JPanel finalDisplay) {
        JButton addProduct = new JButton("Add Row to Table");
        addProduct.setPreferredSize(new Dimension(200, 50));
        addProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addRow(new String[]{"", "", "", "", "", "", ""});
            }
        });
        finalDisplay.add(addProduct);
    }

    //EFFECTS: adds a save button to the panel that lets the user save the warehouse to its file path
    private void addSaveButton(JPanel panel) {
        JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(200, 50));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    warehouse.removeAllProducts();
                    for (int i = 0; i < table.getRowCount(); i++) {
                        ProductEntry temp = makeProductAtRow(i);
                        warehouse.addProductEntry(temp);
                    }
                } catch (Exception ex) {
                    new ErrorWindow("Error! Couldn't save!");
                }
                try {
                    JsonWriter.write(warehouse, filePath);
                    table = initTable();
                    model = (DefaultTableModel) table.getModel();
                } catch (Exception ex) {
                    //pass
                }
            }
        });
        panel.add(save);
    }

    //EFFECTS: Constructs a ProductEntry from the information of a row in the table
    private ProductEntry makeProductAtRow(int i) throws InvalidAmountException {
        String name = (String) table.getValueAt(i, 0);
        String type = (String) table.getValueAt(i, 1);
        double price = Double.parseDouble((String) table.getValueAt(i, 2));
        double cost = Double.parseDouble((String) table.getValueAt(i, 3));
        int stock = Integer.parseInt((String) table.getValueAt(i, 4));
        int sold = Integer.parseInt((String) table.getValueAt(i, 5));
        ProductEntry temp = new ProductEntry(name, type, price, cost, stock);
        temp.setUnitsSold(sold);
        return temp;
    }


    //EFFECTS: Arranges the products in the warehouse by the chosen method
    private void arrangeBy(String operation, int selectedIndex) {
        //tableDisplay contains the scrollPane that contains the table itself. First, get the scrollPane
        JScrollPane tempScrollPane = (JScrollPane) tableDisplay.getComponent(0);
        //next, get the table from the scrollPane
        JTable tempTable = (JTable) tempScrollPane.getViewport().getView(); //idk why this works xD
        DefaultTableModel tempModel = (DefaultTableModel) tempTable.getModel();
        switch (operation) {
            case "Name":
            case "Type":
                arrangeByNameOrType(selectedIndex, tempModel);
                break;
            case "Price":
            case "Cost":
            case "Stock":
            case "Sold":
            default:
                arrangeByValue(selectedIndex, tempModel);

        }
    }

    //EFFECTS: Helper method for arranging the products by a particular value
    private void arrangeByValue(int n, DefaultTableModel tempModel) {
        //insertion sort
        for (int i = 1; i < tempModel.getRowCount(); i++) {
            int j = i - 1;
            double key = Double.parseDouble((String) tempModel.getValueAt(i, n));
            while (j >= 0 && Double.parseDouble((String) tempModel.getValueAt(j, n)) < key) {
                tempModel.moveRow(j + 1, j + 1, j);
                j--;
            }
        }
    }


    //EFFECTS: Helper method for arranging the products by name or type
    private void arrangeByNameOrType(int n, DefaultTableModel tempModel) {

        //insertion sort
        for (int i = 1; i < tempModel.getRowCount(); i++) {
            int j = i - 1;
            String key = (String) tempModel.getValueAt(i, n);
            while (j >= 0 && key.compareTo((String) tempModel.getValueAt(j, n)) < 0) {
                tempModel.moveRow(j + 1, j + 1, j);
                j--;
            }
        }
    }

    //Methods for PendingOrdersFrame class only
    public String getWarehouseName() {
        return warehouseName;
    }

    public ProductList getWarehouse() {
        return warehouse;
    }

    public String getFilePath() {
        return filePath;
    }

    //EFFECTS: updates the table after it is called by PendingOrdersFrame
    public void refreshTable() {
        try {
            warehouse = JsonReader.readWarehouse(filePath);
            table = initTable();
            model = (DefaultTableModel) table.getModel();
        } catch (Exception e) {
            new ErrorWindow("Couldn't save!");
        }
    }
}
