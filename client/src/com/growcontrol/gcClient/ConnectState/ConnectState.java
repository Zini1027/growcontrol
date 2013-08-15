package com.growcontrol.gcClient.ConnectState;


public abstract class ConnectState {

	public static enum State {
		CLOSED,
		CONNECTING,
		CONNECTED,
		READY
	};
	protected State state = null;


	public ConnectState() {}
	public ConnectState(State state) {
		if(state == null)
			this.state = null;
		else
			setConnectState(state);
	}


	// connection state
	public State getConnectionState() {
		synchronized(state) {
			if(state == null)
				return State.CLOSED;
			return state;
		}
	}
	public void setConnectState(State state) {
		if(state == null) throw new NullPointerException("state can't be null!");
		synchronized(state) {
			// no change
			if(this.state != null)
				if(this.state.equals(state))
					return;
			State lastState = this.state;
			this.state = state;
			// do change
			doChangedState(lastState);
		}
	}
	protected abstract void doChangedState(State lastState);


}
