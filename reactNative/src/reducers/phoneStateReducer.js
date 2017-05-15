const initialState = {}

export default function PhoneStateReducer(state = initialState, action) {
	switch (action.type) {
		case "SETPHONESTATE":
			return Object.assign({}, state, {
				phoneState: action.phoneState
			});
			
		default:
			return state;
	}
};