package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EventCalander extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtDate;
	private JTextArea txtAreaShowEvent;
	private String selectedDate;
	private JButton btnAddEvent;

	private String listEvent[][] = new String[10000][2];
	private int num = 0;// số lượng hàng của mảng hiện đang có sự kiện
	// 1 mảng 2 chiều mà mỗi hàng gồm 2 phần tử (phần tử thứ nhất chứa ngày,
	// phần tử thứ hai chứa sự kiện của ngày đó)

	public EventCalander(String date) {
		selectedDate = date;
		initJFrame();
		initComponents();
		this.readEvent();
	}

	public void initJFrame() {
		this.setSize(500, 398);
		this.setLocationRelativeTo(null);
		this.setLocale(null);
		Point p = this.getLocation();
		this.setLocation(p.x + 245, p.y);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	public void initComponents() {
		txtDate = new JTextField(selectedDate);
		txtDate.setFont(new Font("Algerian", Font.BOLD, 40));
		txtDate.setForeground(Color.white);
		txtDate.setBackground(Color.black);
		txtDate.setHorizontalAlignment(JTextField.CENTER);
		txtDate.setEnabled(false);

		txtAreaShowEvent = new JTextArea();
		txtAreaShowEvent.setFont(new Font("Consolas", Font.BOLD, 20));
		txtAreaShowEvent.setBackground(Color.black);
		txtAreaShowEvent.setForeground(Color.white);

		btnAddEvent = new JButton("New Event");
		btnAddEvent.setFont(new Font("Algerian", Font.BOLD, 25));
		btnAddEvent.setBackground(Color.gray);
		btnAddEvent.setForeground(Color.white);
		btnAddEvent.addActionListener(this);

		this.setLayout(new BorderLayout());
		this.add(txtDate, BorderLayout.NORTH);
		this.add(txtAreaShowEvent, BorderLayout.CENTER);
		this.add(btnAddEvent, BorderLayout.SOUTH);
		this.setVisible(true);
	}

	public String getEvent(String time) {
		for (int i = 0; i < num; i++) {
			if (listEvent[i][0].equals(time)) {
				String res = listEvent[i][1];
				res = res.replaceAll(",", "\n");
				return res;
			}
		}
		return "No Event";
	}

	public void updateEvent(String date) {
		selectedDate = date;
		txtDate.setText(date);
		String evt = this.getEvent(date);
		if(!evt.equals("No Event"))
			txtAreaShowEvent.setForeground(Color.yellow);
		else txtAreaShowEvent.setForeground(Color.white);
		evt = "\n" + evt;
		evt = evt.replaceAll("\n", "\n       >");
		txtAreaShowEvent.setText(evt);
	}

	public void addEventIntoSelectedDate(String date, String event) {
		boolean checkIsExist = false;
		for (int i = 0; i < num; i++) {
			if (listEvent[i][0].equals(date)) {
				listEvent[i][1] += "," + event;
				checkIsExist = true;
				break;
			}
		}

		if (!checkIsExist) {
			listEvent[num][0] = date;
			listEvent[num++][1] = event;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String str = JOptionPane.showInputDialog(this, "More events for " + selectedDate, "Add new event",
				JOptionPane.INFORMATION_MESSAGE);
		if (str != null && str.length() > 0) {
			// str==null nếu nhấn cancel
			this.addEventIntoSelectedDate(selectedDate, str);
			this.updateEvent(selectedDate);
		}
	}

	public void readEvent() {
		num = 0;
		//Phải mở file kiểu này mới export ra có dữ liệu được
		//File f=new File(this.getClass().getClassLoader().getResource("dataOutput.txt").getPath());
		File f=new File("test.txt");
		try (BufferedReader reader = Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				// Mỗi dòng gồm date và sự kiện ngăn cách nhau bởi :
				String ds[] = line.split(":");
				if (ds.length == 2) {
					while (ds[1].indexOf("  ") >= 0)
						ds[1] = ds[1].replace("  ", " ");
					listEvent[num][0] = ds[0];
					listEvent[num++][1] = ds[1];
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeEvent() {
		File f=new File("test.txt");
		//File f = new File(this.getClass().getClassLoader().getResource("dataOutput.txt").getPath());
		try (FileOutputStream fos = new FileOutputStream(f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
				BufferedWriter writer = new BufferedWriter(osw);) {
			for (int i = 0; i < num; i++) {
				writer.write(listEvent[i][0] + ":" + listEvent[i][1]);
				if (i < num - 1)
					writer.newLine();
			}

			writer.close();
			osw.close();
			fos.close();
		} catch (IOException e) {
			
		}
	}
}
