package process;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Simple Swing launcher for importing and deleting account data.
 */
public class Home {
    private JFrame mainFrame;
    private String account = null;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Home window = new Home();
                window.mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Home() {
        initialize();
    }

    private void initialize() {
        mainFrame = new JFrame();
        mainFrame.setTitle("ANSWER CENTRE OF LA");
        mainFrame.setBounds(100, 100, 240, 296);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(new CardLayout());

        JPanel menuPanel = createMenuPanel();
        mainFrame.getContentPane().add(menuPanel, "MenuPanel");
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(null);

        JComboBox<String> comboBox = createAccountComboBox();
        menuPanel.add(comboBox);

        JButton btnGo = new JButton("IMPORT LIST");
        btnGo.setBounds(44, 149, 116, 23);
        btnGo.addActionListener(e -> {
            Import2SQL.main(account);
            JOptionPane.showMessageDialog(menuPanel, "List Imported");
        });
        menuPanel.add(btnGo);

        JButton btnDelete = new JButton("DELETE ALL");
        btnDelete.setBounds(44, 106, 116, 23);
        btnDelete.addActionListener(e -> deleteRecords(account));
        menuPanel.add(btnDelete);

        return menuPanel;
    }

    private JComboBox<String> createAccountComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(new String[] {"Select Account", "115", "218", "537", "903"});
        comboBox.setBounds(44, 67, 116, 22);
        comboBox.addActionListener(e -> account = (String) comboBox.getSelectedItem());
        return comboBox;
    }

    private void deleteRecords(String account) {
        String sql;
        switch (account) {
            case "537" -> sql = "DELETE FROM Patients";
            case "903" -> sql = "DELETE FROM OnCall_903";
            case "218" -> sql = "DELETE FROM Advantage_218";
            case "115" -> sql = "DELETE FROM Properties_115";
            default -> {
                JOptionPane.showMessageDialog(null, "Unsupported account");
                return;
            }
        }
        try (Connection conn = getDatabaseConnection(account); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.execute();
            JOptionPane.showMessageDialog(null, "Records deleted for account: " + account);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting records: " + e.getMessage());
        }
    }

    private Connection getDatabaseConnection(String account) throws SQLException {
        String host = System.getenv("DB_HOST");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        String dbName = account.equals("537") ? System.getenv("DB_NAME_537") : System.getenv("DB_NAME_CUSTOMERS");
        String url = String.format("jdbc:sqlserver://%s;database=%s;user=%s;password=%s;loginTimeout=30;", host, dbName, user, password);
        return DriverManager.getConnection(url);
    }
}
