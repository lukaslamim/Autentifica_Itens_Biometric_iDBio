/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author lucasantonio-fit
 */
public class habilitado_receber {

    private final int id;
    private final int id_item;
    private final int id_dados;

    public habilitado_receber(int id, int id_item, int id_dados) {
        this.id = id;
        this.id_item = id_item;
        this.id_dados = id_dados;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the id_colaborador
     */
    public int getId_item() {
        return id_item;
    }
    
    public int getId_dados_rh() {
        return id_dados;
    }
}
