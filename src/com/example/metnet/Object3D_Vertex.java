package com.example.metnet;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

public class Object3D_Vertex {
	
	public static Object3D getVertex(float scale)
	{
		Object3D vertex=Primitives.getSphere(scale);
		vertex.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		return vertex;
	}
}
