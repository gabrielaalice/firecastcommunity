package pojo;

import java.math.BigDecimal;
import model.Entidade;

public class Usuario extends Entidade {
    
     private String nome;
     private String login;
     private String senha;
     private String email;
     private String telefone;
     private BigDecimal radioDistancia;
     private int idCidade;

    public Usuario() {
    }
    
    public Usuario(String nome, String login, String senha, int idCidade) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.idCidade = idCidade;
    }
    public Usuario(String nome, String login, String senha, String email, String telefone, BigDecimal radioDistancia, int idCidade) {
       this.nome = nome;
       this.login = login;
       this.senha = senha;
       this.email = email;
       this.telefone = telefone;
       this.radioDistancia = radioDistancia;
       this.idCidade = idCidade;
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getLogin() {
        return this.login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenha() {
        return this.senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefone() {
        return this.telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public BigDecimal getRadioDistancia() {
        return this.radioDistancia;
    }
    
    public void setRadioDistancia(BigDecimal radioDistancia) {
        this.radioDistancia = radioDistancia;
    }
    public int getIdCidade() {
        return this.idCidade;
    }
    
    public void setIdCidade(int idCidade) {
        this.idCidade = idCidade;
    }




}


