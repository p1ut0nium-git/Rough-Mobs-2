package com.p1ut0nium.roughmobsrevamped.util.handlers;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class SpamFilter implements Filter {

	@Override
	public boolean isLoggable(LogRecord arg0) {
		return !arg0.getMessage().contains("Fetching addPacket for removed entity");
	}
}
