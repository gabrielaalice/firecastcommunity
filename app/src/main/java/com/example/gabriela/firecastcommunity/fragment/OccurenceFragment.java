package com.example.gabriela.firecastcommunity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gabriela.firecastcommunity.OccurenceAdapter;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.data.FirecastApi;
import com.example.gabriela.firecastcommunity.data.FirecastClient;
import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.domain.OccurrenceType;
import com.example.gabriela.firecastcommunity.helper.DistanceCalculator;
import com.example.gabriela.firecastcommunity.helper.MetodsHelpers;
import com.google.android.gms.maps.model.LatLng;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.zbra.androidlinq.Linq.stream;


public class OccurenceFragment extends Fragment {

    LatLng actualPosition;
    final List<Occurrence> result = new ArrayList<>();
    final List<String> cityNames = new ArrayList<>();
    TextView info_city;
    private RecyclerView recycler;
    private OccurenceAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    public OccurenceFragment(){
    }

    public static OccurenceFragment newInstance(String param1, String param2) {
        OccurenceFragment fragment = new OccurenceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            LoadingOccurrence();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_occurence, container, false);

        info_city = (TextView) view.findViewById(R.id.info_city);
        String teste = info_city.getText().toString();
        info_city.setText("Teste de alteração do texto de cidade");
        String a = info_city.getText().toString();

        this.recycler = (RecyclerView) view.findViewById(R.id.recyclerViewOccurrence);
        this.recycler.setLayoutManager(new LinearLayoutManager(recycler.getContext()));

        try {
            //LoadingOccurrence();
            Occurrence o = new Occurrence();
            o.addressComplement="Complemento";
            o.addressNeighborhood="Bairro";
            o.addressNumber=(float)222;
            o.addressReferencePoint="Referência";
            o.adressStreet="Rua";
            o.city=new City(8105, "FLORIANOPOLIS");
            o.date=new Date().toLocaleString();
            o.description="descrição";
            o.distance=(double)10;
            o.id=1;
            o.latitude=(double)10;
            o.longitude=(double)10;
            o.type = new OccurrenceType(1,"Teste");
            result.add(o);
        } finally {
            List<Occurrence> listFilter = ExecuteFilter(result);
            UpdateRecicleViewList(listFilter);
        }

