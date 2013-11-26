package ru.sberbank.jms.util.messaging;

// ===========================================================================
//
// %PUB_START%
// Licensed Materials - Property of IBM
// 
// 5724-H72, 5655-L82, 5724-L26
// 
// (c) Copyright IBM Corp. 1995, 2002, 2005
// 
// US Government Users Restricted Rights - Use, duplication or
// disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
// %PUB_END%
//
// ===========================================================================
// WebSphere MQ classes for Java sample application
//
// This sample runs as a Java application using the command :-
// 
// 		java MQSample

import com.ibm.mq.*; //Include the WebSphere MQ classes for Java package

import java.net.URL;

public class MQSample {

  // code identifier
  public static final String sccsid = "@(#) javabase/samples/MQSample.java, java, j600, j600-204-080509 1.10.1.2 07/11/15 14:21:41";

  // define the name of the QueueManager
  private static final String qManager = "my_queue_manager";
  // and define the name of the Queue
  private static final String qName = "SYSTEM.DEFAULT.LOCAL.QUEUE";

  // main method: simply call the runSample() method
  public static void main(String args[]) {
    new MQSample().runSample();
  }

  public void runSample() {

    try {
      // Create a connection to the QueueManager
      System.out.println("Connecting to queue manager: " + qManager);
      MQQueueManager qMgr = new MQQueueManager(qManager);

      // Set up the options on the queue we wish to open
      int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT;

      // Now specify the queue that we wish to open and the open options
      System.out.println("Accessing queue: " + qName);
      MQQueue queue = qMgr.accessQueue(qName, openOptions);

      // Define a simple WebSphere MQ Message ...
      MQMessage msg = new MQMessage();
      // ... and write some text in UTF8 format
      msg.writeUTF("Hello, World!");

      // Specify the default put message options
      MQPutMessageOptions pmo = new MQPutMessageOptions();

      // Put the message to the queue
      System.out.println("Sending a message...");
      queue.put(msg, pmo);

      // Now get the message back again. First define a WebSphere MQ message
      // to receive the data
      MQMessage rcvMessage = new MQMessage();

      // Specify default get message options
      MQGetMessageOptions gmo = new MQGetMessageOptions();

      // Get the message off the queue.
      System.out.println("...and getting the message back again");
      queue.get(rcvMessage, gmo);

      // And display the message text...
      String msgText = rcvMessage.readUTF();
      System.out.println("The message is: " + msgText);

      // Close the queue
      System.out.println("Closing the queue");
      queue.close();

      // Disconnect from the QueueManager
      System.out.println("Disconnecting from the Queue Manager");
      qMgr.disconnect();
      System.out.println("Done!");
    }
    catch (MQException ex) {
      System.out.println("A WebSphere MQ Error occured : Completion Code " + ex.completionCode
          + " Reason Code " + ex.reasonCode);
    }
    catch (java.io.IOException ex) {
      System.out.println("An IOException occured whilst writing to the message buffer: " + ex);
    }
  }
}