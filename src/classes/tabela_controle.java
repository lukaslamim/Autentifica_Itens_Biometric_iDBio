/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author lucas
 */
public class tabela_controle {

    private final String matricula;
    private final String nome;

    public tabela_controle(String matricula, String nome) {
        this.matricula = matricula;
        this.nome = nome;
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }
}
