/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import banco.ConnectionBD;
import classes.colaborador;
import classes.dados;
import classes.digital;
import classes.entrega;
import classes.ocorrencias;
import classes.tabela_controle;
import classes.tabela_liberados;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

/**
 *
 * @author lucasantonio-fit
 */
public class BD {

    //upa o som no banco de dados
    
    public void UploadSom() {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "insert into sons (id, nome, arquivo) values (default, ? , ?)";
            String caminho = "C:\\liberado.wav";
            InputStream in = new FileInputStream(caminho);
            pst = conect.prepareStatement(query);

            pst.setString(1, "liberado");
            pst.setBlob(2, in);

            pst.executeUpdate();
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
        } finally {;
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }
    }
    
    public void UploadSom2() {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "insert into sons (id, nome, arquivo) values (default, ? , ?)";
            String caminho = "C:\\nao_liberado.wav";
            InputStream in = new FileInputStream(caminho);
            pst = conect.prepareStatement(query);

            pst.setString(1, "not_liberado");
            pst.setBlob(2, in);

            pst.executeUpdate();
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
        } finally {;
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }
    }
     
    //recupera som do banco
    public void RecuperarSomLiberado() {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = conect.createStatement();
            String query = ("select * from sons where id = 1");

            rs = st.executeQuery(query);

            while (rs.next()) {
                Blob blob = rs.getBlob("arquivo");
                String nome = rs.getString("nome");

                byte byteArray[] = blob.getBytes(1, (int) blob.length());

                FileOutputStream outPutStream = new FileOutputStream(
                        "C:\\Users\\Public\\apprh\\som" + File.separator + "liberado.wav");
                outPutStream.write(byteArray);
            }
            st.close();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
    }

    public void RecuperarSomNotLiberado() {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conect.createStatement();
            String query = ("select * from sons where id = 2");

            rs = st.executeQuery(query);

            while (rs.next()) {
                Blob blob = rs.getBlob("arquivo");
                String nome = rs.getString("nome");

                byte byteArray[] = blob.getBytes(1, (int) blob.length());

                FileOutputStream outPutStream = new FileOutputStream(
                        "C:\\Users\\Public\\apprh\\som" + File.separator + "not_liberado.wav");
                outPutStream.write(byteArray);
            }
            st.close();

        } catch (SQLException | IOException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
    }

    //inserts
    public void inserir_item_entregue(String item) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "insert into item_entregue (id, item_nome) values (default, ?)";
            pst = conect.prepareStatement(query);

            pst.setString(1, item);

            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }

    }

    public void inserir_hablitado(int id_item, int id_dados) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "insert into habilitado_a_receber (id, id_item, id_dados) values (default, ?, ?)";
            pst = conect.prepareStatement(query);

            pst.setInt(1, id_item);
            pst.setInt(2, id_dados);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }

    }

    public void inserir_colaborador(int dados, int id_digital) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "insert into colaborador (id, id_dados, id_digital) values (default, ? , ?)";
            pst = conect.prepareStatement(query);

            pst.setInt(1, dados);
            pst.setInt(2, id_digital);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }

    }

    public void inserir_dados(String matricula, String nome) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "insert into dados (id, matricula, nome) values (default, ? , ?)";
            pst = conect.prepareStatement(query);

            pst.setString(1, matricula);
            pst.setString(2, nome);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }

    }

    public int inserir_digital(String digital, String qualDedo) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        int id = 0;
        try {
            switch (qualDedo) {
                case "digital_1" -> {

                    String query = "insert into digital (id, digital_1) values (default, ?)";
                    pst = conect.prepareStatement(query);

                    pst.setString(1, digital);

                    pst.executeUpdate();

                }
                case "digital_2" -> {

                    String query = "insert into digital (id, digital_2) values (default, ?)";
                    pst = conect.prepareStatement(query);

                    pst.setString(1, digital);

                    pst.executeUpdate();

                }
                case "digital_3" -> {

                    String query = "insert into digital (id, digital_3) values (default, ?)";
                    pst = conect.prepareStatement(query);

                    pst.setString(1, digital);

                    pst.executeUpdate();

                }
                case "digital_4" -> {

                    String query = "insert into digital (id, digital_4) values (default, ?)";
                    pst = conect.prepareStatement(query);

                    pst.setString(1, digital);

                    pst.executeUpdate();

                }
                case "digital_5" -> {

                    String query = "insert into digital (id, digital_5) values (default, ?)";
                    pst = conect.prepareStatement(query);

                    pst.setString(1, digital);

                    pst.executeUpdate();

                }
                case "digital_6" -> {

                    String query = "insert into digital (id, digital_6) values (default, ?)";
                    pst = conect.prepareStatement(query);

                    pst.setString(1, digital);

                    pst.executeUpdate();

                }
                case "digital_7" -> {

                    String query = "insert into digital (id, digital_7) values (default, ?)";
                    pst = conect.prepareStatement(query);

                    pst.setString(1, digital);

                    pst.executeUpdate();

                }
                case "digital_8" -> {

                    String query = "insert into digital (id, digital_8) values (default, ?)";
                    pst = conect.prepareStatement(query);

                    pst.setString(1, digital);

                    pst.executeUpdate();

                }
                case "digital_9" -> {

                    String query = "insert into digital (id, digital_9) values (default, ?)";
                    pst = conect.prepareStatement(query);

                    pst.setString(1, digital);

                    pst.executeUpdate();
                }

                case "digital_10" -> {

                    String query = "insert into digital (id, digital_10) values (default, ?)";
                    pst = conect.prepareStatement(query);

                    pst.setString(1, digital);

                    pst.executeUpdate();

                }
                default -> {
                }
            }
            // Após a inserção, obtenha o ID da linha inserida
            String query = "SELECT LAST_INSERT_ID() AS id";
            ResultSet resultSet = pst.executeQuery(query);

            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }
        return id;
    }

    public void inserir_entrega(int id_colaborador, String item_entregue) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "insert into entrega (id, id_colaborador, date, item_entregue) values (default, ?, default, ?)";
            pst = conect.prepareStatement(query);

            pst.setInt(1, id_colaborador);
            pst.setString(2, item_entregue);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }
    }

    public void inserir_ocorrencias(String nome, String itens, String justificativa) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "insert into ocorrencias (id, nome, data, liberado, justificativa) values (default, ?, default, ?, ?)";
            pst = conect.prepareStatement(query);

            pst.setString(1, nome);
            pst.setString(2, itens);
            pst.setString(3, justificativa);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }

    }

    public void inserir_item(int id_item, int id_dados) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "insert into habalitado_a_receber (id, id_item, id_dados) values (default, ?, ?)";
            pst = conect.prepareStatement(query);

            pst.setInt(1, id_item);
            pst.setInt(2, id_dados);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }

    }

    //selects all
    public List getAll_IDs_colaborador() {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        List<Integer> lista_IDs = new ArrayList<>();

        try {
            st = conect.createStatement();
            String query = "select id from colaborador";
            rs = st.executeQuery(query);
            while (rs.next()) {
                lista_IDs.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return lista_IDs;
    }
    
    public List getTabela_liberados(){
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        List<tabela_liberados> lista_liberados = new ArrayList<>();
        
        try {
            st = conect.createStatement();
            String query = "select dados.matricula, dados.nome, item_entregue.item_nome from dados inner join habilitado_a_receber on dados.id = habilitado_a_receber.id_dados inner join item_entregue on habilitado_a_receber.id_item = item_entregue.id";
            rs = st.executeQuery(query);
            while (rs.next()) {
                lista_liberados.add(new tabela_liberados(rs.getString(1),rs.getString(2),rs.getString(3)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return lista_liberados;
    }
    
    public List getTabela_Negados(){
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        List<tabela_liberados> lista_Negados = new ArrayList<>();
        
        try {
            st = conect.createStatement();
            String query = "select dados.matricula, dados.nome, item_entregue.item_nome from dados inner join habilitado_a_receber on dados.id != habilitado_a_receber.id_dados inner join item_entregue on habilitado_a_receber.id_item = item_entregue.id order by dados.matricula";
            rs = st.executeQuery(query);
            while (rs.next()) {
                lista_Negados.add(new tabela_liberados(rs.getString(1),rs.getString(2),rs.getString(3)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return lista_Negados;
    }
    
    public List getAll_Entregas(int id_colaborador) {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        List<Integer> lista_IDs = new ArrayList<>();

        try {
            st = conect.createStatement();
            String query = "select id from entrega where id_colaborador = '" + id_colaborador +"'";
            rs = st.executeQuery(query);
            while (rs.next()) {
                lista_IDs.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return lista_IDs;
    }

    public List getAll_IDs_digital() {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        List<Integer> lista_IDs = new ArrayList<>();

        try {
            st = conect.createStatement();
            String query = "select id from digital";
            rs = st.executeQuery(query);
            while (rs.next()) {
                lista_IDs.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return lista_IDs;
    }

    public List getAll_itens() {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        List<String> lista_itens = new ArrayList<>();

        try {
            st = conect.createStatement();
            String query = "select item_nome from item_entregue";
            rs = st.executeQuery(query);
            while (rs.next()) {
                lista_itens.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return lista_itens;
    }

    public List getAll_ocorrencias() {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        List<ocorrencias> lista_correncias = new ArrayList<>();

        try {
            st = conect.createStatement();
            String query = "select * from ocorrencias";
            rs = st.executeQuery(query);
            while (rs.next()) {
                lista_correncias.add(new ocorrencias(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return lista_correncias;
    }
    
    //selects
    public tabela_controle getTabelaDados(int id_colaborador) {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;

        String matricula = "0";
        String nome = "0";

        try {
            st = conect.createStatement();
            String query = "SELECT dados.matricula, dados.nome FROM colaborador INNER JOIN dados ON colaborador.id_dados = dados.id where id_colaborador = '" + id_colaborador + "'";
            rs = st.executeQuery(query);
            if (rs.next()) {
                matricula = rs.getString(1);
                nome = rs.getString(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }

        tabela_controle dados = new tabela_controle(matricula, nome);
        return dados;
    }

    public dados getDados(String matricula) {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;

        int Id = 0;
        String Matricula = "0";
        String Nome = "0";

        try {
            st = conect.createStatement();
            String query = "select * from dados where matricula = " + "'" + matricula + "'";
            rs = st.executeQuery(query);
            if (rs.next()) {
                Id = rs.getInt(1);
                Matricula = rs.getString(2);
                Nome = rs.getString(3);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }

        dados dados = new dados(Id, Matricula, Nome);
        return dados;
    }

    public colaborador getColaborador(int Id_dados) {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;

        int Id = 0;
        int id_dados = 0;
        int Id_digital = 0;

        try {
            st = conect.createStatement();
            String query = "select * from colaborador where id_dados = " + "'" + Id_dados + "'";
            rs = st.executeQuery(query);
            if (rs.next()) {
                Id = rs.getInt(1);
                id_dados = rs.getInt(2);
                Id_digital = rs.getInt(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        colaborador COLABORADOR = new colaborador(Id, id_dados, Id_digital);
        return COLABORADOR;
    }
    
    public colaborador getColaboradorID(int id_digital) {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;

        int Id = 0;
        int id_dados = 0;

        try {
            st = conect.createStatement();
            String query = "select id from colaborador where id_digital = " + "'" + id_digital + "'";
            rs = st.executeQuery(query);
            if (rs.next()) {
                Id = rs.getInt(1);
                id_dados = rs.getInt(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        colaborador COLABORADOR = new colaborador(Id, id_dados, id_digital);
        return COLABORADOR;
    }

    public digital getDigitais(int Id_digital) {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        digital Digitais = null;
        try {
            st = conect.createStatement();
            String query = "select * from digital where id = " + "'" + Id_digital + "'";
            rs = st.executeQuery(query);
            if (rs.next()) {
                Digitais = new digital(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return Digitais;
    }

    public int getDigital(int Id_colaborador) {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        int digital = 0;
        try {
            st = conect.createStatement();
            String query = "select id_digital from colaborador where id = " + "'" + Id_colaborador + "'";
            rs = st.executeQuery(query);
            if (rs.next()) {
                digital = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return digital;
    }

    public List<String> getHablitado(int Id_dados) {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        List<String> lista_habilitados = new ArrayList<>();
        
        try {
            st = conect.createStatement();
            String query = "select * from hablitado_a_receber where id_dados = " + "'" + Id_dados + "'";
            rs = st.executeQuery(query);
            while (rs.next()) {
                lista_habilitados.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return lista_habilitados;
    }

    public int getId_hablitado(int Id_dados, int id_item) {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        try {
            st = conect.createStatement();
            String query = "select id from hablitado_a_receber where id_dados = " + "'" + Id_dados + "' and id_item = '" + id_item + "'" ;
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return id;
    }
    
    public entrega getEntrega(int id) {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        entrega ENTREGA = null;
        try {
            st = conect.createStatement();
            String query = "select * from entrega where id = " + "'" + id + "'";
            rs = st.executeQuery(query);
            if (rs.next()) {
                ENTREGA = new entrega(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return ENTREGA;
    }

    public int getId_item(String item_nome) {
        Connection conect = ConnectionBD.FazerConeccao();
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        try {
            st = conect.createStatement();
            String query = "select id from item_entregue where item_nome = " + "'" + item_nome + "'";
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(conect);
        }
        return id;
    }
    
    //delete
    public void deleteDados(int id_dados) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "delete from dados where id = ?";
            pst = conect.prepareStatement(query);

            pst.setInt(1, id_dados);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }
    }

    public void deleteDigital(int id_digital) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "delete from digital where id = ?";
            pst = conect.prepareStatement(query);

            pst.setInt(1, id_digital);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }
    }

    public void deleteColaborador(int id_colaborador) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "delete from colaborador where id = ?";
            pst = conect.prepareStatement(query);

            pst.setInt(1, id_colaborador);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }
    }

    public void deleteHabilitado(int id_habilitado) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "delete from habilitado_a_receber where id = ?";
            pst = conect.prepareStatement(query);

            pst.setInt(1, id_habilitado);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }
    }

    public void deleteAll_habilitado(int id_dados) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query = "delete from habilitado_a_receber where id_dados = ?";
            pst = conect.prepareStatement(query);

            pst.setInt(1, id_dados);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }
    }

    //upload
    public void updateDigital(int id_digital, String digital, String qualDedo) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;

        try {
            String query = "update digital set " + qualDedo + " = ? where id = ?";
            pst = conect.prepareStatement(query);
            pst.setString(1, digital);
            pst.setInt(2, id_digital);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }
    }

    public void updateDados(int id_dados, String nome) {
        Connection conect = ConnectionBD.FazerConeccao();
        PreparedStatement pst = null;
        try {
            String query2 = "update dados set nome = ? where id = ?";
            pst = conect.prepareStatement(query2);
            pst.setString(1, nome);
            pst.setInt(2, id_dados);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BD.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(pst);
            DbUtils.closeQuietly(conect);
        }
    }

}
