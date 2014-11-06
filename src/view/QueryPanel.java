package view;

import database.DataBaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * @author Vikas Piddempally
 */
public class QueryPanel extends JFrame {

    DataBaseConnection db;

    public QueryPanel(DataBaseConnection db) {
        this.db = db;
    }

    public JPanel getQueryPanel() {
        panel_North = new JPanel();
        panel_Container = new JPanel();
        lbl_QueryType = new JLabel();
        cbox_QueryType = new JComboBox<>();
        lbl_EnterQuery = new JLabel();
        txt_Query = new JTextField(30);
        btn_SubmitQuery = new JButton();
        panel_QueryResults = new JPanel();
        //======== panel_North ========
        {
            panel_North.setLayout(new FlowLayout(FlowLayout.LEFT));

            //---- lbl_QueryType ----
            lbl_QueryType.setText("Select Query Type :");
            panel_North.add(lbl_QueryType);

            //---- cbox_QueryType ----
            cbox_QueryType.setModel(new DefaultComboBoxModel<>(new String[]{
                "INSERT",
                "UPDATE",
                "DELETE",
                "DROP",
                "ALTER",
                "SELECT"
            }));
            panel_North.add(cbox_QueryType);

            //---- lbl_EnterQuery ----
            lbl_EnterQuery.setText("Enter Query :");
            panel_North.add(lbl_EnterQuery);
            panel_North.add(txt_Query);

            //---- btn_SubmitQuery ----
            btn_SubmitQuery.setText("Submit Query");

            btn_SubmitQuery.addActionListener(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    action_SubmitQueryPerformed();
                }

            });
            panel_North.add(btn_SubmitQuery);
        }
        panel_Container.add(panel_North, BorderLayout.NORTH);

        //======== panel_QueryResults ========
        {
            panel_QueryResults.setLayout(new BorderLayout(5, 5));
        }
        panel_Container.add(panel_QueryResults, BorderLayout.CENTER);
        return panel_Container;
    }

    private void action_SubmitQueryPerformed() {
        String queryType = cbox_QueryType.getSelectedItem().toString();
        
        switch (queryType) {
            case "SELECT":
                db.generateCsvFile(txt_Query.getText());
                break;
            case "DELETE":
            case "UPDATE":
            case "ALTER":
            case "DROP":
               db.executeUpdateCommand(txt_Query.getText());
                break;
        }

    }
    private JPanel panel_Container;
    private JPanel panel_North;
    private JLabel lbl_QueryType;
    private JComboBox<String> cbox_QueryType;
    private JLabel lbl_EnterQuery;
    private JTextField txt_Query;
    private JButton btn_SubmitQuery;
    private JPanel panel_QueryResults;
}
