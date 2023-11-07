/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author lucas
 */
public class tabela_liberados {

    private final String matricula;
    private final String nome;
    private final String item;

    public tabela_liberados(String matricula, String nome, String item) {
        this.matricula = matricula;
        this.nome = nome;
        this.item = item;
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    public String getItem() {
        return item;
    }
    
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }
}
