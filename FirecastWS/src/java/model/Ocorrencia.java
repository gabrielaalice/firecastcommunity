package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.ManyToOne;

@Entity
public class Ocorrencia implements Serializable {

    @Id
    @GeneratedValue
    private Integer id_ocorrencia;
    @Column
    private Date ts_ocorrencia;
    @Column
    private String descricao;
    @Column(length = 100)
    private String logradouro;
    @Column
    private Integer numero;
    @Column(length = 100)
    private String bairro;
    @Column(length = 100)
    private String referencia;
    @Column
    private Double latitude;
    @Column
    private Double longitude;
    @Column
    private Boolean ativa;
    @ManyToOne
    private Cidade cidade;
    @ManyToOne
    private TipoEmergencia tipoEmergencia;

    /**
     * @return the id_ocorrencia
     */
    public Integer getId_ocorrencia() {
        return id_ocorrencia;
    }

    /**
     * @param id_ocorrencia the id_ocorrencia to set
     */
    public void setId_ocorrencia(Integer id_ocorrencia) {
        this.id_ocorrencia = id_ocorrencia;
    }

    /**
     * @return the ts_ocorrencia
     */
    public Date getTs_ocorrencia() {
        return ts_ocorrencia;
    }

    /**
     * @param ts_ocorrencia the ts_ocorrencia to set
     */
    public void setTs_ocorrencia(Date ts_ocorrencia) {
        this.ts_ocorrencia = ts_ocorrencia;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the logradouro
     */
    public String getLogradouro() {
        return logradouro;
    }

    /**
     * @param logradouro the logradouro to set
     */
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    /**
     * @return the numero
     */
    public Integer getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    /**
     * @return the bairro
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * @param bairro the bairro to set
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    /**
     * @return the referencia
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * @param referencia the referencia to set
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the ativa
     */
    public Boolean getAtiva() {
        return ativa;
    }

    /**
     * @param ativa the ativa to set
     */
    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
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

    /**
     * @return the tipoEmergencia
     */
    public TipoEmergencia getTipoEmergencia() {
        return tipoEmergencia;
    }

    /**
     * @param tipoEmergencia the tipoEmergencia to set
     */
    public void setTipoEmergencia(TipoEmergencia tipoEmergencia) {
        this.tipoEmergencia = tipoEmergencia;
    }
}
