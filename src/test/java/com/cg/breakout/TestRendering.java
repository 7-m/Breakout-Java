package com.cg.breakout;

import com.cg.breakout.events.DrawEvent;
import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.Test;

public class TestRendering {
	@Test
	void events() throws InterruptedException {
		EventBus ev = new EventBus();
		GameContext gtx = new GameContext(null, null, null,0);

		IOThread rt = new IOThread(ev);
		rt.start();

		ev.post(new DrawEvent(gtx));
	}
}
