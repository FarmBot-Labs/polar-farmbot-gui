package farmBot;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class FarmBotGui {
   private JButton yButton;
   private JButton xButton;
   private JButton x_Button;
   private JButton y_Button;
   private JButton rectMoveButton;
   private JButton polarMoveButton;
   private JPanel root;
   private JSpinner spinnerX;
   private JSpinner spinnerY;
   private JSpinner spinnerR;
   private JSpinner spinnerT;
   private JSpinner spinnerZ;
   private JTextField currentX;
   private JTextField currentY;
   private JTextField currentZ;
   private JTextField currentT;
   private JTextField currentR;
   private JButton zButton;
   private JButton z_Button;
   private JRadioButton a100RadioButton;
   private JRadioButton a500RadioButton;
   private JRadioButton a1000RadioButton;
   private JButton rButton;
   private JButton r_Button;
   private JButton tButton;
   private JButton t_Button;
   private JTextArea scriptArea;
   private JCheckBox repeatCheckBox;
   private JButton runButton;

   private FarmBotSerial serial;

   private static final int RADIUS_STEPS = 6000;
   // private static final double STEPS_PER_DEG 14.9173553 // 27:1
   private static final double STEPS_PER_DEG = 28.5714286; // 51:1

   /* t in degrees */
   private long[] polarToRect(int r, int tDeg) {
      long rect[] = new long[2];
      double tRad = tDeg * Math.PI / 180;

      rect[0] = (long) (r * cos(tRad)) + RADIUS_STEPS; // x
      rect[1] = (long) (r * sin(tRad)) + RADIUS_STEPS; // y

      return rect;
   }

   private FarmBotGui() {
      serial = new FarmBotSerial();
      serial.initialize(this);
      yButton.addActionListener(e -> {
         Integer newY = Integer.parseInt(currentY.getText());

         if (a100RadioButton.isSelected()) newY += 100;
         else if (a500RadioButton.isSelected()) newY += 500;
         else if (a1000RadioButton.isSelected()) newY += 1000;
         else newY += 3000;
         serial.send("G00 X" + currentX.getText() + " Y" + newY.toString() + " Z" + currentZ.getText());
      });
      y_Button.addActionListener(e -> {
         Integer newY = Integer.parseInt(currentY.getText());

         if (a100RadioButton.isSelected()) newY -= 100;
         else if (a500RadioButton.isSelected()) newY -= 500;
         else if (a1000RadioButton.isSelected()) newY -= 1000;
         else newY -= 3000;
         serial.send("G00 X" + currentX.getText() + " Y" + newY.toString() + " Z" + currentZ.getText());
      });
      xButton.addActionListener(e -> {
         Integer newX = Integer.parseInt(currentX.getText());

         if (a100RadioButton.isSelected()) newX += 100;
         else if (a500RadioButton.isSelected()) newX += 500;
         else if (a1000RadioButton.isSelected()) newX += 1000;
         else newX += 3000;
         serial.send("G00 X" + newX.toString() + " Y" + currentY.getText() + " Z" + currentZ.getText());
      });
      x_Button.addActionListener(e -> {
         Integer newX = Integer.parseInt(currentX.getText());

         if (a100RadioButton.isSelected()) newX -= 100;
         else if (a500RadioButton.isSelected()) newX -= 500;
         else if (a1000RadioButton.isSelected()) newX -= 1000;
         else newX -= 3000;
         serial.send("G00 X" + newX.toString() + " Y" + currentY.getText() + " Z" + currentZ.getText());
      });
      zButton.addActionListener(e -> {
         Integer newZ = Integer.parseInt(currentZ.getText());

         if (a100RadioButton.isSelected()) newZ += 100;
         else if (a500RadioButton.isSelected()) newZ += 500;
         else if (a1000RadioButton.isSelected()) newZ += 1000;
         else newZ += 3000;
         serial.send("G00 X" + currentX.getText() + " Y" + currentY.getText() + " Z" + newZ.toString());
      });
      z_Button.addActionListener(e -> {
         Integer newZ = Integer.parseInt(currentZ.getText());

         if (a100RadioButton.isSelected()) newZ -= 100;
         else if (a500RadioButton.isSelected()) newZ -= 500;
         else if (a1000RadioButton.isSelected()) newZ -= 1000;
         else newZ -= 3000;
         serial.send("G00 X" + currentX.getText() + " Y" + currentY.getText() + " Z" + newZ.toString());
      });
      rButton.addActionListener(e -> {
         Long newX = Long.parseLong(currentX.getText());
         Long newY = Long.parseLong(currentY.getText());
         double tRad = Integer.parseInt(currentT.getText()) * Math.PI / 180;

         if (a100RadioButton.isSelected()) newX += (long) (100 * cos(tRad));
         else if (a500RadioButton.isSelected()) newX += (long) (500 * cos(tRad));
         else if (a1000RadioButton.isSelected()) newX += (long) (1000 * cos(tRad));
         else newX += (long) (3000 * cos(tRad));
         if (a100RadioButton.isSelected()) newY += (long) (100 * sin(tRad));
         else if (a500RadioButton.isSelected()) newY += (long) (500 * sin(tRad));
         else if (a1000RadioButton.isSelected()) newY += (long) (1000 * sin(tRad));
         else newY += (long) (3000 * sin(tRad));
         serial.send("G00 X" + newX.toString() + " Y" + newY.toString() + " Z" + currentZ.getText());
      });
      r_Button.addActionListener(e -> {
         Long newX = Long.parseLong(currentX.getText());
         Long newY = Long.parseLong(currentY.getText());
         double tRad = Integer.parseInt(currentT.getText()) * Math.PI / 180;

         if (a100RadioButton.isSelected()) newX -= (long) (100 * cos(tRad));
         else if (a500RadioButton.isSelected()) newX -= (long) (500 * cos(tRad));
         else if (a1000RadioButton.isSelected()) newX -= (long) (1000 * cos(tRad));
         else newX -= (long) (3000 * cos(tRad));
         if (a100RadioButton.isSelected()) newY -= (long) (100 * sin(tRad));
         else if (a500RadioButton.isSelected()) newY -= (long) (500 * sin(tRad));
         else if (a1000RadioButton.isSelected()) newY -= (long) (1000 * sin(tRad));
         else newY -= (long) (3000 * sin(tRad));
         serial.send("G00 X" + newX.toString() + " Y" + newY.toString() + " Z" + currentZ.getText());
      });
      rectMoveButton.addActionListener(e -> serial.send(
       "G00 X" + spinnerX.getValue().toString() + " Y" + spinnerY.getValue().toString() + " Z" +
        spinnerZ.getValue().toString()));
      polarMoveButton.addActionListener(e -> {
         long rect[] = polarToRect((int) spinnerR.getValue(), (int) spinnerT.getValue());

         serial.send(
           "G00 X" + Long.toString(rect[0]) + " Y" + Long.toString(rect[1]) + " Z" +
            spinnerZ.getValue().toString());
      });
      spinnerX.setValue(6000);
      spinnerY.setValue(6000);
      runButton.addActionListener(e -> serial.cmdQueue.addAll(Arrays.asList(scriptArea.getText().split("\\n"))));
   }

   public static void main(String[] args) {
      JFrame frame = new JFrame("FarmBotGui");
      FarmBotGui fbg = new FarmBotGui();

      frame.setContentPane(fbg.root);
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.pack();
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            fbg.serial.close();
            e.getWindow().dispose();
         }
      });
      try {
         Thread.sleep(3000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      fbg.rectMoveButton.doClick();
   }

   void update(String x, String y, String r, String t, String z) {
      currentX.setText(x);
      currentY.setText(y);
      currentR.setText(r);
      currentT.setText(Integer.toString((int) (Long.parseLong(t) / STEPS_PER_DEG)));
      currentZ.setText(z);
   }

   void doneWithScript() {
      if (repeatCheckBox.isSelected()) runButton.doClick();
   }
}
