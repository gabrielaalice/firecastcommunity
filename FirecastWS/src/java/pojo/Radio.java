package pojo;

import model.Entidade;

public class Radio extends Entidade {
    
     private String url;
     private boolean tpAmbienteInterno;
     private int idCidade;

    public Radio() {
    }

    public Radio(String url, boolean tpAmbienteInterno, int idCidade) {
       this.url = url;
       this.tpAmbienteInterno = tpAmbienteInterno;
       this.idCidade = idCidade;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    public boolean isTpAmbienteInterno() {
        return this.tpAmbienteInterno;
    }
    
    public void setTpAmbienteInterno(boolean tpAmbienteInterno) {
        this.tpAmbienteInterno = tpAmbienteInterno;
    }
    public int getIdCidade() {
        return this.idCidade;
    }
    
    public void setIdCidade(int idCidade) {
        this.idCidade = idCidade;
    }




}


