package com.me.stars;

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.net.*;

public class Main extends Applet implements Runnable
{
	// Thread
	Thread th;

	// Deklaration der Variablen
	private int speed;			// Reguliert die Geschwindigkeit des Threads

	// RandomVariable
	Random rnd = new Random();

	// Object für die Zufallslandschaft
	Landscape land;

	// Objekt zur Erzeugung des SternenHimmels
	Stars stars;

	// Variablen für die Doppelpufferung
	private Image dbImage;
	private Graphics dbg;

	public void init ()
	{
		// Initialisierung der Variablen
		speed = 20;

		// Hintergrundfarbe
		setBackground (Color.black);

		// Initialisierung der Objekte
		land = new Landscape ();
		stars = new Stars();

		repaint();
	}

	public void start ()
	{
		// Schaffen eines neuen Threads, in dem das Spiel läuft
		th = new Thread (this);
		th.start ();
	}

	public void stop()
	{
		th.stop();
	}

	public void run ()
	{
		// Erniedrigen der ThreadPriority um zeichnen zu erleichtern
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		while (true)
		{
			repaint();

			try
			{
				// Stoppen des Threads für in speed angegebene Millisekunden
				Thread.sleep (speed);
			}
			catch (InterruptedException ex)
			{
				break;
			}

			// Zurücksetzen der ThreadPriority auf Maximalwert
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	public boolean mouseDown (Event e, int x, int y)
	{
		land = new Landscape();

		return true;
	}

	public void paint (Graphics g)
	{
		g.setColor (Color.yellow);
		g.drawString ("Click on applet to generate new landscape", 100, 50);

		// Zeichnet Sterne
		stars.paintStars(g);

		// Zeichnen der Landschaft
		land.paintMap (g);
	}

 	/** Update - Methode, Realisierung der Doppelpufferung zur Reduzierung des Bildschirmflackerns */
	public void update (Graphics g)
	{
		// Initialisierung des DoubleBuffers
		if (dbImage == null)
		{
			dbImage = createImage (this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics ();
		}

		// Bildschirm im Hintergrund löschen
		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

		// Auf gelöschten Hintergrund Vordergrund zeichnen
		dbg.setColor (getForeground());
		paint (dbg);

		// Nun fertig gezeichnetes Bild Offscreen auf dem richtigen Bildschirm anzeigen
		g.drawImage (dbImage, 0, 0, this);
	}
}













