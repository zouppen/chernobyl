import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.GroupLayout.Alignment.CENTER;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

public class Ui extends JFrame {

    private JProgressBar progBar;
    private JButton startBtn;
    private JLabel display;
    private final int MAX_VAL = 1000;
    private boolean running = false;

    public void updateDose(double dose) {
	String payload = String.format("Dose: %.2f mSv", dose);
	display.setText(payload);
	progBar.setValue((int)dose);

	if (dose > MAX_VAL) {
	    progBar.setStringPainted(true);
	    progBar.setForeground(Color.red);
	    progBar.setString("DEAD");
	    startBtn.setEnabled(false);
	}
    }

    public boolean isRunning() {
	return running;
    }
    
    public Ui() {
        initUI();
    }

    private void initUI() {

        progBar = new JProgressBar(0, MAX_VAL);
	progBar.setOrientation(JProgressBar.VERTICAL);
        progBar.setStringPainted(true);

	display = new JLabel("XXXXXXXXXXXXXX");
	
        startBtn = new JButton("Start");
        startBtn.addActionListener(new ClickAction());

        createLayout(progBar, display, startBtn);

        setTitle("Chernobyl");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	updateDose(0);
    }

    private void createLayout(JComponent... arg) {
        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
                .addPreferredGap(UNRELATED)
                .addGroup(gl.createParallelGroup()
                        .addComponent(arg[1])
                        .addComponent(arg[2]))
        );

        gl.setVerticalGroup(gl.createParallelGroup(CENTER)
                .addComponent(arg[0])
                .addGroup(gl.createSequentialGroup()
                        .addComponent(arg[1])
                        .addComponent(arg[2]))
        );

        pack();
    }

    private class ClickAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (running) {
		running = false;
                startBtn.setText("Start");
            } else {
		running = true;
                startBtn.setText("Found!");
            }
        }
    }
}
