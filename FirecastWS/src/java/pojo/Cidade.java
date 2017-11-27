package pojo;

import model.Entidade;

public class Cidade extends Entidade {
    
     private String nmCidade;

    public Cidade() {
    }

    public Cidade(String nmCidade) {
       this.nmCidade = nmCidade;
    }
    
    public String getNmCidade() {
        return this.nmCidade;
    }
    
    public void setNmCidade(String nmCidade) {
        this.nmCidade = nmCidade;
    }




}


