/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_test.to_keep;

import database_test.to_keep.database_manager.database_manager;
import java.lang.reflect.Array;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author USER
 */
public class UI_Handler {
    
    public void tree_update( JTree database_tree , database_manager dao ){
        //DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) database_tree.getSelectionPath().getLastPathComponent();

        database_tree.setModel( new DefaultTreeModel( new DefaultMutableTreeNode("Databases") ) );
        TreeSelectionModel smd = database_tree.getSelectionModel();
        
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) database_tree.getModel().getRoot();

        List<Object[]> return_bases = dao.query_list("SHOW DATABASES");
        
        for( int I = 0 ; I < return_bases.size() ; I++ ){
            
            //System.out.println( String.valueOf(return_bases.get(I)) + " : "  );
            DefaultMutableTreeNode top=new DefaultMutableTreeNode( String.valueOf(return_bases.get(I)) );
            
            List<Object[]> return_tables = dao.query_list("SHOW TABLES FROM " + String.valueOf(return_bases.get(I)));
            
            for( int J = 0 ; J < return_tables.size() ; J++ ){
                //System.out.println("\t" + String.valueOf(return_tables.get(J)) );
                
                DefaultMutableTreeNode a=new DefaultMutableTreeNode( String.valueOf(return_tables.get(J)) );
                top.add(a);
                
            }
            
            selectedNode.add(top);
            
        }
        
        DefaultTreeModel model = (DefaultTreeModel) database_tree.getModel();
        model.reload();
        
    }
    
    public void update_JTable( DefaultMutableTreeNode node , JTable data_table , database_manager dao ){
        
        Object nodeInfo = node.getUserObject();
        
        //System.out.print(nodeInfo + " : ");
        //System.out.println(node.isLeaf());  ///If yes, node is alone, that means it is a table
        
        String[] columns = null;
        Object[][] data = null;
        
        String path = String.valueOf(node.getParent()) + "." + String.valueOf(nodeInfo);    ///Goes to table
        
        try{
        
            if( node.isLeaf() ){
                List<Object[]> column_names = dao.query_list("DESCRIBE " + path);
                String[] cols = new String[column_names.size()];
                for( int I = 0 ; I < column_names.size() ; I++ ){

                    cols[I] = String.valueOf(column_names.get(I)[0]);

                }
                columns = cols;

                List<Object[]> data_list = dao.query_list("SELECT * FROM " + path);

                if( data_list.size() > 0 ){ ///If list isnt empty

                    Object[][] dat = new Object[ data_list.size() ][ Array.getLength( data_list.get(0) ) ];

                    for( int I = 0 ; I < data_list.size() ; I++ ){
                        for( int J = 0 ; J < data_list.get(I).length ; J++ ){
                            dat[I][J] = data_list.get(I)[J];
                        }
                    }
                    data = dat;

                }

                DefaultTableModel tm = new DefaultTableModel(data , columns);
                data_table.setModel( tm );
                data_table.revalidate();

            }
        }catch( IllegalArgumentException ex ){
            throw ex;
        }
    }
    
}
