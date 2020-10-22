/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zerp labs
 */
public class AsynchronousExecUtil {
    
    private static Logger LOGGER = LoggerFactory.getLogger(AsynchronousExecUtil.class);
    
    private static ExecutorService pool;
    
    static { 
        pool = Executors.newCachedThreadPool();
    }
    
    public static <T> Future<T> call(Callable<T> t) {
        
        return  pool.submit(t);
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        
        if(!pool.isTerminated()) {
            LOGGER.warn("There are some unfiunished jobs during shutdown");
        }
    }
    
}
