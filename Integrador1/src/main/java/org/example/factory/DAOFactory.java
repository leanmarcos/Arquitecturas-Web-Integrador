package org.example.factory;

public abstract class DAOFactory{

    private static final int mysql = 1;
    private static DAOFactory instance;

    public static DAOFactory getInstance(int db){
       if (instance==null){
           switch (db){
               case mysql:
                   // instance = va ala fabrica concreta que es mysqldaofactory
                   break;
               default:
                   return null;
           }
       }
       return instance;
    }
    //y aca va los metodos abstractos que deben ser implementados por la fabrica concreta
}
