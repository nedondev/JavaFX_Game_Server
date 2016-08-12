package PangPang;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author KJW finish at 2016/ 08/ 12
 * @version 2.0.0v
 * @description this class for the PangPang, this class manage the pangapng
 *              Map_Controler
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class Map_Controler {

	/**
	 * x axis direction
	 */
	private float direction_dis_X[] = { 0f, 0.39f, 0.75f, 0.93f, 1f, 0.93f, 0.75f, 0.39f, 0f, -0.39f, -0.75f, -0.93f,
			-1f, -0.93f, -0.75f, -0.39f };

	/**
	 * y axis direction
	 */
	private float dircetion_dis_Y[] = { -1f, -0.93f, -0.75f, -0.39f, -0f, +0.39f, +0.75f, +0.93f, +1f, +0.93f, +0.75f,
			+0.39f, 0f, -0.39f, -0.75f, -0.93f };

	public int syncCnt = 0;
	public int dirLen = 75;
	public int dir = 4;
	public int dirCnt = 0;

	private Path mPath;
	private Selection mSelect;
	private DelayTime mDelay;
	private Position mPos;
	private Enemy_life mEny_life;

	private int enemy_Cnt;
	private int attackTime;

	/**
	 * Map_Controler constructor
	 */
	public Map_Controler() {
	}

	/**
	 * read map information from stage0x.stg
	 * 
	 * @param map_Number
	 *            - x number
	 */
	public void readMap(int map_Number) {

		InputStream map_file;
		try {
			map_file = new FileInputStream("src/Asset/stage0" + map_Number + ".stg");

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

	/**
	 * make information by splitting information file stage0x.stg
	 * 
	 * @param data
	 */
	public void makeMap(String data) {
		int n1 = data.indexOf("selection");
		mPath = new Path(data.substring(0, n1)); // Path

		int n2 = data.indexOf("delay");
		mSelect = new Selection(data.substring(n1, n2)); // Selection
		enemy_Cnt = mSelect.GetEnemyCount(); // enemy number

		n1 = data.indexOf("position");
		mDelay = new DelayTime(data.substring(n2, n1)); // Delay
		attackTime = mDelay.getDelay(0, 5); // final count

		n2 = data.indexOf("shield");
		mPos = new Position(data.substring(n1, n2)); // Position

		mEny_life = new Enemy_life(data.substring(n2)); // enemy_life

	}

	/**
	 * get Enemy number which living now on the game
	 * 
	 * @return
	 */
	public int get_Enemy_live_Count() {
		return this.enemy_Cnt;
	}

	/**
	 * get enemy attack time
	 * 
	 * @return
	 */
	public int get_Attack_Time() {
		return this.attackTime;
	}

	/**
	 * get enemy attack or moving path
	 * 
	 * @param num
	 * @return
	 */
	public SinglePath get_Path(int num) {
		return this.mPath.getPath(num);
	}

	/**
	 * get enemy sort selection
	 * 
	 * @param kind
	 * @param num
	 * @return
	 */
	public int get_Selection(int kind, int num) {
		return this.mSelect.getSelection(kind, num);
	}

	/**
	 * get position x information
	 * 
	 * @param kind
	 * @param num
	 * @return
	 */
	public int get_Pos_X(int kind, int num) {
		return this.mPos.get_Pos_X(kind, num);
	}

	/**
	 * get position y information
	 * 
	 * @param kind
	 * @param num
	 * @return
	 */
	public int get_Pos_Y(int kind, int num) {
		return this.mPos.get_Pos_Y(kind, num);
	}

	/**
	 * get enemy number
	 * 
	 * @param kind
	 * @param num
	 * @return
	 */
	public int get_Enemy_Num(int kind, int num) {
		return this.mPos.getEnemyNum(kind, num);
	}

	/**
	 * get enemy life number
	 * 
	 * @param kind
	 * @param num
	 * @return
	 */
	public int get_Enemy_Life(int kind, int num) {
		return this.mEny_life.get_Enemy_Life(kind, num);
	}

	/**
	 * get enemy delay information
	 * 
	 * @param kind
	 * @param num
	 * @return
	 */
	public int get_Delay(int kind, int num) {
		return this.mDelay.getDelay(kind, num);
	}

	/**
	 * get x axis
	 * 
	 * @param tag
	 * @return
	 */
	public float get_Direc_Dis_X(int tag) {
		return this.direction_dis_X[tag];
	}

	/**
	 * get y axis
	 * 
	 * @param tag
	 * @return
	 */
	public float get_Direc_Dis_Y(int tag) {
		return this.dircetion_dis_Y[tag];
	}

	/**
	 * decrease enemy number count
	 */
	public void set_Enemy_Cnt_Minus() {
		this.enemy_Cnt -= 1;
	}

	/**
	 * set enemy number
	 * 
	 * @param number
	 */
	public void set_Enemy_Cnt(int number) {
		this.enemy_Cnt = number;
	}
}
