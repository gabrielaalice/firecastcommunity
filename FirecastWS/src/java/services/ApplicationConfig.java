package services;

import static br.com.zbra.androidlinq.Linq.stream;
import com.google.gson.Gson;
import helper.GenericDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.core.Application;
import model.Occurrence;
import pojo.Cidade;
import pojo.Ocorrencia;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    GenericDAO<Ocorrencia> ocorrenciaDAO = new GenericDAO<>();
    GenericDAO<Cidade> cidadeDAO = new GenericDAO<>();
    
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable periodicTask = new Runnable() {
        public void run() {
            buscarOcorrencias();
        }
    };

    public ApplicationConfig() {
        executor.scheduleAtFixedRate(periodicTask, 0, 5, TimeUnit.MINUTES);
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    } 


    private void buscarOcorrencias() {

        List<Cidade> cidades = cidadeDAO.listarTodos(Cidade.class);
        List<Ocorrencia> ocorrenciasApi = new ArrayList<>();

        for (Cidade cidade : cidades) {

            String urlApi = "http://aplicativosweb.cbm.sc.gov.br/e193comunitario/"
                    + "listar_ocorrencia_aberta_by_servidor_json.php?cidade="
                    + cidade.getNmCidade();

            try {
                BufferedReader br;
                URL url = new URL(urlApi);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                InputStream in = conn.getInputStream();

                int responseCode = conn.getResponseCode();
                //aceita os status de 200 a 299
                if (responseCode / 100 != 2) {
                    System.out.println("RESPONSE CODE:" + responseCode);
                }

                br = new BufferedReader(new InputStreamReader(in));
                try {

                    Gson gson = new Gson();

                    Occurrence[] occurrencesServer = gson.fromJson(br, Occurrence[].class);

                    if (occurrencesServer != null) {

                        List<Integer> idsOcorrenciasApi = stream(ocorrenciasApi).select(x -> x.id).toList();

                        for (Occurrence occurrence : occurrencesServer) {

                            if (!idsOcorrenciasApi.contains(occurrence.id)) {
                                ocorrenciasApi.add(new Ocorrencia(occurrence));
                            }

                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    conn.disconnect();
                    br.close();
                    in.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace(); //fixme
            } catch (IOException e) {
                e.printStackTrace(); //fixme
            }
        }

        List<Integer> idsOcorrenciasApi = stream(ocorrenciasApi).select(x -> x.id).toList();
        List<Ocorrencia> desativadas = stream(ocorrenciaDAO.listarTodos(Ocorrencia.class))
                .where(x -> x.isAtiva()
                && !idsOcorrenciasApi.contains(x.id)).toList();

        ocorrenciasApi.forEach((ocorrencia)
                -> {
            ocorrenciaDAO.salvar(ocorrencia);
        });

        desativadas.stream().map((ocorrencia)
                -> {
            ocorrencia.setAtiva(false);
            return ocorrencia;

        })
                .forEachOrdered((ocorrencia)
                        -> {
                    ocorrenciaDAO.salvar(ocorrencia);
                });

    }

    private static String converterInputStreamToString(InputStream is) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while ((linha = br.readLine()) != null) {
                buffer.append(linha);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(services.FirecastResource.class);
    }

}
