package PangPang;

/**
 * @author KJW finish at 2016/ 08/ 11
 * @version 2.0.0v
 * @description this class for the PangPang, this class manage the pangapng
 *              enemy delaytime
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class DelayTime {

	private int delay[][] = new int[6][8];

	/**
	 * DelayTiem constructor
	 * 
	 * @param str
	 *            - data
	 */
	public DelayTime(String str) {

		String tmp[] = str.split("\n");
		String s;
		for (int i = 1; i < tmp.length; i++) {
			for (int j = 0; j < 8; j++) {
				s = tmp[i].substring(j * 4, (j + 1) * 4).trim();
				if (s.equals("---"))
					delay[i - 1][j] = -1;
				else
					delay[i - 1][j] = Integer.parseInt(s);
			} // j
		} // i
	} // creator

	/**
	 * get enemy delay time
	 * 
	 * @param kind
	 * @param num
	 * @return
	 */
	public int getDelay(int kind, int num) {
		return this.delay[kind][num];
	}

}
