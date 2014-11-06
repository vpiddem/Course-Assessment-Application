package view;

import database.DataBaseConnection;
import structures.AccountDetails;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class UpdateAccount {

    DataBaseConnection db;

    public UpdateAccount(DataBaseConnection db) {
        this.db = db;
    }

    public JPanel createAccountSettingsUI() {
        panel_SignUpContainer = new JPanel();
        panel_FormEntries = new JPanel();
        lbl_UserName = new JLabel();
        txt_UserName = new JTextField();
        lbl_Password = new JLabel();
        txt_Password = new JTextField();
        lbl_Email = new JLabel();
        txt_Email = new JTextField();
        lbl_Phone = new JLabel();
        txt_PhoneNumber = new NumericTextField(0, 10);
        panel_SouthButtons = new JPanel();
        btn_UpdateInfo = new JButton();
        btn_Reset = new JButton();

        //======== panel_SignUpContainer ========
        {
            panel_SignUpContainer.setBackground(Color.white);

            panel_SignUpContainer.setLayout(new BorderLayout(5, 5));

            //======== panel_FormEntries ========
            {
                panel_FormEntries.setBackground(Color.white);
                panel_FormEntries.setLayout(new GridBagLayout());
                ((GridBagLayout) panel_FormEntries.getLayout()).columnWidths = new int[]{0, 81, 0};
                ((GridBagLayout) panel_FormEntries.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0};
                ((GridBagLayout) panel_FormEntries.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0E-4};
                ((GridBagLayout) panel_FormEntries.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- lbl_UserName ----
                lbl_UserName.setText("UserName");
                panel_FormEntries.add(lbl_UserName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                panel_FormEntries.add(txt_UserName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                //---- lbl_Password ----
                lbl_Password.setText("New Password");
                panel_FormEntries.add(lbl_Password, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                panel_FormEntries.add(txt_Password, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                //---- lbl_Email ----
                lbl_Email.setText("Email");
                panel_FormEntries.add(lbl_Email, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                panel_FormEntries.add(txt_Email, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                //---- lbl_Phpne ----
                lbl_Phone.setText("Phone Number");
                panel_FormEntries.add(lbl_Phone, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));
                panel_FormEntries.add(txt_PhoneNumber, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            panel_SignUpContainer.add(panel_FormEntries, BorderLayout.CENTER);

            //======== panel2 ========
            {
                panel_SouthButtons.setLayout(new FlowLayout());

                //---- btn_SignUp ----
                btn_UpdateInfo.setText("Update Info");
                btn_UpdateInfo.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        action_UpdateAccountInfo();
                    }

                });
                panel_SouthButtons.add(btn_UpdateInfo);

                //---- btn_Cancel ----
                btn_Reset.setText("Cancel");
                panel_SouthButtons.add(btn_Reset);
            }
            panel_SignUpContainer.add(panel_SouthButtons, BorderLayout.SOUTH);
        }
        return panel_SignUpContainer;
    }

    private void action_UpdateAccountInfo() {
        AccountDetails accDetails = new AccountDetails();
        accDetails.setEmail(txt_Email.getText());
        accDetails.setName(txt_UserName.getText());
        accDetails.setPassword(txt_Password.getText());
        accDetails.setPhoneNumberString(txt_PhoneNumber.getText());
        if(db.isPasswordEqualsOldPassword(txt_Password.getText().trim())){
            JOptionPane.showMessageDialog(panel_FormEntries, "Password Same as Old One \n Please Choose a different One.","Info",JOptionPane.WARNING_MESSAGE);
            return;
        }
//        db.setOldPasswordValue();
        db.updateLoginUserDetails(accDetails);
        JOptionPane.showMessageDialog(panel_FormEntries, "Successfully updated.","Success",JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel panel_SignUpContainer;
    private JPanel panel_FormEntries;
    private JLabel lbl_UserName;
    private JTextField txt_UserName;
    private JLabel lbl_Password;
    private JTextField txt_Password;
    private JLabel lbl_Email;
    private JTextField txt_Email;
    private JLabel lbl_Phone;
    private JTextField txt_PhoneNumber;
    private JPanel panel_SouthButtons;
    private JButton btn_UpdateInfo;
    private JButton btn_Reset;
}
