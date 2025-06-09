import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DataPengunjungFrame extends JFrame {
    JTextField txtId, txtNama, txtTelepon, txtCheckIn, txtCheckOut;
    JTextArea txtAlamat, txtCatatan;
    JRadioButton rbLaki, rbPerempuan;
    JSpinner spinnerJumlah;
    JComboBox<String> comboTipe, comboBayar;
    JTable table;
    JButton btnSimpan, btnEdit, btnHapus, btnExit;

    public DataPengunjungFrame() {
        setTitle("Data Pengunjung Villa");
        setSize(900, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(200, 225, 250));

        JLabel lblTitle = new JLabel("Form Pemesanan Villa");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBounds(330, 10, 300, 30);
        panel.add(lblTitle);

        int y = 60;
        panel.add(addLabel("ID Pengunjung", 50, y));
        txtId = addTextField(panel, 200, y); y += 40;

        panel.add(addLabel("Nama Pengunjung", 50, y));
        txtNama = addTextField(panel, 200, y); y += 40;

        panel.add(addLabel("Jenis Kelamin", 50, y));
        rbLaki = new JRadioButton("Laki-Laki");
        rbPerempuan = new JRadioButton("Perempuan");
        rbLaki.setBounds(200, y, 100, 25);
        rbPerempuan.setBounds(300, y, 100, 25);
        ButtonGroup bgJK = new ButtonGroup();
        bgJK.add(rbLaki); bgJK.add(rbPerempuan);
        panel.add(rbLaki); panel.add(rbPerempuan);
        y += 40;

        panel.add(addLabel("No. Telepon", 50, y));
        txtTelepon = addTextField(panel, 200, y); y += 40;

        panel.add(addLabel("Jumlah Orang", 50, y));
        spinnerJumlah = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinnerJumlah.setBounds(200, y, 60, 25);
        panel.add(spinnerJumlah); y += 40;

        panel.add(addLabel("Alamat", 50, y));
        txtAlamat = new JTextArea();
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);
        scrollAlamat.setBounds(200, y, 300, 60);
        panel.add(scrollAlamat); y += 80;

        panel.add(addLabel("Tanggal Check-in", 50, y));
        txtCheckIn = new JTextField("YYYY-MM-DD");
        txtCheckIn.setBounds(200, y, 200, 25);
        panel.add(txtCheckIn); y += 40;

        panel.add(addLabel("Tanggal Check-out", 50, y));
        txtCheckOut = new JTextField("YYYY-MM-DD");
        txtCheckOut.setBounds(200, y, 200, 25);
        panel.add(txtCheckOut); y += 40;

        panel.add(addLabel("Tipe Villa", 50, y));
        comboTipe = new JComboBox<>(new String[]{"Standar", "VIP", "Family"});
        comboTipe.setBounds(200, y, 150, 25);
        panel.add(comboTipe); y += 40;

        panel.add(addLabel("Metode Pembayaran", 50, y));
        comboBayar = new JComboBox<>(new String[]{"Cash", "Transfer", "QRIS"});
        comboBayar.setBounds(200, y, 150, 25);
        panel.add(comboBayar); y += 40;

        panel.add(addLabel("Catatan Khusus", 50, y));
        txtCatatan = new JTextArea();
        JScrollPane scrollCatatan = new JScrollPane(txtCatatan);
        scrollCatatan.setBounds(200, y, 300, 60);
        panel.add(scrollCatatan); y += 80;

        table = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{
            "ID", "Nama", "JK", "Telepon", "Jumlah", "Alamat",
            "Check-in", "Check-out", "Tipe", "Bayar", "Catatan"
        }));
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBounds(50, y, 780, 150);
        panel.add(scrollTable); y += 170;

        btnSimpan = new JButton("Simpan");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        btnExit = new JButton("Exit");

        btnSimpan.setBounds(100, y, 100, 30);
        btnEdit.setBounds(220, y, 100, 30);
        btnHapus.setBounds(340, y, 100, 30);
        btnExit.setBounds(460, y, 100, 30);

        panel.add(btnSimpan);
        panel.add(btnEdit);
        panel.add(btnHapus);
        panel.add(btnExit);

        add(panel);

        // Load data saat mulai
        loadData();

        // Tombol Simpan (insert ke DB)
        btnSimpan.addActionListener(e -> {
            String id = txtId.getText().trim();
            String nama = txtNama.getText().trim();
            String jk = rbLaki.isSelected() ? "Laki-Laki" : rbPerempuan.isSelected() ? "Perempuan" : "";
            String telepon = txtTelepon.getText().trim();
            int jumlah = (int) spinnerJumlah.getValue();
            String alamat = txtAlamat.getText().trim();
            String checkin = txtCheckIn.getText().trim();
            String checkout = txtCheckOut.getText().trim();
            String tipe = comboTipe.getSelectedItem().toString();
            String bayar = comboBayar.getSelectedItem().toString();
            String catatan = txtCatatan.getText().trim();

            if (id.isEmpty() || nama.isEmpty() || jk.isEmpty() || telepon.isEmpty() || alamat.isEmpty() || checkin.isEmpty() || checkout.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua data wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menyimpan data ini?", "Konfirmasi Simpan", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                try (Connection conn = KoneksiDB.getConnection()) {
                    String sql = "INSERT INTO pengunjung (id, nama, jenis_kelamin, telepon, jumlah_orang, alamat, check_in, check_out, tipe_villa, metode_pembayaran, catatan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, id);
                    ps.setString(2, nama);
                    ps.setString(3, jk);
                    ps.setString(4, telepon);
                    ps.setInt(5, jumlah);
                    ps.setString(6, alamat);
                    ps.setString(7, checkin);
                    ps.setString(8, checkout);
                    ps.setString(9, tipe);
                    ps.setString(10, bayar);
                    ps.setString(11, catatan);

                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
                    loadData();
                    clearForm();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal simpan data: " + ex.getMessage());
                }
            }
        });

        // Tombol Edit (update DB)
        btnEdit.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang akan diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = txtId.getText().trim();
            String nama = txtNama.getText().trim();
            String jk = rbLaki.isSelected() ? "Laki-Laki" : rbPerempuan.isSelected() ? "Perempuan" : "";
            String telepon = txtTelepon.getText().trim();
            int jumlah = (int) spinnerJumlah.getValue();
            String alamat = txtAlamat.getText().trim();
            String checkin = txtCheckIn.getText().trim();
            String checkout = txtCheckOut.getText().trim();
            String tipe = comboTipe.getSelectedItem().toString();
            String bayar = comboBayar.getSelectedItem().toString();
            String catatan = txtCatatan.getText().trim();

            if (id.isEmpty() || nama.isEmpty() || jk.isEmpty() || telepon.isEmpty() || alamat.isEmpty() || checkin.isEmpty() || checkout.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua data wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin mengubah data ini?", "Konfirmasi Edit", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                try (Connection conn = KoneksiDB.getConnection()) {
                    String sql = "UPDATE pengunjung SET nama=?, jenis_kelamin=?, telepon=?, jumlah_orang=?, alamat=?, check_in=?, check_out=?, tipe_villa=?, metode_pembayaran=?, catatan=? WHERE id=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, nama);
                    ps.setString(2, jk);
                    ps.setString(3, telepon);
                    ps.setInt(4, jumlah);
                    ps.setString(5, alamat);
                    ps.setString(6, checkin);
                    ps.setString(7, checkout);
                    ps.setString(8, tipe);
                    ps.setString(9, bayar);
                    ps.setString(10, catatan);
                    ps.setString(11, id);

                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
                    loadData();
                    clearForm();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal mengubah data: " + ex.getMessage());
                }
            }
        });

        // Tombol Hapus (delete dari DB)
        btnHapus.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String id = table.getValueAt(selected, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                try (Connection conn = KoneksiDB.getConnection()) {
                    String sql = "DELETE FROM pengunjung WHERE id=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                    loadData();
                    clearForm();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + ex.getMessage());
                }
            }
        });

        // Tombol Exit
        btnExit.addActionListener(e -> System.exit(0));

        // Klik baris di tabel akan mengisi form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtId.setText(table.getValueAt(row, 0).toString());
                txtNama.setText(table.getValueAt(row, 1).toString());
                String jk = table.getValueAt(row, 2).toString();
                rbLaki.setSelected(jk.equals("Laki-Laki"));
                rbPerempuan.setSelected(jk.equals("Perempuan"));
                txtTelepon.setText(table.getValueAt(row, 3).toString());
                spinnerJumlah.setValue(Integer.parseInt(table.getValueAt(row, 4).toString()));
                txtAlamat.setText(table.getValueAt(row, 5).toString());
                txtCheckIn.setText(table.getValueAt(row, 6).toString());
                txtCheckOut.setText(table.getValueAt(row, 7).toString());
                comboTipe.setSelectedItem(table.getValueAt(row, 8).toString());
                comboBayar.setSelectedItem(table.getValueAt(row, 9).toString());
                txtCatatan.setText(table.getValueAt(row, 10).toString());
            }
        });
    }

    private JLabel addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 140, 25);
        return label;
    }

    private JTextField addTextField(JPanel panel, int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 200, 25);
        panel.add(field);
        return field;
    }

    // Method untuk load data dari DB ke tabel
    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Bersihkan tabel dulu
        try (Connection conn = KoneksiDB.getConnection()) {
            String sql = "SELECT * FROM pengunjung";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Object[] row = {
                    rs.getString("id"),
                    rs.getString("nama"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("telepon"),
                    rs.getInt("jumlah_orang"),
                    rs.getString("alamat"),
                    rs.getString("check_in"),
                    rs.getString("check_out"),
                    rs.getString("tipe_villa"),
                    rs.getString("metode_pembayaran"),
                    rs.getString("catatan")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
        }
    }

    // Method untuk bersihkan form input
    private void clearForm() {
        txtId.setText("");
        txtNama.setText("");
        rbLaki.setSelected(false);
        rbPerempuan.setSelected(false);
        txtTelepon.setText("");
        spinnerJumlah.setValue(1);
        txtAlamat.setText("");
        txtCheckIn.setText("YYYY-MM-DD");
        txtCheckOut.setText("YYYY-MM-DD");
        comboTipe.setSelectedIndex(0);
        comboBayar.setSelectedIndex(0);
        txtCatatan.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DataPengunjungFrame().setVisible(true);
        });
    }
}