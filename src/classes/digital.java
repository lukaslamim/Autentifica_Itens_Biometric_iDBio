/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucasantonio-fit
 */
public class digital {

    private final int id;
    private final List<String> lista_digitais = new ArrayList<>();

    public digital(int id, String digital_1, String digital_2, String digital_3, String digital_4, String digital_5, String digital_6, String digital_7, String digital_8, String digital_9, String digital_10) {
        this.id = id;
        this.lista_digitais.add(digital_1);
        this.lista_digitais.add(digital_2);
        this.lista_digitais.add(digital_3);
        this.lista_digitais.add(digital_4);
        this.lista_digitais.add(digital_5);
        this.lista_digitais.add(digital_6);
        this.lista_digitais.add(digital_7);
        this.lista_digitais.add(digital_8);
        this.lista_digitais.add(digital_9);
        this.lista_digitais.add(digital_10);
    }
    
        /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the lista_digitais
     */
    public List<String> getLista_digitais() {
        return lista_digitais;
    }
}
