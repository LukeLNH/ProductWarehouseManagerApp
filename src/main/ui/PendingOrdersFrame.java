package ui;

import exceptions.InvalidAmountException;
import model.PendingOrder;
import model.ProductList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PendingOrdersFrame {

    private GUI instance;

    private JTable pendingTable;
    private DefaultTableModel pendingModel;
    private JPanel pendingTableDisplay;
    private JPanel pendingButtonDisplay;

    public PendingOrdersFrame(GUI instance) {
        this.instance = instance;
        makeFrame();
    }

    //EFFECTS: Makes the frame containing the pending orders
    private void makeFrame() {
        JFrame pendingFrame = new JFrame(instance.getWarehouseName() + " Pending Orders");
        pendingFrame.setSize(new Dimension(620, 450));
        pendingFrame.setLocationRelativeTo(null);

        JSplitPane mainPane = new JSplitPane();
        mainPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        mainPane.setDividerLocation(285);
        pendingFrame.add(mainPane);

        initTableDisplay();
        initButtonDisplay();

        mainPane.setTopComponent(pendingTableDisplay);
        mainPane.setBottomComponent(pendingButtonDisplay);

        pendingFrame.revalidate();
        pendingFrame.repaint();
        pendingFrame.setVisible(true);


    }

    //EFFECTS: initializes the table display
    private void initTableDisplay() {
        pendingTableDisplay = new JPanel();

        String[] labels = {"NAME", "TYPE", "QUANTITY", "BUYER ID"}; //table labels
        pendingModel = new DefaultTableModel();
        pendingModel.setColumnIdentifiers(labels);
        pendingTable = new JTable(pendingModel);

        JScrollPane scrollPane = new JScrollPane(pendingTable);
        scrollPane.setPreferredSize(new Dimension(600, 340));

        loadPendingOrders();
        pendingTableDisplay.add(scrollPane);
    }

    //EFFECTS: Loads preexisting pending orders from data file into the table
    private void loadPendingOrders() {
        for (int i = 0; i < instance.getWarehouse().getNumPendingOrders(); i++) {
            PendingOrder order = instance.getWarehouse().getPendingOrderAtIndex(i);
            String[] temp = new String[]{order.getName(), order.getType(), Integer.toString(order.getQuantity()),
                    order.getBuyerID()};
            pendingModel.addRow(temp);
        }
    }

    //EFFECTS: Initializes the buttons
    private void initButtonDisplay() {
        pendingButtonDisplay = new JPanel();
        JButton addPending = makeAddPendingButton();
        JButton resolvePending = makeResolvePendingButton();
        JButton removeWithoutResolving = makeRemoveWithoutResolvingButton();
        pendingButtonDisplay.add(addPending);
        pendingButtonDisplay.add(resolvePending);
        pendingButtonDisplay.add(removeWithoutResolving);
        addSaveButton(pendingButtonDisplay);
    }


    private JButton makeRemoveWithoutResolvingButton() {
        JButton remove = new JButton("Force Remove Order");
        remove.setPreferredSize(new Dimension(180,50));
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = pendingTable.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    pendingModel.removeRow(rows[i] - i);
                }
            }
        });
        return remove;
    }

    private JButton makeAddPendingButton() {
        JButton addPending = new JButton("Add Pending Order");
        addPending.setPreferredSize(new Dimension(180, 50));
        addPending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pendingModel.addRow(new String[]{"", "", "", ""});
            }
        });
        return addPending;
    }

    private JButton makeResolvePendingButton() {
        JButton resolvePending = new JButton("Resolve Pending Order");
        resolvePending.setPreferredSize(new Dimension(180, 50));
        resolvePending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = pendingTable.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    try {
                        PendingOrder order = makePendingOrderAtRow(rows[i] - i);
                        instance.getWarehouse().resolvePendingOrder(order);
                        pendingModel.removeRow(rows[i] - i);
                    } catch (Exception ex) {
                        new ErrorWindow("Error! Couldn't resolve all orders!");
                        break;
                    }
                }
            }
        });
        return resolvePending;
    }

    //EFFECTS: adds a save button to the panel that lets the user save the warehouse to its file path
    private void addSaveButton(JPanel panel) {
        JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(180, 50));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pendingOrderAddAction();
            }
        });
        panel.add(save);
    }

    private void pendingOrderAddAction() {
        try {
            instance.getWarehouse().removeAllPendingOrders();
            for (int i = 0; i < pendingTable.getRowCount(); i++) {
                PendingOrder temp = makePendingOrderAtRow(i);
                instance.getWarehouse().addPendingOrder(temp);
            }
        } catch (Exception ex) {
            new ErrorWindow("Error! Couldn't save!");
        }
        try {
            JsonWriter.write(instance.getWarehouse(), instance.getFilePath());
            instance.refreshTable();
        } catch (Exception ex) {
            //pass
        }
    }

    private PendingOrder makePendingOrderAtRow(int i) throws InvalidAmountException {
        String name = (String) pendingTable.getValueAt(i, 0);
        String type = (String) pendingTable.getValueAt(i, 1);
        int quantity = Integer.parseInt((String) pendingTable.getValueAt(i, 2));
        String buyerID = (String) pendingTable.getValueAt(i, 3);
        PendingOrder temp = new PendingOrder(name, type, quantity, buyerID);
        return temp;
    }

}
