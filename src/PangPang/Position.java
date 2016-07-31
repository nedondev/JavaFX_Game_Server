package PangPang;

import ServerMainBody.Settings;

public class Position {
	private int pos_X[][] = new int[6][8];
	private int pos_Y[][] = new int[6][8];
	private int enemy[][] = new int[6][8];

	public Position(String data) {
		String tmp[] = data.split("\n");
		String s;
		char ch;

		// enemy number
		for (int i = 1; i < tmp.length; i++) {
			s = tmp[i];
			for (int j = 0; j < 8; j++) {
				ch = s.charAt(j);
				if (ch == '-')
					enemy[i - 1][j] = -1;
				else if (ch <= '9')
					enemy[i - 1][j] = ch - 48; // 0-9
				else
					enemy[i - 1][j] = ch - 87; // 'a'~'z'
			} // for j
		} // for i

		int top = 105; // ���� �������� ���� ������ �Ÿ� �̹��� ũ�� 20 * 20
		int left = 90; // ���� ���� ����
		int wid = 25; // ���� ���� �¿� ����
		int x;

		// ���� view�� �׷��� ó�� ��ǥ�� ���ϴ� ��.
		for (int i = 0; i < 6; i++) {
			if (i <= 1) {
				for (int j = 0; j < 8; j++) {
					pos_X[i][j] = j * wid + left;
					pos_Y[i][j] = Settings.nGameAsteroidSceneHeight - ((6-i) * wid + top);
				} // for j
			} else {
				for (int j = 0; j < 8; j++) {
					if (j % 2 == 0)
						x = 3 - j / 2;
					else
						x = j / 2 + 4;
					pos_X[i][j] = x * wid + left;
					pos_Y[i][j] = Settings.nGameAsteroidSceneHeight -((6-i) * wid + top);
				}// for j
			} // if
		}// for i

	}
	
	//-------------------------------------
	//ĳ���� ��ȣ ���ϱ�
	//-------------------------------------
	public int getEnemyNum(int kind, int num){
		return enemy[kind][num];
	}
	
	//-------------------------------------
	//pos_x
	//-------------------------------------
	public int get_Pos_X(int kind, int num){
		return pos_X[kind][num];
	}
	
	//-------------------------------------
	//pos_y
	//-------------------------------------
	public int get_Pos_Y(int kind, int num){
		return pos_Y[kind][num];
	}
}
