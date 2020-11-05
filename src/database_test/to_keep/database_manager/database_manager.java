/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_test.to_keep.database_manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author USER
 */
public class database_manager {
    
    private String persistence_unit;
    private String username;
    private String password;
    private String IP;
    private String PORT;
    
    public database_manager(  ){
        
    }
    
    public EntityManager getEM(){
        
        String IP_MID = "jdbc:mysql://" + IP +":" + PORT + "/mysql?serverTimezone=UTC";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.url", IP_MID);
        properties.put("javax.persistence.jdbc.user", this.username);
        properties.put("javax.persistence.jdbc.password", this.password);
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(this.persistence_unit , properties);
        return factory.createEntityManager();
        
    }
    
    public String ping_server(){
        EntityManager em = getEM();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.createNativeQuery("SELECT 1");
            em.getTransaction();
            tx.commit();
        }catch( Exception err ){
            em.getTransaction().rollback();
            em.close();
            return String.valueOf(err);
        }
        
        return "";
        
    }
    
    public String update_database ( String query ){
        
        EntityManager em = getEM();
        EntityTransaction tx = em.getTransaction();
        
        try{
            tx.begin();
            em.createNativeQuery(query).executeUpdate();
            em.getTransaction();
            tx.commit();
        }catch(Exception err){
            em.getTransaction().rollback();
            em.close();
            return String.valueOf(err);
        }
        
        return "";
        
    }
    
    public List<Object[]> query_list( String query ){
        
        EntityManager em = getEM();
        EntityTransaction tx = em.getTransaction();
        List<Object[]> output_query = null;
        
        try{
        
            Query q = em.createNativeQuery(query);
            output_query = q.getResultList();

            em.getTransaction().commit();
        
        }catch(Exception err){
            
            em.getTransaction().rollback();
            
        }finally{
            
            em.close();
        
            return output_query;
            
        }
        
    }

    /**
     * @param persistence_unit the persistence_unit to set
     */
    public void setPersistence_unit(String persistence_unit) {
        this.persistence_unit = persistence_unit;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param IP the IP to set
     */
    public void setIP(String IP) {
        this.IP = IP;
    }

    /**
     * @param PORT the PORT to set
     */
    public void setPORT(String PORT) {
        this.PORT = PORT;
    }
    
}
