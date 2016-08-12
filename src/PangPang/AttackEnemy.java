package PangPang;

import java.util.Random;

import ServerMainBody.Settings;

/**
 * @author KJW finish at 2016/ 08/ 11
 * @version 2.0.0v
 * @description this class for the PangPang, this class manage the PangPang
 *              Enemy
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class AttackEnemy {
	public int loop = 0; // 루프 카운터
	private Random rnd = new Random();
	private int r1, r2;
	private Map_Controler map_ctrl;
	private PangPangEnemy enemys[][];

	/**
	 * pangapng Enemy constructor
	 * 
	 * @param mpCtrl
	 *            - Map_Controler
	 * @param mEnemy
	 *            - Enemy Set
	 */
	public AttackEnemy(Map_Controler mpCtrl, PangPangEnemy mEnemy[][]) {
		this.map_ctrl = mpCtrl;
		enemys = mEnemy;
	}

	/**
	 * init enemy attack
	 */
	public void ResetAttack() {
		loop = 0;
	}

	/**
	 * Attack mode
	 */
	public void Attack() {
		if (map_ctrl.get_Enemy_live_Count() <= Settings.nTotalPangPangEnemyAttackWhenThisNumber) {
			AttackAll(); // 일제 총공격
			return;
		}

		loop++;
		int n = loop - (map_ctrl.get_Attack_Time() + 180);
		if (n < 0)
			return; // 모든 캐릭터가 입장할 때 까지 대기

		switch (n % 1800) {
		case 0:
			r1 = rnd.nextInt(10) + 1;
			AttackPath(3, 1, r1); // 3등급, 1번기 : r1번 공격 루트로 출격
			AttackPath(3, 3, r1);
			AttackPath(2, 1, r1);
			break;
		case 150:
			r1 = rnd.nextInt(10) + 1;
			AttackPath(5, 4, r1); // 5등급 4번기 : r1번 공격 루트로 출력
			AttackPath(5, 2, r1);
			AttackPath(4, 0, r1);
			break;
		case 300:
			r1 = rnd.nextInt(10) + 1;
			AttackPath(3, 0, r1);
			AttackPath(3, 2, r1);
			AttackPath(2, 4, r1);
			break;
		case 450:
			r1 = rnd.nextInt(10) + 1;
			AttackPath(0, 2, r1);
			AttackPath(1, 3, r1);
			AttackPath(1, 4, r1);
			break;
		case 600:
			r1 = rnd.nextInt(10) + 1;
			AttackPath(5, 3, r1);
			AttackPath(5, 5, r1);
			AttackPath(4, 6, r1);
			break;
		case 750:
			r1 = rnd.nextInt(10) + 1;
			AttackPath(3, 6, r1);
			AttackPath(3, 4, r1);
			AttackPath(2, 2, r1);
			break;
		case 900:
			r1 = rnd.nextInt(10) + 1;
			r2 = rnd.nextInt(10) + 1;
			AttackPath(2, 7, r1);
			AttackPath(2, 5, r1);
			AttackPath(0, 5, r2);
			AttackPath(1, 1, r2);
			break;
		case 1050:
			r1 = rnd.nextInt(10) + 1;
			r2 = rnd.nextInt(10) + 1;
			AttackPath(4, 6, r1);
			AttackPath(4, 5, r1);
			AttackPath(3, 5, r1);
			AttackPath(3, 7, r2);
			AttackPath(4, 4, r2);
			break;
		case 1200:
			r1 = rnd.nextInt(10) + 1;
			r2 = rnd.nextInt(10) + 1;
			AttackPath(5, 6, r1);
			AttackPath(5, 1, r1);
			AttackPath(2, 6, r2);
			AttackPath(2, 3, r2);
			break;
		case 1350:
			r1 = rnd.nextInt(10) + 1;
			r2 = rnd.nextInt(10) + 1;
			AttackPath(1, 2, r1);
			AttackPath(1, 6, r1);
			AttackPath(2, 0, r2);
			AttackPath(4, 3, r2);
			break;
		case 1600:
			r1 = rnd.nextInt(10) + 1;
			r2 = rnd.nextInt(10) + 1;
			AttackPath(4, 2, r1);
			AttackPath(4, 1, r1);
			AttackPath(1, 5, r2);
			AttackPath(5, 7, r2);
			break;
		}
	}

	/**
	 * decide Attack Path when Enemy state is Attack
	 * 
	 * @param kind
	 * @param num
	 * @param aKind
	 */
	private void AttackPath(int kind, int num, int aKind) {
		// 위에서 지시한 공격 루트로 이동 - Sprite Attack 참조
		enemys[kind][num].begin_Attack(aKind);
	}

	/**
	 * all of enemy Attack mode
	 */
	private void AttackAll() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				if (enemys[i][j].get_Statue() == PangPangEnemy.SYNC)
					AttackPath(i, j, rnd.nextInt(10) + 1); // 무작위 공격
			}
		}
	}
} // end Class
