package com.me.stars;
import java.awt.*;

import java.util.*;

public class Stars
{
	// Deklaration der Variablen
	private int numStars;			// Anzahl der Sterne
	private int radius;				// Radius der Sterne
	private int mapwidth;			// Breite der Landschaft
	private int starzone;			// Zone, in der Sterne in y - Richtung gezeichnet werden können

	// Deklaration der beiden Arrays zur Speicherung der x bzw. y - Position der Sterne
	private int [] x_array;
	private int [] y_array;

	// Objekt zur Erzeugung von Zufallszahlen
	Random rnd = new Random();

	/** Construktor */
	public Stars()
	{
		// Initialisierung der Variablen
		numStars = 70;
		radius = 2;
		mapwidth = 700;
		starzone = 300;

		// Initialisierung der Sternenarrays
		generateStars();
	}

	/** Funktion zur Erstellung eines Sternenhimmels und Speicherung der Koordinaten in den
	beiden Arrays x_array und y_array */

	private void generateStars()
	{
		// Initialisierung der Arrays
		x_array = new int [numStars];
		y_array = new int [numStars];

		// Schleife zu Initialisierung der einzelnen Arrayfelder
		for (int index = 0; index < numStars; index ++)
		{
			x_array [index] = Math.abs(rnd.nextInt() % mapwidth);
			y_array [index] = Math.abs(rnd.nextInt() % starzone);
		}
	}

	/** Diese Methode zeichnet die Sterne */

	public void paintStars (Graphics g)
	{
		g.setColor (Color.white);

		for (int index = 0; index < numStars; index ++)
		{
			g.fillOval (x_array [index], y_array [index], radius, radius);
		}
	}
}
