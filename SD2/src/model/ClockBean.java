/**
 * Raul Barbosa 2014-11-07
 */
package clock.model;

import java.util.Date;

public class ClockBean {
	private Date date;
	
	public ClockBean() {
		setDate(new Date());
	}

	public String getDateAndTime() {
		return date.toString();
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
}
