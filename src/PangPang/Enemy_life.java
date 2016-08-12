package PangPang;

/**
 * @author KJW finish at 2016/ 08/ 12
 * @version 2.0.0v
 * @description this class for the PangPang, this class manage the pangapng
 *              enemy life
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class Enemy_life {
	private int enemy_Life[][] = new int[6][8];

	/**
	 * Enemy life constructor
	 * 
	 * @param data
	 *            - information string
	 */
	public Enemy_life(String data) {
		String tmp[] = data.split("\n");
		String s;

		for (int i = 1; i < tmp.length; i++) {
			s = tmp[i];
			for (int j = 0; j < 8; j++) {
				switch (s.charAt(j)) {
				case '-':
					enemy_Life[i - 1][j] = -1;
					break;
				default:
					enemy_Life[i - 1][j] = s.charAt(j) - 48; // 숫자로 변환
				}
			} // for j
		} // for i

	}

	/**
	 * get enemy life
	 * 
	 * @param kind
	 * @param num
	 * @return
	 */

	public int get_Enemy_Life(int kind, int num) {
		return enemy_Life[kind][num];
	}

}
