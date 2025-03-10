package com.me.stars;
import java.applet.*;
import java.awt.*;

public class BallbewegungDoppel extends Applet implements Runnable
{
	// Initialisierung der Variablen
	int x_pos = 10;		// x - Position des Balles
	int y_pos = 100;	// y - Position des Balles
	int radius = 20;	// Radius des Balles

	// Variablen f�r die Doppelpufferung
	private Image dbImage;
	private Graphics dbg;

	public void init()
	{
		setBackground (Color.blue);
	}

	public void start ()
	{
		// Schaffen eines neuen Threads, in dem das Spiel l�uft
		Thread th = new Thread (this);
		// Starten des Threads
		th.start ();
	}

	public void stop()
	{

	}

	public void destroy()
	{

	}

	public void run ()
	{
		// Erniedrigen der ThreadPriority um zeichnen zu erleichtern
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		// Solange true ist l�uft der Thread weiter
		while (true)
		{
			// Ver�ndern der x- Koordinate
			x_pos ++;

			// Neuzeichnen des Applets
			repaint();

			try
			{
				// Stoppen des Threads f�r in Klammern angegebene Millisekunden
				Thread.sleep (20);
			}
			catch (InterruptedException ex)
			{
				// do nothing
			}

			// Zur�cksetzen der ThreadPriority auf Maximalwert
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
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

		// Bildschirm im Hintergrund l�schen
		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

		// Auf gel�schten Hintergrund Vordergrund zeichnen
		dbg.setColor (getForeground());
		paint (dbg);

		// Nun fertig gezeichnetes Bild Offscreen auf dem richtigen Bildschirm anzeigen
		g.drawImage (dbImage, 0, 0, this);
	}

	public void paint (Graphics g)
	{
		g.setColor  (Color.red);

		g.fillOval (x_pos - radius, y_pos - radius, 2 * radius, 2 * radius);
	}
}
