package ru.sberbank.jms.util.messaging; /*****************************************************************************/
/*                                                                           */
/* (c) Copyright IBM Corp. 2004  All rights reserved.                        */
/*                                                                           */
/* This sample program is owned by International Business Machines           */
/* Corporation or one of its subsidiaries ("IBM") and is copyrighted         */
/* and licensed, not sold.                                                   */
/*                                                                           */
/* You may copy, modify, and distribute this sample program in any           */
/* form without payment to IBM,  for any purpose including developing,       */
/* using, marketing or distributing programs that include or are             */
/* derivative works of the sample program.                                   */
/*                                                                           */
/* The sample program is provided to you on an "AS IS" basis, without        */
/* warranty of any kind.  IBM HEREBY  EXPRESSLY DISCLAIMS ALL WARRANTIES,    */
/* EITHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED     */
/* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.       */
/* Some jurisdictions do not allow for the exclusion or limitation of        */
/* implied warranties, so the above limitations or exclusions may not        */
/* apply to you.  IBM shall not be liable for any damages you suffer as      */
/* a result of using, modifying or distributing the sample program or        */
/* its derivatives.                                                          */
/*                                                                           */
/*****************************************************************************/
/*                                                                           */
/* Program name: MQSetMsgId                                                  */
/*                                                                           */
/* Description: This program sets the message id and correlation id of a     */
/*              message before putting it on the SYSTEM.DEFAULT.LOCAL.QUEUE. */
/*                                                                           */
/* Note: This program has been tested against WebSphere MQ 5.3 CSD 7 using   */
/*       Java JDK 1.4.2.  You will need to change the qManager name if you   */
/*       are not using the default queue manager.                            */
/*                                                                           */
/*****************************************************************************/
import java.util.Hashtable;

import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;            // Include the MQ package

public class MQSetMsgId {

  /***********************************************************************/
  /* set qManager to the name of your queue manager if not using default */
  /***********************************************************************/
  private String qManager = "";
  private MQQueueManager qMgr;

  /***********************************************************************/
  /* Start everything up ....                                            */
  /***********************************************************************/
  public static void main (String args[]) {
    MQSetMsgId mySample = new MQSetMsgId();
    mySample.init();
    mySample.start();
    System.exit(0);
  }


  public void init() {
   /* initialization goes here */
  } // end of init


  /***********************************************************************/
  /* Actually run the program....                                        */
  /***********************************************************************/
  public void start() {

    try {
      /*****************************/
      /* Create a queue manager    */
      /*****************************/
    	Hashtable properties = new Hashtable();
    	properties.put(MQC.HOST_NAME_PROPERTY,"esk1.ca.sbrf.ru");
    	properties.put(MQC.PORT_PROPERTY, "1415");
    	properties.put(MQC.CHANNEL_PROPERTY, "SYSTEM.DEF.SVRCONN");
    	properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
      qMgr = new MQQueueManager("JMS01.DEMO",properties);

      /*****************************/
      /* Open the queue for output */
      /*****************************/
      int openOptions = MQC.MQOO_OUTPUT ;
      MQQueue SDLQ = qMgr.accessQueue("JMS.LQ",
                                      openOptions, null, null, null);

      /*****************************/
      /* Create a message to put   */
      /*****************************/
      MQMessage hello_world = new MQMessage();
      hello_world.writeUTF("Hello World!");

      MQPutMessageOptions pmo = new MQPutMessageOptions(); 

      /***************************************/
      /* Set the messageId and correlationId */
      /***************************************/
      String messageId     = "MessageId goes here.....";
      hello_world.messageId = messageId.getBytes();

      String correlationId = "SBERBANK.MINSK";
      hello_world.correlationId = correlationId.getBytes();

      /*****************************/
      /* Put the message           */
      /*****************************/
      SDLQ.put(hello_world,pmo);
      System.out.println("The message is put.....");

      /*****************************/
      /* Close and disconnect      */
      /*****************************/
      SDLQ.close();
      qMgr.disconnect();
    }

    /************************************/
    /* Catch any errors and report them */
    /************************************/
    catch (MQException ex) {
      System.out.println("An MQ error occurred : Completion code " +
                         ex.completionCode +
                         " Reason code " + ex.reasonCode);
    } catch (java.io.IOException ex) {
      System.out.println("An error occurred writing to message buffer: " +
                         ex);
    }

  } /* end of start */

} /* end of program */




