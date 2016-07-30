package PangPang;

public class DelayTime {

	private int delay[][] = new int[6][8];

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

	public int getDelay(int kind, int num) {
		return this.delay[kind][num];
	}

}
