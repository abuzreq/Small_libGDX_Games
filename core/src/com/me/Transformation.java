/**
 *  Desc:   Functions for converting 2D vectors between World and Local
 *          space.
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.me;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class Transformation {
//--------------------------- WorldTransform -----------------------------
//
//  given a std::vector of 2D vectors, a position, orientation and scale,
//  this function transforms the 2D vectors into the object's world space
//------------------------------------------------------------------------
    public static List<Vector2> WorldTransform(List<Vector2> points,
            Vector2 pos,
            Vector2 forward,
            Vector2 side,
            Vector2 scale) {
        //copy the original vertices into the buffer about to be transformed
        List<Vector2> TranVector2s = CppToJava.clone(points);

        //create a transformation matrix
        C2DMatrix matTransform = new C2DMatrix();

        //scale
        if ((scale.x != 1.0) || (scale.y != 1.0)) {
            matTransform.Scale(scale.x, scale.y);
        }

        //rotate
        matTransform.Rotate(forward, side);

        //and translate
        matTransform.Translate(pos.x, pos.y);

        //now transform the object's vertices
        matTransform.TransformVector2s(TranVector2s);

        return TranVector2s;
    }

/**
 *  given a std::vector of 2D vectors, a position and  orientation
 *  this function transforms the 2D vectors into the object's world space
 */
    public static List<Vector2> WorldTransform(List<Vector2> points,
            Vector2 pos,
            Vector2 forward,
            Vector2 side) {
        //copy the original vertices into the buffer about to be transformed
        List<Vector2> TranVector2s = CppToJava.clone(points);
        for(Vector2 v: points) {
            TranVector2s.add(v);
        }

        //create a transformation matrix
        C2DMatrix matTransform = new C2DMatrix();

        //rotate
        matTransform.Rotate(forward, side);

        //and translate
        matTransform.Translate(pos.x, pos.y);

        //now transform the object's vertices
        matTransform.TransformVector2s(TranVector2s);

        return TranVector2s;
    }

//--------------------- PointToWorldSpace --------------------------------
//
//  Transforms a point from the agent's local space into world space
//------------------------------------------------------------------------
    public static Vector2 PointToWorldSpace(Vector2 point,
            Vector2 AgentHeading,
            Vector2 AgentSide,
            Vector2 AgentPosition) {
        //make a copy of the point
        Vector2 TransPoint = new Vector2(point);

        //create a transformation matrix
        C2DMatrix matTransform = new C2DMatrix();

        //rotate
        matTransform.Rotate(AgentHeading, AgentSide);

        //and translate
        matTransform.Translate(AgentPosition.x, AgentPosition.y);

        //now transform the vertices
        matTransform.TransformVector2s(TransPoint);

        return TransPoint;
    }

//--------------------- VectorToWorldSpace --------------------------------
//
//  Transforms a vector from the agent's local space into world space
//------------------------------------------------------------------------
    public static Vector2 VectorToWorldSpace(Vector2 vec,
            Vector2 AgentHeading,
            Vector2 AgentSide) {
        //make a copy of the point
        Vector2 TransVec = new Vector2(vec);

        //create a transformation matrix
        C2DMatrix matTransform = new C2DMatrix();

        //rotate
        matTransform.Rotate(AgentHeading, AgentSide);

        //now transform the vertices
        matTransform.TransformVector2s(TransVec);

        return TransVec;
    }

//--------------------- PointToLocalSpace --------------------------------
//
//------------------------------------------------------------------------
    public static Vector2 PointToLocalSpace(Vector2 point,
            Vector2 AgentHeading,
            Vector2 AgentSide,
            Vector2 AgentPosition) {

        //make a copy of the point
        Vector2 TransPoint = new Vector2(point);

        //create a transformation matrix
        C2DMatrix matTransform = new C2DMatrix();

        double Tx = -AgentPosition.dot(AgentHeading);
        double Ty = -AgentPosition.dot(AgentSide);

        //create the transformation matrix
        matTransform._11(AgentHeading.x);   matTransform._12(AgentSide.x);
        matTransform._21(AgentHeading.y);   matTransform._22(AgentSide.y);
        matTransform._31(Tx);               matTransform._32(Ty);

        //now transform the vertices
        matTransform.TransformVector2s(TransPoint);

        return TransPoint;
    }

//--------------------- VectorToLocalSpace --------------------------------
//
//------------------------------------------------------------------------
    public static Vector2 VectorToLocalSpace(Vector2 vec,
            Vector2 AgentHeading,
            Vector2 AgentSide) {

        //make a copy of the point
        Vector2 TransPoint = new Vector2(vec);

        //create a transformation matrix
        C2DMatrix matTransform = new C2DMatrix();

        //create the transformation matrix
        matTransform._11(AgentHeading.x);   matTransform._12(AgentSide.x);
        matTransform._21(AgentHeading.y);   matTransform._22(AgentSide.y);

        //now transform the vertices
        matTransform.TransformVector2s(TransPoint);

        return TransPoint;
    }

//-------------------------- Vec2DRotateAroundOrigin --------------------------
//
//  rotates a vector ang rads around the origin
//-----------------------------------------------------------------------------
    public static void Vec2DRotateAroundOrigin(Vector2 v, double ang) {
        //create a transformation matrix
        C2DMatrix mat = new C2DMatrix();

        //rotate
        mat.Rotate(ang);

        //now transform the object's vertices
        mat.TransformVector2s(v);
    }

//------------------------ CreateWhiskers ------------------------------------
//
//  given an origin, a facing direction, a 'field of view' describing the 
//  limit of the outer whiskers, a whisker length and the number of whiskers
//  this method returns a vector containing the end positions of a series
//  of whiskers radiating away from the origin and with equal distance between
//  them. (like the spokes of a wheel clipped to a specific segment size)
//----------------------------------------------------------------------------
    public static List<Vector2> CreateWhiskers(int NumWhiskers,
            double WhiskerLength,
            double fov,
            Vector2 facing,
            Vector2 origin) {
        //this is the magnitude of the angle separating each whisker
        double SectorSize = fov / (double) (NumWhiskers - 1);

        List<Vector2> whiskers = new ArrayList<Vector2>(NumWhiskers);
        Vector2 temp;
        double angle = -fov * 0.5;

        for (int w = 0; w < NumWhiskers; ++w) {
            //create the whisker extending outwards at this angle
            temp = facing;
            Vec2DRotateAroundOrigin(temp, angle);
            whiskers.add(origin.add(temp.scl((float) WhiskerLength)));

            angle += SectorSize;
        }

        return whiskers;
    }
}