package pojo;

import model.Entidade;

public class TpEmergencia extends Entidade {
    
     private String nmTpEmergencia;

    public TpEmergencia() {
    }

    public TpEmergencia(String nmTpEmergencia) {
       this.nmTpEmergencia = nmTpEmergencia;
    }
    
    public String getNmTpEmergencia() {
        return this.nmTpEmergencia;
    }
    
    public void setNmTpEmergencia(String nmTpEmergencia) {
        this.nmTpEmergencia = nmTpEmergencia;
    }




}


