package com.me.clothsimulation;

import java.util.Random;


//Body
//Here we construct and store a ragdoll
public class Body {
	 /*
    O
   /|\
  / | \
   / \
  |   |
 */
	
	
	
	Random random = new Random();
 PointMass head;
 PointMass shoulder;
 PointMass elbowLeft;
 PointMass elbowRight;
 PointMass handLeft;
 PointMass handRight;
 PointMass pelvis;
 PointMass kneeLeft;
 PointMass kneeRight;
 PointMass footLeft;
 PointMass footRight;
 Circle headCircle;
 
 float headWidth;
 float headLength;
 
 public int random()
 {
	 return random.nextInt(10)-5 ;
 }
 Body (float x, float y, float bodyHeight) {
   headLength = bodyHeight / 7.5f;
   headWidth = headLength * 3/4;
   head = new PointMass(x + random() ,y + random());
   head.mass = 4;
   shoulder = new PointMass(x + random(),y + random());
   shoulder.mass = 26; // shoulder to torso
   head.attachTo(shoulder, 5/4 * headLength, 1, bodyHeight*2, true);
   
   elbowLeft = new PointMass(x + random(),y + random());
   elbowRight = new PointMass(x + random(),y + random());
   elbowLeft.mass = 2; // upper arm mass
   elbowRight.mass = 2; 
   elbowLeft.attachTo(shoulder, headLength*3/2, 1, bodyHeight*2, true);
   elbowRight.attachTo(shoulder, headLength*3/2, 1, bodyHeight*2, true);
   
   handLeft = new PointMass(x + random(),y + random());
   handRight = new PointMass(x + random(),y + random());
   handLeft.mass = 2;
   handRight.mass = 2;
   handLeft.attachTo(elbowLeft, headLength*2, 1, bodyHeight*2, true);
   handRight.attachTo(elbowRight, headLength*2, 1, bodyHeight*2, true);
   
   pelvis = new PointMass(x + random(),y + random());
   pelvis.mass = 15; // pelvis to lower torso
   pelvis.attachTo(shoulder,headLength*3.5f,0.8f,bodyHeight*2, true);
   // this restraint keeps the head from tilting in extremely uncomfortable positions
   pelvis.attachTo(head, headLength*4.75f, 0.02f, bodyHeight*2, false);
   
   kneeLeft = new PointMass(x + random(),y + random());
   kneeRight = new PointMass(x + random(),y + random());
   kneeLeft.mass = 10;
   kneeRight.mass = 10;
   kneeLeft.attachTo(pelvis, headLength*2, 1, bodyHeight*2, true);
   kneeRight.attachTo(pelvis, headLength*2, 1, bodyHeight*2, true);
   
   footLeft = new PointMass(x + random(),y + random());
   footRight = new PointMass(x + random(),y + random());
   footLeft.mass = 5; // calf + foot
   footRight.mass = 5;
   footLeft.attachTo(kneeLeft, headLength*2, 1, bodyHeight*2, true);
   footRight.attachTo(kneeRight, headLength*2, 1, bodyHeight*2, true);
   
   // these constraints resist flexing the legs too far up towards the body
   footLeft.attachTo(shoulder, headLength*7.5f, 0.001f, bodyHeight*2, false);
   footRight.attachTo(shoulder, headLength*7.5f, 0.001f, bodyHeight*2, false);
   
   headCircle = new Circle(headLength*0.75f);
   headCircle.attachToPointMass(head);
   
   Verlet.physics.addCircle(headCircle);
   Verlet.addPointMass(head);
   Verlet.addPointMass(shoulder);
   Verlet.addPointMass(pelvis);
   Verlet.addPointMass(elbowLeft);
   Verlet.addPointMass(elbowRight);
   Verlet.addPointMass(handLeft);
   Verlet.addPointMass(handRight);
   Verlet.addPointMass(kneeLeft);
   Verlet.addPointMass(kneeRight);
   Verlet.addPointMass(footLeft);
   Verlet.addPointMass(footRight);
 }
 public void removeFromWorld () {
   Verlet.physics.removeCircle(headCircle);
   Verlet.removePointMass(head);
   Verlet.removePointMass(shoulder);
   Verlet.removePointMass(pelvis);
   Verlet.removePointMass(elbowLeft);
   Verlet.removePointMass(elbowRight);
   Verlet.removePointMass(handLeft);
   Verlet.removePointMass(handRight);
   Verlet.removePointMass(kneeLeft);
   Verlet.removePointMass(kneeRight);
   Verlet.removePointMass(footLeft);
   Verlet.removePointMass(footRight);
 }
}
