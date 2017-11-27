package services;

import static br.com.zbra.androidlinq.Linq.stream;
import helper.GenericDAO;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import com.google.gson.Gson;
import java.text.Normalizer;
import java.util.List;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import model.Occurrence;
import pojo.Cidade;
import pojo.Ocorrencia;
import pojo.Radio;
import pojo.TpEmergencia;
import pojo.Usuario;

@Path("firecast")
public class FirecastResource {

    private Gson g;

    @Context
    private UriInfo context;

    public FirecastResource() {
        g = new Gson();
    }

    @GET
    @Produces("application/json")
    @Path("/Cidade/list")
    public String listCidades() {
        GenericDAO<Cidade> dao = new GenericDAO<>();
        return g.toJson(dao.listarTodos(Cidade.class));
    }

    @GET
    @Produces("application/json")
    @Path("/Cidade")
    public String getCidade(@QueryParam("nome") String nome) {
        GenericDAO<Cidade> dao = new GenericDAO<>();

        List<Cidade> cidade = stream(dao.listarTodos(Cidade.class))
                .where(x -> removerAcentos(x.getNmCidade())
                        .equalsIgnoreCase(removerAcentos(nome)))
                .toList();

        return g.toJson(cidade);
    }

    @GET
    @Produces("application/json")
    @Path("/TipoOcorrencia/list")
    public String listTipoOcorrencias() {
        GenericDAO<TpEmergencia> dao = new GenericDAO<>();
        return g.toJson(dao.listarTodos(TpEmergencia.class));
    }

    @GET
    @Produces("application/json")
    @Path("/Radio/list")
    public String listRadios() {
        GenericDAO<Radio> dao = new GenericDAO<>();
        return g.toJson(dao.listarTodos(Radio.class));
    }
    
    @GET
    @Produces("application/json")
    @Path("/Radio")
    public String getRadioCidade(@QueryParam("cidade") String nome) {
        GenericDAO<Radio> dao = new GenericDAO<>();
        GenericDAO<Cidade> cidadeDao = new GenericDAO<>();

        List<Cidade> cidade = stream(cidadeDao.listarTodos(Cidade.class))
                .where(x -> removerAcentos(x.getNmCidade())
                        .equalsIgnoreCase(removerAcentos(nome)))
                .toList();

        List<Radio> list = null;

        if (cidade.size() > 0) {
            list = stream(dao.listarTodos(Radio.class))
                    .where(x -> x.getIdCidade() == cidade.get(0).getId())
                    .toList();
        }

        return g.toJson(list);
    }
    
    @GET
    @Produces("application/json")
    @Path("/RadioInterna")
    public String getRadioInternaCidade(@QueryParam("cidade") String nome) {
        GenericDAO<Radio> dao = new GenericDAO<>();
        GenericDAO<Cidade> cidadeDao = new GenericDAO<>();

        List<Cidade> cidade = stream(cidadeDao.listarTodos(Cidade.class))
                .where(x -> removerAcentos(x.getNmCidade())
                        .equalsIgnoreCase(removerAcentos(nome)))
                .toList();

        List<Radio> list = null;

        if (cidade.size() > 0) {
            list = stream(dao.listarTodos(Radio.class))
                    .where(x -> x.getIdCidade() == cidade.get(0).getId() &&
                            x.isTpAmbienteInterno())
                    .toList();
        }

        return g.toJson(list);
    }
    
    @GET
    @Produces("application/json")
    @Path("/RadioExterna")
    public String getRadioExternaCidade(@QueryParam("cidade") String nome) {
        GenericDAO<Radio> dao = new GenericDAO<>();
        GenericDAO<Cidade> cidadeDao = new GenericDAO<>();

        List<Cidade> cidade = stream(cidadeDao.listarTodos(Cidade.class))
                .where(x -> removerAcentos(x.getNmCidade())
                        .equalsIgnoreCase(removerAcentos(nome)))
                .toList();

        List<Radio> list = null;

        if (cidade.size() > 0) {
            list = stream(dao.listarTodos(Radio.class))
                    .where(x -> x.getIdCidade() == cidade.get(0).getId() &&
                            !x.isTpAmbienteInterno())
                    .toList();
        }

        return g.toJson(list);
    }

