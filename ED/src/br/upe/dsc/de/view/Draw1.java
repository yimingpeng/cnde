package br.upe.dsc.de.view;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;

class Draw1 extends JFrame // create frame for canvas
{
	public Draw1() // constructor
	{
		super("Draw1");
		setBounds(10, 10, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con = this.getContentPane();
		con.setBackground(Color.white);
		GCanvas canvas = new GCanvas();
		con.add(canvas);
		setVisible(true);
	}

	public static void main(String arg[]) {
		new Draw1();
	}
}