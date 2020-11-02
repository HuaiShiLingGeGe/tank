package com.xcl.game;

import com.xcl.game.Constant.Dir;
import com.xcl.game.Constant.Group;
import com.xcl.game.tank.Tank;
import com.xcl.game.tank.TankFrame;

public class App {
	private static TankFrame tankFrame = new TankFrame();

	public App() {
	}

	public static void main(String[] args) throws InterruptedException {
		// 初始化敌对坦克
		for (int i = 0; i < 3; i++) {
			tankFrame.getEnemyTanks().add(new Tank(80 + 50 * i, 80, Dir.DOWN, Group.BAD, tankFrame));
		}
		while (true) {

			Thread.sleep(10);
			tankFrame.repaint();
		}
	}
}
