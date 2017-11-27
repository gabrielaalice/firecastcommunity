package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Radio implements Serializable {

    @Id
    @GeneratedValue
    private Integer id_radio;
    @Column
    private String url;
    @Column
    private Boolean tp_ambiente_interno;
    @ManyToOne
    private Cidade cidade;
    
    public Radio(){}
    
    public Radio(Integer id_radio,String url,Boolean tp_ambiente_interno,Cidade cidade){
        this.id_radio=id_radio;
        this.url=url;
        this.tp_ambiente_interno=tp_ambiente_interno;
        this.cidade=cidade;
    }

    /**
     * @return the id_radio
     */
    public Integer getId_radio() {
        return id_radio;
    }

    /**
     * @param id_radio the id_radio to set
     */
    public void setId_radio(Integer id_radio) {
        this.id_radio = id_radio;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the tp_ambiente_interno
     */
    public Boolean getTp_ambiente_interno() {
        return tp_ambiente_interno;
    }

    /**
     * @param tp_ambiente_interno the tp_ambiente_interno to set
     */
    public void setTp_ambiente_interno(Boolean tp_ambiente_interno) {
        this.tp_ambiente_interno = tp_ambiente_interno;
    }

    /**
     * @return the cidade
     */
    public Cidade getCidade() {
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
}
