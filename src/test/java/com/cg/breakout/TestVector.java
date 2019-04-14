package com.cg.breakout;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestVector {
	@Test
	void rotation(){
		Vector2D v1 = new Vector2D(1.0, 0.0);

		v1.rotateBy(Math.toRadians(90));
		System.out.println(v1);
	}
}
