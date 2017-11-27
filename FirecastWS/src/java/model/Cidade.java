package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Cidade implements Serializable{
	@Id
	@GeneratedValue
	private Integer id_cidade;
	@Column (length=100)
	private String nm_cidade;
        
        public Cidade(){}

    public Cidade(Integer id_cidade, String nm_cidade) {
        this.id_cidade = id_cidade;
        this.nm_cidade = nm_cidade;
    }
        
        

    /**
     * @return the id_cidade
     */
    public Integer getId_cidade() {
        return id_cidade;
    }

    /**
     * @param id_cidade the id_cidade to set
     */
    public void setId_cidade(Integer id_cidade) {
        this.id_cidade = id_cidade;
    }

    /**
     * @return the nm_cidade
     */
    public String getNm_cidade() {
        return nm_cidade;
    }

    /**
     * @param nm_cidade the nm_cidade to set
     */
    public void setNm_cidade(String nm_cidade) {
        this.nm_cidade = nm_cidade;
    }
}
