package model;

import com.google.gson.annotations.SerializedName;
import helper.GenericDAO;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import pojo.Ocorrencia;

public class Occurrence implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    public int id;

    @SerializedName("ts")
    public String date;

    @SerializedName("descricao")
    public String description;

    @SerializedName("logradouro")
    public String adressStreet;

    @SerializedName("numero")
    public Float addressNumber;

    @SerializedName("complemento")
    public String addressComplement;

    @SerializedName("bairro")
    public String addressNeighborhood;

    @SerializedName("referencia")
    public String addressReferencePoint;

    @SerializedName("cidade")
    public City city;

    @SerializedName("tipoEmergencia")
    public OccurrenceType type;

    @SerializedName("listViatura")
    public List<Car> dispatchedCars;

    @SerializedName("latitude")
    public Double latitude;

    @SerializedName("longitude")
    public Double longitude;

    public Occurrence() {
    }

    public Occurrence(Ocorrencia x) {
        this.addressComplement = null;
        this.addressNeighborhood = x.getBairro();
        this.addressNumber = x.getNumero() == null ? null : Float.parseFloat(x.getNumero().toString());
        this.addressReferencePoint = x.getReferencia();
        this.adressStreet = x.getLogradouro();
        this.city = CarregarCidade(x.getIdCidade());
        this.date = ConverterDataEmString(x.getTsOcorrencia());
        this.description = x.getDescricao();
        this.dispatchedCars = null;
        this.id = x.getId();
        this.latitude = x.getLatitude();
        this.longitude = x.getLongitude();
        this.type = CarregarTipoEmergencia(x.getIdTpEmergencia());
    }

    private String ConverterDataEmString(Date tsOcorrencia) {
        if (tsOcorrencia != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

            Date today = Calendar.getInstance().getTime();

            String reportDate = df.format(today);

            return reportDate;
        }
        return null;
    }

    private City CarregarCidade(int idCidade) {
        GenericDAO<Cidade> dao = new GenericDAO<>();
        Cidade c = dao.buscarObjeto(Cidade.class, idCidade);

        if (c != null) {
            return new City(c.getId_cidade(), c.getNm_cidade());
        }

        return null;
    }

    private OccurrenceType CarregarTipoEmergencia(int idTpEmergencia) {
        GenericDAO<TipoEmergencia> dao = new GenericDAO<>();
        TipoEmergencia t = dao.buscarObjeto(TipoEmergencia.class, idTpEmergencia);

        if (t != null) {
            return new OccurrenceType(t.getId_tp_emergencia(), t.getNm_tp_emergencia());
        }

        return null;
    }
}
