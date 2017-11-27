package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity(name = "tp_emergencia")
public class TipoEmergencia implements Serializable{
	@Id
	@GeneratedValue
	private Integer id_tp_emergencia;
	@Column (length=100)
	private String nm_tp_emergencia;	

    /**
     * @return the id_tp_emergencia
     */
    public Integer getId_tp_emergencia() {
        return id_tp_emergencia;
    }

    /**
     * @param id_tp_emergencia the id_tp_emergencia to set
     */
    public void setId_tp_emergencia(Integer id_tp_emergencia) {
        this.id_tp_emergencia = id_tp_emergencia;
    }

    /**
     * @return the nm_tp_emergencia
     */
    public String getNm_tp_emergencia() {
        return nm_tp_emergencia;
    }

    /**
     * @param nm_tp_emergencia the nm_tp_emergencia to set
     */
    public void setNm_tp_emergencia(String nm_tp_emergencia) {
        this.nm_tp_emergencia = nm_tp_emergencia;
    }
}
