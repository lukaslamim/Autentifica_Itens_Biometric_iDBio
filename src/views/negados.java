package views;


import DAO.BD;
import classes.tabela_liberados;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author lucas
 */
public final class negados extends javax.swing.JFrame {

    /**
     * Creates new form negados
     */
    public negados() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ajutarBox();
        atualizar();
    }

    public void chamarFiltros() {

        atualizar();

        DefaultTableModel tabela_negados;
        tabela_negados = (DefaultTableModel) exibir.getModel();
        int total = tabela_negados.getRowCount();
        for (int i = 0; i < total; i++) {
            String pesquisar = nome_text.getText().toLowerCase();
            if (!tabela_negados.getValueAt(i, 1).toString().toLowerCase().contains(pesquisar)) {
                tabela_negados.removeRow(i);
                i--;
                total--;
            }
        }

        total = tabela_negados.getRowCount();
        for (int i = 0; i < total; i++) {
            String pesquisar = matricula_text.getText().toLowerCase();
            if (!tabela_negados.getValueAt(i, 0).toString().toLowerCase().startsWith(pesquisar)) {
                tabela_negados.removeRow(i);
                i--;
                total--;
            }
        }

        if (item_liberado_box.getSelectedItem() != null && !item_liberado_box.getSelectedItem().toString().equals("todos")) {
            tabela_negados = (DefaultTableModel) exibir.getModel();
            total = tabela_negados.getRowCount();
            if (item_liberado_box.getSelectedItem().toString().equals("nenhum")) {
                for (int i = 0; i < total; i++) {
                    if (!tabela_negados.getValueAt(i, 2).toString().toLowerCase().equals("")) {
                        tabela_negados.removeRow(i);
                        i--;
                        total--;
                    }
                }
            }
            for (int i = 0; i < total; i++) {
                String pesquisar = item_liberado_box.getSelectedItem().toString().toLowerCase();
                if (!tabela_negados.getValueAt(i, 2).toString().toLowerCase().contains(pesquisar)) {
                    tabela_negados.removeRow(i);
                    i--;
                    total--;
                }
            }
        }
    }

    public DefaultTableModel atualizar() {
        DefaultTableModel tabela_negados;
        tabela_negados = (DefaultTableModel) exibir.getModel();
        BD banco = new BD();
        
        tabela_negados.setRowCount(0);
        List<tabela_liberados> lista_negados = banco.getTabela_Negados();

        for (tabela_liberados negado : lista_negados) {
            tabela_negados.addRow(new String[]{negado.getMatricula(), negado.getNome(), negado.getItem()});
        }

        int max = tabela_negados.getRowCount() - 1;
        for (int i = 0; i < max; i++) {
            if (tabela_negados.getValueAt(i, 0).equals(tabela_negados.getValueAt(i + 1, 0))) {
                tabela_negados.setValueAt(tabela_negados.getValueAt(i, 2) + ", " + tabela_negados.getValueAt(i + 1, 2), i, 2);
                tabela_negados.removeRow(i + 1);
                max--;
                i--;
            }
        }

        // Define um renderizador centralizado para todas as colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int columnIndex = 0; columnIndex < tabela_negados.getColumnCount(); columnIndex++) {
            exibir.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
        ((DefaultTableCellRenderer) exibir.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        return tabela_negados;
    }

    public void ajutarBox() {
        item_liberado_box.removeAllItems();
        item_liberado_box.addItem("todos");
        BD banco = new BD();
        List<String> Itens = banco.getAll_itens();
        for (String item : Itens) {
            item_liberado_box.addItem(item);
        }
        banco = null;
        item_liberado_box.addItem("nenhum");
        item_liberado_box.setSelectedItem("todos");
    }

    public void exportaParaExcel() throws IOException {

        JFileChooser seleccionar = new JFileChooser();
        File arquivo;
        if (seleccionar.showDialog(null, "Exportar Excel") == JFileChooser.APPROVE_OPTION) {

            arquivo = seleccionar.getSelectedFile();
            TableModel model = exibir.getModel();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            Row row;
            Cell cell;

            // write the column headers
            row = sheet.createRow(0);
            for (int c = 0; c < model.getColumnCount(); c++) {
                cell = row.createCell(c);
                cell.setCellValue(model.getColumnName(c));
            }

            // write the data rows
            for (int r = 0; r < model.getRowCount(); r++) {
                row = sheet.createRow(r + 1);
                for (int c = 0; c < model.getColumnCount(); c++) {
                    cell = row.createCell(c);
                    Object value = model.getValueAt(r, c);
                    cell.setCellValue((String) value.toString());
                }
            }

            FileOutputStream out = null;

            out = new FileOutputStream(arquivo + ".xlsx");

            workbook.write(out);
            out.close();
            workbook.close();
            JOptionPane.showMessageDialog(null, "Exportado com sucesso!");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        exibir = new javax.swing.JTable();
        matricula_text = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nome_text = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        item_liberado_box = new javax.swing.JComboBox<>();
        exportar = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("NEGADOS");

        exibir.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "matricula", "nome", "item(ns) negado(s)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        exibir.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(exibir);

        matricula_text.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));
        matricula_text.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                matricula_textKeyReleased(evt);
            }
        });

        jLabel2.setText("Matricula:");

        jLabel5.setText("Nome:");

        nome_text.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nome_textKeyReleased(evt);
            }
        });

        jLabel8.setText("item liberado");

        item_liberado_box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "todos" }));
        item_liberado_box.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                item_liberado_boxItemStateChanged(evt);
            }
        });

        exportar.setText("Exportar Excel");
        exportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(matricula_text, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                        .addComponent(nome_text, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(item_liberado_box, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(139, 139, 139)
                        .addComponent(jLabel8)
                        .addGap(52, 52, 52)))
                .addComponent(exportar)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel2))
                            .addComponent(jLabel8))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(nome_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(matricula_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(item_liberado_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(exportar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void item_liberado_boxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_item_liberado_boxItemStateChanged
        // TODO add your handling code here:
        chamarFiltros();
    }//GEN-LAST:event_item_liberado_boxItemStateChanged

    private void exportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportarActionPerformed
        try {
            // TODO add your handling code here:
            exportaParaExcel();
        } catch (IOException ex) {
            Logger.getLogger(negados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_exportarActionPerformed

    private void nome_textKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nome_textKeyReleased
        // TODO add your handling code here:
        chamarFiltros();
    }//GEN-LAST:event_nome_textKeyReleased

    private void matricula_textKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_matricula_textKeyReleased
        // TODO add your handling code here:
        chamarFiltros();
    }//GEN-LAST:event_matricula_textKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(negados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(negados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(negados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(negados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new negados().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable exibir;
    private javax.swing.JToggleButton exportar;
    private javax.swing.JComboBox<String> item_liberado_box;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JFormattedTextField matricula_text;
    private javax.swing.JTextField nome_text;
    // End of variables declaration//GEN-END:variables
}
