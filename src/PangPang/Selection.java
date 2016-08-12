package PangPang;

/**
 * @author KJW finish at 2016/ 08/ 12
 * @version 2.0.0v
 * @description this class for the PangPang, this class manage the pangapng
 *              Selection
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class Selection {
	private int pathNum[][] = new int[6][8];
	private int enemy_ToTal_Cnt; // total enemy number.

	public Selection(String data) {
		String tmp[] = data.split("\n");
		String s;
		enemy_ToTal_Cnt = 0;
		char ch;

		for (int i = 1; i < tmp.length; i++) {
			s = tmp[i];
			for (int j = 0; j < 8; j++) {
				ch = s.charAt(j);
				switch (ch) {
				case '-':
					pathNum[i - 1][j] = -1;
					break;

				default:
					enemy_ToTal_Cnt++; // total enemy number
					if (ch <= '9')
						pathNum[i - 1][j] = ch - 48; // 0 ~9;
					else
						pathNum[i - 1][j] = ch - 87; // 'a'~'z'
				}
			} // for j
		} // for i

	}// creator

	/**
	 * get Enemy path information
	 * 
	 * @param kind
	 * @param num
	 * @return
	 */
	public int getSelection(int kind, int num) {

		return this.pathNum[kind][num];
	}

	/**
	 * get enemy count information
	 * 
	 * @return
	 */
	public int GetEnemyCount() {
		return this.enemy_ToTal_Cnt;
	}

}
