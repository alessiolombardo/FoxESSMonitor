package model;

public class ResponseToken {

	public class Result {

		private String token;

		private int access;

		private String user;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public int getAccess() {
			return access;
		}

		public void setAccess(int access) {
			this.access = access;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		@Override
		public String toString() {
			return "Result [token=" + token + ", access=" + access + ", user=" + user + "]";
		}

	}

	private int errno;

	private Result result;

	public int getErrno() {
		return errno;
	}

	public void setErrno(int errno) {
		this.errno = errno;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Response [errno=" + errno + ", result=" + result + "]";
	}

}
