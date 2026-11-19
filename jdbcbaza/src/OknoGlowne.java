import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class OknoGlowne {
    private JPanel panelGlowny;
    private JTextField imieField;
    private JTextField nazwiskoField;
    private JTextField stanowiskoField;
    private JButton dodajButton;
    private JTable tabela;
    private DefaultTableModel model;

    private static final String URL = "jdbc:mysql://localhost:3306/baza_egzaminacyjna";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public OknoGlowne() {
        String[] kolumny = {"ID", "Imię", "Nazwisko", "Stanowisko"};
        model = new DefaultTableModel(kolumny, 0);
        tabela.setModel(model);

        odswiez();

        dodajButton.addActionListener(e -> dodajPracownika());
    }

    private void odswiez() {
        model.setRowCount(0);
        String query = "SELECT id, imie, nazwisko, stanowisko FROM pracownicy";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("stanowisko")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panelGlowny, "Błąd bazy danych: " + ex.getMessage());
        }
    }

    private void dodajPracownika() {
        String imie = imieField.getText().trim();
        String nazwisko = nazwiskoField.getText().trim();
        String stanowisko = stanowiskoField.getText().trim();

        if (imie.isEmpty() || nazwisko.isEmpty() || stanowisko.isEmpty()) {
            JOptionPane.showMessageDialog(panelGlowny, "Wypełnij wszystkie pola!");
            return;
        }

        String query = "INSERT INTO pracownicy (imie, nazwisko, stanowisko) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, imie);
            stmt.setString(2, nazwisko);
            stmt.setString(3, stanowisko);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(panelGlowny, "Dodano pracownika.");
            imieField.setText("");
            nazwiskoField.setText("");
            stanowiskoField.setText("");
            odswiez();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panelGlowny, "Błąd bazy danych: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("INF.04 - System Obsługi Pracowników");
        frame.setContentPane(new OknoGlowne().panelGlowny);
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}