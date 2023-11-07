/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author lucasantonio-fit
 */
public class colaborador {

    private final int id;
    private final int id_dados;
    private final int id_digital;

    public colaborador(int id, int id_dados, int id_digital) {
        this.id = id;
        this.id_dados = id_dados;
        this.id_digital = id_digital;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the id_dados
     */
    public int getId_dados() {
        return id_dados;
    }

    /**
     * @return the id_digital
     */
    public int getId_digital() {
        return id_digital;
    }
}
