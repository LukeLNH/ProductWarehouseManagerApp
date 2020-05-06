package ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphFrame {
    private String warehouseName;
    private DefaultTableModel model;

    //REQUIRES: an existing warehouse has warehouse name, an existing JTable uses model
    public GraphFrame(String warehouseName, DefaultTableModel model) {
        this.warehouseName = warehouseName;
        this.model = model;
        makeGraphFrame();
    }

    //EFFECTS: Constructs a new frame displaying the different graphs of data of the warehouse
    private void makeGraphFrame() {
        JFrame graphFrame = new JFrame(warehouseName + " Graph");
        graphFrame.setSize(new Dimension(600,500));
        graphFrame.setLocationRelativeTo(null);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Bar Graph", graphPanel("Bar"));
        tabbedPane.addTab("Pie Chart", graphPanel("Pie"));
        graphFrame.add(tabbedPane);
        graphFrame.setVisible(true);
    }

    //EFFECTS: Helper method to let the user select what aspect of their products they want graphed
    private JComboBox addGraphLabels(JPanel panel) {
        JPanel cbPanel = new JPanel();
        JComboBox cb = new JComboBox(new String[] {"Price", "Cost", "Stock", "Sold", "Profit"});
        cb.setPreferredSize(new Dimension(100,30));
        cbPanel.add(cb);
        cbPanel.setBackground(Color.WHITE);
        panel.add(cbPanel);
        return cb;
    }


    //EFFECTS: creates the panel containing the graph, to be put in the graph frame
    private JPanel graphPanel(String type) {
        JPanel graphPanel = new JPanel();
        graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
        JComboBox cb = addGraphLabels(graphPanel);
        JPanel tempPanel = new JPanel();
        graphPanel.add(tempPanel);

        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempPanel.removeAll();
                if (type.equals("Bar")) {
                    tempPanel.add(barGraph(cb.getSelectedIndex() + 2));
                } else {
                    tempPanel.add(pieChart(cb.getSelectedIndex() + 2));
                }
            }
        });
        cb.setSelectedIndex(4);
        return graphPanel;
    }

    //EFFECTS: Constructs a panel containing a piechart of the appropriate data
    private JPanel pieChart(int n) {
        String[] names = new String[model.getRowCount()];
        double[] data = new double[model.getRowCount()];
        for (int i = 0; i < model.getRowCount(); i++) {
            names[i] = (String) model.getValueAt(i,0);
            data[i] = Double.parseDouble((String) model.getValueAt(i,n));
        }
        DefaultPieDataset dataset = createPieData(names, data);
        JFreeChart chart = ChartFactory.createPieChart("Warehouse Statistics", dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
    }

    //EFFECTS: Constructs a panel containing a bar graph of the appropriate data
    private JPanel barGraph(int n) {
        String[] names = new String[model.getRowCount()];
        double[] data = new double[model.getRowCount()];
        for (int i = 0; i < model.getRowCount(); i++) {
            names[i] = (String) model.getValueAt(i,0);
            data[i] = Double.parseDouble((String) model.getValueAt(i,n));
        }
        CategoryDataset dataset = createBarData(names,data);
        JFreeChart chart = ChartFactory.createBarChart("Warehouse Statistics", "Product Name",
                "Amount", dataset, PlotOrientation.VERTICAL, false, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600,380));
        return chartPanel;
    }

    //EFFECTS: Constructs the data used to create the bar graph
    private CategoryDataset createBarData(String[] names, double[] data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < names.length; i++) {
            dataset.addValue(data[i], "", names[i]);
        }
        return dataset;
    }

    //EFFECTS: Constructs the data used to create the pie chart
    private DefaultPieDataset createPieData(String[] names, double[] data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int i = 0; i < names.length; i++) {
            dataset.setValue(names[i], data[i]);
        }
        return dataset;
    }

}
