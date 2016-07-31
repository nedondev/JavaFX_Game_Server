package PangPang;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Map_Controler {


	// 16 ���⿡ ���� �ﰢ�Լ� x�� ������ ������ �������ذ� ���⼭ 0���� ������ �������� �ð������ ȸ���Ѵ�.
	private float direction_dis_X[] = { 0f, 0.39f, 0.75f, 0.93f, 1f, 0.93f,
			0.75f, 0.39f, 0f, -0.39f, -0.75f, -0.93f, -1f, -0.93f, -0.75f,
			-0.39f };

	private float dircetion_dis_Y[] = { -1f, -0.93f, -0.75f, -0.39f, -0f, +0.39f,
			+0.75f, +0.93f, +1f, +0.93f, +0.75f, +0.39f, 0f, -0.39f, -0.75f,
			-0.93f };

	public int syncCnt = 0; // ��ũ ����
	public int dirLen = 75; // ���� ���� ���� �� ó������ �̵��� �Ÿ�
	public int dir = 4; // ���� ����
	public int dirCnt = 0; // ���� �δ밡 �̵��� �Ÿ�

	private Path mPath; // Path
	private Selection mSelect; // Select
	private DelayTime mDelay; // Delay
	private Position mPos; // Position
	private Enemy_life mEny_life; // Enemy_life

	private int enemy_Cnt; // �� �������� ����ִ� ����
	private int attackTime; // ���� ���� �ð�

	public Map_Controler() {
	}

	// -------------------------------------
	// Read map file
	// -------------------------------------
	public void readMap(int map_Number) {
		// stage map loading

		InputStream map_file;
		try {
			map_file = new FileInputStream("src/Asset/stage0"+map_Number+".stg");
			
			try {
				byte[] data = new byte[map_file.available()];
				map_file.read(data);
				map_file.close();
				String load_data = new String(data, "EUC-KR");
				makeMap(load_data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void makeMap(String data) {
		int n1 = data.indexOf("selection");
		mPath = new Path(data.substring(0, n1)); // Path

		int n2 = data.indexOf("delay");
		mSelect = new Selection(data.substring(n1, n2)); // Selection
		enemy_Cnt = mSelect.GetEnemyCount(); // ������ ��

		n1 = data.indexOf("position");
		mDelay = new DelayTime(data.substring(n2, n1)); // Delay
		attackTime = mDelay.getDelay(0, 5); // ������ ĳ����

		n2 = data.indexOf("shield");
		mPos = new Position(data.substring(n1, n2)); // Position

		mEny_life = new Enemy_life(data.substring(n2)); // enemy_life

	}

	// -------------------------------------------------------------
	// get_function
	// -------------------------------------------------------------

	// life_Enemy_count
	public int get_Enemy_live_Count() {
		return this.enemy_Cnt;
	}

	// init_start_attack_time
	public int get_Attack_Time() {
		return this.attackTime;
	}

	// single_path
	public SinglePath get_Path(int num) {
		return this.mPath.getPath(num);
	}

	// get_selection(���� ��ġ)
	public int get_Selection(int kind, int num) {
		return this.mSelect.getSelection(kind, num);
	}

	// get_pos_x (���� ��ǥ_X)
	public int get_Pos_X(int kind, int num) {
		return this.mPos.get_Pos_X(kind, num);
	}

	// get_pos_y (���� ��ǥ_Y)
	public int get_Pos_Y(int kind, int num) {
		return this.mPos.get_Pos_Y(kind, num);
	}

	// get_enemy_num
	public int get_Enemy_Num(int kind, int num) {
		return this.mPos.getEnemyNum(kind, num);
	}

	// get_enemy_life
	public int get_Enemy_Life(int kind, int num) {
		return this.mEny_life.get_Enemy_Life(kind, num);
	}

	// get_delay
	public int get_Delay(int kind, int num) {
		return this.mDelay.getDelay(kind, num);
	}

	public float get_Direc_Dis_X(int tag) {
		return this.direction_dis_X[tag];
	}

	public float get_Direc_Dis_Y(int tag) {
		return this.dircetion_dis_Y[tag];
	}

	public void set_Enemy_Cnt_Minus() {
		this.enemy_Cnt -= 1;
	}
	
	public void set_Enemy_Cnt(int number) {
		this.enemy_Cnt = number;
	}
}
