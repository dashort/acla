package process;

<<<<<<< HEAD
import java.awt.CardLayout;
import java.awt.EventQueue;
=======

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
>>>>>>> bb500479f201c5b0b636cd9283c4f25d780a2c0f
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
<<<<<<< HEAD
import javax.swing.*;

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
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Select Account", "115", "218", "537", "903"});
        comboBox.setBounds(44, 67, 116, 22);
        comboBox.addActionListener(e -> account = (String) comboBox.getSelectedItem());
        return comboBox;
    }

    private void deleteRecords(String account) {
        String sql;
        switch (account) {
            case "537":
                sql = "DELETE FROM Patients";
                break;
            case "903":
                sql = "DELETE FROM OnCall_903";
                break;
            case "218":
                sql = "DELETE FROM Advantage_218";
                break;
            case "115":
                sql = "DELETE FROM Properties_115";
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unsupported account");
                return;
        }
        try (Connection conn = getDatabaseConnection(account);
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.execute();
            JOptionPane.showMessageDialog(null, "Records deleted for account: " + account);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting records: " + e.getMessage());
        }
    }

    private Connection getDatabaseConnection(String account) throws SQLException {
        String connectionUrl = "jdbc:sqlserver://AmtelcoRDS6.miAmtelcoCloud.com;" +
                (account.equals("537") ? "database=537_2414_1;" : "database=Customers_2414_1;") +
                "user=user2414;password=N6sgWPUTcp6J;loginTimeout=30;";
        return DriverManager.getConnection(connectionUrl);
    }
}
=======
import java.text.ParseException;
import javax.swing.*;

public class Home {

	private JFrame frmAbsecLlc;
	public String realm = null;
	public String account = null;
//	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home window = new Home();
					window.frmAbsecLlc.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Home() throws ParseException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws ParseException {
		frmAbsecLlc = new JFrame();
		frmAbsecLlc.setTitle("ANSWER CENTRE OF LA");
		frmAbsecLlc.setBounds(100, 100, 240, 296);
		frmAbsecLlc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAbsecLlc.getContentPane().setLayout(new CardLayout(0, 0));

		final JPanel panelMenu = new JPanel();
		frmAbsecLlc.getContentPane().add(panelMenu, "name_1473696262299771");
		panelMenu.setLayout(null);
		panelMenu.setVisible(true);
		 
		 JComboBox comboBox = new JComboBox(); comboBox.setModel(new DefaultComboBoxModel(new String[] {
		 "Select Account","537", "903" })); comboBox.setToolTipText("Account"); comboBox.setBounds(44,
		 67, 116, 22); panelMenu.add(comboBox);
		 comboBox.setSelectedIndex(0);
		 comboBox.addActionListener(new ActionListener() {
			 
			 @Override
			 public void actionPerformed(ActionEvent e) {
				 if(e.getSource()==comboBox) {
					 account = (String) comboBox.getSelectedItem();
					 
					 System.out.println("Account is " + account);
					 
					 if (account == "537") {txtSelectAccount.setText("Hospital Medicine");}
					 else if (account == "903") {txtSelectAccount.setText("Chevron");
					 
					//	 System.out.println(comboBox.getSelectedIndex());
					 
					 
					 }
				 
				 
			 }
			 } });
		  
		 
		JButton btnGo = new JButton("IMPORT LIST");
		btnGo.setBounds(44, 149, 116, 23);
		panelMenu.add(btnGo);
		btnGo.addActionListener(new ActionListener() {
			
		//	String account="537";
			
		
			@Override
			public void actionPerformed(ActionEvent e) {
				
Import2SQL.main(account);
				
				
JOptionPane.showMessageDialog(panelMenu, "List Imported");

			}
		});

		JButton btnDelete = new JButton("DELETE ALL");
		btnDelete.setBounds(44, 106, 116, 23);
		panelMenu.add(btnDelete);
		
		txtSelectAccount = new JTextField();
		txtSelectAccount.setHorizontalAlignment(SwingConstants.CENTER);
		txtSelectAccount.setText("SELECT ACCOUNT");
		txtSelectAccount.setBounds(44, 39, 113, 20);
		panelMenu.add(txtSelectAccount);
		txtSelectAccount.setColumns(10);
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (account == "903") {

				Connection conn;
				try {


					DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
					
//					String connectionUrl = "jdbc:sqlserver://acla.cnbusmu0fj2t.us-east-1.rds.amazonaws.com;" + "user=sa;" + "password=5020Glendale;" + "loginTimeout=30;" + "database=Customers;";
					String connectionUrl = "jdbc:sqlserver://AmtelcoRDS6.miAmtelcoCloud.com;" + "database=Customers_2414_1;" + "user=user2414;" + "password=N6sgWPUTcp6J;" + "loginTimeout=30;";
					conn = DriverManager.getConnection(connectionUrl);

					String delete_all = "DELETE FROM OnCall_903";

					PreparedStatement pst = conn.prepareStatement(delete_all);
					pst.execute();
					
				JOptionPane.showMessageDialog(panelMenu, "Worker List Deleted");
				
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				}
				else if (account == "537") {
					
					Connection conn;
					try {


						DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
						

						String connectionUrl = "jdbc:sqlserver://AmtelcoRDS6.miAmtelcoCloud.com;" + "database=537_2414_1;" + "user=user2414;" + "password=N6sgWPUTcp6J;" + "loginTimeout=30;";
						conn = DriverManager.getConnection(connectionUrl);

						String delete_all = "DELETE FROM Patients";

						PreparedStatement pst = conn.prepareStatement(delete_all);
						pst.execute();
						
					JOptionPane.showMessageDialog(panelMenu, "Patient List Deleted");
					
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
				};
				
			}
		});
		;

	}
	;

	JPanel panel = new JPanel();
	private JTextField txtSelectAccount;
}

>>>>>>> bb500479f201c5b0b636cd9283c4f25d780a2c0f
