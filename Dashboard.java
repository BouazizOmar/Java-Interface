import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dashboard extends JDialog {

    public Dashboard(Frame owner) {
        super(owner, "DashBoard");
        setSize(550, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(true);
        JFreeChart typeBarChart = null;
        JFreeChart ratePieChart = null;
        JFreeChart securityPieChart = null;

        try {
            typeBarChart = ChartFactory.createBarChart("Type Count Chart", "", "", createDataset(getTypeData(), "Type", "Count", "Type"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            ratePieChart = ChartFactory.createPieChart("Rate Count Chart",  createDataset(getRateData(), "Rate", "Count"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            securityPieChart = ChartFactory.createPieChart("Security Count Chart", createDataset(getSecurityData(), "Security", "Count"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        ChartPanel typePanel = new ChartPanel(typeBarChart);
        ChartPanel ratePanel = new ChartPanel(ratePieChart);
        ChartPanel securityPanel = new ChartPanel(securityPieChart);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 10, 10));

        addComponent(mainPanel, typePanel, 1, 1);
        addComponent(mainPanel, ratePanel, 1, 2);
        addComponent(mainPanel, securityPanel, 2, 2);
        add(mainPanel);


        setVisible(true);

    }

    private void addComponent(Container container, Component component, int gridy, int gridx) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.insets = new Insets(5, 5, 5, 5);
        container.add(component, gbc);
    }



    private CategoryDataset createDataset(ResultSet resultSet, String xLabel, String yLabel, String rowKey) throws SQLException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Populate dataset with data from MySQL
        while (resultSet.next()) {
            String xValue = resultSet.getString(xLabel);
            int yValue = resultSet.getInt(yLabel);
            dataset.addValue(yValue, rowKey, xValue);
        }

        return dataset;

    }

    private PieDataset createDataset(ResultSet resultSet, String xLabel, String yLabel) throws SQLException {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Populate dataset with data from MySQL
        while (resultSet.next()) {
            String xValue = resultSet.getString(xLabel);
            int yValue = resultSet.getInt(yLabel);
            dataset.setValue(xValue, yValue);
        }

        return dataset;

    }

    private ResultSet getData(String query) throws SQLException {
        Connection con = Connect.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    private ResultSet getTypeData() throws SQLException{
        String query = "SELECT Type,count(*) as \"Count\" from avis_citoyen GROUP BY Type;";
        return getData(query);
    }

    private ResultSet getRateData() throws SQLException{
    String query = "SELECT rate as \"Rate\",count(*) as \"Count\" from avis_citoyen GROUP BY Rate;";
        return getData(query);
    }

    private ResultSet getSecurityData() throws SQLException{
        String query = "SELECT security as \"Security\",count(*) as \"Count\" from avis_citoyen GROUP BY Security;";
        return getData(query);
    }

}
