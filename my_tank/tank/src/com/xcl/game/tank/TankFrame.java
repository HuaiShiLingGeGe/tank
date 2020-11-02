package com.xcl.game.tank;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.xcl.game.Constant.Constant;
import com.xcl.game.Constant.Dir;
import com.xcl.game.Constant.Group;
import com.xcl.game.bullet.Bullet;
import com.xcl.game.bullet.Explode;

public class TankFrame extends Frame {

	private Tank myTank = new Tank(375, 550, Dir.UP, Group.GOOD, this);
	private List<Bullet> bullets = new CopyOnWriteArrayList<>();
	private List<Tank> enemyTanks = new CopyOnWriteArrayList<>();
	private List<Explode> explodes = new CopyOnWriteArrayList<Explode>();

	public Tank getMyTank() {
		return myTank;
	}

	public void setMyTank(Tank myTank) {
		this.myTank = myTank;
	}

	public List<Explode> getExplodes() {
		return explodes;
	}

	public void setExplodes(List<Explode> explodes) {
		this.explodes = explodes;
	}

	public TankFrame() {
		setResizable(false);
		setTitle("坦克大战");
		setVisible(true);
		setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);

		// 窗口监听
		addWindowListener(new WindowAdapter() {
			// 启用窗口关闭功能
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// 键盘事件监听
		addKeyListener(new KeyAdapter() {

			// ButtonUp, ButtonDown, ButtonLeft, ButtonRight四个按键， 用于记录按键是否被按下
			boolean bU = false;
			boolean bD = false;
			boolean bL = false;
			boolean bR = false;

			// 按键被按下时触发
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_UP:
					bU = true;
					break;

				case KeyEvent.VK_DOWN:
					bD = true;
					break;

				case KeyEvent.VK_LEFT:
					bL = true;
					break;

				case KeyEvent.VK_RIGHT:
					bR = true;
					break;

				case KeyEvent.VK_SPACE:
					myTank.fire();
					break;

				default:
					break;
				}
				setMainTank();

			}

			// 按键松开时触发 松开时四个按键方向变为false
			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_UP:
					bU = false;
					break;

				case KeyEvent.VK_DOWN:
					bD = false;
					break;

				case KeyEvent.VK_LEFT:
					bL = false;
					break;

				case KeyEvent.VK_RIGHT:
					bR = false;
					break;

				default:
					break;
				}
				setMainTank();
			}

			public void setMainTank() {
				if (!bD && !bU && !bL && !bR) {
					myTank.setMoving(false);
				} else {
					myTank.setMoving(true);

					if (bU)
						myTank.setDir(Dir.UP);
					if (bD)
						myTank.setDir(Dir.DOWN);
					if (bL)
						myTank.setDir(Dir.LEFT);
					if (bR)
						myTank.setDir(Dir.RIGHT);
				}
			}
		});

	}

	public Tank getTank() {
		return myTank;
	}

	public void setTank(Tank tank) {
		this.myTank = tank;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(List<Bullet> bullets) {
		this.bullets = bullets;
	}

	@Override
	public void paint(Graphics g) {

		// 获取子弹数量
		Color color = g.getColor();
		g.setColor(Color.RED);
		g.drawString("子弹数量： " + bullets.size(), 10, 60);
		g.drawString("敌人数量： " + enemyTanks.size(), 100, 60);
		g.setColor(color);

		// 画出主坦克
		myTank.paint(g);

		// 花出子弹
		for (Bullet bullet : bullets) {
			bullet.paint(g);
		}

		// 画出敌对坦克
		for (Tank tank : enemyTanks) {
			tank.paint(g);
		}

		// 判断子弹和坦克碰撞之后的逻辑
		for (int i = 0; i < bullets.size(); i++) {
			for (int j = 0; j < enemyTanks.size(); j++) {
				bullets.get(i).collideWith(enemyTanks.get(j));
			}
		}

		// 显示爆炸效果
		for (int i = 0; i < explodes.size(); i++) {
			explodes.get(i).paint(g);
//			explodes.remove(i);
		}
	}

	public List<Tank> getEnemyTanks() {
		return enemyTanks;
	}

	public void setEnemyTanks(List<Tank> enemyTanks) {
		this.enemyTanks = enemyTanks;
	}

	// 双缓冲处理屏幕闪烁的问题
	Image offScreImage = null;

	@Override
	public void update(Graphics g) {
		if (offScreImage == null) {
			offScreImage = this.createImage(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
		}
		Graphics gOffScren = offScreImage.getGraphics();
		Color color = gOffScren.getColor();
		gOffScren.setColor(Color.WHITE);
		gOffScren.fillRect(0, 0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
		gOffScren.setColor(color);
		paint(gOffScren);
		g.drawImage(offScreImage, 0, 0, null);
	}

}
