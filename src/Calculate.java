import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;
public class Calculate extends JFrame {
	public Calculate() {
		setTitle("김혜원 - 2020316011");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(new NorthPannel(), BorderLayout.NORTH);
		c.add(new CenterPannel(), BorderLayout.CENTER);
		c.add(new SouthPannel(), BorderLayout.SOUTH);
		setSize(500, 500);
		setVisible(true);
	}
	public static void main(String[] args) {
		new Calculate();
		
	}
}

class NorthPannel extends JPanel {
	public NorthPannel() {
		setLayout(new GridLayout(3, 1));
		JLabel jl1 = new JLabel("");
		jl1.setBackground(Color.BLACK);
		jl1.setOpaque(true);
		add(jl1);
		FlickeringLabel clockLabel = new FlickeringLabel("", 1000);
		clockLabel.setFont(new Font("휴먼둥근헤드라인", Font.PLAIN, 25));
		clockLabel.setForeground(Color.WHITE);
		clockLabel.setOpaque(true);
		clockLabel.setBackground(Color.BLACK);
		add(clockLabel);
		JLabel jl2 = new JLabel();
		jl2.setBackground(Color.BLACK);
		jl2.setOpaque(true);
		add(jl2);

		ClockThread th = new ClockThread(clockLabel);
		setVisible(true);
		th.start();
	}
}

class SouthPannel extends JPanel {
	public static JLabel txt = new JLabel("0", SwingConstants.RIGHT);
	public SouthPannel() {
		txt.setFont(new Font("D2Coding 보통", Font.PLAIN, 50));
		txt.setForeground(Color.WHITE);
		setBackground(Color.DARK_GRAY);
		add(txt);
	}
}

class CenterPannel extends JPanel {
	String btn [] = {"C", "", "%", "/", "7", "8", "9", "x", "4", "5", "6", "-", "1", "2", "3", "+", "", "0", ".", "="};
	JButton jBtn [] = new JButton[btn.length];
	
	public CenterPannel() {
		setLayout(new GridLayout(5, 4, 4, 4));
		setBackground(Color.DARK_GRAY);

		for(int i=0; i<btn.length; i++) {
			jBtn[i] = new JButton(btn[i]);
			if (i<=2) {
				jBtn[i].setBackground(Color.LIGHT_GRAY);
			} else if((i%4)==3) {
				jBtn[i].setBackground(Color.GREEN);
			} else {
				jBtn[i].setBackground(Color.YELLOW);
			}
			jBtn[i].setFont(new Font("D2Coding 보통", Font.PLAIN, 25));
			jBtn[i].setBorderPainted(false);
			add(jBtn[i]);
			jBtn[i].addActionListener(new MyActionListener());
		}
	}
}

class MyActionListener implements ActionListener {
	private ArrayList<String> op = new ArrayList<String>(List.of("%", "/", "x", "-", "+"));
	private static String num1;
	private static String num2;
	private static String num3;
	private static String tmp; 
	
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		
		if(b.getText().equals("C")) {
			SouthPannel.txt.setText("0");
			num1 = null;
			num2 = null;
			tmp = null;
		} 	
		else if(op.contains(b.getText())) {
			if(num1 != null && tmp == null) {
				if(num3 != null) {
					num1 = num3 + "." + num1;
					num3 = null;
				}
				tmp = b.getText();
				num2 = num1;
				num1 = null;
				SouthPannel.txt.setText(SouthPannel.txt.getText() + b.getText());
			} else {
				SouthPannel.txt.setText("0");
			}
		} 	
		else if(b.getText().equals("=")) {
			Double d = null;
			
			if(num1 != null && tmp == null ) {
				if(num3 != null) {
					num1 = num3 + "." + num1;
					num3 = null;
				}
				d = Double.parseDouble(num1);
			}
			else if(num1 != null && num2 != null) {
				if(num3 != null) {
					num1 = num3 + "." + num1;
					num3 = null;
				}
				Double dNum1 = Double.parseDouble(num1);
				Double dNum2 = Double.parseDouble(num2);
				switch(tmp) {
				case "%":
					d = (dNum2/100)*dNum1;
					break;
				case "/":
					d = dNum2/dNum1;
					break;
				case "x":
					d = dNum2*dNum1;
					break;
				case "-":
					d = dNum2-dNum1;
					break;
				case "+":
					d = dNum2+dNum1;
					break;
				}
			}
			else if(num2 != null && tmp.equals("%")) {
				d = Double.parseDouble(num2)/100;
			}
			
			num1 = d.toString();
			num2 = null;
			tmp = null;
			
			SouthPannel.txt.setText(d.toString());
		}	
		else if(b.getText().equals(".")) {
			if(SouthPannel.txt.getText().equals("0")) {
				num1 = "0";
			}
			if(num1 != null) {
				num3 = num1;
				num1 = null;
				SouthPannel.txt.setText(SouthPannel.txt.getText() + b.getText());
			} else {
				SouthPannel.txt.setText("0");
			}
		}
		else if(b.getText().equals("")) {
			SouthPannel.txt.setText("0");
		}
		else {
			if(SouthPannel.txt.getText().equals("0")) {
				SouthPannel.txt.setText("");
			}
			if(b.getText().equals("0")) {
				if(SouthPannel.txt.getText().equals("0")) {
					SouthPannel.txt.setText("0");
				} else if(num1 == "0") {
					SouthPannel.txt.setText(SouthPannel.txt.getText());
				} else {
					if(num1 == null) {
						num1 = b.getText();
					} else {
						num1 = num1 + b.getText();
					}
					SouthPannel.txt.setText(SouthPannel.txt.getText() + b.getText());
				}
			} else {
				if(num1 == null) {
					num1 = b.getText();
				} else {
					num1 = num1 + b.getText();
				}
				SouthPannel.txt.setText(SouthPannel.txt.getText() + b.getText());
			}
		}
	}
}

class ClockThread extends Thread {
	private JLabel clockLabel;
	
	public ClockThread(JLabel clockLabel) {
		this.clockLabel = clockLabel;
	}
	public void run() {
		int i=0;
		while(true) {
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String disp = sdf.format(dt);
			i++;
			clockLabel.setText(disp);
			try {
				Thread.sleep(1000);
			} catch(Exception e) {
				return;
			}
		}
	}
}

class FlickeringLabel extends JLabel implements Runnable {
	private long delay;
	Color [] colors = {Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW};
	public FlickeringLabel(String text, long delay) {
		super(text, SwingConstants.CENTER);
		this.delay = delay;
		setOpaque(true);
		Thread th = new Thread(this);
		th.start();
	}
	@Override
	public void run() {
		int n = 0;
		while(true) {
			Random r = new Random();
			if(n == 0)
				setForeground(colors[r.nextInt(colors.length)]);
			else
				setForeground(colors[r.nextInt(colors.length)]);
			if (n == 0) n = 1; else n = 0;
			try {
				Thread.sleep(delay);
			} catch(InterruptedException e) {
				return;
			}
		}
	}
}