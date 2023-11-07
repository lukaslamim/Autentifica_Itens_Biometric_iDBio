/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author lucasantonio-fit
 */
public class ocorrencias {

    private final int id;
    private final String nome;
    private final String data;
    private final String item_liberado;
    private final String justificativa;
    
    public ocorrencias(int id, String nome, String data, String item_liberado, String justificativa) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.item_liberado = item_liberado;
        this.justificativa = justificativa;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the data
     */
    public String getItem_liberado() {
        return item_liberado;
    }

    /**
     * @return the justificativa
     */
    public String getJustificativa() {
        return justificativa;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }
}
