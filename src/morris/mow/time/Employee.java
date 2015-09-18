package morris.mow.time;

public class Employee {
	private String name;
	private float rate;
	private int rowID;
	public float start;
	public float stop;
	public String datePaid;
	public String paid;

	public Employee() {

	}

	public Employee(String name, float rate, int rowID) {
		this.name = name;
		this.rate = rate;
		this.rowID = rowID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getRowID() {
		return rowID;
	}

	public void setRowID(int rowID) {
		this.rowID = rowID;
	}

	public float getStart() {
		return start;
	}

	public void setStart(float start) {
		this.start = start;
	}

	public float getStop() {
		return stop;
	}

	public void setStop(float stop) {
		this.stop = stop;
	}

	public String getDatePaid() {
		return datePaid;
	}

	public void setDatePaid(String datePaid) {
		this.datePaid = datePaid;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public void setState(boolean checked) {
		// TODO Auto-generated method stub
	}

	public boolean getState() {
		return false;

	}

}