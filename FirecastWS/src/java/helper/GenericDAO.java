package helper;

import static br.com.zbra.androidlinq.Linq.stream;
import java.util.List;
import model.Entidade;
import org.hibernate.Session;
import util.HibernateUtil;

public class GenericDAO<T> {

    public static final int SALVAR = 0;
    public static final int EXCLUIR = 1;
    private Session manager;
    
    public GenericDAO() {
        manager = HibernateUtil.getSessionFactory().openSession();
    }

    public void abrirConexao() {
        if (!manager.getTransaction().isActive()) {
            manager.getTransaction().begin();
        }
    }

    private void fecharConexao() {
        if (manager.getTransaction().isActive()) {
            manager.close();
        }
    }

    public List<T> listarTodos(Class clazz) {
        abrirConexao();
        List<T> list = manager.createCriteria(clazz).list();        
        executarCommit();
        return list;
    }

    public boolean salvar(Entidade objeto) {
        return tentarEfetuarTransacao(objeto, SALVAR);
    }

    public boolean excluir(Entidade objeto) {
        return tentarEfetuarTransacao(objeto, EXCLUIR);
    }

    public T buscarObjeto(Class clazz, int id) {
        List<T> list = stream(listarTodos(clazz))
                .where(x-> ((Entidade) x).id==id).toList();
        
        if(list.size()>0){
            return list.get(0);
        }
        
        return null;
    }

    private boolean tentarEfetuarTransacao(Entidade objeto, int acao) {
        abrirConexao();
        try {
            return efetuadaComSucesso(objeto, acao);
        } catch (Exception e) {
            return executarRoolBack(e);
        }
    }

    private boolean efetuadaComSucesso(Entidade objeto, int acao) {
        executarAcao(objeto, acao);
        return executarCommit();
    }

    private boolean executarCommit() {
        manager.getTransaction().commit();
        fecharConexao();
        return true;
    }

    private boolean executarRoolBack(Exception e) {
        manager.getTransaction().rollback();
        e.printStackTrace();
        fecharConexao();
        return false;
    }

    private void executarAcao(Entidade objeto, int condicao) {
        switch (condicao) {
            case SALVAR:
                
                if(objeto.getId()>0){
                    manager.merge(objeto);
                }else{
                    manager.persist(objeto);
                }
                
                break;
                
            case EXCLUIR:
                manager.delete(objeto);
                break;
                
            default:
                break;
        }
    }
}