    @GET
    @Produces("application/json")
    @Path("/Ocorrencia/list")
    public String listOcorrencias() {
        GenericDAO<Ocorrencia> dao = new GenericDAO<>();
        
        List<Occurrence> list = stream(dao.listarTodos(Ocorrencia.class))
                .select(x-> new Occurrence(x)).toList();
        
        return g.toJson(list);
    }

    @GET
    @Produces("application/json")
    @Path("/Ocorrencia")
    public String getOcorrenciaPorCidade(@QueryParam("cidade") String nome) {
        GenericDAO<Ocorrencia> dao = new GenericDAO<>();
        GenericDAO<Cidade> cidadeDao = new GenericDAO<>();

        List<Cidade> cidade = stream(cidadeDao.listarTodos(Cidade.class))
                .where(x -> removerAcentos(x.getNmCidade())
                        .equalsIgnoreCase(removerAcentos(nome)))
                .toList();

        List<Occurrence> list = null;

        if (cidade.size() > 0) {
            list = stream(dao.listarTodos(Ocorrencia.class))
                    .where(x -> x.getIdCidade() == cidade.get(0).getId())
                    .select(x -> new Occurrence(x))
                    .toList();
            
        }

        return g.toJson(list);
    }

    @GET
    @Produces("application/json")
    @Path("/OcorrenciaAtivaCidade")
    public String getOcorrenciaAtivaPorCidade(@QueryParam("cidade") String nome) {
        GenericDAO<Ocorrencia> dao = new GenericDAO<>();
        GenericDAO<Cidade> cidadeDao = new GenericDAO<>();

        List<Cidade> cidade = stream(cidadeDao.listarTodos(Cidade.class))
                .where(x -> removerAcentos(x.getNmCidade())
                        .equalsIgnoreCase(removerAcentos(nome)))
                .toList();

        List<Occurrence> list = null;

        if (cidade.size() > 0) {
            list = stream(dao.listarTodos(Ocorrencia.class))
                    .where(x -> x.getIdCidade() == cidade.get(0).getId()
                    && x.isAtiva())
                    .select(x-> new Occurrence(x))
                    .toList();
        }

        return g.toJson(list);
    }

    @GET
    @Produces("application/json")
    @Path("/OcorrenciaAtiva/list")
    public String listOcorrenciasAtiva() {
        GenericDAO<Ocorrencia> dao = new GenericDAO<>();

        List<Occurrence> list = stream(dao.listarTodos(Ocorrencia.class))
                .where(x -> x.isAtiva())
                .select(x-> new Occurrence(x))
                .toList();

        return g.toJson(list);
    }

    @GET
    @Produces("application/json")
    @Path("/Usuario/list")
    public String listUsuarios() {
        GenericDAO<Usuario> dao = new GenericDAO<>();

        List<Usuario> list = stream(dao.listarTodos(Usuario.class)).toList();

        return g.toJson(list);
    }

    @GET
    @Produces("application/json")
    @Path("/Usuario")
    public String getUsuarioInLogin(
            @QueryParam("email") String email,
            @QueryParam("senha") String senha) {

        GenericDAO<Usuario> dao = new GenericDAO<>();

        List<Usuario> list = stream(dao.listarTodos(Usuario.class))
                .where(x -> x.getEmail().equalsIgnoreCase(email)
                && x.getSenha().equals(senha))
                .toList();

        return g.toJson(list);
    }
    
    @GET
    @Produces("application/json")
    @Path("/UsuarioPorEmail")
    public String getUsuario(@QueryParam("email") String email) {

        GenericDAO<Usuario> dao = new GenericDAO<>();

        List<Usuario> list = stream(dao.listarTodos(Usuario.class))
                .where(x -> x.getEmail().equalsIgnoreCase(email))
                .toList();

        return g.toJson(list);
    }

    @POST
    @Path("/Usuario/adicionar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void adicionarUsuario(Usuario usuario) {
        GenericDAO<Usuario> dao = new GenericDAO<>();
        dao.salvar(usuario);
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

}