        return inflater.inflate(R.layout.fragment_occurence, container, false);

    }
    private Occurrence bindLatitudeLongitudeStreet(Occurrence occurrence) {
        return occurrence;
    }

    private Occurrence getLocationFromCity(Occurrence occurrence) {
        if (occurrence.city == null) {
            return occurrence;
        }
        return bindLatitudeLongitudeCity(occurrence);
    }

    private Occurrence bindLatitudeLongitudeCity(Occurrence occurrence) {
        return occurrence;
    }

    private Occurrence getLocationFromAdress(Occurrence occurrence) {
        if (occurrence.adressStreet == null) {
            return getLocationFromCity(occurrence);
        }
        return bindLatitudeLongitudeStreet(occurrence);
    }

    private Occurrence getLocation(Occurrence occurrence) {
        if (occurrence.latitude != null || occurrence.longitude != null) {
            return occurrence;
        }
        return getLocationFromAdress(occurrence);
    }

    private String getCityLocation() {
        return "Florianópolis";
    }

    private void LoadingOccurrence() {

        //swipeRefreshLayout.setRefreshing(true);
        result.removeAll(result);
        cityNames.removeAll(cityNames);
        String city = MetodsHelpers.normalizeString(getCityLocation());
        FirecastClient fire = new FirecastClient();
        FirecastApi api = fire.retrofit.create(FirecastApi.class);
        List<City> listCities = cities();

        for (City cidade : listCities) {
            api.getOccurrences(cidade.name)
                    .enqueue(new Callback<List<Occurrence>>() {

                        public void onResponse(Call<List<Occurrence>> call, Response<List<Occurrence>> response) {

                            if (!cityNames.contains(cidade.name)) {
                                cityNames.add(cidade.name);

                                info_city.setText("Aguarde: " + cidade.name +
                                        " (" + cityNames.size() + " de " + listCities.size() + ")");

                                List<Occurrence> list = response.body();
                                if (list != null) {
                                    List<Integer> listIds = stream(result).select(c -> c.id).toList();
                                    List<Occurrence> listList = stream(list).where(c -> !listIds.contains(c.id)).toList();

                                    for (Occurrence occ : listList) {
                                        if(actualPosition!=null) {
                                            Double distance = new DistanceCalculator()
                                                    .distancia(actualPosition, getLocation(occ));
                                            if (distance == 0 || distance < 0) {
                                                occ.distance = null;
                                            } else {
                                                occ.distance = distance / 1000;
                                            }
                                        }

                                    }

//                                    listList.forEach(occ->
//                                            occ.distance = new DistanceCalculator()
//                                                    .distance(actualPosition, getLocation(occ)));

                                    result.addAll(listList);
                                }
                            }

                            if (cityNames.size() == listCities.size()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Occurrence>> call, Throwable t) {
                            if (!cityNames.contains(cidade.name)) {
                                cityNames.add(cidade.name);
                                info_city.setText("Aguarde: " + cidade.name);
                            }

                            if (cityNames.size() == listCities.size()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
        }
    }

    private List<Occurrence> ExecuteFilter(List<Occurrence> list) {
        list = stream(list)
                .distinct()
                .orderBy(c -> c.distance == null ? 1000000 : c.distance)
                .thenBy(x -> x.city.name)
                .thenBy(x -> x.date)
                .toList();

        return stream(list)
                .distinct()
                //.where(x -> filterOccurrences.contains(x.type.id))

//                .where(x-> checkBoxRadiusDistance.isChecked()?
//                        (x.distance !=null ?
//                                x.distance <= sb.getProgress()
//                                : true)
//                        : true)
//
//                .where(x-> checkBoxCidade.isChecked()?
//                        (x.city !=null ?
//                                x.city.id == ((City)spCities.getSelectedItem()).id
//                                : true)
//                        : true)

                .orderBy(c -> c.distance == null ? 1000000 : c.distance)
                .thenBy(x -> x.city.name)
                .thenBy(x -> x.date)
                .toList();
    }

    private void UpdateRecicleViewList(List<Occurrence> list){
        adapter = new OccurenceAdapter(getContext(), list);
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
    }


    private List<City> cities() {
        List<City> cities = new ArrayList<>();
        cities.add(new City(9939, "ABDON BATISTA"));
        cities.add(new City(8001, "ABELARDO LUZ"));
        cities.add(new City(8003, "AGROLANDIA"));
        cities.add(new City(8005, "AGRONOMICA"));
        cities.add(new City(8807, "AGUA DOCE"));
        cities.add(new City(8009, "AGUAS DE CHAPECO"));
        cities.add(new City(5577, "AGUAS FRIAS"));
        cities.add(new City(8011, "AGUAS MORNAS"));
        cities.add(new City(8013, "ALFREDO WAGNER"));
        cities.add(new City(8869, "ALTO BELA VISTA"));
        cities.add(new City(8015, "ANCHIETA"));
        cities.add(new City(8017, "ANGELINA"));
        cities.add(new City(8019, "ANITA GARIBALDI"));
        cities.add(new City(8021, "ANITAPOLIS"));
        cities.add(new City(8023, "ANTONIO CARLOS"));
        cities.add(new City(9941, "APIUNA"));
        cities.add(new City(5597, "ARABUTA"));
        cities.add(new City(8025, "ARAQUARI"));
        cities.add(new City(8027, "ARARANGUA"));
        cities.add(new City(8029, "ARMAZEM"));
        cities.add(new City(8031, "ARROIO TRINTA"));
        cities.add(new City(5599, "ARVOREDO"));
        cities.add(new City(8033, "ASCURRA"));
        cities.add(new City(8035, "ATALANTA"));
        cities.add(new City(8037, "AURORA"));
        cities.add(new City(8885, "BALNEARIO ARROIO DO SILVA"));
        cities.add(new City(8039, "BALNEARIO CAMBORIU"));
        cities.add(new City(8907, "BALNEARIO GAIVOTA"));
        cities.add(new City(1192, "BALNEARIO RINCAO"));
        cities.add(new City(8923, "BANDEIRANTE"));
        cities.add(new City(8940, "BARRA BONITA"));
        cities.add(new City(5549, "BARRA DO SUL"));
        cities.add(new City(8041, "BARRA VELHA"));
        cities.add(new City(8966, "BELA VISTA DO TOLDO"));
        cities.add(new City(5745, "BELMONTE"));
        cities.add(new City(8043, "BENEDITO NOVO"));
        cities.add(new City(8045, "BIGUACU"));
        cities.add(new City(8047, "BLUMENAU"));
        cities.add(new City(8982, "BOCAINA DO SUL"));
        cities.add(new City(8389, "BOM JARDIM DA SERRA"));
        cities.add(new City(9008, "BOM JESUS"));
        cities.add(new City(9024, "BOM JESUS DO OESTE"));
        cities.add(new City(8049, "BOM RETIRO"));
        cities.add(new City(5537, "BOMBINHAS"));
        cities.add(new City(8051, "BOTUVERA"));
        cities.add(new City(8053, "BRACO DO NORTE"));
        cities.add(new City(5557, "BRACO DO TROMBUDO"));
        cities.add(new City(9040, "BRUNOPOLIS"));
        cities.add(new City(8055, "BRUSQUE"));
        cities.add(new City(8057, "CACADOR"));
        cities.add(new City(8059, "CAIBI"));
        cities.add(new City(5553, "CALMON"));
        cities.add(new City(8061, "CAMBORIU"));
        cities.add(new City(8063, "CAMPO ALEGRE"));
        cities.add(new City(8065, "CAMPO BELO DO SUL"));
        cities.add(new City(8067, "CAMPO ERE"));
        cities.add(new City(8069, "CAMPOS NOVOS"));
        cities.add(new City(8071, "CANELINHA"));
        cities.add(new City(8073, "CANOINHAS"));
        cities.add(new City(9067, "CAPAO ALTO"));
        cities.add(new City(8075, "CAPINZAL"));
        cities.add(new City(5545, "CAPIVARI DE BAIXO"));
        cities.add(new City(8077, "CATANDUVAS"));
        cities.add(new City(8079, "CAXAMBU DO SUL"));
        cities.add(new City(9943, "CELSO RAMOS"));
        cities.add(new City(5567, "CERRO NEGRO"));
        cities.add(new City(9083, "CHAPADAO DO LAGEADO"));
        cities.add(new City(8081, "CHAPECO"));
        cities.add(new City(5543, "COCAL DO SUL"));
        cities.add(new City(8083, "CONCORDIA"));
        cities.add(new City(5579, "CORDILHEIRA ALTA"));
        cities.add(new City(8085, "CORONEL FREITAS"));
        cities.add(new City(5735, "CORONEL MARTINS"));
        cities.add(new City(8395, "CORREIA PINTO"));
        cities.add(new City(8087, "CORUPA"));
        cities.add(new City(8089, "CRICIUMA"));
        cities.add(new City(8091, "CUNHA PORA"));
        cities.add(new City(9105, "CUNHATAI"));
        cities.add(new City(8093, "CURITIBANOS"));
        cities.add(new City(8095, "DESCANSO"));
        cities.add(new City(8097, "DIONISIO CERQUEIRA"));
        cities.add(new City(8099, "DONA EMMA"));
        cities.add(new City(9945, "DOUTOR PEDRINHO"));
        cities.add(new City(9121, "ENTRE RIOS"));
        cities.add(new City(9148, "ERMO"));
        cities.add(new City(8101, "ERVAL VELHO"));
        cities.add(new City(8103, "FAXINAL DOS GUEDES"));
        cities.add(new City(9164, "FLOR DO SERTAO"));
        cities.add(new City(8105, "FLORIANOPOLIS"));
        cities.add(new City(5581, "FORMOSA DO SUL"));
        cities.add(new City(9733, "FORQUILHINHA"));
        cities.add(new City(8107, "FRAIBURGO"));
        cities.add(new City(9180, "FREI ROGERIO"));
        cities.add(new City(8109, "GALVAO"));
        cities.add(new City(8113, "GAROPABA"));
        cities.add(new City(8115, "GARUVA"));
        cities.add(new City(8117, "GASPAR"));
        cities.add(new City(8111, "GOVERNADOR CELSO RAMOS"));
        cities.add(new City(8119, "GRAO PARA"));
        cities.add(new City(8121, "GRAVATAL"));
        cities.add(new City(8123, "GUABIRUBA"));
        cities.add(new City(8125, "GUARACIABA"));
        cities.add(new City(8127, "GUARAMIRIM"));
        cities.add(new City(8129, "GUARUJA DO SUL"));
        cities.add(new City(5583, "GUATAMBU"));
        cities.add(new City(8131, "HERVAL DO OESTE"));
        cities.add(new City(9202, "IBIAM"));
        cities.add(new City(8133, "IBICARE"));
        cities.add(new City(8135, "IBIRAMA"));
        cities.add(new City(8137, "ICARA"));
        cities.add(new City(8139, "ILHOTA"));
        cities.add(new City(8141, "IMARUI"));
        cities.add(new City(8143, "IMBITUBA"));
        cities.add(new City(8145, "IMBUIA"));
        cities.add(new City(8147, "INDAIAL"));
        cities.add(new City(9229, "IOMERE"));
        cities.add(new City(8149, "IPIRA"));
        cities.add(new City(9951, "IPORA DO OESTE"));
        cities.add(new City(5737, "IPUACU"));
        cities.add(new City(8151, "IPUMIRIM"));
        cities.add(new City(9953, "IRACEMINHA"));
        cities.add(new City(8153, "IRANI"));
        cities.add(new City(5585, "IRATI"));
        cities.add(new City(8155, "IRINEOPOLIS"));
        cities.add(new City(8157, "ITA"));
        cities.add(new City(8159, "ITAIOPOLIS"));
        cities.add(new City(8161, "ITAJAI"));
        cities.add(new City(8163, "ITAPEMA"));
        cities.add(new City(8165, "ITAPIRANGA"));
        cities.add(new City(9985, "ITAPOA"));
        cities.add(new City(8167, "ITUPORANGA"));
        cities.add(new City(8169, "JABORA"));
        cities.add(new City(8171, "JACINTO MACHADO"));
        cities.add(new City(8173, "JAGUARUNA"));
        cities.add(new City(8175, "JARAGUA DO SUL"));
        cities.add(new City(5587, "JARDINOPOLIS"));
        cities.add(new City(8177, "JOACABA"));
        cities.add(new City(8179, "JOINVILLE"));
        cities.add(new City(9957, "JOSE BOITEUX"));
        cities.add(new City(9245, "JUPIA"));
        cities.add(new City(8181, "LACERDOPOLIS"));
        cities.add(new City(5739, "LAGEADO GRANDE"));
        cities.add(new City(8183, "LAGES"));
        cities.add(new City(8185, "LAGUNA"));
        cities.add(new City(8187, "LAURENTINO"));
        cities.add(new City(8189, "LAURO MULLER"));
        cities.add(new City(8191, "LEBON REGIS"));
        cities.add(new City(8193, "LEOBERTO LEAL"));
        cities.add(new City(9961, "LINDOIA DO SUL"));
        cities.add(new City(8195, "LONTRAS"));
        cities.add(new City(8197, "LUIZ ALVES"));
        cities.add(new City(9261, "LUZERNA"));
        cities.add(new City(5575, "MACIEIRA"));
        cities.add(new City(8199, "MAFRA"));
        cities.add(new City(8201, "MAJOR GERCINO"));
        cities.add(new City(8203, "MAJOR VIEIRA"));
        cities.add(new City(8391, "MARACAJA"));
        cities.add(new City(8205, "MARAVILHA"));
        cities.add(new City(9963, "MAREMA"));
        cities.add(new City(8207, "MASSARANDUBA"));
        cities.add(new City(8209, "MATOS COSTA"));
        cities.add(new City(8211, "MELEIRO"));
        cities.add(new City(5559, "MIRIM DOCE"));
        cities.add(new City(8213, "MODELO"));
        cities.add(new City(8215, "MONDAI"));
        cities.add(new City(8217, "MONTE CASTELO"));
        cities.add(new City(5561, "MONTECARLO"));
        cities.add(new City(8219, "MORRO DA FUMACA"));
        cities.add(new City(5539, "MORRO GRANDE"));
        cities.add(new City(8221, "NAVEGANTES"));
        cities.add(new City(8223, "NOVA ERECHIM"));
        cities.add(new City(5589, "NOVA ITABERABA"));
        cities.add(new City(8225, "NOVA TRENTO"));
        cities.add(new City(8227, "NOVA VENEZA"));
        cities.add(new City(5591, "NOVO HORIZONTE"));
        cities.add(new City(8229, "ORLEANS"));
        cities.add(new City(8397, "OTACILIO COSTA"));
        cities.add(new City(8231, "OURO"));
        cities.add(new City(5741, "OURO VERDE"));
        cities.add(new City(9288, "PAIAL"));
        cities.add(new City(9300, "PAINEL"));
        cities.add(new City(8233, "PALHOCA"));
        cities.add(new City(8235, "PALMA SOLA"));
        cities.add(new City(9326, "PALMEIRA"));
        cities.add(new City(8237, "PALMITOS"));
        cities.add(new City(8239, "PAPANDUVA"));
        cities.add(new City(5747, "PARAISO"));
        cities.add(new City(5541, "PASSO DE TORRES"));
        cities.add(new City(5743, "PASSOS MAIA"));
        cities.add(new City(8241, "PAULO LOPES"));
        cities.add(new City(8243, "PEDRAS GRANDES"));
        cities.add(new City(8245, "PENHA"));
        cities.add(new City(8247, "PERITIBA"));
        cities.add(new City(1194, "PESCARIA BRAVA"));
        cities.add(new City(8249, "PETROLANDIA"));
        cities.add(new City(8251, "PICARRAS"));
        cities.add(new City(8253, "PINHALZINHO"));
        cities.add(new City(8255, "PINHEIRO PRETO"));
        cities.add(new City(8257, "PIRATUBA"));
        cities.add(new City(5593, "PLANALTO ALEGRE"));
        cities.add(new City(8259, "POMERODE"));
        cities.add(new City(8261, "PONTE ALTA"));
        cities.add(new City(5569, "PONTE ALTA DO NORTE"));
        cities.add(new City(8263, "PONTE SERRADA"));
        cities.add(new City(8265, "PORTO BELO"));
        cities.add(new City(8267, "PORTO UNIAO"));
        cities.add(new City(8269, "POUSO REDONDO"));
        cities.add(new City(8271, "PRAIA GRANDE"));
        cities.add(new City(8273, "PRESIDENTE CASTELO BRANCO"));
        cities.add(new City(8275, "PRESIDENTE GETULIO"));
        cities.add(new City(8277, "PRESIDENTE NEREU"));
        cities.add(new City(9342, "PRINCESA"));
        cities.add(new City(8279, "QUILOMBO"));
        cities.add(new City(8281, "RANCHO QUEIMADO"));
        cities.add(new City(8283, "RIO DAS ANTAS"));
        cities.add(new City(8285, "RIO DO CAMPO"));
        cities.add(new City(8287, "RIO DO OESTE"));
        cities.add(new City(8291, "RIO DO SUL"));
        cities.add(new City(8289, "RIO DOS CEDROS"));
        cities.add(new City(8293, "RIO FORTUNA"));
        cities.add(new City(8295, "RIO NEGRINHO"));
        cities.add(new City(5571, "RIO RUFINO"));
        cities.add(new City(5749, "RIQUEZA"));
        cities.add(new City(8297, "RODEIO"));
        cities.add(new City(8299, "ROMELANDIA"));
        cities.add(new City(8301, "SALETE"));
        cities.add(new City(9369, "SALTINHO"));
        cities.add(new City(8303, "SALTO VELOSO"));
        cities.add(new City(5547, "SANGAO"));
        cities.add(new City(8305, "SANTA CECILIA"));
        cities.add(new City(5751, "SANTA HELENA"));
        cities.add(new City(8307, "SANTA ROSA DE LIMA"));
        cities.add(new City(9967, "SANTA ROSA DO SUL"));
        cities.add(new City(5555, "SANTA TEREZINHA"));
        cities.add(new City(9385, "SANTA TEREZINHA DO PROGRESSO"));
        cities.add(new City(9407, "SANTIAGO DO SUL"));
        cities.add(new City(8309, "SANTO AMARO DA IMPERATRIZ"));
        cities.add(new City(8311, "SAO BENTO DO SUL"));
        cities.add(new City(9423, "SAO BERNARDINO"));
        cities.add(new City(8313, "SAO BONIFACIO"));
        cities.add(new City(8315, "SAO CARLOS"));
        cities.add(new City(5573, "SAO CRISTOVAO DO SUL"));
        cities.add(new City(8317, "SAO DOMINGOS"));
        cities.add(new City(8319, "SAO FRANCISCO DO SUL"));
        cities.add(new City(8321, "SAO JOAO BATISTA"));
        cities.add(new City(5551, "SAO JOAO DO ITAPERIU"));
        cities.add(new City(5753, "SAO JOAO DO OESTE"));
        cities.add(new City(8323, "SAO JOAO DO SUL"));
        cities.add(new City(8325, "SAO JOAQUIM"));
        cities.add(new City(8327, "SAO JOSE"));
        cities.add(new City(8329, "SAO JOSE DO CEDRO"));
        cities.add(new City(8331, "SAO JOSE DO CERRITO"));
        cities.add(new City(8333, "SAO LOURENCO DO OESTE"));
        cities.add(new City(8335, "SAO LUDGERO"));
        cities.add(new City(8337, "SAO MARTINHO"));
        cities.add(new City(5755, "SAO MIGUEL DA BOA VISTA"));
        cities.add(new City(8339, "SAO MIGUEL DO OESTE"));
        cities.add(new City(9440, "SAO PEDRO DE ALCANTARA"));
        cities.add(new City(8341, "SAUDADES"));
        cities.add(new City(8343, "SCHROEDER"));
        cities.add(new City(8345, "SEARA"));
        cities.add(new City(9989, "SERRA ALTA"));
        cities.add(new City(8347, "SIDEROPOLIS"));
        cities.add(new City(8349, "SOMBRIO"));
        cities.add(new City(5595, "SUL BRASIL"));
        cities.add(new City(8351, "TAIO"));
        cities.add(new City(8353, "TANGARA"));
        cities.add(new City(9466, "TIGRINHOS"));
        cities.add(new City(8355, "TIJUCAS"));
        cities.add(new City(8393, "TIMBE DO SUL"));
        cities.add(new City(8357, "TIMBO"));
        cities.add(new City(9971, "TIMBO GRANDE"));
        cities.add(new City(8359, "TRES BARRAS"));
        cities.add(new City(9482, "TREVISO"));
        cities.add(new City(8361, "TREZE DE MAIO"));
        cities.add(new City(8363, "TREZE TILIAS"));
        cities.add(new City(8365, "TROMBUDO CENTRAL"));
        cities.add(new City(8367, "TUBARAO"));
        cities.add(new City(9991, "TUNAPOLIS"));
        cities.add(new City(8369, "TURVO"));
        cities.add(new City(9973, "UNIAO DO OESTE"));
        cities.add(new City(8371, "URUBICI"));
        cities.add(new City(9975, "URUPEMA"));
        cities.add(new City(8373, "URUSSANGA"));
        cities.add(new City(8375, "VARGEAO"));
        cities.add(new City(5563, "VARGEM"));
        cities.add(new City(5565, "VARGEM BONITA"));
        cities.add(new City(8377, "VIDAL RAMOS"));
        cities.add(new City(8379, "VIDEIRA"));
        cities.add(new City(9977, "VITOR MEIRELES"));
        cities.add(new City(8381, "WITMARSUM"));
        cities.add(new City(8383, "XANXERE"));
        cities.add(new City(8385, "XAVANTINA"));
        cities.add(new City(8387, "XAXIM"));
        cities.add(new City(9504, "ZORTEA"));
        return cities;
    }


}
