package com.cg.breakout;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEntity {

	@Test
	 void collisionDetection(){
		Entity o1 = new Entity(10, 20);
		Entity o2 = new Entity(4, 4);

		assertTrue(o1.collidesWith(o2));

		o2.setX(10);
		assertTrue(o1.collidesWith(o2));


	}
}
