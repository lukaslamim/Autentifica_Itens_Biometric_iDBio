package views;


import DAO.BD;
import classes.ocorrencias;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
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
 * @author lucasantonio-fit
 */
public final class Ocorrencias extends javax.swing.JFrame {

    /**
     * Creates new form ocorrencias
     */
    public Ocorrencias() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        ((DefaultTableCellRenderer) exibir.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        LocalDateTime now = LocalDateTime.now();
        //int dia = now.getDayOfMonth();
        int mes = now.getMonthValue() - 1;
        int mes2 = now.getMonthValue();
        int ano = now.getYear();
        de.setText("" + 25 + "" + mes + "" + ano);
        a.setText("" + 0 + "" + 5 + "" + mes2 + "" + ano);

        atualizaTabela_ocorrencias();
    }

    public void chamarFiltros() {
        DefaultTableModel tabela;
        tabela = (DefaultTableModel) exibir.getModel();

        int total = tabela.getRowCount();
        for (int i = 0; i < total; i++) {
            String pesquisar = nome_text.getText().toLowerCase();
            if (!tabela.getValueAt(i, 1).toString().toLowerCase().contains(pesquisar)) {
                tabela.removeRow(i);
                i--;
                total--;
            }
        }

        if (jCheckBox1.isSelected()) {
            for (int i = 0; i < total; i++) {
                if (tabela.getValueAt(i, 2).toString().equals("colaborador em negado")) {
                    tabela.removeRow(i);
                    i--;
                    total--;
                }
            }
        }

        String DE = de.getText();
        String A = a.getText();
        total = tabela.getRowCount();
        for (int rowIndex = 0; rowIndex < total; rowIndex++) {
            String dataTemp = converterCurrentTimestampParaFormato(tabela.getValueAt(rowIndex, 1).toString());
            if (!dataEstaEntre(dataTemp, DE, A)) {
                tabela.removeRow(rowIndex);
            }
        }

        // Define um renderizador centralizado para todas as colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int columnIndex = 0; columnIndex < tabela.getColumnCount(); columnIndex++) {
            exibir.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
    }

    public static String converterCurrentTimestampParaFormato(String currentTimestampStr) {
        try {
            // Criar um SimpleDateFormat para o formato da string SQL
            SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Fazer o parsing da string para obter um objeto Date
            Date date = sqlDateFormat.parse(currentTimestampStr);

            // Formatá-lo no formato desejado (dia/mês/ano)
            SimpleDateFormat formatoDesejado = new SimpleDateFormat("dd/MM/yyyy");
            String dataFormatada = formatoDesejado.format(date);

            return dataFormatada;
        } catch (ParseException e) {
            return null;
        }
    }

    public boolean dataEstaEntre(String dataVerificarStr, String dataInicioStr, String dataFimStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Formato de data (dia/mês/ano)

        LocalDate dataVerificar = LocalDate.parse(dataVerificarStr, formatter);
        LocalDate dataInicio = LocalDate.parse(dataInicioStr, formatter);
        LocalDate dataFim = LocalDate.parse(dataFimStr, formatter);

        return !dataVerificar.isBefore(dataInicio) && !dataVerificar.isAfter(dataFim);
    }

    public void exportaParaExcel() throws IOException {

        JFileChooser seleccionar = new JFileChooser();
        File arquivo;
        if (seleccionar.showDialog(null, "Exportar Excel") == JFileChooser.APPROVE_OPTION) {

            arquivo = seleccionar.getSelectedFile();
            TableModel model = getExibir().getModel();
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

    public void atualizaTabela_ocorrencias() {
        BD banco = new BD();
        DefaultTableModel tabela;
        tabela = (DefaultTableModel) exibir.getModel();
        tabela.setRowCount(0);
        List<ocorrencias> lista_ocorrencias = banco.getAll_ocorrencias();
        for (ocorrencias ocorrencia : lista_ocorrencias) {
            tabela.addRow(new String[]{ocorrencia.getNome(), ocorrencia.getData(), ocorrencia.getItem_liberado(), ocorrencia.getJustificativa()});
        }
        banco = null;
        chamarFiltros();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        exibir = new javax.swing.JTable();
        exportar = new javax.swing.JButton();
        periodo1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        a = new javax.swing.JFormattedTextField();
        de = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        nome_text = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OCORRENCIAS");

        exibir.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "nome", "data", "liberado", "justificativa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(exibir);

        exportar.setText("Expotar Excel");
        exportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportarActionPerformed(evt);
            }
        });

        periodo1.setText("Definir Periodo");
        periodo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                periodo1ActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("A:");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("DE:");

        try {
            a.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        a.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        try {
            de.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        de.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel5.setText("Nome:");

        nome_text.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nome_textKeyReleased(evt);
            }
        });

        jCheckBox1.setText("Liberado");
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(nome_text, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(de, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(periodo1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(exportar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
            .addGroup(layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nome_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(de, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(a, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(periodo1)
                    .addComponent(exportar)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportarActionPerformed
        try {
            // TODO add your handling code here:
            exportaParaExcel();
        } catch (IOException ex) {
            Logger.getLogger(Ocorrencias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_exportarActionPerformed

    private void periodo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_periodo1ActionPerformed
        // TODO add your handling code here:
        if (de.getText().equals("  /  /    ") && a.getText().equals("  /  /    ")) {
            atualizaTabela_ocorrencias();
        } else {
            // Crie um formato para a data no formato "dd/MM/yyyy"
            SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
            // Tente analisar a data
            try {
                Date DE = formatoData.parse(de.getText());
                Date A = formatoData.parse(a.getText());
                // Se a análise for bem-sucedida, a data está no formato correto

            } catch (ParseException e) {
                // Se ocorrer uma exceção ParseException, a data não está no formato correto
                JOptionPane.showMessageDialog(null, "formato errado de data, formato certo: 01/01/2001");
            } finally {
                atualizaTabela_ocorrencias();
            }
        }
    }//GEN-LAST:event_periodo1ActionPerformed

    private void nome_textKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nome_textKeyReleased
        // TODO add your handling code here:
        atualizaTabela_ocorrencias();
    }//GEN-LAST:event_nome_textKeyReleased

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged
        // TODO add your handling code here:
        atualizaTabela_ocorrencias();
    }//GEN-LAST:event_jCheckBox1StateChanged

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ocorrencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ocorrencias().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField a;
    private javax.swing.JFormattedTextField de;
    private javax.swing.JTable exibir;
    private javax.swing.JButton exportar;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField nome_text;
    private javax.swing.JButton periodo1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the exibir
     */
    public javax.swing.JTable getExibir() {
        return exibir;
    }
}
