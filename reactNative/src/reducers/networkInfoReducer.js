const initialState = {}

export default function NetworkInfoReducer(state = initialState, action) {
	switch (action.type) {
		case "SETNETWORKINFO":
			return Object.assign({}, state, {
				network: action.info
			});
			
		default:
			return state;
	}
};