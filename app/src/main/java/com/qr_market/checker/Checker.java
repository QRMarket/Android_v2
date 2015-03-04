package com.qr_market.checker;

/**
 * @author Kemal Sami KARACA
 * @since 04.03.2015
 * @version v1.01
 */
public class Checker {

    public Checker(){}


    /**
     *
     * @param args
     * @return
     *
     * This function check that any parameter is null
     */
    public static <T> boolean anyNull(T... args){

        boolean notNull=true;
        for(T arg:args)
            notNull = notNull && ( arg!=null );

        return !notNull;
    }

}
