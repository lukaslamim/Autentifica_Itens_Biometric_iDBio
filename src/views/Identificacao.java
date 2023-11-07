package views;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import CIDBio.CIDBio;
import DAO.BD;
import banco.ConnectionBD;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

/**
 *
 * @author 09303940970-OTIFIT
 */
//sql, usar caso o banco resetar, sem o *
/**
 *
 *
 */
public final class Identificacao extends javax.swing.JFrame {

    private File file = null;
    private String itemLiberado = "generico";
    private int minScore = 17000; // ate 20000
    private int minQuality = 85; // ate 100

    /**
     * Creates new form TelaPrincipal
     */
    public Identificacao() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        BD banco = new BD();
        // verificar qual o diretorio da aplicação
        File dir1 = new File(".");
        try {
            file = new File(dir1.getCanonicalPath());
        } catch (IOException e) {
        }

        // verifica se tem conecção com o banco
        if (ConnectionBD.FazerConeccao() == null) {
            JOptionPane.showMessageDialog(null, "Erro ao estabelecer conexão com o banco de dados, verifique a conexao com o servidor de terceiro;");
            System.exit(0);
        } else {
            // cria pasta de som
            File file_som = new File("C:\\Users\\Public\\apprh\\som");

            file_som.mkdirs();

            //baixa som do banco
            File liberado = new File(file.getAbsolutePath() + File.separator + "arquivos_complementares" + File.separator + "liberado.wav");
            File not_liberado = new File(file.getAbsolutePath() + File.separator + "arquivos_complementares" + File.separator + "not_liberado.wav");

            if (!liberado.exists()) {
                banco.RecuperarSomLiberado();
                banco.UploadSom();
            }

            if (!not_liberado.exists()) {
                banco.RecuperarSomNotLiberado();
                banco.UploadSom2();
            }
            // nao rodar mais de uma aplicação
            String userHome = System.getProperty("user.home");
            File ark = new File(userHome, "my.lock_cad");
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
            startPrograma();
        }
    }

    public String formatarNome(String nomeCompleto) {

        // Dividir o nome completo em nome e sobrenome
        String[] partesNome = nomeCompleto.split(" ");

        if (partesNome.length >= 2) {
            String nome = partesNome[0];
            String sobrenome = partesNome[partesNome.length - 1];

            System.out.println("Nome: " + nome);
            System.out.println("Sobrenome: " + sobrenome);
        }
        return nomeCompleto;
    }

    public void fazerBaruilhoLiberado(String nome) {
        //roda audio de liberação
        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(
                    new File("C:\\Users\\Public\\apprh\\som" + File.separator + "liberado.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(0);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger
                    .getLogger(Identificacao.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

        //alerta de liberado
        alerta_cima.setText("USUARIO:");
        alerta_centro.setText(formatarNome(nome));
        alerta_baixo.setText("LIBERADO");
        alerta.setBackground(Color.green);
    }

    public void fazerBaruilhoNotLiberado(String nome) {
        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(
                    new File("C:\\Users\\Public\\apprh\\som" + File.separator + "not_liberado.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(0);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger
                    .getLogger(Identificacao.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
        //alerta de liberado
        alerta_cima.setText("USUARIO:");
        alerta_centro.setText(formatarNome(nome));
        alerta_baixo.setText("NAO LIBERADO");
        alerta.setBackground(Color.red);
    }

    public boolean digitalExiste(String Digital) {
        BD banco = new BD();
        CIDBio iBio = new CIDBio();

        List<Integer> lista_IDs = banco.getAll_IDs_colaborador();

        for (Integer id : lista_IDs) {

            List<String> lista_digitais = banco.getDigitais(id).getLista_digitais();

            for (String digital : lista_digitais) {
                if (iBio.MatchTemplates(Digital, digital).getScore() > minScore) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean liberacao(String Digital) {
        BD banco = new BD();
        CIDBio iBio = new CIDBio();

        List<Integer> lista_IDs = banco.getAll_IDs_digital();
        int digitalVector[] = digitalVector(lista_IDs);
        int contador = 0;

        for (Integer id : lista_IDs) {
            List<String> lista_digitais = banco.getDigitais(id).getLista_digitais();
            for (String digital : lista_digitais) {
                if (iBio.MatchTemplates(Digital, digital).getScore() > minScore) {
                    int id_digital = digitalVector[contador];
                    int id_dados = banco.getColaboradorID(id_digital).getId_dados();
                    List<String> lista_itens_habilitados = banco.getHablitado(id_dados);
                    List<String> lista_itens = banco.getAll_itens();
                    for (String item_hablitado : lista_itens_habilitados) {
                        for (String item : lista_itens) {
                            if (item.equals(item_hablitado)) {
                                itemLiberado = item;
                                //exclui liberacao de item
                                banco.deleteHabilitado(banco.getId_hablitado(id_dados, banco.getId_item(item)));
                                return true;
                            }
                        }
                    }
                }
            }
            contador++;
        }
        return false;
    }

    public String[] getInfo(String Digital) {
        BD banco = new BD();
        CIDBio iBio = new CIDBio();
        String infoVector[] = new String[2];
        List<Integer> lista_IDs = banco.getAll_IDs_digital();
        int digitalVector[] = digitalVector(lista_IDs);
        int contador = 0;
        infoVector[0] = "error";
        infoVector[1] = "error";
        for (Integer id : lista_IDs) {
            List<String> lista_digitais = banco.getDigitais(id).getLista_digitais();
            for (String digital : lista_digitais) {
                if (iBio.MatchTemplates(Digital, digital).getScore() > minScore) {
                    int id_digital = digitalVector[contador];
                    int id_dados = banco.getColaboradorID(id_digital).getId_dados();
                    infoVector[0] = banco.getTabelaDados(banco.getColaborador(id_dados).getId()).getMatricula();
                    infoVector[1] = banco.getTabelaDados(banco.getColaborador(id_dados).getId()).getNome();
                }
            }
            contador++;
        }
        return infoVector;
    }

    public String qualDedo(int id_digital) {
        BD banco = new BD();
        int contador = 1;
        List<String> lista_digitais = banco.getDigitais(id_digital).getLista_digitais();
        for (String digital : lista_digitais) {
            String dedo = "digital_";
            if (digital.equals("")) {
                dedo = dedo + "" + contador;
                return dedo;
            }
            contador++;
        }
        return "";
    }

    public Object[] itensVector(List<String> listaItens) {
        Object[] vetorDeObjetos = new Object[listaItens.size()];

        for (int i = 0; i < listaItens.size(); i++) {
            vetorDeObjetos[i] = listaItens.get(i);
        }
        return vetorDeObjetos;
    }

    public int[] digitalVector(List<Integer> listaItens) {
        Object[] vetorDeObjetos = new Object[listaItens.size()];

        for (int i = 0; i < listaItens.size(); i++) {
            vetorDeObjetos[i] = listaItens.get(i);
        }
        int[] vetorDeInteiros = new int[vetorDeObjetos.length];

        for (int i = 0; i < vetorDeObjetos.length; i++) {
            if (vetorDeObjetos[i] instanceof Integer) {
                vetorDeInteiros[i] = (int) vetorDeObjetos[i];
            } else {
                // Lida com objetos que não podem ser convertidos em inteiros
                // Pode ser lançada uma exceção ou atribuir um valor padrão, dependendo do seu caso.
                vetorDeInteiros[i] = 0; // Valor padrão, você pode escolher outro valor se preferir.
            }
        }
        return vetorDeInteiros;
    }

    public void startPrograma() {
        new Thread(() -> {
            CIDBio iBio = new CIDBio();
            BD banco = new BD();

            while (true) {
                var check = iBio.CheckFingerprint();
                String retName = check.getRetCode().name();
                switch (retName) {
                    case "ERROR_NO_FINGER_DETECTED" -> {
                        alerta_centro.setText("CONECTADO");
                        alerta_cima.setText("");
                        alerta_baixo.setText("AGUARDANDO DIGITAL");
                        alerta.setBackground(Color.blue);

                        //captura digital
                        var raw = iBio.CaptureImageAndTemplate();
                        while (raw.getQuality() < minQuality) {
                            alerta_cima.setText("POSICIONE O DEDO CORRETAMENTE");
                            raw = iBio.CaptureImageAndTemplate();
                        }
                        String digital_capturada = raw.getTemplateString();
                        //verificação de digitais
                        if (digitalExiste(digital_capturada)) {

                            //permitido
                            if (liberacao(digital_capturada)) {

                                int id_colaborador = banco.getColaborador(banco.getDados(getInfo(digital_capturada)[1]).getId()).getId();
                                //roda audio de liberação
                                fazerBaruilhoLiberado(banco.getTabelaDados(id_colaborador).getNome());

                                banco.inserir_entrega(id_colaborador, itemLiberado);

                            } //nao permitido
                            else {
                                String infoVectorTe[] = this.getInfo(digital_capturada);
                                String justificativa = "Alerta de Sistema usuario: " + infoVectorTe[1] + " negado; " + "matricula: " + infoVectorTe[0];
                                fazerBaruilhoNotLiberado(infoVectorTe[1]);
                                banco.inserir_ocorrencias(infoVectorTe[0], "colaborador em negado", justificativa);
                            }
                            //adormece a thread por 3 segundos
                            try {
                                Thread.sleep(Duration.ofSeconds(3));

                            } catch (InterruptedException ex) {
                                Logger.getLogger(Identificacao.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            alerta_baixo.setText("");
                            alerta_cima.setText("");
                            alerta_centro.setText("CADASTRO");
                            alerta.setBackground(Color.YELLOW);
                            String Matricula = JOptionPane.showInputDialog("Insira a matricula:");
                            String ErroMatricula = Matricula;
                            int contador = 0;
                            while (banco.getDados(Matricula).getMatricula().equals("0")) {
                                if (Matricula == null) {
                                    break;
                                }
                                JOptionPane.showMessageDialog(null, "Matricula não encontrada");
                                Matricula = JOptionPane.showInputDialog("Insira a matricula:");
                                ErroMatricula += ", " + Matricula;
                                contador++;
                                if (contador % 3 == 0) {

                                    JOptionPane.showMessageDialog(null, "Matricula não encontrada, favor comunicar ao setor responsavel");
                                    int liberacao = JOptionPane.showInternalConfirmDialog(null, "Deseja liberar sem passar digital?", "AVISO", JOptionPane.YES_NO_OPTION);
                                    if (liberacao == JOptionPane.YES_OPTION) {

                                        String senha = JOptionPane.showInputDialog("Insira a senha:");
                                        if (senha.equals("1369")) {
                                            String nome = JOptionPane.showInputDialog("nome do colaborador:");
                                            String justificativa = JOptionPane.showInputDialog("justifique:");
                                            justificativa += "\n matriculas tentadas:" + ErroMatricula;
                                            int item = JOptionPane.showInternalOptionDialog(null, "qual item esta sendo liberado", "liberação manual", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, itensVector(banco.getAll_itens()), null);
                                            banco.inserir_ocorrencias(nome, banco.getAll_itens().get(item).toString(), justificativa);
                                            JOptionPane.showMessageDialog(null, "Liberado");
                                            Matricula = null; 
                                            break;
                                        }
                                    }
                                }
                            }
                            //verifica se existe alguma digital vinculada
                            int id_colaborador = banco.getColaborador(banco.getDados(Matricula).getId()).getId();
                            if (id_colaborador != 0) {
                                int id_digital = banco.getDigital(id_colaborador);
                                banco.updateDigital(id_digital, digital_capturada, qualDedo(id_digital));
                            } //sem vinculo de digital
                            else if(Matricula != null){
                                banco.inserir_colaborador(banco.getDados(Matricula).getId(), banco.inserir_digital(digital_capturada, "digital_1"));
                            }
                        }
                    }

                    default -> {
                        alerta_centro.setText("\\o ERRO o/");
                        alerta_baixo.setText(check.getRetCode().name());
                        alerta_cima.setText("");
                        alerta.setBackground(Color.red);
                        iBio.CancelCapture();
                        //adormece a thread por 3 segundos
                        try {
                            Thread.sleep(Duration.ofSeconds(3));

                        } catch (InterruptedException ex) {
                            Logger.getLogger(Identificacao.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }
        }).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        alerta = new javax.swing.JPanel();
        alerta_cima = new javax.swing.JLabel();
        alerta_centro = new javax.swing.JLabel();
        alerta_baixo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("IDENTIFICAÇÃO");

        alerta.setBackground(new java.awt.Color(204, 0, 0));

        alerta_cima.setFont(new java.awt.Font("sansserif", 0, 36)); // NOI18N
        alerta_cima.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        alerta_cima.setToolTipText("");

        alerta_centro.setFont(new java.awt.Font("sansserif", 0, 36)); // NOI18N
        alerta_centro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        alerta_centro.setText("DESCONECTADO");
        alerta_centro.setToolTipText("");

        alerta_baixo.setFont(new java.awt.Font("sansserif", 0, 36)); // NOI18N
        alerta_baixo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        alerta_baixo.setToolTipText("");

        javax.swing.GroupLayout alertaLayout = new javax.swing.GroupLayout(alerta);
        alerta.setLayout(alertaLayout);
        alertaLayout.setHorizontalGroup(
            alertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(alerta_cima, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(alertaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(alerta_centro, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(alertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(alertaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(alerta_baixo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        alertaLayout.setVerticalGroup(
            alertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alertaLayout.createSequentialGroup()
                .addComponent(alerta_cima, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alerta_centro, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(127, Short.MAX_VALUE))
            .addGroup(alertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alertaLayout.createSequentialGroup()
                    .addContainerGap(208, Short.MAX_VALUE)
                    .addComponent(alerta_baixo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(alerta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(alerta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Identificacao.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Identificacao().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel alerta;
    private javax.swing.JLabel alerta_baixo;
    private javax.swing.JLabel alerta_centro;
    private javax.swing.JLabel alerta_cima;
    // End of variables declaration//GEN-END:variables
}
