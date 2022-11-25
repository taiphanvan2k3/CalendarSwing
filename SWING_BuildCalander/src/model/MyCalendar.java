package model;
public class MyCalendar {
	private static boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
			return true;
		return false;
	}

	public static int getDayInMonth(int month, int year) {
		int days[] = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (isLeapYear(year))
			days[2] = 29;
		return days[month];
	}

	// Hàm tìm số ngày từ ngày 01/01/01 đến ngày 01/month/year
	public static int getDay(int month, int year) {
		int N = year - 1;
		int res = 365 * N + (N / 4 - N / 100 + N / 400);
		for (int i = 1; i < month; i++) {
			res += getDayInMonth(i, year);
		}
		return res;
	}

	// Lấy ra thứ của ngày đầu tiên của tháng month năm year
	public static int getThu(int month, int year) {
		// Vì ngày 1/1/1 là thứ 2 nên nếu số ngày có dạng 7k
		// thì thứ đó cũng là thứ 2
		return getDay(month, year) % 7 + 2;
		// Giá trị trả về: 2->8
	}

	public static int[][] update(int month, int year) {
		// Mảng a gồm 6 hàng 7 cột ( 7 cột tương ứng là 7 thứ )
		int a[][] = new int[6][7];
		int thu = getThu(month, year);
		int DaysOfCurrentMonth = getDayInMonth(month, year);
		int DaysOfPreviousMonth;
		if (month > 1)
			DaysOfPreviousMonth = getDayInMonth(month - 1, year);
		else {
			// Số ngày của tháng 12 năm trước
			DaysOfPreviousMonth = getDayInMonth(12, year - 1);
		}
		int start = thu - 1;

		// Nếu ngày đầu tiên của tháng là ngày CN thì sẽ không in các
		// ngày của tháng trước
		if (start == 7)
			start = 0;

		int i = 0, j = start;
		for (int k = 1; k <= DaysOfCurrentMonth; k++) {
			a[i][j] = k;
			j++;
			if (j == 7) {
				i++;
				j = 0;
			}
		}

		// Điền số ngày của một phần tháng trước ngay trước ngày đầu
		// của tháng hiện tại
		for (int k = start - 1; k >= 0; k--)
			a[0][k] = DaysOfPreviousMonth--;

		int temp = 1;
		while (!(i == 6 && j == 0)) {
			a[i][j] = temp++;
			j++;
			if (j == 7) {
				i++;
				j = 0;
			}
		}
		return a;
	}

	public static int getDayOfWeek(int day, int month, int year) {
		int thu = getThu(month, year);
		if ((thu + day - 1) % 7 == 1)
			return 8;
		return (thu + day - 1) % 7;
	}
}
