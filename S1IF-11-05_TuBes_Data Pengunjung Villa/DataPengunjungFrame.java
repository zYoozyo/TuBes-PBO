import java.awt.*;
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

        // Tombol Simpan
        btnSimpan.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menyimpan data ini?", "Konfirmasi Simpan", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
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

                model.addRow(new Object[]{id, nama, jk, telepon, jumlah, alamat, checkin, checkout, tipe, bayar, catatan});
                clearForm();
            }
        });

        // Tombol Edit
        btnEdit.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang akan diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin mengedit data ini?", "Konfirmasi Edit", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setValueAt(txtId.getText().trim(), selected, 0);
                model.setValueAt(txtNama.getText().trim(), selected, 1);
                model.setValueAt(rbLaki.isSelected() ? "Laki-Laki" : "Perempuan", selected, 2);
                model.setValueAt(txtTelepon.getText().trim(), selected, 3);
                model.setValueAt(spinnerJumlah.getValue(), selected, 4);
                model.setValueAt(txtAlamat.getText().trim(), selected, 5);
                model.setValueAt(txtCheckIn.getText().trim(), selected, 6);
                model.setValueAt(txtCheckOut.getText().trim(), selected, 7);
                model.setValueAt(comboTipe.getSelectedItem().toString(), selected, 8);
                model.setValueAt(comboBayar.getSelectedItem().toString(), selected, 9);
                model.setValueAt(txtCatatan.getText().trim(), selected, 10);
                clearForm();
            }
        });

        // Tombol Hapus
        btnHapus.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                ((DefaultTableModel) table.getModel()).removeRow(selected);
                clearForm();
            }
        });

        // Tombol Exit
        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar dari aplikasi?", "Konfirmasi Keluar", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        });

        // Tabel listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                txtId.setText(table.getValueAt(i, 0).toString());
                txtNama.setText(table.getValueAt(i, 1).toString());
                String jk = table.getValueAt(i, 2).toString();
                if (jk.equals("Laki-Laki")) rbLaki.setSelected(true); else rbPerempuan.setSelected(true);
                txtTelepon.setText(table.getValueAt(i, 3).toString());
                spinnerJumlah.setValue(Integer.parseInt(table.getValueAt(i, 4).toString()));
                txtAlamat.setText(table.getValueAt(i, 5).toString());
                txtCheckIn.setText(table.getValueAt(i, 6).toString());
                txtCheckOut.setText(table.getValueAt(i, 7).toString());
                comboTipe.setSelectedItem(table.getValueAt(i, 8).toString());
                comboBayar.setSelectedItem(table.getValueAt(i, 9).toString());
                txtCatatan.setText(table.getValueAt(i, 10).toString());
            }
        });
    }

    private JLabel addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 150, 25);
        return lbl;
    }

    private JTextField addTextField(JPanel panel, int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 200, 25);
        panel.add(tf);
        return tf;
    }

    private void clearForm() {
        txtId.setText(""); txtNama.setText(""); txtTelepon.setText(""); txtAlamat.setText("");
        txtCheckIn.setText("YYYY-MM-DD"); txtCheckOut.setText("YYYY-MM-DD"); txtCatatan.setText("");
        rbLaki.setSelected(false); rbPerempuan.setSelected(false); spinnerJumlah.setValue(1);
        comboTipe.setSelectedIndex(0); comboBayar.setSelectedIndex(0); table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DataPengunjungFrame().setVisible(true));
    }
}
