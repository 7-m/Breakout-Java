package com.cg.breakout;

import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.Test;

public class TestRendering {
	@Test
	void events() throws InterruptedException {
		EventBus ev = new EventBus();
		GameContext gtx = new GameContext(null, null, null);

		IOThread rt = new IOThread(ev, gtx);
		rt.start();

		ev.post(new DrawEvent());
	}
}
