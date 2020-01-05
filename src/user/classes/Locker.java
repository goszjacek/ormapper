package user.classes;

public class Locker {
	private Integer lockerId;
	private Student owner;
	public Integer getLockerId() {
		return lockerId;
	}
	public void setLockerId(Integer lockerId) {
		this.lockerId = lockerId;
	}
	public Student getOwner() {
		return owner;
	}
	public void setOwner(Student owner) {
		this.owner = owner;
	}
	
}
