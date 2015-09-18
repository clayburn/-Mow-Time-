package morris.mow.time;

public class Lawn {
	private String nameLawn;
	private String location;
	private long toBeCut;
	private long lastCut;
	private Float price;
	private long avgCutTime;
	private int cutFrequency;
	private int rowID;
	private long tempTime;
	private String accomplished;
	private long timeSpentCutting;
	private long pauseTime;
	private boolean paused;
	private String phone_number;

	public Lawn() {
	}

	public Lawn(String name) {
		this.nameLawn = name;
	}

	public Lawn(String name, String phone) {
		this.nameLawn = name;
		this.phone_number = phone;
	}

	public String getNameLawn() {
		return nameLawn;
	}

	public void setNameLawn(String nameLawn) {
		this.nameLawn = nameLawn;
	}

	public long getToBeCut() {
		return toBeCut;
	}

	public void setToBeCut(long toBeCut) {
		this.toBeCut = toBeCut;
	}

	public long getLastCut() {
		return lastCut;
	}

	public void setLastCut(long lastCut) {
		this.lastCut = lastCut;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public long getAvgCutTime() {
		return avgCutTime;
	}

	public void setAvgCutTime(long avgCutTime) {
		this.avgCutTime = avgCutTime;
	}

	public long getCutFrequency() {
		return cutFrequency;
	}

	public void setCutFrequency(int cutFrequency) {
		this.cutFrequency = cutFrequency;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setRowID(int rowID) {
		this.rowID = rowID;
	}

	public int getRowID() {
		return rowID;
	}

	public long getTimeSpentCutting() {
		return timeSpentCutting;
	}

	public void setTimeSpentCutting(long timeSpendtCutting) {
		this.timeSpentCutting = timeSpendtCutting;
	}

	public long getTempTime() {
		return tempTime;
	}

	public void setTempTime(long tempTime) {
		this.tempTime = tempTime;
	}

	public String getAccomplished() {
		return accomplished;
	}

	public void setAccomplished(String accomplished) {
		this.accomplished = accomplished;
	}

	public long getPauseTime() {
		return pauseTime;
	}

	public void setPauseTime(long pauseTime) {
		this.pauseTime = pauseTime;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean started) {
		this.paused = started;
	}

	public String getPhoneNumber() {
		return phone_number;
	}

	public void setPhoneNumber(String phone_number) {
		this.phone_number = phone_number;
	}
}
