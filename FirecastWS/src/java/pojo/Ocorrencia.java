package pojo;

import java.util.Date;
import model.Entidade;
import model.Occurrence;

public class Ocorrencia extends Entidade {
    
     private Date tsOcorrencia;
     private String descricao;
     private String logradouro;
     private Long numero;
     private String bairro;
     private String referencia;
     private Double latitude;
     private Double longitude;
     private boolean ativa;
     private int idCidade;
     private int idTpEmergencia;

    public Ocorrencia() {
    }

	
    public Ocorrencia(Date tsOcorrencia, String descricao, String logradouro, String bairro, boolean ativa, int idCidade, int idTpEmergencia) {
        this.tsOcorrencia = tsOcorrencia;
        this.descricao = descricao;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.ativa = ativa;
        this.idCidade = idCidade;
        this.idTpEmergencia = idTpEmergencia;
    }
    
    public Ocorrencia(Date tsOcorrencia, String descricao, String logradouro, Long numero, String bairro, String referencia, Double latitude, Double longitude, boolean ativa, int idCidade, int idTpEmergencia) {
       this.tsOcorrencia = tsOcorrencia;
       this.descricao = descricao;
       this.logradouro = logradouro;
       this.numero = numero;
       this.bairro = bairro;
       this.referencia = referencia;
       this.latitude = latitude;
       this.longitude = longitude;
       this.ativa = ativa;
       this.idCidade = idCidade;
       this.idTpEmergencia = idTpEmergencia;
    }
    
    public Ocorrencia(Occurrence occurrence){
        this.id = occurrence.id;
        this.tsOcorrencia = ConverterStringEmData(occurrence.date);
        this.descricao = occurrence.description;
        this.logradouro = occurrence.adressStreet;
        this.numero = occurrence.addressNumber == null ? null : occurrence.addressNumber.longValue();
        this.bairro = occurrence.addressNeighborhood;
        this.referencia = occurrence.addressReferencePoint;
        this.latitude = occurrence.latitude;
        this.longitude = occurrence.longitude;
        this.ativa = true;
        this.idCidade = occurrence.city == null ? null : occurrence.city.id;
        this.idTpEmergencia = occurrence.type == null ? null : occurrence.type.id;
    }
    
    public Date getTsOcorrencia() {
        return this.tsOcorrencia;
    }
    
    public void setTsOcorrencia(Date tsOcorrencia) {
        this.tsOcorrencia = tsOcorrencia;
    }
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getLogradouro() {
        return this.logradouro;
    }
    
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }
    public Long getNumero() {
        return this.numero;
    }
    
    public void setNumero(Long numero) {
        this.numero = numero;
    }
    public String getBairro() {
        return this.bairro;
    }
    
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public String getReferencia() {
        return this.referencia;
    }
    
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    public Double getLatitude() {
        return this.latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return this.longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public boolean isAtiva() {
        return this.ativa;
    }
    
    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
    public int getIdCidade() {
        return this.idCidade;
    }
    
    public void setIdCidade(int idCidade) {
        this.idCidade = idCidade;
    }
    public int getIdTpEmergencia() {
        return this.idTpEmergencia;
    }
    
    public void setIdTpEmergencia(int idTpEmergencia) {
        this.idTpEmergencia = idTpEmergencia;
    }
    
    public Date ConverterStringEmData(String dataOcorrencia){
        String[] dataArray = dataOcorrencia.split("-");
        int anoJava = Integer.parseInt(dataArray[0]) - 1900;
        int mes = Integer.parseInt(dataArray[1]);
        int dia = Integer.parseInt(dataArray[dataArray.length-1].substring(0, 2));
        
        String[] horarioArray = dataArray[dataArray.length-1].split(":");
        int hora = Integer.parseInt(horarioArray[0].substring(horarioArray[0].length() - 2));
        
        int minutos = Integer.parseInt(horarioArray[1]);
        int segundos = Integer.parseInt(horarioArray[horarioArray.length-1].substring(0, 2));
        
        Date date = new Date(anoJava, mes, dia, hora, minutos, segundos);
        
        return date;
    }

}


