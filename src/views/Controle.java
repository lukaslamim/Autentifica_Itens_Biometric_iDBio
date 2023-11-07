package views;


import DAO.BD;
import banco.ConnectionBD;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
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
public final class Controle extends javax.swing.JFrame {

    /**
     * Creates new form RH
     */
    private File file = null;

    public Controle() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);

        //verifica se existe outra aplicação aberta
        verificarInicializar();

        if (ConnectionBD.FazerConeccao() == null) {
            JOptionPane.showMessageDialog(null, "Erro ao estabelecer conexão com o banco de dados, verifique a conexao com o servidor de terceiro;");
            System.exit(0);
        }

        // verificar qual o diretorio da aplicação
        File dir1 = new File(".");
        try {
            file = new File(dir1.getCanonicalPath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao tentar localizar sistema de arquivos, se o erro persistir, reinicie o computador");
            System.exit(0);
        }

        //define intervalo inical padrao
        LocalDateTime now = LocalDateTime.now();
        int mes = now.getMonthValue();
        int ano = now.getYear();
        de.setText("" + 25 + "" + mes + "" + ano);
        a.setText("" + 0 + "" + 5 + "" + mes + "" + ano);
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

        total = tabela.getRowCount();
        for (int i = 0; i < total; i++) {
            String pesquisar = matricula_text.getText().toLowerCase();
            if (!tabela.getValueAt(i, 0).toString().toLowerCase().contains(pesquisar)) {
                tabela.removeRow(i);
                i--;
                total--;
            }
        }
        if (item_liberado_box.getSelectedItem() != null && !item_liberado_box.getSelectedItem().toString().equals("todos")) {
            tabela = (DefaultTableModel) exibir.getModel();
            total = tabela.getRowCount();
            if (item_liberado_box.getSelectedItem().toString().equals("nenhum")) {
                for (int i = 0; i < total; i++) {
                    if (!tabela.getValueAt(i, 2).toString().toLowerCase().equals("")) {
                        tabela.removeRow(i);
                        i--;
                        total--;
                    }
                }
            }
            for (int i = 0; i < total; i++) {
                String pesquisar = item_liberado_box.getSelectedItem().toString().toLowerCase();
                if (!tabela.getValueAt(i, 2).toString().toLowerCase().contains(pesquisar)) {
                    tabela.removeRow(i);
                    i--;
                    total--;
                }
            }
        }
    }

    //verifica se existe outra aplicação aberta
    public void verificarInicializar() {
        // nao rodar mais de uma aplicação
        String userHome = System.getProperty("user.home");
        File ark = new File(userHome, "my.lock");
        try {
            FileChannel fc = FileChannel.open(ark.toPath(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);
            FileLock lock = fc.tryLock();
            if (lock == null) {
                JOptionPane.showMessageDialog(null, "Outra instacia da aplicação aberta, se o erro continuar, reinicie seu computador;");
                System.exit(0);
            }
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    //chama a funcao lerExcel que le o dados da planiljha base_dados
    public void AtualizaBanco() {

        DefaultTableModel tabela;
        tabela = (DefaultTableModel) exibir.getModel();
        tabela = atualizaTabela(exibir);
        chamarFiltros();
        // Define um renderizador centralizado para todas as colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int columnIndex = 0; columnIndex < tabela.getColumnCount(); columnIndex++) {
            exibir.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
        ((DefaultTableCellRenderer) exibir.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    }

    public boolean dataEstaEntre(String dataVerificarStr, String dataInicioStr, String dataFimStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Formato de data (dia/mês/ano)

        LocalDate dataVerificar = LocalDate.parse(dataVerificarStr, formatter);
        LocalDate dataInicio = LocalDate.parse(dataInicioStr, formatter);
        LocalDate dataFim = LocalDate.parse(dataFimStr, formatter);

        return !dataVerificar.isBefore(dataInicio) && !dataVerificar.isAfter(dataFim);
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

    public DefaultTableModel atualizaTabela(JTable TABELA) {
        BD banco = new BD();
        DefaultTableModel tabela;
        tabela = (DefaultTableModel) TABELA.getModel();
        //tabela.addRow(new String[]{"", "" ,"" , "" ,""});

        List<Integer> lista_IDs = banco.getAll_IDs_colaborador();
        for (Integer id : lista_IDs) {
            List<Integer> lista_IDs_entregas = banco.getAll_Entregas(id);
            for (Integer id_entrega : lista_IDs_entregas) {
                String matricula = banco.getTabelaDados(id).getMatricula();
                String nome = banco.getTabelaDados(id).getNome();
                nome = this.formatarNome(nome);
                String data = banco.getEntrega(id_entrega).getData();
                String item = banco.getEntrega(id_entrega).getItem_entregue();
                String DE = de.getText();
                String A = de.getText();

                if (DE.equals("  /  /    ") && A.equals("  /  /    ")) {
                    //String entrega = banco.getEntrega(banco.getColaborador(banco.getDados(matricula).getId()).getId()).getItem_entregue();
                    int maxValue = tabela.getRowCount();
                    for (int rowIndex = 0; rowIndex < maxValue; rowIndex++) {
                        if (tabela.getValueAt(rowIndex, 0).toString().equals(banco.getTabelaDados(id).getMatricula())
                                && tabela.getValueAt(rowIndex, 3).toString().isEmpty()) {
                            tabela.removeRow(rowIndex);
                            break;
                        }
                    }
                    tabela.addRow(new String[]{matricula, nome, item, data});
                } else {
                    String dataTemp = converterCurrentTimestampParaFormato(data);
                    if (dataEstaEntre(dataTemp, DE, A)) {
                        tabela.addRow(new String[]{matricula, nome, item, data});
                    }
                }
            }

        }
        banco = null;
        return tabela;
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

    //inseri nas tabelas dados , habilitado_a_receber e item_entregue e tabelas locais
    public boolean lerExcel_dados(String caminho) {
        FileInputStream arquivo = null;
        try {
            arquivo = new FileInputStream(new File(caminho));

            // Crie um objeto Workbook a partir do arquivo XLSX
            Workbook workbook = new XSSFWorkbook(arquivo);

            // Obtenha a primeira planilha no arquivo (índice 0)
            Sheet planilha = workbook.getSheetAt(0);

            // Itere pelas linhas da planilha
            for (int rowIndex = 1; rowIndex <= planilha.getLastRowNum(); rowIndex++) {
                Row linha = planilha.getRow(rowIndex);
                if (linha != null) {
                    // Obtenha o valor das duas primeiras colunas (índices 0 e 1)
                    Cell cell1 = linha.getCell(0);
                    Cell cell2 = linha.getCell(1);
                    Cell cell3 = linha.getCell(2);
                    Cell cell4 = linha.getCell(3);
                    Cell cell5 = linha.getCell(4);

                    if (cell1 != null && cell2 != null) {
                        //formta os itens e separa eles
                        BD banco = new BD();
                        List<String> lista_itens_banco = banco.getAll_itens();

                        if (cell3 != null) {
                            String[] itens = cell3.toString().replaceAll(" , ", ",").replaceAll(", ", ",").replaceAll(" ,", ",").replace(" ", "").split(",");

                            //confere se os tens ja existem no banco de dados
                            boolean achou = false;
                            for (String item : itens) {
                                for (String item_banco : lista_itens_banco) {
                                    if (item_banco.equals(item)) {
                                        achou = true;
                                        break;
                                    }
                                }
                                //se nao existem
                                if (!achou && !item.isEmpty()) {
                                    banco.inserir_item_entregue(item);
                                    achou = false;
                                }
                            }
                        }
                        //formata a matricula e o nome 
                        String matricula = cell1.toString().replaceAll("[^0-9]", "");
                        String nome = cell2.toString();
                        if (matricula.isEmpty() || nome.isEmpty()) {
                            return false;
                        }
                        matricula = matricula.substring(0, matricula.length() - 1);
                        //atualiza banco de dados
                        if (!matricula.equals("0")) {
                            //atualiza banco de dados
                            if (!banco.getDados(matricula).getMatricula().equals(matricula)) {
                                banco.inserir_dados(matricula, nome);
                            } else {
                                banco.updateDados(banco.getDados(matricula).getId(), nome);
                            }
                            //deleta habilitação de item
                            int id_dados = banco.getDados(matricula).getId();
                            banco.deleteAll_habilitado(id_dados);
                            if (cell3 != null && !cell3.toString().isEmpty() && cell4 == null || cell3 != null && !cell3.toString().isEmpty() && cell4 != null && cell4.toString().isEmpty()) {
                                String[] itens_hablitados = cell3.toString().replaceAll(" , ", ",").replaceAll(", ", ",").replaceAll(" ,", ",").split(",");
                                for (String item : itens_hablitados) {
                                    banco.inserir_hablitado(banco.getId_item(item), id_dados);
                                }
                            }
                        }
                        banco = null;
                        //ajusta dropbox de itens
                        ajutarBox();
                    }
                    //apaga dado da lista de apagar 
                    if (cell5 != null && !cell5.toString().isEmpty()) {
                        String apagar = cell5.toString().replaceAll("[^0-9]", "");
                        BD banco = new BD();
                        int id_dados = banco.getDados(apagar).getId();
                        int id_colaborador = banco.getColaborador(id_dados).getId();
                        int id_digital = banco.getDigital(id_colaborador);

                        banco.deleteAll_habilitado(id_dados);
                        banco.deleteDigital(id_digital);
                        banco.deleteColaborador(id_colaborador);
                        banco.deleteDados(id_dados);
                        banco = null;
                    }
                }
            }
            arquivo.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erro na planilha base_dados.xlsx, verifique se panilha esta no lugar correto!");
            Logger.getLogger(Identificacao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro na planilha base_dados.xlsx, feche a planilha de dados!");
            Logger.getLogger(Identificacao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
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

    public String formatarNome(String nomeCompleto) {

        // Dividir o nome completo em nome e sobrenome
        String[] partesNome = nomeCompleto.split(" ");

        if (partesNome.length >= 2) {
            String nome = partesNome[0];
            String sobrenome = partesNome[partesNome.length - 1];

            return nome + " " + sobrenome;
        }
        return nomeCompleto;
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
        exportar = new javax.swing.JToggleButton();
        ocorrencias = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        periodo = new javax.swing.JButton();
        de = new javax.swing.JFormattedTextField();
        a = new javax.swing.JFormattedTextField();
        nome_text = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        matricula_text = new javax.swing.JFormattedTextField();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        item_liberado_box = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CONTROLE");

        exibir.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "matricula", "nome", "item entregue", "data"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        exibir.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(exibir);

        exportar.setText("Exportar Excel");
        exportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportarActionPerformed(evt);
            }
        });

        ocorrencias.setText("Ocorrencias");
        ocorrencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ocorrenciasActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("A:");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("DE:");

        periodo.setText("Definir Periodo");
        periodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                periodoActionPerformed(evt);
            }
        });

        try {
            de.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        de.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        try {
            a.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        a.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        nome_text.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nome_textKeyReleased(evt);
            }
        });

        jLabel2.setText("Matricula:");

        jLabel5.setText("Nome:");

        jLabel1.setText("Tabelas");

        jLabel6.setText("Funções");

        matricula_text.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));
        matricula_text.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                matricula_textKeyReleased(evt);
            }
        });

        jButton1.setText("limpar filtros");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setText("item liberado");

        item_liberado_box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "todos" }));
        item_liberado_box.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                item_liberado_boxItemStateChanged(evt);
            }
        });

        jButton2.setText("Atualiza Base");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Liberados");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Negados");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(matricula_text, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel2)))
                        .addGap(52, 52, 52)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(nome_text, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(de, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(a, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(periodo))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel5)))
                        .addGap(0, 64, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ocorrencias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(item_liberado_box, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jLabel1)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(17, 17, 17))
                                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                                    .addComponent(exportar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(36, 36, 36))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(matricula_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nome_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(de, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(a, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(periodo)
                    .addComponent(jButton1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(item_liberado_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ocorrencias)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(exportar)
                        .addGap(32, 32, 32))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportarActionPerformed
        try {
            // TODO add your handling code here:
            exportaParaExcel();

        } catch (IOException ex) {
            Logger.getLogger(Controle.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_exportarActionPerformed

    private void periodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_periodoActionPerformed
        // TODO add your handling code here:
        if (de.getText().equals("  /  /    ") && a.getText().equals("  /  /    ")) {
            AtualizaBanco();
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
                AtualizaBanco();
            }
        }
    }//GEN-LAST:event_periodoActionPerformed

    private void ocorrenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ocorrenciasActionPerformed
        // TODO add your handling code here:
        Ocorrencias Ocorrencias = new Ocorrencias();
        Ocorrencias.setVisible(true);

    }//GEN-LAST:event_ocorrenciasActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        matricula_text.setText("");
        nome_text.setText("");
        de.setText("");
        a.setText("");
        item_liberado_box.setSelectedItem("todos");
        AtualizaBanco();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void item_liberado_boxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_item_liberado_boxItemStateChanged
        // TODO add your handling code here:
        this.AtualizaBanco();
    }//GEN-LAST:event_item_liberado_boxItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        liberados Liberados = new liberados();
        Liberados.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        negados Negados = new negados();
        Negados.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void nome_textKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nome_textKeyReleased
        // TODO add your handling code here:
        this.AtualizaBanco();
    }//GEN-LAST:event_nome_textKeyReleased

    private void matricula_textKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_matricula_textKeyReleased
        // TODO add your handling code here:
        this.AtualizaBanco();

    }//GEN-LAST:event_matricula_textKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //atualiza a tabalea dados e a tabela habitado_a_receber
        String senha = JOptionPane.showInputDialog("Insira a senha:");
        if (senha != null && senha.equals("1369")) {
            int liberacao = JOptionPane.showInternalConfirmDialog(null, "Deseja Atualizar a base de dados?", "AVISO", JOptionPane.YES_NO_OPTION);
            if (liberacao == JOptionPane.YES_OPTION) {
                String caminho = file.getAbsolutePath() + File.separator + "arquivos_complementares" + File.separator + "base_dados.xlsx";

                if (!lerExcel_dados(caminho)) {
                    JOptionPane.showMessageDialog(null, "Erro na planilha base_dados.xlsx, qualquer duvida substitua a plinalha de dados pela de back-up");
                    System.exit(0);
                }
                JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!");
            }
        }

    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(Controle.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Controle().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField a;
    private javax.swing.JFormattedTextField de;
    private javax.swing.JTable exibir;
    private javax.swing.JToggleButton exportar;
    private javax.swing.JComboBox<String> item_liberado_box;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JFormattedTextField matricula_text;
    private javax.swing.JTextField nome_text;
    private javax.swing.JButton ocorrencias;
    private javax.swing.JButton periodo;
    // End of variables declaration//GEN-END:variables
}
