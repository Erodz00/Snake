import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
public class Snake {

	public static void main(String[] args) {

		Window f1 = new Window();

		f1.setTitle("Snake");
		f1.setSize(720, 720);
		f1.setVisible(true);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
class Window extends JFrame {
	private static final long serialVersionUID = -2542001418764869760L;
	public static ArrayList<ArrayList<DataOfSquare>> Grid;
	public static int width = 20;
	public static int height = 20;

	public Window() {

		Grid = new ArrayList<ArrayList<DataOfSquare>>();
		ArrayList<DataOfSquare> data;

		for (int i = 0; i < width; i++) {
			data = new ArrayList<DataOfSquare>();
			for (int j = 0; j < height; j++) {
				DataOfSquare c = new DataOfSquare(2);
				data.add(c);
			}
			Grid.add(data);
		}

		getContentPane().setLayout(new GridLayout(20, 20, 0, 0));

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				getContentPane().add(Grid.get(i).get(j).square);
			}
		}

		jTuple position = new jTuple(10, 10);
		ThreadsController c = new ThreadsController(position);
		c.start();

		this.addKeyListener((KeyListener) new KeyboardListener());

	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
class DataOfSquare {

	ArrayList<Color> C = new ArrayList<Color>();
	int color;
	SquarePanel square;

	public DataOfSquare(int col) {

		C.add(Color.GREEN);// 0
		C.add(Color.RED); // 1
		C.add(Color.BLACK); // 2
		color = col;
		square = new SquarePanel(C.get(color));
	}

	public void lightColorUp(int c) {
		square.ChangeColor(C.get(c));
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
class SquarePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public SquarePanel(Color d) {
		this.setBackground(d);
	}

	public void ChangeColor(Color d) {
		this.setBackground(d);
		this.repaint();
	}

}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
class jTuple {
	public int x;
	public int y;
	public int x1;
	public int y1;

	public jTuple(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void changeValues(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getXf() {
		return x1;
	}

	public int getYf() {
		return y1;
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
class KeyboardListener extends KeyAdapter {

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case 39:
			if (ThreadsController.directionSnake != 2)
				ThreadsController.directionSnake = 1;
			break;
		case 38:
			if (ThreadsController.directionSnake != 4)
				ThreadsController.directionSnake = 3;
			break;

		case 37:
			if (ThreadsController.directionSnake != 1)
				ThreadsController.directionSnake = 2;
			break;

		case 40:
			if (ThreadsController.directionSnake != 3)
				ThreadsController.directionSnake = 4;
			break;
		default:
			break;
		}
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////
class ThreadsController extends Thread {
	ArrayList<ArrayList<DataOfSquare>> Squares = new ArrayList<ArrayList<DataOfSquare>>();
	jTuple headSnakePos;
	int sizeSnake = 3;
	long speed = 50;
	public static int directionSnake;

	ArrayList<jTuple> positions = new ArrayList<jTuple>();
	jTuple foodPosition;

	ThreadsController(jTuple positionDepart) {
		Squares = Window.Grid;
		headSnakePos = new jTuple(positionDepart.x, positionDepart.y);
		directionSnake = 1;
		jTuple headPos = new jTuple(headSnakePos.getX(), headSnakePos.getY());
		positions.add(headPos);
		foodPosition = new jTuple(Window.height - 1, Window.width - 1);
		foodSpawn(foodPosition);

	}

	private void pauser() {
		try {
			sleep(speed);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void checkCollision() {
		jTuple posCritique = positions.get(positions.size() - 1);
		for (int i = 0; i <= positions.size() - 2; i++) {
			boolean suicide = posCritique.getX() == positions.get(i).getX()
					&& posCritique.getY() == positions.get(i).getY();
			if (suicide) {
				stopTheGame();
			}
		}

		boolean eatingFood = posCritique.getX() == foodPosition.y && posCritique.getY() == foodPosition.x;
		if (eatingFood) {
			System.out.println("oh boy i sure do love food");
			sizeSnake = sizeSnake + 1;
			foodPosition = getValAleaNotInSnake();
			foodSpawn(foodPosition);
		}
	}

	private void stopTheGame() {
		System.out.println("u ded \n");
		while (true) {
			pauser();
		}
	}

	private void foodSpawn(jTuple foodPositionIn) {
		Squares.get(foodPositionIn.x).get(foodPositionIn.y).lightColorUp(1);
	}

	private jTuple getValAleaNotInSnake() {
		jTuple p;
		int ranX = 0 + (int) (Math.random() * 19);
		int ranY = 0 + (int) (Math.random() * 19);
		p = new jTuple(ranX, ranY);
		for (int i = 0; i <= positions.size() - 1; i++) {
			if (p.getY() == positions.get(i).getX() && p.getX() == positions.get(i).getY()) {
				ranX = 0 + (int) (Math.random() * 19);
				ranY = 0 + (int) (Math.random() * 19);
				p = new jTuple(ranX, ranY);
				i = 0;
			}
		}
		return p;
	}

	private void moveInterne(int dir) {
		switch (dir) {
		case 4:
			headSnakePos.changeValues(headSnakePos.x, (headSnakePos.y + 1) % 20);
			positions.add(new jTuple(headSnakePos.x, headSnakePos.y));
			break;
		case 3:
			if (headSnakePos.y - 1 < 0) {
				headSnakePos.changeValues(headSnakePos.x, 19);
			} else {
				headSnakePos.changeValues(headSnakePos.x, Math.abs(headSnakePos.y - 1) % 20);
			}
			positions.add(new jTuple(headSnakePos.x, headSnakePos.y));
			break;
		case 2:
			if (headSnakePos.x - 1 < 0) {
				headSnakePos.changeValues(19, headSnakePos.y);
			} else {
				headSnakePos.changeValues(Math.abs(headSnakePos.x - 1) % 20, headSnakePos.y);
			}
			positions.add(new jTuple(headSnakePos.x, headSnakePos.y));

			break;
		case 1:
			headSnakePos.changeValues(Math.abs(headSnakePos.x + 1) % 20, headSnakePos.y);
			positions.add(new jTuple(headSnakePos.x, headSnakePos.y));
			break;
		}
	}

	private void moveExterne() {
		for (jTuple t : positions) {
			int y = t.getX();
			int x = t.getY();
			Squares.get(x).get(y).lightColorUp(0);

		}
	}

	private void subtractTail() {
		int cmpt = sizeSnake;
		for (int i = positions.size() - 1; i >= 0; i--) {
			if (cmpt == 0) {
				jTuple t = positions.get(i);
				Squares.get(t.y).get(t.x).lightColorUp(2);
			} else {
				cmpt--;
			}
		}
		cmpt = sizeSnake;
		for (int i = positions.size() - 1; i >= 0; i--) {
			if (cmpt == 0) {
				positions.remove(i);
			} else {
				cmpt--;
			}
		}
	}

	public void run() {
		while (true) {
			moveInterne(directionSnake);
			checkCollision();
			moveExterne();
			subtractTail();
			pauser();
		}
	}
}