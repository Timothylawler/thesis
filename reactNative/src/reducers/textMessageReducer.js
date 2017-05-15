const initialState = {}

export default function TextMessageReducer(state = initialState, action) {
	switch (action.type) {
		case "SETMESSAGE":
			return Object.assign({}, state, {
				message: action.message
			});
			
		default:
			return state;
	}
};