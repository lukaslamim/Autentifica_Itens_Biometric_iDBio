/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author lucasantonio-fit
 */
public class entrega {

    private final int id;
    private final int id_colaborador;
    private final String data;
    private final String justificativa;
    private final String item_entregue;
    

    public entrega(int id, int id_colaborador, String data, String justificativa, String item_entregue){
        this.id = id;
        this.id_colaborador = id_colaborador;
        this.data = data;
        this.justificativa = justificativa;
        this.item_entregue = item_entregue;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public int getId_colaborador() {
        return id_colaborador;
    }

    public String getItem_entregue() {
        return item_entregue;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }
}
