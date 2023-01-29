package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;
import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.MyCalendar;

public class CalendarUI extends JFrame implements ActionListener, WindowListener {
	
	JButton listBtn[][] = new JButton[7][7];
	private String[] nameOfMonth = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };
	private String[] dayOfWeek = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };
	private Calendar c;
	private int monthCurr, yearCurr, dayCurr;
	private int days[][] = new int[6][7];

	private Timer timer;
	private JTextField txtInputYear;

	private String DateTime[][] = new String[7][7];
	// Dùng hai biến preYear,preMonth để kiểm tra xem việc cập nhật có diễn ra hay
	// không.Nếu người dùng thay đổi month hoặc year thì sẽ cập nhật lại lịch
	int preYear, preMonth;

	// Lưu 2 vị trí đã của ngày trong tháng đã được click trước đó
	// để lát tắt border cho vị trí đó
	private int posI_pressed = -1, posJ_pressed = -1;

	private EventCalander eventCalander;

	public void initComponents() {
		JPanel pnlCalendar = new JPanel(new GridLayout(7, 7));
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				listBtn[i][j] = new JButton("");
				listBtn[i][j].addActionListener(this);
				listBtn[i][j].setActionCommand((i * 7 + j) + "");
				listBtn[i][j].setForeground(Color.white);
				listBtn[i][j].setBackground(Color.black);
				listBtn[i][j].setForeground(Color.white);
				listBtn[i][j].setBorder(null);
				listBtn[i][j].setFont(new Font("Britannic Bold", Font.BOLD, 25));
				pnlCalendar.add(listBtn[i][j]);
			}
		}

		for (int i = 0; i < 7; i++)
			listBtn[0][i].setText(dayOfWeek[i]);

		for (int i = 0; i < 7; i++)
			listBtn[0][i].setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.cyan));
		JComboBox comboBox = new JComboBox<>();
		for (int i = 0; i < nameOfMonth.length; i++)
			comboBox.addItem(nameOfMonth[i]);
		comboBox.setForeground(Color.white);
		comboBox.setBackground(Color.DARK_GRAY);
		comboBox.setFont(new Font("Britannic Bold", 1, 20));
		// vì index đếm từ 0
		comboBox.setSelectedIndex(monthCurr - 1);

		txtInputYear = new JTextField();
		txtInputYear.setHorizontalAlignment(JTextField.CENTER);
		txtInputYear.setBackground(Color.black);
		txtInputYear.setForeground(Color.white);
		txtInputYear.setFont(new Font("Britannic Bold", 1, 20));
		txtInputYear.setBorder(null);

		JPanel pnlNorth = new JPanel(new GridLayout(1, 2));
		pnlNorth.add(comboBox);
		pnlNorth.add(txtInputYear);

		this.add(pnlNorth, BorderLayout.NORTH);
		this.add(pnlCalendar, BorderLayout.CENTER);

		timer = new Timer(200, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Do thời gian là realtime do đó nên lấy thời gian mỗi lúc
				Calendar c = Calendar.getInstance();
				dayCurr = c.get(Calendar.DAY_OF_MONTH);
				monthCurr = c.get(Calendar.MONTH) + 1;
				yearCurr = c.get(Calendar.YEAR);

				int m = comboBox.getSelectedIndex() + 1;
				String txtYear = txtInputYear.getText();
				if (txtYear.length() > 0)
					txtYear.trim();
				if (txtYear.matches("[0-9]+")) {
					int y = Integer.valueOf(txtYear);
					if (y != preYear || m != preMonth) {
						update(m, y);
						preMonth = m;
						preYear = y;
					}
				}
			}
		});
		this.addWindowListener(this);
		this.setVisible(true);
	}

	public void initFrame() {
		this.setSize(500, 398);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		Point p = this.getLocation();
		this.setLocation(p.x - 255, p.y);
	}

	private String length2(int x) {
		String res = "";
		if (x < 10)
			res += "0";
		res += x;
		return res;
	}

	public void reset() {
		for (int i = 0; i < 7; i++)
			for (int j = 0; j < 7; j++) {
				listBtn[i][j].setBorder(null);
				DateTime[i][j] = null;
			}
	}

	public void update(int month, int year) {
		reset();
		int thu = MyCalendar.getThu(month, year);
		int DaysOfCurrentMonth = MyCalendar.getDayInMonth(month, year);
		int DaysOfPreviousMonth;
		if (month > 1)
			DaysOfPreviousMonth = MyCalendar.getDayInMonth(month - 1, year);
		else
			DaysOfPreviousMonth = MyCalendar.getDayInMonth(12, year - 1);

		txtInputYear.setText(year + "");
		int start = thu - 1;
		// Nếu ngày đầu tiên của tháng là ngày CN thì sẽ không in các
		// ngày của tháng trước
		if (start == 7)
			start = 0;
		int i = 1, j = start;
		for (int k = 1; k <= DaysOfCurrentMonth; k++) {
			listBtn[i][j].setText(k + "");
			listBtn[i][j].setForeground(Color.white);
			listBtn[i][j].setBackground(Color.black);
			DateTime[i][j] = length2(k) + "-" + length2(month) + "-" + year;
			if (k == dayCurr && monthCurr == month && year == yearCurr) {
				// Nếu đúng là ngày hiện tại thì tô xanh lên
				listBtn[i][j].setBackground(Color.cyan);
			}
			j++;
			if (j == 7) {
				j = 0;
				i++;
			}
		}

		// Điền số ngày của một phần tháng trước ngay trước ngày đầu
		// của tháng hiện tại
		for (int k = start - 1; k >= 0; k--) {
			listBtn[1][k].setText(DaysOfPreviousMonth-- + "");
			listBtn[1][k].setForeground(Color.gray);
		}

		int cnt = 1;
		while (!(i == 7 && j == 0)) {
			listBtn[i][j].setText(cnt++ + "");
			listBtn[i][j].setForeground(Color.gray);
			j++;
			if (j == 7) {
				i++;
				j = 0;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int k = Integer.valueOf(e.getActionCommand());
		int i = k / 7;
		int j = k % 7;
		if (DateTime[i][j] != null) {
			if (posI_pressed != -1)
				listBtn[posI_pressed][posJ_pressed].setBorder(null);
			posI_pressed = i;
			posJ_pressed = j;
			listBtn[i][j].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.red));
			if (!eventCalander.isVisible()) {
				eventCalander.setVisible(true);
			}
			eventCalander.updateEvent(DateTime[i][j]);
		}
	}

	public CalendarUI() {
		c = Calendar.getInstance();
		this.dayCurr = c.get(Calendar.DAY_OF_MONTH);
		this.preMonth = this.monthCurr = c.get(Calendar.MONTH) + 1;
		this.preYear = this.yearCurr = c.get(Calendar.YEAR);
		eventCalander = new EventCalander(length2(dayCurr) + "-" + length2(monthCurr) + "-" + yearCurr);
		eventCalander.setVisible(false);
		this.initFrame();
		this.initComponents();
		this.update(monthCurr, yearCurr);
		timer.start();
	}

	public void LuuduLieu() {
		if (eventCalander != null)
			eventCalander.writeEvent();
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		eventCalander.writeEvent();
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		//test xem token còn dùng được hay không?
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
