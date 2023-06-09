
import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import java.awt.Toolkit;

public class Main extends JFrame implements ActionListener {

	public Container container = null;
	JMenuItem start, exit;
	JButton landlord[] = new JButton[2];
	JButton publishCard[] = new JButton[2];
	int dizhuFlag;
	int turn;//记录谁是下一个出牌的人，main.turn=(main.turn+1)%3
	JLabel dizhu;
	List<Card> currentList[] = new Vector[3];
	List<Card> playerList[] = new Vector[3];

	List<Card> lordList;

	Card card[] = new Card[56];
	JTextField time[] = new JTextField[3];
	Time t;
	boolean nextPlayer = false;
	dealCard p0;

	public Main() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("images/dizhu.gif"));//窗口标题图表
		Init();
		SetMenu();
		this.setVisible(true);

		CardInit();
		p0=new dealCard(this);;
		Thread pp0 = new Thread(p0);
		pp0.start();

		getLord();
		time[1].setVisible(true);
		t = new Time(this, 10);


		t.start();
	}

	public void Init() {

		this.setTitle("斗地主");
		this.setSize(830, 620);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(getOwner());

		container = this.getContentPane();
		container.setLayout(null);
		container.setBackground(Color.LIGHT_GRAY);
	}

	public void SetMenu() {
		JMenuBar jMenuBar = new JMenuBar();
		jMenuBar.setBorderPainted(false);
		jMenuBar.setBackground(Color.WHITE);
		this.setJMenuBar(jMenuBar);

		start = new JMenuItem("新游戏");
		jMenuBar.add(start);
		exit = new JMenuItem("退出");
		jMenuBar.add(exit);
		exit.addActionListener(this);

		start.addActionListener(this);

		landlord[0] = new JButton("抢地主");
		landlord[1] = new JButton("不     抢");
		publishCard[0] = new JButton("出牌");
		publishCard[1] = new JButton("不要");
		for (int i = 0; i < 2; i++) {
			publishCard[i].setBounds(320 + i * 100, 400, 60, 20);
			landlord[i].setBounds(320 + i * 100, 400, 75, 20);
			container.add(landlord[i]);
			landlord[i].addActionListener(this);
			landlord[i].setVisible(false);
			container.add(publishCard[i]);
			publishCard[i].setVisible(false);
			publishCard[i].addActionListener(this);
		}
		for (int i = 0; i < 3; i++) {
			time[i] = new JTextField("倒计时:");
			time[i].setEditable(false);
			time[i].setVisible(false);
			container.add(time[i]);
		}
		time[0].setBounds(140, 230, 60, 20);
		time[1].setBounds(374, 360, 60, 20);
		time[2].setBounds(620, 230, 60, 20);

		for (int i = 0; i < 3; i++) {
			currentList[i] = new Vector<Card>();
		}
	}

	public void CardInit() {

		int count = 1;
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 13; j++) {
				if ((i == 5) && (j > 2))
					break;
				else {
					card[count] = new Card(this, i + "-" + j, false);
					card[count].setLocation(350, 50);
					container.add(card[count]);
					count++;
				}
			}
		}

		//洗牌，洗100次
		Random random = new Random();
		for (int i = 0; i < 100; i++) {
			int a = random.nextInt(54) + 1;
			int b = random.nextInt(54) + 1;

			Card k = card[a];
			card[a] = card[b];
			card[b] = k;
		}

		for (int i = 0; i < 3; i++)
			playerList[i] = new Vector<Card>();

		lordList = new Vector<Card>();

		dizhu = new JLabel(new ImageIcon("images/dizhu.gif"));
		dizhu.setVisible(false);
		dizhu.setSize(40, 40);
		container.add(dizhu);
	}

	public void getLord() {
		for (int i = 0; i < 2; i++)
			landlord[i].setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exit) {
			this.dispose();
			System.exit(0);
		}
		if (e.getSource() == start) {
			this.dispose();
			new Main();
		}
		if (e.getSource() == landlord[0]) {
			time[1].setText("抢地主");
			t.isRun = false;
		}
		if (e.getSource() == landlord[1]) {
			time[1].setText("不抢");
			t.isRun = false;
		}
		if (e.getSource() == publishCard[1]) {
			this.nextPlayer = true;
			currentList[1].clear();
			time[1].setText("不要");
		}
		if (e.getSource() == publishCard[0]) {
			List<Card> c = new Vector<Card>();
			for (int i = 0; i < playerList[1].size(); i++) {
				Card card = playerList[1].get(i);
				if (card.clicked) {
					c.add(card);
				}
			}
			int flag = 0;

			if (time[0].getText().equals("不要") && time[2].getText().equals("不要")) {
				if (Common.jugdeType(c) != CardType.c0)//可以出牌
					flag = 1;
			} else {
				flag = Common.checkCards(c, currentList, this);
			}

			if (flag == 1) {
				currentList[1] = c;
				playerList[1].removeAll(currentList[1]);
				Point point = new Point();
				point.x = (770 / 2) - (currentList[1].size() + 1) * 15 / 2;
				point.y = 300;
				for (int i = 0, len = currentList[1].size(); i < len; i++) {
					Card card = currentList[1].get(i);
					Common.move(card, card.getLocation(), point);
					point.x += 15;
				}
				Common.rePosition(this, playerList[1], 1);
				time[1].setVisible(false);
				this.nextPlayer = true;
			}

		}
	}

	public static void main(String args[]) {
		new Main();
	}

}
