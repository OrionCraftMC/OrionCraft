package io.github.orioncraftmc.orion.api.event;

import io.github.orioncraftmc.orion.api.event.render.HudRenderEvent;

public class TestThing extends EventHandlerDelegate {
	@Override
	public void call(Event event) {
		Handler.handleIt((HudRenderEvent) event);
	}
}

class Handler {
	static void handleIt(HudRenderEvent event) {

	}
}
