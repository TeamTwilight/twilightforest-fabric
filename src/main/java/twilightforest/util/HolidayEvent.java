package twilightforest.util;

import twilightforest.util.TFBeanRegistry;

import java.time.Month;
import java.time.MonthDay;

public class HolidayEvent {

	public static final HolidayEvent INSTANCE = new HolidayEvent();

	static {
		TFBeanRegistry.register(HolidayEvent.class, INSTANCE);
	}

	public boolean isHalloweenWeek() {
		MonthDay now = MonthDay.now();
		return now.isAfter(MonthDay.of(Month.OCTOBER, 19)) && now.isBefore(MonthDay.of(Month.NOVEMBER, 4));
	}

}
