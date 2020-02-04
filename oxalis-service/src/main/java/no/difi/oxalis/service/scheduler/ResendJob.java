/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.scheduler;

import no.difi.oxalis.service.util.Log;
import no.difi.oxalis.service.transmission.OutboundService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author aktharhussainis
 */
@DisallowConcurrentExecution
public class ResendJob implements Job {
    
    @Override
    public void execute(JobExecutionContext pArg0) throws JobExecutionException {
        
        try {
            
            Log.info("Inside Resend Job...");
            OutboundService outboundService = OutboundService.getInstance();
            outboundService.resend();
        } catch (Exception ex) {
            Log.error("Exception while resend job: " + ex.getMessage());
        }
    }
}
