// Reference: http://playground.arduino.cc/Interfacing/Java

package farmBot;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Enumeration;
import java.util.Queue;

public class FarmBotSerial implements SerialPortEventListener {
   private static final String PORT_NAMES[] = {"/dev/cu.usbmodem1411", "/dev/cu.usbmodem1421"};
   private static final int TIME_OUT = 2000;
   private static final int DATA_RATE = 115200;

   private FarmBotGui gui;
   private SerialPort serialPort;
   private BufferedReader input;
   private OutputStream output;

   Queue<String> cmdQueue = new ArrayDeque<>();

   void initialize(FarmBotGui gui) {
      CommPortIdentifier portId = null;
      Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

      this.gui = gui;
      while (portEnum.hasMoreElements()) {
         CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

         for (String portName : PORT_NAMES) {
            if (currPortId.getName().equals(portName)) {
               portId = currPortId;
               break;
            }
         }
      }
      if (portId == null) {
         System.out.println("Could not find COM port.");

         return;
      }
      try {
         serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
         serialPort
          .setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
         input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
         output = serialPort.getOutputStream();
         serialPort.addEventListener(this);
         serialPort.notifyOnDataAvailable(true);
      } catch (Exception e) {
         System.err.println(e.toString());
      }
   }

   synchronized void close() {
      if (serialPort != null) {
         serialPort.removeEventListener();
         serialPort.close();
      }
   }

   void send(String message) {
      System.out.println(message);
      try {
         output.write(message.getBytes(Charset.forName("UTF-8")));
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public synchronized void serialEvent(SerialPortEvent oEvent) {
      if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
         try {
            String inputLine = input.readLine();

            System.out.println(inputLine);
            if (inputLine.startsWith("R82 ")) {
               String[] parts = inputLine.split(" ");

               if (parts.length == 6) gui
                .update(parts[1].substring(1), parts[2].substring(1), parts[3].substring(1), parts[4].substring(1),
                 parts[5].substring(1));
            }
            if (inputLine.startsWith("R02")) {
               if (!cmdQueue.isEmpty()) send(cmdQueue.poll());
               else gui.doneWithScript();
            }
         } catch (Exception e) {
            System.err.println(e.toString());
         }
      }
   }
}
