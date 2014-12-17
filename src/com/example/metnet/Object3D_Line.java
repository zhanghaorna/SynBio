package com.example.metnet;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;

public class Object3D_Line {

	// note: you will need to define a texture ahead of time. For a solid-color
	// line, you could use something like:
	// TextureManager.getInstance().addTexture( "Red", new Texture( 8, 8,
	// java.awt.Color.Red ) );

	public static Object3D getLine(SimpleVector pointA, SimpleVector pointB,
			float width) {
		Object3D line = new Object3D(8);
		float offset = width / 2.0f;

		// Quad A:
		line.addTriangle(
				new SimpleVector(pointA.x, pointA.y - offset, pointA.z), 0, 0,
				new SimpleVector(pointA.x, pointA.y + offset, pointA.z), 0, 1,
				new SimpleVector(pointB.x, pointB.y + offset, pointB.z), 1, 1);
		line.addTriangle(
				new SimpleVector(pointB.x, pointB.y + offset, pointB.z), 0, 0,
				new SimpleVector(pointB.x, pointB.y - offset, pointB.z), 0, 1,
				new SimpleVector(pointA.x, pointA.y - offset, pointA.z), 1, 1);
		// Quad A, back-face:
		line.addTriangle(
				new SimpleVector(pointB.x, pointB.y - offset, pointB.z), 0, 0,
				new SimpleVector(pointB.x, pointB.y + offset, pointB.z), 0, 1,
				new SimpleVector(pointA.x, pointA.y + offset, pointA.z), 1, 1);
		line.addTriangle(
				new SimpleVector(pointA.x, pointA.y + offset, pointA.z), 0, 0,
				new SimpleVector(pointA.x, pointA.y - offset, pointA.z), 0, 1,
				new SimpleVector(pointB.x, pointB.y - offset, pointB.z), 1, 1);
		// Quad B:
		line.addTriangle(
				new SimpleVector(pointA.x, pointA.y, pointA.z + offset), 0, 0,
				new SimpleVector(pointA.x, pointA.y, pointA.z - offset), 0, 1,
				new SimpleVector(pointB.x, pointB.y, pointB.z - offset), 1, 1);
		line.addTriangle(
				new SimpleVector(pointB.x, pointB.y, pointB.z - offset), 0, 0,
				new SimpleVector(pointB.x, pointB.y, pointB.z + offset), 0, 1,
				new SimpleVector(pointA.x, pointA.y, pointA.z + offset), 1, 1);
		// Quad B, back-face:
		line.addTriangle(
				new SimpleVector(pointB.x, pointB.y, pointB.z + offset), 0, 0,
				new SimpleVector(pointB.x, pointB.y, pointB.z - offset), 0, 1,
				new SimpleVector(pointA.x, pointA.y, pointA.z - offset), 1, 1);
		line.addTriangle(
				new SimpleVector(pointA.x, pointA.y, pointA.z - offset), 0, 0,
				new SimpleVector(pointA.x, pointA.y, pointA.z + offset), 0, 1,
				new SimpleVector(pointB.x, pointB.y, pointB.z + offset), 1, 1);

		// If you don't want the line to react to lighting:
		line.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		line.setLighting(Object3D.LIGHTING_NO_LIGHTS);
		line.setAdditionalColor(RGBColor.WHITE);
		// done
		return line;
	}

}
